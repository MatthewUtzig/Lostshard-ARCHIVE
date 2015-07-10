package com.lostshard.Lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Spells.RangedSpell;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Structures.Bridge;
import com.lostshard.Lostshard.Utils.Bresenham;
import com.lostshard.Lostshard.Utils.IntPoint;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;

public class SPL_Bridge extends RangedSpell {

	public SPL_Bridge(Scroll scroll) {
		super(scroll);
		this.setRange(20);
		this.setCarePlot(false);
	}

	@Override
	public void doAction(Player player) {
		final IntPoint srcPoint = new IntPoint(
				player.getLocation().getBlockX(), player.getLocation()
						.getBlockY() - 1, player.getLocation().getBlockZ());
		final IntPoint destPoint = new IntPoint(this.getFoundBlock().getX(),
				this.getFoundBlock().getY(), this.getFoundBlock().getZ());

		final ArrayList<Block> blocks = new ArrayList<Block>();

		final ArrayList<IntPoint> intPoints = Bresenham.bresenham3d(srcPoint.x,
				srcPoint.y, srcPoint.z, destPoint.x, destPoint.y, destPoint.z);
		for (final IntPoint intPoint : intPoints) {
			Block b = player.getWorld().getBlockAt(intPoint.x, intPoint.y,
					intPoint.z);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x + 1, intPoint.y,
					intPoint.z);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x - 1, intPoint.y,
					intPoint.z);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y,
					intPoint.z + 1);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y,
					intPoint.z - 1);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x + 1, intPoint.y,
					intPoint.z + 1);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x + 1, intPoint.y,
					intPoint.z - 1);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x - 1, intPoint.y,
					intPoint.z - 1);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
			b = player.getWorld().getBlockAt(intPoint.x - 1, intPoint.y,
					intPoint.z + 1);
			if (b.getType().equals(Material.AIR)
					|| b.getType().equals(Material.SNOW)
					|| b.getType().equals(Material.FIRE))
				blocks.add(b);
		}

		if (blocks.size() > 0)
			new Bridge(blocks, player.getUniqueId(), 150);
	}

	@Override
	public void preAction(Player player) {

	}

	@Override
	public boolean verifyCastable(Player player) {
		this.setFoundBlock(SpellUtils.blockInLOS(player, this.getRange()));
		if (this.getFoundBlock() == null) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}

		// check for lapis below you
		if (player.getLocation().getBlock().getRelative(0, -1, 0).getType()
				.equals(Material.LAPIS_BLOCK)
				|| player.getLocation().getBlock().getRelative(0, -2, 0)
						.getType().equals(Material.LAPIS_BLOCK)) {
			Output.simpleError(player,
					"can't bridge from a Lapis Lazuli block.");
			return false;
		}

		// check for lapis
		return SpellUtils.lapisCheck(this.getFoundBlock().getLocation(), player);
	}

}
