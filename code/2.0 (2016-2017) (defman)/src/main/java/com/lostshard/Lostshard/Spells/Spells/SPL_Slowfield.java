package com.lostshard.Lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Spells.RangedSpell;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Structures.WebTrap;
import com.lostshard.Lostshard.Utils.Utils;

public class SPL_Slowfield extends RangedSpell {

	public SPL_Slowfield(Scroll scroll) {
		super(scroll);
		this.setRange(20);
		this.setCarePlot(false);
	}

	/*
	 * The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	@Override
	public void doAction(Player player) {
		final ArrayList<Block> blocks = new ArrayList<Block>();
		for (int x = this.getFoundBlock().getX() - 3; x <= this.getFoundBlock().getX() + 2; x++)
			for (int y = this.getFoundBlock().getY() - 3; y <= this.getFoundBlock().getY() + 2; y++)
				for (int z = this.getFoundBlock().getZ() - 3; z <= this.getFoundBlock().getZ() + 2; z++) {
					final Block blockAt = this.getFoundBlock().getWorld().getBlockAt(x, y, z);
					if (!blockAt.getType().equals(Material.AIR)) {
						final Block blockAbove = this.getFoundBlock().getWorld().getBlockAt(x, y + 1, z);
						if (blockAbove.getType().equals(Material.AIR) || blockAbove.getType().equals(Material.SNOW))
							if (Utils.isWithin(blockAbove.getLocation(), this.getFoundBlock().getLocation(), 3))
								blocks.add(blockAbove);
					}
				}

		for (final Block block : blocks)
			block.setType(Material.WEB);

		if (blocks.size() > 0)
			new WebTrap(blocks, player.getUniqueId(), 50);
	}

	/*
	 * Used for anything that must be handled as soon as the spell is cast, for
	 * example targeting a location for a delayed spell.
	 */
	@Override
	public void preAction(Player player) {

	}

}
