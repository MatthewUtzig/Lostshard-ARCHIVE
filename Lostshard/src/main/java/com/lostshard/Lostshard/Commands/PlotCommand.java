package com.lostshard.Lostshard.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Database.Mappers.MessagesMapper;
import com.lostshard.Lostshard.Database.Mappers.NPCMapper;
import com.lostshard.Lostshard.Database.Mappers.PlayerMapper;
import com.lostshard.Lostshard.Database.Mappers.PlotMapper;
import com.lostshard.Lostshard.Events.EventManager;
import com.lostshard.Lostshard.Events.PlotCreateEvent;
import com.lostshard.Lostshard.Handlers.HelpHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.NPC.NPCType;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.PlotCapturePoint;
import com.lostshard.Lostshard.Objects.Plot.PlotUpgrade;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.TabUtils;
import com.lostshard.Lostshard.Utils.Utils;

/**
 * @author Jacob Emil Ulvedal Rosborg
 *
 */
public class PlotCommand extends LostshardCommand {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();

	public PlotCommand(Lostshard plugin) {
		super(plugin, "plot");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Create plot at player.
	 */
	private void createPlot(Player player, String[] args) {
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);

		int curMoney = pseudoPlayer.getMoney();

		// get recently purchased plots
		final int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
		int plotsThisWeek = 0;
		if (plotCreatePoints > 0)
			plotsThisWeek = plotCreatePoints / 7;

		if (player.isOp())
			plotsThisWeek = 0;

		int plotMoneyCost = Variables.plotCreatePrice;
		final ItemStack plotDiamondCost = Variables.plotCreateItemPrice;
		// for each owned plot, double the price
		for (int i = 0; i < plotsThisWeek; i++) {
			plotMoneyCost *= 2;
			plotDiamondCost.setAmount(plotDiamondCost.getAmount() * 2);
		}
		// make sure the player has enough money and diamonds
		if (!player.isOp()
				&& !(curMoney >= plotMoneyCost && player.getInventory()
				.containsAtLeast(plotDiamondCost,
						plotDiamondCost.getAmount()))) {
			Output.simpleError(player, "can't afford to create a plot, cost: "
					+ plotMoneyCost + " gold & " + plotDiamondCost.getAmount()
					+ " diamonds.");
			return;
		}
		// verify that this plot wouldn't intersect with an existing plot
		final ArrayList<Plot> intersectingRegions = new ArrayList<Plot>();
		final String plotName = StringUtils.join(args, " ", 1, args.length);

		final int nameLength = plotName.length();
		if (nameLength < 1) {
			Output.simpleError(player, "/plot create (name)");
			return;
		}
		if (nameLength > Variables.plotMaxNameLength) {
			Output.simpleError(player,
					"Plot name is invalid or too long, limit is 20 characters.");
			return;
		}
		// find intersecting regions and check names to make sure there isn't a
		// region with that name already
		final Location curLoc = player.getLocation().getBlock().getLocation();
		for (final Plot plot : this.ptm.getPlots()) {
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
			else if (plot.isUpgrade(PlotUpgrade.TOWN))
				sphereOfInfluence = plot.getSize() * 2;
			else
				sphereOfInfluence = (int) Math.ceil(plot.getSize() * 1.5);

			if (Utils.isWithin(curLoc, plot.getLocation(), sphereOfInfluence
					+ Variables.plotStartingSize))
				intersectingRegions.add(plot);
		}

		if (intersectingRegions.size() == 0) {
			final PlotCreateEvent event = new PlotCreateEvent(player,
					pseudoPlayer, player.getLocation(), plotName);
			EventManager.callEvent(event);

			if (event.isCancelled()) {
				Output.simpleError(player, "Some thing went wrong");
				return;
			}

			// money/diamonds verified, placement verified, name verified: good
			// to go
			// first, remove the money/diamonds
			if (!player.isOp())
				curMoney -= plotMoneyCost;
			pseudoPlayer.setMoney(curMoney);
			pseudoPlayer
			.setPlotCreatePoints(pseudoPlayer.getPlotCreatePoints() + 7);
			// debited money successfully, now remove the proper amount of
			// diamonds
			ItemUtils.removeItem(player.getInventory(),
					plotDiamondCost.getType(), plotDiamondCost.getAmount());
			// costs paid, create the plot

			final Plot plot = new Plot(-1, plotName, player.getUniqueId(),
					curLoc);
			PlotMapper.insertPlot(plot);
			this.ptm.getPlots().add(plot);
			Output.positiveMessage(player, "You have created the plot \""
					+ plot.getName() + "\", it cost " + plotMoneyCost
					+ " gc and " + plotDiamondCost.getAmount() + " diamonds.");
		} else {
			player.sendMessage(ChatColor.DARK_RED
					+ "can't create a plot there, too close to the following plots:");
			int maxDisplay = 6;
			if (intersectingRegions.size() < maxDisplay)
				maxDisplay = intersectingRegions.size();
			for (int i = 0; i < maxDisplay; i++)
				player.sendMessage(ChatColor.DARK_RED + "-"
						+ intersectingRegions.get(i).getName());
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("plot")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
//			final PseudoPlayer pPlayer = pm.getPlayer(player);
//			if (args.length < 1) {
//				Plot plot = ptm.findPlotAt(player.getLocation());
//				if(plot != null) {
//					GUI gui = new PlotGUI(pPlayer, plot);
//					gui.openInventory(player);
//				}else{
//					Output.simpleError(player, "Theres no plot here, try /help plot");
//				}
//				return true;
//			}
			final String plotCommand = args[0];
			if (plotCommand.equalsIgnoreCase("help"))
				HelpHandler.helpLandOwnership(sender, new String[0]);
			else if (plotCommand.equalsIgnoreCase("create"))
				this.createPlot(player, args);
			else if (plotCommand.equalsIgnoreCase("survey"))
				this.plotSurvey(player);
			else if (plotCommand.equalsIgnoreCase("info"))
				this.plotInfo(player);
			else if (plotCommand.equalsIgnoreCase("friend"))
				this.plotFriend(player, args);
			else if (plotCommand.equalsIgnoreCase("co-own")
					|| plotCommand.equalsIgnoreCase("coown")
					|| plotCommand.equalsIgnoreCase("co-owner")
					|| plotCommand.equalsIgnoreCase("coowner"))
				this.plotCoOwn(player, args);
			else if (plotCommand.equalsIgnoreCase("unfriend"))
				this.plotUnFriend(player, args);
			else if (plotCommand.equalsIgnoreCase("protect"))
				this.plotProtect(player);
			else if (plotCommand.equalsIgnoreCase("private"))
				this.plotPrivate(player);
			else if (plotCommand.equalsIgnoreCase("public"))
				this.plotPublic(player);
			else if (plotCommand.equalsIgnoreCase("expand"))
				this.plotExpand(player, args);
			else if (plotCommand.equalsIgnoreCase("deposit"))
				this.plotDeposit(player, args);
			else if (plotCommand.equalsIgnoreCase("withdraw"))
				this.plotWithdraw(player, args);
			else if (plotCommand.equalsIgnoreCase("test"))
				this.plotTestToggle(player);
			else if (plotCommand.equalsIgnoreCase("disband"))
				this.plotDisband(player);
			else if (plotCommand.equalsIgnoreCase("list"))
				this.plotList(player, args);
			else if (plotCommand.equalsIgnoreCase("upgrade")
					|| plotCommand.equalsIgnoreCase("upgrades"))
				this.plotUpgrade(player, args);
			else if (plotCommand.equalsIgnoreCase("downgrade"))
				this.plotDowngrade(player, args);
			else if (plotCommand.equalsIgnoreCase("transfer"))
				this.plotTransfer(player, args);
			else if (plotCommand.equalsIgnoreCase("shrink"))
				this.plotShrink(player, args);
			else if (plotCommand.equalsIgnoreCase("rename"))
				this.plotRename(player, args);
			else if (plotCommand.equalsIgnoreCase("friendbuild"))
				this.plotFriendBuildToggle(player);
			else if (plotCommand.equalsIgnoreCase("npc"))
				this.plotNPC(player, args);
			else if (plotCommand.equalsIgnoreCase("sell"))
				this.plotSell(player, args);
			else if (plotCommand.equalsIgnoreCase("buy"))
				this.plotBuy(player);
			else if (plotCommand.equalsIgnoreCase("unsell")
					|| plotCommand.equalsIgnoreCase("unlist")
					|| plotCommand.equalsIgnoreCase("un-sell"))
				this.plotUnSell(player);
			else if (plotCommand.equalsIgnoreCase("explosion"))
				this.plotExplosionToggle(player);
			else if (plotCommand.equalsIgnoreCase("pvp") && player.isOp())
				this.plotPvpToggle(player);
			else if (plotCommand.equalsIgnoreCase("magic") && player.isOp())
				this.plotMagicToggle(player);
			else if (plotCommand.equalsIgnoreCase("capturepoint")
					&& player.isOp())
				this.plotCapturePoint(player);
			else if (plotCommand.equalsIgnoreCase("title") && player.isOp())
				this.plotTitle(player);
			else
				Output.simpleError(player, "Use \"/plot help\" for commands.");
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("plot"))
			if (args.length == 1)
				return TabUtils.StringTab(args, new String[] { "info",
						"create", "deposit", "expand", "rename", "coowner",
						"unfriend", "shrink", "withdraw", "friendbuild",
						"sell", "unsell", "buy", "list", "protect",
						"unprotect", "private", "public", "explosion",
						"upgrade", "downgrade", "friend", "npc" });
			else if (args.length == 2
					&& (args[0].equalsIgnoreCase("upgrade")
							|| args[0].equalsIgnoreCase("upgrades")
							|| args[0].equalsIgnoreCase("downgrade") || args[0]
								.equalsIgnoreCase("downgrades")))
				return TabUtils.StringTab(args, new String[] { "town",
						"dungeon", "autokick", "neutral" });
			else if (args.length >= 2 && args[0].equalsIgnoreCase("npc")) {
				if (args.length == 2)
					return TabUtils.StringTab(args, new String[] { "hire",
							"fire", "move" });
				else if (args.length == 3 && args[1].equalsIgnoreCase("hire"))
					return TabUtils.StringTab(args, new String[] { "banker",
							"vendor" });
			} else if (args.length == 2
					&& (args[1].equalsIgnoreCase("coowner") || args[1]
							.equalsIgnoreCase("friend"))) {
				if (sender instanceof Player)
					return TabUtils.PlotFriend((Player) sender, args);
			} else if (args.length == 2 && args[1].equalsIgnoreCase("unfriend"))
				if (sender instanceof Player)
					return TabUtils.PlotUnfriend((Player) sender, args);
		return TabUtils.empty();
	}

