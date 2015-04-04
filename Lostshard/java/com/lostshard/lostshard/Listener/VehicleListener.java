package com.lostshard.lostshard.Listener;

import org.bukkit.event.Listener;

import com.lostshard.lostshard.Main.Lostshard;

public class VehicleListener implements Listener {

	public VehicleListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

}
