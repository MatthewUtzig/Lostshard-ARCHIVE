package com.lostshard.Lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.lostshard.Lostshard.Spells.MagicStructure;

public class WallOfStone extends MagicStructure {

	protected int buildTicks = 25;

	protected int solidTicks = 125;

	public WallOfStone(ArrayList<Block> blocks, UUID uuid, int numTicksTillCleanup) {
		super(blocks, uuid, numTicksTillCleanup);
	}

	@Override
	public void tick() {
		if (!this.isDead()) {
			this.setCurTick(this.getCurTick() + 1);
			if (this.getCurTick() >= this.getNumTicksTillCleanup())
				this.cleanUp();
			else if (this.getCurTick() >= this.solidTicks) {
				// System.out.println("removing tick");
				final int totalBlocks = this.getBlockStates().size();
				final int blocksPerTick = (int) Math
						.ceil(totalBlocks / ((double) this.getNumTicksTillCleanup() - (double) this.solidTicks));
				final int curBlock = (this.getCurTick() - this.solidTicks) * blocksPerTick;
				int maxSize = curBlock + blocksPerTick;
				if (curBlock + blocksPerTick >= this.getBlockStates().size())
					maxSize = this.getBlockStates().size();
				for (int i = curBlock; i < maxSize; i++) {
					final BlockState b = this.getBlockStates().get(i);
					if (b.equals(b.getBlock().getState()))
						b.getBlock().setType(Material.AIR);
				}
			} else if (this.getCurTick() > this.buildTicks) {
				// solid ticks
			} else {
				final int totalBlocks = this.getBlockStates().size();
				final int blocksPerTick = (int) Math
						.ceil(totalBlocks / ((double) this.getNumTicksTillCleanup() - (double) this.solidTicks));
				final int curBlock = (this.getCurTick() - 1) * blocksPerTick;
				int maxSize = curBlock + blocksPerTick;
				if (curBlock + blocksPerTick >= this.getBlockStates().size())
					maxSize = this.getBlockStates().size();
				for (int i = curBlock; i < maxSize; i++) {
					final Block b = this.getBlockStates().get(i).getBlock();
					if (b.getType().equals(Material.AIR)) {
						b.setType(Material.STONE);
						this.getBlockStates().set(i, b.getState());
					}
				}
			}
		}
	}

}
