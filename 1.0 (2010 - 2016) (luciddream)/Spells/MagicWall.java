package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class MagicWall extends MagicStructure{
	protected int _buildTicks = 25;
	protected int _solidTicks = 125;
	
	public MagicWall(ArrayList<Block> blocks, String creatorName,	int numTicksTillCleanup) {
		super(blocks, creatorName, numTicksTillCleanup);
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public void tick() {
		if(!_isDead) {
			_curTick++;
			if(_curTick >= _numTicksTillCleanup)
				cleanUp();
			else if(_curTick >= _solidTicks) {
				//System.out.println("removing tick");
				int totalBlocks = _blocks.size();
				int blocksPerTick = (int)Math.ceil((double)totalBlocks / ((double)_numTicksTillCleanup-(double)_solidTicks));
				int curBlock = ((_curTick-_solidTicks))*blocksPerTick;
				int maxSize = curBlock+blocksPerTick;
				if(curBlock+blocksPerTick >= _blocks.size())
					maxSize = _blocks.size();
				for(int i=curBlock; i<maxSize; i++) {
					Block b = _blocks.get(i);
					b.setType(Material.AIR);
					/*if(b.getType() == etc.getServer().getBlockAt(b.getX(), b.getY(), b.getZ()).getType()) {
						b.setType(0);
						b.update();
					}*/
				}
			}
			else if(_curTick > _buildTicks) {
				// solid ticks
			}
			else {
				int totalBlocks = _blocks.size();
				int blocksPerTick = (int)Math.ceil((double)totalBlocks / ((double)_numTicksTillCleanup-(double)_solidTicks));
				int curBlock = (_curTick-1)*blocksPerTick;
				int maxSize = curBlock+blocksPerTick;
				if(curBlock+blocksPerTick >= _blocks.size()) {
					maxSize = _blocks.size();
				}
				for(int i=curBlock; i<maxSize; i++) {
					Block b = _blocks.get(i);
					b.setType(Material.STONE);
					/*if(b.getType() == etc.getServer().getBlockAt(b.getX(), b.getY(), b.getZ()).getType()) {
						b.setType(49);
						b.update();
					}*/
				}
			}
		}
	}
}
