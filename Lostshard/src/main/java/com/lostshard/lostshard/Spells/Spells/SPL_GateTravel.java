package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Spells.Structures.Gate;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_GateTravel extends Spell {

	public SPL_GateTravel() {
		super();
		setName("Gate Travel");
		setSpellWords("Gatius Teleportus");
		setCastingDelay(20);
		setCooldown(20);
		setManaCost(50);
		addReagentCost(new ItemStack(Material.STRING));
		addReagentCost(new ItemStack(Material.REDSTONE));
		setMinMagery(720);
		setPage(7);
		setPrompt("What rune would you like to gate travel to?");
	}
	
	public boolean verifyCastable(Player player) {
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Gate Travel...");
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		
		Runebook runebook = pseudoPlayer.getRunebook();
		ArrayList<Rune> runes = runebook.getRunes();
		Rune runeFound = null;
		
		int count = 0;
		for(Rune rune : runes) {
			if(!player.isOp() && !pseudoPlayer.wasSubscribed() && (count >= 8))
				break;
			if(rune.getLabel().equalsIgnoreCase(getResponse())) {
				runeFound = rune;
				break;
			}
			count++;
		}
		
		if(runeFound == null) {
			Output.simpleError(player, "You do not have a rune with that name, re-cast spell.");
			pseudoPlayer.getTimer().cantCastTicks = getCooldown();
			return;
		}
		
		Location runeLoc = runeFound.getLocation();
		Plot plot = ptm.findPlotAt(runeLoc);
		if((plot == null) || !plot.isPrivatePlot() || plot.isFriendOrAbove(player)) {
			
			if(!SpellUtils.isValidRuneLocation(player, player.getLocation())) {
				//Output.simpleError(player, "Your current location is blocked.");
				return;
			}
			
			if(!SpellUtils.isValidRuneLocation(player, runeLoc)) {
				//Output.simpleError(player, "That location is blocked.");
				return;
			}
			
			Location loc = player.getLocation();
				
			Block destBlock = runeLoc.getWorld().getBlockAt(runeLoc.getBlockX(), runeLoc.getBlockY(), runeLoc.getBlockZ());
			Block srcBlock = player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			Block extraDestBlock = runeLoc.getWorld().getBlockAt(runeLoc.getBlockX(), runeLoc.getBlockY()+1, runeLoc.getBlockZ());
			Block extraSrcBlock = player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ());
			ArrayList<Block> blocks = new ArrayList<Block>();
			blocks.add(srcBlock);
			blocks.add(destBlock);
			blocks.add(extraSrcBlock);
			blocks.add(extraDestBlock);
			
			//check for lapis below your target location
			if(destBlock.getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
			   destBlock.getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
				Output.simpleError(player, "Cannot gate to a Lapis Lazuli block.");
				return;
			}
			
			new Gate(blocks, player.getUniqueId(), 150);
		}
		else Output.simpleError(player, "Cannot gate to there, not a friend of the plot.");
	}

	
}
