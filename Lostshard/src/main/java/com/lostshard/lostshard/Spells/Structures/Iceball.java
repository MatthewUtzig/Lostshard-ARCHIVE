package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.lostshard.lostshard.Spells.MagicStructure;

public class Iceball extends MagicStructure {

	private boolean leaveSnow;

	public Iceball(ArrayList<Block> blocks, UUID uuid, int numTicksTillCleanup,
			boolean leaveSnow) {
		super(blocks, uuid, numTicksTillCleanup);
		this.setLeaveSnow(leaveSnow);
	}

	@Override
	public void cleanUp() {
		// go through each block,
		for (int i = 0; i < this.getBlockStates().size(); i++) {
			final BlockState b = this.getBlockStates().get(i);
			if (b.getBlock().getState().equals(b)) {
				final Material blockBelow = b.getWorld()
						.getBlockAt(b.getX(), b.getY() - 1, b.getZ()).getType();
				if (this.isLeaveSnow()) {
					if (blockBelow != Material.AIR
							&& blockBelow != Material.SNOW)
						b.getBlock().setType(Material.SNOW);
					else
						b.getBlock().setType(Material.AIR);
				} else
					b.getBlock().setType(Material.AIR);
			}
		}
		this.setDead(true);
	}

	public boolean isLeaveSnow() {
		return this.leaveSnow;
	}

	public void setLeaveSnow(boolean leaveSnow) {
		this.leaveSnow = leaveSnow;
	}

}
