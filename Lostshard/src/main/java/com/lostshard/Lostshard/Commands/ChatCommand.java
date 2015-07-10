package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.ChatChannel;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.TabUtils;

/**
 * @author Jacob Rosborg
 *
 */
public class ChatCommand extends LostshardCommand {

	PlayerManager pm = PlayerManager.getManager();

	/**
	 * @param Lostshard
	 *            as JavaPlugin
	 */
	public ChatCommand(Lostshard plugin) {
		super(plugin, "global", "shout", "local", "whisper", "c", "p", "msg", "reply", "toggleglobal", "togglemsg");
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output clan chat for player.
	 */
	private void clanChat(Player player, String[] args) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.CLAN);
			Output.positiveMessage(player, "You have togglet clan chat.");
			return;
		}

		final String msg = StringUtils.join(args, " ");
		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.CLAN);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output global chat for player.
	 */
	private void globalChat(Player player, String[] args) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have togglet global chat.");
			return;
		}

		final String msg = StringUtils.join(args, " ");
		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.GLOBAL);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output local chat for player.
	 */
	private void localChat(Player player, String[] args) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.LOCAL);
			Output.positiveMessage(player, "You have togglet local chat.");
			return;
		}

		final String msg = StringUtils.join(args, " ");
		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.LOCAL);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	private void msgChat(Player player, String[] args) {
		if (args.length < 2) {
			Output.simpleError(player, "/msg (Player) (message)");
			return;
		}
		final String targetName = args[0];
		final Player targetPlayer = Bukkit.getPlayer(targetName);
		if (player == null) {
			Output.simpleError(player, "player not online");
			return;
		}
		if (player == targetPlayer) {
			Output.simpleError(player, "You can't msg your self.");
			return;
		}
		final String message = StringUtils.join(args, " ", 1, args.length);

		final PseudoPlayer pPlayer = this.pm.getPlayer(targetPlayer);
		pPlayer.setLastResiver(player.getUniqueId());
		player.sendMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE
				+ "MSG to " + targetPlayer.getName() + ChatColor.WHITE + "] "
				+ message);
		if (!pPlayer.getDisabledChatChannels().contains(ChatChannel.PRIVATE)
				&& !pPlayer.getIgnored().contains(player.getUniqueId()))
			targetPlayer.sendMessage(ChatColor.WHITE + "["
					+ ChatColor.LIGHT_PURPLE + "MSG from " + player.getName()
					+ ChatColor.WHITE + "] " + message);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return true;
		}
		final Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("global"))
			this.globalChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("shout"))
			this.shoutChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("local"))
			this.localChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("whisper"))
			this.whisperChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("c"))
			this.clanChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("p"))
			this.partyChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("msg"))
			this.msgChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("reply"))
			this.replayChat(player, args);
		else if (cmd.getName().equalsIgnoreCase("toggleglobal"))
			this.toggleGlobalChat(player);
		else if (cmd.getName().equalsIgnoreCase("togglemsg"))
			this.toggleMsgChat(player);
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("msg") && args.length == 1)
			if (sender instanceof Player)
				return TabUtils.OnlinePlayersTab(args, (Player) sender);
			else
				return TabUtils.OnlinePlayersTab(args);
		return TabUtils.empty();
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output party chat for player.
	 */
	private void partyChat(Player player, String[] args) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.PARTY);
			Output.positiveMessage(player, "You have togglet party chat.");
			return;
		}

		final String msg = StringUtils.join(args, " ");
		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.PARTY);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	private void replayChat(Player player, String[] args) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if(pPlayer.getLastResiver() == null) {
			Output.simpleError(player, "You havent received any messages.");
			return;
		}

		final Player to = Bukkit.getPlayer(pPlayer.getLastResiver());
		if (to == null) {
			Output.simpleError(player,
					Bukkit.getOfflinePlayer(pPlayer.getLastResiver()).getName()
							+ " is no longer online.");
			return;
		}
		final PseudoPlayer toPp = pm.getPlayer(to);
		toPp.setLastResiver(to.getUniqueId());
		final String message = StringUtils.join(args, " ");

		player.sendMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE
				+ "MSG to " + to.getName() + ChatColor.WHITE + "] " + message);
		to.sendMessage(ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE
				+ "MSG from " + player.getName() + ChatColor.WHITE + "] "
				+ message);
	}

	/**
	 * @param player
	 * @param args
	 *
	 *            Output shout chat for player.
	 */
	private void shoutChat(Player player, String[] args) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.SHOUT);
			Output.positiveMessage(player, "You have toggled shout chat.");
			return;
		}

		final String msg = StringUtils.join(args, " ");
		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.SHOUT);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	private void toggleGlobalChat(Player player) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (pPlayer.isChatChannelDisabled(ChatChannel.GLOBAL)) {
			pPlayer.enableChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have enabled Global Chat.");
		} else {
			pPlayer.disableChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have disabled Global Chat.");
		}
	}

	private void toggleMsgChat(Player player) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
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
	private void whisperChat(Player player, String[] args) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.WHISPER);
			Output.positiveMessage(player, "You have togglet whisper chat.");
			return;
		}

		final String msg = StringUtils.join(args, " ");
		final ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.WHISPER);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

}
