package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Objects.ChestRefill;

public class ChestRefillManager {
	
	public static ChestRefillManager getManager() {
		return manager;
	}
	private static ChestRefillManager manager = new ChestRefillManager();
	
	private List<ChestRefill> chests = new ArrayList<ChestRefill>();

	private ChestRefillManager() {
	
	}

	public void add(ChestRefill cr) {
		chests.add(cr);
		Database.insertChest(cr);
	}

	public ChestRefill getChest(Chest chest) {
		for(ChestRefill cr : chests) {
			if(cr.getLocation().equals(chest.getLocation()))
				return cr;
		}
		Chest sc = null;
		if(chest.getBlock().getRelative(BlockFace.NORTH).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.NORTH).getState();
		else if(chest.getBlock().getRelative(BlockFace.SOUTH).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.SOUTH).getState();
		else if(chest.getBlock().getRelative(BlockFace.WEST).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.WEST).getState();
		else if(chest.getBlock().getRelative(BlockFace.EAST).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.EAST).getState();
		if(sc != null) {
			for(ChestRefill cr : chests) {
				if(cr.getLocation().equals(sc.getLocation()))
					return cr;
			}
		}
		return null;
	}

	public List<ChestRefill> getChests() {
		return chests;
	}
	
	public void remove(ChestRefill cr) {
		chests.remove(cr);
		Database.deleteChest(cr);
	}

	public void setChests(List<ChestRefill> chests) {
		this.chests = chests;
	}
	
	public void tick() {
		for(ChestRefill cr : chests)
			cr.tick();
	}
}
