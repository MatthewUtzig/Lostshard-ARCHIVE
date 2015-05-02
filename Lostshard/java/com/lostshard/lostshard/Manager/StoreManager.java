package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Store.Store;

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

	public Store getStore(int npcID) {
		for(Store s : stores)
			if(s.getNpcId() == npcID)
				return s;
		return null;
	}

	public Store getStore(Location location) {
		NPC npc = npcm.getVendor(location);
		return getStore(npc);
	}
	
	public Store getStore(NPC npc) {
		if(npc == null)
			return null;
		return getStore(npc.getId());
	}
	
	public List<Store> getStores() {
		return stores;
	}
	
	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
}
