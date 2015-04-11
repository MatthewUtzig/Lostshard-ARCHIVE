package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Objects.ChestRefill;

public class ChestRefillManager {
	
	private static ChestRefillManager manager = new ChestRefillManager();
	private List<ChestRefill> chests = new ArrayList<ChestRefill>();
	
	private ChestRefillManager() {
	
	}

	public static ChestRefillManager getManager() {
		return manager;
	}

	public List<ChestRefill> getChests() {
		return chests;
	}

	public void setChests(List<ChestRefill> chests) {
		this.chests = chests;
	}

	public void add(ChestRefill cr) {
		chests.add(cr);
		Database.insertChest(cr);
	}
}
