package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Objects.ChestRefill;

public class ChestRefillManager {
	
	private static ChestRefillManager manager = new ChestRefillManager();
	private List<ChestRefill> clans = new ArrayList<ChestRefill>();
	
	private ChestRefillManager() {
	
	}

	public static ChestRefillManager getManager() {
		return manager;
	}

	public List<ChestRefill> getClans() {
		return clans;
	}

	public void setClans(List<ChestRefill> clans) {
		this.clans = clans;
	}
}
