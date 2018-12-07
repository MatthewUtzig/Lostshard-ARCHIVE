package com.lostshard.RPG.Listeners;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
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
import com.lostshard.RPG.Skills.*;
import com.lostshard.RPG.Spells.SPL_ClearSky;
import com.lostshard.RPG.Spells.SPL_DRG_Envy;
import com.lostshard.RPG.Spells.SPL_DRG_Gluttony;
import com.lostshard.RPG.Spells.SPL_DRG_Greed;
import com.lostshard.RPG.Spells.SPL_DRG_Grief;
import com.lostshard.RPG.Spells.SPL_DRG_Lust;
import com.lostshard.RPG.Spells.SPL_DRG_Pride;
import com.lostshard.RPG.Spells.SPL_DRG_Sloth;
import com.lostshard.RPG.Spells.SPL_DRG_Wrath;
import com.lostshard.RPG.Spells.SPL_Day;
import com.lostshard.RPG.Spells.SPL_MoonJump;
import com.lostshard.RPG.Spells.SPL_PermanentGateTravel;
import com.lostshard.RPG.Spells.SPL_SummonMonster;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class RPGEntityListener implements Listener{
	private final RPG plugin;
	private static final int _criminalTicksPerCrime = 10*300;
	public static final int _RECENT_ATTACKER_TIME = 10*30;
	public HashMap<Entity, Entity> _lastAttackers;
	public HashMap<Integer, Integer> _livingEntityArrowHitHashMap = new HashMap<Integer, Integer>();
	public HashSet<Entity> _recentDeaths;
	public int _recentDeathsTicks = 0;
	
	public RPGEntityListener(RPG instance) {
		plugin = instance;
		_lastAttackers = new HashMap<Entity,Entity>();
		_recentDeaths = new HashSet<Entity>();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/*@Override
    public void onProjectileHit(ProjectileHitEvent event) {
		if(event.getEntity() instanceof Block) {
			Block block = event.
		}
	}*/
	
	@EventHandler
	public void EntityCreatePortalEvent(org.bukkit.event.entity.EntityCreatePortalEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Player player = (Player)entity;
			if(player.isOp()) {
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
				if(pseudoPlayer._convenient)
					event.setFoodLevel(20);
			}
			else {
				int curFoodLevel = player.getFoodLevel();
				int nextFoodLevel = event.getFoodLevel();
				int diff = nextFoodLevel - curFoodLevel;
				if(diff > 0) {
					// Gaining fullness
					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
					int survSkill = pseudoPlayer.getSkill("survivalism");
					double multiplier = 1+(double)survSkill/1000;
					int modFoodLevel = curFoodLevel + (int)((double)diff * multiplier);
					event.setFoodLevel(modFoodLevel);
				}
				//else if (diff < 0){
					// Losing fullness
					
					/*// If the player is going to run out of food
					if(event.getFoodLevel()<1) {
						event.setFoodLevel(1);
					}
					else {
						
					}*/
				//}
			}
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof Wolf) {
			Wolf wolf = (Wolf)entity;
			if(wolf.isTamed()) {
				System.out.println("###TamedWolf:"+wolf.getEntityId());
			}
		}
		
		/*if(entity instanceof Enderman) {
			event.setCancelled(true);
		}*/
		
		
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
	
	public void onEntityDamageByProjectile(EntityDamageByEntityEvent event) {
		if(RPG._debugV)
    		System.out.println("Entity Damage By Projectile");

		Entity entity = event.getEntity();
		Entity entityDamager = event.getDamager(); // returns the actual thing that hurt the player
		/*if(entityDamager instanceof Player) {
			Player playerDamager = (Player)entityDamager;*/
		if(entityDamager instanceof Arrow) {
			Arrow arrow = (Arrow)entityDamager;
			ProjectileSource projectileSource = arrow.getShooter();
			LivingEntity shooterLivingEntity = null;
			if(projectileSource instanceof LivingEntity)
				shooterLivingEntity = (LivingEntity)projectileSource;
			
			if(shooterLivingEntity == null)
			{
				System.out.println("Null arrow shooter");
				return;
			}
			
			Player playerDamager;
			if(shooterLivingEntity instanceof Player) {
				playerDamager = (Player)shooterLivingEntity;
			}
			else
				return;
			
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
				
				/*if(!RPG._damage) {
					RPG._damage = true;*/
					
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
					double knockbackChance = chanceOfEffect * .5;
					
					if(knockbackChance > Math.random()) {
						//knockback
						Location aLoc = playerDamager.getLocation();
						Location dLoc = entity.getLocation();
						Vector v = new Vector();
						v.setX(dLoc.getX() - aLoc.getX());
						v.setY(dLoc.getY() - aLoc.getY());
						v.setZ(dLoc.getZ() - aLoc.getZ());
						v = v.normalize();
						v = v.multiply(2);
						entity.setVelocity(v);
						if(entity instanceof Player) {
							Player defenderPlayer = (Player)entity;
							defenderPlayer.sendMessage(ChatColor.GREEN+"You have been knocked back!");
							playerDamager.sendMessage(ChatColor.GREEN+defenderPlayer.getName()+" has been knocked back!");
						}
					}
					
					double damage = event.getDamage();					
					
					if(entity instanceof Player && playerDamager instanceof Player) {
						
						if(entity instanceof Player) {
							Player defenderPlayer = (Player)entity;
							PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
							if(defenderPseudoPlayer._stoneSkinTicks > 0)
								damage/=2;
						}
						
						//damage/=2;
						if(damage <= 0)
							damage = 1;
					}
					
					double pierceChance = chanceOfEffect * .25;
					if(pierceChance > Math.random()) {
						if(entity instanceof Player) {
							Player defenderPlayer = (Player)entity;
							PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
							
							Boolean canPierce = false;
							if(defenderPseudoPlayer._noPierceTicks > 0) {
								double pierceChance2 = Math.random();
								if(pierceChance2 < .5f) {
									canPierce = true;
								}
							}
							else
								canPierce = true;
							
							if(canPierce) {
								defenderPlayer.sendMessage(ChatColor.GREEN+"The arrow pierces through your armor!");
								playerDamager.sendMessage(ChatColor.GREEN+"Your arrow pierced through "+defenderPlayer.getName()+"'s armor.");
								defenderPseudoPlayer._noPierceTicks = 60;
								event.setCancelled(true);
								
								Damageable damag = defenderPlayer;
								double resultHealth = damag.getHealth();
								double totalDamage = damage+additionalDamage;
								if(totalDamage > 12)
									totalDamage = 12;
								resultHealth -= totalDamage;
								if(resultHealth < 1)
									resultHealth = 1;
								defenderPlayer.setHealth(resultHealth);
							}
						}
					}
					
					double arrowRetrieveChance = chanceOfEffect * .85;
					if(arrowRetrieveChance > Math.random()) {
						if(_livingEntityArrowHitHashMap.containsKey(entity.getEntityId())) {
							int numArrows = _livingEntityArrowHitHashMap.get(entity.getEntityId());
							_livingEntityArrowHitHashMap.put(entity.getEntityId(), numArrows+1);
						}
						else
							_livingEntityArrowHitHashMap.put(entity.getEntityId(), 1);
					}
					
					event.setDamage(damage+additionalDamage);
					
					if(playerDamager.getItemInHand().getType().equals(Material.BOW))
						Archery.possibleSkillGain(playerDamager, pseudoPlayerDamager, entity);
				//}
			}
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if(RPG._debugV)
    		System.out.println("Entity Death");
		
		Entity entity = event.getEntity();
		
		//event.setDroppedExp(0);
		
		//event.setDroppedExp(event.getDroppedExp() * 2);
		
		if(entity instanceof EnderDragon) {
			//System.out.println("YES");
			//if(_lastToDamageDragonPlayer != null) {
				//System.out.println("YES2");
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.DRAGON_EGG));
				try{
					Player closestPlayer = null;
					double closestDistance = Float.MAX_VALUE;
					Player[] players = Bukkit.getOnlinePlayers();
					for(Player player : players) {
						if(player.getWorld().equals(entity.getWorld())) {
							double distance = Utils.distance(player.getLocation(), entity.getLocation());
							if(closestDistance < closestDistance || closestPlayer == null) {
								closestPlayer = player;
								closestDistance = distance;
							}
						}
					}
					
					if(closestPlayer != null) {
					
						PseudoPlayer killerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(closestPlayer.getName());
						Spell droppedSpell = null;
						
						int numSpells = 8;
						int randInt = (int)Math.floor(Math.random()*numSpells);
						switch(randInt) {
							case 0:
								droppedSpell = new SPL_DRG_Greed();
								break;
							case 1:
								droppedSpell = new SPL_DRG_Sloth();
								break;
							case 2:
								droppedSpell = new SPL_DRG_Envy();
								break;
							case 3:
								droppedSpell = new SPL_DRG_Gluttony();
								break;
							case 4:
								droppedSpell = new SPL_DRG_Wrath();
								break;
							case 5:
								droppedSpell = new SPL_DRG_Lust();
								break;
							case 6:
								droppedSpell = new SPL_DRG_Pride();
								break;
							case 7:
								droppedSpell = new SPL_DRG_Grief();
								break;
						}
						
						int createdScrollId = Database.addScroll(killerPseudoPlayer.getId(), droppedSpell);
						if(createdScrollId != -1) {
							droppedSpell.setId(createdScrollId);
							killerPseudoPlayer.addScroll(droppedSpell);
							Database.updatePlayerByPseudoPlayer(killerPseudoPlayer);
							Output.positiveMessage(closestPlayer, "The Ender Dragon dropped a scroll of "+droppedSpell.getName()+".");
						}
						else System.out.println("ERROR: got -1 from database.addscroll");
					}
					else {System.out.println("ERROR: NULL PLAYER");}
				}
				catch(Exception e) {
					System.out.println("ERROR: TRIED TO GIVE DRAGON THING TO OFFLINE PLAYER + ");
					e.printStackTrace();
				}
			//}
		}
		
		int id = entity.getEntityId();
		if(RPG._summondMobsOwnerHashSet.containsKey(id)) {
			RPG._summondMobsOwnerHashSet.remove(id);
			event.getDrops().clear();
			return;
		}
		
		if(!_recentDeaths.contains(entity)) {
			_recentDeaths.add(entity);
			_recentDeathsTicks = 60;
		}
		
		if(entity instanceof Wolf) {
			Wolf wolf = (Wolf)entity;
			Taming.wolfDied(wolf,  event);
		}
		
		if(_livingEntityArrowHitHashMap.containsKey(entity.getEntityId())) {
			int numArrows = _livingEntityArrowHitHashMap.get(entity.getEntityId());
			for(int i=0; i<numArrows; i++) {
				entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.ARROW, 1));
			}
			_livingEntityArrowHitHashMap.remove(entity.getEntityId());
		}
		
		/*if(entity instanceof Player) {
			Player player = (Player)entity;
			player.setExperience(0);
			player.setTotalExperience(0);
		}*/
			
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
						Damageable damag = lastAttackerPlayer;
						double curHealth = damag.getHealth();
						curHealth += 2;
						if(curHealth > 20)
							curHealth = 20;
						if(curHealth != damag.getHealth()) {
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
					else if(rand < .2 || entity instanceof Giant || entity instanceof EnderDragon) {
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
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER.getId(), 1));
							double arrowRand = (int)Math.floor(Math.random()*3);
							for(int i=0; i<arrowRand; i++) {
								entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER.getId(), 1));
							}
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
							entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER.getId(), 1));
							double arrowRand = (int)Math.floor(Math.random()*3);
							for(int i=0; i<arrowRand; i++) {
								entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER.getId(), 1));
							}
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
						Damageable damag = attackerPlayer;
						double curHealth = damag.getHealth();
						curHealth += 1;
						if(curHealth > 20)
							curHealth = 20;
						if(curHealth != damag.getHealth()) {
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
			System.out.println("PLAYER DEATH: " + player.getName() + " @ " + player.getLocation());
			
			NPCHandler.playerDied(player);
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			if(pseudoPlayer == null) {
				return;
			}
			
			if(pseudoPlayer._claiming) {
            	ArrayList<Plot> controlPoints = PlotHandler.getControlPoints();
            	for(Plot cP : controlPoints) {
            		if(cP.isUnderAttack() && cP._capturingPlayerName.equals(player.getName())) {
            			cP.failCaptureDied(player);
            		}
            	}
            }
			
			pseudoPlayer._bleedTicks = 0;
			pseudoPlayer._goToSpawnTicks = 0;
			pseudoPlayer._pvpTicks = 0;
			pseudoPlayer._respawnTicks = 200;
			pseudoPlayer._dieLog = 0;
			pseudoPlayer._lastChanceTicks = 0;
			
			//pseudoPlayer._clearTicks = 5;
			ArrayList<RecentAttacker> recentAttackers = pseudoPlayer.getRecentAttackers();
			for(RecentAttacker recentAttacker : recentAttackers) {
				if(recentAttacker._notCrim)
					continue;
				
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
							Damageable damag = attackerPlayer;
							double curHealth = damag.getHealth();
							curHealth += 4;
							if(curHealth > 20)
								curHealth = 20;
							if(curHealth != damag.getHealth()) {
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
			
			/*if(pseudoPlayer.isRobot()) {
				// spawn tnt?
				//if(pseudoPlayer._killSelfTicks <= 0) {
					
					Output.chatLocal(player, "*BOOP*", false);
					Location location = player.getLocation();
					RoboExplosion roboExplosion = new RoboExplosion(player, 10);
					RPG._roboExplosions.add(roboExplosion);
					((CraftWorld)location.getWorld()).getHandle().createExplosion(null, location.getX(), location.getY(), location.getZ(), 2, false);
					//player.getWorld().strikeLightning(player.getLocation());
				//}
			}*/
			
			int numAttackers = recentAttackers.size();
			
			if(pseudoPlayer._guardAttackTicks > 0) {
				player.getServer().broadcastMessage(player.getDisplayName()+ChatColor.WHITE+" was executed by an Order guard.");
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
    
    @EventHandler
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
	
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
    	event.setCancelled(true);
		return;
    	
		/*Entity entity = event.getEntity();
		
		if(event.getLocation().getWorld() != RPG._theEndWorld2) {
			if(event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
				event.setCancelled(true);
				return;
			}
		}
		
		// Determine if any plots are within 10 blocks of the explosion
		ArrayList<Plot> plots = PlotHandler.getPlots();
		ArrayList<Plot> plotsWithin = new ArrayList<Plot>();
		for(Plot plot : plots) {
			if(plot.getLocation().getWorld().equals(entity.getWorld())) {
				if(Utils.isWithin(plot.getLocation(), entity.getLocation(), plot.getRadius()+10)) {
					if(plot.isProtected()) {
						//if(!plot.isExplosionAllowed()) {
							event.setCancelled(true);
							return;
						//}
						//plotsWithin.add(plot);
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
		event.setCancelled(true);*/
	}
	
    @EventHandler
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
	
    @EventHandler
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
	
    @EventHandler
	public void onEndermanPlace(EntityChangeBlockEvent event) {
    	Entity entity = event.getEntity();
    	if(entity instanceof Enderman) {
			Plot plot = PlotHandler.findPlotAt(entity.getLocation());
			if(plot != null) {
				if(plot.isProtected()) {
					event.setCancelled(true);
				}
			}
    	}
	}
	
    /*@EventHandler
	public void onEndermanPickup(EndermanPickupEvent event) {
		Block block = event.getBlock();
		Plot plot = PlotHandler.findPlotAt(block.getLocation());
		if(plot != null) {
			if(plot.isProtected()) {
				event.setCancelled(true);
			}
		}
	}*/
	
	public static void criminalAction(Player player, Player playerDamager) {
		if(player.getName().equals(playerDamager.getName()))
			return;
		
		PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		PseudoPlayer pseudoPlayerAttacker = PseudoPlayerHandler.getPseudoPlayer(playerDamager.getName());
		
		// only criminal if it happens in the normal world
		if(true) {//player.getWorld().equals(RPG._normalWorld) || player.getWorld().equals(RPG._newmap)) {
			boolean notCrim = false;
			if(!player.getWorld().equals(RPG._normalWorld)) {
				notCrim = true;
			}
			Plot plot = PlotHandler.findPlotAt(player.getLocation());
			// devender is on a plot
			if(plot != null) {
				if(plot.isControlPoint())
					return;
				// and the attacker is a member of the plot
				if(plot.isMember(playerDamager.getName())) {
					// and the defender is NOT a member of the plot
					return;
					/*if(!plot.isMember(player.getName())) {
						// don't do criminal thing: texas rules
						return;
					}*/
				}
			}
			if(player.getWorld() == RPG._hungryWorld)
				return;
			
			// If the defender has attacked anyone within 30 seconds they engaged in combat willingly
			// so attacking them will not be criminal even if they are blue
			
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
			
			RecentAttacker recentAttacker = new RecentAttacker(playerDamager.getName(), _RECENT_ATTACKER_TIME);
			recentAttacker._notCrim = notCrim;
			pseudoPlayerDefender.addRecentAttacker(recentAttacker);
		}
	}
	
	@EventHandler
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
				else if(targetPlayer.getInventory().getHelmet() != null && targetPlayer.getInventory().getHelmet().getType().equals(Material.PUMPKIN)) {
					if(targetPlayer.isSneaking()) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
    public void onEntityTame(EntityTameEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Wolf) {
			Wolf wolf = (Wolf)entity;
			Player ownerPlayer = (Player)event.getOwner();
			
			if(ownerPlayer == null)
				return;
			
			PseudoPlayer ownerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(ownerPlayer.getName());
			if(ownerPseudoPlayer == null)
				return;
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
	
	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Healing.playerRegainHealth((Player)entity, event);
		}
	}
	
	/*@Override
	public static void onEntityRegainHealth(EntityRegainHealthEvent event) {
		
	}*/
	
	/*@EventHandler
	public void onEntityDamage2(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			Player p = (Player)entity;
			System.out.println(event.getDamage());
		}
	}*/
	
	private Player _lastToDamageDragonPlayer = null;
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {		
		Entity damagedEntity = event.getEntity();
		
		if(damagedEntity instanceof EnderDragon) {
			//System.out.println("DamagedDragon");
			if(event instanceof EntityDamageByEntityEvent) {
				//System.out.println("EntityEvent");
				EntityDamageByEntityEvent eDBEEvent = (EntityDamageByEntityEvent)event;
				Entity entityDamager = eDBEEvent.getDamager();
				if(entityDamager instanceof Player) {
					//System.out.println("DamagerPlayer");
					_lastToDamageDragonPlayer = (Player)entityDamager;
				}
				
			}
		}
		
		if(damagedEntity instanceof LivingEntity) {}
		else {
			return;
		}
		
		LivingEntity livingEntity = (LivingEntity)damagedEntity;
		
		if(damagedEntity instanceof Player) {
			Player damagedPlayer = (Player)damagedEntity;
			PseudoPlayer damagedPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(damagedPlayer.getName());
			if(damagedPseudoPlayer == null) {
				event.setCancelled(true);
				return;
			}
			
			if(damagedPseudoPlayer._slothTicks > 0) {
				event.setCancelled(true);
				return;
			}
			
			if(damagedPseudoPlayer._respawnTicks > 0) {
				event.setCancelled(true);
				return;
			}
			
			if(damagedPseudoPlayer._wrathTicks > 0) {
				if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LIGHTNING)) {
					event.setCancelled(true);
					return;
				}
			}
			
			//Attacking an admin with /convenient on
			if(damagedPseudoPlayer._convenient) {
				event.setCancelled(true);
				return;
			}
			
			if(event.getCause().equals(DamageCause.DROWNING)) {				
				if(damagedPlayer.getInventory().getHelmet() != null && damagedPlayer.getInventory().getHelmet().getType().equals(Material.GOLD_HELMET)) {
					event.setCancelled(true);
					return;
				}
			}
			
			if(event.getCause().equals(DamageCause.FALL)) {
				if(damagedPlayer.getInventory().getBoots() != null && damagedPlayer.getInventory().getBoots().getType().equals(Material.GOLD_BOOTS)) {
					event.setCancelled(true);
					return;
				}
				
				if(damagedPseudoPlayer._moonJumpTicks > 0) {// || damagedPseudoPlayer._fireWalkTicks > 0) {
					event.setCancelled(true);
					return;
				}
			
				if(damagedPseudoPlayer._fireWalkTicks > 0 || damagedPseudoPlayer._noFireTicks > 0) {
					Block blockAt = damagedPlayer.getWorld().getBlockAt(damagedPlayer.getLocation());
					if(blockAt.getTypeId() == 10 || blockAt.getTypeId() == 11) {
						event.setCancelled(true);
						return;
					}
				}
				
				Block b = damagedPlayer.getLocation().getBlock();
				if(b.getRelative(BlockFace.DOWN).getType().equals(Material.WOOL)) {
					event.setCancelled(true);
					return;
				}
				
				/*if(damagedPseudoPlayer._noFireTicks > 0) {
					event.setCancelled(true);
					return;
				}*/
				
				if(b.getType().equals(Material.STATIONARY_LAVA) || b.getType().equals(Material.LAVA)) {
					event.setCancelled(true);
					return;
				}
			}
	
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.LAVA)) {
				if(damagedPlayer.getInventory().getLeggings() != null && damagedPlayer.getInventory().getLeggings().getType().equals(Material.GOLD_LEGGINGS)) {
					event.setCancelled(true);
					return;
				}
				
				if(damagedPseudoPlayer._fireWalkTicks > 0) {
					event.setCancelled(true);
					damagedPseudoPlayer._noFireTicks=10;
					return;
				}
			}
			
			if(event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.FIRE_TICK)) {
				if(damagedPseudoPlayer._noFireTicks > 0) {
					damagedPlayer.setFireTicks(0);
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
								damagedPlayer.setFireTicks(0);
								damagedPseudoPlayer._noFireTicks = 10;
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
			
			if(event.getCause().equals(DamageCause.SUFFOCATION)) {
				if(damagedPlayer.getInventory().getHelmet() != null && damagedPlayer.getInventory().getHelmet().getType().equals(Material.GOLD_HELMET)) {
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
				if(damagedPlayer.getInventory().getChestplate() != null && damagedPlayer.getInventory().getChestplate().getType().equals(Material.GOLD_CHESTPLATE)) {
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
			
			if(damagedPseudoPlayer._stoneSkinTicks > 0) {
				double damage = event.getDamage();
				damage/=2;
				event.setDamage(damage);
			}
			
			if(!(event.getCause().equals(DamageCause.PROJECTILE))) {
				if(damagedPlayer.getNoDamageTicks() <= 0) {
					if(damagedPseudoPlayer._activeBandage != null) {
						damagedPseudoPlayer._activeBandage.bandagerHit(damagedPlayer);
					}
				}
			}
		}
		
		if(damagedEntity instanceof Wolf) {
			if(event instanceof EntityDamageByEntityEvent)
				Taming.wolfDamagedByEntity((Wolf)damagedEntity, (EntityDamageByEntityEvent)event);
			else
				Taming.wolfDamaged((Wolf)damagedEntity, (EntityDamageEvent) event);
		}
		
		if(event instanceof EntityDamageByBlockEvent) {
			onEntityDamageByBlock((EntityDamageByBlockEvent)event);
		}
		
		/*if(livingEntity.getNoDamageTicks() > 0) {
			return;
		}*/
		if(event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent)event;
			Entity damagingEntity = entityDamageByEntityEvent.getDamager();
			
			if(damagingEntity instanceof Player) {							
				_lastAttackers.put(damagedEntity, damagingEntity);
					
				Player damagingPlayer = (Player)damagingEntity;
				PseudoPlayer damagingPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(damagingPlayer.getName());
				
				
				Plot plot = PlotHandler.findPlotAt(damagingPlayer.getLocation());
				if(plot != null) {
					if(plot.isNoPvPPlot()) {
						event.setCancelled(true);
						Output.positiveMessage(damagingPlayer, "You cannot engage in PvP in "+plot.getName()+".");
						return;
					}
				}
				
				/*if(damagingPlayer.getItemInHand().getType().equals(Material.WOOL)) {
					Healing.playerBandageEntity(damagingPlayer, damagedEntity);
					event.setCancelled(true);
					return;
				}*/
					
				// If PvP
				if(damagingEntity instanceof Player && damagedEntity instanceof Player) {
					Player damagedPlayer = (Player)damagedEntity;
					
					if(damagingPlayer.getItemInHand().getType().equals(Material.FEATHER)) {
						damagingPlayer.sendMessage(ChatColor.GRAY+"You lightly tickle "+damagedPlayer.getName()+" with a feather.");
						damagedPlayer.sendMessage(ChatColor.GRAY+damagingPlayer.getName()+" lightly tickles you with a feather.");
						event.setCancelled(true);
						return;
					}
					
					if(canPlayerDamagePlayer(damagingPlayer, damagedPlayer)) {
						criminalAction(damagedPlayer, damagingPlayer);
					}
					else {
						event.setCancelled(true);
						return;
					}
								
					PseudoPlayer damagedPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(damagedPlayer.getName());
					if(damagedPseudoPlayer._lastAttackTicks <= 0) {
						damagedPseudoPlayer._lastAttackerName = damagingPlayer.getName();
						damagedPseudoPlayer._lastAttackTicks = 100;
						damagedPlayer.sendMessage(ChatColor.GRAY+damagingPlayer.getName()+" damaged you.");
					}
				}
				
				if(damagedEntity instanceof LivingEntity) {
					LivingEntity damagedLivingEntity = (LivingEntity)damagedEntity;
					ArrayList<PseudoWolf> tamedWolves = damagingPseudoPlayer.getTamedWolves();
					for(PseudoWolf pW : tamedWolves) {
						Wolf wolf = Taming.pseudoWolfToWolf(pW);
						if(wolf != null) {
							if(!damagedLivingEntity.equals(wolf) && !damagedLivingEntity.equals(wolf.getOwner())) {
								wolf.setTarget(damagedLivingEntity);
							}
						}
					}
				}
				
				if(damagingPseudoPlayer._loggedInRecentlyTicks > 0) {
					event.setCancelled(true);
					return;
				}
			}
		}
		
		/*if(!(livingEntity instanceof Player))
			return;*/
		if(livingEntity.getNoDamageTicks() <= 10) {
			//System.out.println("DO");
			if(true) {//!RPG._damage) {
				RPG._damage = true;
				if(event.getCause().equals(DamageCause.PROJECTILE)) {
					onEntityDamageByProjectile((EntityDamageByEntityEvent)event);//(EntityDamageByProjectileEvent)event);
				}
				else if(event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
					EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent)event;
					Entity damagingEntity = entityDamageByEntityEvent.getDamager();
					
					if(damagingEntity instanceof Player) {	
						//System.out.println("PA");
						_lastAttackers.put(damagedEntity, damagingEntity);
						
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
					/*else if(damagingEntity instanceof Blaze) {
					}*/
					else if(damagingEntity instanceof Fireball) {
						if(damagedEntity instanceof Player) {
							Player damagedPlayer = (Player)event.getEntity();
							Fireball fb = (Fireball)damagingEntity;
							if(fb.getShooter() instanceof Player) {
								Player damagingPlayer = (Player)fb.getShooter();
								if(canPlayerDamagePlayer(damagingPlayer, damagedPlayer)) {
									criminalAction(damagedPlayer, damagingPlayer);
								}
							}
						}
					}
					/*else if(damagingEntity instanceof SmallFireball) {
						System.out.println("SMFB");
						if(damagedEntity instanceof Player) {
							Player damagedPlayer = (Player)event.getEntity();
							Fireball fb = (Fireball)damagingEntity;
							if(fb.getShooter() instanceof Blaze) {
								System.out.println("Blaze Attack");
								int mobID = damagingEntity.getEntityId();
								if(RPG._summondMobsOwnerHashSet.containsKey(mobID)) {
									System.out.println("Summoned Blaze Attack");
									String ownerName = RPG._summondMobsOwnerHashSet.get(mobID);
									Player damagingPlayer = Bukkit.getPlayer(ownerName);
									if(damagedPlayer != damagingPlayer) {
										System.out.println("Summoned Blaze Attacked Not Owner");
										if(damagingPlayer != null) {
											System.out.println("Owner Not Null");
											if(canPlayerDamagePlayer(damagingPlayer, damagedPlayer)) {
												System.out.println("Attacked Someone That Can Be Damaged");
												criminalAction(damagedPlayer, damagingPlayer);
											}
										}
									}
								}
							}
						}
					}*/
				}
			}
			else return;
		}
		
		/*if(damagedEntity instanceof Player && event.getDamage() > 0) {
			int modDamage = 0;
			Player damagedPlayer = (Player)damagedEntity;
			ItemStack helmet = damagedPlayer.getInventory().getHelmet();
			ItemStack chestplate = damagedPlayer.getInventory().getChestplate();
			ItemStack leggings = damagedPlayer.getInventory().getLeggings();
			ItemStack boots = damagedPlayer.getInventory().getBoots();
			
			if(helmet.getType().equals(Material.CHAINMAIL_HELMET))
				modDamage+=1;
			else if(helmet.getType().equals(Material.IRON_HELMET))
				modDamage+=2;
			else if(helmet.getType().equals(Material.DIAMOND_HELMET))
				modDamage+=3;
			
			if(chestplate.getType().equals(Material.CHAINMAIL_CHESTPLATE))
				modDamage+=1;
			else if(chestplate.getType().equals(Material.IRON_CHESTPLATE))
				modDamage+=2;
			else if(chestplate.getType().equals(Material.DIAMOND_CHESTPLATE))
				modDamage+=3;
			
			if(leggings.getType().equals(Material.CHAINMAIL_LEGGINGS))
				modDamage+=1;
			else if(leggings.getType().equals(Material.IRON_LEGGINGS))
				modDamage+=2;
			else if(leggings.getType().equals(Material.DIAMOND_LEGGINGS))
				modDamage+=3;
			
			if(boots.getType().equals(Material.CHAINMAIL_BOOTS))
				modDamage+=1;
			else if(boots.getType().equals(Material.IRON_BOOTS))
				modDamage+=2;
			else if(boots.getType().equals(Material.DIAMOND_BOOTS))
				modDamage+=3;
			
			modDamage/=4;
			int damage = event.getDamage();
			damage -= modDamage;
			if(damage < 1)
				damage = 1;
			event.setDamage(damage);
		}*/
		
		// Defense skill - works
		/*if(entity instanceof Player) {
			Player player = (Player)entity;
			Defense.playerDamaged(player, event);
		}*/
		
		//System.out.println("Ending Damage:"+event.getDamage());
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
