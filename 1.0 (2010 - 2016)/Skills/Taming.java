package com.lostshard.RPG.Skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.PseudoWolf;
import com.lostshard.RPG.PseudoWolfHandler;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Taming extends Skill{	
	private static final int CALL_PETS_STAMINA_COST = 20;
	public static void handleCommand(Player player, PseudoPlayer pseudoPlayer, String[] split) {
		if(split.length > 1) {
			String subCommand = split[1];
			if(subCommand.equalsIgnoreCase("call")) {
				Taming.callPets(player, pseudoPlayer);
			}
			else if(subCommand.equalsIgnoreCase("release")) {
				if(split.length == 2) {
					Output.simpleError(player, "Use /pets release (number) to release a pet");
					return;
				}
				else {
					int petNumber = -1;
					try {
						petNumber = Integer.parseInt(split[2]);
					}
					catch(Exception e){}
					if(petNumber <= 0 || petNumber > pseudoPlayer.getTamedWolves().size()) {
						Output.simpleError(player, "Invalid Pet Number, use /pets to see pet numbers.");
						return;
					}
					
					PseudoWolf pseudoWolf = pseudoPlayer.getTamedWolves().get(petNumber-1);
					Wolf wolf = pseudoWolfToWolf(pseudoWolf);
					if(wolf != null) {
						wolf.setTamed(false);
						wolf.setOwner(null);
						wolf.setSitting(false);
						pseudoPlayer.getTamedWolves().remove(pseudoWolf);
						PseudoWolfHandler.remove(pseudoWolf.getUniqueId());
						Database.removePseudoWolf(pseudoWolf);
						Database.updatePseudoPlayerTamedWolves(pseudoPlayer);
						Output.positiveMessage(player, "You have released "+pseudoWolf.getName()+".");
					}
					else {
						pseudoPlayer.getTamedWolves().remove(pseudoWolf);
						PseudoWolfHandler.remove(pseudoWolf.getUniqueId());
						Database.removePseudoWolf(pseudoWolf);
						Database.updatePseudoPlayerTamedWolves(pseudoPlayer);
						Output.positiveMessage(player, "You have released "+pseudoWolf.getName()+".");
					}
				}
			}
		}
		else {
			ArrayList<PseudoWolf> tamedWolves = pseudoPlayer.getTamedWolves();
			int tamingSkill = pseudoPlayer.getSkill("taming");
			int allowedAmount = 1;
			if(tamingSkill >= 1000)
				allowedAmount=3;
			else if(tamingSkill >= 500)
				allowedAmount=2;
			
			int num = tamedWolves.size();
			if(num < allowedAmount)
				num = allowedAmount;
			
			Output.positiveMessage(player, "- "+player.getName()+"'s Pets -");
			for(int i=0; i<num; i++) {
				if(i >= tamedWolves.size()) {
					player.sendMessage(ChatColor.YELLOW+""+(i+1)+": "+ChatColor.RED+"NONE");
				}
				else {
					PseudoWolf pseudoWolf = tamedWolves.get(i);
					player.sendMessage(ChatColor.YELLOW+""+(i+1)+": "+pseudoWolf.getName());
				}
			}
			player.sendMessage(ChatColor.YELLOW+" You currently have "+tamedWolves.size()+" out of "+allowedAmount+" tamed wolves.");
		}
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer) {
		if(pseudoPlayer.isSkillLocked("taming"))
			return;
		
		skillGain(player, pseudoPlayer, .2, "taming", "Taming");
	}
	
	public static boolean isOwnerOnline(Wolf wolf) {
		Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p instanceof AnimalTamer) {
				AnimalTamer animalTamer = (AnimalTamer)p;
				if(wolf.getOwner() == animalTamer)
					return true;
			}
		}
		return false;
	}
	
	public static Player getOwner(Entity tamedEntity) {
		if(tamedEntity instanceof Wolf) {
			Wolf wolf = (Wolf)tamedEntity;
			if(wolf.getOwner() instanceof Player) {
				Player player = (Player)wolf.getOwner();
				return player;
			}
		}
		
		return null;
		/*
		if(tamedEntity instanceof Wolf) {
			Wolf wolf = (Wolf)tamedEntity;
			Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
			for(Player p : onlinePlayers) {
				if(p instanceof AnimalTamer) {
					AnimalTamer animalTamer = (AnimalTamer)p;
					if(wolf.getOwner() == animalTamer)
						return p;
				}
			}
		}
		return null;*/
	}
	
	public static Wolf pseudoWolfToWolf(PseudoWolf pseudoWolf) {
		List<World> worlds = Utils.getPlugin().getServer().getWorlds();
    	for(World w : worlds) {
    		List<LivingEntity> livingEntities = w.getLivingEntities();
    		for(LivingEntity livingEntity : livingEntities) {
    			if(livingEntity instanceof Wolf) {
    				Wolf wolf = (Wolf)livingEntity;
    				if(wolf.isTamed()) {
    					if(wolf.getUniqueId().toString().equals(pseudoWolf.getUniqueId()))
    						return wolf;
    				}
    			}
    		}
    	}
		
		return null;
	}
	
	public static void callPets(Player player, PseudoPlayer pseudoPlayer) {
		/*if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
			Output.simpleError(player, "You cannot summon your pets during or shortly after combat.");
			return;
		}*/
		if(pseudoPlayer.getStamina() >= CALL_PETS_STAMINA_COST) {
			ArrayList<PseudoWolf> tamedWolves = pseudoPlayer.getTamedWolves();
			if(tamedWolves.size() <= 0) {
				Output.simpleError(player, "You do not have any pets you can summon.");
				return;
			}
			pseudoPlayer.setStamina(pseudoPlayer.getStamina()-CALL_PETS_STAMINA_COST);
			
			int tamingSkill = pseudoPlayer.getSkill("taming");
			int allowedAmount = 1;
			if(tamingSkill >= 1000)
				allowedAmount=3;
			else if(tamingSkill >= 500)
				allowedAmount=2;
			
			int numTamedWolves = tamedWolves.size();
			
			if(numTamedWolves > allowedAmount) {
				Output.simpleError(player, "You are only allowed "+allowedAmount+" pets, you must release "+(numTamedWolves - allowedAmount)+" pets before you can use /pets call.");
				return;
			}

	    	for(PseudoWolf pW : tamedWolves) {
	    		Wolf wolf = Taming.pseudoWolfToWolf(pW);
	    		if(wolf != null) {
	    			wolf.setTamed(false);
					wolf.damage(2000);
					System.out.println("Killed old wolf");
	    		}
	    		
	    		String oldUniqueId = pW.getUniqueId();
	    		wolf = (Wolf)player.getWorld().spawnCreature(player.getLocation(), CreatureType.WOLF);
	    		wolf.setTamed(true);
	    		wolf.setOwner((AnimalTamer)player);
	    		pW.setUniqueId(wolf.getUniqueId().toString());
	    			
	    		PseudoWolfHandler.updateHashMap(oldUniqueId, pW.getUniqueId(), pW);
	    			
	    		Database.updatePseudoWolf(pW);
	    		System.out.println("Made a new wolf");
	    		//wolf.teleport(player);
	    	}
	    	Output.positiveMessage(player, "You have summoned your pets.");
		}
		else Output.simpleError(player,  "You do not have enough stamina to summon your pets.");
	}
	
	public static void wolfDamagingEntity(Wolf wolf, EntityDamageByEntityEvent event) {
		PseudoWolf pseudoWolf = PseudoWolfHandler.getPseudoWolf(wolf.getUniqueId().toString());
		if(pseudoWolf != null) {
			if(true) {//Math.random() < .2) {
				AnimalTamer animalTamer = wolf.getOwner();
				Player ownerPlayer = null;
				if(animalTamer instanceof Player) {
					ownerPlayer = (Player)animalTamer;
				}
				if(ownerPlayer != null) {
					PseudoPlayer ownerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(ownerPlayer.getName());
					if(ownerPseudoPlayer != null) {
						Taming.possibleSkillGain(ownerPlayer, ownerPseudoPlayer);
					}
					if(ownerPseudoPlayer.getSkill("taming") >= 1000) {
						Entity damagedEntity = event.getEntity();
						if(damagedEntity instanceof Player) {
							Player damagedPlayer = (Player)damagedEntity;
							Damageable damag = damagedPlayer;
							if(damag.getHealth() > 1)
								damagedPlayer.setHealth(damag.getHealth() - 1);
						}
					}
				}
			}
		}
	}
	
	public static void wolfDamagedByEntity(Wolf wolf, EntityDamageByEntityEvent event) {
		if(!wolf.isTamed())
			return;
		
		PseudoWolf pseudoWolf = PseudoWolfHandler.getPseudoWolf(wolf);
		if(pseudoWolf == null)
			return;
	}
	
	public static void wolfDamaged(Wolf wolf, EntityDamageEvent event) {
		if(!wolf.isTamed())
			return;
		
		PseudoWolf pseudoWolf = PseudoWolfHandler.getPseudoWolf(wolf);
		if(pseudoWolf == null)
			return;
		
		double damage = event.getDamage();
		event.setDamage(damage/2);
	}
	
	public static void wolfDied(Wolf wolf, EntityDeathEvent event) {
		if(!wolf.isTamed())
			return;
		
		PseudoWolf pseudoWolf = PseudoWolfHandler.getPseudoWolf(wolf);
		if(pseudoWolf == null)
			return;
	}
}
