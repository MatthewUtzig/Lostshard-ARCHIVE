package com.lostshard.Lostshard.Handlers;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.lostshard.Lostshard.Events.EventManager;
import com.lostshard.Lostshard.Events.PlotProtectEvent;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.Plot.PlotUpgrade;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;

import me.olivervscreeper.networkutilities.Message;
import me.olivervscreeper.networkutilities.MessageDisplay;

public class PlotProtectionHandler {

	static PlotManager ptm = PlotManager.getManager();

	static PlayerManager pm = PlayerManager.getManager();

	/**
	 * @param event
	 *
	 *            Allow only friends of the plot to break blocks.
	 */
	public static void breakeBlockInPlot(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		final Player player = event.getPlayer();
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		if (!plot.isAllowedToBuild(player) || (ItemUtils.isRedstone(event.getBlock().getType()) && !plot.isCoownerOrAbove(player))) {
			final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			event.setCancelled(true);
			Output.simpleError(player, "Can't break blocks here, " + plot.getName() + " is protected.");
		}
	}
	
	public static void BlockForm(BlockFormEvent event) {
		if (event.isCancelled())
			return;
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		event.setCancelled(true);
	}
	
	public static void BlockSrpead(BlockSpreadEvent event) {
		if (event.isCancelled())
			return;
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		event.setCancelled(true);
	}
	
	public static void leavesDecay(LeavesDecayEvent event) {
		if (event.isCancelled())
			return;
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		event.setCancelled(true);
	}

	/**
	 * @param event
	 *
	 *            Prevent blocks from burning inside a plot.
	 */
	public static void burnBlockInPlot(BlockBurnEvent event) {
		if (event.isCancelled())
			return;
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (plot == null || !plot.isProtected())
			return;
		else
			event.setCancelled(true);
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
		final Plot toPlot = ptm.findPlotAt(event.getToBlock().getLocation());

		// Check if there are a plot.
		if (toPlot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, toPlot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		// Check if the plot is protected
		if (!toPlot.isProtected())
			return;
		final Plot fromPlot = ptm.findPlotAt(event.getBlock().getLocation());
		// Check if its flowing from the same plot to same plot.
		if (fromPlot == toPlot)
			return;
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
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (event.getIgnitingEntity() instanceof Player) {
			final Player player = event.getPlayer();
			if (!plot.isAllowedToBuild(player)) {
				event.setCancelled(true);
				Output.simpleError(player, "Can't ignite blocks here, " + plot.getName() + " is protected.");
			}
		} else
			event.setCancelled(true);
	}

	/**
	 * @param event
	 *
	 *            Prevent explosions for destroy plots.
	 */
	public static void onBlockExplode(EntityExplodeEvent event) {
		final List<Block> destroyed = event.blockList();
		boolean pro = false;
		for (final Block b : destroyed) {
			final Plot plot = ptm.findPlotAt(b.getLocation());
			if (plot == null)
				continue;
			final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			if (!plot.isAllowExplosions()) {
				event.setCancelled(true);
				pro = true;
				break;
			}
		}
		if (pro)
			event.blockList().clear();
	}

	/**
	 * @param event
	 *
	 *            Prevent snow ice and other things from fading.
	 */
	public static void onBlockFade(BlockFadeEvent event) {
		if (event.isCancelled())
			return;
		final Block block = event.getBlock();
		final Plot plot = ptm.findPlotAt(block.getLocation());
		if (plot != null) {
			final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			if (plot.isProtected())
				event.setCancelled(true);
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
		final Player player = event.getPlayer();
		final Plot plot = ptm.findPlotAt(event.getBlockClicked().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player, "Can't spill water or lava here, " + plot.getName() + " is protected.");
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
		final Player player = event.getPlayer();
		final Plot plot = ptm.findPlotAt(event.getBlockClicked().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player, "Can't fill water or lava here, " + plot.getName() + " is protected.");
		}
	}

	/**
	 * @param event
	 *
	 *            Allow only friends to click buttons and leavers inside a plot.
	 */
	public static void onButtonPush(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		final Block block = event.getClickedBlock();
		if (!block.getType().equals(Material.STONE_BUTTON) && !block.getType().equals(Material.LEVER) && 
				!block.getType().equals(Material.DRAGON_EGG))
			return;
		final Plot plot = ptm.findPlotAt(block.getLocation());
		if (plot == null) {
			if (block.getType().equals(Material.DRAGON_EGG)) {
				block.breakNaturally();
				event.setCancelled(true);
			}
			return;
		}
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		final Player player = event.getPlayer();
		if(block.getType().equals(Material.DRAGON_EGG)) {
			event.setCancelled(true);
			if (protectEvent.isCancelled() ) {
				block.breakNaturally();
				return;
			}
			if (!plot.isAllowedToBuild(player)) {
				Output.simpleError(player, "Can't interact with dragon egg, \"" + plot.getName() + "\" is protected.");
				return;
			}
			block.breakNaturally();
			return;
		}
		if (protectEvent.isCancelled())
			return;
		if (!plot.isPrivatePlot())
			return;
		if (plot.isAllowedToInteract(event.getPlayer()))
			return;
		event.setCancelled(true);
		if(block.getType().equals(Material.STONE_BUTTON))
			Output.simpleError(player, "Can't click button, \"" + plot.getName() + "\" is protected.");
		else if(block.getType().equals(Material.LEVER))
			Output.simpleError(player, "Can't click lever, \"" + plot.getName() + "\" is protected.");
	}

	public static void onEntityChangeBlock(EntityChangeBlockEvent event) {
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());

		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (plot.isAllowExplosions())
			return;
		event.setCancelled(true);
	}

