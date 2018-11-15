package com.lostshard.RPG;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpHandler {
	public static void handle(Player player, String[] split) {
		if(split.length == 1) {
			player.sendMessage(ChatColor.GOLD+"-Help-");
			player.sendMessage(ChatColor.GOLD+"For more detailed information you can view the guide at");
			player.sendMessage(ChatColor.GOLD+"http://www.lostshard.com.");
			player.sendMessage(ChatColor.GOLD+"Use \"/help (topic)\" to get more information.");
			player.sendMessage(ChatColor.YELLOW+"Topics:"+ChatColor.GRAY+" chat, land, money, scrolls, clan, party, karma, misc");
		}
		else {
			String topic = split[1];
			
			if(topic.equalsIgnoreCase("chat"))
				helpChat(player, split);
			else if(topic.equalsIgnoreCase("land")||topic.equalsIgnoreCase("plot")||topic.equalsIgnoreCase("plots"))
				helpLandOwnership(player, split);
			else if(topic.equalsIgnoreCase("money"))
				helpMoney(player, split);
			else if(topic.equalsIgnoreCase("skills"))
				helpSkills(player, split);
			else if(topic.equalsIgnoreCase("scrolls"))
				helpScrolls(player, split);
			else if(topic.equalsIgnoreCase("clan"))
				helpClan(player, split);
			else if(topic.equalsIgnoreCase("party"))
				helpParty(player, split);
			else if(topic.equalsIgnoreCase("karma"))
				helpKarma(player, split);
			else if(topic.equalsIgnoreCase("misc"))
				helpMisc(player, split);
		}
	}
	
	public static void helpChat(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-Chat Help-");
		player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" Default chat is global.");
		player.sendMessage(ChatColor.YELLOW+"/l (message)"+ChatColor.GRAY+" - Local chat, talks to nearby players.");
		player.sendMessage(ChatColor.YELLOW+"/s (message)"+ChatColor.GRAY+" - Shouts a message");
		player.sendMessage(ChatColor.YELLOW+"/w (message)"+ChatColor.GRAY+" - Whispers to very close players");
		player.sendMessage(ChatColor.YELLOW+"/msg (player name) (message)"+ChatColor.GRAY+" - Private message");
	}
	
	public static void helpLandOwnership(Player player, String[] split) {
		if(split.length == 2) {
			player.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
			player.sendMessage(ChatColor.GOLD+"Page 1 of 3, use \"/help land (page)\"");
			player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" You may purchase and manage land by creating plots.");
			player.sendMessage(ChatColor.GRAY+"For more information see the guide at http://www.lostshard.com");
			
			player.sendMessage(ChatColor.YELLOW+"/plot create (plot name)"+ChatColor.GRAY+" - Create a plot.");
			player.sendMessage(ChatColor.GRAY+"-Costs 1000 + 1 diamond for the first plot.");
			player.sendMessage(ChatColor.GRAY+"-Starts with a size of 10 block radius.");
			player.sendMessage(ChatColor.YELLOW+"/plot survey"+ChatColor.GRAY+" Helps finding a place for a plot.");
			player.sendMessage(ChatColor.YELLOW+"/plot info"+ChatColor.GRAY+" Information about current plot.");
		}
		else {
			String page = split[2];
			if(page.equalsIgnoreCase("1")) {
				player.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
				player.sendMessage(ChatColor.GOLD+"Page 1 of 3, use \"/help land (page)\"");
				player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" You may purchase and manage land by creating plots.");
				player.sendMessage(ChatColor.GRAY+"For more information see the guide at http://www.lostshard.com");
				
				player.sendMessage(ChatColor.YELLOW+"/plot create (plot name)"+ChatColor.GRAY+" - Create a plot.");
				player.sendMessage(ChatColor.GRAY+"-Costs 1000 + 1 diamond for the first plot.");
				player.sendMessage(ChatColor.GRAY+"-Starts with a size of 10 block radius.");
				player.sendMessage(ChatColor.YELLOW+"/plot survey"+ChatColor.GRAY+" Helps finding a place for a plot.");
				player.sendMessage(ChatColor.YELLOW+"/plot info"+ChatColor.GRAY+" Information about current plot.");
			}
			else if(page.equalsIgnoreCase("2")) {
				player.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
				player.sendMessage(ChatColor.GOLD+"Page 2 of 3, use \"/help land (page)\"");
				
				player.sendMessage(ChatColor.YELLOW+"/plot friend/co-own/unfriend (player name)");
				player.sendMessage(ChatColor.GRAY+"- Add or remove a friend or co-owner");
				player.sendMessage(ChatColor.YELLOW+"/plot protect/unprotect"+ChatColor.GRAY+" - Protects or unprotects your plot.");
				//player.sendMessage(ChatColor.GRAY+"- Protects or unprotects a plot.");
				player.sendMessage(ChatColor.YELLOW+"/plot private/public"+ChatColor.GRAY+" - Locks or unlocks your plot.");
				player.sendMessage(ChatColor.YELLOW+"/plot deposit/withdraw"+ChatColor.GRAY+" - Deposits or withdraws plot funds.");
				player.sendMessage(ChatColor.YELLOW+"/plot expand"+ChatColor.GRAY+" - Expands your plot.");
				player.sendMessage(ChatColor.GRAY+"-Costs plot size times 100, so size 11 to 12 would cost 110.");
			}
			else if(page.equalsIgnoreCase("3")) {
				player.sendMessage(ChatColor.GOLD+"-Land Ownership Help-");
				player.sendMessage(ChatColor.GOLD+"Page 3 of 3, use \"/help land (page)\"");
				
				player.sendMessage(ChatColor.YELLOW+"/plot test"+ChatColor.GRAY+" - Lets you test the protection/locking.");
				player.sendMessage(ChatColor.YELLOW+"/plot endtest"+ChatColor.GRAY+" - Stops testing the protection/locking.");
				player.sendMessage(ChatColor.YELLOW+"/plot disband"+ChatColor.GRAY+" - Deletes your plot.");
				player.sendMessage(ChatColor.YELLOW+"/plot disband (name)"+ChatColor.GRAY+" - Deletes another plot you own.");
				player.sendMessage(ChatColor.YELLOW+"/plot list"+ChatColor.GRAY+" - Lists your current plots.");
			}
		}
	}
	
	public static void helpScrolls(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-Scrolls Help-");
		player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" When you kill monsters, there is a chance they will drop a scroll. Scrolls can be used to cast spells or you can add them to your spellbook and cast them with the magery skill.");
		player.sendMessage(ChatColor.YELLOW+"/scrolls"+ChatColor.GRAY+" - See the scrolls you have collected.");
		player.sendMessage(ChatColor.YELLOW+"/scrolls use (spell name)"+ChatColor.GRAY+" - Use a scroll.");
		player.sendMessage(ChatColor.YELLOW+"/scrolls spellbook (spell name)"+ChatColor.GRAY+" - Add a scroll to your              spellbook.");
	}

	public static void helpSkills(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-Skills Help-");
		player.sendMessage(ChatColor.GOLD+"For an in-depth explation of each skill, view the guide at www.lostshard.com.");
		player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" You can gain skills by performing actions like fighting monsters and casting spells. Each skill can go up to 100 and you can have up to 300 skill points total.");
		player.sendMessage(ChatColor.YELLOW+"/skills"+ChatColor.GRAY+" - See the skills you currently have.");
		player.sendMessage(ChatColor.YELLOW+"/skills reduce (skill name) (amount)"+ChatColor.GRAY+" - Reduces a skill.");
	}
	
	public static void helpMoney(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-Money Help-");
		player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" The currency is gold based, at any time you can trade gold ingots for gold coins.");
		player.sendMessage(ChatColor.YELLOW+"/stats"+ChatColor.GRAY+" - See various stats including your money.");
		player.sendMessage(ChatColor.YELLOW+"/tradegold (amount)"+ChatColor.GRAY+" - Trades gold ingots for gold coins.");
		player.sendMessage(ChatColor.GRAY+"-Exchange rate of 1 gold ingot for 100 gold coins.");
		player.sendMessage(ChatColor.YELLOW+"/pay (player name) (amount)"+ChatColor.GRAY+" - Pays a player some coins.");
		player.sendMessage(ChatColor.YELLOW+"/trade offer (player name) (price)"+ChatColor.GRAY+" - Offers the item in your hand for trade.");
	}
	
	public static void helpClan(Player player, String[] split) {
		if(split.length == 2) {
			player.sendMessage(ChatColor.GOLD+"-Clan Help-");
			player.sendMessage(ChatColor.GOLD+"Page 1 of 2, use \"/help clan (page)\"");
			player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" Clans are permanent player groups. You can only be in one clan at a time.");
			
			player.sendMessage(ChatColor.YELLOW+"/clan create (plot name)"+ChatColor.GRAY+" - Create a clan.");
			player.sendMessage(ChatColor.GRAY+"-Costs 2000 gold coins.");
			player.sendMessage(ChatColor.YELLOW+"/clan transfer (player name)"+ChatColor.GRAY+" - Transfers ownership of a         clan to another player.");
			player.sendMessage(ChatColor.YELLOW+"/clan invite (player name)"+ChatColor.GRAY+" - Invites a player to your clan.");
			player.sendMessage(ChatColor.YELLOW+"/clan uninvite (player name)"+ChatColor.GRAY+" - Uninvites a player.");
		}
		else {
			String page = split[2];
			if(page.equalsIgnoreCase("1")) {
				player.sendMessage(ChatColor.GOLD+"-Clan Help-");
				player.sendMessage(ChatColor.GOLD+"Page 1 of 2, use \"/help clan (page)\"");
				player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" Clans are permanent player groups. You can only be in one clan at a time.");
				
				player.sendMessage(ChatColor.YELLOW+"/clan create (plot name)"+ChatColor.GRAY+" - Create a clan.");
				player.sendMessage(ChatColor.GRAY+"-Costs 2000 gold coins.");
				player.sendMessage(ChatColor.YELLOW+"/clan transfer (player name)"+ChatColor.GRAY+" - Transfers ownership of a         clan to another player.");
				player.sendMessage(ChatColor.YELLOW+"/clan invite (player name)"+ChatColor.GRAY+" - Invites a player to your clan.");
				player.sendMessage(ChatColor.YELLOW+"/clan uninvite (player name)"+ChatColor.GRAY+" - Uninvites a player.");
			}
			else if(page.equalsIgnoreCase("2")) {
				player.sendMessage(ChatColor.GOLD+"-Clan Help-");
				player.sendMessage(ChatColor.YELLOW+"Page 2 of 2, use \"/help land (page)\"");
				
				player.sendMessage(ChatColor.YELLOW+"/clan promote (player name)"+ChatColor.GRAY+" - Promotes a member to leader.");
				player.sendMessage(ChatColor.YELLOW+"/clan demote (player name)"+ChatColor.GRAY+" - Demotes a clan leader.");
				player.sendMessage(ChatColor.YELLOW+"/clan kick (player name)"+ChatColor.GRAY+" - Kicks a player from your clan.");
				player.sendMessage(ChatColor.YELLOW+"/clan leave"+ChatColor.GRAY+" - Leaves your clan.");
				player.sendMessage(ChatColor.YELLOW+"/clan dsiband"+ChatColor.GRAY+" - Disbands your clan.");
				player.sendMessage(ChatColor.YELLOW+"/clan info"+ChatColor.GRAY+" - Displays information about your clan.");
				player.sendMessage(ChatColor.YELLOW+"/c (message)"+ChatColor.GRAY+" - Send a chat message to your clan.");
			}
		}
	}
	
	public static void helpParty(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-Party Help-");
		player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" A party is a temporary player group. You can only be in one party at a time. While in a party, you cannot damage party members with weapons.");
		player.sendMessage(ChatColor.YELLOW+"/party invite (player name)"+ChatColor.GRAY+" - Invites a player to your party.");
		player.sendMessage(ChatColor.GRAY+"-Creates a party if you are not already in one.");
		player.sendMessage(ChatColor.YELLOW+"/party join (player name)"+ChatColor.GRAY+" - Join a player's party if you            have been invited.");
		player.sendMessage(ChatColor.YELLOW+"/party leave"+ChatColor.GRAY+" - Leaves your party.");
		player.sendMessage(ChatColor.YELLOW+"/party info"+ChatColor.GRAY+" - Displays information about your party.");
	}
	
	public static void helpKarma(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-Karma Help-");
		player.sendMessage(ChatColor.YELLOW+"Info:"+ChatColor.GRAY+" If you attack a non-criminal player, you become a criminal. If that player dies, you get a murder count. If you get 5 murder counts you become a murderer. Criminal status lasts 2 minutes. Every day (real time) you lose 1 murder count. If a criminal or murderer enters a plot with guards a non-criminal player can say \"guards\" near them and the guards will kill the criminal/murderer. Criminals and murderers also respawn in a different town located in the nether.");
		player.sendMessage(ChatColor.YELLOW+"/whois (player name)"+ChatColor.GRAY+" - Displays a player's status.");
	}

	public static void helpMisc(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-Misc Help-");
		player.sendMessage(ChatColor.YELLOW+"/who"+ChatColor.GRAY+" - See a list of online players.");
		player.sendMessage(ChatColor.YELLOW+"/kill"+ChatColor.GRAY+" - Kill yourself.");
	}
	
	public static String implodeSplit(String[] split, int startIndex) {
		String message = "";
    	int splitMessageLength = split.length;
		for(int i=startIndex; i<splitMessageLength; i++) {
			message += split[i];
			if(i < splitMessageLength-1)
				message+= " ";
		}
		message = message.trim();
		return message;
	}
}
