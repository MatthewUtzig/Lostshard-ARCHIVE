package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.ChatChannel;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.TabUtils;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class ChatCommand implements CommandExecutor, TabCompleter {

	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public ChatCommand(Lostshard plugin) {
		plugin.getCommand("global").setExecutor(this);
		plugin.getCommand("shout").setExecutor(this);
		plugin.getCommand("local").setExecutor(this);
		plugin.getCommand("whisper").setExecutor(this);
		plugin.getCommand("msg").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "Players may only perform this command.");
			return true;
		}
		Player player = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("global")) {
			globalChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("shout")) {
			shoutChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("local")) {
			localChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("whisper")) {
			whisperChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("msg")) {
			// msgChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("replay")) {
			// replayChat(player, args);
		}
		return true;
	}

	/**
	 * @param player
	 * @param args
	 * 
	 *            Output whisper chat for player.
	 */
	private void whisperChat(Player player, String[] args) {
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.WHISPER);
			Output.positiveMessage(player, "You have togglet whisper chat.");
			return;
		}

		String msg = Utils.getStringFromList(args);
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.WHISPER);
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
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.LOCAL);
			Output.positiveMessage(player, "You have togglet local chat.");
			return;
		}

		String msg = Utils.getStringFromList(args);
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.LOCAL);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	/**
	 * @param player
	 * @param args
	 * 
	 *            Outpit shout chat for player.
	 */
	private void shoutChat(Player player, String[] args) {
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.SHOUT);
			Output.positiveMessage(player, "You have toggled shout chat.");
			return;
		}

		String msg = Utils.getStringFromList(args);
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.SHOUT);
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
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have togglet global chat.");
			return;
		}

		String msg = Utils.getStringFromList(args);
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.GLOBAL);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("msg")) {
			if (sender instanceof Player)
				return TabUtils.OnlinePlayersTab(args,
						new Player[] { (Player) sender });
			else
				return TabUtils.OnlinePlayersTab(args);
		}
		return null;
	}

}
