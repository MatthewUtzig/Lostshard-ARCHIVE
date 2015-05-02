package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Structures.FireField;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Firefield extends RangedSpell {

	public SPL_Firefield(Scroll scroll) {
		super(scroll);
		this.setCarePlot(false);
		this.setRange(20);
	}

	@Override
	public void doAction(Player player) {
		final ArrayList<Block> blocks = new ArrayList<Block>();
		for (int x = this.getFoundBlock().getX() - 2; x <= this.getFoundBlock()
				.getX() + 2; x++)
			for (int y = this.getFoundBlock().getY() - 2; y <= this
					.getFoundBlock().getY() + 2; y++)
				for (int z = this.getFoundBlock().getZ() - 2; z <= this
						.getFoundBlock().getZ() + 2; z++) {
					final Block blockAt = this.getFoundBlock().getWorld()
							.getBlockAt(x, y, z);
					if (!blockAt.getType().equals(Material.AIR)) {
						final Block blockAbove = this.getFoundBlock()
								.getWorld().getBlockAt(x, y + 1, z);
						if (blockAbove.getType().equals(Material.AIR)
								|| blockAbove.getType().equals(Material.SNOW))
							if (Utils.isWithin(blockAbove.getLocation(), this
									.getFoundBlock().getLocation(), 2))
								blocks.add(blockAbove);
					}
				}

		if (blocks.size() > 0)
			new FireField(blocks, player.getUniqueId(), 200);
	}

	@Override
	public void preAction(Player player) {

	}

}
