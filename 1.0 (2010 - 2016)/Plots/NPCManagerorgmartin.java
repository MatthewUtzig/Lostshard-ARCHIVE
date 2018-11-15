package com.lostshard.RPG.Plots;

import org.bukkit.Location;
import org.martin.bukkit.npclib.NPCEntity;

import com.lostshard.RPG.RPG;

public class NPCManagerorgmartin {
	public void despawn(String name) {
		NPCEntity npcEntity = RPG._npcManagerLibrary.getNPC(name);
		if(npcEntity != null)
			RPG._npcManagerLibrary.despawn(name);
		
		/*if(HumanNPCList.containsKey(name)) {
			NPCEntity hnpc = getNPC(name);
			HumanNPCList.remove(name);
			NpcSpawner.RemoveBasiNPCEntity(hnpc);
		}*/
	}
	
	public NPCEntity spawnNPC(String name, Location location) {
		NPCEntity npcEntity = RPG._npcManagerLibrary.getNPC(name);
		if(npcEntity == null)
			return RPG._npcManagerLibrary.spawnNPC(name, location);
		return null;
		/*NPCEntity hnpc = NpcSpawner.SpawnBasiNPCEntity(name, name, location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.HumanNPCList.put(name, hnpc);
		return hnpc;*/
	}
	
	public NPCEntity getNPC(String name) {
		return RPG._npcManagerLibrary.getNPC(name);
	}
	
	public void moveNPC(String name, Location location) {
		NPCEntity npcEntity = RPG._npcManagerLibrary.getNPC(name);
		if(npcEntity != null)
			RPG._npcManagerLibrary.moveNPC(name, location);
		
		/*
		NPCEntity hnpc = getNPC(name);
		hnpc.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());*/
	}
	
	public NPCEntity spawnNPC(String name, Location location, String id) {
		NPCEntity npcEntity = RPG._npcManagerLibrary.getNPC(name);
		if(npcEntity == null)
			return RPG._npcManagerLibrary.spawnNPC(name, location);
		return null;
		/*NPCEntity hnpc = NpcSpawner.SpawnBasiNPCEntity(name, name, location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.HumanNPCList.put(name, hnpc);
		return hnpc;*/
	}
}