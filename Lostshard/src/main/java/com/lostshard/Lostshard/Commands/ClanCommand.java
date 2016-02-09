package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Handlers.HelpHandler;
import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;
import com.sk89q.intake.parametric.annotation.Text;
import com.sk89q.intake.parametric.annotation.Validate;

public class ClanCommand {

	PlayerManager pm = PlayerManager.getManager();
	ClanManager cm = ClanManager.getManager();

	@Command(aliases = { "demote" }, desc = "Demotes a player from leader to member", usage = "<player>")
	public void clanDemote(@Sender Player player, @Sender PseudoPlayer pPlayer, OfflinePlayer target) {
		final Clan clan = pPlayer.getClan();
		if(clan == null) {
			Output.simpleError(player, "You are not currently in a clan.");
			return;
		}
		
		if(!clan.isOwner(player)) {
			Output.simpleError(player, "Only a clan owner may demote clan members.");
			return;
		}
		
		if(!clan.isMember(target)) {
			Output.simpleError(player, "The player "+target.getName()+" is not a member of your clan.");
			return;
		}
		
		if (!clan.isLeader(target.getUniqueId())) {
			Output.simpleError(player, "Only a leader may be demoted, you may kick any member.");
			return;
		}
		clan.getLeaders().remove(target);
		clan.getMembers().add(target);
		if (target.isOnline())
			Output.positiveMessage(target.getPlayer(),
					"You have been demoted to a normal member in your clan.");
		Output.positiveMessage(player,
				"You have demoted " + target.getName() + " to a normal member in your clan.");	
	}

	@Command(aliases = { "disband" }, desc = "Disbands the current clan")
	public void clanDisband(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		final Clan clan = pPlayer.getClan();
		
		if(clan == null) {
			Output.simpleError(player, "You are not currently in a clan.");
			return;
		}
		
		if(!clan.isOwner(player)) {
			Output.simpleError(player, "Only the clan owner may disband the clan.");
			return;
		}
		final List<Player> onlineMembers = clan.getOnlineMembers();
		for (final Player p : onlineMembers)
			// inform them the clan is gone
			Output.simpleError(p, "Your clan has been disbanded.");
		clan.delete();
		this.cm.getClans().remove(clan);
	}

	@Command(aliases = { "info" }, desc = "Displays info about your current clan")
	public void clanInfo(@Sender Player player) {
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		final Clan clan = pseudoPlayer.getClan();
		if (clan != null)
			Output.outputClanInfo(player, clan);
		else
			Output.simpleError(player, "You are not currently in a clan.");
	}

	@Command(aliases = { "invite" }, desc = "Invites a given player to your caln", usage="<player>")
	public void clanInvite(@Sender Player player, @Sender PseudoPlayer pPlayer, Player target) {
		final Clan clan = pPlayer.getClan();
		if (clan != null) {
			if (clan.isOwner(player.getUniqueId()) || clan.isLeader(player.getUniqueId())) {
				if (target != null) {
					if (!target.getName().equalsIgnoreCase(player.getName())) {
						final PseudoPlayer targetPseudoPlayer = this.pm.getPlayer(target);
						if (targetPseudoPlayer.getClan() == null) {
							if (!clan.isOwner(target) && !clan.isLeader(target.getUniqueId())
									&& !clan.isMember(target.getUniqueId())) {
								if (!clan.isInvited(target.getUniqueId())) {
									clan.addInvited(target.getUniqueId());
									Output.positiveMessage(player,
											"You have invited " + target.getName() + " to your clan.");
									Utils.sendSmartTextCommand(target,
											ChatColor.GOLD + player.getName() + " has invited you to "
													+ clan.getName() + ", Click to join.",
											ChatColor.LIGHT_PURPLE + "Click to join the clan.",
											"/clan join " + clan.getName());
									// Output.positiveMessage(targetPlayer,
									// "You have been invited to the "
									// + clan.getName()
									// + " clan.");
									// Output.positiveMessage(
									// targetPlayer,
									// "\"/clan join "
									// + clan.getName()
									// + "\" to join.");
								} else
									Output.simpleError(player,
											target.getName() + " has already been invited to this clan.");
							} else
								Output.simpleError(player, "That player is already a member of this clan.");
						} else
							Output.simpleError(player, "That player is already in a clan.");
					} else
						Output.simpleError(player, "You can't invite yourself to the clan.");
				} else
					Output.simpleError(player, "That player is not currently online.");
			} else
				Output.simpleError(player, "Only a clan owner or leader may invite players.");
		} else
			Output.simpleError(player, "You are not currently in a clan.");
	}

