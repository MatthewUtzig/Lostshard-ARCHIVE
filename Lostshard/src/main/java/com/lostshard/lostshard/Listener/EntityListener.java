package com.lostshard.lostshard.Listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
 
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Handlers.RankHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.lostshard.Skills.BladesSkill;
import com.lostshard.lostshard.Skills.BrawlingSkill;
import com.lostshard.lostshard.Skills.LumberjackingSkill;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Utils;

public class EntityListener implements Listener {

	public HashMap<Entity, Entity> lastAttackers = new HashMap<Entity, Entity>();
	public HashSet<Entity> recentDeath = new HashSet<Entity>();
	
	public EntityListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamagedByEntityLow(EntityDamageByEntityEvent event) {
		BladesSkill.playerDamagedEntityWithSword(event);
		LumberjackingSkill.playerDamagedEntityWithAxe(event);
		BrawlingSkill.playerDamagedEntityWithMisc(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Random random = new Random();
		Entity entity = event.getEntity();
		
		//event.setDroppedExp(0);
		
		//event.setDroppedExp(event.getDroppedExp() * 2);
		
		//Start of skull drop
		
		if(entity instanceof Player && ((Player) entity).getKiller() instanceof Player) {
			
			Player player = (Player) entity;
			Player killer = (Player) player.getKiller();
			
			PseudoPlayer pKiller = PseudoPlayerHandler.getPlayer(killer);
			
			Material wep = killer.getItemInHand().getType();
			
			if(pKiller != null && ItemUtils.isAxe(wep) || ItemUtils.isSword(wep)) {
				int swordsSkill = pKiller.getCurrentBuild().getBlade().getLvl();
				int lumberjackingSkill = pKiller.getCurrentBuild().getLumberjacking().getLvl();
				
				if(swordsSkill >= 1000 || lumberjackingSkill >= 1000) {
					ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1 , (short) SkullType.PLAYER.ordinal());
					SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
					skullMeta.setOwner(player.getName());
			        skullMeta.setDisplayName(ChatColor.RESET + player.getName() + "'s Head");
					skull.setItemMeta(skullMeta);
					event.getDrops().add(skull);
				}
			}
		}
		
		//End of skull drop
		
		//Start of Horse
		
		if(entity instanceof Horse){
			event.getDrops().clear();
		}
		
		//End of Horse
		
//		if(entity instanceof EnderDragon) {
//			//System.out.println("YES");
//			//if(_lastToDamageDragonPlayer != null) {
//				//System.out.println("YES2");
//				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.DRAGON_EGG));
//				try{
//					Player closestPlayer = null;
//					double closestDistance = Float.MAX_VALUE;
//					Player[] players = Bukkit.getOnlinePlayers();
//					for(Player player : players) {
//						if(player.getWorld().equals(entity.getWorld())) {
//							double distance = Utils.distance(player.getLocation(), entity.getLocation());
//							if(closestDistance < closestDistance || closestPlayer == null) {
//								closestPlayer = player;
//								closestDistance = distance;
//							}
//						}
//					}
					
//					if(closestPlayer != null) {
//					
//						PseudoPlayer killerPseudoPlayer = PseudoPlayerHandler.getPlayer(closestPlayer);
//						Spell droppedSpell = null;
						
//						int numSpells = 8;
//						int randInt = (int)Math.floor(Math.random()*numSpells);
//						switch(randInt) {
//							case 0:
//								droppedSpell = new SPL_DRG_Greed();
//								break;
//							case 1:
//								droppedSpell = new SPL_DRG_Sloth();
//								break;
//							case 2:
//								droppedSpell = new SPL_DRG_Envy();
//								break;
//							case 3:
//								droppedSpell = new SPL_DRG_Gluttony();
//								break;
//							case 4:
//								droppedSpell = new SPL_DRG_Wrath();
//								break;
//							case 5:
//								droppedSpell = new SPL_DRG_Lust();
//								break;
//							case 6:
//								droppedSpell = new SPL_DRG_Pride();
//								break;
//							case 7:
//								droppedSpell = new SPL_DRG_Grief();
//								break;
//						}
						
//						int createdScrollId = Database.addScroll(killerPseudoPlayer.getId(), droppedSpell);
//						if(createdScrollId != -1) {
//							droppedSpell.setId(createdScrollId);
//							killerPseudoPlayer.addScroll(droppedSpell);
//							Database.updatePlayerByPseudoPlayer(killerPseudoPlayer);
//							Output.positiveMessage(closestPlayer, "The Ender Dragon dropped a scroll of "+droppedSpell.getName()+".");
//						}
//						else System.out.println("ERROR: got -1 from database.addscroll");
//					}
//					else {System.out.println("ERROR: NULL PLAYER");}
//				}
//				catch(Exception e) {
//					System.out.println("ERROR: TRIED TO GIVE DRAGON THING TO OFFLINE PLAYER + ");
//					e.printStackTrace();
//				}
//			}
//		}
		
//		int id = entity.getEntityId();
//		if(RPG._summondMobsOwnerHashSet.containsKey(id)) {
//			RPG._summondMobsOwnerHashSet.remove(id);
//			event.getDrops().clear();
//			return;
//		}
//		
//		if(!_recentDeaths.contains(entity)) {
//			_recentDeaths.add(entity);
//			_recentDeathsTicks = 60;
//		}
//		
//		if(entity instanceof Wolf) {
//			Wolf wolf = (Wolf)entity;
//			Taming.wolfDied(wolf,  event);
//		}
//		
//		if(_livingEntityArrowHitHashMap.containsKey(entity.getEntityId())) {
//			int numArrows = _livingEntityArrowHitHashMap.get(entity.getEntityId());
//			for(int i=0; i<numArrows; i++) {
//				entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.ARROW, 1));
//			}
//			_livingEntityArrowHitHashMap.remove(entity.getEntityId());
//		}
			
//		if(entity instanceof Monster || entity instanceof Giant || entity instanceof Ghast || entity instanceof PigZombie) {
//			if(RPG._worldEvent != null && RPG._worldEvent instanceof GhastAttackEvent && entity instanceof Ghast) {
//				GhastAttackEvent gae = (GhastAttackEvent)RPG._worldEvent;
//				ArrayList<Entity> _ghasts = gae.getGhasts();
//				if(_ghasts.contains((LivingEntity)entity)) {
//					int randInt = (int)Math.floor(Math.random()*13);
//					if(randInt == 0) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_SWORD.getId(), 1));
//					else if(randInt == 1) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_HOE.getId(), 1));
//					else if(randInt == 2) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_PICKAXE.getId(), 1));
//					else if(randInt == 3) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_AXE.getId(), 1));
//					else if(randInt == 4) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_SPADE.getId(), 1));
//					else if(randInt == 5) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.WATCH.getId(), 1));
//					else if(randInt == 6) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_INGOT.getId(), 1));
//					else if(randInt == 7) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.POWERED_RAIL.getId(), 3));
//					else if(randInt == 8) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLDEN_APPLE.getId(), 3));
//					else if(randInt == 9)
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_HELMET.getId(), 1));
//					else if(randInt == 10) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_CHESTPLATE.getId(), 1));
//					else if(randInt == 11) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_LEGGINGS.getId(), 1));
//					else if(randInt == 12) 
//						entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.GOLD_BOOTS.getId(), 1));
//				}
//			}
			// monster died
			// hit by an entity recently
