package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Mark extends Spell {
	
	private Location markLoc = null;
	
	public Location getMarkLoc() {
		return markLoc;
	}

	public void setMarkLoc(Location markLoc) {
		this.markLoc = markLoc;
	}

	public SPL_Mark() {
		super();
		setName("Mark");
		setSpellWords("Runus Markius");
		setCastingDelay(10);
		setCooldown(10);
		setManaCost(10);
		setPrompt("What would you like to label the marked rune?");
		setPage(4);
		setMinMagery(360);
		addReagentCost(new ItemStack(Material.FEATHER));
		addReagentCost(new ItemStack(Material.REDSTONE));
	}
	
	public boolean verifyCastable(Player player) {
		Plot plot = ptm.findPlotAt(player.getLocation());
		if((plot == null) || !plot.isPrivatePlot() || plot.isFriendOrAbove(player)) {
			setMarkLoc(player.getLocation().getBlock().getLocation());
		}
		else {
			Output.simpleError(player, "You cannot mark a rune here, the plot is private.");
			return false;
		}
		
		if(!SpellUtils.isValidRuneLocation(player, player.getLocation())) {
			return false;
		}
		
		return true;
	}
	
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Mark...");
	}
	
	public void doAction(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if(getResponse().length() > 20 || getResponse().contains("\"") || getResponse().contains("'")) {
			Output.simpleError(player, "Invalid characters or too long, 20 char max.");
		}
		else {
			Runebook runebook = pseudoPlayer.getRunebook();
			int numRunes = runebook.getNumRunes();
			if(player.isOp() || (pseudoPlayer.wasSubscribed() && numRunes < 32) || numRunes < 8) {
				ArrayList<Rune> runes = runebook.getRunes();
				boolean foundMatching = false;
				for(Rune rune : runes) {
					if(rune.getLabel().equalsIgnoreCase(getResponse())) {
						foundMatching = true;
						break;
					}
				}
				if(!foundMatching) {
					int runeId = Database.insertRune(pseudoPlayer.getId(), getResponse(), markLoc);
					Rune newRune = new Rune(markLoc, getResponse(), runeId);
					runebook.addRune(newRune);
					Output.positiveMessage(player, "You have marked a rune for "+getResponse()+".");
				}
				else Output.simpleError(player, "You already have a rune with that name, re-cast the spell.");
			}
			else Output.simpleError(player, "Too many runes, remove one to mark a new rune.");
		}
		pseudoPlayer.getTimer().cantCastTicks = getCooldown();
	}
}
