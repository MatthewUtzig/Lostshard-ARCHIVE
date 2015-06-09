package com.lostshard.lostshard.Spells.Spells;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Teleport extends RangedSpell {

	public SPL_Teleport(Scroll scroll) {
		super(scroll);
		this.setRange(25);
	}

	/*
	 * The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	@Override
	public void doAction(Player player) {
		final Location teleportTo = new Location(player.getWorld(), this
				.getFoundBlock().getX() + .5, (double) this.getFoundBlock()
				.getY() + 1, this.getFoundBlock().getZ() + .5);
		teleportTo.setPitch(player.getLocation().getPitch());
		teleportTo.setYaw(player.getLocation().getYaw());
		player.teleport(teleportTo);
	}

	private boolean isRoom(Block block) {
		if (!SpellUtils.invisibleBlocks.contains(block.getRelative(0, 1, 0)
				.getType())
				|| !SpellUtils.invisibleBlocks.contains(block.getRelative(0, 2,
						0).getType()))
			return false;
		return true;
	}

	/*
	 * Used for anything that must be handled as soon as the spell is cast, for
	 * example targeting a location for a delayed spell.
	 */
	@Override
	public void preAction(Player player) {

	}

	@Override
	public boolean verifyCastable(Player player) {
		final Block blockAt = player.getLocation().getBlock();
		if (!blockAt.getType().equals(Material.IRON_DOOR_BLOCK)) {
			final List<Block> lastTwoBlocks = player.getLastTwoTargetBlocks(
					SpellUtils.invisibleBlocks, this.getRange());

			if (lastTwoBlocks.size() < 2) {
				Output.simpleError(player, "Invalid target.");
				return false;
			}

			boolean ceiling = false;
			final Block blockAboveFace = lastTwoBlocks.get(0).getRelative(0, 1,
					0);
			if (!SpellUtils.invisibleBlocks.contains(blockAboveFace.getType())
					&& SpellUtils.invisibleBlocks.contains(blockAboveFace
							.getRelative(0, -1, 0).getType())) {
				this.setFoundBlock(blockAboveFace.getRelative(0, -3, 0));
				ceiling = true;
			}
			boolean wall = false;
			if (!ceiling)
				if (!this.isRoom(lastTwoBlocks.get(1))) {
					wall = true;
					this.setFoundBlock(lastTwoBlocks.get(0));
				}

			if (!ceiling && !wall)
				this.setFoundBlock(SpellUtils.blockInLOS(player,
						this.getRange()));

			if (this.getFoundBlock() == null) {
				Output.simpleError(player, "That location is too far away.");
				return false;
			}

			if (!SpellUtils.invisibleBlocks.contains(this.getFoundBlock()
					.getRelative(0, 1, 0).getType())
					&& !SpellUtils.invisibleBlocks.contains(this
							.getFoundBlock().getRelative(0, 2, 0))) {
				Output.simpleError(player,
						"There is not enough room to teleport there.");
				return false;
			}

			// check for lapis
			for (int x = this.getFoundBlock().getX() - 3; x <= this
					.getFoundBlock().getX() + 3; x++)
				for (int y = this.getFoundBlock().getY() - 3; y <= this
						.getFoundBlock().getY() + 3; y++)
					for (int z = this.getFoundBlock().getZ() - 3; z <= this
							.getFoundBlock().getZ() + 3; z++)
						if (this.getFoundBlock().getWorld().getBlockAt(x, y, z)
								.getType().equals(Material.LAPIS_BLOCK)) {
							Output.simpleError(player,
									"can't teleport to a location near Lapis Lazuli blocks.");
							return false;
						}
			return true;
		} else
			Output.simpleError(player, "can't teleport from an iron door.");
		return false;
	}

}
