package com.lostshard.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Skills.TamingSkill;

public class VehicleListener implements Listener {

	public VehicleListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onEntityDismount(VehicleExitEvent event) {
		TamingSkill.onDismount(event);
	}
	
}
