package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.Objects.Store.Store;

public class StoreManager {

	public static StoreManager getManager() {
		return manager;
	}

	public static void setManager(StoreManager manager) {
		StoreManager.manager = manager;
	}

	private static StoreManager manager = new StoreManager();

	private List<Store> stores = new ArrayList<Store>();

	NPCManager npcm = NPCManager.getManager();

	private StoreManager() {

	}
	
	public Store createStore() {
		Store store = new Store();
		return store;
	}

	public Store getStore(Location location) {
		final NPC npc = this.npcm.getVendor(location);
		return npc.getStore();
	}

	public List<Store> getStores() {
		return this.stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
}
