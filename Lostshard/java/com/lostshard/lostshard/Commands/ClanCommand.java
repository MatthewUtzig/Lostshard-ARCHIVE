package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.HelpHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.TabUtils;

public class ClanCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	ClanManager cm = ClanManager.getManager();
	
	public ClanCommand(Lostshard plugin) {
		plugin.getCommand("clan").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("clan")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			if(args.length >= 1) {
				String clanCommand = args[0];
				if(clanCommand.equalsIgnoreCase("create"))
					createClan(player, args);
				else if(clanCommand.equalsIgnoreCase("info"))
					clanInfo(player);
				else if(clanCommand.equalsIgnoreCase("help"))
					HelpHandler.helpClan(player, new String[0]);
				else if(clanCommand.equalsIgnoreCase("invite"))
					clanInvite(player, args);
				else if(clanCommand.equalsIgnoreCase("uninvite"))
					clanUnInvite(player, args);
				else if(clanCommand.equalsIgnoreCase("promote"))
					clanPromote(player, args);
				else if(clanCommand.equalsIgnoreCase("demote"))
					clanDemote(player, args);
				else if(clanCommand.equalsIgnoreCase("kick"))
					clanKick(player, args);
				else if(clanCommand.equalsIgnoreCase("leave"))
					clanLeave(player);
				else if(clanCommand.equalsIgnoreCase("transfer"))
					clanTransfer(player, args);
				else if(clanCommand.equalsIgnoreCase("disband"))
					clanDisband(player);
				else if(clanCommand.equalsIgnoreCase("join"))
					clanJoin(player, args);
				else
					Output.simpleError(player, "Use \"/clan help\" for commands.");
			}else
				Output.simpleError(player, "Use \"/clan help\" for commands.");
			return true;
		}else
		return false;
	}
	
	private void createClan(Player player, String[] split) {
		if(split.length >= 2) {
			PseudoPlayer pseudoPlayer = pm.getPlayer(player);
			if(pseudoPlayer.getClan() == null) {
				int splitNameLength = split.length;
				String clanName = "";
				for(int i=1; i<splitNameLength; i++) {
					clanName += split[i];
					if(i < splitNameLength-1)
						clanName+= " ";
				}
				
				clanName = clanName.trim();
				
				if(!clanName.contains("\"")) {
					boolean nameExists = false;
					for(Clan c : cm.getClans()) {
						if(c.getName().equalsIgnoreCase(clanName)) {
							nameExists = true;
							break;
						}
					}
					
					if(!nameExists) {
						if(clanName.length() <= Variables.clanMaxNameLeangh) {
							int curMoney = pseudoPlayer.getMoney();
							if(curMoney >= Variables.clanCreateCost) {
								pseudoPlayer.setMoney(pseudoPlayer.getMoney()-Variables.clanCreateCost);
								Clan clan = new Clan(clanName, player.getUniqueId());
								cm.getClans().add(clan);
								Database.insertClan(clan);
								Output.positiveMessage(player, "You have created the clan "+clan.getName());
							}
							else Output.simpleError(player, "Cannot afford to create clan, cost: " + Variables.clanCreateCost+" gold coins.");
						}
						else Output.simpleError(player, "Clan name too long, 20 characters max.");
					}
					else Output.simpleError(player, "A clan with that name already exists.");
				}
				else Output.simpleError(player, "Cannot use \" in clan name.");
			}
			else Output.simpleError(player, "You must leave your current clan to create a new one.");
		}
		else Output.simpleError(player, "Use \"/clan create (clan name)\"");
	}
	
	private void clanInfo(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			Output.outputClanInfo(player, clan);
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}
	
	private void clanInvite(Player player, String[] split) {
		if(split.length == 2) {
			PseudoPlayer pseudoPlayer = pm.getPlayer(player);
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getUniqueId()) || clan.isLeader(player.getUniqueId())) {
					String targetName = split[1];
					Player targetPlayer = player.getServer().getPlayer(targetName);
					if(targetPlayer != null) {
						if(!targetPlayer.getName().equalsIgnoreCase(player.getName())) {
							PseudoPlayer targetPseudoPlayer = pm.getPlayer(targetPlayer);
							if(targetPseudoPlayer.getClan() == null) {
								if(!clan.isOwner(targetPlayer) && !clan.isLeader(targetPlayer.getUniqueId()) && !clan.isMember(targetPlayer.getUniqueId())) {
									if(!clan.isInvited(targetPlayer.getUniqueId())) {
										clan.addInvited(targetPlayer.getUniqueId());
										Output.positiveMessage(player, "You have invited "+targetPlayer.getName()+" to your clan.");
										Output.positiveMessage(targetPlayer, "You have been invited to the "+clan.getName()+" clan.");
										Output.positiveMessage(targetPlayer, "\"/clan join "+clan.getName()+"\" to join.");
									}
									else Output.simpleError(player, targetPlayer.getName()+" has already been invited to this clan.");
								}
								else Output.simpleError(player, "That player is already a member of this clan.");
							}
							else Output.simpleError(player, "That player is already in a clan.");
						}
						else Output.simpleError(player, "You cannot invite yourself to the clan.");
					}
					else Output.simpleError(player, "That player is not currently online.");
				}
				else Output.simpleError(player, "Only a clan owner or leader may invite players."); 
			}
			else Output.simpleError(player, "You are not currently in a clan.");
			
		}
		else Output.simpleError(player, "Use \"/clan invite (player name)\"");
	}
	
	@SuppressWarnings("deprecation")
	private void clanUnInvite(Player player, String[] split) {
		if(split.length == 2) {
			PseudoPlayer pseudoPlayer = pm.getPlayer(player);
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getUniqueId()) || clan.isLeader(player.getUniqueId())) {
					String targetName = split[1];
					OfflinePlayer targetOfflinePlayer = Bukkit.getOfflinePlayer(targetName);
					if(targetOfflinePlayer.getName() != player.getName()) {
						if(clan.isInvited(targetOfflinePlayer.getUniqueId())) {
							clan.removeInvited(targetOfflinePlayer.getUniqueId());
							Player targetPlayer = targetOfflinePlayer.getPlayer();
							if(targetPlayer != null) {
								Output.simpleError(targetPlayer, "Your invitation to join "+clan.getName()+" has been revoked.");
							}
							Output.positiveMessage(player, "You have uninvited "+targetOfflinePlayer.getName()+" from your clan.");
						}else{
							Output.simpleError(player, "He is currently not invited to your clan.");
						}
					}
				}else{
					Output.simpleError(player, "Only the owner and leaders may perform this command.");
				}
			}else{
				Output.simpleError(player, "You are currently not in a clan.");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void clanPromote(Player player, String[] split) {
		if(split.length == 2) {
			PseudoPlayer pseudoPlayer = pm.getPlayer(player);
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getUniqueId())) {
					String targetName = split[1];
					OfflinePlayer targetOfflinePlayer = Bukkit.getOfflinePlayer(targetName);
					targetName = targetOfflinePlayer.getName();
					if(!targetName.equalsIgnoreCase(player.getName())) {
						if(!clan.isLeader(targetOfflinePlayer.getUniqueId())) {
							if(clan.isMember(targetOfflinePlayer.getUniqueId())) {
								Player targetPlayer = targetOfflinePlayer.getPlayer();
								if(targetPlayer != null) {
									Output.positiveMessage(targetPlayer, "You have been promoted to a leader in your clan.");
									targetName = targetPlayer.getName();
								}
								clan.removeMember(targetOfflinePlayer.getUniqueId());
								clan.promoteMember(targetOfflinePlayer.getUniqueId());
								Output.positiveMessage(player, "You have promoted "+targetName+" to leader in your clan.");
							}
							else Output.simpleError(player, "That player is not currently a member of your clan.");
						}
						else Output.simpleError(player, "That player is already a leader of the clan.");
					}
					else Output.simpleError(player, "You cannot promote yourself.");
				}
				else Output.simpleError(player, "Only a clan owner may promote clan members."); 
			}
			else Output.simpleError(player, "You are not currently in a clan.");
		}
		else Output.simpleError(player, "Use \"/clan invite (player name)\"");
	}

	@SuppressWarnings("deprecation")
	private void clanDemote(Player player, String[] split) {
		if(split.length == 2) {
			PseudoPlayer pseudoPlayer = pm.getPlayer(player.getUniqueId());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getUniqueId())) {
					String targetName = split[1];
					OfflinePlayer targetOfflinePlayer = Bukkit.getOfflinePlayer(targetName);
					targetName = targetOfflinePlayer.getName();
					if(!targetName.equalsIgnoreCase(player.getName())) {
						if(clan.isLeader(targetOfflinePlayer.getUniqueId())) {
							Player targetPlayer = targetOfflinePlayer.getPlayer();
							if(targetPlayer != null)
								targetName = targetPlayer.getName();
							else
								targetName = targetOfflinePlayer.getName();
							clan.demoteLeader(targetOfflinePlayer.getUniqueId());
							clan.addMember(targetOfflinePlayer.getUniqueId());
							if(targetPlayer != null)
								Output.positiveMessage(targetPlayer, "You have been demoted to a normal member in your clan.");
							Output.positiveMessage(player, "You have demoted "+targetName+" to a normal member in your clan.");
						}
						else Output.simpleError(player, "Only a leader may be demoted, you may kick any member.");
					}
					else Output.simpleError(player, "You cannot demote yourself.");
				}
				else Output.simpleError(player, "Only a clan owner may demote clan members."); 
			}
			else Output.simpleError(player, "You are not currently in a clan.");
		}
		else Output.simpleError(player, "Use \"/clan invite (player name)\"");
	}
	
	private void clanJoin(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player.getUniqueId());
		if(pseudoPlayer.getClan() != null) {
			if(pseudoPlayer.getClan().isOwner(player)) {
				Output.simpleError(player, "Cannot join another clan, already a clan owner.");
				return;
			}
		}
		if(split.length >= 2) {
			int splitNameLength = split.length;
			String clanName = "";
			for(int i=1; i<splitNameLength; i++) {
				clanName += split[i];
				if(i < splitNameLength-1)
					clanName+= " ";
			}
			
			clanName = clanName.trim();
			
			Clan clanFound = null;
			for(Clan clan : cm.getClans()) {
				if(clan.getName().equalsIgnoreCase(clanName)) {
					clanFound = clan;
				}
			}
			
			if(clanFound != null) {
				if(clanFound.isInvited(player.getUniqueId())) {
					if(clanFound.getMembersAndLeders().size() >= 20) {
						Output.simpleError(player, "The clan is full, max clan size is 20.");
						return;
					}
					if(!clanFound.isOwner(player.getUniqueId()) && !clanFound.isLeader(player.getUniqueId()) && !clanFound.isMember(player.getUniqueId())) {
						Clan currentClan = pseudoPlayer.getClan();
						if(currentClan != null) {
							currentClan.removeInvited(player.getUniqueId());
							currentClan.demoteLeader(player.getUniqueId());
							currentClan.removeMember(player.getUniqueId());
						}
						clanFound.sendMessage(player.getName()+" has joined the clan.");
						clanFound.addMember(player.getUniqueId());
						clanFound.removeInvited(player.getUniqueId());
						Output.positiveMessage(player, "You have joined the "+clanFound.getName()+" clan.");
					}
					else {
						Output.simpleError(player, "You are already a member of this clan.");
						clanFound.removeInvited(player.getUniqueId());
					}
				}
				else Output.simpleError(player, "You have not been invited to that clan.");
			}
			else Output.simpleError(player, "No clan named "+clanName+" found.");
		}
		else Output.simpleError(player, "Use \"/clan join (clan name)\"");
	}
	
	private void clanKick(Player player, String[] split) {
		if(split.length == 2) {
			PseudoPlayer pseudoPlayer = pm.getPlayer(player.getUniqueId());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getUniqueId()) || clan.isLeader(player.getUniqueId())) {
					String targetName = split[1];
					@SuppressWarnings("deprecation")
					OfflinePlayer targetOfflinePlayer = Bukkit.getOfflinePlayer(targetName);
					targetName = targetOfflinePlayer.getName();
					if(!targetName.equalsIgnoreCase(player.getName())) {
						Player targetPlayer = targetOfflinePlayer.getPlayer();
						if(clan.isInClan(targetOfflinePlayer.getUniqueId())) {
							if(!clan.isLeader(player.getUniqueId()) && clan.isOwner(player.getUniqueId())) {
								clan.demoteLeader(targetOfflinePlayer.getUniqueId());
								clan.removeMember(targetOfflinePlayer.getUniqueId());
								if(targetPlayer != null)
									Output.simpleError(targetPlayer, "You have been kicked from "+clan.getName()+".");
								Output.positiveMessage(player, "You have kicked "+targetOfflinePlayer.getName()+ " from your clan.");
							}
							else Output.simpleError(player, "Only the clan owner may kick a clan leader.");
						}
						else Output.simpleError(player, "That player is not in your clan.");
					}
					else Output.simpleError(player, "You cannot kick yourself from the clan.");
				}
				else Output.simpleError(player, "Only a clan owner or leader may kick players."); 
			}
			else Output.simpleError(player, "You are not currently in a clan.");
		}
		else Output.simpleError(player, "Use \"/clan invite (player name)\"");
	}
	
	private void clanLeave(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player.getUniqueId());
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			if(!clan.isOwner(player.getUniqueId())) {
				clan.removeMember(player.getUniqueId());
				clan.demoteLeader(player.getUniqueId());
				clan.sendMessage(player.getName()+" has left the clan.");
				Output.positiveMessage(player, "You have left the "+clan.getName()+" clan.");
			}
			else {
				Output.simpleError(player, "The clan owner may not leave the clan.");
				Output.simpleError(player, "You may transfer ownership or disband the clan however.");
			}
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}
	
	public Clan findClanByPlayer(Player player) {
		for(Clan clan : cm.getClans()) {
			if(clan.isInClan(player.getUniqueId()))
				return clan;
		}
		return null;
	}
	
	private void clanTransfer(Player player, String[] split) {
		if(split.length < 2) {
			Output.simpleError(player, "/clan transfer (player)");
			return;
		}
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			if(clan.isOwner(player)) {
				String targetName = split[1];
				@SuppressWarnings("deprecation")
				OfflinePlayer targetOfflinePlayer = Bukkit.getOfflinePlayer(targetName);
				targetName = targetOfflinePlayer.getName();
				if(!targetName.equalsIgnoreCase(player.getName())) {
					if(clan.isLeader(targetOfflinePlayer.getUniqueId()) || clan.isMember(targetOfflinePlayer.getUniqueId())) {
						Player targetPlayer = targetOfflinePlayer.getPlayer();
						// player transferring to is online
						clan.removeMember(targetOfflinePlayer.getUniqueId());
						clan.demoteLeader(targetOfflinePlayer.getUniqueId());
						clan.setOwner(targetOfflinePlayer.getUniqueId());
						clan.addMember(player.getUniqueId());
						clan.promoteMember(player.getUniqueId());
						Output.positiveMessage(player, "You have transferred ownership of "+clan.getName());
						Output.positiveMessage(player, "to "+targetOfflinePlayer.getName()+", you are now a clan leader.");
						if(player != null) {
							Output.positiveMessage(targetPlayer, "The clan "+clan.getName()+" has been transferred to you,");
						}
					}
					else Output.simpleError(player, "You may only transfer ownership of the clan to a clan member.");
				}
				else Output.simpleError(player, "You may not transfer clan ownership to yourself.");
			}
			else Output.simpleError(player, "Only the clan owner my transfer ownership.");
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}

	private void clanDisband(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			if(clan.isOwner(player)) {
				List<Player> onlineMembers = clan.getOnlineMembers();
				for(Player p : onlineMembers) {
					// inform them the clan is gone
					Output.simpleError(p, "Your clan has been disbanded.");
				}
				Database.deleteClan(clan);
				cm.getClans().remove(clan);
			}
			else Output.simpleError(player, "Only the clan owner may disband the clan.");
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("clan") && args.length == 1) {
			return TabUtils.StringTab(args, new String[] {"create","info","help","invite","uninvite","promote","demote","kick","leave","transfer","disband","join"});
		}
		return null;
	}
	
}