	/**
	 * @param event
	 *
	 *            Prevent players from destroying Armor stands and ItemFrames in
	 *            plots.
	 */
	public static void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		final Entity entity = event.getEntity();

		final Entity damager = event.getDamager();

		Player player = null;

		if (damager instanceof Player)
			player = (Player) damager;

		final Plot plot = ptm.findPlotAt(entity.getLocation());

		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (entity instanceof ItemFrame) {
			event.setCancelled(true);

			if (player != null)
				Output.simpleError(player, "Can't destroy item frame here " + plot.getName() + " is protected.");

			return;
		} else if (entity instanceof ArmorStand) {
			event.setCancelled(true); // Set velocity downwards to minimize
			// armor stand movement
			entity.setVelocity(new Vector(0, -100, 0));

			if (player != null)
				Output.simpleError(player, "Can't destroy armor stands here " + plot.getName() + " is protected.");

			return;
		} 
	}
	
	public static void VehicleDestroy(VehicleDestroyEvent event) {
		final Plot plot = ptm.findPlotAt(event.getVehicle().getLocation());

		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		Player player = null;
		if(event.getAttacker() instanceof Player)
			player = (Player) event.getAttacker();
		
		if(player != null && plot.isFriendOrAbove(player))
			return;
		
		if(player != null)
			Output.simpleError(player, "Can't destroy vehicles here " + plot.getName() + " is protected.");
		event.setCancelled(true);
	}

	public static void VehicleCreate(VehicleCreateEvent event) {
		
	}
	
	public static void VehicleEnter(VehicleEnterEvent event) {
		final Plot plot = ptm.findPlotAt(event.getVehicle().getLocation());

		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		Player player = null;
		if(event.getEntered() instanceof Player)
			player = (Player) event.getEntered();
		
		if((player != null && plot.isFriendOrAbove(player)) || !plot.isPrivatePlot())
			return;
		
		if(player != null)
			Output.simpleError(player, "Can't enter vehicles here " + plot.getName() + " is protected.");
		event.setCancelled(true);
	}
	
	
	public static void onHangingDestory(HangingBreakEvent event) {
		final Plot plot = ptm.findPlotAt(event.getEntity().getLocation());

		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;

		final Entity entity = event.getEntity();

		Player player = null;
		if (event instanceof HangingBreakByEntityEvent) {
			final HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent) event;
			if (entityEvent.getRemover() instanceof Player)
				player = (Player) entityEvent.getRemover();
		}

		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (!(event.getCause().equals(RemoveCause.EXPLOSION) || event.getCause().equals(RemoveCause.ENTITY)))
			return;
		if (entity instanceof ItemFrame) {
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "Can't destroy item frame here " + plot.getName() + " is protected.");

				return;
			}
		} else if (entity instanceof Painting)
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "Can't destroy paintings here " + plot.getName() + " is protected.");

				return;
			}
	}

	public static void onHangingPlace(HangingPlaceEvent event) {
		final Plot plot = ptm.findPlotAt(event.getEntity().getLocation());

		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;

		final Entity entity = event.getEntity();

		final Player player = event.getPlayer();

		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (entity instanceof ItemFrame) {
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "Can't place item frame here " + plot.getName() + " is protected.");

				return;
			}
		} else if (entity instanceof Painting)
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "Can't place paintings here " + plot.getName() + " is protected.");

				return;
			}
	}

	public static void onMonsterSpawn(EntitySpawnEvent event) {
		if (event.getEntity() instanceof Monster) {
			final Plot plot = ptm.findPlotAt(event.getLocation());
			if (plot == null)
				return;
			final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			if (!plot.isUpgrade(PlotUpgrade.DUNGEON))
				event.setCancelled(true);
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
		for (final Block block : event.getBlocks())
			if (ptm.findPlotAt(block.getRelative(event.getDirection()).getLocation()) != ptm
					.findPlotAt(block.getLocation())) {
				event.setCancelled(true);
				return;
			}
	}

	public static void onPlayerBedEnter(PlayerBedEnterEvent event) {
		final Plot plot = ptm.findPlotAt(event.getBed().getLocation());
		if (plot == null || !plot.isUpgrade(PlotUpgrade.TOWN)) {
			Output.simpleError(event.getPlayer(), "You are only able to set your spawn in a town.");
			event.getPlayer().setBedSpawnLocation(null);
		} else {
			final PseudoPlayer pPlayer = pm.getPlayer(event.getPlayer());
			if (pm.isCriminal(plot.getOwner()) == !pPlayer.isLawfull() || plot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT)) {
				Output.positiveMessage(event.getPlayer(), "You have set your spawn.");
				event.getPlayer().setBedSpawnLocation(event.getBed().getLocation());
			} else
				Output.simpleError(event.getPlayer(), "You are not in the same alignment at the town owner.");
		}
	}

	public static void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			final ItemStack itemInHand = event.getPlayer().getItemInHand();
			if (itemInHand.getType().equals(Material.WOOD_HOE) || itemInHand.getType().equals(Material.STONE_HOE)
					|| itemInHand.getType().equals(Material.IRON_HOE)
					|| itemInHand.getType().equals(Material.DIAMOND_HOE)
					|| itemInHand.getType().equals(Material.GOLD_HOE))
				if (event.getClickedBlock().equals(Material.DIRT)) {
					final Plot plot = ptm.findPlotAt(event.getClickedBlock().getLocation());
					if (plot != null && !plot.isAllowedToBuild(event.getPlayer())) {
						Output.simpleError(event.getPlayer(),
								"You can't hoe soile in \"" + plot.getName() + "\" is protected.");
						event.setCancelled(true);
						return;
					}
				}
			final Block block = event.getClickedBlock().getRelative(event.getBlockFace());

			final Plot plot = ptm.findPlotAt(block.getLocation());

			if (plot == null)
				return;
			final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;

			final Player player = event.getPlayer();

			if (plot != null && plot.isAllowedToBuild(player))
				return;

			if (player.getItemInHand().getType().equals(Material.ARMOR_STAND))
				if (plot != null) {
					event.setCancelled(true);

					if (player != null)
						Output.simpleError(player, "Can't place armor stand here " + plot.getName() + " is protected.");
					return;
				}
		}

	}

	public static void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		final Plot plot = ptm.findPlotAt(event.getPlayer().getLocation());

		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;

		final Player player = event.getPlayer();

		final Entity entity = event.getRightClicked();

		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (entity instanceof ItemFrame) {
			if (plot != null) {
				event.setCancelled(true);

				// if(player != null)
				// Output.simpleError(player,
				// "can't interact with item frame here "+plot.getName()+" is
				// protected.");

				return;
			}
		} else if (entity instanceof ArmorStand)
			if (plot != null) {
				event.setCancelled(true);

				// if(player != null)
				// Output.simpleError(player,
				// "can't interact with armor stand here "+plot.getName()+" is
				// protected.");

				return;
			}
	}

	public static void onPlayerJoin(PlayerJoinEvent event) {
		final Plot plot = ptm.findPlotAt(event.getPlayer().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (plot != null && plot.isUpgrade(PlotUpgrade.AUTOKICK) && !plot.isFriendOrAbove(event.getPlayer())) {
			event.getPlayer().teleport(event.getPlayer().getLocation().getWorld()
					.getHighestBlockAt(event.getPlayer().getLocation()).getLocation());
			Output.simpleError(event.getPlayer(), "You have been kicked from " + plot.getName() + ".");
		}
	}

	public static void onPlayerSpawn(PlayerRespawnEvent event) {
		final PseudoPlayer pPlayer = pm.getPlayer(event.getPlayer());
		if (event.isBedSpawn()) {
			final Plot plot = ptm.findPlotAt(event.getRespawnLocation());
			if (plot == null)
				return;
			final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			if (plot == null || !plot.isUpgrade(PlotUpgrade.TOWN)) {
				event.getPlayer().setBedSpawnLocation(null);
				event.setRespawnLocation(pPlayer.getSpawn());
			} else
				if (!(pm.isCriminal(plot.getOwner()) != pPlayer.isLawfull() || plot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))) {
					Output.simpleError(event.getPlayer(), "You are not in the same alignment as the town owner.");
					event.setRespawnLocation(pPlayer.getSpawn());
			}
		} else
			event.setRespawnLocation(pPlayer.getSpawn());
	}

	public static void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled())
			return;
		if (event.getTo().getBlock() == event.getFrom().getBlock())
			return;
		final Player player = event.getPlayer();
		final Plot fromPlot = ptm.findPlotAt(event.getFrom());
		final Plot toPlot = ptm.findPlotAt(event.getTo());
		
		if (toPlot == fromPlot)
			return;
		
		Message m = new Message(Message.BLANK);
		
		m.addRecipient(player);
		
		
		// Left a Plot
		if(fromPlot != null && toPlot == null) {
			if(fromPlot.isCapturepoint()) {
				m.send(ChatColor.GOLD + "You have left "+fromPlot.getName(), MessageDisplay.TITLE);
				m.send(ChatColor.RED + "Hostile territory", MessageDisplay.SUBTITLE);
			} else if (fromPlot.isTitleEntrence()){
				m.send(fromPlot.getDisplayName(), MessageDisplay.TITLE);
			} else {
				m.send(ChatColor.YELLOW+"You have left \""+fromPlot.getDisplayName()+ChatColor.YELLOW+"\"", MessageDisplay.ACTIONBAR);
			}	
		} 
		// Entered a Plot
		else if(fromPlot == null && toPlot != null) {
			if(toPlot.isCapturepoint()) {
				m.send(ChatColor.GOLD+toPlot.getName(), MessageDisplay.TITLE);
				m.send(ChatColor.RED + "Hostile territory", MessageDisplay.SUBTITLE);
			} else if (toPlot.isTitleEntrence()){
				m.send(toPlot.getDisplayName(), MessageDisplay.TITLE);
			} else {
				m.send(ChatColor.YELLOW+"You have entered \""+toPlot.getDisplayName()+ChatColor.YELLOW+"\"", MessageDisplay.ACTIONBAR);
			}	
		}
		// Left a Plot and entered a Plot
		else if(fromPlot != null && toPlot != null) {
			if(fromPlot.isCapturepoint()) {
				m.send(ChatColor.GOLD+toPlot.getName(), MessageDisplay.TITLE);
				m.send(ChatColor.RED + "Hostile territory", MessageDisplay.SUBTITLE);
			} else if (toPlot.isTitleEntrence()){
				m.send(toPlot.getDisplayName(), MessageDisplay.TITLE);
			} else {
				m.send(ChatColor.YELLOW+"You have left \""+fromPlot.getDisplayName()+ChatColor.YELLOW+"\" and entered \""+toPlot.getDisplayName()+ChatColor.YELLOW+"\"", MessageDisplay.ACTIONBAR);
			}	
		}
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
		final Player player = event.getPlayer();
		final Plot fromPlot = ptm.findPlotAt(event.getFrom().getBlock().getLocation());
		final Plot toPlot = ptm.findPlotAt(event.getTo().getBlock().getLocation());
		if (toPlot == fromPlot)
			return;
		
		Message m = new Message(Message.BLANK);
		
		m.addRecipient(player);
		
		
		// Left a Plot
		if(fromPlot != null && toPlot == null) {
			if(fromPlot.isCapturepoint()) {
				m.send(ChatColor.GOLD + "You have left "+fromPlot.getName(), MessageDisplay.TITLE);
				m.send(ChatColor.RED + "Hostile territory", MessageDisplay.SUBTITLE);
			} else if (fromPlot.isTitleEntrence()){
				m.send(fromPlot.getDisplayName(), MessageDisplay.TITLE);
			} else {
				m.send(ChatColor.YELLOW+"You have left \""+fromPlot.getDisplayName()+ChatColor.YELLOW+"\"", MessageDisplay.ACTIONBAR);
			}	
		} 
		// Entered a Plot
		else if(fromPlot == null && toPlot != null) {
			if(toPlot.isCapturepoint()) {
				m.send(ChatColor.GOLD+toPlot.getName(), MessageDisplay.TITLE);
				m.send(ChatColor.RED + "Hostile territory", MessageDisplay.SUBTITLE);
			} else if (toPlot.isTitleEntrence()){
				m.send(toPlot.getDisplayName(), MessageDisplay.TITLE);
			} else {
				m.send(ChatColor.YELLOW+"You have entered \""+toPlot.getDisplayName()+ChatColor.YELLOW+"\"", MessageDisplay.ACTIONBAR);
			}	
		}
		// Left a Plot and entered a Plot
		else if(fromPlot != null && toPlot != null) {
			if(toPlot.isCapturepoint()) {
				m.send(ChatColor.GOLD+toPlot.getName(), MessageDisplay.TITLE);
				m.send(ChatColor.RED + "Hostile territory", MessageDisplay.SUBTITLE);
			} else if (toPlot.isTitleEntrence()){
				m.send(toPlot.getDisplayName(), MessageDisplay.TITLE);
			} else {
				m.send(ChatColor.YELLOW+"You have left \""+fromPlot.getDisplayName()+ChatColor.YELLOW+"\" and entered \""+toPlot.getDisplayName()+ChatColor.YELLOW+ChatColor.YELLOW+"\"", MessageDisplay.ACTIONBAR);
			}	
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
		final Player player = event.getPlayer();
		final Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		final PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (!plot.isAllowedToBuild(player) || (ItemUtils.isRedstone(event.getBlock().getType()) && !plot.isCoownerOrAbove(player))) {
			event.setCancelled(true);
			Output.simpleError(player, "Can't place blocks here, " + plot.getName() + " is protected.");
		}
	}

}