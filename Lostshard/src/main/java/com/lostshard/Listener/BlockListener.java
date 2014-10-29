package com.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.lostshard.Handlers.PlotHandler;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		PlotHandler.breakeBlockInPlot(event);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		PlotHandler.placeBlockInPlot(event);
	}
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		PlotHandler.burnBlockInPlot(event);
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		PlotHandler.igniteBlockInPlot(event);
	}
	
}
