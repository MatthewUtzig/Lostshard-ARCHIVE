package com.lostshard.lostshard.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.lostshard.lostshard.Objects.ChatChannel;
import com.lostshard.lostshard.Objects.PseudoPlayer;
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
		event.setFormat(Utils.getColoredName(event.getPlayer())
				+ ChatColor.WHITE + " whisper: " + event.getMessage());
	}

	public static void localChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		for (Player p : Utils.getPlayersNear(event.getPlayer(),
				getLocalChatRange()))
			event.getRecipients().add(p);
		event.setFormat(Utils.getColoredName(event.getPlayer())
				+ ChatColor.WHITE + ": " + event.getMessage());
	}

	public static void shoutChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		for (Player p : Utils.getPlayersNear(event.getPlayer(),
				getShoutChatRange()))
			event.getRecipients().add(p);
		event.setFormat(Utils.getColoredName(event.getPlayer())
				+ ChatColor.WHITE + " shouts: " + event.getMessage());
	}

	@SuppressWarnings("deprecation")
	public static void globalChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		String prefix;
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(event.getPlayer());
		if (pPlayer.isSubscriber())
			prefix = ChatColor.GOLD + "[" + ChatColor.YELLOW + "Global"
					+ ChatColor.GOLD + "]* ";
		else
			prefix = ChatColor.WHITE + "[" + ChatColor.YELLOW + "Global"
					+ ChatColor.WHITE + "] ";
		// TODO Check for title here
		for (Player p : Bukkit.getOnlinePlayers()) {
			pPlayer = PseudoPlayerHandler.getPlayer(p);
			if (pPlayer.isGlobalChat())
				event.getRecipients().add(p);
		}
		event.setFormat(prefix + Utils.getColoredName(event.getPlayer())
				+ ChatColor.WHITE + ": " + event.getMessage());
	}

	public static void clanChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		// ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"]"+
		// Utils.getColoredName(player)+ChatColor.WHITE+": ";

	}

	public static void partyChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		// ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"]"+
		// Utils.getColoredName(player)+ChatColor.WHITE+": ";

	}

}
