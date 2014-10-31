package com.lostshard.lostshard.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Utils;

public class ChatHandler {

	private static int whisperChatRange = 5;
	private static int localChatRange = 10;
	private static int shoutChatRange = 50;
	
	public static int getWhisperChatRange() {
		return whisperChatRange;
	}

	public static void setWhisperChatRange(int whisperChatRange) {
		ChatHandler.whisperChatRange = whisperChatRange;
	}

	public static int getLocalChatRange() {
		return localChatRange;
	}

	public static void setLocalChatRange(int localChatRange) {
		ChatHandler.localChatRange = localChatRange;
	}

	public static int getShoutChatRange() {
		return shoutChatRange;
	}

	public static void setShoutChatRange(int shoutChatRange) {
		ChatHandler.shoutChatRange = shoutChatRange;
	}
	
	public static void whisperChat(Player player, String message) {
		for(Player p : Utils.getPlayersNear(player, getWhisperChatRange()))
			p.sendMessage(Utils.getColoredName(player)+ChatColor.WHITE+" whisper: "+message);
	}
	
	public static void localChat(Player player, String message) {
		for(Player p : Utils.getPlayersNear(player, getLocalChatRange()))
			p.sendMessage(Utils.getColoredName(player)+ChatColor.WHITE+": "+message);
	}
	
	public static void shoutChat(Player player, String message) {
		for(Player p : Utils.getPlayersNear(player, getShoutChatRange()))
			p.sendMessage(Utils.getColoredName(player)+ChatColor.WHITE+" shouts: "+message);
	}
	
	public static void globalChat(Player player, String message) {
		String prefix;
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if(pPlayer.isSubscriber())
			prefix = ChatColor.GOLD+"["+ChatColor.YELLOW+"Global"+ChatColor.GOLD+"]* ";
		else
			prefix = ChatColor.WHITE+"["+ChatColor.YELLOW+"Global"+ChatColor.WHITE+"] ";
		//TODO Check for title here
		for(Player p : Bukkit.getOnlinePlayers()) {
			pPlayer = PseudoPlayerHandler.getPlayer(p);
			if(pPlayer.isGlobalChat())
				p.sendMessage(prefix+Utils.getColoredName(player)+ChatColor.WHITE+": ");
		}
	}
	
	public static void clanChat(Player player, String message) {
//		ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"]"+
//	Utils.getColoredName(player)+ChatColor.WHITE+": ";

	}
	
	public static void partyChat(Player player, String message) {
//		ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"]"+
//	Utils.getColoredName(player)+ChatColor.WHITE+": ";

	}
	
}
