package com.lostshard.RPG.Listeners;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

import com.lostshard.RPG.MonsterHandler;
import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.PseudoWolf;
import com.lostshard.RPG.PseudoWolfHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.RecentAttacker;
import com.lostshard.RPG.RoboExplosion;
import com.lostshard.RPG.Events.GhastAttackEvent;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Parties.Party;
import com.lostshard.RPG.Plots.NPCHandler;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Plots.PlotNPC;
import com.lostshard.RPG.Plots.Store;
import com.lostshard.RPG.Skills.Archery;
import com.lostshard.RPG.Skills.Brawling;
import com.lostshard.RPG.Skills.Camp;
import com.lostshard.RPG.Skills.Lumberjacking;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Skills.Survivalism;
import com.lostshard.RPG.Skills.Swordsmanship;
import com.lostshard.RPG.Skills.Taming;
import com.lostshard.RPG.Spells.SPL_ClearSky;
import com.lostshard.RPG.Spells.SPL_Day;
import com.lostshard.RPG.Spells.SPL_MoonJump;
import com.lostshard.RPG.Spells.SPL_PermanentGateTravel;
import com.lostshard.RPG.Spells.SPL_SummonMonster;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class CopyOfRPGEntityListener extends EntityListener{
	private final RPG plugin;
	private static final int _criminalTicksPerCrime = 10*300;
	public static final int _RECENT_ATTACKER_TIME = 10*30;
	public HashMap<Entity, Entity> _lastAttackers;
	public HashSet<Entity> _recentDeaths;
	public int _recentDeathsTicks = 0;
	
	public CopyOfRPGEntityListener(RPG instance) {
		plugin = instance;
		_lastAttackers = new HashMap<Entity,Entity>();
		_recentDeaths = new HashSet<Entity>();
	}
	
	@Override
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof Wolf) {
			Wolf wolf = (Wolf)entity;
			if(wolf.isTamed()) {
				System.out.println("###TamedWolf:"+wolf.getEntityId());
			}
		}
		
		
		if(entity instanceof Squid) {
			if(Math.random() > .25) {
				event.setCancelled(true);
				return;
			}
		}
		if(entity instanceof Monster || entity instanceof Slime) {
			Location loc = event.getLocation();
			Plot plot = PlotHandler.findPlotAt(loc);
			if(plot != null) {
				if(!plot.isDungeon() && plot.isProtected()) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	public void onEntityDamageByProjectile(EntityDamageByProjectileEvent event) {//EntityDamageByEntityEvent event) {
		if(RPG._debugV)
    		System.out.println("Entity Damage By Projectile");

		Entity entity = event.getEntity();
		Entity entityDamager = event.getDamager(); // returns the actual thing that hurt the player
		if(entityDamager instanceof Player) {
			Player playerDamager = (Player)entityDamager;
		/*if(entityDamager instanceof Arrow) {
			Arrow arrow = (Arrow)entityDamager;
			LivingEntity shooterLivingEntity = arrow.getShooter();
			Player playerDamager;
			if(shooterLivingEntity instanceof Player) {
				playerDamager = (Player)shooterLivingEntity;
			}
			else
				return;*/
			
			if(playerDamager instanceof Player) {
				_lastAttackers.put(entity, playerDamager);
			}
			
			if(playerDamager instanceof Player) {
				PseudoPlayer pseudoPlayerDamager = PseudoPlayerHandler.getPseudoPlayer(playerDamager.getName());
				if(entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity)entity;
					ArrayList<PseudoWolf> tamedWolves = pseudoPlayerDamager.getTamedWolves();
					for(PseudoWolf pW : tamedWolves) {
						Wolf wolf = Taming.pseudoWolfToWolf(pW);
						if(wolf != null) {
							if(!livingEntity.equals(wolf) && !livingEntity.equals(wolf.getOwner())) {
								wolf.setTarget(livingEntity);
							}
						}
					}
				}	
				
				if(entity instanceof Player) {
					Player playerDefender = (Player)entity;
					if(playerDamager.equals(playerDefender))
						return;
					
					if(canPlayerDamagePlayer(playerDamager, playerDefender)) {
						criminalAction(playerDefender, playerDamager);
					}
					else {
						event.setCancelled(true);
						return;
					}
					
					//attack notify
					PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(playerDefender.getName());
					
					if(pseudoPlayerDefender._lastAttackTicks <= 0) {
						pseudoPlayerDefender._lastAttackerName = playerDamager.getName();
						pseudoPlayerDefender._lastAttackTicks = 100;
						playerDefender.sendMessage(ChatColor.GRAY+playerDamager.getName()+" damaged you.");
					}
					// end attack notify
				}
				
				if(!RPG._damage) {
					RPG._damage = true;
					
					int skillVal = pseudoPlayerDamager.getSkill("archery");
					int additionalDamage = 0;
					if(skillVal >= 1000)
						additionalDamage = 4;
					else if(skillVal >= 750)
						additionalDamage = 3;
					else if(skillVal >= 500)
						additionalDamage = 2;
					else if(skillVal >= 250)
						additionalDamage = 1;
					
					double chanceOfEffect = (double)skillVal / 1000;
					double knockbackChance = chanceOfEffect * .2;
					
					if(knockbackChance > Math.random()) {
						//knockback
						Location aLoc = playerDamager.getLocation();
						Location dLoc = entity.getLocation();
						Vector v = new Vector();
						v.setX(dLoc.getX() - aLoc.getX());
						v.setY(dLoc.getY() - aLoc.getY());
						v.setZ(dLoc.getZ() - aLoc.getZ());
						v = v.normalize();
						v = v.multiply(1.5);
						entity.setVelocity(v);
						if(entity instanceof Player) {
							Player defenderPlayer = (Player)entity;
							defenderPlayer.sendMessage(ChatColor.GREEN+"You have been knocked back!");
							playerDamager.sendMessage(ChatColor.GREEN+defenderPlayer.getName()+" has been knocked back!");
						}
					}
					
					int damage = event.getDamage();
					
					if(entity instanceof Player && playerDamager instanceof Player) {
						
						if(entity instanceof Player) {
							Player defenderPlayer = (Player)entity;
							PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
							if(defenderPseudoPlayer._stoneSkinTicks > 0)
								damage/=2;
						}
						
						damage/=2;
						if(damage <= 0)
							damage = 1;
					}
					event.setDamage(damage+additionalDamage);
					
					if(playerDamager.getItemInHand().getType().equals(Material.BOW))
						Archery.possibleSkillGain(playerDamager, pseudoPlayerDamager, entity);
				}
			}
		}
	}
	
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(RPG._debugV)
    		System.out.println("Entity Damage By Entity");
		
		if(event.getDamager() == null)
			return;
		
		if(event.getCause().equals(DamageCause.LIGHTNING)) {
			//nothing gets damaged by nulled lightning
			for(int i=0; i<Magery._lightning.size(); i++) {
				if(event.getDamager().equals(Magery._lightning.get(i))) {
					Magery._lastLightningDamageTicks = 100;
					event.setCancelled(true);
					return;
				}
			}
		}
		
		if(event.getDamager() instanceof Fireball) {
			if(event.getEntity() instanceof Player) {
				Player player = (Player)event.getEntity();
				Fireball fb = (Fireball)event.getDamager();
				if(fb.getShooter() instanceof Player) {
					Player shooterPlayer = (Player)fb.getShooter();
					if(canPlayerDamagePlayer(shooterPlayer, player)) {
						criminalAction(player, shooterPlayer);
					}
				}
			}
		}
		
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			if(pseudoPlayer != null) {
				if(pseudoPlayer._convenient) {
					event.setCancelled(true);
					return;
				}
			}
		}
		
		Entity entity = event.getEntity();
		Entity entityDamager = event.getDamager();
		
		if(entityDamager instanceof Wolf) {
			Wolf wolf = (Wolf)entityDamager;
			Taming.wolfDamagingEntity(wolf, event);
		}
		
		if(entityDamager instanceof Player) {
			int damage = event.getDamage();
			Player playerAttacker = (Player)entityDamager;
			
			_lastAttackers.put(entity, entityDamager);			
			
			PseudoPlayer pseudoPlayerAttacker = PseudoPlayerHandler.getPseudoPlayer(playerAttacker.getName());
			ItemStack itemInHand = playerAttacker.getItemInHand();
			if(pseudoPlayerAttacker != null) {
				if(pseudoPlayerAttacker._loggedInRecentlyTicks > 0) {
					event.setCancelled(true);
					return;
				}
				
				if(itemInHand.getType().equals(Material.GOLD_SWORD)) {
					playerAttacker.getWorld().strikeLightningEffect(entity.getLocation());
					damage = 13;
				}
				
				if(entity instanceof Player) {					
					Player playerDefender = (Player)entity;
					PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(playerDefender.getName());
					
					/*if(Utils.distance(playerAttacker.getLocation(), playerDefender.getLocation()) > 3) {
						event.setCancelled(true);
						return;
					}*/
					
					if(itemInHand.getType().equals(Material.FEATHER)) {
						playerAttacker.sendMessage(ChatColor.GRAY+"You lightly tickle "+playerDefender.getName()+" with a feather.");
						playerDefender.sendMessage(ChatColor.GRAY+playerAttacker.getName()+" lightly tickles you with a feather.");
						event.setCancelled(true);
						return;
					}
					
					if(canPlayerDamagePlayer(playerAttacker, playerDefender)) {
						criminalAction(playerDefender, playerAttacker);
					}
					else {
						event.setCancelled(true);
						return;
					}
					
					double dist = Utils.distance(playerDefender.getLocation(), playerAttacker.getLocation());
					String messageString = playerAttacker.getName()+" hit from "+dist+" blocks away.";
					
					double hitDistance = Utils.distance(playerDefender.getLocation(), playerAttacker.getLocation());
					if(hitDistance > 4.5) {
						System.out.println(messageString);
						Output.sendToAdminIRC(null, "CHEATDETECT", messageString);
					}
					
					long lastAttackTime = pseudoPlayerAttacker._lastAttackTime;
					Date date = new Date();
					long curAttackTime = date.getTime();
					long diff = curAttackTime - lastAttackTime;
					pseudoPlayerAttacker._lastAttackTime = curAttackTime;
					messageString = playerAttacker.getName()+" hit too frequently: "+diff+"";
					
					if(diff < 800) {
						System.out.println(messageString);
						Output.sendToAdminIRC(null, "CHEATDETECT", messageString);
					}
					
					//attack notify
					if(pseudoPlayerDefender._lastAttackTicks <= 0) {
						pseudoPlayerDefender._lastAttackerName = playerAttacker.getName();
						pseudoPlayerDefender._lastAttackTicks = 100;
						playerDefender.sendMessage(ChatColor.GRAY+playerAttacker.getName()+" damaged you.");
					}
					// end attack notify
				}
				
				if(!RPG._damage) {
					RPG._damage = true;
				
					// adjust damage based on skills
					int additionalDamage = 0;
					
					/*if(itemInHand.getType().equals(Material.WOOD_AXE) || itemInHand.getType().equals(Material.STONE_AXE) || itemInHand.getType().equals(Material.IRON_AXE) || itemInHand.getType().equals(Material.DIAMOND_AXE) || itemInHand.getType().equals(Material.GOLD_AXE)) {
						Lumberjacking.possibleSkillGain(playerAttacker, pseudoPlayerAttacker, entity);
						if(pseudoPlayerAttacker.getStamina() >= Lumberjacking.getAttackStamCost()) {
							pseudoPlayerAttacker.setStamina(pseudoPlayerAttacker.getStamina()-Lumberjacking.getAttackStamCost());
							int skillVal = pseudoPlayerAttacker.getSkill("lumberjacking");
							if(skillVal >= 1000)
								additionalDamage = 8;
							else if(skillVal >= 750)
								additionalDamage = 6;
							else if(skillVal >= 500)
								additionalDamage = 4;
							else if(skillVal >= 250)
								additionalDamage = 2;
							
							if(itemInHand.getDurability() == -5)
								additionalDamage += 4;
							if(itemInHand.getDurability() == -4)
								additionalDamage += 3;
							if(itemInHand.getDurability() == -3)
								additionalDamage += 2;
							if(itemInHand.getDurability() == -2)
								additionalDamage += 2;
							if(itemInHand.getDurability() == -1)
								additionalDamage += 1;
							
							double chanceOfEffect = (double)skillVal / 1000;
							double bleedChance = chanceOfEffect * .2;
							
							if(bleedChance > Math.random()) {
								if(entity instanceof Player) {
									Player defenderPlayer = (Player)entity;
									PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
									if(defenderPseudoPlayer._bleedTicks <= 0) {
										defenderPseudoPlayer._bleedTicks = 10;
										defenderPlayer.sendMessage(ChatColor.GREEN+"You are bleeding!");
										playerAttacker.sendMessage(ChatColor.GREEN+defenderPlayer.getName()+" is bleeding!");
									}
								}
							}
						}
						else if(!pseudoPlayerAttacker._ljExausted){
							pseudoPlayerAttacker._ljExausted = true;
							playerAttacker.sendMessage(ChatColor.GRAY+"You are out of stamina and attack weakly.");
						}
					}*/
					/*if(itemInHand.getType().equals(Material.WOOD_SWORD) || itemInHand.getType().equals(Material.STONE_SWORD) || itemInHand.getType().equals(Material.IRON_SWORD) || itemInHand.getType().equals(Material.DIAMOND_SWORD) || itemInHand.getType().equals(Material.GOLD_SWORD)) {
						// using a sword
						int skillVal = pseudoPlayerAttacker.getSkill("blades");
						if(skillVal >= 1000)
							additionalDamage = 4;
						else if(skillVal >= 750)
							additionalDamage = 3;
						else if(skillVal >= 500)
							additionalDamage = 2;
						else if(skillVal >= 250)
							additionalDamage = 1;
						
						if(itemInHand.getType().equals(Material.WOOD_SWORD) ||
								itemInHand.getType().equals(Material.STONE_SWORD) ||
								itemInHand.getType().equals(Material.IRON_SWORD) ||
								itemInHand.getType().equals(Material.DIAMOND_SWORD) ||
								itemInHand.getType().equals(Material.GOLD_SWORD)) {
							if(itemInHand.getDurability() == -5)
								additionalDamage += 4;
							if(itemInHand.getDurability() == -4)
								additionalDamage += 3;
							if(itemInHand.getDurability() == -3)
								additionalDamage += 2;
							if(itemInHand.getDurability() == -2)
								additionalDamage += 2;
							if(itemInHand.getDurability() == -1)
								additionalDamage += 1;
						}
						
						Swordsmanship.possibleSkillGain(playerAttacker, pseudoPlayerAttacker, entity);
						
						double chanceOfEffect = (double)skillVal / 1000;
						double bleedChance = chanceOfEffect * .2;
						
						if(bleedChance > Math.random()) {
							if(entity instanceof Player) {
								Player defenderPlayer = (Player)entity;
								PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
								if(defenderPseudoPlayer._bleedTicks <= 0) {
									defenderPseudoPlayer._bleedTicks = 10;
									defenderPlayer.sendMessage(ChatColor.GREEN+"You are bleeding!");
									playerAttacker.sendMessage(ChatColor.GREEN+defenderPlayer.getName()+" is bleeding!");
								}
							}
						}
					}*/
					/*if(itemInHand.getType().equals(Material.BOW)){
						//event.getCause().equals(DamageCause.
					}
					else {
						// using something else
						int skillVal = pseudoPlayerAttacker.getSkill("brawling");
						if(skillVal >= 1000)
							additionalDamage = 4;
						else if(skillVal >= 750)
							additionalDamage = 3;
						else if(skillVal >= 500)
							additionalDamage = 2;
						else if(skillVal >= 250)
							additionalDamage = 1;
						Brawling.possibleSkillGain(playerAttacker, pseudoPlayerAttacker, entity);
						
						double chanceOfEffect = (double)skillVal / 1000;
						double stunChance = chanceOfEffect * .2;
						
						if(stunChance > Math.random()) {
							//stun
							if(entity instanceof Player) {
								Player defenderPlayer = (Player)entity;
								PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
								if(defenderPseudoPlayer._stunTicks <= 0) {
									defenderPseudoPlayer._stunTicks = 30;
									defenderPlayer.sendMessage(ChatColor.GREEN+"You have been stunned!");
									playerAttacker.sendMessage(ChatColor.GREEN+"You stunned "+defenderPlayer.getName()+"!");
								}
							}
						}
						
					}*/
					
					if(entity instanceof Player && entityDamager instanceof Player) {
						Player defenderPlayer = (Player)entity;
						PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
												
						damage /= 2;
						
						if(defenderPseudoPlayer._stoneSkinTicks > 0)
							damage/=2;
						
						if(damage <= 0)
							damage = 1;
												
						//vampire stuff
						if(defenderPseudoPlayer.isVampire()) {
							if(defenderPlayer.getInventory().getChestplate().getType().equals(Material.AIR)) {
								if(playerAttacker.getItemInHand().getType().equals(Material.WOOD_SWORD)) {
									event.setDamage(0);
									defenderPlayer.damage(20);
									return;
								}
							}
						}
						else if(defenderPseudoPlayer.isRobot()) {
							if(itemInHand.getType().equals(Material.GOLD_SWORD)) {
								event.setDamage(0);
								defenderPlayer.damage(10);
								Output.chatLocal(defenderPlayer, "*Zat!*", false);
								return;
							}
						}
						//end vampire stuff
					}
					
					event.setDamage(damage+additionalDamage);
					//System.out.println("Damage actually dealt: " + (damage+additionalDamage));
				}
			}
		}
	}
	
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		if(RPG._debugV)
    		System.out.println("Entity Death");
		
		Entity entity = event.getEntity();
		if(!_recentDeaths.contains(entity)) {
			_recentDeaths.add(entity);
			_recentDeathsTicks = 60;
		}
		
		if(entity instanceof Wolf) {
			Wolf wolf = (Wolf)entity;
			Taming.wolfDied(wolf,  event);
		}
			
		if(entity instanceof Monster || entity instanceof Giant || entity instanceof Ghast || entity instanceof PigZombie) {
			if(RPG._worldEvent != null && RPG._worldEvent instanceof GhastAttackEvent && entity instanceof Ghast) {
				GhastAttackEvent gae = (GhastAttackEvent)RPG._worldEvent;
				ArrayList<LivingEntity> _ghasts = gae.getGhasts();
				if(_ghasts.contains((LivingEntity)entity)) {
					int randInt = (int)Math.floor(Math.random()*13);
					if(randInt == 0) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_SWORD.getId(), 1));
					else if(randInt == 1) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_HOE.getId(), 1));
					else if(randInt == 2) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_PICKAXE.getId(), 1));
					else if(randInt == 3) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_AXE.getId(), 1));
					else if(randInt == 4) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_SPADE.getId(), 1));
					else if(randInt == 5) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.WATCH.getId(), 1));
					else if(randInt == 6) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_INGOT.getId(), 1));
					else if(randInt == 7) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.POWERED_RAIL.getId(), 3));
					else if(randInt == 8) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLDEN_APPLE.getId(), 3));
					else if(randInt == 9)
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_HELMET.getId(), 1));
					else if(randInt == 10) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_CHESTPLATE.getId(), 1));
					else if(randInt == 11) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_LEGGINGS.getId(), 1));
					else if(randInt == 12) 
						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_BOOTS.getId(), 1));
				}
			}
			// monster died
			// hit by an entity recently
			if(_lastAttackers.containsKey(entity)) {
				Entity lastAttackerEntity = _lastAttackers.get(entity);
				if(lastAttackerEntity == null)
					return;
				if(lastAttackerEntity instanceof Player) {
					Player lastAttackerPlayer = (Player)lastAttackerEntity;
					PseudoPlayer pseudoPlayerDamager = PseudoPlayerHandler.getPseudoPlayer(lastAttackerPlayer.getName());
					Survivalism.possibleSkillGain(lastAttackerPlayer, pseudoPlayerDamager);
					if(pseudoPlayerDamager.isVampire()) {
						int curHealth = lastAttackerPlayer.getHealth();
						curHealth += 2;
						if(curHealth > 20)
							curHealth = 20;
						if(curHealth != lastAttackerPlayer.getHealth()) {
							lastAttackerPlayer.setHealth(curHealth);
							Output.positiveMessage(lastAttackerPlayer, "You suck the life out of your prey.");
						}
					}
					double rand = Math.random();
					if(entity instanceof Ghast) {
						if(Math.random() < .2) {
							Spell droppedScroll;
							rand = Math.random();
							if(rand < .2)
								droppedScroll = new SPL_PermanentGateTravel();
							else {
								Random random = new Random();
								int randInt = random.nextInt(4);
								if(randInt == 0)
									droppedScroll = new SPL_Day();
								else if(randInt == 1)
									droppedScroll = new SPL_ClearSky();
								else if(randInt == 2)
									droppedScroll = new SPL_MoonJump();
								else if(randInt == 3)
									droppedScroll = new SPL_SummonMonster();
								else
									droppedScroll = new SPL_MoonJump();
							}
							
							int createdScrollId = Database.addScroll(pseudoPlayerDamager.getId(), droppedScroll);
							if(createdScrollId != -1) {
								pseudoPlayerDamager.addScroll(droppedScroll);
								Database.updatePlayerByPseudoPlayer(pseudoPlayerDamager);
								Output.positiveMessage(lastAttackerPlayer, "The Ghast dropped a scroll of "+droppedScroll.getName()+".");
							}
							else System.out.println("ERROR: got -1 from database.addscroll");
						}
					}
					else if(rand < .2 || entity instanceof Giant) {
						// 10% chance of dropping a spell
						String monsterName = MonsterHandler.getMonsterName(entity);
						Monster monster = (Monster)entity;
						Spell droppedSpell = MonsterHandler.getDroppedSpell(monster);
						if(droppedSpell == null) {
							System.out.println("dropped null spell");
						}
						else {
							//Spellbook spellbook = pseudoPlayerDamager.getSpellbook();
							int createdScrollId = Database.addScroll(pseudoPlayerDamager.getId(), droppedSpell);
							if(createdScrollId != -1) {
								droppedSpell.setId(createdScrollId);
								pseudoPlayerDamager.addScroll(droppedSpell);
								Database.updatePlayerByPseudoPlayer(pseudoPlayerDamager);
								Output.positiveMessage(lastAttackerPlayer, "The "+monsterName+" dropped a scroll of "+droppedSpell.getName()+".");
							}
							else System.out.println("ERROR: got -1 from database.addscroll");
						}
					}
					
					/*if(entity instanceof PigZombie) {
						if(Math.random() < .02) {
							if(
						}
					}*/
					
					if(entity instanceof Monster) {
						if(entity instanceof Zombie) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER.getId(), 1));
						}
						else if(entity instanceof Skeleton) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.BONE.getId(), 1));
							double arrowRand = (int)Math.floor(Math.random()*8);
							for(int i=0; i<arrowRand; i++) {
								entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.ARROW.getId(), 1));
							}
						}
						else if(entity instanceof Spider) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.STRING.getId(), 1));
						}
						else if(entity instanceof Creeper) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.SULPHUR.getId(), 1));
						}
					}
					else if(entity instanceof Animals) {
						if(entity instanceof Pig) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.PORK.getId(), 1));
						}
						else if(entity instanceof Chicken) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER.getId(), 1));
						}
						else if(entity instanceof Cow) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.LEATHER.getId(), 2));
						}
						else if(entity instanceof Sheep) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.WOOL.getId(), 1));
						}
						else if(entity instanceof Squid) {
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.INK_SACK.getId(), 1));
						}
					}
				}
				
				///_lastAttackers.remove(entity);
			}
			else {
				// not killed by a player
				if(entity instanceof LivingEntity) {
					// not killed by a player
					if(entity instanceof Player) {

					}
					else {
						event.getDrops().clear();
					}
				}
			}
		}
		
		if(entity instanceof Animals) {
			if(_lastAttackers.containsKey(entity)) {
				Entity lastAttackerEntity = _lastAttackers.get(entity);
				if(lastAttackerEntity instanceof Player) {
					Player attackerPlayer = (Player)lastAttackerEntity;
					PseudoPlayer pseudoPlayerAttacker = PseudoPlayerHandler.getPseudoPlayer(attackerPlayer.getName());
					
					if(pseudoPlayerAttacker.isVampire()) {
						int curHealth = attackerPlayer.getHealth();
						curHealth += 1;
						if(curHealth > 20)
							curHealth = 20;
						if(curHealth != attackerPlayer.getHealth()) {
							attackerPlayer.setHealth(curHealth);
							Output.positiveMessage(attackerPlayer, "You suck the life out of your prey.");
						}
					}
					
					int survSkill = pseudoPlayerAttacker.getSkill("survivalism");
					double chanceToDropApple = (double)survSkill/2000;
					//System.out.println("CTC:" + chanceToDropApple);
					double rand = Math.random();
					//System.out.println("R:" + rand);
					if(rand < chanceToDropApple) {
						ItemStack itemStack = null;
						if(entity instanceof Pig)
							itemStack = new ItemStack(Material.PORK, 1);
						else if(entity instanceof Chicken)
							itemStack = new ItemStack(Material.FEATHER, 1);
						else if(entity instanceof Cow)
							itemStack = new ItemStack(Material.LEATHER, 1);
						else if(entity instanceof Sheep)
							itemStack = new ItemStack(Material.WOOL, 1);
						else if(entity instanceof Squid)
							itemStack = new ItemStack(Material.INK_SACK, 1);
						if(itemStack != null) {
							entity.getWorld().dropItemNaturally(entity.getLocation(), itemStack);
							//System.out.println("Dropping "+itemStack.getType().name());
						}
					}
					Survivalism.possibleSkillGain(attackerPlayer, pseudoPlayerAttacker);
				}
				
				// do stuff
				///_lastAttackers.remove(entity);
			}
		}
		
		if(entity instanceof Player) {
			Player player = (Player)entity;
			
			
			NPCHandler.playerDied(player);
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			pseudoPlayer._bleedTicks = 0;
			pseudoPlayer._goToSpawnTicks = 0;
			pseudoPlayer._pvpTicks = 0;
			pseudoPlayer._respawnTicks = 200;
			pseudoPlayer._dieLog = 0;
			pseudoPlayer._lastChanceTicks = 0;
			
			//pseudoPlayer._clearTicks = 5;
			ArrayList<RecentAttacker> recentAttackers = pseudoPlayer.getRecentAttackers();
			for(RecentAttacker recentAttacker : recentAttackers) {
				String attackerName = recentAttacker.getName();
				Player attackerPlayer = plugin.getServer().getPlayer(attackerName);

				if(attackerPlayer != null) {
					PseudoPlayer attackerPseudo = PseudoPlayerHandler.getPseudoPlayer(attackerPlayer.getName());
					Survivalism.possibleSkillGain(attackerPlayer, attackerPseudo);
					if(attackerPseudo.isCriminal() && !pseudoPlayer.isCriminal())
					{}
					else continue;
					
					attackerPlayer.sendMessage("You have murdered "+player.getName());
					if(attackerPseudo != null) {
						
						if(attackerPseudo.isVampire()) {
							int curHealth = attackerPlayer.getHealth();
							curHealth += 4;
							if(curHealth > 20)
								curHealth = 20;
							if(curHealth != attackerPlayer.getHealth()) {
								attackerPlayer.setHealth(curHealth);
								Output.positiveMessage(attackerPlayer, "You suck the life out of your prey.");
							}
						}
						
						attackerPseudo.setMurderCounts(attackerPseudo.getMurderCounts()+1);
						Database.updatePlayer(attackerPlayer.getName());
						if(attackerPseudo.isMurderer()) {
							attackerPlayer.setDisplayName(ChatColor.RED+attackerPlayer.getName());
							Utils.setPlayerTitle(attackerPlayer);
							//SpoutManager.getAppearanceManager().setGlobalTitle(attackerPlayer, attackerPlayer.getDisplayName());
							//BukkitContrib.getAppearanceManager().setGlobalTitle(attackerPlayer, attackerPlayer.getDisplayName());
						}
					}
				}
				else {
					try {
						PseudoPlayer attackerPseudo = Database.createPseudoPlayer(attackerName);
						attackerPseudo.setMurderCounts(attackerPseudo.getMurderCounts()+1);
						Database.updatePlayerByPseudoPlayer(attackerPseudo);
					}
					catch(Exception e) {
						System.out.println("ERROR: failed to make the offline murderer pseudoplayer >>"+e.toString());
					}
				}
				//Output.sendToAdminIRC(player, " "+targetPlayer.getName(), msg);
			}
			
			if(pseudoPlayer.isRobot()) {
				// spawn tnt?
				//if(pseudoPlayer._killSelfTicks <= 0) {
					
					Output.chatLocal(player, "*BOOP*", false);
					Location location = player.getLocation();
					RoboExplosion roboExplosion = new RoboExplosion(player, 10);
					RPG._roboExplosions.add(roboExplosion);
					((CraftWorld)location.getWorld()).getHandle().createExplosion(null, location.getX(), location.getY(), location.getZ(), 2, false);
					//player.getWorld().strikeLightning(player.getLocation());
				//}
			}
			
			int numAttackers = recentAttackers.size();
			
			if(pseudoPlayer._guardAttackTicks > 0) {
				player.getServer().broadcastMessage(player.getDisplayName()+ChatColor.WHITE+" was executed by a Trinsic guard.");
			}
			else if(pseudoPlayer._killSelfTicks > 0) {
				//System.out.println(player.getName() + " killed self");
				String message = player.getDisplayName()+ChatColor.WHITE;
				Random generator = new Random();
				int randInt = generator.nextInt(5);
				
				switch(randInt) {
					case 0:
						message += " decided to end it all.";
						break;
					case 1:
						message += " Gomer Pyled.";
						break;
					case 2:
						message += " committed suicide!";
						break;
					case 3:
						message += " just gave up.";
						break;
					case 4:
						message += " really likes the spawn.";
						break;
				}

				player.getServer().broadcastMessage(message);
			}
			else if(numAttackers > 0) {
				//System.out.println(player.getName() + " killed by attacker");
				String deathMessage = player.getDisplayName()+ChatColor.WHITE+" was killed by";
				String attackers = "";
				for(int i=0; i<numAttackers; i++) {
					Player p = Utils.getPlugin().getServer().getPlayer(recentAttackers.get(i).getName());
					if(p != null) {
						if(i == numAttackers-1) {
							if(attackers != "")
								attackers += ChatColor.WHITE+" and " +p.getDisplayName()+ChatColor.WHITE+".";
							else
								attackers += ChatColor.WHITE+" "+p.getDisplayName()+ChatColor.WHITE+".";
						}
						else
							attackers+=ChatColor.WHITE+" "+p.getDisplayName();
					}
				}
				deathMessage += attackers;
				Player[] onlinePlayers = player.getServer().getOnlinePlayers();
				int numOnlinePlayers = onlinePlayers.length;
				for(int i=0; i<numOnlinePlayers; i++) {
					Player p = onlinePlayers[i];
					p.sendMessage(deathMessage);
				}
			}
			else {
				//System.out.println(player.getName() + " something else...");
				EntityDamageEvent e = player.getLastDamageCause();
				String message = player.getDisplayName()+ChatColor.WHITE;
				Plot plot = PlotHandler.findPlotAt(player.getLocation());
				if(e == null) {
					message += " died.";
				}
				else if(e.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
					message += " exploded";
					if(Math.random() < .05)
						message+= " just like the predator *beep*";
					else if(Math.random() <.05)
						message+= " just like jaws *Smile, you son of a bitch!*";
					else
						message+=".";
				}
				else if(e.getCause().equals(DamageCause.DROWNING)) {
					message += " drowned";
					if(plot != null)
						message+=" in "+plot.getName();
					message+=".";
				}
				else if(e.getCause().equals(DamageCause.FALL)) {
					message += " fell to their doom";
					if(plot != null)
						message+=" in "+plot.getName();
					message+=".";
				}
				else if(e.getCause().equals(DamageCause.FIRE) || e.getCause().equals(DamageCause.FIRE_TICK)) {
					message += " burned alive";
					if(plot != null)
						message+=" in "+plot.getName();
					if(Math.random() < .1)
						message+= " just like Nicolas Cage. *AAaahhhhhhh!*";
					else
						message+=".";
				}
				else if(e.getCause().equals(DamageCause.LAVA)) {
					message += " melted in a pit of lava";
					if(plot != null)
						message+=" in "+plot.getName();
					if(Math.random() < .1)
						message+= " just like the Terminator. *thumbs up*";
					else
						message+=".";
				}
				else if(e.getCause().equals(DamageCause.LIGHTNING)) {
					message += " was struck by lightning";
					if(plot != null)
						message+=" in "+plot.getName();
					if(Math.random() < .1)
						message+= " just like Powder. *is pale*";
					else
						message+=".";
				}
				else if(e.getCause().equals(DamageCause.SUFFOCATION)) {
					message += " suffocated";
					if(plot != null)
						message+=" in "+plot.getName();
					if(Math.random() < .1)
						message+= " just like David Carradine. *ew*";
					else
						message+=".";
				}
				else if(e.getCause().equals(DamageCause.VOID)) {
					message += " fell into the abyss.";
				}
				else if(e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
					//System.out.println((player.getName() + " killed by entity attacker"));
					if(e instanceof EntityDamageByEntityEvent) {
						EntityDamageByEntityEvent eDBEE = (EntityDamageByEntityEvent)e;
						Entity damager = eDBEE.getDamager();
						if(damager instanceof Creeper)
							message+= " was killed by a creeper.";
						else if(damager instanceof Zombie)
							message+= " was killed by a zombie.";
						else if(damager instanceof Skeleton)
							message+= " was killed by a skeleton.";
						else if(damager instanceof Ghast)
							message+= " was killed by a ghast.";
						else if(damager instanceof Giant)
							message+= " was killed by a huge fucking Zombie.";
						else if(damager instanceof Slime)
							message+= " was killed by a slime.";
						else if(damager instanceof Spider)
							message+= " was killed by a Spider.";
						else
							message+= "'s life was tragically cut short.";
					}
				}
				else if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
					message += " exploded.";
				}
				else message += " died.";
				
				Player[] onlinePlayers = player.getServer().getOnlinePlayers();
				int numOnlinePlayers = onlinePlayers.length;
				for(int i=0; i<numOnlinePlayers; i++) {
					Player p = onlinePlayers[i];
					p.sendMessage(message);
				}
			}
			pseudoPlayer.clearRecentAttackers();
		}
		_lastAttackers.remove(entity);
	}
	
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
		if(event.getEntity() instanceof Player) {			
			Player playerDefender = (Player)event.getEntity();
			PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(playerDefender.getName());
			Block damager = event.getDamager();
			if(damager != null) {
				if(damager.getType().equals(Material.FIRE) || damager.getType().equals(Material.LAVA)) {
					if(pseudoPlayerDefender._fireWalkTicks > 0) {
						event.setCancelled(true);
						return;
					}
				}
				
				String creatorName = Magery.findCreatorNameOfBlockAt(damager.getX(), damager.getY(), damager.getZ());
				if(creatorName != null) {					
					Player playerAttacker = plugin.getServer().getPlayer(creatorName);
					if(playerAttacker != null) {						
						if(canPlayerDamagePlayer(playerAttacker, playerDefender)) {
							criminalAction(playerDefender, playerAttacker);
						}
						else {
							event.setCancelled(true);
							return;
						}
						
						// attack notify
						
						if(pseudoPlayerDefender._lastAttackTicks <= 0) {
							pseudoPlayerDefender._lastAttackerName = playerAttacker.getName();
							pseudoPlayerDefender._lastAttackTicks = 100;
							playerDefender.sendMessage(ChatColor.GRAY+playerAttacker.getName()+" damaged you.");
						}
						// end attack notify
					}
				}
			}
		}
	}
    
	@Override
	public void onExplosionPrime(ExplosionPrimeEvent event) {
		Entity entity = event.getEntity();
		ArrayList<Plot> plots = PlotHandler.getPlots();
		for(Plot plot : plots) {
			if(plot.getLocation().getWorld().equals(entity.getWorld())) {
				if(Utils.isWithin(plot.getLocation(), entity.getLocation(), plot.getRadius()+10)) {
					if(!plot.getLocation().getWorld().equals(RPG._netherWorld) && plot.isProtected())
						event.setFire(false);
				}
			}
		}
	}
	
	@Override
    public void onEntityExplode(EntityExplodeEvent event) {
		Entity entity = event.getEntity();
		
		// Determine if any plots are within 10 blocks of the explosion
		ArrayList<Plot> plots = PlotHandler.getPlots();
		ArrayList<Plot> plotsWithin = new ArrayList<Plot>();
		for(Plot plot : plots) {
			if(plot.getLocation().getWorld().equals(entity.getWorld())) {
				if(Utils.isWithin(plot.getLocation(), entity.getLocation(), plot.getRadius()+10)) {
					if(plot.isProtected()) {
						if(!plot.isExplosionAllowed()) {
							event.setCancelled(true);
							return;
						}
						plotsWithin.add(plot);
					}
				}
			}
		}
		
		// If not, go right ahead
		if(plotsWithin.size() <= 0)
			return;
		
		// If we are within 10 blocks of a plot, make sure the owner set it off
		int entityId = entity.getEntityId();
		for(int i=0; i<RPG._primedPlotTNT.size(); i++) {
			int primedPlotTNTId = RPG._primedPlotTNT.get(i);
			if(primedPlotTNTId == entityId) {
				RPG._primedPlotTNT.remove(i);
				return;
			}
		}
		event.setCancelled(true);
	}
	
	@Override
    public void onPaintingBreak(PaintingBreakEvent event) {
		Painting painting = event.getPainting();
		Plot plot = PlotHandler.findPlotAt(painting.getLocation());
		if(plot != null && plot.isProtected()) {
			if(event instanceof PaintingBreakByEntityEvent) {
				PaintingBreakByEntityEvent e = (PaintingBreakByEntityEvent)event;
				Entity eRemover = e.getRemover();
				if(eRemover instanceof Player) {
					Player player = (Player)eRemover;
					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
					if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()))) {
						if(plot.isFriendBuild() && plot.isFriend(player.getName())) {
							
						}
						Output.simpleError(player, "You cannot do that here, the plot is protected.");
						event.setCancelled(true);
					}
				}
				else {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@Override
    public void onPaintingPlace(PaintingPlaceEvent event) {
		Painting painting = event.getPainting();
		Plot plot = PlotHandler.findPlotAt(painting.getLocation());
		if(plot != null && plot.isProtected()) {
			Player player = event.getPlayer();
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			if((pseudoPlayer._plottest == plot) || ( !plot.isOwner(player.getName()) && !plot.isCoOwner(player.getName()))) {
				if(plot.isFriendBuild() && plot.isFriend(player.getName())) {
					
				}
				Output.simpleError(player, "You cannot do that here, the plot is protected.");
				event.setCancelled(true);
			}
		}
		else {
			event.setCancelled(true);
		}
	}
	
	public static void criminalAction(Player player, Player playerDamager) {
		if(player.getName().equals(playerDamager.getName()))
			return;
		
		PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		PseudoPlayer pseudoPlayerAttacker = PseudoPlayerHandler.getPseudoPlayer(playerDamager.getName());
		pseudoPlayerDefender.addRecentAttacker(new RecentAttacker(playerDamager.getName(), _RECENT_ATTACKER_TIME));
		
		// only criminal if it happens in the normal world
		if(player.getWorld().equals(RPG._normalWorld)) {
			Plot plot = PlotHandler.findPlotAt(player.getLocation());
			// devender is on a plot
			if(plot != null) {
				// and the attacker is a member of the plot
				if(plot.isMember(playerDamager.getName())) {
					// and the defender is NOT a member of the plot
					if(!plot.isMember(player.getName())) {
						// don't do criminal thing: texas rules
						return;
					}
				}
			}
			
			// If the defender has attacked anyone within 30 seconds they engaged in combat willingly
			// so attacking them will not be criminal even if they are blue
			
			if(pseudoPlayerDefender._engageInCombatTicks > 0) {
				return;
			}
			
			Party party = pseudoPlayerDefender.getParty();
			if(party != null) {
				if(party.isMember(playerDamager.getName())) {
					return;
				}
			}
			Clan clan = pseudoPlayerDefender.getClan();
			if(clan != null) {
				if(clan.isInClan(playerDamager.getName())) {
					return;
				}
			}
			
			//add pvp ticks
			pseudoPlayerDefender._pvpTicks = 150; // 10 seconds
			pseudoPlayerAttacker._engageInCombatTicks = 300; // attacker attacked someone, he is engaging in combat
			
			// Determine if a criminal action has taken place
			if(!pseudoPlayerDefender.isCriminal()) {
				// attacked non criminal, thats a criminal action
				if(!pseudoPlayerAttacker.isCriminal()) {
					playerDamager.sendMessage(ChatColor.RED+"You have committed a criminal action");
				}
				//pseudoPlayerDefender.addRecentAttacker(new RecentAttacker(playerDamager.getName(), _RECENT_ATTACKER_TIME));
				pseudoPlayerAttacker.setCriminalTicks(_criminalTicksPerCrime);
				if(!pseudoPlayerAttacker.isMurderer()) {
					playerDamager.setDisplayName(ChatColor.GRAY+playerDamager.getName());
					Utils.setPlayerTitle(playerDamager);
					//SpoutManager.getAppearanceManager().setGlobalTitle(playerDamager, playerDamager.getDisplayName());
					//BukkitContrib.getAppearanceManager().setGlobalTitle(playerDamager, playerDamager.getDisplayName());
				}
			}
		}
	}
	
	@Override
    public void onEntityTarget(EntityTargetEvent event) {
		Entity entity = event.getEntity();
		Entity targetEntity = event.getTarget();
		
		if(targetEntity instanceof Player) {
			Player targetPlayer = (Player)targetEntity;
			PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
			if(targetPseudoPlayer == null) {
				event.setCancelled(true);
				return;
			}
			
			if(targetPseudoPlayer._convenient) {
				event.setCancelled(true);
				return;
			}
			
			if(entity instanceof Monster) {
				LivingEntity lE = (LivingEntity)entity;
				if(lE.getLastDamage() > 0) {
					return;
				}
				else if(targetPlayer.getInventory().getHelmet().getType().equals(Material.PUMPKIN)) {
					if(targetPlayer.isSneaking()) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	@Override
    public void onEntityTame(EntityTameEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Wolf) {
			Wolf wolf = (Wolf)entity;
			Player ownerPlayer = (Player)event.getOwner();
			
			if(ownerPlayer == null)
				return;
			
			PseudoPlayer ownerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(ownerPlayer.getName());
			ArrayList<PseudoWolf> tamedWolves = ownerPseudoPlayer.getTamedWolves();
			int tamingSkill = ownerPseudoPlayer.getSkill("taming");
			int allowedAmount = 1;
			if(tamingSkill >= 1000)
				allowedAmount=3;
			else if(tamingSkill >= 500)
				allowedAmount=2;
			
			int numTamedWolves = tamedWolves.size();
			if(numTamedWolves >= allowedAmount) {
				Output.simpleError(ownerPlayer, "You cannot tame that, you have already tamed " + numTamedWolves + " out of "+allowedAmount+" animals.");
				event.setCancelled(true);
				return;
			}
			
			// We have room for a new pet
			// Is the pet we just tamed already owned? (Shouldn't be possible...")
			ArrayList<PseudoWolf> pseudoWolves = PseudoWolfHandler.getPseudoWolves();
			for(PseudoWolf pW : pseudoWolves) {
				// If any of the already loaded pseudowolves were just tamed...
				if(pW.getUniqueId().equals(wolf.getUniqueId().toString())) {
					Output.simpleError(ownerPlayer, "That wolf is already tamed.");
					event.setCancelled(true);
					return;
				}
			}

			PseudoWolf pseudoWolf = new PseudoWolf(wolf.getUniqueId().toString(), "Wolfy");
			int id = Database.addPseudoWolf(pseudoWolf);
			pseudoWolf.setId(id);
			PseudoWolfHandler.add(pseudoWolf);
			ownerPseudoPlayer.getTamedWolves().add(pseudoWolf);
			Database.updatePseudoPlayerTamedWolves(ownerPseudoPlayer);
			Taming.possibleSkillGain(ownerPlayer, ownerPseudoPlayer);
			Output.positiveMessage(ownerPlayer, "You have tamed "+pseudoWolf.getName()+".");
			//ownerPseudoPlayer.getTamedWolves().add(wolf);
			
			/*Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
			for(Player p : onlinePlayers) {
				if(event.getOwner().equals(p)) {
					ownerPlayer = p;
					break;
				}
			}
			
			if(ownerPlayer == null)
				return;*/
		}
	}
	
	@Override
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
	}
	
	/*@Override
	public static void onEntityRegainHealth(EntityRegainHealthEvent event) {
		
	}*/
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		Entity damagedEntity = event.getEntity();
	
		if(event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent)event;
			Entity damagingEntity = entityDamageByEntityEvent.getDamager();
			
			// If PvP
			if(damagingEntity instanceof Player && damagedEntity instanceof Player) {
				Player damagingPlayer = (Player)damagingEntity;
				Player damagedPlayer = (Player)damagedEntity;
				
				if(!canPlayerDamagePlayer(damagingPlayer, damagedPlayer)) {
					event.setCancelled(true);
					return;
				}
			}
			
			if(damagingEntity instanceof Player) {
				Player damagingPlayer = (Player)damagingEntity;
				ItemStack itemInHand = damagingPlayer.getItemInHand();
				if(itemInHand.getType().equals(Material.WOOD_SWORD) || itemInHand.getType().equals(Material.STONE_SWORD) || itemInHand.getType().equals(Material.IRON_SWORD) ||	itemInHand.getType().equals(Material.DIAMOND_SWORD) || itemInHand.getType().equals(Material.GOLD_SWORD)) {
					Swordsmanship.playerDamagedEntityWithSword(damagingPlayer, damagedEntity, entityDamageByEntityEvent);
				}
				else if(itemInHand.getType().equals(Material.WOOD_AXE) || itemInHand.getType().equals(Material.STONE_AXE) || itemInHand.getType().equals(Material.IRON_AXE) || itemInHand.getType().equals(Material.DIAMOND_AXE) || itemInHand.getType().equals(Material.GOLD_AXE)) {
					Lumberjacking.playerDamagedEntityWithAxe(damagingPlayer, damagedEntity, entityDamageByEntityEvent);
				}
				else {
					Brawling.playerDamagedEntityWithMisc(damagingPlayer, damagedEntity, entityDamageByEntityEvent);
				}
			}
			else if(damagingEntity instanceof Wolf) {
				Wolf wolf = (Wolf)damagingEntity;
				Taming.wolfDamagingEntity(wolf, entityDamageByEntityEvent);
			}
		}
		
		if(damagedEntity instanceof Player) {
			Player damagedPlayer = (Player)damagedEntity;
			PseudoPlayer damagedPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(damagedPlayer.getName());
					
			//TODO NPC hack
			if(damagedPseudoPlayer == null) {
				//must be attacking an NPC
				event.setCancelled(true);
				return;
			}
			
			//Attacking an admin with /convenient on
			if(damagedPseudoPlayer._convenient) {
				event.setCancelled(true);
				return;
			}
			
			if(event.getCause().equals(DamageCause.DROWNING)) {				
				if(damagedPlayer.getInventory().getHelmet().getType().equals(Material.GOLD_HELMET)) {
					event.setCancelled(true);
					return;
				}
			}
			
			if(event.getCause().equals(DamageCause.FALL)) {
				if(damagedPlayer.getInventory().getBoots().getType().equals(Material.GOLD_BOOTS)) {
					event.setCancelled(true);
					return;
				}
				
				if(damagedPseudoPlayer._moonJumpTicks > 0 || damagedPseudoPlayer._fireWalkTicks > 0) {
					event.setCancelled(true);
					return;
				}
				
				Block b = damagedPlayer.getLocation().getBlock();
				if(b.getRelative(BlockFace.DOWN).getType().equals(Material.WOOL)) {
					event.setCancelled(true);
					return;
				}
				
				if(damagedPseudoPlayer._noFireTicks > 0) {
					event.setCancelled(true);
					return;
				}
			}
	
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LAVA)) {
				if(damagedPlayer.getInventory().getLeggings().getType().equals(Material.GOLD_LEGGINGS)) {
					event.setCancelled(true);
					return;
				}
			}
			
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
				if(damagedPseudoPlayer._noFireTicks > 0) {
					event.setCancelled(true);
					return;
				}
				
				if(damagedPseudoPlayer._fireWalkTicks > 0) {
					event.setCancelled(true);
					return;
				}
				
				ArrayList<Camp> camps = Survivalism.getCamps();
				for(Camp camp : camps) {
					Location loc = camp.getFireBlock().getLocation();
					if(Utils.isWithin(damagedPlayer.getLocation(), new Location(loc.getWorld(), loc.getX()+.5, loc.getY()+.5, loc.getZ()+.5), 3)) {
						event.setCancelled(true);
						damagedPlayer.setFireTicks(0);
						return;
					}
				}
			}
			
			if(event.getCause().equals(DamageCause.SUFFOCATION)) {
				if(damagedPlayer.getInventory().getHelmet().getType().equals(Material.GOLD_HELMET)) {
					event.setCancelled(true);
					return;
				}
				
				ArrayList<Block> blocksToTest = new ArrayList<Block>();
				blocksToTest.add(damagedPlayer.getWorld().getBlockAt(damagedPlayer.getLocation().getBlockX(), damagedPlayer.getLocation().getBlockY(), damagedPlayer.getLocation().getBlockZ()));
				blocksToTest.add(damagedPlayer.getWorld().getBlockAt(damagedPlayer.getLocation().getBlockX(), damagedPlayer.getLocation().getBlockY()+1, damagedPlayer.getLocation().getBlockZ()));
				
				for(Block b : blocksToTest) {
					String creatorName = Magery.findCreatorNameOfBlockAt(b.getX(), b.getY(), b.getZ());
					if((creatorName != null) && (!creatorName.equals(damagedPlayer.getName()))) {
						Player playerAttacker = plugin.getServer().getPlayer(creatorName);
						if(playerAttacker != null) {
							if(canPlayerDamagePlayer(playerAttacker, damagedPlayer)) {
								criminalAction(damagedPlayer, playerAttacker);
							}
							else {
								event.setCancelled(true);
								return;
							}
							
							// attack notify
							if(damagedPseudoPlayer._lastAttackTicks <= 0) {
								damagedPseudoPlayer._lastAttackerName = playerAttacker.getName();
								damagedPseudoPlayer._lastAttackTicks = 100;
								damagedPlayer.sendMessage(ChatColor.GRAY+playerAttacker.getName()+" damaged you.");
							}
							// end attack notify
						}
					}
				}
			}
			
			if(event.getCause().equals(DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				if(damagedPlayer.getInventory().getChestplate().getType().equals(Material.GOLD_CHESTPLATE)) {
					event.setCancelled(true);
					return;
				}
			}
			
			if(damagedPseudoPlayer.getCastDelayTicks() > 0) {
				damagedPseudoPlayer.setCastDelayTicks(0);
				damagedPseudoPlayer.setDelayedSpell(null);
	         	Output.simpleError(damagedPlayer, "Took damage while casting, spell was disrupted.");
	        }
			
			if(damagedPseudoPlayer._goToSpawnTicks > 0) {
				damagedPseudoPlayer._goToSpawnTicks = 0;
	         	Output.simpleError(damagedPlayer, "Took damage while casting, /spawn was disrupted.");
			}
			
			if(damagedPseudoPlayer._traitChangeTicks > 0) {
				damagedPseudoPlayer._traitChangeTicks = 0;
	         	Output.simpleError(damagedPlayer, "Took damage while changing trait, it was disrupted.");
			}
		}
		
		Entity entity = event.getEntity();
		if(entity instanceof Wolf) {
			if(event instanceof EntityDamageByEntityEvent)
				Taming.wolfDamagedByEntity((Wolf)entity, (EntityDamageByEntityEvent)event);
			else
				Taming.wolfDamaged((Wolf)entity, (EntityDamageEvent) event);
		}
		
		boolean isPlayer = false;
		if(entity instanceof Player)
			isPlayer = true;
		
		// Handle camps
		/*if(isPlayer) {
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
				ArrayList<Camp> camps = Survivalism.getCamps();
				for(Camp camp : camps) {
					Location loc = camp.getFireBlock().getLocation();
					if(Utils.isWithin(entity.getLocation(), new Location(loc.getWorld(), loc.getX()+.5, loc.getY()+.5, loc.getZ()+.5), 3)) {
						event.setCancelled(true);
						entity.setFireTicks(0);
						return;
					}
				}
			}
		}*/
		
		// player was hit (including npcs)
		if(event.getEntity() instanceof Player) {
			Player playerDefender = (Player)event.getEntity();
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(playerDefender.getName());
			
			/*if(event.getCause().equals(DamageCause.BLOCK_EXPLOSION) || event.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				if(playerDefender.getInventory().getChestplate().getType().equals(Material.GOLD_CHESTPLATE)) {
					event.setCancelled(true);
					return;
				}
			}*/
			
			/*if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LAVA)) {
				if(playerDefender.getInventory().getLeggings().getType().equals(Material.GOLD_LEGGINGS)) {
					event.setCancelled(true);
					return;
				}
			}*/
			
			/*if(event.getCause().equals(DamageCause.FALL) && (pseudoPlayer._moonJumpTicks > 0 || pseudoPlayer._fireWalkTicks > 0)) {
				event.setCancelled(true);
				return;
			}*/
			
			// If its not a player its probably an NPC
			/*if(pseudoPlayer == null) {
				// If it was hit by a an entity
				if(event instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent)event;
					Entity attackerEntity = event2.getDamager();
					// and the entity is a player
					if(attackerEntity instanceof Player) {
						// A player hit an NPC
						Player attackerPlayer = (Player)attackerEntity;
						PseudoPlayer attackerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(attackerPlayer.getName());
						
						Plot plot = PlotHandler.findPlotAt(playerDefender.getLocation());
						if(plot != null && (plot.isOwner(attackerPlayer.getName()) || plot.isCoOwner(attackerPlayer.getName()))) {
							if(!attackerPlayer.getItemInHand().equals(Material.AIR)) {
								ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
								PlotNPC foundPlotNPC = null;
								for(int i=0; i<plotNPCs.size(); i++) {
									if(playerDefender.getName().equals(plotNPCs.get(i).getName())) {
										foundPlotNPC = plotNPCs.get(i);
									}
								}
								if(foundPlotNPC != null) {
									// we attacked a plot npc who is on our plot
									ItemStack itemInHand = attackerPlayer.getItemInHand();
									// right clicked an NPC while not holding anything
									if(foundPlotNPC != null) {
										// Found the correct plotNPC, determine what kind it is
										if(foundPlotNPC.getJob().equals("vendor")) {
											Store store = RPGPlayerListener.plotNPCVendor2Store(foundPlotNPC);
											if(store == null)
												System.out.println("nullstore");
											if(itemInHand.getType().equals(Material.AIR)) {
												attackerPseudoPlayer._lastStoreAccessed = store;
												store.outputList(attackerPlayer);
											}
											else {
												// right clicked an NPC while holding something
												int itemId = itemInHand.getTypeId();
												int amount = itemInHand.getAmount();
												int durability = itemInHand.getDurability();
												
												//ItemStack foundItem = null;
												int foundIndex = -1;
												ArrayList<ItemStack> items = store.getItemsForSale();
												for(int i=0; i<items.size(); i++) {
													if(items.get(i).getTypeId() == itemId && items.get(i).getDurability() == durability) {
														//foundItem = items.get(i);
														foundIndex = i;
														break;
													}
												}
												
												//if(foundItem != null) {
												if(foundIndex != -1) {
													//existing item in the vendor
													//foundItem.setAmount(foundItem.getAmount()+amount);
													store.getItemStocks().set(foundIndex, store.getItemStocks().get(foundIndex)+ amount);
													attackerPlayer.getInventory().clear(attackerPlayer.getInventory().getHeldItemSlot());
													Database.updateStore(store);
													Output.positiveMessage(attackerPlayer, "You added "+amount+" "+Material.getMaterial(itemId).name()+".");
												}
												else {
													//not existing item
													if(store.getItemsForSale().size() >= 8) {
														Output.simpleError(attackerPlayer, "That vendor already has 8 different items in it.");
														event.setCancelled(true);
														return;
													}
													Output.positiveMessage(attackerPlayer, "How much do you want to charge for each "+Material.getMaterial(itemId).name()+"? (Say the price or say cancel)");
													attackerPseudoPlayer._addingSaleItem = itemInHand;
													attackerPseudoPlayer._addingSaleStore = store;
												}
											}
										}
									}
									//else Output.simpleError(player, "Error retrieving NPC, tell an admin or something.");
								}
							}
						}
					}
				}
				
				event.setCancelled(true);
				return;
			}*/
			
			if(pseudoPlayer._convenient) {
				event.setCancelled(true);
				return;
			}
			
			/*if(event.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
				if(RPG._roboExplosions.size() > 0) {
					int numRoboExplosions = RPG._roboExplosions.size();
					for(int i=0; i<numRoboExplosions; i++) {
						Player playerAttacker = playerDefender.getServer().getPlayer(RPG._roboExplosions.get(i).getName());
						double dist = Utils.distance(playerDefender.getLocation(),RPG._roboExplosions.get(i).getLocation());
						if(dist <= 5) {
							if(canPlayerDamagePlayer(playerAttacker, playerDefender)) {
								criminalAction(playerDefender, playerAttacker);	
							}
							else {
								event.setCancelled(true);
								return;
							}
							
							// attack notify
							PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(playerDefender.getName());
							if(pseudoPlayerDefender._lastAttackTicks <= 0) {
								pseudoPlayerDefender._lastAttackerName = playerAttacker.getName();
								pseudoPlayerDefender._lastAttackTicks = 100;
								playerDefender.sendMessage(ChatColor.GRAY+playerAttacker.getName()+" damaged you.");
							}
						}
					}
				}
			}*/
			
			// respawn immunity
			if(pseudoPlayer._respawnTicks > 0) {
				event.setCancelled(true);
				return;
			}
			
			/*if(event.getCause().equals(DamageCause.LIGHTNING) && event.getDamage() > 0) {
				if(pseudoPlayer.isRobot()) {
					if(playerDefender.getHealth() > 0) {
						event.setDamage(event.getDamage()*2);
						Output.chatLocal(playerDefender, "*Zat!*", false);
					}
				}
			}*/
			
			/*if(event.getCause().equals(DamageCause.FALL)) {
				if(playerDefender.getInventory().getBoots().getType().equals(Material.GOLD_BOOTS)) {
					event.setCancelled(true);
					return;
				}
				
				if(pseudoPlayer._flyTicks > 0) {
					event.setCancelled(true);
					return;
				}
				
				Block b = playerDefender.getLocation().getBlock();
				if(b.getRelative(BlockFace.DOWN).getType().equals(Material.WOOL)) {
					event.setCancelled(true);
					return;
				}
				
				if(pseudoPlayer._noFireTicks > 0) {
					event.setCancelled(true);
					return;
				}
			}*/
			
			/*if(event.getType().equals(DamageCause.FIRE) || event.getType().equals(DamageCause.FIRE_TICK)) {
				if(pseudoPlayer._noFireTicks > 0) {
					event.setCancelled(true);
					return;
				}
			}
			
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LAVA)) {
				if(pseudoPlayer._fireWalkTicks > 0) {
					event.setCancelled(true);
					return;
				}
			}*/
			
			/*if(event.getCause().equals(DamageCause.DROWNING)) {
				if(playerDefender.getInventory().getHelmet().getType().equals(Material.GOLD_HELMET)) {
					event.setCancelled(true);
					return;
				}
			}*/
			
			/*if(event.getCause().equals(DamageCause.SUFFOCATION)) {
				if(playerDefender.getInventory().getHelmet().getType().equals(Material.GOLD_HELMET)) {
					event.setCancelled(true);
					return;
				}
				
				ArrayList<Block> blocksToTest = new ArrayList<Block>();
				blocksToTest.add(playerDefender.getWorld().getBlockAt(playerDefender.getLocation().getBlockX(), playerDefender.getLocation().getBlockY(), playerDefender.getLocation().getBlockZ()));
				blocksToTest.add(playerDefender.getWorld().getBlockAt(playerDefender.getLocation().getBlockX(), playerDefender.getLocation().getBlockY()+1, playerDefender.getLocation().getBlockZ()));
				
				for(Block b : blocksToTest) {
					String creatorName = Magery.findCreatorNameOfBlockAt(b.getX(), b.getY(), b.getZ());
					if((creatorName != null) && (!creatorName.equals(playerDefender.getName()))) {
						Player playerAttacker = plugin.getServer().getPlayer(creatorName);
						if(playerAttacker != null) {
							if(canPlayerDamagePlayer(playerAttacker, playerDefender)) {
								criminalAction(playerDefender, playerAttacker);
							}
							else {
								event.setCancelled(true);
								return;
							}
							
							// attack notify
							PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(playerDefender.getName());
							if(pseudoPlayerDefender._lastAttackTicks <= 0) {
								pseudoPlayerDefender._lastAttackerName = playerAttacker.getName();
								pseudoPlayerDefender._lastAttackTicks = 100;
								playerDefender.sendMessage(ChatColor.GRAY+playerAttacker.getName()+" damaged you.");
							}
							// end attack notify
						}
					}
				}
			}*/
			
			/*if(pseudoPlayer.getCastDelayTicks() > 0) {
	         	pseudoPlayer.setCastDelayTicks(0);
	         	pseudoPlayer.setDelayedSpell(null);
	         	Output.simpleError(playerDefender, "Took damage while casting, spell was disrupted.");
	        }
			
			if(pseudoPlayer._goToSpawnTicks > 0) {
				pseudoPlayer._goToSpawnTicks = 0;
	         	Output.simpleError(playerDefender, "Took damage while casting, /spawn was disrupted.");
			}
			
			if(pseudoPlayer._traitChangeTicks > 0) {
				pseudoPlayer._traitChangeTicks = 0;
	         	Output.simpleError(playerDefender, "Took damage while changing trait, it was disrupted.");
			}*/
		}
		
		if(entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity)event.getEntity();
			if(livingEntity.getNoDamageTicks() > 0) {
				return;
			}
		}
		
		if(event instanceof EntityDamageByBlockEvent) {
			onEntityDamageByBlock((EntityDamageByBlockEvent)event);
		}
		else if(event instanceof EntityDamageByProjectileEvent) {
			onEntityDamageByProjectile((EntityDamageByProjectileEvent)event);
		}
		else if(event instanceof EntityDamageByEntityEvent){
			onEntityDamageByEntity((EntityDamageByEntityEvent)event);
		}
		else {
			if(entity instanceof Player) {
				Player defenderPlayer = (Player)entity;
				PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
				if(defenderPseudoPlayer._stoneSkinTicks > 0) {
					int damage = event.getDamage();
					damage/=2;
					event.setDamage(damage);
				}
			}
		}
		/*else if(event instanceof EntityDamageByEntityEvent) {
			if(event.getCause().equals(DamageCause.PROJECTILE)) {
				onEntityDamageByProjectile((EntityDamageByEntityEvent)event);
			}
			else {
				EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent)event;
				onEntityDamageByEntity(edbeEvent);
			}
		}*/
		
		// Defense skill - works
		/*if(entity instanceof Player) {
			Player player = (Player)entity;
			Defense.playerDamaged(player, event);
		}*/
	}

	public static boolean canPlayerDamagePlayer(Player playerAttacker, Player playerDefender) {
		PseudoPlayer pseudoPlayerAttacker = PseudoPlayerHandler.getPseudoPlayer(playerAttacker.getName());
		PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(playerDefender.getName());
		
		if(pseudoPlayerDefender._allowFriendlyFire)
			return true;
		
		Party attackerParty = pseudoPlayerAttacker.getParty();
		if(attackerParty != null) {
			if(attackerParty.isMember(playerDefender.getName())) {
				return false;
			}
		}
		
		Clan attackerClan = pseudoPlayerAttacker.getClan();
		if(attackerClan != null) {
			if(attackerClan.isInClan(playerDefender.getName())) {
				return false;
			}
		}
		
		return true;
	}
}
