package com.lostshard.RPG.Skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Listeners.RPGEntityListener;
import com.lostshard.RPG.Spells.*;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Lumberjacking extends Skill{
	private static final int ATTACK_STAM_COST = 15;
	
	public static void handleCommand(Player player, String[] split) {
	}
	
	public static void notEnoughStamina(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough stamina to use "+spell.getName()+".");
	}
	
	public static void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast "+spell.getName()+".");
	}
	
	public static int getAttackStamCost() {
		return ATTACK_STAM_COST;
	}
	
	public static void playerDamagedEntityWithAxe(Player player, Entity damagedEntity, EntityDamageByEntityEvent event) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		int lumberjackingSkill = pseudoPlayer.getSkill("lumberjacking");
		double damage = event.getDamage();
		int additionalDamage = 0;
		
		/*if(pseudoPlayer.getStamina() >= ATTACK_STAM_COST) {
			pseudoPlayer.setStamina(pseudoPlayer.getStamina()-ATTACK_STAM_COST);*/
			if(lumberjackingSkill >= 1000)
				additionalDamage = 4;
			else if(lumberjackingSkill >= 750)
				additionalDamage = 3;
			else if(lumberjackingSkill >= 500)
				additionalDamage = 2;
			else if(lumberjackingSkill >= 250)
				additionalDamage = 1;
		/*}
		else {
			if(!pseudoPlayer._ljExausted) {
				player.sendMessage(ChatColor.GRAY+"You are exausted and your attack is not very effective.");
				pseudoPlayer._ljExausted = true;
			}
		}*/
		
		/*ItemStack itemInHand = player.getItemInHand();
		
		if(itemInHand.getDurability() == -5)
			additionalDamage += 4;
		if(itemInHand.getDurability() == -4)
			additionalDamage += 3;
		if(itemInHand.getDurability() == -3)
			additionalDamage += 2;
		if(itemInHand.getDurability() == -2)
			additionalDamage += 2;
		if(itemInHand.getDurability() == -1)
			additionalDamage += 1;*/
		
		/*double chanceOfEffect = (double)lumberjackingSkill / 1000;
		double bleedChance = chanceOfEffect * .2;
		
		if(bleedChance > Math.random()) {
			if(damagedEntity instanceof Player) {
				Player defenderPlayer = (Player)damagedEntity;
				PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
				if(defenderPseudoPlayer._bleedTicks <= 0) {
					defenderPseudoPlayer._bleedTicks = 10;
					defenderPlayer.sendMessage(ChatColor.GREEN+"You are bleeding!");
					player.sendMessage(ChatColor.GREEN+defenderPlayer.getName()+" is bleeding!");
				}
			}
		}*/
		
		int manaburn = 0;
		
		double damagePercent = 0;
		if(lumberjackingSkill >= 1000) {
			damagePercent = .5;
			manaburn = 5;
		}
		else if(lumberjackingSkill >= 750) {
			damagePercent = .35;
			manaburn = 3;
		}
		else if(lumberjackingSkill >= 500) {
			damagePercent = .25;
			manaburn = 2;
		}
		else if(lumberjackingSkill >= 250) {
			damagePercent = .15;
			manaburn = 1;
		}
		
		List<Entity> nearbyEntities = damagedEntity.getNearbyEntities(3, 3, 3);
		
		damage += additionalDamage;
		
		ArrayList<LivingEntity> possiblyDamaged = new ArrayList<LivingEntity>();
		
		for(Entity entity : nearbyEntities) {
			if(entity == damagedEntity)
				continue;
			
			if(entity instanceof LivingEntity) {
				LivingEntity lE = (LivingEntity)entity;
				possiblyDamaged.add(lE);
				//int newDamage = (int)Math.ceil((damage * damagePercent));
				if(lE instanceof Player) {
					Player p = (Player)lE;
					if(p == player)
						continue;
					
					if(!RPGEntityListener.canPlayerDamagePlayer(player, p))
						continue;
					
					possiblyDamaged.add(lE);
					/*RPGEntityListener.criminalAction(p, player);
					int adjustedDamage = Utils.AdjustDamageForArmor(p, newDamage);
					lE.damage(adjustedDamage);*/
				}
				/*else
					lE.damage(newDamage);*/
			}
		}
		
		int numPossiblyDamaged = possiblyDamaged.size();
		if(numPossiblyDamaged > 0) {
			int randomIndex = (int)Math.floor((Math.random() * (double)numPossiblyDamaged));
			LivingEntity cleavedEntity = possiblyDamaged.get(randomIndex);
			if(cleavedEntity != null) {
				int newDamage = (int)Math.ceil((damage * damagePercent));
				if(cleavedEntity instanceof Player) {
					Player p = (Player)cleavedEntity;
					if(p != player && RPGEntityListener.canPlayerDamagePlayer(player, p)) {
						RPGEntityListener.criminalAction(p, player);
						int adjustedDamage = Utils.AdjustDamageForArmor(p, newDamage);
						cleavedEntity.damage(adjustedDamage);
					}
				}
				else
					cleavedEntity.damage(newDamage);
			}
		}
		
		event.setDamage(damage);
		/*if(damagedEntity instanceof Player) {
			Player p = (Player)damagedEntity;
			PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
			if(pP != null) {
				if(pP.getMana() >= manaburn) {
					pP.setMana(pP.getMana() - manaburn);
				}
				else {
					pP.setMana(0);
				}
			}
		}*/
		
		if(!pseudoPlayer.isSkillLocked("lumberjacking")) {
			boolean canGainSkill = false;
			if(damagedEntity instanceof Monster || damagedEntity instanceof Player || damagedEntity instanceof Slime) {
				if(Math.random() < .5)
					canGainSkill = true;
			}
			else if(damagedEntity instanceof Animals) {
				if(Math.random() < .25)
					canGainSkill = true;
			}
			
			if(canGainSkill) {
				skillGain(player, pseudoPlayer, .2, "lumberjacking", "Lumberjacking");
			}
		}
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer, Entity attacked) {
		if(pseudoPlayer.isSkillLocked("lumberjacking"))
			return;
		
		double rand1 = Math.random();
		boolean canGain = false;
		if(attacked == null) {
			if(Math.random() < .3)
				canGain = true;
		}
		else {
			if(attacked instanceof Player || attacked instanceof Monster) {
				if(rand1 < .5)
					canGain = true;
			}
			else if(attacked instanceof Animals) {
				if(rand1 < .25)
					canGain = true;
			}
		}
		if(canGain) {
			skillGain(player, pseudoPlayer, .2, "lumberjacking", "Lumberjacking");
		}
	}
}