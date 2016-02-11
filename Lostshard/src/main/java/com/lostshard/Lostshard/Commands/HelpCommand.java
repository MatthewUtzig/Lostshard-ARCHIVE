package com.lostshard.Lostshard.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;

public class HelpCommand {

	@Command(aliases = { "" }, desc = "Shows all the help commands")
	public void help(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 1) int page) {
		if (page == 1) {
			sender.sendMessage(ChatColor.GOLD + "All help commands:");
			sender.sendMessage(ChatColor.GOLD + "/help plot");
			sender.sendMessage(ChatColor.GOLD + "/help clan");
			sender.sendMessage(ChatColor.GOLD + "/help party");
			sender.sendMessage(ChatColor.GOLD + "/help skills");
			sender.sendMessage(ChatColor.GOLD + "/help magery");
			sender.sendMessage(ChatColor.GOLD + "/help karma");
			
		}
	}
	
	@Command(aliases = { "land", "plot", "plots" }, desc = "Tells you how to create and use plots.")
	public void land(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 8) int page) {
		if (page == 1) { 
			sender.sendMessage(ChatColor.GOLD + "-Plot Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 1 of 8, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY
					+ " You can protect your base, house, items, etc. from other players by creating a plot.");
			sender.sendMessage(ChatColor.GRAY + "-In a plot normal players cannot press stone buttons or break blocks;");
			sender.sendMessage(ChatColor.GRAY + "-however, they can use all types of pressure plates, wooden buttons, and wooden doors.");				
			sender.sendMessage(ChatColor.GRAY + "-Normal players can open chests if that do not have blocks placed over them.");
			sender.sendMessage(ChatColor.GRAY + "-Also keep in mind that plot commands take effect on the plot that you are standing in.");	
			sender.sendMessage(ChatColor.GOLD + "Tax:" + ChatColor.GRAY + " Each real life day tax is taken from your plot.");	
			sender.sendMessage(ChatColor.GRAY + "-tax = 10 * (the size of your plot)");
			sender.sendMessage(ChatColor.GRAY + "-If you fail to pay your tax, your plot will be shrunk by one block");
							
			
		} else if (page == 2) { 
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 2 of 8, use \"/help land (page)\"");	
			sender.sendMessage(ChatColor.GOLD + "Commands:");
			sender.sendMessage(ChatColor.GRAY + "/plot create (plot name)" + ChatColor.GRAY + " - This creates a plot");
			sender.sendMessage(ChatColor.GRAY + "-This costs 1000 gold and 1 diamond for the first plot.");
			sender.sendMessage(ChatColor.GRAY + "-The price inceases for the more plots you create.");			
			sender.sendMessage(ChatColor.GRAY + "-The plot starts with a 10 block radius, which can be increased later.");
			sender.sendMessage(ChatColor.GRAY + "/plot survey" + ChatColor.GRAY + " - tells you how large you could make a plot in a given area");
			sender.sendMessage(ChatColor.GRAY + "-You must be outside of a plot when executing this command.");
			sender.sendMessage(ChatColor.GRAY + "/plot info" + ChatColor.GRAY + " - gives info about a plot");
		
		} else if (page == 3) {
			
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 3 of 8, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Owner Commands:");
			sender.sendMessage(ChatColor.GOLD + "-the owner gets access to all co-owner and friend commands.");
			sender.sendMessage(ChatColor.GOLD + "/plot co-own (player)");
			sender.sendMessage(ChatColor.GRAY + "-Gives a player the ability to use all co-owner and friend commands (shown below),");
			sender.sendMessage(ChatColor.GRAY + "-the ability to break blocks, and the ability to use stone buttons.");
			sender.sendMessage(ChatColor.GOLD + "/plot friend (player)");
			sender.sendMessage(ChatColor.GRAY + "-Gives a player the ability to use all friend commands(shown below)");
			sender.sendMessage(ChatColor.GRAY + "-They can't break blocks, but they can use stone buttons."); 
					
		} else if (page == 4) {
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 4 of 8, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Owner Commands continued:");
			sender.sendMessage(ChatColor.GOLD + "/plot disband" + ChatColor.GRAY + " - Deletes the plot you are standing on.");
			sender.sendMessage(ChatColor.GRAY + "-This gives you all the gold in the plot");
			sender.sendMessage(ChatColor.GOLD + "/plot transfer (player)" + ChatColor.GRAY + " - Gives owner to someone else." );
			sender.sendMessage(ChatColor.GRAY + "-This will remove you from the plot.");
			sender.sendMessage(ChatColor.GOLD + "/plot protect/unprotect" + ChatColor.GRAY + " - protects/unprotects a plot.");
			sender.sendMessage(ChatColor.GOLD + "/plot list" + ChatColor.GRAY + " - List all your current plots.");
		} else if (page == 5) {
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 5 of 8, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Co-owner commands:");
			sender.sendMessage(ChatColor.GOLD + "/plot withdraw/deposit");
			sender.sendMessage(ChatColor.GRAY + "-Allows you to add or remove gold from your plot.");
			sender.sendMessage(ChatColor.GOLD + "/plot expand/shrink" + ChatColor.GRAY + " - Increase/decrease the plot size by 1 block.");
			sender.sendMessage(ChatColor.GRAY + "-Expanding costs gold, which increases the bigger your plot is.");
			sender.sendMessage(ChatColor.GRAY + "-Shrinking costs nothing.");
			sender.sendMessage(ChatColor.GOLD + "/plot upgrade (upgrade)" + ChatColor.GRAY + " - Allows you to buy plot upgrades for gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-upgrades are on pages 7 and 8.");
			
			
		} else if (page == 6) {
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 6 of 8, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Co-owner commands continued:");
			sender.sendMessage(ChatColor.GOLD + "/plot test" + ChatColor.GRAY + " - Toggles whether you are testing a plot.");
			sender.sendMessage(ChatColor.GRAY + "-Testing a plot prevents you from breaking blocks and using stone buttons.");
			sender.sendMessage(ChatColor.GRAY + "-Friends can also test, but this only removes the ability to press stone buttons.");
			sender.sendMessage(ChatColor.GOLD + "/plot friend/unfriend" + ChatColor.GRAY + " - Promote a player to friend or.");
			sender.sendMessage(ChatColor.GRAY + "-demote a player to a non-member.");
			sender.sendMessage(ChatColor.GOLD + "Friends:");
			sender.sendMessage(ChatColor.GOLD + "/plot deposit" + ChatColor.GRAY + " - Friends can donate gold to the plot, ");
			sender.sendMessage(ChatColor.GRAY + "-but they can't withdraw.");
			
	
		} else if (page == 7) {
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 7 of 8, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Plot upgrades:");
			sender.sendMessage(ChatColor.GOLD + "Town:" + ChatColor.GRAY + " Costs 100,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows any player to set their spawn with a bed in your plot.");
			sender.sendMessage(ChatColor.GRAY + "-Allows the ability to spawn a banker or a vender in your plot ");
			sender.sendMessage(ChatColor.GRAY + "-with /plot npc hire [banker/vender] (name).");
			sender.sendMessage(ChatColor.GRAY + "-To move a npc: /plot npc move (name).");
			sender.sendMessage(ChatColor.GRAY + "-To remove a npc: /plot npc fire (name).");
			sender.sendMessage(ChatColor.GOLD + "Neutral Alignment:" + ChatColor.GRAY + " Costs 2,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows both criminals and non-crimals to set spawn in a bed.");
			sender.sendMessage(ChatColor.GOLD + "AutoKick:" + ChatColor.GRAY + " Costs 5,000 gold coins. ");
			sender.sendMessage(ChatColor.GRAY + "-When a player relogs in your plot they are sent out of it.");
			
		} else if (page == 8) {
			sender.sendMessage(ChatColor.GOLD + "-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 8 of 8, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Plot upgrades Continued:");
			sender.sendMessage(ChatColor.GOLD + "Dungeon:" + ChatColor.GRAY + " Costs 10,000 gold coins.");
			sender.sendMessage(ChatColor.GRAY + "-Allows hostile mobs to spawn in your plot.");
			sender.sendMessage(ChatColor.GOLD + "/plot downgrade (upgrade)" + ChatColor.GRAY + " -removes a plot upgrade.");
		}
	
	}
	
	@Command(aliases = { "clan", "clans" }, desc = "Tells you how to create and manage clans.")
	public void clan(CommandSender sender, @Optional(value = "1") @Range(min = 1, max = 3) int page) {
		
		if (page == 1) {
			sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 1 of 3, use \"/help clan (page)\"");
			sender.sendMessage(ChatColor.GOLD + "Info:" + ChatColor.GRAY
					+ " Unlike parties, Clans are permanent player groups. You can only be in one clan at a time.");
			sender.sendMessage(ChatColor.GRAY + "A clan is required to capture hostility. This can be done by doing /claim while in hostility.");
			sender.sendMessage(ChatColor.GRAY + "You can also teleport to your clanmates by casting Clan Teleport.");
		} else {
			if (page == 2) {
			sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 1 of 3, use \"/help clan (page)\"");
			sender.sendMessage(ChatColor.GOLD + "/clan create (plot name)" + ChatColor.GRAY + " - Create a clan.");
			sender.sendMessage(ChatColor.GRAY + "-Costs 2,000 gold coins.");
			sender.sendMessage(ChatColor.GOLD + "/clan transfer (player name)" + ChatColor.GRAY + "  Gives your clan to another player.");
			sender.sendMessage(ChatColor.GOLD + "/clan [invite/uninvite] (player name)" + ChatColor.GRAY+ "  Invites/uninvites a player to your clan.");
		} else if (page == 3) {
			sender.sendMessage(ChatColor.GOLD + "-Clan Help-");
			sender.sendMessage(ChatColor.GOLD + "Page 3 of 3, use \"/help land (page)\"");

			sender.sendMessage(ChatColor.GOLD + "/clan promote (player name)" + ChatColor.GRAY  + " - Promotes a member to leader. This can only be used by the owner.");
			sender.sendMessage(ChatColor.GRAY + "-Leaders can use most commands and can kick players from the clan.");
			sender.sendMessage(ChatColor.GOLD + "/clan demote (player name)" + ChatColor.GRAY + " - Demotes a clan leader back to member.");
			sender.sendMessage(ChatColor.GOLD + "/clan kick (player name)" + ChatColor.GRAY + " - Kicks a player from your clan.");
			sender.sendMessage(ChatColor.GOLD + "/clan leave" + ChatColor.GRAY + " - Leaves your clan. You can't be the owner");
			sender.sendMessage(ChatColor.GOLD + "/clan disband" + ChatColor.GRAY + " - Disbands your clan. This can be used by owner only.");
			sender.sendMessage(ChatColor.GOLD + "/clan info" + ChatColor.GRAY + " - Displays information about your clan. Leaders and owners get more information.");
			sender.sendMessage(ChatColor.GOLD + "/c (message)" + ChatColor.GRAY + " - Send a chat message to your clan.");
			sender.sendMessage(ChatColor.GOLD + "/c" + ChatColor.GRAY + " - Makes all future messages in clan chat. Use /g to get back.");
			}
		}	
	}
	
	@Command(aliases = { "" }, desc = "Help")
	public void party(CommandSender sender, int page) {
		
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
