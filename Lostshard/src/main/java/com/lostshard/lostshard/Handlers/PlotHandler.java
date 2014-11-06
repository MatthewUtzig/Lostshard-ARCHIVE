package com.lostshard.lostshard.Handlers;

import org.bukkit.ChatColor;
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
import org.bukkit.event.player.PlayerMoveEvent;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class PlotHandler {

	/**
	 * @param location
	 * @return
	 * 
	 * Find plot at location.
	 */
	public static Plot findPlotAt(Location location) {
		for (Plot plot : Lostshard.getPlots())
			if (Utils.isWithin(plot.getLocation(), location, plot.getSize()))
				return plot;
			else
				continue;
		return null;
	}

	/**
	 * @param location
	 * @param buffer
	 * @return Plot
	 * 
	 * Find plot at location
	 */
	public static Plot findPlotAt(Location location, int buffer) {
		for (Plot plot : Lostshard.getPlots())
			if (Utils.isWithin(plot.getLocation(), location, plot.getSize()
					+ buffer))
				return plot;
			else
				continue;
		return null;
	}

	/**
	 * @param event
	 * 
	 * Allow only friends of the plot to break blocks.
	 */
	public static void breakeBlockInPlot(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player,
					"Cannot break blocks here, " + plot.getName()
							+ " is protected.");
		}
	}

	/**
	 * @param event
	 * 
	 * Allow only friends of the plot to place blocks.
	 */
	public static void placeBlockInPlot(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player,
					"Cannot place blocks here, " + plot.getName()
							+ " is protected.");
		}
	}

	public static void removePlot(Plot plot) {
		Lostshard.getPlots().remove(plot);
	}

	/**
	 * @param event
	 * 
	 * Prevent blocks from burning inside a plot.
	 */
	public static void burnBlockInPlot(BlockBurnEvent event) {
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if (plot == null || !plot.isProtected())
			return;
		else
			event.setCancelled(true);
	}

	/**
	 * @param event
	 * 
	 * Allow only friends of the plot to ignite blocks.
	 */
	public static void igniteBlockInPlot(BlockIgniteEvent event) {
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		if (event.getIgnitingEntity() instanceof Player) {
			Player player = event.getPlayer();
			if (!plot.isAllowedToBuild(player)) {
				event.setCancelled(true);
				Output.simpleError(player,
						"Cannot ignite blocks here, " + plot.getName()
								+ " is protected.");
			}
		} else {
			event.setCancelled(true);
		}
	}

	/**
	 * @param event
	 * 
	 * Prevent water to flow into plots, and wither block destruction.
	 */
	public static void fromBlockToBlock(BlockFromToEvent event) {
		Plot toPlot = findPlotAt(event.getBlock().getLocation());
		// Check if there are a plot.
		if (toPlot == null)
			return;
		// Check if the plot is protected
		if (!toPlot.isProtected())
			return;
		Plot fromPlot = findPlotAt(event.getBlock().getLocation());
		// Check if its flowing from the same plot to same plot.
		if (fromPlot == toPlot)
			return;
		event.setCancelled(true);
	}

	/**
	 * @param event
	 * 
	 * Allow only friends to click buttons and leavers inside a plot.
	 */
	public static void onButtonPush(PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (!block.getType().equals(Material.STONE_BUTTON)
				&& !block.getType().equals(Material.LEVER))
			return;
		Plot plot = findPlotAt(block.getLocation());
		if (!plot.isPrivatePlot())
			return;
		if (plot.isFriendOrAbove(event.getPlayer()))
			return;
		event.setCancelled(true);
		Player player = event.getPlayer();
		Output.simpleError(player, "Cannot click button in \"" + plot.getName()
				+ "\" is protected.");
	}

	/**
	 * @param id
	 * @return plot
	 * 
	 * Get plot from id.
	 */
	public static Plot getPlotById(int id) {
		for (Plot plot : Lostshard.getPlots())
			if (plot.getId() == id)
				return plot;
		return null;
	}
	
	/**
	 * @param event
	 * 
	 * Display plot enter message.
	 */
	public static void onPlotEnter(PlayerMoveEvent event) {
		if(event.getTo().getBlock() == event.getFrom().getBlock())
			return;
		Player player = event.getPlayer();
    	Plot fromPlot = PlotHandler.findPlotAt(event.getFrom().getBlock().getLocation());
    	Plot toPlot = PlotHandler.findPlotAt(event.getTo().getBlock().getLocation());            	
    	if(fromPlot == null && toPlot != null) {
    		// must be entering a plot
    		player.sendMessage(ChatColor.GRAY+"You have entered "+toPlot.getName());
    	}
    	else if(toPlot == null && fromPlot != null) {
    		// must be leaving a plot
    		player.sendMessage(ChatColor.GRAY+"You have left "+fromPlot.getName());
    	}
    	else if(fromPlot != null && toPlot != null && fromPlot != toPlot){
    		// must be moving from one plot to another
    		player.sendMessage(ChatColor.GRAY+"You have left "+fromPlot.getName()+" and entered "+toPlot.getName());
    	}
	}

}