	@Command(aliases = { "join" }, desc = "Joins a given clan", usage="<clan>")
	public void clanJoin(@Sender Player player, @Sender PseudoPlayer pPlayer, @Text String clanName) {
		if (pPlayer.getClan() != null)
			if (pPlayer.getClan().isOwner(player)) {
				Output.simpleError(player, "can't join another clan, already a clan owner.");
				return;
			}

		clanName = clanName.trim();

		Clan clanFound = null;
		for (final Clan clan : this.cm.getClans())
			if (clan.getName().equalsIgnoreCase(clanName))
				clanFound = clan;

		if (clanFound != null) {
			if (clanFound.isInvited(player.getUniqueId())) {
				if (clanFound.getMembersAndLeders().size() >= 20) {
					Output.simpleError(player, "The clan is full, max clan size is 20.");
					return;
				}
				if (!clanFound.isOwner(player.getUniqueId()) && !clanFound.isLeader(player.getUniqueId())
						&& !clanFound.isMember(player.getUniqueId())) {
					final Clan currentClan = pPlayer.getClan();
					if (currentClan != null) {
						currentClan.getInvited().remove(player);
						currentClan.getLeaders().remove(player);
						currentClan.getMembers().remove(player);
					}
					clanFound.sendMessage(player.getName() + " has joined the clan.");
					clanFound.getMembers().add(player);
					clanFound.getInvited().remove(player);
					Output.positiveMessage(player, "You have joined the " + clanFound.getName() + " clan.");
				} else {
					Output.simpleError(player, "You are already a member of this clan.");
					clanFound.getInvited().remove(player);
				}
			} else
				Output.simpleError(player, "You have not been invited to that clan.");
		} else
			Output.simpleError(player, "No clan named " + clanName + " found.");
	}

	@Command(aliases = { "kick" }, desc = "Kicks a given player from the clan", usage="<player>")
	public void clanKick(@Sender Player player, @Sender PseudoPlayer pPlayer, OfflinePlayer target) {
		final Clan clan = pPlayer.getClan();
		if (clan != null) {
			if (clan.isOwner(player.getUniqueId()) || clan.isLeader(player.getUniqueId())) {
				if (!target.getUniqueId().equals(player.getUniqueId())) {
					if (clan.inClan(target.getUniqueId())) {
						if (!clan.isLeader(player.getUniqueId()) && clan.isOwner(player.getUniqueId())) {
							clan.getLeaders().remove(target);
							clan.getMembers().remove(target);
							if (target.isOnline())
								Output.simpleError(target.getPlayer(),
										"You have been kicked from " + clan.getName() + ".");
							Output.positiveMessage(player,
									"You have kicked " + target.getName() + " from your clan.");
						} else
							Output.simpleError(player, "Only the clan owner may kick a clan leader.");
					} else
						Output.simpleError(player, "That player is not in your clan.");
				} else
					Output.simpleError(player, "You can't kick yourself from the clan.");
			} else
				Output.simpleError(player, "Only a clan owner or leader may kick players.");
		} else
			Output.simpleError(player, "You are not currently in a clan.");
	}

	@Command(aliases = { "leave" }, desc = "Leaves your current clan")
	public void clanLeave(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		final Clan clan = pPlayer.getClan();
		if (clan != null) {
			if (!clan.isOwner(player.getUniqueId())) {
				clan.getMembers().remove(player);
				clan.sendMessage(player.getName() + " has left the clan.");
				Output.positiveMessage(player, "You have left the " + clan.getName() + " clan.");
			} else {
				Output.simpleError(player, "The clan owner may not leave the clan.");
				Output.simpleError(player, "You may transfer ownership or disband the clan however.");
			}
		} else
			Output.simpleError(player, "You are not currently in a clan.");
	}

	@Command(aliases = { "promote" }, desc = "Promote a given player in your caln", usage="<player>")
	public void clanPromote(@Sender Player player, @Sender PseudoPlayer pPlayer, OfflinePlayer target) {
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		final Clan clan = pseudoPlayer.getClan();
		if (clan != null) {
			if (clan.isOwner(player.getUniqueId())) {
				if (!target.getUniqueId().equals(player.getUniqueId())) {
					if (!clan.isLeader(target.getUniqueId())) {
						if (clan.isMember(target.getUniqueId())) {
							if (target.isOnline()) {
								Output.positiveMessage(target.getPlayer(),
										"You have been promoted to a leader in your clan.");
							}
							clan.getMembers().remove(target);
							clan.getLeaders().add(target);
							Output.positiveMessage(player,
									"You have promoted " + target.getName() + " to leader in your clan.");
						} else
							Output.simpleError(player, "That player is not currently a member of your clan.");
					} else
						Output.simpleError(player, "That player is already a leader of the clan.");
				} else
					Output.simpleError(player, "You can't promote yourself.");
			} else
				Output.simpleError(player, "Only a clan owner may promote clan members.");
		} else
			Output.simpleError(player, "You are not currently in a clan.");
	}

