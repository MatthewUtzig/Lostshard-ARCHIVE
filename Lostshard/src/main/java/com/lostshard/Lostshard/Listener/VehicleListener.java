package com.lostshard.Lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Skills.TamingSkill;

public class VehicleListener extends LostshardListener {

	public VehicleListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler
	public void onEntityDismount(VehicleExitEvent event) {
		TamingSkill.onDismount(event);
	}
	
}
