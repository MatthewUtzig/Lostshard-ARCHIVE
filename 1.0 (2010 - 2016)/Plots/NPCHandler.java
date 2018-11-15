package com.lostshard.RPG.Plots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import me.neodork.npclib.entity.HumanNPC;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.Bresenham;
import com.lostshard.RPG.Utils.IntPoint;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class NPCHandler {
	public static HashMap<String, RPGNPC> _NPCHashMap = new HashMap<String, RPGNPC>();
	public static HashSet<String> _NPCChunkHashSet = new HashSet<String>();
	
	private static ArrayList<GuardAttack> _guardAttacks = new ArrayList<GuardAttack>();
	
	//public static NPCManager _npcManager = new NPCManager(Utils.getPlugin());
	public static MyNPCManager _npcManager = new MyNPCManager();
	
	public static ArrayList<HumanNPC> _npcGuards = new ArrayList<HumanNPC>();
	public static ArrayList<HumanNPC> _npcVendors = new ArrayList<HumanNPC>();
	public static ArrayList<HumanNPC> _npcBankers = new ArrayList<HumanNPC>();
	
	public static void handleCommand(Player player, String[] split) {
		if(split.length >= 2) {
			String subCommand = split[1];
			if(subCommand.equalsIgnoreCase("create")) {
				HumanNPC npc = cmdCreateNPC(player, split);
				if(npc != null)
					Database.addNPC(npc, split[2], split[3]);
			}
			else if(subCommand.equalsIgnoreCase("remove")) {
				HumanNPC npc = cmdDespawnNPC(player, split);
				if(npc != null)
					Database.removeNPC(split[2]);
			}
			else if(subCommand.equalsIgnoreCase("move")) {
				HumanNPC npc = cmdMoveNPC(player, split);
				if(npc != null)
					Database.updateNPC(npc, split[2]);
			}
			/*else if(subCommand.equalsIgnoreCase("attack")) {
				cmdAttackNPC(player, split);
			}
			else if(subCommand.equalsIgnoreCase("say")) {
				cmdSayNPC(player, split);
			}
			else if(subCommand.equalsIgnoreCase("sic")) {
				if(split.length == 3) {
					String targetName = split[2];
					Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
					if(targetPlayer != null) {
						BasicHumanNpc guard = findClosestGuard(targetPlayer.getLocation());
						if(guard != null) {
							guardAttack(guard, targetPlayer);
						}
						else Output.simpleError(player, "No guards");
					}
				}
			}*/
		}
	}
	
	/*public static void addPlotNPC(String uniqueId, String name, int itemInHandId, String job, Location location) {
		PlotNPC plotNPC = new PlotNPC(
		Chunk chunk = location.getWorld().getChunkAt(location);
		String chunkString = chunk.toString();
		_npcChunkHashMap.put(chunkString, uniqueId);
		_npcNameToPlotNPC(uniqueId, 
	}*/
	
	public static HumanNPC cmdCreateNPC(Player player, String[] split) {
		if(split.length == 4) {
			String npcName = split[2];
			String job = split[3];
			
			if(_npcManager.getNPC(npcName) == null) {
				Location location = player.getLocation();
				HumanNPC npc = _npcManager.spawnNPC(npcName, location, npcName);

				if(job.equalsIgnoreCase("guard")) {
					HumanEntity he = (HumanEntity)npc.getBukkitEntity();
					he.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET.getId(), 1));
					he.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE.getId(), 1));
					he.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS.getId(), 1));
					he.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS.getId(), 1));
					he.getInventory().setItemInHand(new ItemStack(Material.IRON_SWORD.getId(), 1));
					_npcGuards.add(npc);
				}
				else if(job.equalsIgnoreCase("vendor")) {
					HumanEntity he = (HumanEntity)npc.getBukkitEntity();
					he.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET.getId(), 1));
					he.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE.getId(), 1));
					he.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS.getId(), 1));
					he.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS.getId(), 1));
					_npcVendors.add(npc);
				}
				else if(job.equalsIgnoreCase("banker")) {
					HumanEntity he = (HumanEntity)npc.getBukkitEntity();
					he.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET.getId(), 1));
					he.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE.getId(), 1));
					he.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS.getId(), 1));
					he.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS.getId(), 1));
					he.getInventory().setItemInHand(new ItemStack(Material.GOLD_INGOT.getId(), 1));
					_npcBankers.add(npc);
				}
					
				return npc;
            }
			else Output.simpleError(player, "NPC-ID in use");
		}
		else Output.simpleError(player, "Use \"/npc create (npc-name) (job)\"");
		return null;
	}
	
	public static void deSpawnNPC(String npcId) {
		_npcManager.despawn(npcId);
	}
	
	public static void equipNPC(RPGNPC rpgNpc) {
		if(rpgNpc.getJob().equalsIgnoreCase("guard")) {
			HumanEntity he = (HumanEntity)rpgNpc.getNPCEntity().getBukkitEntity();
			he.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET.getId(), 1));
			he.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE.getId(), 1));
			he.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS.getId(), 1));
			he.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS.getId(), 1));
			he.getInventory().setItemInHand(new ItemStack(Material.IRON_SWORD.getId(), 1));
			_npcGuards.add(rpgNpc.getNPCEntity());
		}
		else if(rpgNpc.getJob().equalsIgnoreCase("vendor")) {
			HumanEntity he = (HumanEntity)rpgNpc.getNPCEntity().getBukkitEntity();
			he.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET.getId(), 1));
			he.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE.getId(), 1));
			he.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS.getId(), 1));
			he.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS.getId(), 1));
			_npcVendors.add(rpgNpc.getNPCEntity());
		}
		else if(rpgNpc.getJob().equalsIgnoreCase("banker")) {
			HumanEntity he = (HumanEntity)rpgNpc.getNPCEntity().getBukkitEntity();
			he.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET.getId(), 1));
			he.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE.getId(), 1));
			he.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS.getId(), 1));
			he.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS.getId(), 1));
			he.getInventory().setItemInHand(new ItemStack(Material.GOLD_INGOT.getId(), 1));
			_npcBankers.add(rpgNpc.getNPCEntity());
		}
	}
	
	public static HumanNPC cmdDespawnNPC(Player player, String[] split) {
		if(split.length >= 3) {
			String npcName = "";
			for(int i=2; i<split.length; i++) {
				npcName += (split[i] + " "); 
			}
			npcName = npcName.trim();
			//String npcName = split[2];
			if(_npcManager.getNPC(npcName) != null) {
				HumanNPC npc = _npcManager.getNPC(npcName);
				_npcManager.despawn(npcName);
				return npc;
			}
			else Output.simpleError(player, "NPC "+npcName+" not found");
		}
		else Output.simpleError(player, "Use \"/npc remove (npc-id)\"");
		return null;
	}
	
	public static HumanNPC cmdMoveNPC(Player player, String[] split) {
		if(split.length == 3) {
			String npcName = split[2];
			if(_npcManager.getNPC(npcName) != null) {
				_npcManager.moveNPC(npcName, player.getLocation());
				return _npcManager.getNPC(npcName);
			}
			else Output.simpleError(player, "NPC "+npcName+" not found");
		}
		else Output.simpleError(player, "Use \"/npc move (npc name)\"");
		return null;
	}
	
	public static void cmdAttackNPC(Player player, String[] split) {
		if(split.length == 4) {
			String npcName = split[2];
			if(_npcManager.getNPC(npcName) != null) {
				String targetName = split[3];
				Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
				if(targetPlayer != null) {
					HumanNPC npc = _npcManager.getNPC(npcName);
					_npcManager.moveNPC(npcName, targetPlayer.getLocation());
					npc.animateArmSwing();
					targetPlayer.damage(10);
				}
				else Output.simpleError(player, "Invalid target player");
			}
			else Output.simpleError(player, "NPC "+npcName+" not found");
		}
		else Output.simpleError(player, "Use \"/npc attack (npc name) (player name)\"");
	}
	
	public static void cmdSayNPC(Player player, String[] split) {
		/*if(split.length > 3) {
			String npcId = split[2];
			BasicHumanNpc npc = RPG._npcManagerLibrary.getNPC(npcId);
			if(npc != null) {				
				String message = "";
				for(int i=3; i<split.length; i++) {
					message += (split[i]+" ");
				}
				message = message.trim();
				sayNPC(npc.getName(), npc.getBukkitEntity().getLocation(), message);
			}
			else Output.simpleError(player, "NPC-ID: " +npcId+" not found");
		}*/
	}
	
	public static void sayNPC(String npcName, Location npcLocation, String message) {
		if(npcName.equalsIgnoreCase("Notch"))
			npcName = ChatColor.BLUE+npcName+ChatColor.WHITE;
		else if(npcName.equalsIgnoreCase("Herobrine"))
			npcName = ChatColor.BLACK+npcName+ChatColor.WHITE;
		else if(npcName.equalsIgnoreCase("Gorp") || npcName.equalsIgnoreCase("Bunt"))
			npcName = ChatColor.RED+npcName+ChatColor.WHITE;
		Output.npcChatLocal(npcName, npcLocation, message);
	}
	
	public static HumanNPC findClosestGuard(Location location) {
		HumanNPC closestGuard = null;
		double closestDist = 9999999;
		for(HumanNPC npc : _npcGuards) {
			double dist = Utils.distance(npc.getBukkitEntity().getLocation(), location);
			if(dist < closestDist) {
				System.out.println("Found a guard close");
				closestDist = dist;
				closestGuard = npc;
			}
		}
		return closestGuard;
	}
	
	public static void guardAttack(HumanNPC guard, Player targetPlayer) {
		GuardAttack ga = new GuardAttack(guard, targetPlayer, guard.getBukkitEntity().getLocation(), 200);
		_guardAttacks.add(ga);
	}
	
	private static int _tickCount = 0;
	
	public static void tick() {
		_tickCount++;
		
		try {
			if(_tickCount % 20 == 0) {
				Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
				for(HumanNPC guard : _npcGuards) {
					//if(!isGuardAttacking(guard)) {
						ArrayList<Player> playersNear = new ArrayList<Player>();
						for(Player p : onlinePlayers) {
							if(Utils.isWithin(p.getLocation(), guard.getBukkitEntity().getLocation(), 10)) {
								playersNear.add(p);
							}
						}
						for(Player p : playersNear) {
							Damageable damag = p;
							if(damag.getHealth() <= 0)
								break;
							PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
							if(!pP._convenient && pP.isCriminal() && !isBeingGuardAttacked(p)) {
								Location pLoc = p.getLocation();
								Location gLoc = guard.getBukkitEntity().getLocation();
								boolean noLOS = false;
								ArrayList<IntPoint> lineOfSightPoints = Bresenham.bresenham3d(pLoc.getBlockX(), pLoc.getBlockY(), pLoc.getBlockZ(), gLoc.getBlockX(), gLoc.getBlockY(), gLoc.getBlockZ());
								for(IntPoint intPoint : lineOfSightPoints) {
									if(!Spell.invisibleBlocks.contains((byte)pLoc.getWorld().getBlockTypeIdAt(intPoint.x, intPoint.y, intPoint.z))) {
										noLOS = true;
										break;
									}
								}
								if(!noLOS) {
									
									//GuardAttack ga = new GuardAttack(guard, p, guard.getBukkitEntity().getLocation(), 200);								
									p.damage(200.0);
									guardChat(guard, 10);
									
									//_guardAttacks.add(ga);
									break;
								}
							}
						}
					//}
				}
			}
			
			int numGuardAttacks = _guardAttacks.size();
			for(int i=numGuardAttacks-1; i>=0; i--) {
				GuardAttack ga = _guardAttacks.get(i);
				ga.tick();
				if(ga.isDead()) {
					Location loc = ga.getOriginalLocation();
					_npcManager.moveNPC(ga.getGuard().getName(), loc);
					_guardAttacks.remove(i);
				}
				else {
					if(ga.getTicksRemaining() == 1) {
						Player targetPlayer = ga.getTargetPlayer();
						PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
						targetPseudoPlayer._guardAttackTicks = 30;
						targetPlayer.damage(1000);
						targetPlayer.getWorld().strikeLightningEffect(targetPlayer.getLocation());
					}
					if(ga.getTicksRemaining() % 5 == 0) {
						_npcManager.moveNPC(ga.getGuard().getName(), ga.getTargetPlayer().getLocation());
					}
					if(ga.getTicksRemaining() % 10 == 0) {
						//ga.getGuard().attackLivingEntity(ga.getTargetPlayer());
						Player targetPlayer = ga.getTargetPlayer();
						HumanNPC npc = ga.getGuard();
						npc.animateArmSwing();
						PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
						if(targetPseudoPlayer == null) {
							ga._isDead = true;
							return;
						}
						targetPseudoPlayer._guardAttackTicks = 30;
						targetPlayer.damage(1000);
						
						if(ga.getTicksRemaining() == 90) {
							guardChat(npc, 10);
						}
					}
				}
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	public static void playerDied(Player player) {
		int numGuardAttacks = _guardAttacks.size();
		for(int i=numGuardAttacks-1; i>=0; i--) {
			GuardAttack ga = _guardAttacks.get(i);
			if(ga.getTargetPlayer().getName().equalsIgnoreCase(player.getName())) {
				Location loc = ga.getOriginalLocation();
				_npcManager.moveNPC(ga.getGuard().getName(), loc);
				_guardAttacks.remove(i);
			}
		}
	}
	
	public static void guardChat(HumanNPC npc, int range) {
		Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(Utils.isWithin(p.getLocation(), npc.getBukkitEntity().getLocation(), 10)) {
				Random generator = new Random();
				int randInt = generator.nextInt(5);
				switch(randInt) {
					case 0:
						p.sendMessage(ChatColor.BLUE+npc.getName()+ChatColor.WHITE+": Wait! I just want to talk!");
						break;
					case 1:
						p.sendMessage(ChatColor.BLUE+npc.getName()+ChatColor.WHITE+": Foul criminal, your days of abusing the legal system are over!");
						break;
					case 2:
						p.sendMessage(ChatColor.BLUE+npc.getName()+ChatColor.WHITE+": Did anyone else just see that?");
						break;
					case 3:
						p.sendMessage(ChatColor.BLUE+npc.getName()+ChatColor.WHITE+": WHY DO I USE LIGHTNING TO KILL PEOPLE!?");
						break;
					case 4:
						p.sendMessage(ChatColor.BLUE+npc.getName()+ChatColor.WHITE+": I'm a guard i'm a guard i'm a guard hello.");
						break;
					default:
						p.sendMessage(ChatColor.BLUE+npc.getName()+ChatColor.WHITE+": Halt criminal, and let me kill you!");
						break;
				}
				//p.sendMessage(ChatColor.BLUE+npc.getName()+ChatColor.WHITE+": "+_criminalMessage);
			}
		}
	}
	
	public static ArrayList<HumanNPC> getGuards() {
		return _npcGuards;
	}
	
	public static boolean isBeingGuardAttacked(Player player) {
		for(GuardAttack ga : _guardAttacks) {
			if(ga.getTargetPlayer().getName().equalsIgnoreCase(player.getName()))
				return true;
		}
		return false;
	}
	
	public static boolean isGuardAttacking(HumanNPC guard) {
		for(GuardAttack ga : _guardAttacks) {
			if(ga.getGuard().getName().equalsIgnoreCase(guard.getName()))
				return true;
		}
		return false;
	}
}