	@Command(aliases = { "transfer" }, desc = "Transfer the clan to a given member of your clan", usage="<player>")
	public void clanTransfer(@Sender Player player, @Sender PseudoPlayer pPlayer, OfflinePlayer target) {
		final Clan clan = pPlayer.getClan();
		if (clan != null) {
			if (clan.isOwner(player)) {
				if (target.getUniqueId().equals(player.getUniqueId())) {
					if (clan.isLeader(target.getUniqueId())
							|| clan.isMember(target.getUniqueId())) {
						// player transferring to is online
						clan.getMembers().remove(target);
						clan.getLeaders().remove(target);
						clan.setOwner(target);
						clan.getLeaders().add(player.getUniqueId());
						Output.positiveMessage(player, "You have transferred ownership of " + clan.getName());
						Output.positiveMessage(player,
								"to " + target.getName() + ", you are now a clan leader.");
						if (target.isOnline())
							Output.positiveMessage(target.getPlayer(),
									"The clan " + clan.getName() + " has been transferred to you,");
					} else
						Output.simpleError(player, "You may only transfer ownership of the clan to a clan member.");
				} else
					Output.simpleError(player, "You may not transfer clan ownership to yourself.");
			} else
				Output.simpleError(player, "Only the clan owner my transfer ownership.");
		} else
			Output.simpleError(player, "You are not currently in a clan.");
	}

	@Command(aliases = { "uninvite" }, desc = "Uninvites a given player", usage="<player>")
	public void clanUnInvite(@Sender Player player, @Sender PseudoPlayer pPlayer, OfflinePlayer target) {
		final Clan clan = pPlayer.getClan();
		if (clan != null) {
			if (clan.isOwner(player.getUniqueId()) || clan.isLeader(player.getUniqueId())) {
				if (target.getUniqueId().equals(player.getUniqueId()))
					if (clan.isInvited(target.getUniqueId())) {
						clan.getMembers().remove(target);
						final Player targetPlayer = target.getPlayer();
						if (targetPlayer != null)
							Output.simpleError(targetPlayer,
									"Your invitation to join " + clan.getName() + " has been revoked.");
						Output.positiveMessage(player,
								"You have uninvited " + target.getName() + " from your clan.");
					} else
						Output.simpleError(player, "He is currently not invited to your clan.");
			} else
				Output.simpleError(player, "Only the owner and leaders may perform this command.");
		} else
			Output.simpleError(player, "You are currently not in a clan.");
	}

	@Command(aliases = { "create" }, desc = "Creates a clan with a given name", usage="<name>")
	public void createClan(@Sender Player player, @Sender PseudoPlayer pPlayer, @Validate(regex="\\w{2, }") @Text String clanName) {
		if (pPlayer.getClan() != null) {
			Output.simpleError(player, "You must leave your current clan to create a new one.");
			return;
		}
		clanName = clanName.trim();

		boolean nameExists = false;
		for (final Clan c : this.cm.getClans())
			if (c.getName().equalsIgnoreCase(clanName)) {
				nameExists = true;
				break;
			}

		if (nameExists) {
			Output.simpleError(player, "A clan with that name already exists.");
			return;
		}
		
		if (!pPlayer.getWallet().contains(Variables.clanCreateCost)) {
			Output.simpleError(player, "Can't afford to create clan, cost: "
					+ Utils.getDecimalFormater().format(Variables.clanCreateCost) + " gold coins.");
			return;
		}
		
		pPlayer.getWallet().subtract(null, Variables.clanCreateCost, "the creation of the clan\""+clanName+"\"");
		final Clan clan = new Clan(clanName, player.getUniqueId());
		this.cm.getClans().add(clan);
		clan.insert();
			Output.positiveMessage(player, "You have created the clan " + clan.getName());
	}

	public Clan findClanByPlayer(Player player) {
		for (final Clan clan : this.cm.getClans())
			if (clan.inClan(player.getUniqueId()))
				return clan;
		return null;
	}
	
	@Command(aliases = { "help", "" }, desc = "Shows help about clan", usage="<page>")
	public void help(CommandSender sender, @Range(min=0) @Optional(value="0") int page) {
		HelpHandler.helpClan(sender, page);
	}

}
