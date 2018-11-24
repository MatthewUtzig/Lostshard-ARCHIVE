package com.lostshard.RPG;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Parties.Party;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class ChatHandler {
	public static int CHAT_CHANNEL_GLOBAL = 1001;
	public static int CHAT_CHANNEL_LOCAL = 1002;
	public static int CHAT_CHANNEL_SHOUT = 1003;
	public static int CHAT_CHANNEL_WHISPER = 1004;
	public static int CHAT_CHANNEL_CLAN = 1005;
	public static int CHAT_CHANNEL_PARTY = 1006;
	public static int CHAT_CHANNEL_ADMIN = 1007;
	public static int CHAT_CHANNEL_MSG = 1008;
	
	public static boolean handleChat(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		String cmd = split[0];
		if(cmd.equalsIgnoreCase("/l") || cmd.equalsIgnoreCase("/local")) {
			if(split.length == 1) {
				Output.positiveMessage(player, "You have set your active channel to local.");
				pseudoPlayer._activeChannel = CHAT_CHANNEL_LOCAL;
				return true;
			}
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_LOCAL;
			chatLocal(player, split);
			return true;
		}
		if(cmd.equalsIgnoreCase("/g") || cmd.equalsIgnoreCase("/global")) {
			if(split.length == 1) {
				Output.positiveMessage(player, "You have set your active channel to global.");
				pseudoPlayer._activeChannel = CHAT_CHANNEL_GLOBAL;
				return true;
			}
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_GLOBAL;
			chatGlobal(player, split);
			return true;
		}
		if((cmd.equalsIgnoreCase("/a") || cmd.equalsIgnoreCase("/admin")) && player.isOp()) {
			if(split.length == 1) {
				Output.positiveMessage(player, "You have set your active channel to admin.");
				pseudoPlayer._activeChannel = CHAT_CHANNEL_ADMIN;
				return true;
			}
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_ADMIN;
			chatAdmin(player, split);
			return true;
		}
		else if(cmd.equalsIgnoreCase("/s")) {
			if(split.length == 1) {
				Output.positiveMessage(player, "You have set your active channel to shout.");
				pseudoPlayer._activeChannel = CHAT_CHANNEL_SHOUT;
				return true;
			}
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_SHOUT;
			chatShout(player, split);
			return true;
		}
		else if(cmd.equalsIgnoreCase("/w")) {
			if(split.length == 1) {
				Output.positiveMessage(player, "You have set your active channel to whisper.");
				pseudoPlayer._activeChannel = CHAT_CHANNEL_WHISPER;
				return true;
			}
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_WHISPER;
			chatWhisper(player, split);
			return true;
		}
		else if(cmd.equalsIgnoreCase("/p")) {
			if(split.length == 1) {
				Output.positiveMessage(player, "You have set your active channel to party.");
				pseudoPlayer._activeChannel = CHAT_CHANNEL_PARTY;
				return true;
			}
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_PARTY;
			chatParty(player, split);
			return true;
		}
		else if(cmd.equalsIgnoreCase("/c")) {
			if(split.length == 1) {
				Output.positiveMessage(player, "You have set your active channel to clan.");
				pseudoPlayer._activeChannel = CHAT_CHANNEL_CLAN;
				return true;
			}
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_CLAN;
			chatClan(player, split);
			return true;
		}
		else if(cmd.equalsIgnoreCase("/m") || cmd.equalsIgnoreCase("/msg")) {
			if(split.length > 1) {
				pseudoPlayer._otherChatChannel = CHAT_CHANNEL_MSG;
				String targetName = split[1];
				Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
				if(targetPlayer != null) {
					pseudoPlayer._msgPlayer = targetPlayer.getName();
				}
				chatMessage(player, split);
			}
			return true;
		}
		else if(cmd.equalsIgnoreCase("/r") || cmd.equalsIgnoreCase("/reply")) {
			pseudoPlayer._otherChatChannel = CHAT_CHANNEL_MSG;
			Player lastMsgPlayer = player.getServer().getPlayer(""+pseudoPlayer._lastPlayerNameMsg);
			pseudoPlayer._msgPlayer = pseudoPlayer._lastPlayerNameMsg;
			if(lastMsgPlayer != null) {
				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(lastMsgPlayer.getName());
				if(!targetPseudoPlayer.isIgnoring(player.getName())) {
					String msg = implodeSplit(split, 1);
					lastMsgPlayer.sendMessage("["+ChatColor.LIGHT_PURPLE+"MSG"+ChatColor.WHITE+"] "+player.getName()+": "+msg);
					player.sendMessage("["+ChatColor.LIGHT_PURPLE+"MSG to "+lastMsgPlayer.getName()+ChatColor.WHITE+"] "+msg);
					targetPseudoPlayer._lastPlayerNameMsg = player.getName();
					Output.sendToAdminIRC(player, "MSG to "+lastMsgPlayer.getName(), msg);
				}
			}
			return true;
		}
		else if(cmd.equalsIgnoreCase("/enableglobal") || cmd.equalsIgnoreCase("/globalenable")) {
			if(pseudoPlayer.isGlobalEnabled())
				Output.simpleError(player, "Global chat is already enabled.");
			else {
				pseudoPlayer.setGlobalEnabled(true);
				Output.positiveMessage(player, "You have enabled global chat.");
			}
			return true;
		}
		else if(cmd.equalsIgnoreCase("/disableglobal") || cmd.equalsIgnoreCase("/globaldisable")) {
			if(!pseudoPlayer.isGlobalEnabled())
				Output.simpleError(player, "Global chat is already disabled.");
			else {
				pseudoPlayer.setGlobalEnabled(false);
				Output.positiveMessage(player, "You have disabled global chat.");
			}
			return true;
		}
		return false;
	}
	
	public static void chatAdmin(Player player, String[] split) {
		if(split.length > 1) {
			Output.chatAdmin(player, implodeSplit(split, 1));
		}
		else Output.simpleError(player, "Use \"/a (message)\".");
	}
	
	public static void chatLocal(Player player, String[] split) {
		if(split.length > 1) {
			Output.chatLocal(player, implodeSplit(split, 1));
		}
		else Output.simpleError(player, "Use \"/l (message)\".");
	}
	
	public static void chatGlobal(Player player, String[] split) {
		if(split.length > 1) {
			Output.chatGlobal(player, implodeSplit(split, 1));
		}
		else Output.simpleError(player, "Use \"/g (message)\".");
	}
	
	public static void chatShout(Player player, String[] split) {
		if(split.length > 1) {
			Output.chatShout(player, implodeSplit(split, 1));
		}
		else Output.simpleError(player, "Use \"/s (message)\".");
	}
	
	public static void chatWhisper(Player player, String[] split) {
		if(split.length > 1) {
			Output.chatWhisper(player, implodeSplit(split, 1));
		}
		else Output.simpleError(player, "Use \"/w (message)\".");
	}
	
	public static void chatClan(Player player, String[] split) {
		if(split.length > 1) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null)
				Output.clanSay(clan, player, implodeSplit(split, 1));
			else
				Output.simpleError(player, "You are not currently in a clan.");
		}
		else Output.simpleError(player, "Use \"/c (message)\".");
	}
	
	public static void chatParty(Player player, String[] split) {
		if(split.length > 1) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Party party = pseudoPlayer.getParty();
			if(party != null)
				Output.partySay(party, player, implodeSplit(split, 1));
			else
				Output.simpleError(player, "You are not currently in a party.");
		}
		else Output.simpleError(player, "Use \"/p (message)\".");
	}
	
	public static void chatClan(Player player, String message) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Clan clan = pseudoPlayer.getClan();
		if(clan != null)
			Output.clanSay(clan, player, message);
		else
			Output.simpleError(player, "You are not currently in a clan.");
	}
	
	public static void chatParty(Player player, String message) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Party party = pseudoPlayer.getParty();
		if(party != null)
			Output.partySay(party, player, message);
		else
			Output.simpleError(player, "You are not currently in a party.");
	}
	
	public static void chatMessage(Player player, String[] split) {
		if(split.length > 2) {
			String targetName = split[1];
			Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
			if(targetPlayer != null) {
				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
				if(targetPseudoPlayer._secret) {
					Output.simpleError(player, targetName+" is not currently online.");
				}
				else if(!targetPseudoPlayer.isIgnoring(player.getName())) {
					String msg = implodeSplit(split, 2);
					targetPlayer.sendMessage("["+ChatColor.LIGHT_PURPLE+"MSG"+ChatColor.WHITE+"] "+player.getName()+": "+msg);
					player.sendMessage("["+ChatColor.LIGHT_PURPLE+"MSG to "+targetPlayer.getName()+ChatColor.WHITE+"] "+msg);
					targetPseudoPlayer._lastPlayerNameMsg = player.getName();
					Output.sendToAdminIRC(player, "MSG to "+targetPlayer.getName(), msg);
				}
			}
			else Output.simpleError(player, targetName+" is not currently online.");
		}
		else Output.simpleError(player, "Use \"/m (player name) (message)\".");
	}
	
	public static String implodeSplit(String[] split, int startIndex) {
		String message = "";
    	int splitMessageLength = split.length;
		for(int i=startIndex; i<splitMessageLength; i++) {
			message += split[i];
			if(i < splitMessageLength-1)
				message = message + " ";
		}
		message = message.trim();
		return message;
	}
}