//			if(lastAttackers.containsKey(entity)) {
//				Entity lastAttackerEntity = lastAttackers.get(entity);
//				if(lastAttackerEntity == null)
//					return;
//				if(lastAttackerEntity instanceof Player) {
//					Player lastAttackerPlayer = (Player)lastAttackerEntity;
//					PseudoPlayer pseudoPlayerDamager = PseudoPlayerHandler.getPlayer(lastAttackerPlayer);
//					Survivalism.possibleSkillGain(lastAttackerPlayer, pseudoPlayerDamager);
//					double rand = Math.random();
//					if(entity instanceof Ghast) {
//						if(Math.random() < .2) {
//							Spell droppedScroll;
//							rand = Math.random();
//							if(rand < .2)
//								droppedScroll = new SPL_PermanentGateTravel();
//							else {
//								Random random = new Random();
//								int randInt = random.nextInt(4);
//								if(randInt == 0)
//									droppedScroll = new SPL_Day();
//								else if(randInt == 1)
//									droppedScroll = new SPL_ClearSky();
//								else if(randInt == 2)
//									droppedScroll = new SPL_MoonJump();
//								else if(randInt == 3)
//									droppedScroll = new SPL_SummonMonster();
//								else
//									droppedScroll = new SPL_MoonJump();
//							}
//							
//							int createdScrollId = Database.addScroll(pseudoPlayerDamager.getId(), droppedScroll);
//							if(createdScrollId != -1) {
//								pseudoPlayerDamager.addScroll(droppedScroll);
//								Database.updatePlayerByPseudoPlayer(pseudoPlayerDamager);
//								Output.positiveMessage(lastAttackerPlayer, "The Ghast dropped a scroll of "+droppedScroll.getName()+".");
//							}
//							else System.out.println("ERROR: got -1 from database.addscroll");
//						}
//					}
//					else if(rand < .2 || entity instanceof Giant || entity instanceof EnderDragon) {
//						// 10% chance of dropping a spell
//						String monsterName = MonsterHandler.getMonsterName(entity);
//						Monster monster = (Monster)entity;
//						Spell droppedSpell = MonsterHandler.getDroppedSpell(monster);
//						if(droppedSpell == null) {
//							System.out.println("dropped null spell");
//						}
//						else {
//							//Spellbook spellbook = pseudoPlayerDamager.getSpellbook();
//							int createdScrollId = Database.addScroll(pseudoPlayerDamager.getId(), droppedSpell);
//							if(createdScrollId != -1) {
//								droppedSpell.setId(createdScrollId);
//								pseudoPlayerDamager.addScroll(droppedSpell);
//								Database.updatePlayerByPseudoPlayer(pseudoPlayerDamager);
//								Output.positiveMessage(lastAttackerPlayer, "The "+monsterName+" dropped a scroll of "+droppedSpell.getName()+".");
//							}
//							else System.out.println("ERROR: got -1 from database.addscroll");
//						}
//					}
					
					/*if(entity instanceof PigZombie) {
						if(Math.random() < .02) {
							if(
						}
					}*/
	
		//Start of extra drops
		if(entity instanceof Monster)
			if(entity instanceof Zombie)
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER, random.nextInt(4)+1));
			else if(entity instanceof Skeleton) {
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.BONE, 1));
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.ARROW, random.nextInt(7)+1));
			}
			else if(entity instanceof Spider)
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.STRING, 1));
			else if(entity instanceof Creeper)
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.SULPHUR, 1));
		else if(entity instanceof Animals)
			if(entity instanceof Pig)
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.PORK, 1));
			else if(entity instanceof Chicken) {
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.FEATHER, random.nextInt(5)+1));
			}
			else if(entity instanceof Cow)
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.LEATHER, 2));
			else if(entity instanceof Sheep)
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.WOOL, 1));
			else if(entity instanceof Squid)
				entity.getLocation().getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.INK_SACK, 1));
		
		//Survivalism
		
		if(entity instanceof Animals) {
			if(lastAttackers.containsKey(entity)) {
				Entity lastAttackerEntity = lastAttackers.get(entity);
				if(lastAttackerEntity instanceof Player) {
					Player attackerPlayer = (Player)lastAttackerEntity;
					PseudoPlayer pseudoPlayerAttacker = PseudoPlayerHandler.getPlayer(attackerPlayer);
					
					int survSkill = pseudoPlayerAttacker.getCurrentBuild().getSurvivalism().getLvl();
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
//					Survivalism.possibleSkillGain(attackerPlayer, pseudoPlayerAttacker);
				}
				
				// do stuff
				///lastAttackers.remove(entity);
			}
		}
		
		//End of survivalism
		
		if(entity instanceof Player) {
			Player player = (Player)entity;
			System.out.println("PLAYER DEATH: " + player.getName() + " @ " + player.getLocation());
			
//			NPCHandler.playerDied(player);
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
			if(pseudoPlayer == null) {
				return;
			}
			
//			if(pseudoPlayer._claiming) {
//            	ArrayList<Plot> controlPoints = PlotHandler.getControlPoints();
//            	for(Plot cP : controlPoints) {
//            		if(cP.isUnderAttack() && cP._capturingPlayerName.equals(player.getName())) {
//            			cP.failCaptureDied(player);
//            		}
//            	}
//            }
			
			pseudoPlayer.setBleedTick(0);
//			pseudoPlayer._goToSpawnTicks = 0;
			pseudoPlayer.setPvpTicks(0);
//			pseudoPlayer._respawnTicks = 200;
//			pseudoPlayer._dieLog = 0;
//			pseudoPlayer._lastChanceTicks = 0;
			
			if(player.getVehicle() instanceof Horse)
				((Horse) player.getVehicle()).setHealth(0d);
			
			//pseudoPlayer._clearTicks = 5;
			ArrayList<RecentAttacker> recentAttackers = pseudoPlayer.getRecentAttackers();
			
			//Rank stuff
			
			RankHandler.rank(pseudoPlayer);
			
			pseudoPlayer.setLastDeath(new Date().getTime());
			
			for(RecentAttacker recentAttacker : recentAttackers) {	
				if(recentAttacker.isNotCrime())
					continue;
				
				UUID attackerUUID = recentAttacker.getUUID();
				Player attackerPlayer = Bukkit.getPlayer(attackerUUID);

				if(attackerPlayer != null) {
					PseudoPlayer attackerPseudo = PseudoPlayerHandler.getPlayer(attackerPlayer);
//					Survivalism.possibleSkillGain(attackerPlayer, attackerPseudo);
					if(!(attackerPseudo.isCriminal() && !pseudoPlayer.isCriminal()))
					continue;
					
					attackerPlayer.sendMessage("You have murdered "+player.getName());
					if(attackerPseudo != null) {
						
						attackerPseudo.setMurderCounts(attackerPseudo.getMurderCounts()+1);
//						Database.updatePlayer(attackerPlayer.getName());
						if(attackerPseudo.isMurderer()) {
							attackerPlayer.setDisplayName(ChatColor.RED+attackerPlayer.getName());
//							SimpleScoreBoard.updatePlayers();
						}
					}
				}
				else {
					try {
//						PseudoPlayer attackerPseudo = Database.createPseudoPlayer(attackerName);
//						attackerPseudo.setMurderCounts(attackerPseudo.getMurderCounts()+1);
//						Database.updatePlayerByPseudoPlayer(attackerPseudo);
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
			
			//Start of death messages
			
			if(((LivingEntity) player).getLastDamage() >= 200d)
				Bukkit.broadcastMessage(player.getDisplayName()+ChatColor.WHITE+" was executed by an Order guard.");
			else if(player.getLastDamageCause().equals(DamageCause.SUICIDE)) {
				String message = player.getDisplayName()+ChatColor.WHITE;
				int randInt = random.nextInt(5);
				
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
					Player p = Bukkit.getPlayer(recentAttackers.get(i).getUUID());
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
				Player[] onlinePlayers = Bukkit.getOnlinePlayers();
				int numOnlinePlayers = onlinePlayers.length;
				for(int i=0; i<numOnlinePlayers; i++) {
					Player p = onlinePlayers[i];
					p.sendMessage(deathMessage);
				}
			} else {
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
					if(e instanceof EntityDamageByEntityEvent) {
						EntityDamageByEntityEvent eDBEE = (EntityDamageByEntityEvent)e;
						Entity damager = eDBEE.getDamager();
						if(damager instanceof Creeper)
							message+= " was killed by a Creeper.";
						if(damager instanceof PigZombie)
							message+= " was killed by a Zombie Pigman.";
						else if(damager instanceof Zombie)
							message+= " was killed by a Zombie.";
						else if(damager instanceof Skeleton)
							message+= " was killed by a Skeleton.";
						else if(damager instanceof Ghast)
							message+= " was killed by a Ghast.";
						else if(damager instanceof Giant)
							message+= " was killed by a huge fucking Zombie.";
						else if(damager instanceof Slime)
							message+= " was killed by a Slime.";
						else if(damager instanceof Spider)
							message+= " was killed by a Spider.";
						else if(damager instanceof Witch)
							message+= " was killed by a Witch";
						else if(damager instanceof Silverfish)
							message+= " was killed by a Silverfish";
						else if(damager instanceof IronGolem)
							message+= " was killed by a Iron Golem";
						else if(damager instanceof Wither)
							message+= " was killed by a Wither";
						else if(damager instanceof Enderman)
							message+= " was killed by a Enderman";
						else if(damager instanceof Blaze)
							message+= " was killed by a Blaze";
						else if(damager instanceof CaveSpider)
							message+= " was killed by a Cave Spider";
						else if(damager instanceof MagmaCube)
							message+= " was killed by a Magma Cube";
						else if(damager instanceof Endermite)
							message+= " was killed by a Endermite";
						else if(damager instanceof Guardian)
							message+= " was killed by a Guardian";
						else
							message+= "'s life was tragically cut short.";
					}
				}
				else if(e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
					message += " exploded.";
				}
				else message += " died.";
				
				Player[] onlinePlayers = Bukkit.getOnlinePlayers();
				int numOnlinePlayers = onlinePlayers.length;
				for(int i=0; i<numOnlinePlayers; i++) {
					Player p = onlinePlayers[i];
					p.sendMessage(message);
				}
			}
			pseudoPlayer.clearRecentAttackers();
		}
		lastAttackers.remove(entity);
	}
	
}
