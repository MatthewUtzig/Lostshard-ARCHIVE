package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.lostshard.lostshard.Spells.MagicStructure;

public class WallOfStone extends MagicStructure {

	protected int buildTicks = 25;

	protected int solidTicks = 125;
	public WallOfStone(ArrayList<Block> blocks, UUID uuid,
			int numTicksTillCleanup) {
		super(blocks, uuid, numTicksTillCleanup);
	}
	
	@Override 
	public void tick() {
		if(!isDead()) {
			setCurTick(getCurTick()+1);
			if(getCurTick() >= getNumTicksTillCleanup())
				cleanUp();
			else if(getCurTick() >= solidTicks) {
				//System.out.println("removing tick");
				int totalBlocks = getBlockStates().size();
				int blocksPerTick = (int)Math.ceil(totalBlocks / ((double)getNumTicksTillCleanup()-(double)solidTicks));
				int curBlock = ((getCurTick()-solidTicks))*blocksPerTick;
				int maxSize = curBlock+blocksPerTick;
				if(curBlock+blocksPerTick >= getBlockStates().size())
					maxSize = getBlockStates().size();
				for(int i=curBlock; i<maxSize; i++) {
					BlockState b = getBlockStates().get(i);
					if(b.equals(b.getBlock().getState()))
						b.getBlock().setType(Material.AIR);
				}
			}
			else if(getCurTick() > buildTicks) {
				// solid ticks
			}
			else {
				int totalBlocks = getBlockStates().size();
				int blocksPerTick = (int)Math.ceil(totalBlocks / ((double)getNumTicksTillCleanup()-(double)solidTicks));
				int curBlock = (getCurTick()-1)*blocksPerTick;
				int maxSize = curBlock+blocksPerTick;
				if(curBlock+blocksPerTick >= getBlockStates().size()) {
					maxSize = getBlockStates().size();
				}
				for(int i=curBlock; i<maxSize; i++) {
					Block b = getBlockStates().get(i).getBlock();
					if(b.getType().equals(Material.AIR)){
						b.setType(Material.STONE);
						getBlockStates().set(i, b.getState());
					}
				}
			}
		}
	}

	
}
