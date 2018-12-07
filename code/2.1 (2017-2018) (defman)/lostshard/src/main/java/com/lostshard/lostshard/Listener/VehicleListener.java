package com.lostshard.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

import com.lostshard.lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.lostshard.main.Lostshard;
import com.lostshard.lostshard.Skills.TamingSkill;

public class VehicleListener extends LostshardListener {

	public VehicleListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler
	public void onEntityDismount(VehicleExitEvent event) {
		TamingSkill.onDismount(event);
	}
	
	@EventHandler
	public void VehicleDestroy(VehicleDestroyEvent event) {
		PlotProtectionHandler.VehicleDestroy(event);
	}

	@EventHandler
	public void VehicleCreate(VehicleCreateEvent event) {
		PlotProtectionHandler.VehicleCreate(event);
	}
	
	@EventHandler
	public void VehicleEnter(VehicleEnterEvent event) {
		PlotProtectionHandler.VehicleEnter(event);
	}
}
