package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.block.Block;

public class Iceball extends MagicStructure{
	private boolean _leaveSnow;
	
	public Iceball(ArrayList<Block> blocks, String creatorName,	int numTicksTillCleanup, boolean leaveSnow) {
		super(blocks, creatorName, numTicksTillCleanup);
		_leaveSnow = leaveSnow;
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void cleanUp() {
		// go through each block, 
		for(Block b : _blocks) {
			int blockTypeIdNow = b.getWorld().getBlockTypeIdAt(b.getX(), b.getY(), b.getZ());
			if(b.getTypeId() == blockTypeIdNow) {
				int blockIdBelow = b.getWorld().getBlockTypeIdAt(b.getX(), b.getY()-1, b.getZ());
				if(_leaveSnow) {
					if(blockIdBelow != 0 && blockIdBelow != 78)
						b.setTypeId(78);
					else
						b.setTypeId(0);
				}
				else {
					b.setTypeId(0);
				}
			}
		}
		_isDead = true;
	}

}
