package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Objects.Player.Rune;
import com.lostshard.lostshard.Objects.Player.Runebook;
import com.lostshard.lostshard.plot.Plot;
import com.lostshard.lostshard.plot.Plot.PlotToggleable;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Mark extends Spell {

	private Location markLoc = null;

	public SPL_Mark(Scroll scroll) {
		super(scroll);
		this.setPrompt("What would you like to label the marked rune?");
	}

	@Override
	public void doAction(Player player) {
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		if (this.getResponse().length() > 20 || this.getResponse().contains("\"") || this.getResponse().contains("'"))
			Output.simpleError(player, "Invalid characters or too long, 20 char max.");
		else {
			final Runebook runebook = pseudoPlayer.getRunebook();
			final int numRunes = runebook.size();
			if (player.isOp() || pseudoPlayer.wasSubscribed() && numRunes < 32 || numRunes < 8) {
				boolean foundMatching = false;
				for (final Rune rune : runebook)
					if (rune.getLabel().equalsIgnoreCase(this.getResponse())) {
						foundMatching = true;
						break;
					}
				if (!foundMatching) {
					final Rune newRune = new Rune(this.markLoc, this.getResponse(), 0);
					runebook.add(newRune);
					Output.positiveMessage(player, "You have marked a rune for " + this.getResponse() + ".");
				} else
					Output.simpleError(player, "You already have a rune with that name, re-cast the spell.");
			} else
				Output.simpleError(player, "Too many runes, remove one to mark a new rune.");
		}
	}

	public Location getMarkLoc() {
		return this.markLoc;
	}

	@Override
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Mark...");
	}

	@Override
	public void runebook(Player player) {

	}

	public void setMarkLoc(Location markLoc) {
		this.markLoc = markLoc;
	}

	@Override
	public boolean verifyCastable(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null || !plot.getToggleables().contains(PlotToggleable.PRIVATE) || plot.isFriendOrAbove(player))
			this.setMarkLoc(player.getLocation().getBlock().getLocation());
		else {
			Output.simpleError(player, "You can't mark a rune here, the plot is private.");
			return false;
		}

		if (!SpellUtils.isValidRuneLocation(player, player.getLocation()))
			return false;

		return true;
	}
}
