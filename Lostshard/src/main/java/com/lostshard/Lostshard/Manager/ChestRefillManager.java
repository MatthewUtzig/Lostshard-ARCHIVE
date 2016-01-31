package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

import com.lostshard.Lostshard.Objects.ChestRefill;

public class ChestRefillManager {

	private static ChestRefillManager manager = new ChestRefillManager();

	public static ChestRefillManager getManager() {
		return manager;
	}

	private List<ChestRefill> chests = new ArrayList<ChestRefill>();

	private ChestRefillManager() {

	}

	public void add(ChestRefill cr) {
		this.chests.add(cr);
		cr.insert();
	}

	public ChestRefill getChest(Chest chest) {
		for (final ChestRefill cr : this.chests)
			if (cr.getLocation().equals(chest.getLocation()))
				return cr;
		Chest sc = null;
		if (chest.getBlock().getRelative(BlockFace.NORTH).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.NORTH).getState();
		else if (chest.getBlock().getRelative(BlockFace.SOUTH).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.SOUTH).getState();
		else if (chest.getBlock().getRelative(BlockFace.WEST).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.WEST).getState();
		else if (chest.getBlock().getRelative(BlockFace.EAST).getState() instanceof Chest)
			sc = (Chest) chest.getBlock().getRelative(BlockFace.EAST).getState();
		if (sc != null)
			for (final ChestRefill cr : this.chests)
				if (cr.getLocation().equals(sc.getLocation()))
					return cr;
		return null;
	}

	public List<ChestRefill> getChests() {
		return this.chests;
	}

	public void remove(ChestRefill cr) {
		this.chests.remove(cr);
		cr.delete();
	}

	public void setChests(List<ChestRefill> chests) {
		this.chests = chests;
	}

	public void tick() {
		for (final ChestRefill cr : this.chests)
			cr.tick();
	}
}
