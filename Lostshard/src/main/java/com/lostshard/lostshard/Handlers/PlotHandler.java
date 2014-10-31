package com.lostshard.lostshard.Handlers;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

	public class PlotHandler {

	/*
	 * Find the plot on the location.
	 */
	public static Plot findPlotAt(Location location) {
		for(Plot plot : Lostshard.getPlots())
			if(Utils.isWithin(plot.getLocation(), location, plot.getSize()))
				return plot;
			else continue;
		return null;
	}

	/*
	 * Find the plot on the location with a buffer
	 */
	public static Plot findPlotAt(Location location, int buffer) {
		for(Plot plot : Lostshard.getPlots())
			if(Utils.isWithin(plot.getLocation(), location, plot.getSize()+buffer))
				return plot;
			else continue;
		return null;
	}

	/*
	 * Allow only friends of the plot to break blocks.
	 */
	public static void breakeBlockInPlot(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player, "Cannot break blocks here, "+plot.getName()+" is protected.");
		}
	}
	
	/*
	 * Allow only friends of the plot to place blocks.
	 */
	public static void placeBlockInPlot(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player, "Cannot place blocks here, "+plot.getName()+" is protected.");
		}
	}
	
	public static void removePlot(Plot plot) {
		Lostshard.getPlots().remove(plot);
	}
	
	/*
	 * Prevent blocks from burning inside a plot. 
	 */
	public static void burnBlockInPlot(BlockBurnEvent event) {
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(plot == null || !plot.isProtected())
			return;
		else
			event.setCancelled(true);
	}

	/*
	 * Allow only friends of the plot to ignite blocks.
	 */
	public static void igniteBlockInPlot(BlockIgniteEvent event) {
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if(plot == null)
			return;
		if(event.getIgnitingEntity() instanceof Player){
			Player player = event.getPlayer();
			if(!plot.isAllowedToBuild(player)) {
				event.setCancelled(true);
				Output.simpleError(player, "Cannot ignite blocks here, "+plot.getName()+" is protected.");
			}
		}else{
			event.setCancelled(true);
		}
	}

	/*
	 * Prevent water to flow into plots, and wither block destruction.
	 */
	public static void fromBlockToBlock(BlockFromToEvent event) {
		Plot toPlot = findPlotAt(event.getBlock().getLocation());
		//Check if there are a plot.
		if(toPlot == null)
			return;
		//Check if the plot is protected
		if(!toPlot.isProtected())
			return;
		Plot fromPlot = findPlotAt(event.getBlock().getLocation());
		//Check if its flowing from the same plot to same plot.
		if(fromPlot == toPlot)
			return;
		event.setCancelled(true);	
	}
	
	/*
	 * Allow only friends to click buttons inside a plot.
	 */
	public static void buttonPush(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if(!block.getType().equals(Material.STONE_BUTTON) && !block.getType().equals(Material.LEVER))
			return;
		Plot plot = findPlotAt(block.getLocation());
		if(!plot.isPrivatePlot())
			return;
		if(plot.isFriendOrAbove(event.getPlayer()))
			return;
		event.setCancelled(true);
		Player player = event.getPlayer();
		Output.simpleError(player, "Cannot click button in \""+plot.getName()+"\" is protected.");
	}
	
}
