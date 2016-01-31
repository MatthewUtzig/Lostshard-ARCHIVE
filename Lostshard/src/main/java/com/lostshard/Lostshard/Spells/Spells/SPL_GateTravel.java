package com.lostshard.Lostshard.Spells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.Rune;
import com.lostshard.Lostshard.Objects.Player.Runebook;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Spells.Structures.Gate;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;

public class SPL_GateTravel extends Spell {

	public SPL_GateTravel(Scroll scroll) {
		super(scroll);
		this.setPrompt("What rune would you like to gate travel to?");
	}

	/*
	 * The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	@Override
	public void doAction(Player player) {
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);

		final Runebook runebook = pseudoPlayer.getRunebook();
		final List<Rune> runes = runebook.getRunes();
		Rune runeFound = null;

		int count = 0;
		for (final Rune rune : runes) {
			if (!player.isOp() && !pseudoPlayer.wasSubscribed() && count >= 8)
				break;
			if (rune.getLabel().equalsIgnoreCase(this.getResponse())) {
				runeFound = rune;
				break;
			}
			count++;
		}

		if (runeFound == null) {
			Output.simpleError(player, "You do not have a rune with that name, re-cast spell.");
			return;
		}

		final Location runeLoc = runeFound.getLocation();
		final Plot plot = this.ptm.findPlotAt(runeLoc);
		if (plot == null || !plot.isPrivatePlot() || plot.isFriendOrAbove(player)) {

			if (!SpellUtils.isValidRuneLocation(player, player.getLocation()))
				// Output.simpleError(player,
				// "Your current location is blocked.");
				return;

			if (!SpellUtils.isValidRuneLocation(player, runeLoc))
				// Output.simpleError(player, "That location is blocked.");
				return;

			final Location loc = player.getLocation();

			final Block destBlock = runeLoc.getWorld().getBlockAt(runeLoc.getBlockX(), runeLoc.getBlockY(),
					runeLoc.getBlockZ());
			final Block srcBlock = player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
			final Block extraDestBlock = runeLoc.getWorld().getBlockAt(runeLoc.getBlockX(), runeLoc.getBlockY() + 1,
					runeLoc.getBlockZ());
			final Block extraSrcBlock = player.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 1,
					loc.getBlockZ());
			final ArrayList<Block> blocks = new ArrayList<Block>();
			blocks.add(srcBlock);
			blocks.add(destBlock);
			blocks.add(extraSrcBlock);
			blocks.add(extraDestBlock);

			// check for lapis below your target location
			if (destBlock.getRelative(0, -1, 0).getType().equals(Material.LAPIS_BLOCK)
					|| destBlock.getRelative(0, -2, 0).getType().equals(Material.LAPIS_BLOCK)) {
				Output.simpleError(player, "can't gate to a Lapis Lazuli block.");
				return;
			}

			if (destBlock.getRelative(0, 1, 0).getType().equals(Material.PORTAL)
					|| destBlock.getType().equals(Material.PORTAL)) {
				Output.simpleError(player, "can't gate to another gate.");
				return;
			}

			final int yaw = Math.round(Math.abs(player.getLocation().getYaw() / 90));
			boolean direction;
			if (yaw == 0 || yaw == 2)
				direction = true;
			else
				direction = false;

			new Gate(blocks, player.getUniqueId(), 150, direction);
		} else
			Output.simpleError(player, "can't gate to there, not a friend of the plot.");
	}

	/*
	 * Used for anything that must be handled as soon as the spell is cast, for
	 * example targeting a location for a delayed spell.
	 */
	@Override
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Gate Travel...");
	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

}
