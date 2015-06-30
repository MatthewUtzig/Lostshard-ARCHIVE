package com.lostshard.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Skills.TamingSkill;

public class VehicleListener extends LostshardListener {

	public VehicleListener(Lostshard plugin) {
		super(plugin);
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	public void onEntityDismount(VehicleExitEvent event) {
		TamingSkill.onDismount(event);
	}
	
}
