package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SandStorm extends MagicStructure{	
	public SandStorm(ArrayList<Block> blocks, String creatorName, int numTicksTillCleanup) {
		super(blocks, creatorName, numTicksTillCleanup);
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void cleanUp() {
		// go through each block, 
		for(Block b : _blocks) {
			b.setType(Material.getMaterial(b.getWorld().getBlockTypeIdAt(b.getX(), b.getY(), b.getZ())));
		}
		_isDead = true;
	}

}
