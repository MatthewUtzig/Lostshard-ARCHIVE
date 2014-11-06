package com.lostshard.lostshard.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.HelpHandler;
import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.TabUtils;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 * @author Luciddream
 *
 */
public class PlotCommand implements CommandExecutor, TabCompleter {

	public PlotCommand(Lostshard plugin) {
		plugin.getCommand("plot").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("plot")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			if (args.length < 1) {
				Output.plotHelp(player);
				return true;
			}
			String plotCommand = args[0];
			if (plotCommand.equalsIgnoreCase("create"))
				createPlot(player, args);
			else if (plotCommand.equalsIgnoreCase("survey"))
				plotSurvey(player);
			else if (plotCommand.equalsIgnoreCase("info"))
				plotInfo(player);
			else if (plotCommand.equalsIgnoreCase("friend"))
				plotFriend(player, args);
			else if (plotCommand.equalsIgnoreCase("co-own")
					|| plotCommand.equalsIgnoreCase("coown")
					|| plotCommand.equalsIgnoreCase("co-owner")
					|| plotCommand.equalsIgnoreCase("coowner"))
				plotCoOwn(player, args);
			else if (plotCommand.equalsIgnoreCase("unfriend"))
				plotUnFriend(player, args);
			else if (plotCommand.equalsIgnoreCase("protect"))
				plotProtect(player);
			else if (plotCommand.equalsIgnoreCase("private"))
				plotPrivate(player);
			else if (plotCommand.equalsIgnoreCase("public"))
				plotPublic(player);
			else if (plotCommand.equalsIgnoreCase("expand"))
				plotExpand(player, args);
			else if (plotCommand.equalsIgnoreCase("deposit"))
				plotDeposit(player, args);
			else if (plotCommand.equalsIgnoreCase("withdraw"))
				plotWithdraw(player, args);
			else if (plotCommand.equalsIgnoreCase("test"))
				plotTestToggle(player);
			else if (plotCommand.equalsIgnoreCase("disband"))
				plotDisband(player);
			else if (plotCommand.equalsIgnoreCase("list"))
				plotList(player, args);
			else if (plotCommand.equalsIgnoreCase("upgrade")
					|| plotCommand.equalsIgnoreCase("upgrades"))
				plotUpgrade(player, args);
			else if (plotCommand.equalsIgnoreCase("downgrade"))
				plotDowngrade(player, args);
			else if (plotCommand.equalsIgnoreCase("transfer"))
				plotTransfer(player, args);
			else if (plotCommand.equalsIgnoreCase("shrink"))
				plotShrink(player, args);
			else if (plotCommand.equalsIgnoreCase("rename"))
				plotRename(player, args);
			else if (plotCommand.equalsIgnoreCase("friendbuild"))
				plotFriendBuildToggle(player);
			else if (plotCommand.equalsIgnoreCase("npc"))
				plotNPC(player, args);
			else if (plotCommand.equalsIgnoreCase("sell"))
				plotSell(player, args);
			else if (plotCommand.equalsIgnoreCase("buy"))
				plotBuy(player);
			else if (plotCommand.equalsIgnoreCase("unsell")
					|| plotCommand.equalsIgnoreCase("unlist")
					|| plotCommand.equalsIgnoreCase("un-sell"))
				plotUnSell(player);
			else if (plotCommand.equalsIgnoreCase("explosion"))
				plotExplosionToggle(player);
			else if (plotCommand.equalsIgnoreCase("pvp") && player.isOp())
				plotPvpToggle(player);
			else if (plotCommand.equalsIgnoreCase("magic") && player.isOp())
				plotMagicToggle(player);
			else {
				Output.plotHelp(player);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @param player
	 * 
	 * Toggles magic for plot at player
	 */
	private void plotMagicToggle(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!player.isOp()) {
			Output.simpleError(player, "Ops may only toggle magic for plots.");
		}
		if (plot.isAllowMagic()) {
			Output.positiveMessage(player, "You have turned off magic for "
					+ plot.getName() + ".");
			plot.setAllowMagic(false);
		} else {
			Output.positiveMessage(player, "You have turned on magic for "
					+ plot.getName() + ".");
			plot.setAllowMagic(true);
		}
	}
	
	/**
	 * @param player
	 * 
	 * Toggle pvp for plot at player.
	 */
	private void plotPvpToggle(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!player.isOp()) {
			Output.simpleError(player, "Ops may only toggle pvp for plots.");
		}
		if (plot.isAllowPvp()) {
			Output.positiveMessage(player, "You have turned off pvp for "
					+ plot.getName() + ".");
			plot.setAllowPvp(false);
		} else {
			Output.positiveMessage(player,
					"You have turned on pvp for " + plot.getName() + ".");
			plot.setAllowPvp(true);
		}
	}
	
