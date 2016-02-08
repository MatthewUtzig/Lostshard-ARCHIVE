package com.lostshard.Lostshard.Commands;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Intake.Vanish;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.ChatChannel;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Output;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Text;

/**
 * @author Jacob Rosborg
 *
 */
public class ChatCommand {

	PlayerManager pm = PlayerManager.getManager();

	/**
	 * @param player
	 * @param args
	 *
	 *            Output clan chat for player.
	 */
	@SuppressWarnings("unchecked")
	@Command(aliases = {"c"}, 
			desc = "Toggles or sends a message in the clan channel",
			help = "Toggles or sends a message in the clan channel",
			usage = "(message)")
	public void clanChat(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String message) {
		if (message == null) {
			pPlayer.setChatChannel(ChatChannel.CLAN);
			Output.positiveMessage(player, "You have toggled clan chat.");
			return;
		}

		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.CLAN);
		Bukkit.getPluginManager().callEvent(new AsyncPlayerChatEvent(true, player, message, (Set<Player>) Bukkit.getOnlinePlayers()));
		pPlayer.setChatChannel(curChannel);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output global chat for player.
	 */
	@SuppressWarnings("unchecked")
	@Command(aliases = {"global", "g"}, 
			desc = "Toggles or sends a message in the global channel",
			help = "Toggles or sends a message in the global channel",
			usage = "(message)")
	public void globalChat(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String message) {
		if (message == null) {
			pPlayer.setChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have toggled global chat.");
			return;
		}

		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.GLOBAL);
		Bukkit.getPluginManager().callEvent(new AsyncPlayerChatEvent(true, player, message, (Set<Player>) Bukkit.getOnlinePlayers()));
		pPlayer.setChatChannel(curChannel);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output local chat for player.
	 */
	@SuppressWarnings("unchecked")
	@Command(aliases = {"local", "l"}, 
			desc = "Toggles or sends a message in the local channel",
			help = "Toggles or sends a message in the local channel",
			usage = "(message)")
	public void localChat(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String message) {
		if (message == null) {
			pPlayer.setChatChannel(ChatChannel.LOCAL);
			Output.positiveMessage(player, "You have toggled local chat.");
			return;
		}

		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.LOCAL);
		Bukkit.getPluginManager().callEvent(new AsyncPlayerChatEvent(true, player, message, (Set<Player>) Bukkit.getOnlinePlayers()));
		pPlayer.setChatChannel(curChannel);
	}

	
	@Command(aliases = {"msg"}, 
			desc = "Send private messages to players",
			help = "Send private messages to players",
			usage = "<player> <message>",
			min = 3)
	public void msgChat(@Sender Player player, @Vanish Player target, @Optional @Text String message) {
		if (player == target) {
			Output.simpleError(player, "You can't msg your self.");
			return;
		}

		final PseudoPlayer pPlayer = this.pm.getPlayer(target);
		player.sendMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "MSG to " + target.getName()
				+ ChatColor.WHITE + "] " + message);
		if (!pPlayer.getDisabledChatChannels().contains(ChatChannel.PRIVATE)
				&& !pPlayer.getIgnored().contains(player.getUniqueId())) {
			target.sendMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "MSG from " + player.getName()
					+ ChatColor.WHITE + "] " + message);
			pm.getPlayer(target).setLastResiver(player.getUniqueId());
		}
	}
	
	/**
	 * @param player
	 * @param args
	 *
	 *            Output party chat for player.
	 */
	@SuppressWarnings("unchecked")
	@Command(aliases = {"p"}, 
			desc = "Toggles or sends a message in the party channel",
			help = "Toggles or sends a message in the party channel",
			usage = "(message)")
	public void partyChat(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String message) {
		if (message == null) {
			pPlayer.setChatChannel(ChatChannel.PARTY);
			Output.positiveMessage(player, "You have toggled party chat.");
			return;
		}

		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.PARTY);
		Bukkit.getPluginManager().callEvent(new AsyncPlayerChatEvent(true, player, message, (Set<Player>) Bukkit.getOnlinePlayers()));
		pPlayer.setChatChannel(curChannel);
	}

	
	@Command(aliases = {"reply", "r"}, 
			desc = "Reply's to private messages",
			help = "Reply's to last received private messages",
			usage = "<message>",
			min = 3)
	public void replyChat(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String message) {
		if (pPlayer.getLastResiver() == null) {
			Output.simpleError(player, "You havent received any messages.");
			return;
		}

		final Player to = Bukkit.getPlayer(pPlayer.getLastResiver());
		if (to == null) {
			Output.simpleError(player,
					Bukkit.getOfflinePlayer(pPlayer.getLastResiver()).getName() + " is no longer online.");
			return;
		}
		final PseudoPlayer toPp = this.pm.getPlayer(to);
		toPp.setLastResiver(player.getUniqueId());

		player.sendMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "MSG to " + to.getName() + ChatColor.WHITE
				+ "] " + message);
		to.sendMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "MSG from " + player.getName() + ChatColor.WHITE
				+ "] " + message);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output shout chat for player.
	 */
	@SuppressWarnings("unchecked")
	@Command(aliases = {"shout", "s"}, 
			desc = "Toggles or sends a message in the shout channel",
			help = "Toggles or sends a message in the shout channel",
			usage = "(message)")
	public void shoutChat(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String message) {
		if (message == null) {
			pPlayer.setChatChannel(ChatChannel.SHOUT);
			Output.positiveMessage(player, "You have toggled shout chat.");
			return;
		}

		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.SHOUT);
		Bukkit.getPluginManager().callEvent(new AsyncPlayerChatEvent(true, player, message, (Set<Player>) Bukkit.getOnlinePlayers()));
		pPlayer.setChatChannel(curChannel);
	}

	@Command(aliases = {"toggleglobal", "toggleglobalchat"}, 
			desc = "Toggles where the global chat should be visible",
			help = "Toggles where the global chat should be visible",
			max = 1)
	public void toggleGlobalChat(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (pPlayer.isChatChannelDisabled(ChatChannel.GLOBAL)) {
			pPlayer.enableChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have enabled Global Chat.");
		} else {
			pPlayer.disableChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have disabled Global Chat.");
		}
	}

	@Command(aliases = {"togglemsg", "toggleprivatechat", "toggleprivate"}, 
			desc = "Toggles where the private chat should be visible",
			help = "Toggles where the private chat should be visible",
			max = 1)
	public void toggleMsgChat(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (pPlayer.isChatChannelDisabled(ChatChannel.PRIVATE)) {
			pPlayer.enableChatChannel(ChatChannel.PRIVATE);
			Output.positiveMessage(player, "You have enabled Private Chat.");
		} else {
			pPlayer.disableChatChannel(ChatChannel.PRIVATE);
			Output.positiveMessage(player, "You have disabled Private Chat.");
		}
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output whisper chat for player.
	 */
	@SuppressWarnings("unchecked")
	@Command(aliases = {"whisper", "w"}, 
			desc = "Toggles or sends a message in the whisper channel",
			help = "Toggles or sends a message in the whisper channel",
			usage = "(message)")
	public void whisperChat(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String message) {
		if (message == null) {
			pPlayer.setChatChannel(ChatChannel.WHISPER);
			Output.positiveMessage(player, "You have toggled whisper chat.");
			return;
		}

		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.WHISPER);
		Bukkit.getPluginManager().callEvent(new AsyncPlayerChatEvent(true, player, message, (Set<Player>) Bukkit.getOnlinePlayers()));
		pPlayer.setChatChannel(curChannel);
	}

}
