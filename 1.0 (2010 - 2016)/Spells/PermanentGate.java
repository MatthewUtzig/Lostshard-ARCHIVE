package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.block.Block;

public class PermanentGate extends Gate{
	private Block _srcBlock;
	private Block _destBlock;
	private int _id;
	
	public PermanentGate(ArrayList<Block> blocks, String creatorName) {
		super(blocks, creatorName, -1);
		_srcBlock = blocks.get(0);
		_destBlock = blocks.get(1);
		//RPG._permChunks.add(_srcBlock.getChunk().toString());
		//RPG._permChunks.add(_destBlock.getChunk().toString());
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
	
	public void setId(int id) {
		_id = id;
	}
	
	public int getId() {
		return _id;
	}
}
