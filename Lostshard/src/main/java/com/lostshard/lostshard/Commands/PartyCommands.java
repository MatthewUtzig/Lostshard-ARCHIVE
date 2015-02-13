package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class PartyCommands implements CommandExecutor, TabCompleter {

	static PlayerManager pm = PlayerManager.getManager();
	
	public PartyCommands(Lostshard plugin) {
		plugin.getCommand("party").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("party")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			if(args.length > 0) {
				String secondaryCommand = args[0];
				if(secondaryCommand.equalsIgnoreCase("invite"))
					partyInvite(player, args);
				else if(secondaryCommand.equalsIgnoreCase("join"))
					partyJoin(player, args);
				else if(secondaryCommand.equalsIgnoreCase("leave"))
					partyLeave(player);
				else if(secondaryCommand.equalsIgnoreCase("info"))
					partyInfo(player);
				else if(secondaryCommand.equalsIgnoreCase("help")) {
					player.sendMessage(ChatColor.GOLD+"-Party Commands-");
					player.sendMessage(ChatColor.YELLOW+"/party invite (player name)");
					player.sendMessage(ChatColor.YELLOW+"/party join");
					player.sendMessage(ChatColor.YELLOW+"/party leave");
				}
			}
			else {
				Output.simpleError(player, "Use \"/party help\" for commands.");
			}

			return true;
		}
		return false;
	}

	public static void partyInvite(Player player, String[] split) {
		if(split.length > 1) {
			PseudoPlayer pseudoPlayer = pm.getPlayer(player);
			Party party = pseudoPlayer.getParty();
			if(party == null) {
				party = new Party();
				party.addMember(player.getUniqueId());
				pseudoPlayer.setParty(party);
			}
			@SuppressWarnings("deprecation")
			Player invitedPlayer = Bukkit.getPlayer(split[1]);
			if(invitedPlayer != null) {
				if(invitedPlayer == player) {
					Output.simpleError(player, "You cant invite your self.");
					return;
				}
//				PseudoPlayer invitedPseudoPlayer = pm.getPlayer(invitedPlayer);
//				if(invitedPseudoPlayer._secret) {
//					Output.simpleError(player, "That player is not online.");
//					return;
//				}else
				if(!party.isMember(invitedPlayer)) {
					if(!party.isInvited(invitedPlayer.getUniqueId())) {
						party.addInvited(invitedPlayer.getUniqueId());
						Output.positiveMessage(invitedPlayer, player.getName()+" has invited you to join a party.");
						Output.positiveMessage(invitedPlayer, "Use /party join "+player.getName());
						Output.positiveMessage(player, "You have invited "+invitedPlayer.getName()+" to your party.");
					} else Output.simpleError(player, invitedPlayer.getName()+" has already been invited to the party.");
				} else Output.simpleError(player, invitedPlayer.getName()+" is already a member of the party.");
			} else Output.simpleError(player, "That player is not online.");
		} else Output.simpleError(player, "Use \"/party invite (player name)\"");
	}
	
	public static void partyJoin(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		Party party = pseudoPlayer.getParty();
		if(party == null) {
			if(split.length > 1)  {
				@SuppressWarnings("deprecation")
				Player inviterPlayer = Bukkit.getPlayer(split[1]);
				if(inviterPlayer != null) {
					PseudoPlayer inviterPseudoPlayer = pm.getPlayer(inviterPlayer);
					Party inviterParty = inviterPseudoPlayer.getParty();
					if(inviterParty != null) {
						if(inviterParty.isInvited(player.getUniqueId())) {
							inviterParty.sendMessage(player.getName()+" has joined the party.");
							inviterParty.removeInvited(player.getUniqueId());
							inviterParty.addMember(player.getUniqueId());
							pseudoPlayer.setParty(inviterParty);
							Output.positiveMessage(player, "You have joined "+inviterPlayer.getName()+"'s party.");
						}
						else Output.simpleError(player, "You have not been invited to that party.");
					}
					else Output.simpleError(player, inviterPlayer.getName()+" is not in a party.");
				}
				else Output.simpleError(player, "That player is not online.");
			}
			else Output.simpleError(player, "Invalid syntax.");
		}
		else Output.simpleError(player, "You are already in a party.");
	}
	
	public static void partyLeave(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		Party party = pseudoPlayer.getParty();
		if(party != null) {
			party.removeMember(player.getUniqueId());
			pseudoPlayer.setParty(null);
			party.sendMessage(player.getName()+" has left the party.");
			Output.positiveMessage(player, "You have left the party.");
		}
		else Output.simpleError(player, "You are not currently in a party.");
	}
	
	public static void partyInfo(Player player) {
		// Output party details
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		Party party = pseudoPlayer.getParty();
		if(party != null) {
			player.sendMessage(ChatColor.GOLD+"-Your Party-");
			String partyMembersString = Utils.listToString(Utils.UUIDArrayToUsernameArray(party.getMembers()));
			player.sendMessage(ChatColor.YELLOW+"Party Members: "+ChatColor.WHITE+partyMembersString);
			
		}
		else Output.simpleError(player, "You are not currently in a party.");
	}

	
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
	
}
