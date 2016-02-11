package com.lostshard.Lostshard.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.lostshard.Lostshard.Handlers.ChatHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.InventoryGUI.GUI;
import com.lostshard.Lostshard.Objects.InventoryGUI.RunebookGUI;
import com.lostshard.Lostshard.Objects.InventoryGUI.ScrollGUI;
import com.lostshard.Lostshard.Objects.InventoryGUI.SkillsGUI;
import com.lostshard.Lostshard.Objects.InventoryGUI.SpellbookGUI;
import com.lostshard.Lostshard.Objects.InventoryGUI.SpellbookPageGUI;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.Rune;
import com.lostshard.Lostshard.Objects.Player.Runebook;
import com.lostshard.Lostshard.Objects.Player.SpellBook;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.Plot.PlotToggleable;
import com.lostshard.Lostshard.Objects.Plot.Plot.PlotUpgrade;
import com.lostshard.Lostshard.Skills.Build;
import com.lostshard.Lostshard.Skills.Skill;
import com.lostshard.Lostshard.Spells.Scroll;

import me.olivervscreeper.networkutilities.Message;
import me.olivervscreeper.networkutilities.MessageDisplay;

public class Output {

	static PlayerManager pm = PlayerManager.getManager();

	public static int broadcast(String message) {
		for (final Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.GREEN + message);
			p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1, 1);
		}
		return Bukkit.getOnlinePlayers().size();
	}

	public static int broadcastArea(String message, Location location, int range) {
		int players = 0;
		for (final Player p : Bukkit.getOnlinePlayers()) {
			if (p.getLocation().distance(location) <= range)
				p.sendMessage(ChatColor.GREEN + message);
			players++;
		}
		return players;
	}

	public static void capturePointsInfo(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "-Control Points-");
		for(Plot p : PlotManager.getManager().getCapturePoints())
			sender.sendMessage(ChatColor.YELLOW + p.getName()+": "+p.getLocation().getBlockX() + ", " +
		p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() );	
	}

	public static void displayLoginMessages(Player player) {
		player.sendMessage(ChatColor.GOLD + "Welcome to Lost Shard, www.lostshard.com for more info.");
		player.sendMessage(ChatColor.GOLD + "Lostshard is curently in alpha!");
		player.sendMessage(ChatColor.GOLD + "Visit wiki.lostshard.com for more info");
		player.sendMessage(ChatColor.GOLD + "The guide has valuable information for getting started.");
		player.sendMessage(ChatColor.GOLD + "Use /rules for rules and /help for help");
		player.sendMessage(ChatColor.GOLD + "Please communicate in English when using Global Chat.");
		player.sendMessage(ChatColor.RED + "-Combat logging drops your items on logout.");
		Message m = new Message(Message.BLANK);
		m.addRecipient(player);
		m.send(ChatColor.GOLD + "Welcome to Lostshard", MessageDisplay.TITLE);
		m.send(ChatColor.RED + "ALPHA", MessageDisplay.SUBTITLE);
	}

	public static void displayRules(CommandSender sender) {
		Output.positiveMessage(sender, "-Lost Shard Rules-");
		sender.sendMessage(ChatColor.YELLOW + "Short Version: Don't Cheat");
		sender.sendMessage(ChatColor.YELLOW + "Long Version: http://wiki.lostshard.com/index.php?title=Rules");
	}

	public static void displayTitles(Player player, Player target) {
		player.sendMessage(ChatColor.GOLD + "-" + target.getName() + "'s Titles-");
		final PseudoPlayer pPlayer = pm.getPlayer(target);
		for (final String title : pPlayer.getTitles())
			player.sendMessage(ChatColor.YELLOW + " - " + ChatColor.WHITE + title);
	}

	public static void gainSkill(Player player, String skillName, int gainAmount, int totalSkill) {
		if (gainAmount <= 0)
			return;
		player.sendMessage(ChatColor.GOLD + "You have gained " + Utils.scaledIntToString(gainAmount) + " " + skillName
				+ ", it is now " + Utils.scaledIntToString(totalSkill) + ".");
	}

	public static void outputClanInfo(Player player, Clan clan) {
		player.sendMessage(ChatColor.GOLD + "-" + clan.getName() + "'s Info-");
		player.sendMessage(ChatColor.YELLOW + "Clan Owner: " + ChatColor.WHITE
				+ Bukkit.getOfflinePlayer(clan.getOwner()).getName());

		player.sendMessage(ChatColor.YELLOW + "Clan Leaders: " + ChatColor.WHITE
				+ Joiner.on(", ").join(clan.getLeaders().usernames()));

		player.sendMessage(ChatColor.YELLOW + "Clan Members: " + ChatColor.WHITE
				+ Joiner.on(", ").join(clan.getMembers().usernames()));
	}

	public static void outputPlayerlist(CommandSender sender) {
		final List<String> filteredPlayers = new LinkedList<>();
		for (final Player p : Bukkit.getOnlinePlayers())
			if (!Lostshard.isVanished(p))
				filteredPlayers.add(Utils.getDisplayName(p));

		Collections.sort(filteredPlayers, String.CASE_INSENSITIVE_ORDER);
		sender.sendMessage(ChatColor.YELLOW + "Online Players (" + filteredPlayers.size() + "/"
				+ Lostshard.getMaxPlayers() + ")");
		sender.sendMessage(Joiner.on(", ").join(filteredPlayers));
	}

	public static void outputRunebook(Player player, int page) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if (pseudoPlayer.isAllowGui()) {
			final GUI gui = new RunebookGUI(pseudoPlayer);
			gui.openInventory(player);
			return;
		}
		player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Runebook-");
		final Runebook runebook = pseudoPlayer.getRunebook();

		int totalRunes = runebook.size();
		final int numPages = (int) Math.floor(totalRunes/8) + 1;

		if (player.isOp())
			player.sendMessage(ChatColor.YELLOW + "Pg 1 of " + numPages + " (" + totalRunes
					+ " of lots of runes used) [ Use /runebook page (#) ]");
		else if (pseudoPlayer.wasSubscribed())
			player.sendMessage(ChatColor.YELLOW + "Pg 1 of " + numPages + " (" + totalRunes
					+ " of 16 runes used) [ Use /runebook page (#) ]");
		else
			player.sendMessage(ChatColor.YELLOW + "Pg 1 of " + numPages + " (" + totalRunes + " of 8 runes used)");
		
		if(runebook.size() <= 0) {
			Output.simpleError(player, "You do not have any runes.");
			return;
		}
		
		for(Rune r : FluentIterable.from(runebook).skip(numPages*8).limit(8).toList()) {
			final Location loc = r.getLocation();
			player.sendMessage("- " + r.getLabel() + " (" + loc.getBlockX() + "," + loc.getBlockY() + ","
					+ loc.getBlockZ() + ")");
		}
		
	}

	public static void outputScrolls(Player player) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if (pseudoPlayer.isAllowGui()) {
			final GUI gui = new ScrollGUI(pseudoPlayer);
			gui.openInventory(player);
			return;
		}
		final Set<Scroll> scrolls = new LinkedHashSet<>(pseudoPlayer.getScrolls());
		player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Scrolls-");
		player.sendMessage(ChatColor.YELLOW + "(\"+\" means it is already in your spellbook)");
		if (scrolls.size() > 0) {
			final StringBuilder output = new StringBuilder();
			for (final Scroll s : scrolls) {
				int scrollAmount = 1;
				scrollAmount = Collections.frequency(pseudoPlayer.getScrolls(), s);
				if (pseudoPlayer.getSpellbook().contains(s))
					output.append("+");
				output.append(s.getName() + " (" + scrollAmount + "), ");
			}
			player.sendMessage(output.toString());
		} else
			player.sendMessage(ChatColor.RED + "You do not currently have any scrolls.");

	}

	public static void outputSkills(Player player) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if (pseudoPlayer.isAllowGui()) {
			final GUI gui = new SkillsGUI(pseudoPlayer);
			gui.openInventory(player);
			return;
		}
		player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Skills-");
		player.sendMessage(ChatColor.YELLOW + "You currently have "
				+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getTotalSkillVal()) + "/"
				+ Utils.scaledIntToString(pseudoPlayer.getMaxSkillValTotal()) + " skill points.");
		final Build build = pseudoPlayer.getCurrentBuild();
		for (final Skill s : build.getSkills())
			if (s.isLocked())
				player.sendMessage(ChatColor.YELLOW + s.getName() + "(L):  " + ChatColor.WHITE
						+ Utils.scaledIntToString(s.getLvl()));
			else
				player.sendMessage(
						ChatColor.YELLOW + s.getName() + ": " + ChatColor.WHITE + Utils.scaledIntToString(s.getLvl()));
	}

	public static void outputSpellbook(Player player, int page) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		final SpellBook spellbook = pseudoPlayer.getSpellbook();
		if (page != 0) {
			if (pseudoPlayer.isAllowGui()) {
				final GUI gui = new SpellbookPageGUI(pseudoPlayer, page);
				gui.openInventory(player);
				return;
			}
			if (page >= 1 && page <= 9) {
				player.sendMessage(
						ChatColor.GOLD + "-" + player.getName() + "'s Spellbook [Page " + page + "]-");
				int minMagery = (page - 1) * 12;
				if (page == 9)
					minMagery = 100;
				player.sendMessage(ChatColor.YELLOW + "-Minimum Magery: " + minMagery);
				player.sendMessage(ChatColor.YELLOW + "Spell Name - (Reagent Cost)");
				final ArrayList<Scroll> spellsOnPage = spellbook.getSpellsOnPage(page);
				if (spellsOnPage.size() > 0)
					for (final Scroll scroll : spellsOnPage) {
						final List<ItemStack> reagents = scroll.getReagentCost();
						String reagentString = "(";
						final int numReagents = reagents.size();
						for (int i = 0; i < numReagents; i++) {
							reagentString += reagents.get(i).getType().name();
							if (i < numReagents - 1)
								reagentString += ",";
						}
						reagentString += ")";
						player.sendMessage(
								ChatColor.YELLOW + scroll.getName() + ChatColor.WHITE + " - " + reagentString);
					}
				else
					player.sendMessage(ChatColor.RED + "You don't have any spells on this page.");
			} else
				simpleError(player, "That page doesn't exist, use 1-9");
		} else {
			if (pseudoPlayer.isAllowGui()) {
				final GUI gui = new SpellbookGUI(pseudoPlayer);
				gui.openInventory(player);
				return;
			}
			player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Spellbook-");
			player.sendMessage(ChatColor.YELLOW + "Your spellbook has 9 pages in it. Each page lists the");
			player.sendMessage(ChatColor.YELLOW + "spells and associated reagent costs for one circle");
			player.sendMessage(ChatColor.YELLOW + "of magic. Each page of spells has a minimum magery.");
			player.sendMessage(ChatColor.YELLOW + "the easiest spells are on page 1, and the hardest");
			player.sendMessage(ChatColor.YELLOW + "spells are on page 9.");
			player.sendMessage(ChatColor.YELLOW + "Use /spellbook page (page number)");
			player.sendMessage(ChatColor.YELLOW + "Ex: /spellbook page 1");
		}

	}

	public static void outputWho(CommandSender sender, PseudoPlayer whoPseudoPlayer) {
		sender.sendMessage(ChatColor.GOLD + "-Player Information-");
		sender.sendMessage(ChatColor.YELLOW + "Name: " + whoPseudoPlayer.getColoredName());
		if (whoPseudoPlayer.isMurderer())
			sender.sendMessage(ChatColor.RED + "This player is a murderer.");
		else if (whoPseudoPlayer.isCriminal())
			sender.sendMessage(ChatColor.RED + "This player is a criminal.");
		sender.sendMessage(ChatColor.YELLOW + "Murder Counts: " + ChatColor.WHITE + whoPseudoPlayer.getMurderCounts());
		final Clan clan = whoPseudoPlayer.getClan();
		if (clan != null)
			sender.sendMessage(ChatColor.YELLOW + "Clan: " + ChatColor.WHITE + clan.getName());
		else
			sender.sendMessage(ChatColor.YELLOW + "Clan: " + ChatColor.WHITE + ChatColor.ITALIC + "none");
	}

	public static void playerStats(Player player) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Statistics-");
		player.sendMessage(ChatColor.YELLOW + "Gold Coins: " + ChatColor.WHITE
				+ pseudoPlayer.getWallet());
		player.sendMessage(ChatColor.YELLOW + "Mana: " + ChatColor.WHITE + pseudoPlayer.getMana() + "/" + pseudoPlayer.getMaxMana());
		player.sendMessage(ChatColor.YELLOW + "Stamina: " + ChatColor.WHITE + pseudoPlayer.getStamina() + "/" + pseudoPlayer.getMaxStamina());
		player.sendMessage(ChatColor.YELLOW + "Build: " + ChatColor.WHITE + pseudoPlayer.getCurrentBuildId());
	}

	public static void plotInfo(Player player, Plot plot) {
		player.sendMessage(ChatColor.GOLD + "-" + plot.getName() + "'s Plot Info-");
		String infoText = "";
		if (plot.getToggleables().contains(PlotToggleable.PROTECTION))
			infoText += ChatColor.YELLOW + "Protected: " + ChatColor.WHITE + "Yes";
		else
			infoText += ChatColor.YELLOW + "Protected: " + ChatColor.WHITE + "No";
		infoText += ChatColor.YELLOW + ", ";
		if (plot.getToggleables().contains(PlotToggleable.PRIVATE))
			infoText += ChatColor.YELLOW + "Status: " + ChatColor.WHITE + "Private";
		else
			infoText += ChatColor.YELLOW + "Status: " + ChatColor.WHITE + "Public";
		infoText += ChatColor.YELLOW + ", ";
		if (plot.getUpgrades().contains(PlotUpgrade.NEUTRALALIGNMENT))
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.WHITE + "Neutral";
		else if (pm.isCriminal(plot.getOwner()))
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.RED + "Criminal";
		else
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.BLUE + "Lawful";
		player.sendMessage(infoText);

		infoText = "";
		if (plot.getToggleables().contains(PlotToggleable.EXPLOSIONS))
			infoText += ChatColor.YELLOW + "Allow Explosions: " + ChatColor.WHITE + "Yes";
		else
			infoText += ChatColor.YELLOW + "Allow Explosions: " + ChatColor.WHITE + "No";

		player.sendMessage(infoText);

		// Display your position in the plot
		plot.getPlayerStatusOfPlotString(player);
		
		if (plot.isCapturepoint()) {
			final Clan clan = plot.getCapturepointData().getOwningClan();
			if (clan != null)
				player.sendMessage(ChatColor.YELLOW + "Owning Clan: " + ChatColor.WHITE + clan.getName());
			else
				player.sendMessage(ChatColor.YELLOW + "Owning Clan: " + ChatColor.RED + "NONE");
		}

		// Show this stuff to everyone
		if (plot.getSalePrice() > 0)
			player.sendMessage(
					ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + Bukkit.getOfflinePlayer(plot.getOwner()).getName()
							+ ", " + ChatColor.YELLOW + "Sale Price: " + ChatColor.WHITE + plot.getSalePrice());
		else
			player.sendMessage(
					ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + Bukkit.getOfflinePlayer(plot.getOwner()).getName()
							+ ChatColor.YELLOW + ", " + "Sale Price: " + ChatColor.RED + "Not for sale");
		// Only show the owner/co-owner the amount of money in the region bank
		if (plot.isCoownerOrAbove(player)) {
			player.sendMessage(ChatColor.YELLOW + "Size: " + ChatColor.WHITE + plot.getSize() + ChatColor.YELLOW
					+ ", Funds: " + ChatColor.WHITE + plot.getWallet()
					+ ChatColor.YELLOW + ", Tax: " + ChatColor.WHITE + Utils.getDecimalFormater().format(plot.getTax())
					+ ChatColor.YELLOW + ", Plot Value: " + ChatColor.WHITE
					+ Utils.getDecimalFormater().format(plot.getValue()));
			player.sendMessage(ChatColor.GRAY + "(" + Utils.getDecimalFormater().format(plot.getWallet().intValue() / plot.getTax())
					+ " days worth of funds remaining.)");
			final int distanceFromCenter = (int) Math.round(Utils.distance(player.getLocation(), plot.getLocation()));
			player.sendMessage(ChatColor.YELLOW + "Center: " + ChatColor.WHITE + "(" + plot.getLocation().getBlockX()
					+ "," + plot.getLocation().getBlockY() + "," + plot.getLocation().getBlockZ() + ")"
					+ ChatColor.YELLOW + ", Distance From Center: " + ChatColor.WHITE + distanceFromCenter);
		} else
			player.sendMessage(
					ChatColor.YELLOW + "Size: " + ChatColor.WHITE + Utils.getDecimalFormater().format(plot.getSize()));
		// Show member lists to everyone who is at least a friend
		if (plot.isFriendOrAbove(player)) {
			player.sendMessage(ChatColor.YELLOW + "Co-Owners: " + ChatColor.WHITE
					+ Joiner.on(", ").join(plot.getCoowners().usernames()));
			player.sendMessage(ChatColor.YELLOW + "Friends: " + ChatColor.WHITE
					+ Joiner.on(", ").join(plot.getFriends().usernames()));
		}
	}

	public static void positiveMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GOLD + message);
	}

	public static void sendEffectTextNearby(Player player, String string) {
		// Notify nearby players
		for (final Player p : Bukkit.getOnlinePlayers())
			if (Utils.isWithin(player.getLocation(), p.getLocation(), ChatHandler.getLocalChatRange()))
				p.sendMessage(ChatColor.GRAY + string);
	}

	public static void simpleError(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_RED + message);
	}
}