	/**
	 * @param player
	 * 
	 * Toggle explosions for plot at player.
	 */
	private void plotExplosionToggle(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owners may toggle explosions.");
			return;
		}
		if (plot.isAllowExplosions()) {
			plot.setAllowExplosions(false);
			Output.positiveMessage(player,
					"You have disabled explosions on your plot.");
		} else {
			plot.setAllowExplosions(true);
			Output.positiveMessage(player,
					"You have enabled explosions on your plot.");
		}
	}
	
	/**
	 * @param player
	 * 
	 * Take plot of the market.
	 */
	private void plotUnSell(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.plotNotCoowner(player);
			return;
		}
		plot.setSalePrice(0);
		Output.positiveMessage(player, "This plot is no longer for sale.");
	}

	/**
	 * @param player
	 * 
	 * Buy plot at player.
	 */
	private void plotBuy(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}

		int salePrice = plot.getSalePrice();
		if (salePrice <= 0) {
			Output.simpleError(player, "This plot is not for sale.");
			return;
		}

		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);

		if (pseudoPlayer.getMoney() < salePrice) {
			Output.simpleError(player, "Cannot afford to buy plot, cost: "
					+ salePrice + ".");
			return;
		}

		pseudoPlayer.setMoney(pseudoPlayer.getMoney() - salePrice);
		UUID lastOwner = plot.getOwner();
		plot.setOwner(player.getUniqueId());
		plot.setSalePrice(0);
		plot.getCoowners().clear();
		plot.getFriends().clear();

		Output.positiveMessage(player,
				"You have purchased the plot " + plot.getName() + " from "
						+ Bukkit.getOfflinePlayer(lastOwner).getName()
						+ " for " + salePrice + ".");
		Player sellerPlayer = Bukkit.getPlayer(lastOwner);
		PseudoPlayer sellerPseudoPlayer = PseudoPlayerHandler
				.getPlayer(sellerPlayer);
		sellerPseudoPlayer.setMoney(sellerPseudoPlayer.getMoney() + salePrice);
		if (sellerPlayer.isOnline())
			sellerPlayer.sendMessage("You have sold the plot " + plot.getName()
					+ " to " + player.getName() + " for " + salePrice + ".");
		// else
		// player.addOflinemsg
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Sell plot at player.
	 */
	private void plotSell(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may sell the plot.");
			return;
		}
		if (args.length < 2) {
			player.sendMessage(ChatColor.GRAY + "Use /plot sell (amount)");
			return;
		}
		int amount = 0;
		try {
			amount = Integer.parseInt(args[1]);
		} catch (Exception e) {
			player.sendMessage(ChatColor.GRAY + "Use /plot sell (amount)");
			return;
		}

		if (amount < 1) {
			Output.simpleError(player, "Invalid price, must be 1 or greater");
			return;
		}

		if (amount > 9999999) {
			Output.simpleError(player, "Max sale price is 9999999");
			return;
		}

		plot.setSalePrice(amount);
		Output.positiveMessage(player, "You have set this plot for sale at "
				+ amount + "gc.");
	}

	/*
	 * Conrtole npcs for plot at player.
	 */
	/**
	 * @param player
	 * @param args
	 * 
	 * Manage npc's for plot at player.
	 */
	private void plotNPC(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (args.length < 4) {
			Output.simpleError(player, "/plot npc hire (Banker|Vendor) (name)");
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owner may manage plot NPCs");
			return;
		}

		if (args[1].equalsIgnoreCase("hire")) {
			String name;
			if (args[2].equalsIgnoreCase("banker")) {
				if (!plot.isTown()) {
					Output.simpleError(player,
							"You can only place a banker in a town.");
					return;
				}
				name = args[3];
				name.trim();
				if (name.length() > 7) {
					Output.simpleError(player,
							"Banker name must be 7 characters or less.");
					return;
				}

				if (name.length() < 2) {
					Output.simpleError(player,
							"Banker name must be more 1 character or more.");
					return;
				}

				if (name.contains("\"") || name.contains("'")
						|| name.contains(" ")) {
					Output.simpleError(player,
							"Cannot use \" or spaces in NPC names.");
					return;
				}

				Plot npcPlot = null;

				for (NPC npc : Lostshard.getNpcs()) {
					if (npc.getName().equalsIgnoreCase(name)) {
						Output.simpleError(player,
								"An NPC with that name already exists.");
						return;
					}
					npcPlot = PlotHandler.findPlotAt(npc.getLocation());
					if (npcPlot == plot && !player.isOp())
						Output.simpleError(player,
								"You may only have 1 banker per plot.");
					return;
				}

				NPC npc = new NPC(NPCType.BANKER, name, player.getLocation(),
						plot.getId());
				Database.insertNPC(npc);
				plot.getNpcs().add(npc);
				npc.spawn();
				Output.positiveMessage(player, "You have hired a banker named "
						+ name + ".");
			} else if (args[2].equalsIgnoreCase("vendor")) {
				name = args[3];
				name.trim();
				if (name.length() > 7) {
					Output.simpleError(player,
							"Vendor name must be 7 characters or less.");
					return;
				}

				if (name.length() < 2) {
					Output.simpleError(player,
							"Vendor name must be more 1 character or more.");
					return;
				}

				if (name.contains("\"") || name.contains("'")
						|| name.contains(" ")) {
					Output.simpleError(player,
							"Cannot use \" or spaces in NPC names.");
					return;
				}

				int currentAmountOfVendors = 1;

				for (NPC npc : plot.getNpcs()) {
					if (npc.getName().equalsIgnoreCase(name)) {
						Output.simpleError(player,
								"An NPC with that name already exists.");
						return;
					}
					if (npc.getType().equals(NPCType.VENDOR))
						currentAmountOfVendors++;
				}

				if (currentAmountOfVendors < plot.getMaxVendors()
						&& !player.isOp()) {
					Output.simpleError(player,
							"You may only have " + plot.getMaxVendors()
									+ " vendors in your plot.");
					return;
				}

				NPC npc = new NPC(NPCType.VENDOR, name, player.getLocation(),
						plot.getId());
				Database.insertNPC(npc);
				plot.getNpcs().add(npc);
				npc.spawn();

				Output.positiveMessage(player, "You have hired a vendor named "
						+ name + ".");
			} else if (args[2].equalsIgnoreCase("guard") && player.isOp()) {
				name = args[3];
				name.trim();
				if (name.length() > 7) {
					Output.simpleError(player,
							"Guard name must be 7 characters or less.");
					return;
				}

				if (name.length() < 2) {
					Output.simpleError(player,
							"Guard name must be more 1 character or more.");
					return;
				}

				if (name.contains("\"") || name.contains("'")
						|| name.contains(" ")) {
					Output.simpleError(player,
							"Cannot use \" or spaces in NPC names.");
					return;
				}

				for (NPC npc : plot.getNpcs()) {
					if (npc.getName().equalsIgnoreCase(name)) {
						Output.simpleError(player,
								"An NPC with that name already exists.");
						return;
					}
				}

				NPC npc = new NPC(NPCType.GUARD, name, player.getLocation(),
						plot.getId());
				Database.insertNPC(npc);
				plot.getNpcs().add(npc);
				npc.spawn();

				Output.positiveMessage(player, "You have hired a guard named "
						+ name + ".");
			}
		} else if (args[2].equalsIgnoreCase("move")) {
			if (args.length < 3) {
				Output.simpleError(player, "Use /plot npc move (npc name)");
				return;
			}

			String npcName = args[2];
			npcName = npcName.trim();

			for (NPC npc : plot.getNpcs())
				if (npc.getName().equalsIgnoreCase(npcName)) {
					npc.setLocation(player.getLocation());
					npc.move(player.getLocation());
					Output.positiveMessage(player, "You have moved " + npcName
							+ ".");
					return;
				}
			Output.simpleError(player, "Cannot find an NPC named " + npcName
					+ " on this plot.");
		} else if (args[1].equalsIgnoreCase("fire")) {
			if (args.length < 3) {
				Output.simpleError(player, "/plot npc fire (name)");
				return;
			}
			String name = args[2];

			for (NPC npc : plot.getNpcs())
				if (npc.getName().equalsIgnoreCase(name)) {
					npc.fire();
					Output.positiveMessage(player,
							"You have fired " + npc.getName() + ".");
					return;
				}
		} else if (args[1].equalsIgnoreCase("list")) {
			player.sendMessage(ChatColor.GOLD + "-" + plot.getName()
					+ "'s NPCs-");
			ArrayList<NPC> npcs = plot.getNpcs();
			int numOfNpcs = npcs.size();

			if (numOfNpcs <= 0) {
				player.sendMessage(ChatColor.RED + "No NPCs");
				return;
			}
			for (NPC npc : npcs)
				player.sendMessage(ChatColor.YELLOW + npc.getName() + " ("
						+ npc.getType().toString() + ")");
			return;
		}
		HelpHandler.plotNpcHelp(player);
	}

	
	/**
	 * @param player
	 * 
	 * Toggle friendbuild for plot at player.
	 */
	private void plotFriendBuildToggle(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owner may toggle friendbuild.");
			return;
		}
		if (plot.isFriendBuild()) {
			plot.setFriendBuild(false);
			Output.positiveMessage(player,
					"You have turned on friend build for " + plot.getName()
							+ ".");
			return;
		}
	}
	
	/**
	 * @param player
	 * @param args
	 * 
	 * Rename plot at player.
	 */
	private void plotRename(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may rename the plot.");
			return;
		}
		// Verify that we can afford the rename.
		int plotMoney = plot.getMoney();
		if (plotMoney >= 1000) {
			Output.simpleError(player,
					"Not enough in the plot treasury to rename. "
							+ Variables.plotRenamePrice + "gc.");
			return;
		}
		// Figure out the name that the player input
		int splitNameLength = args.length;
		String plotName = "";
		for (int i = 1; i < splitNameLength; i++) {
			plotName += args[i];
			if (i < splitNameLength - 1)
				plotName += " ";
		}

		plotName = plotName.trim();

		boolean nameValid = true;
		if (plotName.contains("\"") || plotName.contains("'")) {
			Output.simpleError(player, "Cannot use \" in plot name.");
			nameValid = false;
		}

		int nameLength = plotName.length();
		if (nameLength > Variables.plotMaxNameLength && nameValid) {
			Output.simpleError(player,
					"Name is invalid or too long, limit is 20 characters.");
			return;
		}

		for (Plot p : Lostshard.getPlots()) {
			if (p.getName().equalsIgnoreCase(plotName)) {
				Output.simpleError(player, "Cannot use that name, it is taken.");
				return;
			}
		}
		// We are good to go
		plot.setMoney(plot.getMoney() - Variables.plotRenamePrice);
		plot.setName(plotName);
		Output.positiveMessage(player, "You have renamed the plot to "
				+ plotName + ".");
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Shrink plot at player
	 */
	private void plotShrink(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may shrink the plot.");
			return;
		}

		if (args.length < 2) {
			player.sendMessage(ChatColor.GRAY
					+ "/plot shrink (amount) will shrink the plot");
			return;
		}
		int amount = -1;
		try {
			amount = Integer.parseInt(args[1]);
		} catch (Exception e) {
		}

		if (amount < 1) {
			Output.simpleError(player, "Cannot shrink less than 1 block.");
			return;
		}

		if (plot.getSize() <= amount) {
			Output.simpleError(player, "Cannot shrink that much.");
			return;
		}

		plot.setSize(plot.getSize() - amount);
		Output.positiveMessage(player, "You have shrunk the plot by " + amount
				+ " blocks.");
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Transfer plot at player to target player.
	 */
	private void plotTransfer(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may transfer the plot.");
			return;
		}
		if (args.length < 2) {// someone just typed /plot transfer
			player.sendMessage(ChatColor.GRAY
					+ "/plot transfer (player name) will transfer the plot");
			player.sendMessage(ChatColor.GRAY + "to the player named.");
			return;
		}
		String targetName = args[1];
		@SuppressWarnings("deprecation")
		Player targetPlayer = Bukkit.getPlayer(targetName);
		if (targetPlayer == null) {
			Output.simpleError(player, targetName + " not found.");
			return;
		}
		plot.removeCoowner(targetPlayer);
		plot.removeFriend(targetPlayer);
		plot.setOwner(targetPlayer.getUniqueId());
		Output.positiveMessage(player, "Transferred plot \"" + plot.getName()
				+ "\" to " + targetPlayer.getName());
		Output.positiveMessage(targetPlayer, player.getName()
				+ " transferred plot \"" + plot.getName() + "\" to you.");
	}
	
	/**
	 * @param player
	 * @param args
	 * 
	 * Downgrade plot at player.
	 */
	private void plotDowngrade(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		int numAvail = 0;
		if (plot.isOwner(player)) {

			if (args.length == 1) {
				Output.positiveMessage(player, plot.getName() + "'s Upgrades:");

				if (plot.isTown()) {
					numAvail++;
					player.sendMessage(ChatColor.YELLOW + "- Town");
				}

				if (plot.isDungeon()) {
					numAvail++;
					player.sendMessage(ChatColor.YELLOW + "- Dungeon");
				}

				if (plot.isAutoKick()) {
					numAvail++;
					player.sendMessage(ChatColor.YELLOW
							+ "- AutoKick (non-friend login auto eject)");
				}

				if (plot.isNeutralAlignment()) {
					numAvail++;
					player.sendMessage(ChatColor.YELLOW + "- Neutral");
				}

				if (numAvail <= 0) {
					Output.simpleError(player, plot.getName()
							+ " has not been upgraded.");
				}
			}

			else if (args.length >= 2) { // someone typed /plot upgrade
											// (something)
				if (args[1].equalsIgnoreCase("town")) {
					if (plot.isTown()) {
						plot.setMoney(plot.getMoney() + 75000);
						plot.setTown(false);
						if (plot.isNeutralAlignment()) {
							plot.setNeutralAlignment(false);
							plot.setMoney(plot.getMoney() + 7500);
						}
						Output.positiveMessage(player, plot.getName()
								+ " downgrade from a town to a normal plot.");
					} else
						Output.simpleError(player, plot.getName()
								+ " is not a town.");
				} else if (args[1].equalsIgnoreCase("dungeon")) {
					if (plot.isDungeon()) {
						plot.setMoney(plot.getMoney() + 15000);
						plot.setDungeon(false);
						Output.positiveMessage(player, plot.getName()
								+ " downgrade from a dungeon to a normal plot.");
					} else
						Output.simpleError(player, plot.getName()
								+ " is not a dungeon.");
				} else if (args[1].equalsIgnoreCase("autokick")) {
					if (plot.isAutoKick()) {
						plot.setMoney(plot.getMoney() + 7500);
						plot.setAutoKick(false);
						Output.positiveMessage(player, plot.getName()
								+ " downgrade from autokick to a normal plot.");
					} else
						Output.simpleError(player, plot.getName()
								+ " do not have autokick.");
				} else if (args[1].equalsIgnoreCase("neutral")) {
					if (plot.isTown()) {
						if (plot.isNeutralAlignment()) {
							plot.setMoney(plot.getMoney() + 7500);
							plot.setNeutralAlignment(false);
							Output.positiveMessage(
									player,
									plot.getName()
											+ " downgrade from a neutral town to a town.");
						} else
							Output.simpleError(player, plot.getName()
									+ " is not a neutral.");
					} else
						Output.simpleError(player, plot.getName()
								+ " must be a town to downgrade this upgrade.");
				} else {
					numAvail = 0;
					Output.positiveMessage(player, plot.getName()
							+ "'s Upgrades:");

					if (plot.isTown()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW + "- Town");
					}

					if (plot.isDungeon()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW + "- Dungeon");
					}

					if (plot.isAutoKick()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW
								+ "- AutoKick (non-friend login auto eject)");
					}

					if (plot.isNeutralAlignment()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW + "- Neutral");
					}

					if (numAvail <= 0) {
						Output.simpleError(player, plot.getName()
								+ " has not been upgraded.");
					}
				}
			}
		}
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Upgrade plot at player.
	 */
	private void plotUpgrade(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		int numAvail = 0;
		if (args.length < 2) {
			Output.positiveMessage(player, plot.getName() + "'s Upgrades:");

			if (plot.isTown()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW + "- Town");
			}

			if (plot.isDungeon()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW + "- Dungeon");
			}

			if (plot.isAutoKick()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW
						+ "- AutoKick (non-friend login auto eject)");
			}

			if (plot.isNeutralAlignment()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW + "- Neutral");
			}

			if (numAvail <= 0) {
				Output.simpleError(player, plot.getName()
						+ " has not been upgraded.");
			}
		}
		if (plot.isCoownerOrAbove(player)) {
			if (args.length >= 1) {// someone only typed /plot upgrade
				Output.positiveMessage(player, "-Plot Upgrades Available-)");
				numAvail = 0;
				if (!plot.isTown()) {
					numAvail++;
					player.sendMessage(ChatColor.YELLOW + "- Town [100,000 gc]");
				}

				if (!plot.isDungeon()) {
					numAvail++;
					player.sendMessage(ChatColor.YELLOW
							+ "- Dungeon [20,000 gc]");
				}

				if (!plot.isAutoKick()) {
					numAvail++;
					player.sendMessage(ChatColor.YELLOW
							+ "- AutoKick [10,000 gc]");
				}

				if (plot.isTown()) {
					if (!plot.isNeutralAlignment()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW
								+ "- Neutral Alignment [4,000 gc]");
					}
				}
			} else if (args.length >= 2) { // someone typed /plot upgrade
											// (something)
				if (args[1].equalsIgnoreCase("town")) {
					if (!plot.isTown()) {
						if (plot.getMoney() >= 100000) {
							plot.setMoney(plot.getMoney() - 100000);
							plot.setTown(true);
							Output.positiveMessage(player, plot.getName()
									+ " upgraded to a town.");
						} else
							Output.simpleError(player,
									"Not enough money in plot funds. (100,000 gc)");
					} else
						Output.simpleError(player, plot.getName()
								+ " is already a town.");
				} else if (args[1].equalsIgnoreCase("dungeon")) {
					if (!plot.isDungeon()) {
						if (plot.getMoney() >= 20000) {
							plot.setMoney(plot.getMoney() - 20000);
							plot.setDungeon(true);
							Output.positiveMessage(player, plot.getName()
									+ " upgraded to a dungeon.");
						} else
							Output.simpleError(player,
									"Not enough money in plot funds. (20,000 gc)");
					} else
						Output.simpleError(player, plot.getName()
								+ " is already a dungeon.");
				} else if (args[1].equalsIgnoreCase("autokick")) {
					if (!plot.isAutoKick()) {
						if (plot.getMoney() >= 10000) {
							plot.setMoney(plot.getMoney() - 10000);
							plot.setAutoKick(true);
							Output.positiveMessage(player, plot.getName()
									+ " upgraded with AutoKick.");
						} else
							Output.simpleError(player,
									"Not enough money in plot funds. (10,000 gc)");
					} else
						Output.simpleError(player, plot.getName()
								+ " already has the AutoKick upgrade.");
				} else if (args[1].equalsIgnoreCase("neutral")) {
					if (plot.isTown()) {
						if (!plot.isNeutralAlignment()) {
							if (plot.getMoney() >= 4000) {
								plot.setMoney(plot.getMoney() - 4000);
								plot.setNeutralAlignment(true);
								Output.positiveMessage(player, plot.getName()
										+ " upgraded to Neutral Alignment.");
							} else
								Output.simpleError(player,
										"Not enough money in plot funds. (4,000 gc)");
						} else
							Output.simpleError(
									player,
									plot.getName()
											+ " already has the Neutral Alignment upgrade.");
					} else
						Output.simpleError(player, plot.getName()
								+ " must be a town to purchase this upgrade.");
				} else
					Output.simpleError(player, args[1]
							+ " upgrade is not available.");
			}
		}
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * List plots for player.
	 */
	@SuppressWarnings("deprecation")
	private void plotList(Player player, String[] args) {
		if (args.length >= 2 && player.isOp()) {
			String name = args[1];
			Output.positiveMessage(player, "-" + name + "'s Plots-");
			for (Plot plot : Lostshard.getPlots()) {
				if (plot.getOwner().equals(
						Bukkit.getOfflinePlayer(name).getUniqueId())) {
					player.sendMessage(" - " + plot.getName() + " ("
							+ plot.getSize() + ") @("
							+ plot.getLocation().getBlockX() + ","
							+ plot.getLocation().getBlockY() + ","
							+ plot.getLocation().getBlockZ() + ")");
				}
			}
		} else {
			Output.positiveMessage(player, "-" + player.getName() + "'s Plots-");
			boolean foundOne = false;
			for (Plot plot : Lostshard.getPlots()) {
				if (plot.isOwner(player)) {
					foundOne = true;
					player.sendMessage(" - " + plot.getName() + " ("
							+ plot.getSize() + ") @("
							+ plot.getLocation().getBlockX() + ","
							+ plot.getLocation().getBlockY() + ","
							+ plot.getLocation().getBlockZ() + ")");
				}
			}
			if (!foundOne)
				Output.simpleError(player,
						"You do not currently own any plots.");
		}
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Withdraw money from plot founds at player.
	 */
	private void plotWithdraw(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner or co-owners may withdraw from the plot funds.");
			return;
		}
		if (args.length < 2) {
			Output.simpleError(player,
					"/plot withdraw (amount) to withdraw founds from plot.");
			return;
		}
		int amount = -1;
		try {
			amount = Integer.parseInt(args[1]);
		} catch (Exception e) {
			Output.simpleError(player, ".");
			return;
		}
		if (amount > 0) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
			if (plot.getMoney() >= amount) {
				plot.setMoney(plot.getMoney() - amount);
				pseudoPlayer.setMoney(pseudoPlayer.getMoney() + amount);
				Output.positiveMessage(player, "You have withdrawn " + amount
						+ " gold coins from the plot fund.");
			} else
				Output.simpleError(player,
						"The plot does not have that many gold coins.");
		} else
			Output.simpleError(player, "Invalid amount.");
	}

	/**
	 * @param player
	 * 
	 * Toggle test for player and plot.
	 */
	private void plotTestToggle(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		pseudoPlayer.setTestPlot(plot);
		Output.positiveMessage(player,
				"You are currently testing " + plot.getName() + ".");
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Expand plot to given size.
	 */
	private void plotExpand(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owner may expand the plot.");
			return;
		}
		int amount = 1;
		if(args.length >= 2) {
			try {
				amount = Integer.parseInt(args[1]);
			} catch(Exception e) {
				Output.simpleError(player, "Invalid amount. /plot expand (amount)");
				return;
			}
		}
		if(amount < 1) {
			Output.simpleError(player, "Expand size must be greater than 0.");
			return;
		}
		// see if we would expand into an existing region
		for (Plot p : Lostshard.getPlots()) {
			if (!p.getLocation().getWorld().equals(player.getWorld()))
				continue;
			if (p == plot)
				continue;
			
			int sphereOfInfluence;
			if (p.isCoownerOrAbove(player)) {
				sphereOfInfluence = p.getSize();
			} else {
				if (p.isTown())
					sphereOfInfluence = p.getSize() * 2;
				else
					sphereOfInfluence = (int) Math
							.ceil(p.getSize() * 1.5);
			}

			if (Utils.isWithin(p.getLocation(), plot.getLocation(),
					sphereOfInfluence + plot.getSize() + 1 + amount)) {
				if(amount == 1)
				Output.simpleError(player,
						"Cannot expand, " + p.getName()
								+ " is too close.");
				else
					Output.simpleError(player, "You may only expand "+
						plot.getName()+" "+
						p.getLocation().distanceSquared(player.getLocation())+".");
				return;
			}
		}

		// verify that we can afford the expansion
		int plotMoney = plot.getMoney();
		int expansionCost = plot.getExpandPrice(amount);
		if (plotMoney >= expansionCost) {
			// we are good to go
			plot.setMoney(plot.getMoney() - expansionCost);
			plot.setSize(plot.getSize() + amount);
			Output.positiveMessage(player,
					"You have expanded the plot to size " + plot.getSize()
							+ ".");
		} else
			Output.simpleError(player,
					"Not enough money in the plot treasury to expand. "
					+ "It will cost "+expansionCost+" to expand to "+
							plot.getSize()+amount);
	}

	/**
	 * @param player
	 * 
	 * Make plot public at player.
	 */
	private void plotPublic(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner may make the plot public.");
			return;
		}
		if (!plot.isPrivatePlot()) {
			Output.simpleError(player, "The plot is already public.");
		} else {
			plot.setPrivatePlot(true);
			Output.positiveMessage(player, "You have made the plot public.");
		}
	}

	/**
	 * @param player
	 * 
	 *  Make plot private at player.
	 */
	private void plotPrivate(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner may make the plot private.");
			return;
		}
		if (plot.isPrivatePlot()) {
			Output.simpleError(player, "The plot is already private.");
		} else {
			plot.setPrivatePlot(true);
			Output.positiveMessage(player, "You have made the plot private.");
		}
	}

	/**
	 * @param player
	 * 
	 * Toggle protection for plot at player.
	 */
	private void plotProtect(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may toggle protection.");
			return;
		}
		if (plot.isProtected()) {
			plot.setProtected(false);
			Output.positiveMessage(player,
					"You have turned off protection for " + plot.getName()
							+ ".");
		} else {
			plot.setProtected(true);
			Output.positiveMessage(player, "You have turned on protection for "
					+ plot.getName() + ".");
		}
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Unfriend player of plot at player.
	 */
	private void plotUnFriend(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owners may unfriend players from this plot.");
			return;
		}

		String targetName = args[1];
		@SuppressWarnings("deprecation")
		OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);
		if (targetPlayer == player) {
			Output.simpleError(player, "You may not unfriend yourself.");
			return;
		}

		if (!plot.getFriends().contains(targetPlayer.getUniqueId())
				|| !plot.getCoowners().contains(targetPlayer.getUniqueId())) {
			Output.simpleError(player, targetPlayer.getName()
					+ " is not a friend or co-owner of this plot.");
			return;
		}

		if (plot.getCoowners().contains(targetPlayer.getUniqueId())
				&& plot.getCoowners().contains(player.getUniqueId())) {
			Output.simpleError(player,
					"Only the owner may unfriend a co-owner.");
			return;
		}

		if (plot.getCoowners().contains(targetPlayer.getUniqueId())) {
			Output.positiveMessage(player, targetPlayer.getName()
					+ " is no longer a friend of this plot.");
			if (targetPlayer.isOnline())
				Output.positiveMessage(targetPlayer.getPlayer(),
						"You are no longer a friend of " + plot.getName() + ".");
			plot.getCoowners().remove(targetPlayer.getUniqueId());
		} else {
			Output.positiveMessage(player, targetPlayer.getName()
					+ " is no longer a friend of this plot.");
			if (targetPlayer.isOnline())
				Output.positiveMessage(targetPlayer.getPlayer(),
						"You are no longer a friend of " + plot.getName() + ".");
			plot.getFriends().remove(targetPlayer.getUniqueId());
		}
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Co-owner player of plot at player.
	 */
	private void plotCoOwn(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}

		if (args.length < 2) {
			Output.simpleError(player, "/plot co-owner (player)");
			return;
		}

		String targetName = args[1];

		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may co-owner players.");
			return;
		}

		@SuppressWarnings("deprecation")
		Player targetPlayer = Bukkit.getPlayer(targetName);

		if (targetPlayer == null) {
			Output.simpleError(player,
					"Cannot co-own that person, hes not online.");
			return;
		}

		if (player == targetPlayer) {
			Output.simpleError(player, "You may not co-owner your self.");
			return;
		}

		if (plot.isFriend(targetPlayer)) {
			plot.getFriends().remove(targetPlayer.getUniqueId());
		}
		plot.addCoowner(targetPlayer);
		Output.positiveMessage(player, targetPlayer.getName()
				+ " is now a co-owner of this plot.");
		Output.positiveMessage(targetPlayer, "You are now a co-owner of "
				+ plot.getName() + ".");
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Friend player of plot at player.
	 */
	private void plotFriend(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}

		if (args.length < 2) {
			Output.simpleError(player, "/plot friend (player)");
			return;
		}

		String targetName = args[1];

		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner and co-owner may friend players.");
			return;
		}

		@SuppressWarnings("deprecation")
		Player targetPlayer = Bukkit.getPlayer(targetName);

		if (targetPlayer == null) {
			Output.simpleError(player,
					"Cannot friend that person, hes not online.");
			return;
		}

		if (player == targetPlayer) {
			Output.simpleError(player, "You may not friend your self.");
			return;
		}

		if (plot.isCoowner(targetPlayer) && plot.isCoowner(player)) {
			Output.simpleError(player, "Only the owner may friend co-owners.");
			return;
		}

		if (plot.isCoowner(targetPlayer)) {
			plot.getCoowners().remove(targetPlayer.getUniqueId());
		}
		plot.addCoowner(targetPlayer);
		Output.positiveMessage(player, targetPlayer.getName()
				+ " is now a friend of this plot.");
		Output.positiveMessage(targetPlayer,
				"You are now a friend of " + plot.getName() + ".");
	}

	/**
	 * @param player
	 * 
	 * Display info for plot at player.
	 */
	private void plotInfo(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		Output.plotInfo(player, plot);
	}

	/**
	 * @param player
	 * 
	 * Survey nearby plots of player.
	 */
	private void plotSurvey(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		ArrayList<Plot> plots = Lostshard.getPlots();
		int numPlots = plots.size();

		// First, determine if we are currently in a plot
		boolean inPlot = false;
		if (plot != null) {
			inPlot = true;
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.RED
					+ "cannot" + ChatColor.YELLOW + " create a plot here.");
			player.sendMessage(ChatColor.YELLOW
					+ "You are currently in the plot \"" + plot.getName()
					+ "\".");

			// return;
		}

		// Determine the max range
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		int miningSkill = 0; // pseudoPlayer.getSkill("mining");
		double percent = (double) miningSkill / 1000;
		int range = (int) Math.ceil(200 * percent) + 100;

		Location curLoc = new Location(player.getWorld(), player.getLocation()
				.getBlockX() + .5, player.getLocation().getBlockY() + .5,
				player.getLocation().getBlockZ() + .5);

		// If we are not in a plot, determine if there are any plots within
		// range
		ArrayList<Plot> plotsInRange = new ArrayList<Plot>();
		for (int i = 0; i < numPlots; i++) {
			Plot p = plots.get(i);
			if (!p.getLocation().getWorld().equals(player.getWorld()))
				continue;
			if (plot != null && p.getName().equalsIgnoreCase(plot.getName()))
				continue;
			int border;
			if (p.isCoownerOrAbove(player)) {
				border = p.getSize();
			} else {
				if (p.isTown())
					border = p.getSize() * 2;
				else
					border = (int) Math.ceil(p.getSize() * 1.5);
			}

			// If a plot's border is within range of the player
			if (Utils.isWithin(curLoc, p.getLocation(), range + border)) {
				plotsInRange.add(p);
				// System.out.println("In range - "+p.getName());
			} else {
				// System.out.println("Out of range - "+p.getName());
			}
		}

		if (!inPlot) {
			// If there are no plots within range
			if (plotsInRange.size() == 0) {
				Output.positiveMessage(player, "-Plot Survey Results-");
				player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.GOLD
						+ "can" + ChatColor.YELLOW + " create a plot here.");
				player.sendMessage(ChatColor.YELLOW
						+ "There are no plots within " + range
						+ " blocks of here.");

				int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
				int plotsThisWeek = 0;
				if (plotCreatePoints > 0) {
					plotsThisWeek = (plotCreatePoints / 7);
				}
				int plotMoneyCost = Variables.plotCreatePrice;
				int plotDiamondCost = Variables.plotCreateItemPrice.getAmount();
				// for each owned plot, double the price
				for (int i = 0; i < plotsThisWeek; i++) {
					plotMoneyCost *= 2;
					plotDiamondCost *= 2;
				}

				player.sendMessage(ChatColor.YELLOW + "It would cost "
						+ plotMoneyCost + " gc and " + plotDiamondCost
						+ " diamonds to create a size 10 plot here.");
				return;
			}
		}
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Create plot at player.
	 */
	private void createPlot(Player player, String[] args) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		int curMoney = pseudoPlayer.getMoney();

		// get recently purchased plots
		int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
		int plotsThisWeek = 0;
		if (plotCreatePoints > 0) {
			plotsThisWeek = (plotCreatePoints / 7);
		}

		if (player.isOp())
			plotsThisWeek = 0;

		int plotMoneyCost = Variables.plotCreatePrice;
		ItemStack plotDiamondCost = Variables.plotCreateItemPrice;
		// for each owned plot, double the price
		for (int i = 0; i < plotsThisWeek; i++) {
			plotMoneyCost *= 2;
			plotDiamondCost.setAmount(plotDiamondCost.getAmount() * 2);
		}
		// make sure the player has enough money and diamonds
		if (!player.isOp() && !(curMoney >= plotMoneyCost && player.getInventory().containsAtLeast(plotDiamondCost, plotDiamondCost.getAmount()))) {
			Output.simpleError(player, "Cannot afford to create a plot, cost: "
					+ plotMoneyCost + " gold & " + plotDiamondCost.getAmount()
					+ " diamonds.");
			return;
		}
		// verify that this plot wouldn't intersect with an existing plot
		ArrayList<Plot> intersectingRegions = new ArrayList<Plot>();

		// figure out the name that the player input
		int splitNameLength = args.length;
		String plotName = "";
		for (int i = 1; i < splitNameLength; i++) {
			plotName += args[i];
			if (i < splitNameLength - 1)
				plotName += " ";
		}

		plotName = plotName.trim();

		if (plotName.contains("\"") || plotName.contains("'")) {
			Output.simpleError(player, "Cannot use \" in plot name.");
			return;
		}

		int nameLength = plotName.length();
		if(nameLength < 1) {
			Output.simpleError(player,
					"/plot create (name)");
			return;
		}
		if (nameLength > Variables.plotMaxNameLength) {
			Output.simpleError(player,
					"Plot name is invalid or too long, limit is 20 characters.");
			return;
		}
		// find intersecting regions and check names to make sure there isn't a
		// region with that name already
		Location curLoc = player.getLocation().getBlock().getLocation();
		for (Plot plot : Lostshard.getPlots()) {
			if (plot.getName().equalsIgnoreCase(plotName)) {
				Output.simpleError(player,
						"A plot with that name already exists, please choose another.");
				return;
			}
			if (!curLoc.getWorld().equals(plot.getLocation().getWorld()))
				continue;
			int sphereOfInfluence;
			if (plot.isCoownerOrAbove(player))
				sphereOfInfluence = plot.getSize();
			else {
				if (plot.isTown())
					sphereOfInfluence = plot.getSize() * 2;
				else
					sphereOfInfluence = (int) Math
							.ceil(plot.getSize() * 1.5);
			}

			if (Utils.isWithin(curLoc, plot.getLocation(),
					sphereOfInfluence + Variables.plotStartingSize))
				intersectingRegions.add(plot);
		}

		if (intersectingRegions.size() == 0) {
			// money/diamonds verified, placement verified, name verified: good
			// to go
			// first, remove the money/diamonds
			if(!player.isOp())
				curMoney -= plotMoneyCost;
			pseudoPlayer.setMoney(curMoney);
			pseudoPlayer
					.setPlotCreatePoints(pseudoPlayer.getPlotCreatePoints() + 7);
			// debited money successfully, now remove the proper amount of
			// diamonds
			player.getInventory().remove(plotDiamondCost);
			// costs paid, create the plot

			Plot plot = new Plot(plotName, player.getUniqueId(), curLoc);
			Lostshard.getPlots().add(plot);
			Output.positiveMessage(player, "You have created the plot \""+plot.getName()+"\", it cost "+plotMoneyCost+" gc and "+plotDiamondCost.getAmount()+" diamonds.");
		} else {
			player.sendMessage(ChatColor.DARK_RED
					+ "Cannot create a plot there, too close to the following plots:");
			int maxDisplay = 6;
			if (intersectingRegions.size() < maxDisplay)
				maxDisplay = intersectingRegions.size();
			for (int i = 0; i < maxDisplay; i++)
				player.sendMessage(ChatColor.DARK_RED + "-"
						+ intersectingRegions.get(i).getName());
		}
	}

	/**
	 * @param player
	 * 
	 * Disband plot at player.
	 */
	private void plotDisband(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner may disband " + plot.getName() + ".");
			return;
		}
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		pPlayer.setMoney(pPlayer.getMoney() + plot.getValue());
		// Output positive message that plot has bin disbanded and the value of
		// the plot.
		Output.positiveMessage(player, "You have disbanded " + plot.getName()
				+ ", and got " + plot.getValue() + "gc.");
		PlotHandler.removePlot(plot);
	}

	/**
	 * @param player
	 * @param args
	 * 
	 * Deposit money into plot found at player.
	 */
	private void plotDeposit(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isFriendOrAbove(player)) {
			Output.simpleError(player, "Only friends may deposit money into "
					+ plot.getName() + ".");
			return;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[1]);
		} catch (Exception e) {
			Output.notNumber(player);
			return;
		}
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if (pPlayer.getMoney() >= amount) {
			plot.setMoney(plot.getMoney() + amount);
			pPlayer.setMoney(pPlayer.getMoney() - amount);
			Output.positiveMessage(player, "You have deposited " + amount
					+ " gold coins into the plot fund.");
		} else {
			Output.simpleError(player, "You do not have that much money.");
			return;
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("plot"))
			if (args.length == 1)
				return TabUtils.StringTab(args, new String[] { "info",
						"create", "deposit", "expand", "rename", "coowner",
						"unfriend", "shrink", "withdraw", "friendbuild",
						"sell", "unsell", "buy", "list", "preotect",
						"unprotect", "private", "public", "explosions",
						"upgrade", "downgrade" });
		return null;
	}

}