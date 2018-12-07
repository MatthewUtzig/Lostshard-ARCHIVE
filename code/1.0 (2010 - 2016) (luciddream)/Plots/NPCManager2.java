package com.lostshard.RPG.Plots;

import org.bukkit.Location;

import com.thevoxelbox.bukkit.port.NPC.BasicHumanNpc;
import com.thevoxelbox.bukkit.port.NPC.BasicHumanNpcList;
import com.thevoxelbox.bukkit.port.NPC.NpcSpawner;

public class NPCManager2 {
	public BasicHumanNpcList HumanNPCList = new BasicHumanNpcList();
	
	public void despawn(String name) {
		if(HumanNPCList.containsKey(name)) {
			BasicHumanNpc hnpc = getNPC(name);
			HumanNPCList.remove(name);
			NpcSpawner.RemoveBasicHumanNpc(hnpc);
		}
		else {}
	}
	
	/*public void loadNPC(String name, Location location) {
		npcChunkHashMap.put(location.getWorld().getChunkAt(location).toString(), name);
	}*/
	
	public BasicHumanNpc spawnNPC(String name, Location location) {
		BasicHumanNpc hnpc = NpcSpawner.SpawnBasicHumanNpc(name, name, location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.HumanNPCList.put(name, hnpc);
		return hnpc;
	}
	
	public BasicHumanNpc getNPC(String name) {
		if(HumanNPCList.containsKey(name))
			return HumanNPCList.get(name);
		return null;
	}
	
	public void moveNPC(String name, Location location) {
		BasicHumanNpc hnpc = getNPC(name);
		hnpc.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
	}
	
	public BasicHumanNpc spawnNPC(String name, Location location, String id) {
		BasicHumanNpc hnpc = NpcSpawner.SpawnBasicHumanNpc(name, name, location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.HumanNPCList.put(name, hnpc);
		return hnpc;
	}
}