package com.lostshard.RPG.Utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.ensifera.animosity.craftirc.EndPoint;
import com.ensifera.animosity.craftirc.RelayedMessage;
import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Parties.Party;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Skills.Rune;
import com.lostshard.RPG.Skills.Runebook;
import com.lostshard.RPG.Skills.Spellbook;
import com.lostshard.RPG.Spells.Spell;

public class Output {
	private static RPG _plugin;
	private static final int _whisperChatDist = 5;
	private static final int _localChatDist = 20;
	private static final int _shoutChatDist = 40;
	
	public static void init(RPG plugin) {
		_plugin = plugin;
	}
	
	public static int getLocalRange() {
		return _localChatDist;
	}
	
	public static void gainSkill(Player player, String skillName, int gainAmount, int totalSkill) {
		player.sendMessage(ChatColor.GOLD+"You have gained "+Utils.scaledIntToString(gainAmount)+" "+skillName+", it is now "+Utils.scaledIntToString(totalSkill)+".");
	}
	
	public static void sendEffectTextNearby(Player player, String text) {
		//Notify nearby players
		Player[] players = Utils.getPlugin().getServer().getOnlinePlayers();
		int numPlayers = players.length;
		for(int i=0; i<numPlayers; i++) {
			if(players[i].getWorld().equals(player.getWorld())) {
				if(Utils.isWithin(player.getLocation(), players[i].getLocation(), _localChatDist)) {
					players[i].sendMessage(ChatColor.GRAY+text);
				}
			}
		}
	}
	
	public static void sendEffectTextNearbyExcludePlayer(Player player, String text) {
		//Notify nearby players
		Player[] players = Utils.getPlugin().getServer().getOnlinePlayers();
		int numPlayers = players.length;
		for(int i=0; i<numPlayers; i++) {
			if(!players[i].equals(player)) {
				if(players[i].getWorld().equals(player.getWorld())) {
					if(Utils.isWithin(player.getLocation(), players[i].getLocation(), _localChatDist)) {
						players[i].sendMessage(ChatColor.GRAY+text);
					}
				}
			}
		}
	}
	
	public static void clanMessage(Clan clan, String message) {
		if(clan != null) {
			ArrayList<Player> _onlineClanMembers = clan.getOnlineMembers();
			for(Player onlineMember : _onlineClanMembers) {
				if(onlineMember != null) {
					onlineMember.sendMessage("["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"] "+message);
				}
			}
		}
		else System.out.println("I AM ERROR");
	}
	
	public static void clanSay(Clan clan, Player player, String message) {
		ArrayList<Player> _onlineClanMembers = clan.getOnlineMembers();
		for(Player onlineMember : _onlineClanMembers) {
			if(onlineMember != null) {
				onlineMember.sendMessage("["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"] "+player.getDisplayName()+ChatColor.WHITE+": "+message);
			}
		}
		sendToAdminIRC(player, "Clan", message);
	}
	
	public static void partySay(Party party, Player player, String message) {
		party.sendMessage("["+ChatColor.DARK_PURPLE+"Party"+ChatColor.WHITE+"] "+player.getDisplayName()+ChatColor.WHITE+": "+message);
		sendToAdminIRC(player, "Party", message);
	}
	
	public static void partyMessage(Party party, String message) {
		party.sendMessage("["+ChatColor.DARK_PURPLE+"Party"+ChatColor.WHITE+"] "+message);
	}
	