	/**
	 * @param player
	 *
	 *            Buy plot at player.
	 */
	private void plotBuy(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}

		final int salePrice = plot.getSalePrice();
		if (salePrice <= 0) {
			Output.simpleError(player, "This plot is not for sale.");
			return;
		}

		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);

		if (pseudoPlayer.getMoney() < salePrice) {
			Output.simpleError(player, "can't afford to buy plot, cost: "
					+ salePrice + ".");
			return;
		}

		if (plot.getOwner().equals(player.getUniqueId())) {
			Output.simpleError(player,
					"You can't buy your own plot. Use /unlist to remove your plot from the market.");
			return;
		}

		pseudoPlayer.setMoney(pseudoPlayer.getMoney() - salePrice);
		final UUID lastOwner = plot.getOwner();
		plot.setOwner(player.getUniqueId());
		plot.setSalePrice(0);
		plot.getCoowners().clear();
		plot.getFriends().clear();

		Output.positiveMessage(player,
				"You have purchased the plot " + plot.getName() + " from "
						+ Bukkit.getOfflinePlayer(lastOwner).getName()
						+ " for " + salePrice + ".");
		final Player sellerPlayer = Bukkit.getPlayer(lastOwner);
		final PseudoPlayer sellerPseudoPlayer = this.pm.getPlayer(sellerPlayer);
		sellerPseudoPlayer.setMoney(sellerPseudoPlayer.getMoney() + salePrice);
		if (sellerPlayer.isOnline())
			sellerPlayer.sendMessage("You have sold the plot " + plot.getName()
					+ " to " + player.getName() + " for " + salePrice + ".");
		else
			MessagesMapper.insertMessages(sellerPlayer.getUniqueId(),
					"You have sold the plot " + plot.getName() + " to "
							+ player.getName() + " for " + salePrice + ".");
		final List<PseudoPlayer> plist = new ArrayList<PseudoPlayer>();
		plist.add(sellerPseudoPlayer);
		PlayerMapper.updatePlayers(plist);
	}

	private void plotCapturePoint(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!player.isOp())
			Output.simpleError(player,
					"Ops may only toggle capturepoint for plots.");
		if (plot instanceof PlotCapturePoint) {
			Plot newPlot = new Plot(plot.getId(), plot.getName(), plot.getOwner(), plot.getLocation());
			newPlot.setCoowners(plot.getCoowners());
			newPlot.setFriends(plot.getFriends());
			newPlot.setMoney(plot.getMoney());
			ptm.getPlots().remove(plot);
			ptm.getPlots().add(newPlot);
			Output.positiveMessage(player, "You have turend " + plot.getName()
			+ " into a normal plot.");
		} else
			Output.positiveMessage(player, "You have turend " + plot.getName()
					+ " into a capturepoint.");
	}

	/**
	 * @param player the executor of the command
	 * @param args the message that contains the player name, who the executor wants to co-own.
	 *
	 *            Co-owner player of plot at player.
	 */
	private void plotCoOwn(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}

		if (args.length < 2) {
			Output.simpleError(player, "/plot co-owner (player)");
			return;
		}

		final String targetName = args[1];

		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may co-owner players.");
			return;
		}

		final Player targetPlayer = Bukkit.getPlayer(targetName);

		if (targetPlayer == null) {
			Output.simpleError(player,
					"can't co-own that person, hes not online.");
			return;
		}

		if (player == targetPlayer) {
			Output.simpleError(player, "You may not co-owner your self.");
			return;
		}

		if (plot.isFriend(targetPlayer))
			plot.getFriends().remove(targetPlayer.getUniqueId());
		
		if(plot.isCoowner(targetPlayer)) {
			Output.simpleError(player, "They are already a friend of the plot.");
			return;
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
	 *            Deposit money into plot found at player.
	 */
	private void plotDeposit(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
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
		} catch (final Exception e) {
			Output.notNumber(player);
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
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

	/**
	 * @param player
	 *
	 *            Disband plot at player.
	 */
	private void plotDisband(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner may disband " + plot.getName() + ".");
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		pPlayer.setMoney(pPlayer.getMoney() + plot.getValue());
		// Output positive message that plot has bin disbanded and the value of
		// the plot.
		Output.positiveMessage(player, "You have disbanded " + plot.getName()
				+ ", and got " + plot.getValue() + " gc.");
		plot.disband();
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Downgrade plot at player.
	 */
	private void plotDowngrade(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (plot.isOwner(player)) {

			if (args.length < 2) {
				Output.positiveMessage(player, plot.getName() + "'s Upgrades:");

				if (plot.getUpgrades().size() == 0)
					Output.simpleError(player, plot.getName()
							+ " has not been upgraded.");
				else
					for (final PlotUpgrade upgrade : plot.getUpgrades())
						player.sendMessage(ChatColor.YELLOW + "- "
								+ upgrade.getName() + " ("
								+ Utils.getDecimalFormater().format(upgrade.getPrice()) + " gc)");
				Output.positiveMessage(player, "-Plot Upgrades Available-");
				for (final PlotUpgrade upgrade : PlotUpgrade.values()) {
					if (plot.isUpgrade(upgrade))
						continue;
					player.sendMessage(ChatColor.YELLOW + "- "
							+ upgrade.getName());
				}
			} else if (args.length >= 2) {

				final PlotUpgrade upgrade = PlotUpgrade.getByName(StringUtils
						.join(args, " ", 1, args.length));

				if (upgrade == null) {
					Output.positiveMessage(player, plot.getName()
							+ "'s Upgrades:");
					for (final PlotUpgrade u : PlotUpgrade.values()) {
						if (plot.isUpgrade(u))
							continue;
						player.sendMessage(ChatColor.YELLOW + "- "
								+ u.getName() + " ("
								+ Utils.getDecimalFormater().format(u.getPrice()) + " gc)");
					}
					return;
				}

				if (plot.isUpgrade(upgrade)) {
					plot.removeUpgrade(upgrade);
					plot.setMoney((int) Math.floor(upgrade.getPrice() * .75));
					Output.positiveMessage(player, plot.getName()
							+ " downgrade from a " + upgrade.getName()
							+ " to a normal plot.");
					if (upgrade.equals(PlotUpgrade.TOWN)
							&& plot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT)) {
						plot.removeUpgrade(PlotUpgrade.NEUTRALALIGNMENT);
						plot.setMoney(plot.getMoney()
								+ (int) Math.floor(PlotUpgrade.NEUTRALALIGNMENT
										.getPrice() * 75));
					}
				} else
					Output.simpleError(player, plot.getName()
							+ " is not a town.");
			}
		} else
			Output.simpleError(player, "Only the owner may downgrade the plot.");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Expand plot to given size.
	 */
	private void plotExpand(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
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
		if (args.length >= 2)
			try {
				amount = Integer.parseInt(args[1]);
			} catch (final Exception e) {
				Output.simpleError(player,
						"Invalid amount. /plot expand (amount)");
				return;
			}
		if (amount < 1) {
			Output.simpleError(player, "Expand size must be greater than 0.");
			return;
		}
		// see if we would expand into an existing region
		for (final Plot p : this.ptm.getPlots()) {
			if (!p.getLocation().getWorld().equals(player.getWorld()))
				continue;
			if (p == plot)
				continue;

			int sphereOfInfluence;
			if (p.isCoownerOrAbove(player))
				sphereOfInfluence = p.getSize();
			else if (p.isUpgrade(PlotUpgrade.TOWN))
				sphereOfInfluence = p.getSize() * 2;
			else
				sphereOfInfluence = (int) Math.ceil(p.getSize() * 1.5);

			if (Utils.isWithin(p.getLocation(), plot.getLocation(),
					sphereOfInfluence + plot.getSize() + 1 + amount)) {
				if (amount == 1)
					Output.simpleError(player, "can't expand, " + p.getName()
							+ " is too close.");
				else
					Output.simpleError(
							player,
							"You may only expand "
									+ plot.getName()
									+ " "
									+ p.getLocation().distanceSquared(
											player.getLocation()) + ".");
				return;
			}
		}

		// verify that we can afford the expansion
		final int plotMoney = plot.getMoney();
		final int expansionCost = plot.getExpandPrice(plot.getSize() + amount);
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
							+ "It will cost " + expansionCost
							+ " to expand to " + (plot.getSize() + amount));
	}

	/**
	 * @param player
	 *
	 *            Toggle explosions for plot at player.
	 */
	private void plotExplosionToggle(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
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
	 * @param args
	 *
	 *            Friend player of plot at player.
	 */
	private void plotFriend(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}

		if (args.length < 2) {
			Output.simpleError(player, "/plot friend (player)");
			return;
		}

		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owner may friend players.");
			return;
		}

		final String targetName = args[1];

		final Player targetPlayer = Bukkit.getPlayer(targetName);

		if (targetPlayer == null) {
			Output.simpleError(player,
					"can't friend that person, hes not online.");
			return;
		}

		if (player == targetPlayer) {
			Output.simpleError(player, "You may not friend your self.");
			return;
		}

		if (plot.isCoowner(targetPlayer) && !plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may friend co-owners.");
			return;
		}
		
		if(plot.isFriend(targetPlayer)) {
			Output.simpleError(player, "They are already a friend of the plot.");
			return;
		}
		
		if (plot.isCoowner(targetPlayer))
			plot.getCoowners().remove(targetPlayer.getUniqueId());
		plot.addFriend(targetPlayer);
		Output.positiveMessage(player, targetPlayer.getName()
				+ " is now a friend of this plot.");
		Output.positiveMessage(targetPlayer,
				"You are now a friend of " + plot.getName() + ".");
	}

	/**
	 * @param player
	 *
	 *            Toggle friendbuild for plot at player.
	 */
	private void plotFriendBuildToggle(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owner may toggle friendbuild.");
			return;
		}
		if (!plot.isFriendBuild()) {
			plot.setFriendBuild(true);
			Output.positiveMessage(player,
					"You have turned on friend build for " + plot.getName()
					+ ".");
			return;
		}else{
			plot.setFriendBuild(false);
			Output.positiveMessage(player,
					"You have turned off friend build for " + plot.getName()
					+ ".");
			return;
		}
	}

	/**
	 * @param player
	 *
	 *            Display info for plot at player.
	 */
	private void plotInfo(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		Output.plotInfo(player, plot);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            List plots for player.
	 */
	@SuppressWarnings("deprecation")
	private void plotList(Player player, String[] args) {
		if (args.length >= 2 && player.isOp()) {
			final String name = args[1];
			Output.positiveMessage(player, "-" + name + "'s Plots-");
			for (final Plot plot : this.ptm.getPlots())
				if (plot.getOwner().equals(
						Bukkit.getOfflinePlayer(name).getUniqueId()))
					player.sendMessage(" - " + plot.getName() + " ("
							+ plot.getSize() + ") @("
							+ plot.getLocation().getBlockX() + ","
							+ plot.getLocation().getBlockY() + ","
							+ plot.getLocation().getBlockZ() + ")");
		} else {
			Output.positiveMessage(player, "-" + player.getName() + "'s Plots-");
			boolean foundOne = false;
			for (final Plot plot : this.ptm.getPlots())
				if (plot.isOwner(player)) {
					foundOne = true;
					player.sendMessage(" - " + plot.getName() + " ("
							+ plot.getSize() + ") @("
							+ plot.getLocation().getBlockX() + ","
							+ plot.getLocation().getBlockY() + ","
							+ plot.getLocation().getBlockZ() + ")");
				}
			if (!foundOne)
				Output.simpleError(player,
						"You do not currently own any plots.");
		}
	}

	/**
	 * @param player
	 *
	 *            Toggles magic for plot at player
	 */
	private void plotMagicToggle(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!player.isOp())
			Output.simpleError(player, "Ops may only toggle magic for plots.");
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
	 * @param args
	 *
	 *            Manage npc's for plot at player.
	 */
	private void plotNPC(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (args.length < 2) {
			Output.simpleError(player, "/plot npc hire (Banker|Vendor) (name)");
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owner may manage plot NPCs");
			return;
		}

		if (args[1].equalsIgnoreCase("hire")) {
			if (args.length < 4) {
				Output.simpleError(player,
						"/plot npc hire (Banker|Vendor) (name)");
				return;
			}
			String name;
			if (args[2].equalsIgnoreCase("banker")) {
				if (!plot.isUpgrade(PlotUpgrade.TOWN)) {
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
							"can't use \" or spaces in NPC names.");
					return;
				}

				Plot npcPlot = null;

				for (final NPC npc : plot.getNpcs()) {
					if (npc.getName().equalsIgnoreCase(name)) {
						Output.simpleError(player,
								"An NPC with that name already exists.");
						return;
					}
					npcPlot = this.ptm.findPlotAt(npc.getLocation());
					if (npcPlot == plot && !player.isOp()) {
						Output.simpleError(player,
								"You may only have 1 banker per plot.");
						return;
					}
				}

				final NPC npc = new NPC(NPCType.BANKER, name,
						player.getLocation(), plot.getId());
				NPCMapper.insertNPC(npc);
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
							"can't use \" or spaces in NPC names.");
					return;
				}

				int currentAmountOfVendors = 1;

				for (final NPC npc : plot.getNpcs()) {
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

				final NPC npc = new NPC(NPCType.VENDOR, name,
						player.getLocation(), plot.getId());
				NPCMapper.insertNPC(npc);
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
							"can't use \" or spaces in NPC names.");
					return;
				}

				for (final NPC npc : plot.getNpcs())
					if (npc.getName().equalsIgnoreCase(name)) {
						Output.simpleError(player,
								"An NPC with that name already exists.");
						return;
					}

				final NPC npc = new NPC(NPCType.GUARD, name,
						player.getLocation(), plot.getId());
				NPCMapper.insertNPC(npc);
				plot.getNpcs().add(npc);
				npc.spawn();

				Output.positiveMessage(player, "You have hired a guard named "
						+ name + ".");
			}
		} else if (args[1].equalsIgnoreCase("move")) {
			if (args.length < 3) {
				Output.simpleError(player, "Use /plot npc move (npc name)");
				return;
			}

			String npcName = args[2];
			npcName = npcName.trim();

			for (final NPC npc : plot.getNpcs())
				if (npc.getName().equalsIgnoreCase(npcName)) {
					npc.setLocation(player.getLocation());
					npc.move(player.getLocation());
					Output.positiveMessage(player, "You have moved " + npcName
							+ ".");
					return;
				}
			Output.simpleError(player, "can't find an NPC named " + npcName
					+ " on this plot.");
		} else if (args[1].equalsIgnoreCase("fire")) {
			if (args.length < 3) {
				Output.simpleError(player, "/plot npc fire (name)");
				return;
			}
			final String name = args[2];

			for (final NPC npc : plot.getNpcs())
				if (npc.getName().equalsIgnoreCase(name)) {
					npc.fire();
					Output.positiveMessage(player,
							"You have fired " + npc.getName() + ".");
					return;
				}
		} else if (args[1].equalsIgnoreCase("list")) {
			player.sendMessage(ChatColor.GOLD + "-" + plot.getName()
					+ "'s NPCs-");
			final ArrayList<NPC> npcs = plot.getNpcs();
			final int numOfNpcs = npcs.size();

			if (numOfNpcs <= 0) {
				player.sendMessage(ChatColor.RED + "No NPCs");
				return;
			}
			for (final NPC npc : npcs)
				player.sendMessage(ChatColor.YELLOW + npc.getName() + " ("
						+ npc.getType().toString() + ")");
			return;
		}
		HelpHandler.helpLandOwnership(player, args);
	}

	/**
	 * @param player
	 *
	 *            Make plot private at player.
	 */
	private void plotPrivate(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner may make the plot private.");
			return;
		}
		if (plot.isPrivatePlot())
			Output.simpleError(player, "The plot is already private.");
		else {
			plot.setPrivatePlot(true);
			Output.positiveMessage(player, "You have made the plot private.");
		}
	}

	/**
	 * @param player
	 *
	 *            Toggle protection for plot at player.
	 */
	private void plotProtect(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
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
	 *
	 *            Make plot public at player.
	 */
	private void plotPublic(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner may make the plot public.");
			return;
		}
		if (!plot.isPrivatePlot())
			Output.simpleError(player, "The plot is already public.");
		else {
			plot.setPrivatePlot(false);
			Output.positiveMessage(player, "You have made the plot public.");
		}
	}

	/**
	 * @param player
	 *
	 *            Toggle pvp for plot at player.
	 */
	private void plotPvpToggle(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!player.isOp())
			Output.simpleError(player, "Ops may only toggle pvp for plots.");
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
	 * @param args
	 *
	 *            Rename plot at player.
	 */
	private void plotRename(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may rename the plot.");
			return;
		}
		// Verify that we can afford the rename.
		final int plotMoney = plot.getMoney();
		if (plotMoney < 1000) {
			Output.simpleError(player,
					"Not enough in the plot treasury to rename. "
							+ Variables.plotRenamePrice + " gc.");
			return;
		}
		// Figure out the name that the player input
		final int splitNameLength = args.length;
		String plotName = "";
		for (int i = 1; i < splitNameLength; i++) {
			plotName += args[i];
			if (i < splitNameLength - 1)
				plotName += " ";
		}

		plotName = plotName.trim();

		boolean nameValid = true;
		if (plotName.contains("\"") || plotName.contains("'")) {
			Output.simpleError(player, "can't use \" in plot name.");
			nameValid = false;
		}

		final int nameLength = plotName.length();
		if (nameLength > Variables.plotMaxNameLength && nameValid) {
			Output.simpleError(player,
					"Name is invalid or too long, limit is 20 characters.");
			return;
		}

		for (final Plot p : this.ptm.getPlots())
			if (p.getName().equalsIgnoreCase(plotName)) {
				Output.simpleError(player, "can't use that name, it is taken.");
				return;
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
	 *            Sell plot at player.
	 */
	private void plotSell(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
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
		} catch (final Exception e) {
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
				+ amount + " gc.");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Shrink plot at player
	 */
	private void plotShrink(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
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
		} catch (final Exception e) {
		}

		if (amount < 1) {
			Output.simpleError(player, "can't shrink less than 1 block.");
			return;
		}

		if (plot.getSize() <= amount) {
			Output.simpleError(player, "can't shrink that much.");
			return;
		}

		plot.setSize(plot.getSize() - amount);
		Output.positiveMessage(player, "You have shrunk the plot by " + amount
				+ " blocks.");
	}

	/**
	 * @param player
	 *
	 *            Survey nearby plots of player.
	 */
	private void plotSurvey(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		final List<Plot> plots = this.ptm.getPlots();
		final int numPlots = plots.size();

		// First, determine if we are currently in a plot
		if (plot != null && !plot.isCoowner(player)) {
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.RED
					+ "can't" + ChatColor.YELLOW + " create a plot here.");
			player.sendMessage(ChatColor.YELLOW
					+ "You are currently in the plot \"" + plot.getName()
					+ "\".");
			return;
		}

		// Determine the max range
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		final int miningSkill = 0; // pseudoPlayer.getSkill("mining");
		final double percent = (double) miningSkill / 1000;
		final int range = (int) Math.ceil(200 * percent) + 100;

		final Location curLoc = new Location(player.getWorld(), player
				.getLocation().getBlockX() + .5, player.getLocation()
				.getBlockY() + .5, player.getLocation().getBlockZ() + .5);

		// If we are not in a plot, determine if there are any plots within
		// range
		final ArrayList<Plot> plotsInRange = new ArrayList<Plot>();
		for (int i = 0; i < numPlots; i++) {
			final Plot p = plots.get(i);
			if (!p.getLocation().getWorld().equals(player.getWorld()))
				continue;
			if (plot != null && p.getName().equalsIgnoreCase(plot.getName()))
				continue;
			int border;
			if (p.isCoownerOrAbove(player))
				border = p.getSize();
			else if (p.isUpgrade(PlotUpgrade.TOWN))
				border = p.getSize() * 2;
			else
				border = (int) Math.ceil(p.getSize() * 1.5);

			// If a plot's border is within range of the player
			if (Utils.isWithin(curLoc, p.getLocation(), range + border))
				plotsInRange.add(p);
		}

		// If there are no plots within range
		if (plotsInRange.isEmpty()) {
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.GOLD
					+ "can" + ChatColor.YELLOW + " create a plot here.");
			player.sendMessage(ChatColor.YELLOW
					+ "There are no plots within " + range
					+ " blocks of here.");

			final int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
			int plotsThisWeek = 0;
			if (plotCreatePoints > 0)
				plotsThisWeek = plotCreatePoints / 7;
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
		
		// If there ARE plots within range
		double closestDist = 99999;
		Plot closestPlot = null;
		int numPlotsInRange = plotsInRange.size();
		for(int i=0; i<numPlotsInRange; i++) {
			Plot p = plotsInRange.get(i);
			int border;
			if(p.isCoownerOrAbove(player)) {
				border = p.getSize();
			}
			else {
				if(p.getUpgrades().contains(PlotUpgrade.TOWN))
					border = p.getSize() * 2;
				else
					border = (int)Math.ceil(p.getSize() * 1.5);
			}
				
			// Determine the distance from the player to the border of the plot
			double dist = Utils.distance(curLoc, p.getLocation()) - border;
			//System.out.println("Dist to "+p.getName()+" - "+dist);
			
			if(dist < closestDist) {
				closestDist = dist;
				closestPlot = p;
			}
		}
		
		if(plot != null && plot.isCoownerOrAbove(player)) {
			if(closestPlot != null) {
				player.sendMessage(ChatColor.YELLOW+"The nearest plot is \""+closestPlot.getName()+"\"");
				player.sendMessage(ChatColor.YELLOW+"it is "+(Variables.plotStartingSize+(int)closestDist-10)+" blocks from here.");
			}
			
			if(closestDist > range)
				closestDist = range;
			
			player.sendMessage(ChatColor.YELLOW+"If you expanded this plot, you could expand it to at least size "+(Variables.plotStartingSize+(int)(closestDist-10)));
			return;
		}
		
		// Pretty much can't happen, but I'm checking for it anyway
		if(closestPlot == null) {
			Output.simpleError(player, "Something went wrong, tell an admin =(");
			return;
		}
		
		// Determine whether the closest plot border is within 10 blocks of the player's position (plot minimum size)
		if(closestDist <= Variables.plotStartingSize) {
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(ChatColor.YELLOW+"You "+ChatColor.RED+"cannot"+ChatColor.YELLOW+" create a plot here.");
			player.sendMessage(ChatColor.YELLOW+"This location is too close to the plot");
			player.sendMessage(ChatColor.YELLOW+"\""+closestPlot.getName()+"\".");
			return;
		}
		
		// We are not within the min plot size of an existing plot
		Output.positiveMessage(player, "-Plot Survey Results-");
		player.sendMessage(ChatColor.YELLOW+"You "+ChatColor.GOLD+"can"+ChatColor.YELLOW+" create a plot here.");
		player.sendMessage(ChatColor.YELLOW+"The nearest plot is \""+closestPlot.getName()+"\"");
		player.sendMessage(ChatColor.YELLOW+"it is "+(Variables.plotStartingSize+(int)closestDist-10)+" blocks from here. If you created a");
		player.sendMessage(ChatColor.YELLOW+"plot here, you could expand it to about size "+(Variables.plotStartingSize+(int)(closestDist-10)));
		
		int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
		int plotsThisWeek = 0;
		if(plotCreatePoints > 0) {
			plotsThisWeek = (plotCreatePoints/7);
		}
		int plotMoneyCost = Variables.plotCreatePrice;
		int plotDiamondCost = Variables.plotCreateItemPrice.getAmount();
		// for each owned plot, double the price
		for(int i=0; i<plotsThisWeek; i++) {
			plotMoneyCost *= 2;
			plotDiamondCost *= 2;
		}
		
		player.sendMessage(ChatColor.YELLOW+"It would cost $"+plotMoneyCost+" and "+plotDiamondCost+" diamonds to create a size 10 plot here.");
		return;
	}

	/**
	 * @param player
	 *
	 *            Toggle test for player and plot.
	 */
	private void plotTestToggle(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		if (pseudoPlayer.getTestPlot() != null) {
			Output.positiveMessage(player, "You are no longer testing "
					+ pseudoPlayer.getTestPlot().getName() + ".");
			pseudoPlayer.setTestPlot(null);
			return;
		}
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		pseudoPlayer.setTestPlot(plot);
		Output.positiveMessage(player, "You are now testing " + plot.getName()
				+ ".");
	}

	private void plotTitle(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!player.isOp())
			Output.simpleError(player, "Ops may only toggle magic for plots.");
		if (plot.isTitleEntrence()) {
			Output.positiveMessage(player, "You have turned off title for "
					+ plot.getName() + ".");
			plot.setTitleEntrence(false);
		} else {
			Output.positiveMessage(player, "You have turned on title for "
					+ plot.getName() + ".");
			plot.setTitleEntrence(true);
		}
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Transfer plot at player to target player.
	 */
	private void plotTransfer(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player) && !player.isOp()) {
			Output.simpleError(player, "Only the owner may transfer the plot.");
			return;
		}
		if (args.length < 2) {// someone just typed /plot transfer
			player.sendMessage(ChatColor.GRAY
					+ "/plot transfer (player name) will transfer the plot");
			player.sendMessage(ChatColor.GRAY + "to the player named.");
			return;
		}

		final String targetName = args[1];

		final Player targetPlayer = Bukkit.getPlayer(targetName);
		if (targetPlayer == null) {
			Output.simpleError(player, targetName + " not found.");
			return;
		}
		if (plot.getOwner().equals(targetPlayer.getUniqueId())) {
			Output.simpleError(player,
					"You can't transfer your plot to yourself.");
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
	 *            Unfriend player of plot at player.
	 */
	private void plotUnFriend(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owners may unfriend players from this plot.");
			return;
		}

		final String targetName = args[1];
		@SuppressWarnings("deprecation")
		final OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(targetName);
		if (targetPlayer == player) {
			Output.simpleError(player, "You may not unfriend yourself.");
			return;
		}

		if (!plot.isFriendOrAbove(targetPlayer.getUniqueId())) {
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
	 *
	 *            Take plot of the market.
	 */
	private void plotUnSell(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if (!plot.isOwner(player)) {
			Output.simpleError(player,
					"Only the owner may remove the plot from the market.");
			return;
		}
		plot.setSalePrice(0);
		Output.positiveMessage(player, "This plot is no longer for sale.");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Upgrade plot at player.
	 */
	private void plotUpgrade(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null) {
			Output.plotNotIn(player);
			return;
		}

		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player,
					"Only the owner and co-owners may upgrade the plot.");
			return;
		}

		if (args.length < 2) {
			Output.positiveMessage(player, plot.getName() + "'s Upgrades:");

			if (plot.getUpgrades().size() == 0)
				Output.simpleError(player, plot.getName()
						+ " has not been upgraded.");
			else
				for (final PlotUpgrade upgrade : plot.getUpgrades())
					player.sendMessage(ChatColor.YELLOW + "- "
							+ upgrade.getName());
			Output.positiveMessage(player, "-Plot Upgrades Available-");
			for (final PlotUpgrade upgrade : PlotUpgrade.values()) {
				if (plot.isUpgrade(upgrade))
					continue;
				player.sendMessage(ChatColor.YELLOW + "- " + upgrade.getName()
						+ " (" + Utils.getDecimalFormater().format(upgrade.getPrice()) + " gc)");
			}
		} else if (args.length >= 2) {

			final PlotUpgrade upgrade = PlotUpgrade.getByName(StringUtils.join(
					args, " ", 1, args.length));

			if (upgrade == null) {
				Output.positiveMessage(player, "-Plot Upgrades Available-");
				for (final PlotUpgrade u : PlotUpgrade.values()) {
					if (plot.isUpgrade(u))
						continue;
					player.sendMessage(ChatColor.YELLOW + "- " + u.getName()
							+ " (" + Utils.getDecimalFormater().format(u.getPrice()) + " gc)");
				}
				return;
			}

			if (!plot.isUpgrade(upgrade)) {
				if (plot.getMoney() >= upgrade.getPrice()) {
					if (upgrade.equals(PlotUpgrade.NEUTRALALIGNMENT)
							&& !plot.isUpgrade(PlotUpgrade.TOWN)) {
						Output.simpleError(player,
								"must be a town to purchase this upgrade.");
						return;
					}
					plot.addUpgrade(upgrade);
					plot.setMoney(plot.getMoney() - upgrade.getPrice());
					Output.positiveMessage(player, plot.getName()
							+ " upgraded to " + upgrade.getName() + ".");
				} else
					Output.simpleError(
							player,
							"Not enough money in plot funds. ("
									+ Utils.getDecimalFormater().format(upgrade.getPrice())
									+ " gc)");
			} else
				Output.simpleError(player, plot.getName()
						+ " already have the upgrade " + upgrade.getName()
						+ ".");
		}
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Withdraw money from plot founds at player.
	 */
	private void plotWithdraw(Player player, String[] args) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
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
		} catch (final Exception e) {
			Output.simpleError(player, ".");
			return;
		}
		if (amount > 0) {
			final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
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

}