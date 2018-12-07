package com.lostshard.RPG.Groups.Parties;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class PartyHandler {
	public static void handleCommand(Player player, String[] split) {
		if(split.length > 1) {
			String secondaryCommand = split[1];
			if(secondaryCommand.equalsIgnoreCase("invite"))
				partyInvite(player, split);
			else if(secondaryCommand.equalsIgnoreCase("join"))
				partyJoin(player, split);
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
	}
	
	public static void partyInvite(Player player, String[] split) {
		if(split.length > 2) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Party party = pseudoPlayer.getParty();
			if(party == null) {
				party = new Party();
				party.addMember(player.getName());
				pseudoPlayer.setParty(party);
				RPG.addParty(party);
			}
			Player invitedPlayer = Utils.getPlugin().getServer().getPlayer(split[2]);
			if(invitedPlayer != null) {
				PseudoPlayer invitedPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(invitedPlayer.getName());
				if(invitedPseudoPlayer._secret) {
					Output.simpleError(player, "That player is not online.");
					return;
				}
				else if(!party.isMember(invitedPlayer.getName())) {
					if(!party.isInvited(invitedPlayer.getName())) {
						party.addInvited(invitedPlayer.getName());
						Output.positiveMessage(invitedPlayer, player.getName()+" has invited you to join a party.");
						Output.positiveMessage(invitedPlayer, "Use /party join "+player.getName());
						Output.positiveMessage(player, "You have invited "+invitedPlayer.getName()+" to your party.");
					}
					else Output.simpleError(player, invitedPlayer.getName()+" has already been invited to the party.");
				}
				else Output.simpleError(player, invitedPlayer.getName()+" is already a member of the party.");
			}
			else Output.simpleError(player, "That player is not online.");
		}
		else Output.simpleError(player, "Use \"/party invite (player name)\"");
	}
	
	public static void partyJoin(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Party party = pseudoPlayer.getParty();
		if(party == null) {
			if(split.length > 2) {
				Player inviterPlayer = Utils.getPlugin().getServer().getPlayer(split[2]);
				if(inviterPlayer != null) {
					PseudoPlayer inviterPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(inviterPlayer.getName());
					Party inviterParty = inviterPseudoPlayer.getParty();
					if(inviterParty != null) {
						if(inviterParty.isInvited(player.getName())) {
							Output.partyMessage(inviterParty, player.getName()+" has joined the party.");
							inviterParty.removeInvited(player.getName());
							inviterParty.addMember(player.getName());
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
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Party party = pseudoPlayer.getParty();
		if(party != null) {
			party.removeMember(player.getName());
			pseudoPlayer.setParty(null);
			party.sendMessage(player.getName()+" has left the party.");
			Output.positiveMessage(player, "You have left the party.");
		}
		else Output.simpleError(player, "You are not currently in a party.");
	}
	
	public static void partyInfo(Player player) {
		// Output party details
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Party party = pseudoPlayer.getParty();
		if(party != null) {
			player.sendMessage(ChatColor.GOLD+"-Your Party-");
			ArrayList<String> partyMemberNames = party.getPartyMemberNames();
			String partyMembersString = "";
			int numPartyMembers = partyMemberNames.size();
			for(int i=0; i<numPartyMembers; i++) {
				partyMembersString += partyMemberNames.get(i);
				if(i < numPartyMembers-1)
					partyMembersString += ", ";
			}
			player.sendMessage(ChatColor.YELLOW+"Party Members: "+ChatColor.WHITE+partyMembersString);
			
		}
		else Output.simpleError(player, "You are not currently in a party.");
	}
}
