package com.lostshard.lostshard.Commands;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.ChatChannel;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.TabUtils;

/**
 * @author Jacob Rosborg
 *
 */
public class ChatCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as JavaPlugin
	 */
	public ChatCommand(Lostshard plugin) {
		plugin.getCommand("global").setExecutor(this);
		plugin.getCommand("shout").setExecutor(this);
		plugin.getCommand("local").setExecutor(this);
		plugin.getCommand("whisper").setExecutor(this);
		plugin.getCommand("c").setExecutor(this);
		plugin.getCommand("p").setExecutor(this);
		plugin.getCommand("msg").setExecutor(this);
		plugin.getCommand("replay").setExecutor(this);
		plugin.getCommand("toggleglobal").setExecutor(this);
		plugin.getCommand("togglemsg").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
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
		} else if (cmd.getName().equalsIgnoreCase("c")) {
			clanChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("p")) {
			partyChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("msg")) {
			msgChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("replay")) {
			replayChat(player, args);
		} else if (cmd.getName().equalsIgnoreCase("toggleglobal")) {
			toggleGlobalChat(player);
		} else if (cmd.getName().equalsIgnoreCase("togglemsg")) {
			toggleMsgChat(player);
		}
		return true;
	}

	private void toggleMsgChat(Player player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.isChatChannelDisabled(ChatChannel.PRIVATE)) {
			pPlayer.enableChatChannel(ChatChannel.PRIVATE);
			Output.positiveMessage(player, "You have enabled Private Chat.");
		}else{
			pPlayer.disableChatChannel(ChatChannel.PRIVATE);
			Output.positiveMessage(player, "You have disabled Private Chat.");			
		}
	}

	private void toggleGlobalChat(Player player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.isChatChannelDisabled(ChatChannel.GLOBAL)) {
			pPlayer.enableChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have enabled Global Chat.");
		}else{
			pPlayer.disableChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have disabled Global Chat.");			
		}
	}

	private void replayChat(Player player, String[] args) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Player to = Bukkit.getPlayer(pPlayer.getLastResiver());
		if(to == null) {
			Output.simpleError(player, "ERROR player not online.");
			return;
		}
		String message = StringUtils.join(args, " ");
		
		player.sendMessage(ChatColor.WHITE+"["+ChatColor.LIGHT_PURPLE+"MSG to "+to.getName()+ChatColor.WHITE+"] " +  message);
		to.sendMessage(ChatColor.WHITE+"["+ChatColor.LIGHT_PURPLE+"MSG from "+player.getName()+ChatColor.WHITE+"] " + message);
	}

	private void msgChat(Player player, String[] args) {
		if(args.length < 2) {
			Output.simpleError(player, "/msg (Player) (message)");
			return;
		}
		String targetName = args[0];
		Player targetPlayer = Bukkit.getPlayer(targetName);
		if(player == null) {
			Output.simpleError(player, "player not online");
			return;
		}
		String message = StringUtils.join(args, " ", 1, args.length);
		
		player.sendMessage(ChatColor.WHITE+"["+ChatColor.LIGHT_PURPLE+"MSG to "+targetPlayer.getName()+ChatColor.WHITE+"] " +  message);
		targetPlayer.sendMessage(ChatColor.WHITE+"["+ChatColor.LIGHT_PURPLE+"MSG from "+player.getName()+ChatColor.WHITE+"] " + message);
		
	}

	/**
	 * @param player
	 * @param args
	 * 
	 *            Output whisper chat for player.
	 */
	private void whisperChat(Player player, String[] args) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.WHISPER);
			Output.positiveMessage(player, "You have togglet whisper chat.");
			return;
		}

		String msg = StringUtils.join(args, " ");
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
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.LOCAL);
			Output.positiveMessage(player, "You have togglet local chat.");
			return;
		}

		String msg = StringUtils.join(args, " ");
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.LOCAL);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}

	/**
	 * @param player
	 * @param args
	 * 
	 *            Output shout chat for player.
	 */
	private void shoutChat(Player player, String[] args) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.SHOUT);
			Output.positiveMessage(player, "You have toggled shout chat.");
			return;
		}

		String msg = StringUtils.join(args, " ");
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
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.GLOBAL);
			Output.positiveMessage(player, "You have togglet global chat.");
			return;
		}

		String msg = StringUtils.join(args, " ");
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.GLOBAL);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}
	
	/**
	 * @param player
	 * @param args
	 * 
	 *            Output clan chat for player.
	 */
	private void clanChat(Player player, String[] args) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.CLAN);
			Output.positiveMessage(player, "You have togglet clan chat.");
			return;
		}
		
		String msg = StringUtils.join(args, " ");
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.CLAN);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}
	
	/**
	 * @param player
	 * @param args
	 * 
	 *            Output party chat for player.
	 */
	private void partyChat(Player player, String[] args) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if (args.length < 1) {
			pPlayer.setChatChannel(ChatChannel.PARTY);
			Output.positiveMessage(player, "You have togglet party chat.");
			return;
		}

		String msg = StringUtils.join(args, " ");
		ChatChannel curChannel = pPlayer.getChatChannel();
		pPlayer.setChatChannel(ChatChannel.PARTY);
		player.chat(msg);
		pPlayer.setChatChannel(curChannel);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("msg") && args.length == 1) {
			if (sender instanceof Player)
				return TabUtils.OnlinePlayersTab(args,
						new Player[] { (Player) sender });
			else
				return TabUtils.OnlinePlayersTab(args);
		}
		return TabUtils.empty();
	}

}
