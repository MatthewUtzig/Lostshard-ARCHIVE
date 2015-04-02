package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Store.Store;
import com.lostshard.lostshard.Utils.Utils;

public class NPCHandler {

	public static List<NPC> getBankers() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : Lostshard.getRegistry().getNpcs())
			if (npc.getType().equals(NPCType.BANKER))
				result.add(npc);
		return result;
	}

	public static List<NPC> getGuards() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : Lostshard.getRegistry().getNpcs())
			if (npc.getType().equals(NPCType.GUARD))
				result.add(npc);
		return result;
	}

	public static List<NPC> getVendors() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : Lostshard.getRegistry().getNpcs())
			if (npc.getType().equals(NPCType.VENDOR))
				result.add(npc);
		return result;
	}

	public static int getNextId() {
		int result = 0;
		for (NPC npc : Lostshard.getRegistry().getNpcs())
			if (npc.getId() > 0)
				result = npc.getId() + 1;
		return result;
	}
	
	public static NPC getNearestVendor(Player player) {
		NPC nearestVendor = null;
		double nearestVendorDistance = 100;
		for(NPC vendor : getVendors()) {
			if(!vendor.getLocation().getWorld().equals(player.getLocation().getWorld()))
				continue;
			double distance = Utils.fastDistance(player.getLocation(), vendor.getLocation());
			if(distance < nearestVendorDistance) {
				nearestVendor = vendor;
				nearestVendorDistance = distance;
			}
		}
		return nearestVendor;
	}
	
	public static Store getStore(NPC npc) {
		for(Store store : Lostshard.getRegistry().getStores()) {
			if(store.getNpcId() == npc.getId())
				return store;
		}
		return null;
	}

}
