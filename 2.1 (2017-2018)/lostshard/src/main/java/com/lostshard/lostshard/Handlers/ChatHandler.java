package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.ChatChannel;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class ChatHandler {

	static SpellManager sm = SpellManager.getManager();

	static PlayerManager pm = PlayerManager.getManager();

	private static int whisperChatRange = 5;

	private static int localChatRange = 10;

	private static int shoutChatRange = 50;

	public static void clanChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Clan clan = pPlayer.getClan();
		if (clan == null) {
			Output.simpleError(player, "You are currently not in a clan.");
			return;
		}
		for (final Player p : pPlayer.getClan().getOnlineMembers())
			event.getRecipients().add(p);
		event.setFormat(ChatColor.WHITE + "[" + ChatColor.GREEN + "Clan" + ChatColor.WHITE + "]" + ChatColor.WHITE + " %s" + ChatColor.WHITE + ": %s");
	}

	public static int getLocalChatRange() {
		return localChatRange;
	}

	public static int getShoutChatRange() {
		return shoutChatRange;
	}

	public static int getWhisperChatRange() {
		return whisperChatRange;
	}

	public static void globalChat(AsyncPlayerChatEvent event, List<UUID> ignore) {
		if (event.isCancelled())
			return;
		String prefix;
		PseudoPlayer pPlayer = pm.getPlayer(event.getPlayer());
		String star = "";
		if (pPlayer.isSubscriber())
			star = ChatColor.GOLD + "*";
		String title = pPlayer.getCurrentTitle();
		if (title != "")
			title += " ";
		prefix = ChatColor.WHITE + "[" + ChatColor.YELLOW + "Global" + ChatColor.WHITE + "]" + star;
		for (final Player p : Bukkit.getOnlinePlayers()) {
			pPlayer = pm.getPlayer(p);
			if (!pPlayer.isChatChannelDisabled(ChatChannel.GLOBAL) && !ignore.contains(p.getUniqueId()))
				event.getRecipients().add(p);
		}
		event.setFormat(prefix + " " + title + "%s" + ChatColor.WHITE + ": " + "%s");
	}

	public static void localChat(AsyncPlayerChatEvent event, List<UUID> ignore) {
		if (event.isCancelled())
			return;
		for (final Player p : Utils.getPlayersNear(event.getPlayer(), getLocalChatRange()))
			if (!ignore.contains(p.getUniqueId()))
				event.getRecipients().add(p);
		event.setFormat("%s" + ChatColor.WHITE + ": " + "%s");
	}

	public static void onPlayerChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		event.getRecipients().clear();
		final List<UUID> ignore = new ArrayList<UUID>();
		for (final Player p : Bukkit.getOnlinePlayers()) {
			final PseudoPlayer pP = pm.getPlayer(p);
			if (pP.getIgnored().contains(event.getPlayer().getUniqueId()))
				ignore.add(p.getUniqueId());
		}
		final PseudoPlayer pPlayer = pm.getPlayer(event.getPlayer());
		if (pPlayer.getPromptedSpell() != null) {
			sm.onPlayerPromt(event);
			return;
		}
		if (pPlayer.getChatChannel().equals(ChatChannel.LOCAL))
			localChat(event, ignore);
		else if (pPlayer.getChatChannel().equals(ChatChannel.SHOUT))
			shoutChat(event, ignore);
		else if (pPlayer.getChatChannel().equals(ChatChannel.WHISPER))
			whisperChat(event, ignore);
		else if (pPlayer.getChatChannel().equals(ChatChannel.CLAN))
			clanChat(event);
		else if (pPlayer.getChatChannel().equals(ChatChannel.PARTY))
			partyChat(event);
		else
			globalChat(event, ignore);
	}

	public static void partyChat(AsyncPlayerChatEvent event) {
		if (event.isCancelled())
			return;
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Party party = pPlayer.getParty();
		if (party == null) {
			Output.simpleError(player, "You are currently not in a party.");
			return;
		}
		for (final Player p : pPlayer.getParty().getOnlineMembers())
			event.getRecipients().add(p);
		event.setFormat(ChatColor.WHITE + "[" + ChatColor.DARK_PURPLE + "Party" + ChatColor.WHITE + "]" + ChatColor.WHITE + " %s" + ChatColor.WHITE + ": %s");
	}

	public static void setLocalChatRange(int localChatRange) {
		ChatHandler.localChatRange = localChatRange;
	}

	public static void setShoutChatRange(int shoutChatRange) {
		ChatHandler.shoutChatRange = shoutChatRange;
	}

	public static void setWhisperChatRange(int whisperChatRange) {
		ChatHandler.whisperChatRange = whisperChatRange;
	}

	public static void shoutChat(AsyncPlayerChatEvent event, List<UUID> ignore) {
		if (event.isCancelled())
			return;
		for (final Player p : Utils.getPlayersNear(event.getPlayer(), getShoutChatRange()))
			if (!ignore.contains(p.getUniqueId()))
				event.getRecipients().add(p);
		event.setFormat("%s" + ChatColor.WHITE + " shouts: " + "%s");
	}

	public static void whisperChat(AsyncPlayerChatEvent event, List<UUID> ignore) {
		if (event.isCancelled())
			return;
		for (final Player p : Utils.getPlayersNear(event.getPlayer(), getWhisperChatRange()))
			if (!ignore.contains(p.getUniqueId()))
				event.getRecipients().add(p);
		event.setFormat("%s" + ChatColor.WHITE + " whisper: " + "%s");
	}

}
