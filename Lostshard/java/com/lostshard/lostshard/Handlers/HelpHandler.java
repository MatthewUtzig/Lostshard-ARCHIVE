package com.lostshard.lostshard.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class HelpHandler {
	
	public static void handle(CommandSender sender, String[] split) {
		if(split.length < 1) {
			if(sender instanceof Player) {
				ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
				BookMeta bookMeta = (BookMeta) book.getItemMeta();
				bookMeta.setAuthor(ChatColor.GOLD+"Lostshard guide");
				bookMeta.setDisplayName(ChatColor.GOLD+"Lostahrd guide");
				bookMeta.setTitle(ChatColor.GOLD+"Lostahrd guide");
				
				//Add pages
				bookMeta.addPage(ChatColor.GOLD+""+ChatColor.BOLD+"Lostshard guide\n\n\n"+ChatColor.GOLD+"- "+ChatColor.DARK_PURPLE+"Landowner ship\n"+ChatColor.GOLD+"- "+ChatColor.DARK_PURPLE+"Groups\n");
				
				
				book.setItemMeta(bookMeta);
				Player player = (Player) sender;
				player.getInventory().addItem(book);
				return;
			}
			sender.sendMessage(ChatColor.GOLD+"-Help-");
			sender.sendMessage(ChatColor.GOLD+"For more detailed information you can view the guide at");
			sender.sendMessage(ChatColor.GOLD+"http://www.lostshard.com.");
			sender.sendMessage(ChatColor.GOLD+"Use \"/help (topic)\" to get more information.");
			sender.sendMessage(ChatColor.YELLOW+"Topics:"+ChatColor.GRAY+" chat, land, money, scrolls, clan, party, karma, misc");
		} else {
			String topic = split[0];
			
			if(topic.equalsIgnoreCase("chat"))
				helpChat(sender);
			else if(topic.equalsIgnoreCase("land")||topic.equalsIgnoreCase("plot")||topic.equalsIgnoreCase("plots"))
				helpLandOwnership(sender, split);
			else if(topic.equalsIgnoreCase("money"))
				helpMoney(sender);
			else if(topic.equalsIgnoreCase("skills"))
				helpSkills(sender);
			else if(topic.equalsIgnoreCase("scrolls"))
				helpScrolls(sender);
			else if(topic.equalsIgnoreCase("clan"))
				helpClan(sender, split);
			else if(topic.equalsIgnoreCase("party"))
				helpParty(sender);
			else if(topic.equalsIgnoreCase("karma"))
				helpKarma(sender);
			else if(topic.equalsIgnoreCase("misc"))
				helpMisc(sender);
		}
	}
	
	public static void helpChat(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"-Chat Help-");
		sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" Default chat is global.");
		sender.sendMessage(ChatColor.YELLOW+"/l (message)"+ChatColor.GRAY+" - Global chat, talks in global chat.");
		sender.sendMessage(ChatColor.YELLOW+"/l (message)"+ChatColor.GRAY+" - Local chat, talks to nearby players.");
		sender.sendMessage(ChatColor.YELLOW+"/s (message)"+ChatColor.GRAY+" - Shouts a message");
		sender.sendMessage(ChatColor.YELLOW+"/w (message)"+ChatColor.GRAY+" - Whispers to very close players");
		sender.sendMessage(ChatColor.YELLOW+"/msg (player name) (message)"+ChatColor.GRAY+" - Private message");
	}
	
	public static void helpLandOwnership(CommandSender sender, String[] split) {
		if(split.length < 2) {
			sender.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
			sender.sendMessage(ChatColor.GOLD+"Page 1 of 3, use \"/help land (page)\"");
			sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" You may purchase and manage land by creating plots.");
			sender.sendMessage(ChatColor.GRAY+"For more information see the guide at http://www.lostshard.com");
			
			sender.sendMessage(ChatColor.YELLOW+"/plot create (plot name)"+ChatColor.GRAY+" - Create a plot.");
			sender.sendMessage(ChatColor.GRAY+"-Costs 1000 + 1 diamond for the first plot.");
			sender.sendMessage(ChatColor.GRAY+"-Starts with a size of 10 block radius.");
			sender.sendMessage(ChatColor.YELLOW+"/plot survey"+ChatColor.GRAY+" Helps finding a place for a plot.");
			sender.sendMessage(ChatColor.YELLOW+"/plot info"+ChatColor.GRAY+" Information about current plot.");
		} else {
			String page = split[1];
			if(page.equalsIgnoreCase("1")) {
				sender.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
				sender.sendMessage(ChatColor.GOLD+"Page 1 of 3, use \"/help land (page)\"");
				sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" You may purchase and manage land by creating plots.");
				sender.sendMessage(ChatColor.GRAY+"For more information see the guide at http://www.lostshard.com");
				
				sender.sendMessage(ChatColor.YELLOW+"/plot create (plot name)"+ChatColor.GRAY+" - Create a plot.");
				sender.sendMessage(ChatColor.GRAY+"-Costs 1000 + 1 diamond for the first plot.");
				sender.sendMessage(ChatColor.GRAY+"-Starts with a size of 10 block radius.");
				sender.sendMessage(ChatColor.YELLOW+"/plot survey"+ChatColor.GRAY+" Helps finding a place for a plot.");
				sender.sendMessage(ChatColor.YELLOW+"/plot info"+ChatColor.GRAY+" Information about current plot.");
			} else if(page.equalsIgnoreCase("2")) {
				sender.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
				sender.sendMessage(ChatColor.GOLD+"Page 2 of 3, use \"/help land (page)\"");
				
				sender.sendMessage(ChatColor.YELLOW+"/plot friend/co-own/unfriend (player name)");
				sender.sendMessage(ChatColor.GRAY+"- Add or remove a friend or co-owner");
				sender.sendMessage(ChatColor.YELLOW+"/plot protect/unprotect"+ChatColor.GRAY+" - Protects or unprotects your plot.");
				//player.sendMessage(ChatColor.GRAY+"- Protects or unprotects a plot.");
				sender.sendMessage(ChatColor.YELLOW+"/plot private/public"+ChatColor.GRAY+" - Locks or unlocks your plot.");
				sender.sendMessage(ChatColor.YELLOW+"/plot deposit/withdraw"+ChatColor.GRAY+" - Deposits or withdraws plot funds.");
				sender.sendMessage(ChatColor.YELLOW+"/plot expand"+ChatColor.GRAY+" - Expands your plot.");
				sender.sendMessage(ChatColor.GRAY+"-Costs plot size times 100, so size 11 to 12 would cost 110.");
			} else if(page.equalsIgnoreCase("3")) {
				sender.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
				sender.sendMessage(ChatColor.GOLD+"Page 3 of 3, use \"/help land (page)\"");
				
				sender.sendMessage(ChatColor.YELLOW+"/plot test"+ChatColor.GRAY+" - Lets you test the protection/locking.");
				sender.sendMessage(ChatColor.YELLOW+"/plot endtest"+ChatColor.GRAY+" - Stops testing the protection/locking.");
				sender.sendMessage(ChatColor.YELLOW+"/plot disband"+ChatColor.GRAY+" - Deletes your plot.");
				sender.sendMessage(ChatColor.YELLOW+"/plot disband (name)"+ChatColor.GRAY+" - Deletes another plot you own.");
				sender.sendMessage(ChatColor.YELLOW+"/plot list"+ChatColor.GRAY+" - Lists your current plots.");
			}
		}
	}
	
	public static void helpScrolls(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"-Scrolls Help-");
		sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" When you kill monsters, there is a chance they will drop a scroll. Scrolls can be used to cast spells or you can add them to your spellbook and cast them with the magery skill.");
		sender.sendMessage(ChatColor.YELLOW+"/scrolls"+ChatColor.GRAY+" - See the scrolls you have collected.");
		sender.sendMessage(ChatColor.YELLOW+"/scrolls use (spell name)"+ChatColor.GRAY+" - Use a scroll.");
		sender.sendMessage(ChatColor.YELLOW+"/scrolls spellbook (spell name)"+ChatColor.GRAY+" - Add a scroll to your              spellbook.");
	}

	public static void helpSkills(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"-Skills Help-");
		sender.sendMessage(ChatColor.GOLD+"For an in-depth explation of each skill, view the guide at www.lostshard.com.");
		sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" You can gain skills by performing actions like fighting monsters and casting spells. Each skill can go up to 100 and you can have up to 300 skill points total.");
		sender.sendMessage(ChatColor.YELLOW+"/skills"+ChatColor.GRAY+" - See the skills you currently have.");
		sender.sendMessage(ChatColor.YELLOW+"/skills reduce (skill name) (amount)"+ChatColor.GRAY+" - Reduces a skill.");
	}
	
	public static void helpMoney(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"-Money Help-");
		sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" The currency is gold based, at any time you can trade gold ingots for gold coins.");
		sender.sendMessage(ChatColor.YELLOW+"/stats"+ChatColor.GRAY+" - See various stats including your money.");
		sender.sendMessage(ChatColor.YELLOW+"/tradegold (amount)"+ChatColor.GRAY+" - Trades gold ingots for gold coins.");
		sender.sendMessage(ChatColor.GRAY+"-Exchange rate of 1 gold ingot for 100 gold coins.");
		sender.sendMessage(ChatColor.YELLOW+"/pay (player name) (amount)"+ChatColor.GRAY+" - Pays a player some coins.");
	}
	
	public static void helpClan(CommandSender sender, String[] split) {
		if(split.length < 2) {
			sender.sendMessage(ChatColor.GOLD+"-Clan Help-");
			sender.sendMessage(ChatColor.GOLD+"Page 1 of 2, use \"/help clan (page)\"");
			sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" Clans are permanent player groups. You can only be in one clan at a time.");
			
			sender.sendMessage(ChatColor.YELLOW+"/clan create (plot name)"+ChatColor.GRAY+" - Create a clan.");
			sender.sendMessage(ChatColor.GRAY+"-Costs 2000 gold coins.");
			sender.sendMessage(ChatColor.YELLOW+"/clan transfer (player name)"+ChatColor.GRAY+" - Transfers ownership of a         clan to another player.");
			sender.sendMessage(ChatColor.YELLOW+"/clan invite (player name)"+ChatColor.GRAY+" - Invites a player to your clan.");
			sender.sendMessage(ChatColor.YELLOW+"/clan uninvite (player name)"+ChatColor.GRAY+" - Uninvites a player.");
		}else {
			String page = split[1];
			if(page.equalsIgnoreCase("1")) {
				sender.sendMessage(ChatColor.GOLD+"-Clan Help-");
				sender.sendMessage(ChatColor.GOLD+"Page 1 of 2, use \"/help clan (page)\"");
				sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" Clans are permanent player groups. You can only be in one clan at a time.");
				
				sender.sendMessage(ChatColor.YELLOW+"/clan create (plot name)"+ChatColor.GRAY+" - Create a clan.");
				sender.sendMessage(ChatColor.GRAY+"-Costs 2000 gold coins.");
				sender.sendMessage(ChatColor.YELLOW+"/clan transfer (player name)"+ChatColor.GRAY+" - Transfers ownership of a         clan to another player.");
				sender.sendMessage(ChatColor.YELLOW+"/clan invite (player name)"+ChatColor.GRAY+" - Invites a player to your clan.");
				sender.sendMessage(ChatColor.YELLOW+"/clan uninvite (player name)"+ChatColor.GRAY+" - Uninvites a player.");
			} else if(page.equalsIgnoreCase("2")) {
				sender.sendMessage(ChatColor.GOLD+"-Clan Help-");
				sender.sendMessage(ChatColor.YELLOW+"Page 2 of 2, use \"/help land (page)\"");
				
				sender.sendMessage(ChatColor.YELLOW+"/clan promote (player name)"+ChatColor.GRAY+" - Promotes a member to leader.");
				sender.sendMessage(ChatColor.YELLOW+"/clan demote (player name)"+ChatColor.GRAY+" - Demotes a clan leader.");
				sender.sendMessage(ChatColor.YELLOW+"/clan kick (player name)"+ChatColor.GRAY+" - Kicks a player from your clan.");
				sender.sendMessage(ChatColor.YELLOW+"/clan leave"+ChatColor.GRAY+" - Leaves your clan.");
				sender.sendMessage(ChatColor.YELLOW+"/clan dsiband"+ChatColor.GRAY+" - Disbands your clan.");
				sender.sendMessage(ChatColor.YELLOW+"/clan info"+ChatColor.GRAY+" - Displays information about your clan.");
				sender.sendMessage(ChatColor.YELLOW+"/c (message)"+ChatColor.GRAY+" - Send a chat message to your clan.");
			}
		}
	}
	
	public static void helpParty(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"-Party Help-");
		sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" A party is a temporary player group. You can only be in one party at a time. While in a party, you can't damage party members with weapons.");
		sender.sendMessage(ChatColor.YELLOW+"/party invite (player name)"+ChatColor.GRAY+" - Invites a player to your party.");
		sender.sendMessage(ChatColor.GRAY+"-Creates a party if you are not already in one.");
		sender.sendMessage(ChatColor.YELLOW+"/party join (player name)"+ChatColor.GRAY+" - Join a player's party if you            have been invited.");
		sender.sendMessage(ChatColor.YELLOW+"/party leave"+ChatColor.GRAY+" - Leaves your party.");
		sender.sendMessage(ChatColor.YELLOW+"/party info"+ChatColor.GRAY+" - Displays information about your party.");
	}
	
	public static void helpKarma(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"-Karma Help-");
		sender.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" If you attack a non-criminal player, you become a criminal. If that player dies, you get a murder count. If you get 5 murder counts you become a murderer. Criminal status lasts 2 minutes. Every day (real time) you lose 1 murder count. If a criminal or murderer enters a plot with guards a non-criminal player can say \"guards\" near them and the guards will kill the criminal/murderer. Criminals and murderers also respawn in a different town located in the nether.");
		sender.sendMessage(ChatColor.YELLOW+"/whois (player name)"+ChatColor.GRAY+" - Displays a player's status.");
	}

	public static void helpMisc(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD+"-Misc Help-");
		sender.sendMessage(ChatColor.YELLOW+"/who"+ChatColor.GRAY+" - See a list of online players.");
		sender.sendMessage(ChatColor.YELLOW+"/kill"+ChatColor.GRAY+" - Kill yourself.");
	}
}
