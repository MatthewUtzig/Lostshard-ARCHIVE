package com.lostshard.Listener;

import org.bukkit.event.Listener;

import com.lostshard.Main.Lostshard;

public class PlayerListener implements Listener {

	public PlayerListener(Lostshard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
}
