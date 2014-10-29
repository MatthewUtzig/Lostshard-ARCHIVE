package com.lostshard.Handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.lostshard.Main.Lostshard;
import com.lostshard.Objects.Plot;
import com.lostshard.Utils.Output;
import com.lostshard.Utils.Utils;

	public class PlotHandler {

	public static Plot findPlotAt(Location location) {
		for(Plot plot : Lostshard.getPlots())
			if(Utils.isWithin(plot.getLocation(), location, plot.getSize()))
				return plot;
			else continue;
		return null;
	}

	public static Plot findPlotAt(Location location, int buffer) {
		for(Plot plot : Lostshard.getPlots())
			if(Utils.isWithin(plot.getLocation(), location, plot.getSize()+buffer))
				return plot;
			else continue;
		return null;
	}

	public static void breakeBlockInPlot(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpelError(player, "Cannot break blocks here, "+plot.getName()+" is protected.");
		}
	}
	
	public static void placeBlockInPlot(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpelError(player, "Cannot place blocks here, "+plot.getName()+" is protected.");
		}
	}
	
	public static void removePlot(Plot plot) {
		Lostshard.getPlots().remove(plot);
	}

	public static void burnBlockInPlot(BlockBurnEvent event) {
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(plot == null || !plot.isProtected())
			return;
		else
			event.setCancelled(true);
	}

	public static void igniteBlockInPlot(BlockIgniteEvent event) {
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(plot == null)
			return;
		if(event.getIgnitingEntity() instanceof Player){
			Player player = event.getPlayer();
			if(!plot.isAllowedToBuild(player)) {
				event.setCancelled(true);
				Output.simpelError(player, "Cannot ignite blocks here, "+plot.getName()+" is protected.");
			}
		}else{
			event.setCancelled(true);
		}
	}
	
}
