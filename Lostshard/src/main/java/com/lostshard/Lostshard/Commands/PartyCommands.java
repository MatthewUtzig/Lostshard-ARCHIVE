package com.lostshard.Lostshard.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.base.Joiner;
import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Intake.Vanish;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Groups.Party;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;

public class PartyCommands {

	static PlayerManager pm = PlayerManager.getManager();

	@Command(aliases = { "info" }, desc = "Shows info about your current party")
	public void partyInfo(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		final Party party = pPlayer.getParty();
		if (party != null) {
			player.sendMessage(ChatColor.GOLD + "-Your Party-");
			final String partyMembersString = Joiner.on(", ").join(party.getMembers().usernames());
			player.sendMessage(ChatColor.YELLOW + "Party Members: " + ChatColor.WHITE + partyMembersString);

		} else
			Output.simpleError(player, "You are not currently in a party.");
	}

	@Command(aliases = { "invite" }, desc = "Joins a given party")
	public void partyInvite(@Sender Player player, @Sender PseudoPlayer pPlayer, @Vanish Player invite) {
		Party party = pPlayer.getParty();
		if (party == null) {
			party = new Party();
			party.getMembers().add(player);
			pPlayer.setParty(party);
		}
		if (invite == player) {
			Output.simpleError(player, "You cant invite your self.");
			return;
		}
		if (!party.isMember(invite)) {
			if (!party.isInvited(invite.getUniqueId())) {
				party.addInvited(invite.getUniqueId());
				Utils.sendSmartTextCommand(invite,
						ChatColor.GOLD + player.getName() + " has invited you to a party, Click to join.",
						ChatColor.LIGHT_PURPLE + "Click to join the party.", "/party join " + player.getName());
				// Output.positiveMessage(invitedPlayer,
				// player.getName()
				// + " has invited you to join a party.");
				// Output.positiveMessage(invitedPlayer,
				// "Use /party join " + player.getName());
				Output.positiveMessage(player,
						"You have invited " + invite.getName() + " to your party.");
			} else
				Output.simpleError(player, invite.getName() + " has already been invited to the party.");
		} else
			Output.simpleError(player, invite.getName() + " is already a member of the party.");
	}

	@Command(aliases = { "join" }, desc = "Joins a given party")
	public void partyJoin(@Sender Player player, @Sender PseudoPlayer pPlayer, @Vanish Player inviterPlayer) {
		final Party party = pPlayer.getParty();
		if (party == null) {
			final PseudoPlayer inviterPseudoPlayer = pm.getPlayer(inviterPlayer);
			final Party inviterParty = inviterPseudoPlayer.getParty();
			if (inviterParty != null) {
				if (inviterParty.isInvited(player.getUniqueId())) {
					if (inviterParty.getMembers().size() >= 8) {
						Output.simpleError(player, "The party is full, max clan size is 8.");
						return;
					}
					inviterParty.sendMessage(player.getName() + " has joined the party.");
					inviterParty.getInvited().remove(player);
					inviterParty.getMembers().add(player);
					pPlayer.setParty(inviterParty);
					Output.positiveMessage(player, "You have joined " + inviterPlayer.getName() + "'s party.");
				} else
					Output.simpleError(player, "You have not been invited to that party.");
			} else
				Output.simpleError(player, inviterPlayer.getName() + " is not in a party.");
		} else
			Output.simpleError(player, "You are already in a party.");
	}
	
	@Command(aliases = { "leave" }, desc = "Leaves your current party")
	public void partyLeave(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		final Party party = pPlayer.getParty();
		if (party != null) {
			party.getMembers().remove(player);
			pPlayer.setParty(null);
			party.sendMessage(player.getName() + " has left the party.");
			Output.positiveMessage(player, "You have left the party.");
		} else
			Output.simpleError(player, "You are not currently in a party.");
	}

	@Command(aliases = { "help", "" }, desc = "Party help", usage="(page)")
	public void party(CommandSender sender, @Optional(value="0") @Range(min=0) int page) {
		sender.sendMessage(ChatColor.GOLD + "-Party Commands-");
		sender.sendMessage(ChatColor.YELLOW + "/party invite (player name)");
		sender.sendMessage(ChatColor.YELLOW + "/party join");
		sender.sendMessage(ChatColor.YELLOW + "/party leave");
	}
}
