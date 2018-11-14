package com.lostshard.Lostshard.Commands;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.CommandManager.Annotations.Sender;
import com.lostshard.CommandManager.Annotations.Vanish;
import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Events.EventManager;
import com.lostshard.Lostshard.Events.PlotCreateEvent;
import com.lostshard.Lostshard.Handlers.HelpHandler;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableLocation;
import com.lostshard.Lostshard.Objects.Player.OfflineMessage;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.lostshard.Plots.NPC;
import com.lostshard.Plots.NPCType;
import com.lostshard.Plots.PlotManager;
import com.lostshard.Plots.Models.Plot;
import com.lostshard.Plots.Models.Plot.PlotEffect;
import com.lostshard.Plots.Models.Plot.PlotToggleable;
import com.lostshard.Plots.Models.Plot.PlotUpgrade;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;
import com.sk89q.intake.parametric.annotation.Switch;
import com.sk89q.intake.parametric.annotation.Text;
import com.sk89q.intake.parametric.annotation.Validate;

/**
 * @author Jacob Emil Ulvedal Rosborg
 *
 */
public class PlotCommand {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();

	
	@Command(aliases = {"help", ""}, desc = "Shows a help menu", usage="(page)")
	public void help(CommandSender sender, @Optional(value = "1") int page) {
		HelpHandler.helpLandOwnership(sender, page);
	}
	
	/**
	 * @param player
	 * @param args
	 *
	 *            Create plot at player.
	 */
	@Command(aliases = { "create" }, desc = "creates a plot", usage = "<name>", 
			help = "Creats a plot with a given name, it cost 1000 gc and a diamond, this cost increase for every plot you create with in a week.")
	public void createPlot(@Sender Player player, @Sender PseudoPlayer pPlayer, @Validate(regex="\\w{2,"+Variables.plotMaxNameLength+"}") @Text String plotName) {
		// get recently purchased plots
		final int plotCreatePoints = pPlayer.getPlotCreatePoints();
		int plotsThisWeek = 0;
		if (plotCreatePoints > 0)
			plotsThisWeek = (int) Math.ceil(plotCreatePoints / 7);

		if (player.isOp())
			plotsThisWeek = 0;

		int plotMoneyCost = Variables.plotCreatePrice;
		int plotDiamondCost = Variables.plotCreateItemPrice.getAmount();
		// for each owned plot, double the price
		// for each owned plot, double the price
		plotMoneyCost *= Math.pow(2, plotsThisWeek);
		plotDiamondCost *= Math.pow(2, plotsThisWeek);
		// make sure the player has enough money and diamonds
		if (!player.isOp() && !pPlayer.getWallet().contains(plotMoneyCost)
				&& ItemUtils.containsAmount(player.getInventory(), Variables.plotCreateItemPrice.getType()) >= plotDiamondCost) {
			Output.simpleError(player, "can't afford to create a plot, cost: " + plotMoneyCost + " gold & "
					+ plotDiamondCost + " diamonds.");
			return;
		}
		// verify that this plot wouldn't intersect with an existing plot
		final ArrayList<Plot> intersectingRegions = new ArrayList<Plot>();

		final int nameLength = plotName.length();
		if (nameLength < 1) {
			Output.simpleError(player, "/plot create (name)");
			return;
		}
		
		// find intersecting regions and check names to make sure there isn't a
		// region with that name already
		final Location curLoc = player.getLocation().getBlock().getLocation();
		for (final Plot plot : this.ptm.getPlots()) {
			if (plot.getName().equalsIgnoreCase(plotName)) {
				Output.simpleError(player, "A plot with that name already exists, please choose another.");
				return;
			}
			if (!curLoc.getWorld().equals(plot.getLocation().getWorld()))
				continue;
			int sphereOfInfluence;
			if (plot.isCoownerOrAbove(player))
				sphereOfInfluence = plot.getSize();
			else if (plot.getUpgrades().contains(PlotUpgrade.TOWN))
				sphereOfInfluence = plot.getSize() * 2;
			else
				sphereOfInfluence = (int) Math.ceil(plot.getSize() * 1.5);

			if (Utils.isWithin(curLoc, plot.getLocation(), sphereOfInfluence + Variables.plotStartingSize))
				intersectingRegions.add(plot);
		}

		if (intersectingRegions.size() == 0) {
			final PlotCreateEvent event = new PlotCreateEvent(player, pPlayer, player.getLocation(), plotName);
			EventManager.callEvent(event);

			if (event.isCancelled()) {
				Output.simpleError(player, "Some thing went wrong");
				return;
			}

			// money/diamonds verified, placement verified, name verified: good
			// to go
			// first, remove the money/diamonds
			if (!player.isOp())
				pPlayer.getWallet().subtract(null, plotMoneyCost, "Plot creation");

			pPlayer.setPlotCreatePoints(pPlayer.getPlotCreatePoints() + 7);
			// debited money successfully, now remove the proper amount of
			// diamonds
			ItemUtils.removeItem(player.getInventory(), Variables.plotCreateItemPrice.getType(), plotDiamondCost);
			// costs paid, create the plot

			final Plot plot = new Plot(plotName, player.getUniqueId(), curLoc);
			plot.insert();
			this.ptm.getPlots().add(plot);
			Output.positiveMessage(player,
					"You have created the plot \"" + plot.getName() + "\", it cost "
							+ Utils.getDecimalFormater().format(plotMoneyCost) + " gc and "
							+ plotDiamondCost + " diamonds.");
		} else {
			player.sendMessage(ChatColor.DARK_RED + "can't create a plot there, too close to the following plots:");
			int maxDisplay = 6;
			if (intersectingRegions.size() < maxDisplay)
				maxDisplay = intersectingRegions.size();
			for (int i = 0; i < maxDisplay; i++)
				player.sendMessage(ChatColor.DARK_RED + "-" + intersectingRegions.get(i).getName());
		}
	}

