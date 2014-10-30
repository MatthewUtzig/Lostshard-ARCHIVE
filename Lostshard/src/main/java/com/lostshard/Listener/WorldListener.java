package com.lostshard.Listener;

import org.bukkit.event.Listener;

import com.lostshard.Main.Lostshard;

public class WorldListener implements Listener {
	
	public WorldListener(Lostshard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}
