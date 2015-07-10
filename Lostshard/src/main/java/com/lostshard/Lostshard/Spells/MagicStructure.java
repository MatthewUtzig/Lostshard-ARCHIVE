package com.lostshard.Lostshard.Spells;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;

public class MagicStructure {

	public static MagicStructure findMagicStuckture(Block block) {
		for (final MagicStructure ms : magicstructures)
			if (ms.getBlockStates().contains(block.getState()))
				return ms;
		return null;
	}

	public static List<MagicStructure> getMagicStructures() {
		return magicstructures;
	}

	public static PlayerManager getPm() {
		return pm;
	}

	public static PlotManager getPtm() {
		return ptm;
	}

	public static void removeAll() {
		for (final MagicStructure ms : magicstructures)
			ms.cleanUp();
		magicstructures.clear();
	}

	public static void setPm(PlayerManager pm) {
		MagicStructure.pm = pm;
	}

	public static void setPtm(PlotManager ptm) {
		MagicStructure.ptm = ptm;
	}

	public static void tickGlobal() {
		for (final MagicStructure ms : magicstructures)
			ms.tick();
		magicstructures.removeIf(ms -> ms.isDead);
	}

	protected static PlotManager ptm = PlotManager.getManager();
	protected static PlayerManager pm = PlayerManager.getManager();

	public static List<MagicStructure> magicstructures = new ArrayList<MagicStructure>();

	private UUID creatorUUID;

	private boolean isDead = false;

	private ArrayList<BlockState> blocks = new ArrayList<BlockState>();

	private int numTicksTillCleanup;

	private int curTick = 0;

	public MagicStructure(ArrayList<Block> blocks, UUID uuid,
			int numTicksTillCleanup) {
		for (final Block b : blocks)
			this.blocks.add(b.getState());
		this.setCreatorUUID(uuid);
		this.numTicksTillCleanup = numTicksTillCleanup;
		magicstructures.add(this);
	}

	public void addBlock(Block block) {
		this.blocks.add(block.getState());
	}

	public void cleanUp() {
		// go through each block,
		for (int i = 0; i < this.blocks.size(); i++) {
			final BlockState b = this.blocks.get(i);
			if (b.getType() == b.getBlock().getType())
				b.getBlock().setType(Material.AIR);
		}
		this.isDead = true;
		this.lastThing();
	}

	public ArrayList<Block> getBlocks() {
		final ArrayList<Block> result = new ArrayList<Block>();
		for (final BlockState b : this.blocks)
			result.add(b.getBlock());
		return result;
	}

	public ArrayList<BlockState> getBlockStates() {
		return this.blocks;
	}

	public UUID getCreatorUUID() {
		return this.creatorUUID;
	}

	public int getCurTick() {
		return this.curTick;
	}

	public int getNumTicksTillCleanup() {
		return this.numTicksTillCleanup;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public boolean isPermanent() {
		return this.numTicksTillCleanup < 0;
	}

	public void lastThing() {
	}

	public void setBlocks(ArrayList<BlockState> blocks) {
		this.blocks = blocks;
	}

	public void setBlocks(List<Block> blocks) {
		for (final Block b : blocks)
			this.blocks.add(b.getState());
	}

	public void setBlockStates(ArrayList<Block> blocks) {
		for (final Block block : blocks)
			this.blocks.add(block.getState());
	}

	public void setCreatorUUID(UUID creatorUUID) {
		this.creatorUUID = creatorUUID;
	}

	public void setCurTick(int curTick) {
		this.curTick = curTick;
	}

	public void setDead(boolean dead) {
		this.isDead = dead;
	}

	public void setNumTicksTillCleanup(int numTicksTillCleanup) {
		this.numTicksTillCleanup = numTicksTillCleanup;
	}

	public void tick() {
		if (!this.isDead)
			if (this.numTicksTillCleanup > 0) {
				this.curTick++;
				if (this.curTick >= this.numTicksTillCleanup)
					this.cleanUp();
			}
	}
}
