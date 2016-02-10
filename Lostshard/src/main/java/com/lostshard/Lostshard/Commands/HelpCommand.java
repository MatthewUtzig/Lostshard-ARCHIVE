package com.lostshard.Lostshard.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;

public class HelpCommand {

	@Command(aliases = { "" }, desc = "Shows all the help commands")
	public void help(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "All help commands:");
		sender.sendMessage(ChatColor.YELLOW + "/help plot");
		sender.sendMessage(ChatColor.YELLOW + "/help clan");
		sender.sendMessage(ChatColor.YELLOW + "/help party");
		sender.sendMessage(ChatColor.YELLOW + "/help skills");
		sender.sendMessage(ChatColor.YELLOW + "/help magery");
		sender.sendMessage(ChatColor.YELLOW + "/help karma");
	}
	
	@Command(aliases = { "land", "plot", "plots" }, desc = "Tells you how to create and use plots.")
	public void land(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 8) int page) {
			
		sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
		sender.sendMessage(ChatColor.GOLD + "Page " + page + " of 8, use \"/help land (page)\"");
			
		switch(page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY
					+ " You can protect your base, house, items, etc. from other players by creating a plot.");
			sender.sendMessage(ChatColor.GRAY + "-In a plot normal players cannot press stone buttons or break blocks;");
			sender.sendMessage(ChatColor.GRAY + "-however, they can use all types of pressure plates, wooden buttons, and wooden doors.");				
			sender.sendMessage(ChatColor.GRAY + "-Normal players can open chests if that do not have blocks placed over them.");
			sender.sendMessage(ChatColor.GRAY + "-Also keep in mind that plot commands take effect on the plot that you are standing in.");	
			sender.sendMessage(ChatColor.GOLD + "Tax:" + ChatColor.GRAY + " Each real life day tax is taken from your plot.");	
			sender.sendMessage(ChatColor.GRAY + "-tax = 10 * (the size of your plot)");
			sender.sendMessage(ChatColor.GRAY + "-If you fail to pay your tax, your plot will be shrunk by one block");
			break;	
		case 2:	
			sender.sendMessage(ChatColor.GOLD + "Commands:");
			sender.sendMessage(ChatColor.YELLOW + "/plot create (plot name)" + ChatColor.GRAY + " - This creates a plot");
			sender.sendMessage(ChatColor.GRAY + "-This costs 1000 YELLOW and 1 diamond for the first plot.");
			sender.sendMessage(ChatColor.GRAY + "-The price inceases for the more plots you create.");			
			sender.sendMessage(ChatColor.GRAY + "-The plot starts with a 10 block radius, which can be increased later.");
			sender.sendMessage(ChatColor.YELLOW + "/plot survey" + ChatColor.GRAY + " - tells you how large you could make a plot in a given area");
			sender.sendMessage(ChatColor.GRAY + "-You must be outside of a plot when executing this command.");
			sender.sendMessage(ChatColor.YELLOW + "/plot info" + ChatColor.GRAY + " - gives info about a plot");
			break;
		case 3:
			sender.sendMessage(ChatColor.GOLD + "Owner Commands:");
			sender.sendMessage(ChatColor.YELLOW + "-the owner gets access to all co-owner and friend commands.");
			sender.sendMessage(ChatColor.YELLOW + "/plot co-own (player)");
			sender.sendMessage(ChatColor.GRAY + "-Gives a player the ability to use all co-owner and friend commands (shown below),");
			sender.sendMessage(ChatColor.GRAY + "-the ability to break blocks, and the ability to use stone buttons.");
			sender.sendMessage(ChatColor.YELLOW + "/plot friend (player)");
			sender.sendMessage(ChatColor.GRAY + "-Gives a player the ability to use all friend commands(shown below)");
			sender.sendMessage(ChatColor.GRAY + "-They can't break blocks, but they can use stone buttons.");
			sender.sendMessage(ChatColor.GRAY + "-Co-owners can also use this command.");
			break;
		case 4:
			sender.sendMessage(ChatColor.GOLD + "Owner Commands continued:");
			sender.sendMessage(ChatColor.YELLOW + "/plot disband" + ChatColor.GRAY + " - Deletes the plot you are standing on.");
			sender.sendMessage(ChatColor.GRAY + "-This gives you all the YELLOW in the plot");
			sender.sendMessage(ChatColor.YELLOW + "/plot transfer (player)" + ChatColor.GRAY + " - Gives owner to someone else." );
			sender.sendMessage(ChatColor.GRAY + "-This will remove you from the plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot protect/unprotect" + ChatColor.GRAY + " - protects/unprotects a plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot list" + ChatColor.GRAY + " - List all your current plots.");
			break;
		case 5:
			sender.sendMessage(ChatColor.GOLD + "Co-owner commands:");
			sender.sendMessage(ChatColor.YELLOW + "/plot withdraw/deposit");
			sender.sendMessage(ChatColor.GRAY + "-Allows you to add or remove YELLOW from your plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot expand/shrink" + ChatColor.GRAY + " - Increase/decrease the plot size by 1 block.");
			sender.sendMessage(ChatColor.GRAY + "-Expanding costs YELLOW, which increases the bigger your plot is.");
			sender.sendMessage(ChatColor.GRAY + "-Shrinking costs nothing.");
			sender.sendMessage(ChatColor.YELLOW + "/plot upgrade (upgrade)" + ChatColor.GRAY + " - Allows you to buy plot upgrades for gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-upgrades are on pages 7 and 8.");
			break;
		case 6:
			sender.sendMessage(ChatColor.GOLD + "Co-owner commands continued:");
			sender.sendMessage(ChatColor.YELLOW + "/plot test" + ChatColor.GRAY + " - Toggles whether you are testing a plot.");
			sender.sendMessage(ChatColor.GRAY + "-Testing a plot prevents you from breaking blocks and using stone buttons.");
			sender.sendMessage(ChatColor.GRAY + "-Friends can also test, but this only removes the ability to press stone buttons.");
			sender.sendMessage(ChatColor.YELLOW + "/plot friend/unfriend" + ChatColor.GRAY + " - Promote a player to friend or.");
			sender.sendMessage(ChatColor.GRAY + "-demote a player to a non-member.");
			sender.sendMessage(ChatColor.GOLD + "Friends:");
			sender.sendMessage(ChatColor.YELLOW + "/plot deposit" + ChatColor.GRAY + " - Friends can donate gold to the plot, ");
			sender.sendMessage(ChatColor.GRAY + "-but they can't withdraw.");
			break;
		case 7:
			sender.sendMessage(ChatColor.GOLD + "Plot upgrades:");
			sender.sendMessage(ChatColor.YELLOW + "Town:" + ChatColor.GRAY + " Costs 100,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows any player to set their spawn with a bed in your plot.");
			sender.sendMessage(ChatColor.GRAY + "-Allows the ability to spawn a banker or a vender in your plot ");
			sender.sendMessage(ChatColor.GRAY + "-with /plot npc hire [banker/vender] (name).");
			sender.sendMessage(ChatColor.GRAY + "-To move a npc: /plot npc move (name).");
			sender.sendMessage(ChatColor.GRAY + "-To remove a npc: /plot npc fire (name).");
			sender.sendMessage(ChatColor.YELLOW + "Neutral Alignment:" + ChatColor.GRAY + " Costs 2,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows both criminals and non-crimals to set spawn in a bed.");
			sender.sendMessage(ChatColor.YELLOW + "AutoKick:" + ChatColor.GRAY + " Costs 5,000 gold coins. ");
			sender.sendMessage(ChatColor.GRAY + "-When a player relogs in your plot they are sent out of it.");
			break;
		case 8:
			sender.sendMessage(ChatColor.GOLD + "Plot upgrades Continued:");
			sender.sendMessage(ChatColor.YELLOW + "Dungeon:" + ChatColor.GRAY + " Costs 10,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows hostile mobs to spawn in your plot.");
			sender.sendMessage(ChatColor.YELLOW + "/plot downgrade (upgrade)" + ChatColor.GRAY + " -removes a plot upgrade.");
			break;
		}
	}
	
	@Command(aliases = { "clan", "clans" }, desc = "Tells you how to create and manage clans.")
	public void clan(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 3) int page) {
		sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
		sender.sendMessage(ChatColor.GOLD + "Page " + page + " of 3, use \"/help clan (page)\"");
		switch(page) {
		case 1:
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY + " Unlike parties, Clans are permanent player groups.");
			sender.sendMessage(ChatColor.GRAY + "A clan is required to capture hostility. This can be done by doing /claim while in hostility.");
			sender.sendMessage(ChatColor.GRAY + "Capturing hostility will reward you with 5 free gold ingots from the vender.");
			sender.sendMessage(ChatColor.GRAY + "You can also teleport to your clanmates by casting Clan Teleport.");
			break;
		case 2:
			sender.sendMessage(ChatColor.YELLOW + "/clan create (plot name)" + ChatColor.GRAY + " - Create a clan.");
			sender.sendMessage(ChatColor.GRAY + "-Costs 2,000 YELLOW coins.");
			sender.sendMessage(ChatColor.YELLOW + "/clan transfer (player name)" + ChatColor.GRAY + "  Gives your clan to another player.");
			sender.sendMessage(ChatColor.YELLOW + "/clan [invite/uninvite] (player name)" + ChatColor.GRAY+ "  Invites/uninvites a player to your clan.");
			break;
		case 3:
			sender.sendMessage(ChatColor.YELLOW + "/clan promote (player name)" + ChatColor.GRAY  + " - Promotes a member to leader. This can only be used by the owner.");
			sender.sendMessage(ChatColor.GRAY + "-Leaders can use most commands and can kick players from the clan.");
			sender.sendMessage(ChatColor.YELLOW + "/clan demote (player name)" + ChatColor.GRAY + " - Demotes a clan leader back to member.");
			sender.sendMessage(ChatColor.YELLOW + "/clan kick (player name)" + ChatColor.GRAY + " - Kicks a player from your clan.");
			sender.sendMessage(ChatColor.YELLOW + "/clan leave" + ChatColor.GRAY + " - Leaves your clan. You can't be the owner");
			sender.sendMessage(ChatColor.YELLOW + "/clan disband" + ChatColor.GRAY + " - Disbands your clan. This can be used by owner only.");
			sender.sendMessage(ChatColor.YELLOW + "/clan info" + ChatColor.GRAY + " - Displays information about your clan. Leaders and owners get more information.");
			sender.sendMessage(ChatColor.YELLOW + "/c (message)" + ChatColor.GRAY + " - Send a chat message to your clan.");
			sender.sendMessage(ChatColor.YELLOW + "/c" + ChatColor.GRAY + " - Makes all future messages in clan chat. Use /g to get back.");
			break;
		}	
	}
	
	@Command(aliases = { "party", "parties", "partys" }, desc = "Tells you what parties are.")
	public void party(CommandSender sender) {
			sender.sendMessage(ChatColor.GOLD + "-Party Help-");
			sender.sendMessage(ChatColor.YELLOW + "Info:" + ChatColor.GRAY + " Unlike clans, a party is a temporary player group. You can only be in one party at a time.");
			sender.sendMessage(ChatColor.GRAY + "-You cannot damage party member; however, if both players toggle friendly fire with /ff they can damage each other.");
			sender.sendMessage(ChatColor.GRAY + "-When you leave the game you are automatically removed from your party.");
			sender.sendMessage(ChatColor.GOLD + "Commands:");
			sender.sendMessage(ChatColor.YELLOW + "/party join (player name)" + ChatColor.GRAY + " - Join a player's party if you have been invited by that player.");
			sender.sendMessage(ChatColor.YELLOW + "/party invite (player name)" + ChatColor.GRAY + " - Invites a player to your party. Anyone in a party can invite people.");
			sender.sendMessage(ChatColor.YELLOW + "/party leave" + ChatColor.GRAY + " - Leaves your party.");
			sender.sendMessage(ChatColor.YELLOW + "/party info" + ChatColor.GRAY + " - Displays who is in your current party.");
	}
	
	@Command(aliases = { "" }, desc = "Help")
	public void skills(CommandSender sender, int page) {
		
	}
	
	@Command(aliases = { "" }, desc = "Help")
	public void magery(CommandSender sender, int page) {
		
	}
	
	@Command(aliases = { "" }, desc = "Help")
	public void karma(CommandSender sender, int page) {
		
	}
	
}