	@Command(aliases = { "setcapzone" }, desc = "Sets the location and range for capzone")
	@Require("plot.admin")
	public void plotSetCapzone(@Sender Player player, @Sender Plot plot, @Optional(value="5") int range) {
		if(!plot.isCapturepoint()) {
			Output.simpleError(player, "This plot is not a capturepoint");
			return;
		}
		plot.getCapturepointData().setCapZone(new SavableLocation(player.getLocation()));
		plot.getCapturepointData().setRange(range);
		Output.positiveMessage(player, "Plot cap position and range have been set.");			
	}

	@Command(aliases = { "effect" }, desc = "Addes effects to plot")
	@Require("lostshard.plot.admin")
	public void plotEffect(@Sender Player player, @Sender Plot plot, @Optional PlotEffect effect, @Switch('r') boolean remove) {
		if(effect == null) {
			player.sendMessage(ChatColor.GOLD + "-Effects Available-");
			for(PlotEffect pe : PlotEffect.values())
				if(plot.getEffects().contains(pe))
					continue;
				else
					player.sendMessage(ChatColor.YELLOW + "- " + pe.name().toLowerCase());
			player.sendMessage(ChatColor.GOLD+"-" + plot.getName() + "'s Effects-");
			for(PlotEffect pe : plot.getEffects())
				player.sendMessage(ChatColor.YELLOW + "- " + pe.name().toLowerCase());
			return;
		}
		if(plot.getUpgrades().contains(effect)) {
			if(remove) {
				plot.getUpgrades().remove(effect);
				Output.positiveMessage(player, "Have removed the effect "+effect.name().toLowerCase()+" from the plot "+plot.getName()+".");
			}else
				Output.simpleError(player, "The plot already have this effect");
			return;
		}	
		
		plot.getEffects().add(effect);
		Output.positiveMessage(player, "Have added the effect "+effect.name().toLowerCase()+" to the plot "+plot.getName()+".");
	}

	/**
	 * @param player
	 *
	 *            Buy plot at player.
	 */
	@Command(aliases = {"buy"}, desc = "Buys the plot if its for sale")
	public void plotBuy(@Sender Player player, @Sender Plot plot) {
		final int salePrice = plot.getSalePrice();
		if (salePrice <= 0) {
			Output.simpleError(player, "This plot is not for sale.");
			return;
		}

		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);

		if (pseudoPlayer.getWallet().contains(salePrice)) {
			Output.simpleError(player, "can't afford to buy plot, cost: " + salePrice + ".");
			return;
		}

		if (plot.getOwner().equals(player.getUniqueId())) {
			Output.simpleError(player, "You can't buy your own plot. Use /unlist to remove your plot from the market.");
			return;
		}

		final UUID lastOwner = plot.getOwner();
		final Player sellerPlayer = Bukkit.getPlayer(lastOwner);
		final PseudoPlayer sellerPseudoPlayer = this.pm.getPlayer(sellerPlayer);
		
		pseudoPlayer.getWallet().subtract(sellerPseudoPlayer.getWallet(), salePrice, "Plot sale");
		plot.setOwner(player.getUniqueId());
		plot.setSalePrice(0);
		plot.getCoowners().clear();
		plot.getFriends().clear();
		
