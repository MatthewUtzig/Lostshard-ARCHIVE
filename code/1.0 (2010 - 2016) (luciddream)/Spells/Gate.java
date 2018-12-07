package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.block.Block;

public class Gate extends MagicStructure{
	private Block _srcBlock;
	private Block _destBlock;
	
	public Gate(ArrayList<Block> blocks, String creatorName, int numTicksTillCleanup) {
		super(blocks, creatorName, numTicksTillCleanup);
		_srcBlock = blocks.get(0);
		_destBlock = blocks.get(1);
	}
	
	public boolean isSourceBlock(Block block) {
		if((block.getX() == _srcBlock.getX()) && (block.getY() == _srcBlock.getY()) && (block.getZ() == _srcBlock.getZ()))
			return true;
		return false;
	}
	
	public boolean isDestBlock(Block block) {
		if((block.getX() == _destBlock.getX()) && (block.getY() == _destBlock.getY()) && (block.getZ() == _destBlock.getZ()))
			return true;
		return false;
	}
	
	public Block getSourceBlock() {
		return _srcBlock;
	}
	
	public Block getDestBlock() {
		return _destBlock;
	}
}
