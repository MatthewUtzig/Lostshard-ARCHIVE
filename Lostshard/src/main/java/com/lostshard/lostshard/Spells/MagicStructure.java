package com.lostshard.lostshard.Spells;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;

public class MagicStructure {
	
	protected static PlotManager ptm = PlotManager.getManager();
	protected static PlayerManager pm = PlayerManager.getManager();
	
	public static List<MagicStructure> magicstructures = new ArrayList<MagicStructure>();
	
	public static void tickGlobal() {
		for(MagicStructure ms : magicstructures) {
			ms.tick();
		}
		magicstructures.removeIf(ms -> ms.isDead);
	}
	
	public static void removeAll() {
		for(MagicStructure ms : magicstructures)
			ms.cleanUp();
		magicstructures.clear();
	}
	
	private UUID creatorUUID;
	private boolean isDead = false;
	private ArrayList<BlockState> blocks  = new ArrayList<BlockState>();
	private int numTicksTillCleanup;
	private int curTick = 0;
	
	public MagicStructure(ArrayList<Block> blocks, UUID uuid, int numTicksTillCleanup) {
		for(Block b : blocks)
			this.blocks.add(b.getState());
		this.setCreatorUUID(uuid);
		this.numTicksTillCleanup = numTicksTillCleanup;
		magicstructures.add(this);
	}
	
	public void tick() {
		if(!isDead) {
			if(numTicksTillCleanup > 0) {
				curTick++;
				if(curTick >= numTicksTillCleanup)
					cleanUp();
			}
		}
	}
	
	public void cleanUp() {
		// go through each block, 
		for(int i=0; i<blocks.size(); i++) {
			BlockState b = blocks.get(i);
			if(b.getBlock().getType() == b.getType())
				b.getBlock().setType(Material.AIR);
		}
		isDead = true;
	}
	
	public void addBlock(Block block) {
		blocks.add(block.getState());
	}
	
	public ArrayList<Block> getBlocks() {
		ArrayList<Block> result = new ArrayList<Block>();
		for(BlockState b : blocks)
			result.add(b.getBlock());
		return result;
	}
	
	public ArrayList<BlockState> getBlockStates() {
		return blocks;
	}
	
	public void setBlockStates(ArrayList<Block> blocks) {
		for(Block block : blocks)
			this.blocks.add(block.getState());
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public void setDead(boolean dead) {
		this.isDead = dead;
	}

	public UUID getCreatorUUID() {
		return creatorUUID;
	}

	public void setCreatorUUID(UUID creatorUUID) {
		this.creatorUUID = creatorUUID;
	}
	
	public boolean isPermanent() {
		return numTicksTillCleanup < 0;
	}
	
	public static MagicStructure findMagicStuckture(Block block) {
		for(MagicStructure ms : magicstructures)
			if(ms.getBlockStates().contains(block.getState()))
				return ms;
		return null;
	}

	public static List<MagicStructure> getMagicStructures() {
		return magicstructures;
	}
	
	public void setBlocks(List<Block> blocks) {
		for(Block b : blocks)
			this.blocks.add(b.getState());
	}

	public static PlotManager getPtm() {
		return ptm;
	}

	public static void setPtm(PlotManager ptm) {
		MagicStructure.ptm = ptm;
	}

	public static PlayerManager getPm() {
		return pm;
	}

	public static void setPm(PlayerManager pm) {
		MagicStructure.pm = pm;
	}

	public int getNumTicksTillCleanup() {
		return numTicksTillCleanup;
	}

	public void setNumTicksTillCleanup(int numTicksTillCleanup) {
		this.numTicksTillCleanup = numTicksTillCleanup;
	}

	public int getCurTick() {
		return curTick;
	}

	public void setCurTick(int curTick) {
		this.curTick = curTick;
	}

	public void setBlocks(ArrayList<BlockState> blocks) {
		this.blocks = blocks;
	}
}
