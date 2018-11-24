package com.lostshard.RPG.Groups.Clans;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class ClanHandler {
	private static final int _MAX_NAME_LENGTH = 20;
	private static final int _CLAN_CREATION_COST = 2000;
	private static ArrayList<Clan> _clans = new ArrayList<Clan>();
	private static HashMap<String, Clan> _clansHashMap = new HashMap<String, Clan>();
	private static Clan _clanThatControlsGorp = null;
	private static Clan _clanThatControlsBunt = null;
	
	public static void loadClans() {
		try {
			_clans = Database.getClans();
			for(Clan clan : _clans) {
				_clansHashMap.put(clan.getName(), clan);
			}
			System.out.println("Loaded "+_clans.size()+" Clans.");
		}
		catch (Exception e) {
			System.out.println("(CRITICAL ERROR) Exception loading clans: "+e.toString());
		}
	}
	
	public static void handleCommand(Player player, String[] split, RPG plugin) {
		if(split.length >= 2) {
			String clanCommand = split[1];
			if(clanCommand.equalsIgnoreCase("create"))
				createClan(player, split);
			else if(clanCommand.equalsIgnoreCase("info"))
				clanInfo(player);
			else if(clanCommand.equalsIgnoreCase("help"))
				clanHelp(player);
			else if(clanCommand.equalsIgnoreCase("invite"))
				clanInvite(player, split);
			else if(clanCommand.equalsIgnoreCase("uninvite"))
				clanUnInvite(player, split);
			else if(clanCommand.equalsIgnoreCase("promote"))
				clanPromote(player, split);
			else if(clanCommand.equalsIgnoreCase("demote"))
				clanDemote(player, split);
			else if(clanCommand.equalsIgnoreCase("kick"))
				clanKick(player, split);
			else if(clanCommand.equalsIgnoreCase("leave"))
				clanLeave(player);
			else if(clanCommand.equalsIgnoreCase("transfer"))
				clanTransfer(player, split);
			else if(clanCommand.equalsIgnoreCase("disband"))
				clanDisband(player);
			else if(clanCommand.equalsIgnoreCase("join"))
				clanJoin(player, split);
			else if(clanCommand.equalsIgnoreCase("cloak"))
				clanCloak(player, split);
		}
		else Output.simpleError(player, "Use \"/clan help\" for commands.");
	}
	
	private static void createClan(Player player, String[] split) {
		if(split.length >= 3) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			if(pseudoPlayer.getClan() == null) {
				int splitNameLength = split.length;
				String clanName = "";
				for(int i=2; i<splitNameLength; i++) {
					clanName += split[i];
					if(i < splitNameLength-1)
						clanName+= " ";
				}
				
				clanName = clanName.trim();
				
				if(!clanName.contains("\"")) {
					boolean nameExists = false;
					for(Clan c : _clans) {
						if(c.getName().equalsIgnoreCase(clanName)) {
							nameExists = true;
							break;
						}
					}
					
					if(!nameExists) {
						if(clanName.length() <= _MAX_NAME_LENGTH) {
							int curMoney = pseudoPlayer.getMoney();
							if(curMoney >= _CLAN_CREATION_COST) {
								pseudoPlayer.setMoney(pseudoPlayer.getMoney()-_CLAN_CREATION_COST);
								Clan clan = new Clan(clanName, player.getName(), new ArrayList<String>(), new ArrayList<String>());
								pseudoPlayer.setClan(clan);
								Database.addClan(clan);
								_clans.add(clan);
								_clansHashMap.put(clan.getName(), clan);
								Database.updatePlayer(player.getName());
								Output.positiveMessage(player, "You have created the clan "+clan.getName());
							}
							else Output.simpleError(player, "Cannot afford to create clan, cost: " + _CLAN_CREATION_COST+" gold coins.");
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
	
	private static void clanInfo(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			Output.outputClanInfo(player, clan);
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}
	
	private static void clanInvite(Player player, String[] split) {
		if(split.length == 3) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getName()) || clan.isLeader(player.getName())) {
					String targetName = split[2];
					Player targetPlayer = player.getServer().getPlayer(targetName);
					if(targetPlayer != null) {
						PseudoPlayer invitedPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
						if(invitedPseudoPlayer._secret) {
							Output.simpleError(player, "That player is not online.");
							return;
						}
						else if(!targetPlayer.getName().equalsIgnoreCase(player.getName())) {
							PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
							if(targetPseudoPlayer.getClan() == null) {
								if(!clan.isOwner(targetPlayer.getName()) && !clan.isLeader(targetPlayer.getName()) && !clan.isMember(targetPlayer.getName())) {
									if(!clan.isInvitedStrict(targetPlayer.getName())) {
										clan.addInvited(targetPlayer.getName());
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
	
	private static void clanUnInvite(Player player, String[] split) {
		if(split.length == 3) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getName()) || clan.isLeader(player.getName())) {
					String targetName = split[2];
					if(!targetName.equalsIgnoreCase(player.getName())) {
						if(clan.isInvited(targetName)) {
							clan.removeInvited(targetName);
							Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
							if(targetPlayer != null) {
								Output.simpleError(targetPlayer, "Your invitation to join "+clan.getName()+" has been revoked.");
								targetName = targetPlayer.getName();
							}
							Output.positiveMessage(player, "You have uninvited "+targetName+" from your clan.");
						}
						else Output.simpleError(player, "That player has not been invited to the clan.");
					}
					else Output.simpleError(player, "You cannot uninvite yourself from the clan.");
				}
				else Output.simpleError(player, "Only a clan owner or leader may uninvite players."); 
			}
			else Output.simpleError(player, "You are not currently in a clan.");
		}
		else Output.simpleError(player, "Use \"/clan invite (player name)\"");
	}
	
	private static void clanPromote(Player player, String[] split) {
		if(split.length == 3) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getName())) {
					String targetName = split[2];
					if(!targetName.equalsIgnoreCase(player.getName())) {
						if(!clan.isLeader(targetName)) {
							if(clan.isMember(targetName)) {
								Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
								if(targetPlayer != null) {
									Output.positiveMessage(targetPlayer, "You have been promoted to a leader in your clan.");
									targetName = targetPlayer.getName();
									clan.removeMember(targetName);
									clan.addLeader(targetName);
									Output.positiveMessage(player, "You have promoted "+targetName+" to leader in your clan.");
									Database.updateClan(clan);
								}
								else {
									PseudoPlayer targetPseudo;
									try {
										targetPseudo = Database.createPseudoPlayer(targetName);
									}
									catch(Exception e) {
										targetPseudo = null;
									}
									if(targetPseudo != null) {
										targetName = targetPseudo.getName();
										clan.removeMember(targetName);
										clan.addLeader(targetName);
										Output.positiveMessage(player, "You have promoted "+targetName+" to leader in your clan.");
										Database.updateClan(clan);
									}
									else Output.simpleError(player, "That player was not found.");
								}
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

	private static void clanDemote(Player player, String[] split) {
		if(split.length == 3) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getName())) {
					String targetName = split[2];
					if(!targetName.equalsIgnoreCase(player.getName())) {
						if(clan.isLeader(targetName)) {
							Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
							if(targetPlayer != null) {
								targetName = targetPlayer.getName();
								clan.removeLeader(targetName);
								clan.addMember(targetName);
								Output.positiveMessage(targetPlayer, "You have been demoted to a normal member in your clan.");
								Output.positiveMessage(player, "You have demoted "+targetName+" to member in your clan.");
								Database.updateClan(clan);
							}
							else {
								PseudoPlayer targetPseudo;
								try {
									targetPseudo = Database.createPseudoPlayer(targetName);
								}
								catch(Exception e) {
									targetPseudo = null;
								}
								if(targetPseudo != null) {
									targetName = targetPseudo.getName();
									clan.removeLeader(targetName);
									clan.addMember(targetName);
									Output.positiveMessage(player, "You have demoted "+targetName+" to member in your clan.");
									Database.updateClan(clan);
								}
								else Output.simpleError(player, "That player was not found.");
							}
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
	
	private static void clanJoin(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer.getClan() != null) {
			if(pseudoPlayer.getClan().isOwner(player.getName())) {
				Output.simpleError(player, "Cannot join another clan, already a clan owner.");
				return;
			}
		}
		if(split.length >= 3) {
			int splitNameLength = split.length;
			String clanName = "";
			for(int i=2; i<splitNameLength; i++) {
				clanName += split[i];
				if(i < splitNameLength-1)
					clanName+= " ";
			}
			
			clanName = clanName.trim();
			
			Clan clanFound = null;
			for(Clan clan : _clans) {
				if(clan.getName().equalsIgnoreCase(clanName)) {
					clanFound = clan;
				}
			}
			
			if(clanFound != null) {
				if(clanFound.isInvited(player.getName())) {
					if(!clanFound.isOwner(player.getName()) && !clanFound.isLeader(player.getName()) && !clanFound.isMember(player.getName())) {
						Output.clanMessage(clanFound, player.getName()+" has joined the clan.");
						clanFound.addMember(player.getName());
						clanFound.removeInvited(player.getName());
						Clan currentClan = pseudoPlayer.getClan();
						pseudoPlayer.setClan(clanFound);
						if(currentClan != null) {
							currentClan.removeInvited(player.getName());
							currentClan.removeLeader(player.getName());
							currentClan.removeMember(player.getName());
						}
						Output.positiveMessage(player, "You have joined the "+clanFound.getName()+" clan.");
						Database.updateClan(clanFound);
						if(currentClan != null)
							Database.updateClan(currentClan);
						Database.updatePlayer(player.getName());
						Utils.setPlayerTitle(player);
					}
					else {
						Output.simpleError(player, "You are already a member of this clan.");
						clanFound.removeInvited(player.getName());
					}
				}
				else Output.simpleError(player, "You have not been invited to that clan.");
			}
			else Output.simpleError(player, "No clan named "+clanName+" found.");
		}
		else Output.simpleError(player, "Use \"/clan join (clan name)\"");
	}
	
	private static void clanKick(Player player, String[] split) {
		if(split.length == 3) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			Clan clan = pseudoPlayer.getClan();
			if(clan != null) {
				if(clan.isOwner(player.getName()) || clan.isLeader(player.getName())) {
					String targetName = split[2];
					if(!targetName.equalsIgnoreCase(player.getName())) {
						Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
						if(targetPlayer != null) {
							if(clan.isInClan(targetPlayer.getName())) {
								if(clan.isOwner(player.getName())) {
									clan.removeLeader(targetPlayer.getName());
									clan.removeMember(targetPlayer.getName());
									PseudoPlayer targetPseudo = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
									targetPseudo.setClan(null);
									Output.simpleError(targetPlayer, "You have been kicked from "+clan.getName()+".");
									Output.positiveMessage(player, "You have kicked "+targetPlayer.getName()+ " from your clan.");
									Database.updateClan(clan);
									Database.updatePlayer(targetPlayer.getName());
									Utils.setPlayerTitle(targetPlayer);
								}
								else if(clan.isLeader(player.getName())) {
									if(!clan.isLeader(targetPlayer.getName())) {
										if(!clan.isOwner(targetPlayer.getName())) {
											clan.removeMember(targetPlayer.getName());
											PseudoPlayer targetPseudo = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
											targetPseudo.setClan(null);
											Output.simpleError(targetPlayer, "You have been kicked from "+clan.getName()+".");
											Output.positiveMessage(player, "You have kicked "+targetPlayer.getName()+ " from your clan.");
											Database.updateClan(clan);
											Database.updatePlayer(targetPlayer.getName());
											Utils.setPlayerTitle(targetPlayer);
										}
										else Output.simpleError(player, "You may not kick the leader from the clan.");
									}
									else Output.simpleError(player, "Only the clan owner may kick a clan leader.");
								}
							}
							else Output.simpleError(player, "That player is not in your clan.");
						}
						else {
							if(clan.isInClan(targetName)) {
								// player not online
								if(clan.isOwner(player.getName())) {
									clan.removeLeader(targetName);
									clan.removeMember(targetName);
									Output.positiveMessage(player, "You have kicked "+targetName+ " from your clan.");
									Database.updateClan(clan);
								}
								else if(clan.isLeader(player.getName())) {
									if(!clan.isLeader(targetName)) {
										if(!clan.isOwner(targetName)) {
											clan.removeMember(targetName);
											Output.positiveMessage(player, "You have kicked "+targetName+ " from your clan.");
											Database.updateClan(clan);
										}
										else Output.simpleError(player, "You may not kick the leader from the clan.");
									}
									else Output.simpleError(player, "Only the clan owner may kick a clan leader.");
								}
							}
							else Output.simpleError(player, "That player is not in your clan.");
						}
					}
					else Output.simpleError(player, "You cannot kick yourself from the clan.");
				}
				else Output.simpleError(player, "Only a clan owner or leader may kick players."); 
			}
			else Output.simpleError(player, "You are not currently in a clan.");
			
		}
		else Output.simpleError(player, "Use \"/clan invite (player name)\"");
	}
	
	private static void clanLeave(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			if(!clan.isOwner(player.getName())) {
				clan.removeMember(player.getName());
				clan.removeLeader(player.getName());
				pseudoPlayer.setClan(null);
				Database.updateClan(clan);
				Database.updatePlayer(player.getName());
				Output.clanMessage(clan, player.getName()+" has left the clan.");
				Output.positiveMessage(player, "You have left the "+clan.getName()+" clan.");
				Utils.setPlayerTitle(player);
			}
			else {
				Output.simpleError(player, "The clan owner may not leave the clan.");
				Output.simpleError(player, "You may transfer ownership or disband the clan however.");
			}
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}
	
	public static Clan findClanByPlayer(Player player) {
		for(Clan clan : _clans) {
			if(clan.isMember(player.getName()))
				return clan;
		}
		return null;
	}
	
	private static void clanTransfer(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			if(clan.isOwner(player.getName())) {
				String targetName = split[2];
				if(!targetName.equalsIgnoreCase(player.getName())) {
					if(clan.isLeader(targetName) || clan.isMember(targetName)) {
						Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
						if(targetPlayer != null) {
							// player transferring to is online
							clan.removeMember(targetPlayer.getName());
							clan.removeLeader(targetPlayer.getName());
							clan.setOwner(targetPlayer.getName());
							clan.addLeader(player.getName());
							Output.positiveMessage(player, "You have transferred ownership of "+clan.getName());
							Output.positiveMessage(player, "to "+targetPlayer.getName()+", you are now a clan leader.");
							Output.positiveMessage(targetPlayer, "The clan "+clan.getName()+" has been transferred to you,");
							Output.positiveMessage(targetPlayer, "you are now the owner.");
							Database.updateClan(clan);
						}
						else {
							PseudoPlayer targetPseudo;
							try {
								targetPseudo = Database.createPseudoPlayer(targetName);
							}
							catch(Exception e) {
								targetPseudo = null;
							}
							if(targetPseudo != null) {
								targetName = targetPseudo.getName();
								clan.removeMember(targetName);
								clan.removeLeader(targetName);
								clan.setOwner(targetName);
								clan.addLeader(player.getName()); 
								targetPseudo.setClan(clan);
								Output.positiveMessage(player, "You have transferred ownership of "+clan.getName());
								Output.positiveMessage(player, "to "+targetPseudo.getName()+", you are now a clan leader.");
								Database.updateClan(clan);
								Database.updatePlayerByPseudoPlayer(targetPseudo);
							}
							else Output.simpleError(player, "That player was not found.");
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

	private static void clanDisband(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			if(clan.isOwner(player.getName())) {
				ArrayList<Player> onlineMembers = clan.getOnlineMembers();
				for(Player p : onlineMembers) {
					// inform them the clan is gone
					Output.simpleError(p, "Your clan has been disbanded.");
					// get pseudo
					PseudoPlayer pseudo = PseudoPlayerHandler.getPseudoPlayer(p.getName());
					// wipe their clan
					pseudo.setClan(null);
					// update the database
					Database.updatePlayer(p.getName());
				}
				Database.removeClan(clan);
				removeClan(clan);
			}
			else Output.simpleError(player, "Only the clan owner may disband the clan.");
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}
	
	private static void clanCloak(Player player, String[] split) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Clan clan = pseudoPlayer.getClan();
		if(clan != null) {
			if(clan.isOwner(player.getName()) || clan.isLeader(player.getName())) {
				if(split.length > 2) {
					String subCommand = split[2];
					if(subCommand.equalsIgnoreCase("set")) {
						if(split.length >= 4) {
							String cloakURL = Utils.implodeSplit(split, 3);
							try {
								////SpoutManager.getAppearanceManager().setGlobalCloak(player, cloakURL);
							}
							catch(Exception e) {
								Output.simpleError(player, "Invalid URL, make sure it is a PNG.");
								return;
							}
							clan.setCloakURL(cloakURL);
							Database.updateClan(clan);
							
							////spout
							/*SpoutPlayer[] onlinePlayers = SpoutManager.getPlayerManager().getOnlinePlayers();
							for(SpoutPlayer sP : onlinePlayers) {
								PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(sP.getName());
								try {
									if(pP.getClan().equals(clan)) {
										if(sP.isSpoutCraftEnabled())
											SpoutManager.getAppearanceManager().setGlobalCloak(sP,  cloakURL);
									}
								}
								catch(Exception e) {System.out.println("FFF"); e.printStackTrace();}
							}
							
							Output.positiveMessage(player, "You have set your clan's cloak to "+cloakURL+".");*/
						}
						else {
							Output.simpleError(player, "Use /clan cloak set (png url)");
						}
					}
					else if(subCommand.equalsIgnoreCase("clear")) {
						clan.setCloakURL("");
						Database.updateClan(clan);
						
						////spout
						/*SpoutPlayer[] onlinePlayers = SpoutManager.getPlayerManager().getOnlinePlayers();
						for(SpoutPlayer sP : onlinePlayers) {
							PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(sP.getName());
							if(pP.getClan().equals(clan)) {
								SpoutManager.getAppearanceManager().resetGlobalCloak(sP);
							}
						}*/
						
						Output.positiveMessage(player, "You have cleared your clan's cloak.");
					}
				}
				else Output.simpleError(player,  "Use /clan cloak set (png url) or /clan cloak clear");
			}
			else Output.simpleError(player, "Only the clan owner or leaders may manage cloaks.");
		}
		else Output.simpleError(player, "You are not currently in a clan.");
	}
	
	private static void removeClan(Clan clan) {
		int numClans = _clans.size();
		for(int i=numClans-1; i>=0; i--) {
			if(_clans.get(i).getName().equalsIgnoreCase(clan.getName())) {
				_clans.remove(i);
				_clansHashMap.remove(clan.getName());
			}
		}
	}
	
	public static Clan findClanByHashmap(String clanName) {
		if(_clansHashMap.containsKey(clanName))
			return _clansHashMap.get(clanName);
		return null;
	}
	
	public static void SetGorpControlClan(Clan clan) {
		if(_clanThatControlsGorp != null) {
			_clanThatControlsGorp.SetMaxMana(100);
		}
		
		_clanThatControlsGorp = clan;
		
		if(_clanThatControlsGorp != null) {
			_clanThatControlsGorp.SetMaxMana(115);
		}
	}
	
	public static void SetBuntControlClan(Clan clan) {
		if(_clanThatControlsBunt != null) {
			_clanThatControlsBunt.SetMaxStamina(100);
		}
		
		_clanThatControlsBunt = clan;
		
		if(_clanThatControlsBunt != null) {
			_clanThatControlsBunt.SetMaxStamina(115);
		}
	}
	
	public static Clan GetGorpControlClan() {
		return _clanThatControlsGorp;
	}
	
	public static Clan GetBuntControlClan() {
		return _clanThatControlsBunt;
	}
	
	public static void clanHelp(Player player) {
		player.sendMessage(ChatColor.GOLD+"-Clan Commands-");
		player.sendMessage(ChatColor.YELLOW+"/clan create (clan name) [Costs 2000]");
		player.sendMessage(ChatColor.YELLOW+"/clan info");
		player.sendMessage(ChatColor.YELLOW+"/clan invite/uninvite (player name)");
		player.sendMessage(ChatColor.YELLOW+"/clan promote/demote (player name)");
		player.sendMessage(ChatColor.YELLOW+"/clan kick (player name)");
		player.sendMessage(ChatColor.YELLOW+"/clan transfer (player name)");
		player.sendMessage(ChatColor.YELLOW+"/clan leave");
		player.sendMessage(ChatColor.YELLOW+"/clan disband");
	}
}