		Output.positiveMessage(player, "You have purchased the plot " + plot.getName() + " from "
				+ Bukkit.getOfflinePlayer(lastOwner).getName() + " for " + salePrice + ".");
		if (sellerPlayer.isOnline())
			sellerPlayer.sendMessage(
					"You have sold the plot " + plot.getName() + " to " + player.getName() + " for " + salePrice + ".");
		else
			new OfflineMessage(sellerPlayer.getUniqueId(),
					"You have sold the plot " + plot.getName() + " to " + player.getName() + " for " + salePrice + ".");
		sellerPseudoPlayer.save();

	}

	@Command(aliases = { "capturepoint" }, desc = "Toggles plot capturepoint")
	@Require("lostshard.plot.admin")
	public void plotCapturePoint(@Sender Player player, @Sender Plot plot) {
		if (plot.isCapturepoint()) {
			plot.setCapturepoint(false);
			plot.setCapturepointData(null);
			Output.positiveMessage(player, "You have turend " + plot.getName() + " into a normal plot.");
		} else {
			plot.setCapturepoint(true);
			Output.positiveMessage(player, "You have turend " + plot.getName() + " into a capturepoint.");
		}
	}

	/**
	 * @param player
	 *            the executor of the command
	 * @param args
	 *            the message that contains the player name, who the executor
	 *            wants to co-own.
	 *
	 *            Co-owner player of plot at player.
	 */
	@Command(aliases = { "co-owner", "coown", "co", "coowner", "co-own" }, desc = "Makes a given player co-owner of the plot", usage ="<player>")
	public void plotCoOwn(@Sender Player player, @Sender Plot plot, @Vanish Player targetPlayer) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may co-owner players.");
			return;
		}

		if (player == targetPlayer) {
			Output.simpleError(player, "You may not co-owner your self.");
			return;
		}

		plot.getFriends().remove(targetPlayer.getUniqueId());

		if (plot.getCoowners().contains(targetPlayer)) {
			Output.simpleError(player, "They are already a friend of the plot.");
			return;
		}

		plot.getCoowners().add(targetPlayer);
		Output.positiveMessage(player, targetPlayer.getName() + " is now a co-owner of this plot.");
		Output.positiveMessage(targetPlayer, "You are now a co-owner of " + plot.getName() + ".");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Deposit money into plot found at player.
	 */
	@Command(aliases = { "deposit" }, desc = "Deposit a given amount of gold coins into the plot", usage ="<amount>")
	public void plotDeposit(@Sender Player player, @Sender Plot plot, @Range(min=0) int amount) {
		if (!plot.isFriendOrAbove(player)) {
			Output.simpleError(player, "Only friends may deposit money into " + plot.getName() + ".");
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (pPlayer.getWallet().contains(amount)) {
			plot.getWallet().add(pPlayer.getWallet(), amount, "Plot deposit");
			Output.positiveMessage(player, "You have deposited " + Utils.getDecimalFormater().format(amount)
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
	@Command(aliases = { "disband" }, desc = "Disbands a plot")
	public void plotDisband(@Sender Player player, @Sender PseudoPlayer pPlayer, @Sender Plot plot) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may disband " + plot.getName() + ".");
			return;
		}
		int value = plot.getValue();
		plot.disband();
		pPlayer.getWallet().add(plot.getWallet(), plot.getWallet().intValue(), "Plot disband - founds");
		pPlayer.getWallet().add(plot.getWallet(), value, "Plot disband - value");
		// Output positive message that plot has bin disbanded and the value of
		// the plot.
		Output.positiveMessage(player, "You have disbanded " + plot.getName() + ", and got "
				+ Utils.getDecimalFormater().format(plot.getValue()) + " gc.");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Downgrade plot at player.
	 */
	@Command(aliases = { "downgrade" }, desc = "Downgrade a plot from a given upgrade", usage ="<upgrade>")
	public void plotDowngrade(@Sender Player player, @Sender Plot plot, @Optional PlotUpgrade upgrade) {
		if (plot.isOwner(player)) {

			if (upgrade == null) {
				Output.positiveMessage(player, plot.getName() + "'s Upgrades:");

				if (plot.getUpgrades().size() == 0)
					Output.simpleError(player, plot.getName() + " has not been upgraded.");
				else
					for (final PlotUpgrade u : plot.getUpgrades())
						player.sendMessage(ChatColor.YELLOW + "- " + u.getName() + " ("
								+ Utils.getDecimalFormater().format(u.getPrice()) + " gc)");
				Output.positiveMessage(player, "-Plot Upgrades Available-");
				for (final PlotUpgrade u : PlotUpgrade.values()) {
					if (plot.getUpgrades().contains(u))
						continue;
					player.sendMessage(ChatColor.YELLOW + "- " + u.getName());
				}
			} else {
				if (plot.getUpgrades().contains(upgrade)) {
					plot.getUpgrades().remove(upgrade);
					int amount = (int) Math.floor(upgrade.getPrice() * .75);
					plot.getWallet().add(plot.getWallet(), amount, "Plot downgrade");
					Output.positiveMessage(player,
							plot.getName() + " downgrade from a " + upgrade.getName() + " to a normal plot.");
					if (upgrade.equals(PlotUpgrade.TOWN) && plot.getUpgrades().contains(PlotUpgrade.NEUTRALALIGNMENT)) {
						plot.getUpgrades().contains(PlotUpgrade.NEUTRALALIGNMENT);
						plot.getWallet().add(plot.getWallet(), (int) Math.floor(PlotUpgrade.NEUTRALALIGNMENT.getPrice() * 75), "Plot downgrade - neutralalignment");
					}
					if(upgrade.equals(PlotUpgrade.TOWN))
						for(NPC n : plot.getNpcs())
							if(n.getType().equals(NPCType.BANKER))
								n.fire();
				} else
					Output.simpleError(player, plot.getName() + " does not have the upgrade \""+upgrade.getName()+"\"");
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
	@Command(aliases = { "expand" }, desc = "Expands the plot with a given amount", usage ="(amount)")
	public void plotExpand(@Sender Player player, @Sender Plot plot, @Optional(value = {"1"}) @Range(min=1)int amount) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may expand the plot.");
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
			else if (p.getUpgrades().contains(PlotUpgrade.TOWN))
				sphereOfInfluence = p.getSize() * 2;
			else
				sphereOfInfluence = (int) Math.ceil(p.getSize() * 1.5);

			if (Utils.isWithin(p.getLocation(), plot.getLocation(), sphereOfInfluence + plot.getSize() + 1 + amount)) {
				if (amount == 1)
					Output.simpleError(player, "can't expand, " + p.getName() + " is too close.");
				else
					Output.simpleError(player,
							"You may only expand " + plot.getName() + " to size"
									+ Utils.getDecimalFormater()
											.format((int) Math
													.floor(p.getLocation().distanceSquared(player.getLocation())))
							+ ".");
				return;
			}
		}

		// verify that we can afford the expansion
		final int expansionCost = plot.getExpandPrice(plot.getSize() + amount);
		if (plot.getWallet().contains(expansionCost)) {
			// we are good to go
			plot.getWallet().subtract(null, expansionCost, "Plot expand");
			plot.setSize(plot.getSize() + amount);
			Output.positiveMessage(player, "You have expanded the plot to size " + plot.getSize() + ".");
		} else
			Output.simpleError(player, "Not enough money in the plot treasury to expand. " + "It will cost "
					+ expansionCost + " to expand to " + (plot.getSize() + amount));
	}

	/**
	 * @param player
	 *
	 *            Toggle explosions for plot at player.
	 */
	@Command(aliases = { "explosion", "explosions", "toggleexplosion" , "toggleexplosions" }, desc = "Toggles explosions")
	public void plotExplosionToggle(@Sender Player player, @Sender Plot plot) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owners may toggle explosions.");
			return;
		}
		if (plot.getToggleables().contains(PlotToggleable.EXPLOSIONS)) {
			plot.getToggleables().remove(PlotToggleable.EXPLOSIONS);
			Output.positiveMessage(player, "You have disabled explosions on your plot.");
		} else {
			plot.getToggleables().add(PlotToggleable.EXPLOSIONS);
			Output.positiveMessage(player, "You have enabled explosions on your plot.");
		}
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Friend player of plot at player.
	 */
	@Command(aliases = { "friend" }, desc = "Makes a given player friend of the plot", usage ="<player>")
	public void plotFriend(@Sender Player player, @Sender Plot plot, @Vanish Player targetPlayer) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may friend players.");
			return;
		}

		if (player == targetPlayer) {
			Output.simpleError(player, "You may not friend your self.");
			return;
		}

		if (plot.getCoowners().contains(targetPlayer) && !plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may friend co-owners.");
			return;
		}

		if (plot.getFriends().contains(targetPlayer)) {
			Output.simpleError(player, "They are already a friend of the plot.");
			return;
		}

		if (plot.getCoowners().contains(targetPlayer))
			plot.getCoowners().remove(targetPlayer.getUniqueId());
		plot.getFriends().add(targetPlayer);
		Output.positiveMessage(player, targetPlayer.getName() + " is now a friend of this plot.");
		Output.positiveMessage(targetPlayer, "You are now a friend of " + plot.getName() + ".");
	}

	/**
	 * @param player
	 *
	 *            Toggle friendbuild for plot at player.
	 */
	@Command(aliases = { "friendbuild" }, desc = "Toggles friendbuild")
	public void plotFriendBuildToggle(@Sender Player player, @Sender Plot plot) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may toggle friendbuild.");
			return;
		}
		if (!plot.getToggleables().contains(PlotToggleable.FRIENDBUILD)) {
			plot.getToggleables().add(PlotToggleable.FRIENDBUILD);
			Output.positiveMessage(player, "You have turned on friend build for " + plot.getName() + ".");
			return;
		} else {
			plot.getToggleables().remove(PlotToggleable.FRIENDBUILD);
			Output.positiveMessage(player, "You have turned off friend build for " + plot.getName() + ".");
			return;
		}
	}

	/**
	 * @param player
	 *
	 *            Display info for plot at player.
	 */
	@Command(aliases = { "info" }, desc = "Shows information about the plot")
	public void plotInfo(@Sender Player player, @Sender Plot plot) {
		Output.plotInfo(player, plot);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            List plots for player.
	 */
	@Command(aliases = { "list" }, desc = "Shows a list of your plots")
	public void plotList(@Sender Player player, @Optional String name) {
		if (name != null && player.isOp()) {
			@SuppressWarnings("deprecation")
			final OfflinePlayer tPlayer = Bukkit.getOfflinePlayer(name);
			if (tPlayer == null) {
				Output.simpleError(player, "No player exist whith that name.");
				return;
			}
			Output.positiveMessage(player, "-" + tPlayer.getName() + "'s Plots-");
			for (final Plot plot : this.ptm.getPlots())
				if (plot.getOwner().equals(tPlayer.getUniqueId()))
					player.sendMessage(" - " + plot.getName() + " (" + plot.getSize() + ") @("
							+ plot.getLocation().getBlockX() + "," + plot.getLocation().getBlockY() + ","
							+ plot.getLocation().getBlockZ() + ")");
		} else {
			Output.positiveMessage(player, "-" + player.getName() + "'s Plots-");
			boolean foundOne = false;
			for (final Plot plot : this.ptm.getPlots())
				if (plot.isOwner(player)) {
					foundOne = true;
					player.sendMessage(" - " + plot.getName() + " (" + plot.getSize() + ") @("
							+ plot.getLocation().getBlockX() + "," + plot.getLocation().getBlockY() + ","
							+ plot.getLocation().getBlockZ() + ")");
				}
			if (!foundOne)
				Output.simpleError(player, "You do not currently own any plots.");
		}
	}

	/**
	 * @param player
	 *
	 *            Toggles magic for plot at player
	 */
	@Command(aliases = { "magic" }, desc = "Toggles magic for a plot")
	@Require("lostshard.plot.admin")
	public void plotMagicToggle(@Sender Player player, @Sender Plot plot) {
		if (plot.getToggleables().add(PlotToggleable.NOMAGIC)) {
			Output.positiveMessage(player, "You have turned off magic for " + plot.getName() + ".");
		} else if(plot.getToggleables().remove(PlotToggleable.NOMAGIC)) {
			Output.positiveMessage(player, "You have turned on magic for " + plot.getName() + ".");
		}
	}

	/**
	 * @param player
	 *
	 *            Make plot private at player.
	 */
	@Command(aliases = { "private" }, desc = "Sets the plot to private")
	public void plotPrivate(@Sender Player player, @Sender Plot plot) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may make the plot private.");
			return;
		}
		if (plot.getToggleables().add(PlotToggleable.PRIVATE))
			Output.positiveMessage(player, "You have made the plot private.");
		else {
			Output.simpleError(player, "The plot is already private.");
		}
	}

	/**
	 * @param player
	 *
	 *            Toggle protection for plot at player.
	 */
	@Command(aliases = { "protect" }, desc = "Toggles protection for a plot")
	public void plotProtect(@Sender Player player, @Sender Plot plot) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may toggle protection.");
			return;
		}
		if (plot.getToggleables().remove(PlotToggleable.PROTECTION)) {
			Output.positiveMessage(player, "You have turned off protection for " + plot.getName() + ".");
		} else {
			Output.positiveMessage(player, "You have turned on protection for " + plot.getName() + ".");
		}
	}

	/**
	 * @param player
	 *
	 *            Make plot public at player.
	 */
	@Command(aliases = { "public" }, desc = "Sets the plot to public")
	public void plotPublic(@Sender Player player, @Sender Plot plot) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may make the plot public.");
			return;
		}
		if (plot.getToggleables().remove(PlotToggleable.PRIVATE))
			Output.positiveMessage(player, "You have made the plot public.");
		else {
			Output.simpleError(player, "The plot is already public.");
		}
	}

	/**
	 * @param player
	 *
	 *            Toggle pvp for plot at player.
	 */
	@Command(aliases = { "pvp" }, desc = "Toggles pvp for the plot")
	@Require("losthard.plot.admin")
	public void plotPvpToggle(@Sender Player player, @Sender Plot plot) {
		if (plot.getToggleables().add(PlotToggleable.NOPVP)) {
			Output.positiveMessage(player, "You have turned off pvp for " + plot.getName() + ".");
		} else {
			Output.positiveMessage(player, "You have turned on pvp for " + plot.getName() + ".");
		}
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Rename plot at player.
	 */
	@Command(aliases = { "rename" }, desc = "Renames the plot", usage ="<name>")
	public void plotRename(@Sender Player player, @Sender Plot plot, @Validate(regex="\\w{2,"+Variables.plotMaxNameLength+"}") @Text String plotName) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may rename the plot.");
			return;
		}
		// Verify that we can afford the rename.
		if (plot.getWallet().contains(Variables.plotRenamePrice)) {
			Output.simpleError(player, "Not enough in the plot treasury to rename. "
					+ Utils.getDecimalFormater().format(Variables.plotRenamePrice) + " gc.");
			return;
		}
		// Figure out the name that the player input

		plotName = plotName.trim();

		boolean nameValid = true;
		if (plotName.contains("\"") || plotName.contains("'")) {
			Output.simpleError(player, "can't use \" in plot name.");
			nameValid = false;
		}

		final int nameLength = plotName.length();
		if (nameLength > Variables.plotMaxNameLength && nameValid) {
			Output.simpleError(player, "Name is invalid or too long, limit is 20 characters.");
			return;
		}

		for (final Plot p : this.ptm.getPlots())
			if (p.getName().equalsIgnoreCase(plotName)) {
				Output.simpleError(player, "can't use that name, it is taken.");
				return;
			}
		// We are good to go
		plot.getWallet().subtract(null, Variables.plotRenamePrice, "Plot rename");
		plot.setName(plotName);
		Output.positiveMessage(player, "You have renamed the plot to " + plotName + ".");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Sell plot at player.
	 */
	@Command(aliases = { "sell" }, desc = "Puts the plot on the plot market", usage ="<price>")
	public void plotSell(@Sender Player player, @Sender Plot plot, @Range(min=1, max=9999999) int price) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may sell the plot.");
			return;
		}

		if (price < 1) {
			Output.simpleError(player, "Invalid price, must be 1 or greater");
			return;
		}

		plot.setSalePrice(price);
		Output.positiveMessage(player, "You have set this plot for sale at " + price + " gc.");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Shrink plot at player
	 */
	@Command(aliases = { "shrink" }, desc = "Shrinks the plot with a given amount", usage ="(amount)")
	public void plotShrink(@Sender Player player, @Sender Plot plot, @Optional(value="1") @Range(min=1) int amount) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may shrink the plot.");
			return;
		}

		if (plot.getSize() <= amount) {
			Output.simpleError(player, "can't shrink that much.");
			return;
		}

		plot.setSize(plot.getSize() - amount);
		Output.positiveMessage(player, "You have shrunk the plot by " + amount + " blocks.");
	}

	/**
	 * @param player
	 *
	 *            Survey nearby plots of player.
	 */
	@Command(aliases = { "survey" }, desc = "Survey the nearby plots")
	public void plotSurvey(@Sender Player player) {
		Plot plot = ptm.findPlotAt(player.getLocation());
		final Set<Plot> plots = this.ptm.getPlots();

		// First, determine if we are currently in a plot
		if (plot != null && !plot.getCoowners().contains(player)) {
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(
					ChatColor.YELLOW + "You " + ChatColor.RED + "can't" + ChatColor.YELLOW + " create a plot here.");
			player.sendMessage(ChatColor.YELLOW + "You are currently in the plot \"" + plot.getName() + "\".");
			return;
		}

		// Determine the max range
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		final int miningSkill = pseudoPlayer.getCurrentBuild().getMining().getLvl();
		final double percent = (double) miningSkill / 1000;
		final int range = (int) Math.ceil(200 * percent) + 100;

		final Location curLoc = new Location(player.getWorld(), player.getLocation().getBlockX() + .5,
				player.getLocation().getBlockY() + .5, player.getLocation().getBlockZ() + .5);

		// If we are not in a plot, determine if there are any plots within
		// range
		final ArrayList<Plot> plotsInRange = new ArrayList<Plot>();
		for (Plot p : plots) {
			if (!p.getLocation().getWorld().equals(player.getWorld()))
				continue;
			if (plot != null && p.getName().equalsIgnoreCase(plot.getName()))
				continue;
			int border;
			if (p.isCoownerOrAbove(player))
				border = p.getSize();
			else if (p.getUpgrades().contains(PlotUpgrade.TOWN))
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
			player.sendMessage(
					ChatColor.YELLOW + "You " + ChatColor.GOLD + "can" + ChatColor.YELLOW + " create a plot here.");
			player.sendMessage(ChatColor.YELLOW + "There are no plots within " + range + " blocks of here.");

			final int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
			int plotsThisWeek = 0;
			if (plotCreatePoints > 0)
				plotsThisWeek = (int) Math.ceil(plotCreatePoints / 7);
			int plotMoneyCost = Variables.plotCreatePrice;
			int plotDiamondCost = Variables.plotCreateItemPrice.getAmount();
			// for each owned plot, double the price
			plotMoneyCost *= Math.pow(2, plotsThisWeek);
			plotDiamondCost *= Math.pow(2, plotsThisWeek);

			player.sendMessage(ChatColor.YELLOW + "It would cost " + plotMoneyCost + " gc and " + plotDiamondCost
					+ " diamonds to create a size 10 plot here.");
			return;
		}

		// If there ARE plots within range
		double closestDist = 99999;
		Plot closestPlot = null;
		final int numPlotsInRange = plotsInRange.size();
		for (int i = 0; i < numPlotsInRange; i++) {
			final Plot p = plotsInRange.get(i);
			int border;
			if (p.isCoownerOrAbove(player)) {
				border = p.getSize();
			} else {
				if (p.getUpgrades().contains(PlotUpgrade.TOWN))
					border = p.getSize() * 2;
				else
					border = (int) Math.ceil(p.getSize() * 1.5);
			}

			// Determine the distance from the player to the border of the plot
			final double dist = Utils.distance(curLoc, p.getLocation()) - border;
			// System.out.println("Dist to "+p.getName()+" - "+dist);

			if (dist < closestDist) {
				closestDist = dist;
				closestPlot = p;
			}
		}

		if (plot != null && plot.isCoownerOrAbove(player)) {
			if (closestPlot != null) {
				player.sendMessage(ChatColor.YELLOW + "The nearest plot is \"" + closestPlot.getName() + "\"");
				player.sendMessage(ChatColor.YELLOW + "it is " + (Variables.plotStartingSize + (int) closestDist - 10)
						+ " blocks from here.");
			}

			if (closestDist > range)
				closestDist = range;

			player.sendMessage(ChatColor.YELLOW + "If you expanded this plot, you could expand it to at least size "
					+ (Variables.plotStartingSize + (int) (closestDist - 10)));
			return;
		}

		// Pretty much can't happen, but I'm checking for it anyway
		if (closestPlot == null) {
			Output.simpleError(player, "Something went wrong, tell an admin =(");
			return;
		}

		// Determine whether the closest plot border is within 10 blocks of the
		// player's position (plot minimum size)
		if (closestDist <= Variables.plotStartingSize) {
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(
					ChatColor.YELLOW + "You " + ChatColor.RED + "cannot" + ChatColor.YELLOW + " create a plot here.");
			player.sendMessage(ChatColor.YELLOW + "This location is too close to the plot");
			player.sendMessage(ChatColor.YELLOW + "\"" + closestPlot.getName() + "\".");
			return;
		}

		// We are not within the min plot size of an existing plot
		Output.positiveMessage(player, "-Plot Survey Results-");
		player.sendMessage(
				ChatColor.YELLOW + "You " + ChatColor.GOLD + "can" + ChatColor.YELLOW + " create a plot here.");
		player.sendMessage(ChatColor.YELLOW + "The nearest plot is \"" + closestPlot.getName() + "\"");
		player.sendMessage(ChatColor.YELLOW + "it is " + (Variables.plotStartingSize + (int) closestDist - 10)
				+ " blocks from here. If you created a");
		player.sendMessage(ChatColor.YELLOW + "plot here, you could expand it to about size "
				+ (Variables.plotStartingSize + (int) (closestDist - 10)));

		final int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
		int plotsThisWeek = 0;
		if (plotCreatePoints > 0)
			plotsThisWeek = (int) Math.ceil(plotCreatePoints / 7);
		int plotMoneyCost = Variables.plotCreatePrice;
		int plotDiamondCost = Variables.plotCreateItemPrice.getAmount();
		// for each owned plot, double the price
		plotMoneyCost *= Math.pow(2, plotsThisWeek);
		plotDiamondCost *= Math.pow(2, plotsThisWeek);

		player.sendMessage(ChatColor.YELLOW + "It would cost " + plotMoneyCost + "gc and " + plotDiamondCost
				+ " diamonds to create a size 10 plot here.");
		return;
	}

	/**
	 * @param player
	 *
	 *            Toggle test for player and plot.
	 */
	@Command(aliases = { "test" }, desc = "Toggles plot test")
	public void plotTestToggle(@Sender Player player, @Sender PseudoPlayer pPlayer, @Sender Plot plot) {
		if (pPlayer.getTestPlot() != null) {
			Output.positiveMessage(player, "You are no longer testing " + pPlayer.getTestPlot().getName() + ".");
			pPlayer.setTestPlot(null);
			return;
		}
		pPlayer.setTestPlot(plot);
		Output.positiveMessage(player, "You are now testing " + plot.getName() + ".");
	}

	@Command(aliases = { "title" }, desc = "Toggles plot title")
	@Require("lostshard.plot.admin")
	public void plotTitle(@Sender Player player, @Sender Plot plot) {
		if (plot.getToggleables().contains(PlotToggleable.TITLE)) {
			Output.positiveMessage(player, "You have turned off title for " + plot.getName() + ".");
			plot.getToggleables().remove(PlotToggleable.TITLE);
		} else {
			Output.positiveMessage(player, "You have turned on title for " + plot.getName() + ".");
			plot.getToggleables().add(PlotToggleable.TITLE);
		}
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Transfer plot at player to target player.
	 */
	@Command(aliases = { "transfer" }, desc = "Transfer the ownership of a plot to another player", usage ="<player>")
	public void plotTransfer(@Sender Player player, @Sender Plot plot, @Vanish Player target) {
		if (!plot.isOwner(player) && !player.isOp()) {
			Output.simpleError(player, "Only the owner may transfer the plot.");
			return;
		}
		if (plot.getOwner().equals(target.getUniqueId())) {
			Output.simpleError(player, "You can't transfer your plot to yourself.");
			return;
		}
		plot.getCoowners().remove(target);
		plot.getFriends().remove(target);
		plot.setOwner(target.getUniqueId());
		Output.positiveMessage(player, "Transferred plot \"" + plot.getName() + "\" to " + target.getName());
		Output.positiveMessage(target, player.getName() + " transferred plot \"" + plot.getName() + "\" to you.");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Unfriend player of plot at player.
	 */
	@Command(aliases = { "unfriend" }, desc = "Unfriends a give player from the plot", usage ="<player>")
	public void plotUnFriend(@Sender Player player, @Sender Plot plot, OfflinePlayer target) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owners may unfriend players from this plot.");
			return;
		}
		
		if (target == player) {
			Output.simpleError(player, "You may not unfriend yourself.");
			return;
		}

		if (!plot.isFriendOrAbove(target.getUniqueId())) {
			Output.simpleError(player, target.getName() + " is not a friend or co-owner of this plot.");
			return;
		}

		if (plot.getCoowners().contains(target)
				&& plot.getCoowners().contains(player.getUniqueId())) {
			Output.simpleError(player, "Only the owner may unfriend a co-owner.");
			return;
		}

		if (plot.getCoowners().contains(target.getUniqueId())) {
			Output.positiveMessage(player, target.getName() + " is no longer a friend of this plot.");
			if (target.isOnline())
				Output.positiveMessage(target.getPlayer(),
						"You are no longer a friend of " + plot.getName() + ".");
			plot.getCoowners().remove(target.getUniqueId());
		} else {
			Output.positiveMessage(player, target.getName() + " is no longer a friend of this plot.");
			if (target.isOnline())
				Output.positiveMessage(target.getPlayer(),
						"You are no longer a friend of " + plot.getName() + ".");
			plot.getFriends().remove(target.getUniqueId());
		}
	}

	/**
	 * @param player
	 *
	 *            Take plot of the market.
	 */
	@Command(aliases = { "unsell" }, desc = "Takes the plot off the market")
	public void plotUnSell(@Sender Player player, @Sender Plot plot) {
		if (!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may remove the plot from the market.");
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
	@Command(aliases = { "upgrade" }, desc = "Upgrade the plot to the given upgrade", usage ="<upgrade>")
	public void plotUpgrade(@Sender Player player, @Sender Plot plot, @Optional PlotUpgrade upgrade) {

		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owners may upgrade the plot.");
			return;
		}

		if (upgrade == null) {
			Output.positiveMessage(player, plot.getName() + "'s Upgrades:");

			if (plot.getUpgrades().size() == 0)
				Output.simpleError(player, plot.getName() + " has not been upgraded.");
			else
				for (final PlotUpgrade u : plot.getUpgrades())
					player.sendMessage(ChatColor.YELLOW + "- " + u.getName());
			Output.positiveMessage(player, "-Plot Upgrades Available-");
			for (final PlotUpgrade u : PlotUpgrade.values()) {
				if (plot.getUpgrades().contains(u))
					continue;
				player.sendMessage(ChatColor.YELLOW + "- " + u.getName() + " ("
						+ Utils.getDecimalFormater().format(u.getPrice()) + " gc)");
			}
		} else {

			if (!plot.getUpgrades().contains(upgrade)) {
				if (plot.getWallet().contains(upgrade.getPrice())) {
					if (upgrade.equals(PlotUpgrade.NEUTRALALIGNMENT) && !plot.getUpgrades().contains(PlotUpgrade.TOWN)) {
						Output.simpleError(player, "must be a town to purchase this upgrade.");
						return;
					}
					plot.getWallet().subtract(null, upgrade.getPrice(), "Plot upgrade");
					plot.getUpgrades().add(upgrade);
					Output.positiveMessage(player, plot.getName() + " upgraded to " + upgrade.getName() + ".");
				} else
					Output.simpleError(player, "Not enough money in plot funds. ("
							+ Utils.getDecimalFormater().format(upgrade.getPrice()) + " gc)");
			} else
				Output.simpleError(player, plot.getName() + " already have the upgrade " + upgrade.getName() + ".");
		}
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Withdraw money from plot founds at player.
	 */
	@Command(aliases = { "withdraw" }, desc = "Withdraws money from a plot", usage ="<amount>")
	public void plotWithdraw(@Sender Player player, @Sender PseudoPlayer pPlayer, @Sender Plot plot, @Range(min=1) int amount) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner or co-owners may withdraw from the plot funds.");
			return;
		}
		if (plot.getWallet().subtract(pPlayer.getWallet(), amount, "Plot withdraw")) {
			Output.positiveMessage(player, "You have withdrawn " + Utils.getDecimalFormater().format(amount)
					+ " gold coins from the plot fund.");
		} else
			Output.simpleError(player, "The plot does not contain enough gold coins.");
	}

}