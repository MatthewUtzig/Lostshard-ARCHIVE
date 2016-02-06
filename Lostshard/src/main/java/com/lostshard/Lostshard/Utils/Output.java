package com.lostshard.Lostshard.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
			p.playSound(p.getLocation(), Sound.ARROW_HIT, 1, 1);
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
		for(Plot p : PlotManager.getManager().getCapturePoints()) {
			sender.sendMessage(ChatColor.YELLOW + p.getName()+": "+p.getLocation().getBlockX() + ", " +
		p.getLocation().getBlockY() + ", " + p.getLocation().getBlockZ() );	
		}
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

	public static void displayStats(CommandSender sender) {
		if (sender instanceof Player)
			Output.playerStats((Player) sender);
		else
			sender.sendMessage(ChatColor.DARK_RED + "You must be a player to perform this command.");
	}

	public static void displayTitles(Player player) {
		player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Titles-");
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		for (final String title : pPlayer.getTitles())
			player.sendMessage(ChatColor.YELLOW + " - " + ChatColor.WHITE + title);
	}

	public static void displayWho(CommandSender sender, String[] args) {
		if (args.length < 1) {
			Output.simpleError(sender, "Invalid syntax, use /whois (player name)");
			return;
		}

		final String targetName = args[0];
		final Player p = Bukkit.getPlayer(targetName);
		if (p != null || Lostshard.isVanished(p) && !sender.isOp()) {
			final PseudoPlayer targetPseudoPlayer = pm.getPlayer(p);
			Output.outputWho(sender, targetPseudoPlayer);
		} else
			Output.simpleError(sender, "That player is not online.");
		return;
	}

	public static void gainSkill(Player player, String skillName, int gainAmount, int totalSkill) {
		if (gainAmount <= 0)
			return;
		player.sendMessage(ChatColor.GOLD + "You have gained " + Utils.scaledIntToString(gainAmount) + " " + skillName
				+ ", it is now " + Utils.scaledIntToString(totalSkill) + ".");
	}

	public static void help(CommandSender sender) {

	}

	public static void mustBePlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Only players may perform this command!");
	}

	public static void notNumber(Player player) {
		simpleError(player, "Need to be a number.");
	}

	public static void outputClanInfo(Player player, Clan clan) {
		player.sendMessage(ChatColor.GOLD + "-" + clan.getName() + "'s Info-");
		player.sendMessage(ChatColor.YELLOW + "Clan Owner: " + ChatColor.WHITE
				+ Bukkit.getOfflinePlayer(clan.getOwner()).getName());

		player.sendMessage(ChatColor.YELLOW + "Clan Leaders: " + ChatColor.WHITE
				+ Utils.listToString(Utils.UUIDArrayToUsernameArray(clan.getLeaders())));

		player.sendMessage(ChatColor.YELLOW + "Clan Members: " + ChatColor.WHITE
				+ Utils.listToString(Utils.UUIDArrayToUsernameArray(clan.getMembers())));
	}

	public static void outputPlayerlist(CommandSender sender) {
		final ArrayList<String> filteredPlayers = new ArrayList<String>();
		for (final Player p : Bukkit.getOnlinePlayers())
			if (!(Lostshard.isVanished(p) && !sender.isOp()))
				filteredPlayers.add(Utils.getDisplayName(p));

		Collections.sort(filteredPlayers, String.CASE_INSENSITIVE_ORDER);
		String message = ChatColor.YELLOW + "Online Players (" + filteredPlayers.size() + "/"
				+ Lostshard.getMaxPlayers() + "):" + ChatColor.WHITE + " ";
		message += StringUtils.join(filteredPlayers, ChatColor.RESET + ", ");
		sender.sendMessage(message);
	}

	public static void outputRunebook(Player player, String[] split) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if (pseudoPlayer.isAllowGui()) {
			final GUI gui = new RunebookGUI(pseudoPlayer);
			gui.openInventory(player);
			return;
		}
		player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Runebook-");
		final Runebook runebook = pseudoPlayer.getRunebook();
		final List<Rune> runes = runebook.getRunes();

		int totalRunes = runes.size();
		if (!player.isOp() && !pseudoPlayer.wasSubscribed())
			if (totalRunes > 8)
				totalRunes = 8;
		final int numPages = (totalRunes - 1) / 8 + 1;

		if (player.isOp())
			player.sendMessage(ChatColor.YELLOW + "Pg 1 of " + numPages + " (" + totalRunes
					+ " of lots of runes used) [ Use /runebook page (#) ]");
		else if (pseudoPlayer.wasSubscribed())
			player.sendMessage(ChatColor.YELLOW + "Pg 1 of " + numPages + " (" + totalRunes
					+ " of 16 runes used) [ Use /runebook page (#) ]");
		else
			player.sendMessage(ChatColor.YELLOW + "Pg 1 of " + numPages + " (" + totalRunes + " of 8 runes used)");

		if (split.length >= 2 && split[1].equalsIgnoreCase("page") && totalRunes > 0) {
			int page = 0;
			try {
				page = Integer.parseInt(split[2]);
			} catch (final Exception e) {
				page = -1;
			}

			if (page < 0 || page > numPages) {
				Output.simpleError(player, "Invalid page.");
				return;
			}
			// valid page

			final int startingRune = (page - 1) * 8;
			int finalRune = (page - 1) * 8 + 7;
			if (finalRune >= totalRunes)
				finalRune = totalRunes - 1;

			// output
			for (int i = startingRune; i <= finalRune; i++) {
				final Location loc = runes.get(i).getLocation();
				player.sendMessage("- " + runes.get(i).getLabel() + " (" + loc.getBlockX() + "," + loc.getBlockY() + ","
						+ loc.getBlockZ() + ")");
			}
		} else if (runes.size() > 0) {
			int numToDisplay = totalRunes;
			if (numToDisplay >= 8)
				numToDisplay = 8;
			for (int i = 0; i < numToDisplay; i++) {
				final Location loc = runes.get(i).getLocation();
				player.sendMessage("- " + runes.get(i).getLabel() + " (" + loc.getBlockX() + "," + loc.getBlockY() + ","
						+ loc.getBlockZ() + ")");
			}
		} else
			player.sendMessage(ChatColor.RED + "You do not have any runes.");
	}

	public static void outputScrolls(Player player, String[] args) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if (pseudoPlayer.isAllowGui()) {
			final GUI gui = new ScrollGUI(pseudoPlayer);
			gui.openInventory(player);
			return;
		}
		final Set<Scroll> scrolls = new HashSet<>(pseudoPlayer.getScrolls());
		player.sendMessage(ChatColor.GOLD + "-" + player.getName() + "'s Scrolls-");
		player.sendMessage(ChatColor.YELLOW + "(\"+\" means it is already in your spellbook)");
		if (scrolls.size() > 0) {
			final StringBuilder output = new StringBuilder();
			for (final Scroll s : scrolls) {
				int scrollAmount = 1;
				scrollAmount = Collections.frequency(pseudoPlayer.getScrolls(), s);
				if (pseudoPlayer.getSpellbook().containSpell(s))
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

	public static void outputSpellbook(Player player, String[] args) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		final SpellBook spellbook = pseudoPlayer.getSpellbook();
		if (args.length == 2) {
			final String secondaryCommand = args[0];
			if (secondaryCommand.equalsIgnoreCase("page")) {
				int pageNumber;
				try {
					pageNumber = Integer.parseInt(args[1]);
				} catch (final Exception e) {
					pageNumber = -1;
				}
				if (pseudoPlayer.isAllowGui()) {
					final GUI gui = new SpellbookPageGUI(pseudoPlayer, pageNumber);
					gui.openInventory(player);
					return;
				}
				if (pageNumber >= 1 && pageNumber <= 9) {
					player.sendMessage(
							ChatColor.GOLD + "-" + player.getName() + "'s Spellbook [Page " + pageNumber + "]-");
					int minMagery = (pageNumber - 1) * 12;
					if (pageNumber == 9)
						minMagery = 100;
					player.sendMessage(ChatColor.YELLOW + "-Minimum Magery: " + minMagery);
					player.sendMessage(ChatColor.YELLOW + "Spell Name - (Reagent Cost)");
					final ArrayList<Scroll> spellsOnPage = spellbook.getSpellsOnPage(pageNumber);
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
			}
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
//		sender.sendMessage(ChatColor.YELLOW + "Rank: " + ChatColor.WHITE + whoPseudoPlayer.getRank());
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
				+ Utils.getDecimalFormater().format(pseudoPlayer.getMoney()));
		player.sendMessage(ChatColor.YELLOW + "Mana: " + ChatColor.WHITE + pseudoPlayer.getMana() + "/" + pseudoPlayer.getMaxMana());
		player.sendMessage(ChatColor.YELLOW + "Stamina: " + ChatColor.WHITE + pseudoPlayer.getStamina() + "/" + pseudoPlayer.getMaxStamina());
		player.sendMessage(ChatColor.YELLOW + "Build: " + ChatColor.WHITE + pseudoPlayer.getCurrentBuildId());
//		player.sendMessage(
//				ChatColor.YELLOW + "Reputation: " + ChatColor.WHITE + pseudoPlayer.getReputation().getReputation());
//		// player.sendMessage(ChatColor.YELLOW + "Rank: " + ChatColor.WHITE
		// + pseudoPlayer.getRank());
	}

	public static void plotInfo(Player player, Plot plot) {
		player.sendMessage(ChatColor.GOLD + "-" + plot.getName() + "'s Plot Info-");
		String infoText = "";
		if (plot.isProtected())
			infoText += ChatColor.YELLOW + "Protected: " + ChatColor.WHITE + "Yes";
		else
			infoText += ChatColor.YELLOW + "Protected: " + ChatColor.WHITE + "No";
		infoText += ChatColor.YELLOW + ", ";
		if (plot.isPrivatePlot())
			infoText += ChatColor.YELLOW + "Status: " + ChatColor.WHITE + "Private";
		else
			infoText += ChatColor.YELLOW + "Status: " + ChatColor.WHITE + "Public";
		infoText += ChatColor.YELLOW + ", ";
		if (plot.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.WHITE + "Neutral";
		else if (pm.isCriminal(plot.getOwner()))
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.RED + "Criminal";
		else
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.BLUE + "Lawful";
		player.sendMessage(infoText);

		infoText = "";
		if (plot.isAllowExplosions())
			infoText += ChatColor.YELLOW + "Allow Explosions: " + ChatColor.WHITE + "Yes";
		else
			infoText += ChatColor.YELLOW + "Allow Explosions: " + ChatColor.WHITE + "No";

		player.sendMessage(infoText);

		// Display your position in the plot
		if (plot.isOwner(player))
			player.sendMessage(ChatColor.YELLOW + "You are the owner of this plot.");
		else if (plot.isCoowner(player))
			player.sendMessage(ChatColor.YELLOW + "You are a co-owner of this plot.");
		else if (plot.isFriend(player))
			player.sendMessage(ChatColor.YELLOW + "You are a friend of this plot.");
		else
			player.sendMessage(ChatColor.YELLOW + "You are not a friend of this plot.");

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
					+ ", Funds: " + ChatColor.WHITE + Utils.getDecimalFormater().format(plot.getMoney())
					+ ChatColor.YELLOW + ", Tax: " + ChatColor.WHITE + Utils.getDecimalFormater().format(plot.getTax())
					+ ChatColor.YELLOW + ", Plot Value: " + ChatColor.WHITE
					+ Utils.getDecimalFormater().format(plot.getValue()));
			player.sendMessage(ChatColor.GRAY + "(" + Utils.getDecimalFormater().format(plot.getMoney() / plot.getTax())
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
					+ Utils.listToString(Utils.UUIDArrayToUsernameArray(plot.getCoowners())));
			player.sendMessage(ChatColor.YELLOW + "Friends: " + ChatColor.WHITE
					+ Utils.listToString(Utils.UUIDArrayToUsernameArray(plot.getFriends())));
		}
	}

	public static void plotNotIn(Player player) {
		simpleError(player, "You are not currently in a plot.");
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
