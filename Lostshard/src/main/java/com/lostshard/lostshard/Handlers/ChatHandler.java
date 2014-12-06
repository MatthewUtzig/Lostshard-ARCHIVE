package com.lostshard.lostshard.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.lostshard.lostshard.Objects.ChatChannel;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
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

	public static void onPlayerChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		event.getRecipients().clear();
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(event.getPlayer());
		if (pPlayer.getChatChannel().equals(ChatChannel.LOCAL))
			localChat(event);
		else if (pPlayer.getChatChannel().equals(ChatChannel.SHOUT))
			shoutChat(event);
		else if (pPlayer.getChatChannel().equals(ChatChannel.WHISPER))
			whisperChat(event);
		else
			globalChat(event);
	}

	public static void whisperChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		for (Player p : Utils.getPlayersNear(event.getPlayer(),
				getWhisperChatRange()))
			event.getRecipients().add(p);
		event.setFormat(Utils.getDisplayName(event.getPlayer())
				+ ChatColor.WHITE + " whisper: " + event.getMessage());
	}

	public static void localChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		for (Player p : Utils.getPlayersNear(event.getPlayer(),
				getLocalChatRange()))
			event.getRecipients().add(p);
		event.setFormat(Utils.getDisplayName(event.getPlayer())
				+ ChatColor.WHITE + ": " + event.getMessage());
	}

	public static void shoutChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		for (Player p : Utils.getPlayersNear(event.getPlayer(),
				getShoutChatRange()))
			event.getRecipients().add(p);
		event.setFormat(Utils.getDisplayName(event.getPlayer())
				+ ChatColor.WHITE + " shouts: " + event.getMessage());
	}

	public static void globalChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		String prefix;
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(event.getPlayer());
		String star = "";
		if (pPlayer.isSubscriber())
			star="*";
		String title = pPlayer.getCurrentTitle();
		if(title != "")
			title+=" ";
		prefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Global"
					+ ChatColor.GOLD + "]"+star;
		for (Player p : Bukkit.getOnlinePlayers()) {
			pPlayer = PseudoPlayerHandler.getPlayer(p);
			if (!pPlayer.isChatChannelDisabled(ChatChannel.GLOBAL))
				event.getRecipients().add(p);
		}
		event.setFormat(prefix + title + Utils.getDisplayName(event.getPlayer())
				+ ChatColor.WHITE + ": " + event.getMessage());
	}

	public static void clanChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		Clan clan = pPlayer.getClan();
		clan.sendMessage(ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"]"+
				 Utils.getDisplayName(player)+ChatColor.WHITE+": "+event.getMessage());
	}

	public static void partyChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		Party party = pPlayer.getParty();
		party.sendMessage(ChatColor.WHITE+"["+ChatColor.DARK_PURPLE+"Party"+ChatColor.WHITE+"]"+
		 Utils.getDisplayName(player)+ChatColor.WHITE+": "+event.getMessage());
	}

}
