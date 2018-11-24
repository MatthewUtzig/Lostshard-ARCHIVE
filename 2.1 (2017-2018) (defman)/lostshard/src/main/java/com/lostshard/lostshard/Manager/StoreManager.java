package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Store.Store;

public class StoreManager {

	private static StoreManager manager = new StoreManager();

	public static StoreManager getManager() {
		return manager;
	}

	public static void setManager(StoreManager manager) {
		StoreManager.manager = manager;
	}

	private List<Store> stores = new ArrayList<Store>();

	NPCManager npcm = NPCManager.getManager();

	private StoreManager() {

	}

	public Store createStore() {
		final Store store = new Store();
		return store;
	}

	public Store getStore(Location location) {
		final NPC npc = this.npcm.getVendor(location);
		if(npc == null)
			return null;
		return npc.getStore();
	}

	public List<Store> getStores() {
		return this.stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
}
