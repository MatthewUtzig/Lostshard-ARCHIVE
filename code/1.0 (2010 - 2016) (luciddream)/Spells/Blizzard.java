package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Blizzard extends MagicStructure{
	private static final int SOLID_TICKS = 20;
	
	public Blizzard(ArrayList<Block> blocks, String creatorName,	int numTicksTillCleanup) {
		super(blocks, creatorName, numTicksTillCleanup);
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void tick() {
		if(!_isDead) {
			_curTick++;
			if(_curTick >= _numTicksTillCleanup)
				cleanUp();
			
			if(_curTick > SOLID_TICKS) {
				int randBlock = (int)Math.floor(Math.random()*_blocks.size());
				Block b = _blocks.get(randBlock);
				if(b != null) {
					b.setType(Material.AIR);
				}
			}
		}
	}
	
	@Override 
	public void cleanUp() {
		// go through each block, 
		for(Block b : _blocks) {
			int blockTypeIdNow = b.getWorld().getBlockTypeIdAt(b.getX(), b.getY(), b.getZ());
			if(b.getTypeId() == blockTypeIdNow) {
				b.setTypeId(0);
			}
		}
		_isDead = true;
	}

}