	public static void chatAdmin(Player player, String message) {
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.isOp())
				p.sendMessage("[ADMIN] "+player.getDisplayName()+ChatColor.WHITE+": "+message);
		}
	}
	
	public static void chatLocal(Player player, String message) {
		int numHeard = 0;
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.getWorld().equals(player.getWorld())) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), _localChatDist)) {
					p.sendMessage(player.getDisplayName()+ChatColor.WHITE+": "+message);
					if(p.isOp()) {
						PseudoPlayer pseudoP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						if(!pseudoP._vanished || player.isOp())
							numHeard++;
					}
					else {
						if(!p.isSneaking()) {
							numHeard++;
						}
					}
				}
			}
		}
		if(numHeard <= 1)
			player.sendMessage(ChatColor.GRAY+"No one hears you...");
		
		sendToAdminIRC(player, "Local", message);
	}
	
	public static void chatLocal(Player player, String message, boolean doNobodyHears) {
		int numHeard = 0;
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.getWorld().equals(player.getWorld())) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), _localChatDist)) {
					p.sendMessage(player.getDisplayName()+ChatColor.WHITE+": "+message);
					if(p.isOp()) {
						PseudoPlayer pseudoP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						if(!pseudoP._vanished || player.isOp())
							numHeard++;
					}
					else {
						if(!p.isSneaking()) {
							numHeard++;
						}
					}
				}
			}
		}
		if(doNobodyHears && numHeard <= 1)
			player.sendMessage(ChatColor.GRAY+"No one hears you...");
		
		//sendToAdminIRC(player, "Local", message);
	}
	
	public static void npcChatLocal(String npcName, Location location, String message) {
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.getWorld().equals(location.getWorld())) {
				if(Utils.isWithin(location, p.getLocation(), _localChatDist)) {
					p.sendMessage(npcName+": "+message);
				}
			}
		}
	}
	
	public static void chatWhisper(Player player, String message) {
		int numHeard = 0;
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.getWorld().equals(player.getWorld())) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), _whisperChatDist)) {
					p.sendMessage(player.getDisplayName()+ChatColor.WHITE+" whispers: "+message);
					if(p.isOp()) {
						PseudoPlayer pseudoP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						if(!pseudoP._vanished || player.isOp())
							numHeard++;
					}
					else {
						if(!p.isSneaking()) {
							numHeard++;
						}
					}
				}
			}
		}
		if(numHeard <= 1)
			player.sendMessage(ChatColor.GRAY+"No one hears you...");
		
		sendToAdminIRC(player, "Whisper", message);
	}
	
	public static void chatWhisper(Player player, String message, boolean doNobodyHears) {
		int numHeard = 0;
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.getWorld().equals(player.getWorld())) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), _whisperChatDist)) {
					p.sendMessage(player.getDisplayName()+ChatColor.WHITE+" whispers: "+message);
					if(p.isOp()) {
						PseudoPlayer pseudoP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						if(!pseudoP._vanished || player.isOp())
							numHeard++;
					}
					else {
						if(!p.isSneaking()) {
							numHeard++;
						}
					}
				}
			}
		}
		if(doNobodyHears && numHeard <= 1)
			player.sendMessage(ChatColor.GRAY+"No one hears you...");
		
		//sendToAdminIRC(player, "Whisper", message);
	}
	
	public static void chatShout(Player player, String message) {
		int numHeard = 0;
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.getWorld().equals(player.getWorld())) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), _shoutChatDist)) {
					p.sendMessage(player.getDisplayName()+ChatColor.WHITE+" shouts: "+message);
					if(p.isOp()) {
						PseudoPlayer pseudoP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
						if(!pseudoP._vanished || player.isOp())
							numHeard++;
					}
					else {
						if(!p.isSneaking()) {
							numHeard++;
						}
					}
				}
			}
		}
		if(numHeard <= 1)
			player.sendMessage(ChatColor.GRAY+"No one hears you...");
		
		sendToAdminIRC(player, "Shout", message);
	}
	
	public static void chatGlobal(Player player, String message) {
		Player[] onlinePlayers = _plugin.getServer().getOnlinePlayers();
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		for(Player p : onlinePlayers) {
			PseudoPlayer ps = PseudoPlayerHandler.getPseudoPlayer(p.getName());
			if(ps == null) {
				System.out.println("NULLPS");
				return;
			}
			if(p==player || ps.isGlobalEnabled()) {
				if(!ps.isIgnoring(player.getName())) {
					String modMessage;
					
					if(pseudoPlayer.isPremium())
						modMessage = ""+ChatColor.GOLD+"[";
					else
						modMessage = "[";
					
					modMessage += ChatColor.YELLOW+"Global";
					
					if(pseudoPlayer.isPremium())
						modMessage += ChatColor.GOLD+"]* "+ChatColor.WHITE;
					else
						modMessage += ChatColor.WHITE+"] ";
					
					/*if(pseudoPlayer.getName().equalsIgnoreCase("Ayunema")) {
						modMessage += ChatColor.GOLD+"Dungeon Master Ayunema"+ChatColor.WHITE+": "+message;
					}
					else if(pseudoPlayer.getName().equalsIgnoreCase("Fiddle")) {
						modMessage += ChatColor.GOLD+"Badmin "+player.getName()+ChatColor.WHITE+": "+message;
					}
					else if(pseudoPlayer.getName().equalsIgnoreCase("luciddream")) {
						modMessage += ChatColor.GOLD+"Admin "+player.getName()+ChatColor.WHITE+": "+message;
					}
					else if(pseudoPlayer.getName().equalsIgnoreCase("Virus610")) {
						modMessage += ChatColor.GOLD+"Madmin "+player.getName()+ChatColor.WHITE+": "+message;
					}*/
					if(pseudoPlayer._activeTitle.equalsIgnoreCase("santa-bot")) {
						modMessage += ChatColor.RED+"Santa"+ChatColor.GRAY+"-"+ChatColor.GREEN+"Bot"+ChatColor.WHITE+": ";
						if(Math.random()<.5)
							modMessage += ChatColor.GREEN+message;
						else
							modMessage += ChatColor.RED+message;
					}
					else if(pseudoPlayer._activeTitle.equalsIgnoreCase("") || pseudoPlayer._activeTitle.equalsIgnoreCase(" ")) {
						modMessage += player.getDisplayName()+ChatColor.WHITE+": "+message;
					}
					else {
						if(pseudoPlayer._activeTitle.equalsIgnoreCase("admin") || pseudoPlayer._activeTitle.equalsIgnoreCase("Sadmin") || pseudoPlayer._activeTitle.equalsIgnoreCase("u mAdmin")|| pseudoPlayer._activeTitle.equalsIgnoreCase("badmin") || pseudoPlayer._activeTitle.equalsIgnoreCase("madmin")|| pseudoPlayer._activeTitle.equalsIgnoreCase("Radmin") || pseudoPlayer._activeTitle.equalsIgnoreCase("dungeon master") || pseudoPlayer._activeTitle.equalsIgnoreCase("Fullmetal Admin") || pseudoPlayer._activeTitle.equalsIgnoreCase("I'm") || pseudoPlayer._activeTitle.equalsIgnoreCase("Danemin")) {
							modMessage += ChatColor.GOLD+pseudoPlayer._activeTitle+" "+player.getName()+ChatColor.WHITE+": "+message;
						}
						else {
							modMessage += pseudoPlayer._activeTitle + " " + player.getDisplayName()+ChatColor.WHITE+": "+message;
						}
					}
					p.sendMessage(modMessage);
				}
			}
		}
		if(!pseudoPlayer.isGlobalEnabled()) {
			player.sendMessage(ChatColor.GRAY+"You have global disabled and cannot hear anyone else.");
		}
		sendToIRC(player, "Global", message);
		sendToAdminIRC(player, "Global", message);
	}
	
	public static void sendToIRC(Player player, String channel, String message) {
		final RelayedMessage rm = RPG.craftircHandle.newMsg(_plugin, RPG.craftircHandle.getEndPoint("synirclostshard"), "generic");
		
        String compiledMessage;
		
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer.isMurderer())
			compiledMessage = "["+channel+"] 4"+player.getName()+": "+message;
		else if(pseudoPlayer.isCriminal())
			compiledMessage = "["+channel+"] 14"+player.getName()+": "+message;
		else
			compiledMessage = "["+channel+"] 12"+player.getName()+": "+message;
		
		rm.setField("message", compiledMessage);
        rm.post();
	}
	
	public static void sendToAdminIRC(Player player, String channel, String message) {
		final RelayedMessage rm = RPG.craftircHandle.newMsg(_plugin, RPG.craftircHandle.getEndPoint("synirclostshard-admin"), "generic");
		
        String compiledMessage;
        
		if(player == null) {
			compiledMessage = "["+channel+"] "+message;
			rm.setField("message", compiledMessage);
		    rm.post();
			return;
		}
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer.isMurderer())
			compiledMessage = "["+channel+"] 4"+player.getName()+": "+message;
		else if(pseudoPlayer.isCriminal())
			compiledMessage = "["+channel+"] 14"+player.getName()+": "+message;
		else
			compiledMessage = "["+channel+"] 12"+player.getName()+": "+message;
			
		rm.setField("message", compiledMessage);
	    rm.post();
	}
	
	public static void simpleError(Player player, String message) {
		player.sendMessage(ChatColor.DARK_RED+message);
	}
	
	public static void positiveMessage(Player player, String message) {
		player.sendMessage(ChatColor.GOLD+message);
	}
	
	public static void notInPlot(Player player) {
		player.sendMessage(ChatColor.DARK_RED+"You are not currently in a plot.");
	}
	
	public static void beginCastingSpell(Player player, Spell spell) {
		player.sendMessage(ChatColor.BLUE+"You begin casting "+spell.getName()+"...");
	}
	
	public static void outputSkills(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Skills-");
		player.sendMessage(ChatColor.YELLOW+"You currently have "+Utils.scaledIntToString(pseudoPlayer.getTotalSkillVal())+"/"+Utils.scaledIntToString(pseudoPlayer.getMaxSkillValTotal()) + " skill points.");
		if(pseudoPlayer.isSkillLocked("archery"))
			player.sendMessage(ChatColor.YELLOW+"Archery(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("archery")));
		else
			player.sendMessage(ChatColor.YELLOW+"Archery: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("archery")));
		
		if(pseudoPlayer.isSkillLocked("blacksmithy"))
			player.sendMessage(ChatColor.YELLOW+"Blacksmithy(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("blacksmithy")));
		else
			player.sendMessage(ChatColor.YELLOW+"Blacksmithy: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("blacksmithy")));
		
		if(pseudoPlayer.isSkillLocked("brawling"))
			player.sendMessage(ChatColor.YELLOW+"Brawling(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("brawling")));
		else
			player.sendMessage(ChatColor.YELLOW+"Brawling: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("brawling")));
		
		if(pseudoPlayer.isSkillLocked("magery"))
			player.sendMessage(ChatColor.YELLOW+"Magery(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("magery")));
		else
			player.sendMessage(ChatColor.YELLOW+"Magery: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("magery")));
		
		if(pseudoPlayer.isSkillLocked("blades"))
			player.sendMessage(ChatColor.YELLOW+"Blades(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("blades")));
		else
			player.sendMessage(ChatColor.YELLOW+"Blades: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("blades")));
		
		if(pseudoPlayer.isSkillLocked("survivalism"))
			player.sendMessage(ChatColor.YELLOW+"Survivalism(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("survivalism")));
		else
			player.sendMessage(ChatColor.YELLOW+"Survivalism: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("survivalism")));
		
		if(pseudoPlayer.isSkillLocked("mining"))
			player.sendMessage(ChatColor.YELLOW+"Mining(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("mining")));
		else
			player.sendMessage(ChatColor.YELLOW+"Mining: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("mining")));
		
		if(pseudoPlayer.isSkillLocked("lumberjacking"))
			player.sendMessage(ChatColor.YELLOW+"Lumberjacking(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("lumberjacking")));
		else
			player.sendMessage(ChatColor.YELLOW+"Lumberjacking: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("lumberjacking")));
		
		if(pseudoPlayer.isSkillLocked("taming"))
			player.sendMessage(ChatColor.YELLOW+"Taming(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("taming")));
		else
			player.sendMessage(ChatColor.YELLOW+"Taming: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("taming")));
		
		if(pseudoPlayer.isSkillLocked("fishing"))
			player.sendMessage(ChatColor.YELLOW+"Fishing(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("fishing")));
		else
			player.sendMessage(ChatColor.YELLOW+"Fishing: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getSkill("fishing")));
	}
	
	public static void outputStats(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Statistics-");
		player.sendMessage(ChatColor.YELLOW+"Gold Coins: " +ChatColor.WHITE+ pseudoPlayer.getMoney());
		player.sendMessage(ChatColor.YELLOW+"Mana: "+ChatColor.WHITE+ pseudoPlayer.getMana() +"/"+ pseudoPlayer.getMaxMana());
		player.sendMessage(ChatColor.YELLOW+"Stamina: "+ChatColor.WHITE+ pseudoPlayer.getStamina() +"/"+ pseudoPlayer.getMaxStamina());
		player.sendMessage(ChatColor.YELLOW+"Build: "+ChatColor.WHITE+ pseudoPlayer.getBuildNumber());
		player.sendMessage(ChatColor.YELLOW+"Murder Counts: "+ChatColor.WHITE+ pseudoPlayer.getMurderCounts());
	}
	
	public static void outputStats(Player player, Player targetPlayer) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
		player.sendMessage(ChatColor.GOLD+"-"+targetPlayer.getName()+"'s Statistics-");
		player.sendMessage(ChatColor.YELLOW+"Gold Coins: " +ChatColor.WHITE+ pseudoPlayer.getMoney());
		player.sendMessage(ChatColor.YELLOW+"Mana: "+ChatColor.WHITE+ pseudoPlayer.getMana() +"/"+ pseudoPlayer.getMaxMana());
		player.sendMessage(ChatColor.YELLOW+"Stamina: "+ChatColor.WHITE+ pseudoPlayer.getStamina() +"/"+ pseudoPlayer.getMaxStamina());
		player.sendMessage(ChatColor.YELLOW+"Build: "+ChatColor.WHITE+ pseudoPlayer.getBuildNumber());
		player.sendMessage(ChatColor.YELLOW+"Murder Counts: "+ChatColor.WHITE+ pseudoPlayer.getMurderCounts());
	}
	
	public static void outputWho(Player player, PseudoPlayer whoPseudoPlayer) {
		player.sendMessage(ChatColor.GOLD+"-Player Information-");
		String coloredName;
		if(whoPseudoPlayer.isMurderer())
			coloredName = ChatColor.DARK_RED+whoPseudoPlayer.getName();
		else if(whoPseudoPlayer.isCriminal())
			coloredName = ChatColor.GRAY+whoPseudoPlayer.getName();
		else
			coloredName = ChatColor.BLUE+whoPseudoPlayer.getName();
		player.sendMessage(ChatColor.YELLOW+"Name: "+coloredName);
		if(whoPseudoPlayer.isMurderer())
			player.sendMessage(ChatColor.RED+"This player is a murderer.");
		else if(whoPseudoPlayer.isCriminal())
			player.sendMessage(ChatColor.RED+"This player is a criminal.");
		player.sendMessage(ChatColor.YELLOW+"Murder Counts:" +ChatColor.WHITE+whoPseudoPlayer.getMurderCounts());
		Clan clan = whoPseudoPlayer.getClan();
		if(clan != null)
			player.sendMessage(ChatColor.YELLOW+"Clan: "+ChatColor.WHITE+clan.getName());
		else
			player.sendMessage(ChatColor.YELLOW+"Clan: "+ChatColor.WHITE+"none");
	}
	
	public static void outputClanInfo(Player player, Clan clan) {
		//PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		player.sendMessage(ChatColor.GOLD+"-"+clan.getName()+"'s Info-");
		player.sendMessage(ChatColor.YELLOW+"Clan Owner: " +ChatColor.WHITE+ clan.getOwnerName());
		
		String leadersString = "";
		ArrayList<String> leaders = clan.getLeaders();
		int numLeaders = leaders.size();
		for(int i=0; i<numLeaders; i++) {
			leadersString+=leaders.get(i);
			if(i < numLeaders-1)
				leadersString+=", ";
		}
		if(numLeaders <= 0)
			leadersString = " ";
		player.sendMessage(ChatColor.YELLOW+"Clan Leaders: " +ChatColor.WHITE+leadersString);
		
		String membersString = "";
		ArrayList<String> members = clan.getMembers();
		int numMembers = members.size();
		for(int i=0; i<numMembers; i++) {
			membersString+=members.get(i);
			if(i < numMembers-1)
				membersString+=", ";
		}
		if(numMembers <= 0)
			membersString = " ";
		player.sendMessage(ChatColor.YELLOW+"Clan Members: " +ChatColor.WHITE+membersString);
	}
	
	public static void displayPlotInfo(Player player, Plot plot) {
		// Show title to everyone
		player.sendMessage(ChatColor.GOLD+"-"+plot.getName()+"'s Plot Info-");
		String infoText = "";
		if(plot.isProtected())
			infoText+=ChatColor.YELLOW+"Protected: "+ChatColor.WHITE+"Yes";
		else
			infoText+=ChatColor.YELLOW+"Protected: "+ChatColor.WHITE+"No";
		infoText+=ChatColor.YELLOW+", ";
		if(plot.isLocked())
			infoText+=ChatColor.YELLOW+"Status: "+ChatColor.WHITE+"Private";
		else
			infoText+=ChatColor.YELLOW+"Status: "+ChatColor.WHITE+"Public";
		infoText+=ChatColor.YELLOW+", ";
		if(plot.isNeutral())
			infoText+=ChatColor.YELLOW+"Alignment: "+ChatColor.WHITE+"Neutral";
		else if(Database.getPlayerMurderCounts(plot.getOwner()) < 5)
			infoText+=ChatColor.YELLOW+"Alignment: "+ChatColor.BLUE+"Lawful";
		else
			infoText+=ChatColor.YELLOW+"Alignment: "+ChatColor.RED+"Criminal";
		
		player.sendMessage(infoText);
		
		infoText = "";
		if(plot.isExplosionAllowed())
			infoText+=ChatColor.YELLOW+"Allow Explosions: "+ChatColor.WHITE+"Yes";
		else
			infoText+=ChatColor.YELLOW+"Allow Explosions: "+ChatColor.WHITE+"No";
		//infoText+=ChatColor.YELLOW+", ";
		
		player.sendMessage(infoText);
		
		// Display your position in the plot
		if(player.isOp() || plot.isOwner(player.getName()))
			player.sendMessage(ChatColor.YELLOW+"You are the owner of this plot.");
		else if(plot.isCoOwner(player.getName()))
			player.sendMessage(ChatColor.YELLOW+"You are a co-owner of this plot.");
		else if(plot.isFriend(player.getName()))
			player.sendMessage(ChatColor.YELLOW+"You are a friend of this plot.");
		else
			player.sendMessage(ChatColor.YELLOW+"You are not a friend of this plot.");
		
		if(plot.isControlPoint()) {
			Clan clan = plot.getOwningClan();
			if(clan != null)
				player.sendMessage(ChatColor.YELLOW+"Owning Clan: "+ChatColor.WHITE+clan.getName());
			else
				player.sendMessage(ChatColor.YELLOW+"Owning Clan: "+ChatColor.RED+"NONE");
		}
		
		// Show this stuff to everyone
		if(plot.getSaleCost() > 0)
			player.sendMessage(ChatColor.YELLOW+"Owner: " +ChatColor.WHITE+ plot.getOwner()+", "+ChatColor.YELLOW+"Sale Price: "+ChatColor.WHITE+plot.getSaleCost());
		else
			player.sendMessage(ChatColor.YELLOW+"Owner: " +ChatColor.WHITE+ plot.getOwner()+", "+ChatColor.YELLOW+"Sale Price: "+ChatColor.RED+"Not for sale");
		// Only show the owner/co-owner the amount of money in the region bank
		if(player.isOp() || plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
			int moneyPerDay = 5*plot.getRadius();
			player.sendMessage(ChatColor.YELLOW+"Size: "+ChatColor.WHITE+plot.getRadius()+ChatColor.YELLOW+", Funds: " +ChatColor.WHITE+ plot.getMoney()+ChatColor.YELLOW+", Tax: " +ChatColor.WHITE+moneyPerDay + ChatColor.YELLOW+", Plot Value: " +ChatColor.WHITE+ plot.getValue());
			//player.sendMessage(ChatColor.YELLOW+"Tax: " +ChatColor.WHITE+moneyPerDay);
			player.sendMessage(ChatColor.GRAY+"("+(plot.getMoney() / moneyPerDay)+" days worth of funds remaining.)");
			//player.sendMessage(ChatColor.YELLOW+"Plot Value: " +ChatColor.WHITE+ plot.getValue());
			int distanceFromCenter = (int)Math.round(Utils.distance(player.getLocation(), plot.getLocation()));
			player.sendMessage(ChatColor.YELLOW+"Center: " +ChatColor.WHITE+"("+plot.getLocation().getBlockX()+","+plot.getLocation().getBlockY()+","+plot.getLocation().getBlockZ()+"), "+ChatColor.YELLOW+"Distance From Center: "+ChatColor.WHITE+distanceFromCenter);
		}
		else {
			player.sendMessage(ChatColor.YELLOW+"Size: " +ChatColor.WHITE+ plot.getRadius());
		}
		// Show member lists to everyone who is at least a friend
		if(player.isOp() || plot.isOwner(player.getName()) || plot.isCoOwner(player.getName()) || plot.isFriend(player.getName())) {
			String coOwnerString = ChatColor.YELLOW+"Co-Owners: ";
			ArrayList<String> coOwners = plot.getCoOwners();
			//System.out.println("Num coowners: " + coOwners.size());
			if(coOwners.size() > 0)
				coOwnerString+=ChatColor.WHITE;
			for(int i=0; i<coOwners.size(); i++) {
				coOwnerString+=coOwners.get(i);
				if(i < coOwners.size()-1)
					coOwnerString+=", ";
			}
			player.sendMessage(coOwnerString);
			
			String friendString = ChatColor.YELLOW+"Friends: ";
			ArrayList<String> friends = plot.getFriends();
			//System.out.println("Num friends: " + friends.size());
			if(friends.size() > 0)
				friendString+=ChatColor.WHITE;
			for(int i=0; i<friends.size(); i++) {
				friendString+=friends.get(i);
				if(i < friends.size()-1)
					friendString+=", ";
			}
			player.sendMessage(friendString);
		}
		
		
	}

	public static void outputSpellbook(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Spellbook spellbook = pseudoPlayer.getSpellbook();
		if(split.length == 3) {
			String secondaryCommand = split[1];
			if(secondaryCommand.equalsIgnoreCase("page")) {
				int pageNumber;
				try {
					pageNumber = Integer.parseInt(split[2]);
				}
				catch(Exception e) {
					pageNumber = -1;
				}
				if((pageNumber >= 1) && (pageNumber <= 9)) {
					player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Spellbook [Page "+pageNumber+"]-");
					int minMagery = ((pageNumber-1)*12);
					if(pageNumber == 9)
						minMagery = 100;
					player.sendMessage(ChatColor.YELLOW+"-Minimum Magery: "+minMagery);
					player.sendMessage(ChatColor.YELLOW+"Spell Name - (Reagent Cost)");
					ArrayList<Spell> spellsOnPage = spellbook.getSpellsOnPage(pageNumber);
					if(spellsOnPage.size() > 0) {
						for(Spell spell : spellsOnPage) {
							int[] reagents = spell.getReagentCost();
							String reagentString = "(";
							int numReagents = reagents.length;
							for(int i=0; i<numReagents; i++) {
								reagentString += Material.getMaterial(reagents[i]).name();
								if(i < numReagents-1)
									reagentString+=",";
							}
							reagentString += ")";
							player.sendMessage(ChatColor.YELLOW+spell.getName()+ChatColor.WHITE+" - "+reagentString);
						}
					}
					else player.sendMessage(ChatColor.RED+"You don't have any spells on this page.");
				}
				else simpleError(player, "That page doesn't exist, use 1-9");
			}
		}
		else {
			player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Spellbook-");
			player.sendMessage(ChatColor.YELLOW+"Your spellbook has 9 pages in it. Each page lists the");
			player.sendMessage(ChatColor.YELLOW+"spells and associated reagent costs for one circle");
			player.sendMessage(ChatColor.YELLOW+"of magic. Each page of spells has a minimum magery.");
			player.sendMessage(ChatColor.YELLOW+"the easiest spells are on page 1, and the hardest");
			player.sendMessage(ChatColor.YELLOW+"spells are on page 9.");
			player.sendMessage(ChatColor.YELLOW+"Use /spellbook page (page number)");
			player.sendMessage(ChatColor.YELLOW+"Ex: /spellbook page 1");
		}
	}
	
	public static void outputRunebook(Player player, String[] split) {
		player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Runebook-");
		
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Runebook runebook = pseudoPlayer.getRunebook();
		ArrayList<Rune> runes = runebook.getRunes();
		
		int totalRunes = runes.size();
		if(!player.isOp() && !pseudoPlayer.isLargerBank())
			if(totalRunes > 8) 
				totalRunes = 8;
		int numPages = ((totalRunes-1)/8)+1;
		
		if(player.isOp())
			player.sendMessage(ChatColor.YELLOW+"Pg 1 of "+numPages+" ("+totalRunes+" of lots of runes used) [ Use /runebook page (#) ]");
		else if(pseudoPlayer.isLargerBank())
			player.sendMessage(ChatColor.YELLOW+"Pg 1 of "+numPages+" ("+totalRunes+" of 16 runes used) [ Use /runebook page (#) ]");
		else
			player.sendMessage(ChatColor.YELLOW+"Pg 1 of "+numPages+" ("+totalRunes+" of 8 runes used)");
		
		if(split.length >= 2 && split[1].equalsIgnoreCase("page") && totalRunes > 0) {
			int page = 0;
			try {
				page = Integer.parseInt(split[2]);
			}
			catch(Exception e) {
				page = -1;
			}
			
			if(page  < 0 || page > numPages) {
				Output.simpleError(player, "Invalid page.");
				return;
			}
			// valid page
			
			int startingRune = (page-1)*8;
			int finalRune = (page-1)*8+7;
			if(finalRune >= totalRunes) {
				finalRune = totalRunes-1;
			}
			
			//output
			for(int i=startingRune; i<= finalRune; i++) {
				Location loc = runes.get(i).getLocation();
				player.sendMessage("- "+runes.get(i).getLabel() + " - ("+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+")");
			}
		}
		else {
			if(runes.size() > 0) {
				int numToDisplay = totalRunes;
				if(numToDisplay >= 8)
					numToDisplay = 8;
				for(int i=0; i<numToDisplay; i++) {
					Location loc = runes.get(i).getLocation();
					player.sendMessage("- "+runes.get(i).getLabel() + " - ("+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+")");
				}
			}
			else player.sendMessage(ChatColor.RED+"You do not have any runes.");
		}
	}
	
	public static void outputScrolls(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		ArrayList<Spell> scrolls = pseudoPlayer.getScrolls();
		player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Scrolls-");
		player.sendMessage(ChatColor.YELLOW+"(\"+\" means it is already in your spellbook)");
		if(scrolls.size() > 0) {
			String scrollString = "";
			@SuppressWarnings("unchecked")
			ArrayList<Spell> scrollClone = (ArrayList<Spell>) scrolls.clone();
			while(scrollClone.size() > 0) {
				int numScrollsRemaining = scrollClone.size();
				Spell curSpell = scrollClone.get(0);
				int numScrolls = 0;
				// go through all the scrolls, remove them from the scroll list
				for(int i=numScrollsRemaining-1; i>= 0; i--) {
					if(scrollClone.get(i).getName().equals(curSpell.getName())) {
						scrollClone.remove(i);
						numScrolls++;
					}
				}
				if(pseudoPlayer.getSpellbook().hasSpellByName(curSpell.getName()))
					scrollString+="+";
				scrollString += curSpell.getName()+" ("+numScrolls+"), ";
			}
			player.sendMessage(scrollString);
		}
		else player.sendMessage(ChatColor.RED+"You do not currently have any scrolls.");
	}
	
	public static void outputSubscriptionData(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Subscription Info-");
		if(pseudoPlayer.isPremium())
			player.sendMessage(ChatColor.YELLOW+"Subscription Status:"+ChatColor.WHITE+" Active");
		else
			player.sendMessage(ChatColor.YELLOW+"Subscription Status:"+ChatColor.WHITE+" Inactive");
		
		if(pseudoPlayer.getPremiumDays() == -1)
			player.sendMessage(ChatColor.YELLOW+"Subscription Days Remaining: " +ChatColor.WHITE+"Auto Bill");
		else
			player.sendMessage(ChatColor.YELLOW+"Subscription Days Remaining: "+ChatColor.WHITE+ pseudoPlayer.getPremiumDays());
			
	}
}
