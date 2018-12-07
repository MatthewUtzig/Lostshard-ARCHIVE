package com.lostshard.RPG.Listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.Plugin;

import com.lostshard.RPG.RPG;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Utils.Output;

public class RPGVehicleListener implements Listener {
	Plugin plugin;
	
	public RPGVehicleListener(RPG instance) {
    	plugin = instance;
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onVehicleMove(VehicleMoveEvent event) {
		Vehicle vehicle = event.getVehicle();
		Entity passenger = vehicle.getPassenger();
		if(passenger != null && passenger instanceof Player) {
			Player player = (Player)passenger;
			Plot plot = PlotHandler.findPlotAt(event.getTo());
			if(plot != null && plot.isLocked()) {
				if(!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) && !plot.isFriend(player.getName())) {
					Output.simpleError(player, "Cannot ride into a protected plot unless you are a friend.");
					vehicle.eject();
				}
			}
		}
	}
	
	@EventHandler
	public void onVehicleEnter(VehicleEnterEvent event) {
		Vehicle vehicle = event.getVehicle();
		Entity passenger = event.getEntered();
		if(passenger != null && passenger instanceof Player) {
			Player player = (Player)passenger;
			Plot plot = PlotHandler.findPlotAt(vehicle.getLocation());
			if(plot != null && plot.isLocked()) {
				if(!plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()) && !plot.isFriend(player.getName())) {
					Output.simpleError(player, "Cannot enter into a protected plot unless you are a friend.");
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
