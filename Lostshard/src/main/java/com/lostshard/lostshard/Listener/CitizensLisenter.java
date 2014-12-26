package com.lostshard.lostshard.Listener;

import net.citizensnpcs.api.event.CitizensEnableEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCManager;

public class CitizensLisenter implements Listener {

	public CitizensLisenter(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCitizensEnable(CitizensEnableEvent event) {
		System.out.print("Spawning NPCS!");
		NPCManager.getRegistry().deregisterAll();
		for(NPC npc : Lostshard.getRegistry().getNpcs())
			npc.spawn();
	}
	
}
