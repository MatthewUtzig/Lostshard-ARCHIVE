package com.lostshard.RPG.Plots;

import java.util.List;

import me.neodork.npclib.entity.HumanNPC;
import me.neodork.npclib.entity.NPC;

import org.bukkit.Location;

import com.lostshard.RPG.RPG;

public class MyNPCManager {
	//public BasicHumanNpcList HumanNPCList = new BasicHumanNpcList();
	
	public void despawn(String name) {
		RPG._npcManager.despawnHumanByName(name);
		/*if(HumanNPCList.containsKey(name)) {
			HumanNPC hnpc = getNPC(name);
			HumanNPCList.remove(name);
			NpcSpawner.RemoveBasicHumanNpc(hnpc);
		}*/
	}
	
	public HumanNPC spawnNPC(String name, Location location) {
		HumanNPC npc = (HumanNPC)RPG._npcManager.spawnHumanNPC(name, location);
		/*HumanNPC hnpc = NpcSpawner.SpawnBasicHumanNpc(name, name, location);
        this.HumanNPCList.put(name, hnpc);*/
		return npc;
	}
	
	public HumanNPC getNPC(String name) {
		List<NPC> npcsByName = RPG._npcManager.getHumanNPCByName(name);
		if(npcsByName.size() == 0)
			return null;
		
		HumanNPC npc = (HumanNPC)npcsByName.get(0);
		/*if(HumanNPCList.containsKey(name))
			return HumanNPCList.get(name);*/
		return npc;
	}
	
	public void moveNPC(String name, Location location) {
		HumanNPC npc = getNPC(name);
		npc.moveTo(location);
		/*HumanNPC hnpc = getNPC(name);
		hnpc.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());*/
	}
	
	public HumanNPC spawnNPC(String name, Location location, String id) {
		HumanNPC npc = (HumanNPC)RPG._npcManager.spawnHumanNPC(name, location, id);
		/*HumanNPC hnpc = NpcSpawner.SpawnBasicHumanNpc(name, name, location);
        this.HumanNPCList.put(name, hnpc);*/
		return npc;
	}
}
