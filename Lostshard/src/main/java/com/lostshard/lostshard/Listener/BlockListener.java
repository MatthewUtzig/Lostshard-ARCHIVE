package com.lostshard.lostshard.Listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Main.Lostshard;

public class BlockListener implements Listener {

	public BlockListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		PlotHandler.breakeBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		PlotHandler.placeBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurn(BlockBurnEvent event) {
		PlotHandler.burnBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event) {
		PlotHandler.igniteBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent event) {
		PlotHandler.fromBlockToBlock(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		PlotHandler.onPistonExtend(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFade(BlockFadeEvent event) {
		PlotHandler.onBlockFade(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.isCancelled())
			return;
		if (event.getBlock().getType().equals(Material.PORTAL)) {
			event.setCancelled(true);
		}
	}
}
