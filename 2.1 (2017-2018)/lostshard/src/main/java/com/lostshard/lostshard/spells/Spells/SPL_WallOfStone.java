package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Structures.WallOfStone;

public class SPL_WallOfStone extends RangedSpell {

	public SPL_WallOfStone(Scroll scroll) {
		super(scroll);
		this.setRange(20);
		this.setCarePlot(false);
	}

	@Override
	public void doAction(Player player) {
		final ArrayList<Block> stonewallBlocks = new ArrayList<Block>();
		// stonewall effect
		final Location originLoc = player.getLocation();
		originLoc.setY(originLoc.getY() + 1.5);
		final Location targetLoc = new Location(this.getFoundBlock().getWorld(), this.getFoundBlock().getX() + .5,
				this.getFoundBlock().getY() + .5, this.getFoundBlock().getZ() + .5);

		final double slopeX = targetLoc.getX() - originLoc.getX();
		final double slopeZ = targetLoc.getZ() - originLoc.getZ();
		double perpSlopeX = slopeZ;
		double perpSlopeZ = slopeX;

		final double length = Math.sqrt(perpSlopeX * perpSlopeX + perpSlopeZ * perpSlopeZ);
		perpSlopeX /= length;
		perpSlopeZ /= length;

		if (perpSlopeX > 0 && perpSlopeZ > 0)
			perpSlopeZ *= -1;
		else if (perpSlopeX < 0)
			perpSlopeX *= -1;
		else if (perpSlopeZ < 0)
			perpSlopeZ *= -1;

		for (int i = -4; i < 4; i++) {
			final Block b = this.getFoundBlock().getWorld().getBlockAt(
					(int) Math.floor(targetLoc.getX() + i * perpSlopeX), (int) Math.floor(targetLoc.getY()) + 1,
					(int) Math.floor(targetLoc.getZ() + i * perpSlopeZ));
			for (int z = 0; z < 3; z++) {
				final Block vert = this.getFoundBlock().getWorld().getBlockAt(b.getX(), b.getY() + z, b.getZ());
				if (vert.getType().equals(Material.AIR))
					// vert.setType(Material.STONE);
					stonewallBlocks.add(vert);
			}
		}

		new WallOfStone(stonewallBlocks, player.getUniqueId(), 150);
	}

	@Override
	public void preAction(Player player) {

	}

}
