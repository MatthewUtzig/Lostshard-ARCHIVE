package com.lostshard.Lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;

import com.lostshard.Lostshard.Main.Lostshard;

public class WorldListener extends LostshardListener {
	
	public WorldListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldLoadEvent event) {

	}
}
