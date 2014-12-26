package com.lostshard.lostshard.Handlers;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

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
	 *         Find plot at location.
	 */
	public static Plot findPlotAt(Location location) {
		for (Plot plot : Lostshard.getRegistry().getPlots())
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
	 *         Find plot at location
	 */
	public static Plot findPlotAt(Location location, int buffer) {
		for (Plot plot : Lostshard.getRegistry().getPlots())
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
	 *            Allow only friends of the plot to break blocks.
	 */
	public static void breakeBlockInPlot(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
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
	 *            Allow only friends of the plot to place blocks.
	 */
	public static void placeBlockInPlot(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player,
					"Cannot place blocks here, " + plot.getName()
							+ " is protected.");
		}
	}

	public static void removePlot(Plot plot) {
		Lostshard.getRegistry().getPlots().remove(plot);
	}

	/**
	 * @param event
	 * 
	 *            Prevent blocks from burning inside a plot.
	 */
	public static void burnBlockInPlot(BlockBurnEvent event) {
		if (event.isCancelled())
			return;
		Plot plot = findPlotAt(event.getBlock().getLocation());
		if (plot == null || !plot.isProtected())
			return;
		else
			event.setCancelled(true);
	}

	/**
	 * @param event
	 * 
	 *            Allow only friends of the plot to ignite blocks.
	 */
	public static void igniteBlockInPlot(BlockIgniteEvent event) {
		if (event.isCancelled())
			return;
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
	 *            Prevent water to flow into plots, and wither block
	 *            destruction.
	 */
	public static void fromBlockToBlock(BlockFromToEvent event) {
		if (event.isCancelled())
			return;
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
	 *            Allow only friends to click buttons and leavers inside a plot.
	 */
	public static void onButtonPush(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		Block block = event.getClickedBlock();
		if (!block.getType().equals(Material.STONE_BUTTON)
				&& !block.getType().equals(Material.LEVER))
			return;
		Plot plot = findPlotAt(block.getLocation());
		if (plot == null)
			return;
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
	 *         Get plot from id.
	 */
	public static Plot getPlotById(int id) {
		for (Plot plot : Lostshard.getRegistry().getPlots())
			if (plot.getId() == id)
				return plot;
		return null;
	}

	/**
	 * @param event
	 * 
	 *            Display plot enter message.
	 */
	public static void onPlotEnter(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;
		if (event.getTo().getBlock() == event.getFrom().getBlock())
			return;
		Player player = event.getPlayer();
		Plot fromPlot = PlotHandler.findPlotAt(event.getFrom().getBlock()
				.getLocation());
		Plot toPlot = PlotHandler.findPlotAt(event.getTo().getBlock()
				.getLocation());
		if (fromPlot == null && toPlot != null) {
			// must be entering a plot
			player.sendMessage(ChatColor.GRAY + "You have entered "
					+ toPlot.getName());
		} else if (toPlot == null && fromPlot != null) {
			// must be leaving a plot
			player.sendMessage(ChatColor.GRAY + "You have left "
					+ fromPlot.getName());
		} else if (fromPlot != null && toPlot != null && fromPlot != toPlot) {
			// must be moving from one plot to another
			player.sendMessage(ChatColor.GRAY + "You have left "
					+ fromPlot.getName() + " and entered " + toPlot.getName());
		}
	}

	/**
	 * @param event
	 * 
	 *            On bucket empty in plot.
	 */
	public static void onBuckitEmpty(PlayerBucketEmptyEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlockClicked().getLocation());
		if (plot == null)
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player, "Cannot spill water or lava here, "
					+ plot.getName() + " is protected.");
		}
	}

	/**
	 * @param event
	 * 
	 *            On bucket fill in plot.
	 */
	public static void onBuckitFill(PlayerBucketFillEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		Plot plot = findPlotAt(event.getBlockClicked().getLocation());
		if (plot == null)
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player, "Cannot fill water or lava here, "
					+ plot.getName() + " is protected.");
		}
	}

	/**
	 * @param event
	 * 
	 *            Prevent snow ice and other things from fading.
	 */
	public static void onBlockFade(BlockFadeEvent event) {
		if (event.isCancelled())
			return;
		Block block = event.getBlock();
		Plot plot = PlotHandler.findPlotAt(block.getLocation());
		if (plot != null) {
			if (plot.isProtected()) {
				event.setCancelled(true);
			}
		}
	}

	/**
	 * @param event
	 * 
	 *            Prevent flying machines to destroy plots.
	 */
	public static void onPistonExtend(BlockPistonExtendEvent event) {
		if (event.isCancelled())
			return;
		for (Block block : event.getBlocks()) {
			if (PlotHandler.findPlotAt(block.getRelative(event.getDirection())
					.getLocation()) != PlotHandler.findPlotAt(block
					.getLocation())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	/**
	 * @param event
	 * 
	 *            Prevent explosions for destroy plots.
	 */
	public static void onBlockExplode(EntityExplodeEvent event) {
        List<Block> destroyed = event.blockList();
        Iterator<Block> it = destroyed.iterator();
        while (it.hasNext()) {
            Block block = it.next();
            Plot plot = findPlotAt(block.getLocation());
            if (plot != null && !plot.isAllowExplosions())
                it.remove();
        }
	}
	
	/**
	 * @param event
	 * 
	 *            Prevent players from destroying Armor stands and ItemFrames in plots.
	 */
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        Entity damager = event.getDamager();
        
        Player player = null;
        
        if(damager instanceof Player)
        	player = (Player) damager;

        Plot plot = findPlotAt(entity.getLocation());
        
        if(player != null && plot != null && plot.isAllowedToBuild(player))
        	return;
        
        if (entity instanceof ItemFrame) {
        	if (plot != null) {
                event.setCancelled(true);
                
                if(player != null)
                	Output.simpleError(player, "Cannot destroy item frame here "+plot.getName()+" is protected.");
                
                return;
            }
        } else if (entity instanceof ArmorStand) {
        	if (plot != null) {
                event.setCancelled(true); // Set velocity downwards to minimize armor stand movement
                entity.setVelocity(new Vector(0, -100, 0));
                
                if(player != null)
                	Output.simpleError(player, "Cannot destroy armor stands here "+plot.getName()+" is protected.");
                
                return;
            }
        }
    }
    
    public void onHangingDestory(HangingBreakEvent event) {
    	Plot plot = findPlotAt(event.getEntity().getLocation());
    	
    	Player player = null;
    	if(event instanceof HangingBreakByEntityEvent) {
    		HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent) event;
			if(entityEvent.getRemover() instanceof Player)
				player = (Player) entityEvent.getRemover();
    	}
    	
        if(player != null && plot != null && plot.isAllowedToBuild(player))
        	return;
    	
        if (plot != null) {
            event.setCancelled(true);
            
            if(player != null)
            	Output.simpleError(player, "Cannot destroy painting here "+plot.getName()+" is protected.");
            
            return;
        }
        
    }

}
