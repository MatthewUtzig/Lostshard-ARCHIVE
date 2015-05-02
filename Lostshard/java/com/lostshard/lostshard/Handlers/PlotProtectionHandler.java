package com.lostshard.lostshard.Handlers;

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
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.lostshard.lostshard.Events.EventManager;
import com.lostshard.lostshard.Events.PlotProtectEvent;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Objects.Plot.PlotCapturePoint;
import com.lostshard.lostshard.Objects.Plot.PlotUpgrade;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Title;

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
		Player player = event.getPlayer();
		Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		if (!plot.isAllowedToBuild(player)) {
			PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			event.setCancelled(true);
			Output.simpleError(player,
					"can't break blocks here, " + plot.getName()
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
		Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player,
					"can't place blocks here, " + plot.getName()
							+ " is protected.");
		}
	}

	/**
	 * @param event
	 * 
	 *            Prevent blocks from burning inside a plot.
	 */
	public static void burnBlockInPlot(BlockBurnEvent event) {
		if (event.isCancelled())
			return;
		Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
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
	 *            Allow only friends of the plot to ignite blocks.
	 */
	public static void igniteBlockInPlot(BlockIgniteEvent event) {
		if (event.isCancelled())
			return;
		Plot plot = ptm.findPlotAt(event.getBlock().getLocation());
		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (event.getIgnitingEntity() instanceof Player) {
			Player player = event.getPlayer();
			if (!plot.isAllowedToBuild(player)) {
				event.setCancelled(true);
				Output.simpleError(player,
						"can't ignite blocks here, " + plot.getName()
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
		Plot toPlot = ptm.findPlotAt(event.getBlock().getLocation());

		// Check if there are a plot.
		if (toPlot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, toPlot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		// Check if the plot is protected
		if (!toPlot.isProtected())
			return;
		Plot fromPlot = ptm.findPlotAt(event.getBlock().getLocation());
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
		Plot plot = ptm.findPlotAt(block.getLocation());
		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (!plot.isPrivatePlot())
			return;
		if (plot.isAllowedToInteract(event.getPlayer()))
			return;
		event.setCancelled(true);
		Player player = event.getPlayer();
		Output.simpleError(player, "can't click button in \"" + plot.getName()
				+ "\" is protected.");
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
		Plot fromPlot = ptm
				.findPlotAt(event.getFrom().getBlock().getLocation());
		Plot toPlot = ptm.findPlotAt(event.getTo().getBlock().getLocation());
		if (fromPlot == null && toPlot != null) {
			if (toPlot.isTitleEntrence()) {
				if (toPlot instanceof PlotCapturePoint) {
					Title.sendTitle(player, 10, 20, 10,
							ChatColor.GOLD + toPlot.getName(), ChatColor.RED
									+ "Hostile territory");
				} else if (toPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, ChatColor.GREEN
							+ toPlot.getName(), "");
				else {
					PseudoPlayer owner = pm.getPlayer(toPlot.getOwner());
					Title.sendTitle(player, 10, 20, 10, (owner.isCriminal()
							|| owner.isMurderer() ? ChatColor.RED
							: ChatColor.BLUE) + toPlot.getName(), "");
				}
			} else {
				if (toPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, "", ChatColor.GREEN
							+ toPlot.getName());
				else {
					PseudoPlayer owner = pm.getPlayer(toPlot.getOwner());
					Title.sendTitle(player, 10, 20, 10, "", (owner.isCriminal()
							|| owner.isMurderer() ? ChatColor.RED
							: ChatColor.BLUE) + toPlot.getName());
				}
			}
			// must be entering a plot
			// player.sendMessage(ChatColor.GRAY + "You have entered "
			// + toPlot.getName());
		} else if (toPlot == null && fromPlot != null) {
			// must be leaving a plot
			if (fromPlot.isTitleEntrence()) {
				if (fromPlot instanceof PlotCapturePoint) {
					Title.sendTitle(player, 10, 20, 10, ChatColor.GOLD
							+ "You have left " + fromPlot.getName(),
							ChatColor.RED + "Hostile territory");
				} else if (fromPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, ChatColor.GREEN
							+ "You have left " + fromPlot.getName(), "");
				else {
					PseudoPlayer owner = pm.getPlayer(fromPlot.getOwner());
					Title.sendTitle(
							player,
							10,
							20,
							10,
							(owner.isCriminal() || owner.isMurderer() ? ChatColor.RED
									: ChatColor.BLUE)
									+ "You have left " + fromPlot.getName(), "");
				}
			} else {
				if (fromPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, "", ChatColor.GREEN
							+ "You have left " + fromPlot.getName());
				else {
					PseudoPlayer owner = pm.getPlayer(fromPlot.getOwner());
					Title.sendTitle(
							player,
							10,
							20,
							10,
							"",
							(owner.isCriminal() || owner.isMurderer() ? ChatColor.RED
									: ChatColor.BLUE)
									+ "You have left " + fromPlot.getName());
				}
			}
			// player.sendMessage(ChatColor.GRAY + "You have left "
			// + fromPlot.getName());
		} else if (fromPlot != null && toPlot != null && fromPlot != toPlot) {
			// must be moving from one plot to another
			if (fromPlot.isTitleEntrence()) {
				if (toPlot instanceof PlotCapturePoint) {
					Title.sendTitle(player, 10, 20, 10,
							ChatColor.GOLD + toPlot.getName(), ChatColor.RED
									+ "Hostile territory");
				} else if (toPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, ChatColor.GREEN
							+ toPlot.getName(), "");
				else {
					PseudoPlayer owner = pm.getPlayer(toPlot.getOwner());
					Title.sendTitle(player, 10, 20, 10, (owner.isCriminal()
							|| owner.isMurderer() ? ChatColor.RED
							: ChatColor.BLUE) + toPlot.getName(), "");
				}
			} else {
				if (toPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, "", ChatColor.GREEN
							+ toPlot.getName());
				else {
					PseudoPlayer owner = pm.getPlayer(toPlot.getOwner());
					Title.sendTitle(player, 10, 20, 10, "", (owner.isCriminal()
							|| owner.isMurderer() ? ChatColor.RED
							: ChatColor.BLUE) + toPlot.getName());
				}
			}
			// player.sendMessage(ChatColor.GRAY + "You have left "
			// + fromPlot.getName() + " and entered " + toPlot.getName());
		}
	}

	public static void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled())
			return;
		if (event.getTo().getBlock() == event.getFrom().getBlock())
			return;
		Player player = event.getPlayer();
		Plot fromPlot = ptm.findPlotAt(event.getFrom());
		Plot toPlot = ptm.findPlotAt(event.getTo());
		if (fromPlot == null && toPlot != null) {
			if (toPlot.isTitleEntrence()) {
				if (toPlot instanceof PlotCapturePoint) {
					Title.sendTitle(player, 10, 20, 10,
							ChatColor.GOLD + toPlot.getName(), ChatColor.RED
									+ "Hostile territory");
				} else if (toPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, ChatColor.GREEN
							+ toPlot.getName(), "");
				else {
					PseudoPlayer owner = pm.getPlayer(toPlot.getOwner());
					Title.sendTitle(player, 10, 20, 10, (owner.isCriminal()
							|| owner.isMurderer() ? ChatColor.RED
							: ChatColor.BLUE) + toPlot.getName(), "");
				}
			} else
				// must be entering a plot
				player.sendMessage(ChatColor.GRAY + "You have entered "
						+ toPlot.getName());
		} else if (toPlot == null && fromPlot != null) {
			// must be leaving a plot
			if (fromPlot.isTitleEntrence()) {
				if (fromPlot instanceof PlotCapturePoint) {
					Title.sendTitle(player, 10, 20, 10, ChatColor.GOLD
							+ "You have left " + fromPlot.getName(),
							ChatColor.RED + "Hostile territory");
				} else if (fromPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, ChatColor.GREEN
							+ "You have left " + fromPlot.getName(), "");
				else {
					PseudoPlayer owner = pm.getPlayer(fromPlot.getOwner());
					Title.sendTitle(
							player,
							10,
							20,
							10,
							(owner.isCriminal() || owner.isMurderer() ? ChatColor.RED
									: ChatColor.BLUE)
									+ "You have left " + fromPlot.getName(), "");
				}
			} else
				player.sendMessage(ChatColor.GRAY + "You have left "
						+ fromPlot.getName());
		} else if (fromPlot != null && toPlot != null && fromPlot != toPlot) {
			// must be moving from one plot to another
			if (fromPlot.isTitleEntrence()) {
				if (toPlot instanceof PlotCapturePoint) {
					Title.sendTitle(player, 10, 20, 10,
							ChatColor.GOLD + toPlot.getName(), ChatColor.RED
									+ "Hostile territory");
				} else if (toPlot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
					Title.sendTitle(player, 10, 20, 10, ChatColor.GREEN
							+ toPlot.getName(), "");
				else {
					PseudoPlayer owner = pm.getPlayer(toPlot.getOwner());
					Title.sendTitle(player, 10, 20, 10, (owner.isCriminal()
							|| owner.isMurderer() ? ChatColor.RED
							: ChatColor.BLUE) + toPlot.getName(), "");
				}
			} else
				player.sendMessage(ChatColor.GRAY + "You have left "
						+ fromPlot.getName() + " and entered "
						+ toPlot.getName());
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
		Plot plot = ptm.findPlotAt(event.getBlockClicked().getLocation());
		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player, "can't spill water or lava here, "
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
		Plot plot = ptm.findPlotAt(event.getBlockClicked().getLocation());
		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (!plot.isAllowedToBuild(player)) {
			event.setCancelled(true);
			Output.simpleError(player,
					"can't fill water or lava here, " + plot.getName()
							+ " is protected.");
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
		Plot plot = ptm.findPlotAt(block.getLocation());
		if (plot != null) {
			PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
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
			if (ptm.findPlotAt(block.getRelative(event.getDirection())
					.getLocation()) != ptm.findPlotAt(block.getLocation())) {
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
		boolean pro = false;
		for (Block b : destroyed) {
			Plot plot = ptm.findPlotAt(b.getLocation());
			if (plot == null)
				continue;
			PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
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
	 *            Prevent players from destroying Armor stands and ItemFrames in
	 *            plots.
	 */
	public static void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();

		Entity damager = event.getDamager();

		Player player = null;

		if (damager instanceof Player)
			player = (Player) damager;

		Plot plot = ptm.findPlotAt(entity.getLocation());

		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (entity instanceof ItemFrame) {
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "can't destroy item frame here "
							+ plot.getName() + " is protected.");

				return;
			}
		} else if (entity instanceof ArmorStand) {
			if (plot != null) {
				event.setCancelled(true); // Set velocity downwards to minimize
											// armor stand movement
				entity.setVelocity(new Vector(0, -100, 0));

				if (player != null)
					Output.simpleError(player,
							"can't destroy armor stands here " + plot.getName()
									+ " is protected.");

				return;
			}
		}
	}

	public static void onHangingDestory(HangingBreakEvent event) {
		Plot plot = ptm.findPlotAt(event.getEntity().getLocation());

		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;

		Entity entity = event.getEntity();

		Player player = null;
		if (event instanceof HangingBreakByEntityEvent) {
			HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent) event;
			if (entityEvent.getRemover() instanceof Player)
				player = (Player) entityEvent.getRemover();
		}

		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (!(event.getCause().equals(RemoveCause.EXPLOSION) || event
				.getCause().equals(RemoveCause.ENTITY)))
			return;
		if (entity instanceof ItemFrame) {
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "can't destroy item frame here "
							+ plot.getName() + " is protected.");

				return;
			}
		} else if (entity instanceof Painting) {
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "can't destroy paintings here "
							+ plot.getName() + " is protected.");

				return;
			}
		}
	}

	public static void onHangingPlace(HangingPlaceEvent event) {
		Plot plot = ptm.findPlotAt(event.getEntity().getLocation());

		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;

		Entity entity = event.getEntity();

		Player player = event.getPlayer();

		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (entity instanceof ItemFrame) {
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "can't place item frame here "
							+ plot.getName() + " is protected.");

				return;
			}
		} else if (entity instanceof Painting) {
			if (plot != null) {
				event.setCancelled(true);

				if (player != null)
					Output.simpleError(player, "can't place paintings here "
							+ plot.getName() + " is protected.");

				return;
			}
		}
	}

	public static void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			ItemStack itemInHand = event.getPlayer().getItemInHand();
			if ((itemInHand.getType().equals(Material.WOOD_HOE)
					|| itemInHand.getType().equals(Material.STONE_HOE)
					|| itemInHand.getType().equals(Material.IRON_HOE)
					|| itemInHand.getType().equals(Material.DIAMOND_HOE) || itemInHand
					.getType().equals(Material.GOLD_HOE))) {
				if (event.getClickedBlock().equals(Material.DIRT)) {
					Plot plot = ptm.findPlotAt(event.getClickedBlock()
							.getLocation());
					if (plot != null
							&& !plot.isAllowedToBuild(event.getPlayer())) {
						Output.simpleError(event.getPlayer(),
								"You can't hoe soile in \"" + plot.getName()
										+ "\" is protected.");
						event.setCancelled(true);
						return;
					}
				}
			}
			Block block = event.getClickedBlock().getRelative(
					event.getBlockFace());

			Plot plot = ptm.findPlotAt(block.getLocation());

			if (plot == null)
				return;
			PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;

			Player player = event.getPlayer();

			if (plot != null && plot.isAllowedToBuild(player))
				return;

			if (player.getItemInHand().getType().equals(Material.ARMOR_STAND)) {
				if (plot != null) {
					event.setCancelled(true);

					if (player != null)
						Output.simpleError(
								player,
								"can't place armor stand here "
										+ plot.getName() + " is protected.");
					return;
				}
			}
		}

	}

	public static void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Plot plot = ptm.findPlotAt(event.getPlayer().getLocation());

		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;

		Player player = event.getPlayer();

		Entity entity = event.getRightClicked();

		if (player != null && plot != null && plot.isAllowedToBuild(player))
			return;

		if (entity instanceof ItemFrame) {
			if (plot != null) {
				event.setCancelled(true);

				// if(player != null)
				// Output.simpleError(player,
				// "can't interact with item frame here "+plot.getName()+" is protected.");

				return;
			}
		} else if (entity instanceof ArmorStand) {
			if (plot != null) {
				event.setCancelled(true);

				// if(player != null)
				// Output.simpleError(player,
				// "can't interact with armor stand here "+plot.getName()+" is protected.");

				return;
			}
		}
	}

	public static void onEntityChangeBlock(EntityChangeBlockEvent event) {
		Plot plot = ptm.findPlotAt(event.getBlock().getLocation());

		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (plot.isAllowExplosions())
			return;
		event.setCancelled(true);
	}

	public static void onMonsterSpawn(EntitySpawnEvent event) {
		if (event.getEntity() instanceof Monster) {
			Plot plot = ptm.findPlotAt(event.getLocation());
			if (plot == null)
				return;
			PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			if (!plot.isUpgrade(PlotUpgrade.DUNGEON))
				event.setCancelled(true);
		}
	}

	public static void onPlayerBedEnter(PlayerBedEnterEvent event) {
		Plot plot = ptm.findPlotAt(event.getBed().getLocation());
		if (plot == null || !plot.isUpgrade(PlotUpgrade.TOWN)) {
			Output.simpleError(event.getPlayer(),
					"You are only able to set your spawn in a town.");
			event.getPlayer().setBedSpawnLocation(null);
		} else {
			PseudoPlayer pPlayer = pm.getPlayer(event.getPlayer());
			PseudoPlayer plotPlayer = pm.getPlayer(plot.getOwner());
			if ((plotPlayer.isMurderer() == pPlayer.isMurderer()
					|| plotPlayer.isMurderer() == pPlayer.isCriminal()
					|| plotPlayer.isCriminal() == plotPlayer.isMurderer() || plot
						.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))) {
				Output.positiveMessage(event.getPlayer(),
						"You have set your spawn.");
				event.getPlayer().setBedSpawnLocation(
						event.getBed().getLocation());
			} else
				Output.simpleError(event.getPlayer(),
						"You are not in the same alignment at the town owner.");
		}
	}

	public static void onPlayerSpawn(PlayerRespawnEvent event) {
		PseudoPlayer pPlayer = pm.getPlayer(event.getPlayer());
		if (event.isBedSpawn()) {
			Plot plot = ptm.findPlotAt(event.getRespawnLocation());
			if (plot == null)
				return;
			PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
			EventManager.callEvent(protectEvent);
			if (protectEvent.isCancelled())
				return;
			PseudoPlayer plotPlayer = pm.getPlayer(plot.getOwner());
			if (plot == null || !plot.isUpgrade(PlotUpgrade.TOWN)) {
				event.getPlayer().setBedSpawnLocation(null);
				event.setRespawnLocation(pPlayer.getSpawn());
			} else if (!(plotPlayer.isMurderer() == pPlayer.isMurderer()
					|| plotPlayer.isMurderer() == pPlayer.isCriminal()
					|| plotPlayer.isCriminal() == plotPlayer.isMurderer() || plot
						.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))) {
				Output.simpleError(event.getPlayer(),
						"You are not in the same alignment as the town owner.");
				event.setRespawnLocation(pPlayer.getSpawn());
			}
		} else
			event.setRespawnLocation(pPlayer.getSpawn());
	}

	public static void onPlayerJoin(PlayerJoinEvent event) {
		Plot plot = ptm.findPlotAt(event.getPlayer().getLocation());
		if (plot == null)
			return;
		PlotProtectEvent protectEvent = new PlotProtectEvent(event, plot);
		EventManager.callEvent(protectEvent);
		if (protectEvent.isCancelled())
			return;
		if (plot != null && plot.isUpgrade(PlotUpgrade.AUTOKICK)
				&& !plot.isFriendOrAbove(event.getPlayer())) {
			event.getPlayer().teleport(
					event.getPlayer().getLocation().getWorld()
							.getHighestBlockAt(event.getPlayer().getLocation())
							.getLocation());
			Output.simpleError(event.getPlayer(), "You have been kicked from "
					+ plot.getName() + ".");
		}
	}

}