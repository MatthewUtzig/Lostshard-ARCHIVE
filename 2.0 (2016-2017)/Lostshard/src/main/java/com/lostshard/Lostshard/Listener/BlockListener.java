package com.lostshard.Lostshard.Listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.lostshard.Lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Skills.LumberjackingSkill;
import com.lostshard.Lostshard.Skills.MiningSkill;
import com.lostshard.Lostshard.Spells.Structures.Gate;

public class BlockListener extends LostshardListener {

	public BlockListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler
	public void LeavesDecay(LeavesDecayEvent event) {
		PlotProtectionHandler.leavesDecay(event);
	}
	
	@EventHandler
	public void BlockForm(BlockFormEvent event) {
		PlotProtectionHandler.BlockForm(event);
	}
	
	@EventHandler
	public void BlockSrpead(BlockSpreadEvent event) {
		PlotProtectionHandler.BlockSrpead(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreakHigh(BlockBreakEvent event) {
		PlotProtectionHandler.breakeBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreakLowest(BlockBreakEvent event) {
		MiningSkill.onBlockBreak(event);
		LumberjackingSkill.blockBrokeWithAxe(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurn(BlockBurnEvent event) {
		PlotProtectionHandler.burnBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFade(BlockFadeEvent event) {
		PlotProtectionHandler.onBlockFade(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent event) {
		PlotProtectionHandler.fromBlockToBlock(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event) {
		PlotProtectionHandler.igniteBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.isCancelled())
			return;
		if (event.getBlock().getType().equals(Material.PORTAL))
			event.setCancelled(true);
		Gate.onBlockPhysics(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		PlotProtectionHandler.placeBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlaceLow(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		final Block block = event.getBlock();
		block.setMetadata("placed", new FixedMetadataValue(this.getPlugin(), true));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreak(HangingBreakEvent event) {
		PlotProtectionHandler.onHangingDestory(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlace(HangingPlaceEvent event) {
		PlotProtectionHandler.onHangingPlace(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		PlotProtectionHandler.onPistonExtend(event);
	}
}
