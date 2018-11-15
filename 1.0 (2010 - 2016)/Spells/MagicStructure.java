package com.lostshard.RPG.Spells;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.block.Block;

import com.lostshard.RPG.Utils.Utils;

public class MagicStructure {
	private String _creatorName;
	protected boolean _isDead = false;
	protected ArrayList<Block> _blocks;
	private HashMap<String, Block> _hashMapBlocks;
	protected int _numTicksTillCleanup;
	protected int _curTick = 0;
	
	public MagicStructure(ArrayList<Block> blocks, String creatorName, int numTicksTillCleanup) {
		_blocks = blocks;
		_creatorName = creatorName;
		_numTicksTillCleanup = numTicksTillCleanup;
		
		_hashMapBlocks = new HashMap<String, Block>();
		for(Block b : _blocks) {
			String posString = ""+b.getX()+"|"+b.getY()+"|"+b.getZ();
			_hashMapBlocks.put(posString, b);
		}
	}
	
	public void tick() {
		if(!_isDead) {
			if(_numTicksTillCleanup != -1) {
				_curTick++;
				if(_curTick >= _numTicksTillCleanup)
					cleanUp();
			}
		}
	}
	
	public String getCreatorName() {
		return _creatorName;
	}
	
	private void addBlockToHashMap(Block block) {
		String posString = ""+block.getX()+"|"+block.getY()+"|"+block.getZ();
		_hashMapBlocks.put(posString, block);
	}
	
	public Block findBlockAt(int x, int y, int z) {
		String posString = ""+x+"|"+y+"|"+z;
		if(_hashMapBlocks.containsKey(posString))
			return _hashMapBlocks.get(posString);
		return null;
	}
	
	public void cleanUp() {
		// go through each block, 
		for(Block b : _blocks) {
			Utils.loadChunkAtLocation(b.getLocation());
			int blockTypeIdNow = b.getWorld().getBlockTypeIdAt(b.getX(), b.getY(), b.getZ());
			if(b.getTypeId() == blockTypeIdNow) {
				//b.getWorld().getBlockAt(b.getLocation()).setTypeId(0);
				b.setTypeId(0);
			}
		}
		_isDead = true;
	}
	
	public void addBlock(Block block) {
		_blocks.add(block);
		addBlockToHashMap(block);
	}
	
	public ArrayList<Block> getBlocks() {
		return _blocks;
	}
	
	public boolean isDead() {
		return _isDead;
	}
}
