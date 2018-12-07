package com.lostshard.RPG.Skills;

import org.bukkit.ChatColor;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Spells.*;
import com.lostshard.RPG.Utils.Output;

public class Brawling extends Skill {
	public static void handleCommand(Player player, String[] split) {
	}
	
	public static void notEnoughStamina(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough stamina to use "+spell.getName()+".");
	}
	
	public static void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast "+spell.getName()+".");
	}
	
	public static void playerDamagedEntityWithMisc(Player player, Entity damagedEntity, EntityDamageByEntityEvent event) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		int brawlingSkill = pseudoPlayer.getSkill("brawling");
		double damage = event.getDamage();
		int additionalDamage = 0;
		
		if(brawlingSkill >= 1000)
			additionalDamage = 4;
		else if(brawlingSkill >= 750)
			additionalDamage = 3;
		else if(brawlingSkill >= 500)
			additionalDamage = 2;
		else if(brawlingSkill >= 250)
				additionalDamage = 1;
		
		ItemStack itemInHand = player.getItemInHand();
		
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
		
		double chanceOfEffect = (double)brawlingSkill / 1000;
		double stunChance = chanceOfEffect * .25;
		
		double rand = Math.random();
		System.out.println(rand);
		if(stunChance > rand) {
			//stun
			if(damagedEntity instanceof Player) {
				Player defenderPlayer = (Player)damagedEntity;
				PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
				if(defenderPseudoPlayer._stunTicks <= 0) {
					defenderPseudoPlayer._stunTicks = 30;
					defenderPlayer.sendMessage(ChatColor.GREEN+"You have been stunned!");
					player.sendMessage(ChatColor.GREEN+"You stunned "+defenderPlayer.getName()+"!");
				}
			}
		}
		
		if(damagedEntity instanceof Player) {
			if(brawlingSkill >= 1000) {
				Player defenderPlayer = (Player)damagedEntity;
				PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
				if(defenderPseudoPlayer._stunTicks <= 0)
					defenderPseudoPlayer._stunTicks = 17;
			}
		}
		
		if(damagedEntity instanceof Player) {
			// Divide pvp damage by half
			//damage /= 2;
		}
		
		damage += additionalDamage;
		event.setDamage(damage);
		
		if(!pseudoPlayer.isSkillLocked("brawling")) {
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
				skillGain(player, pseudoPlayer, .2, "brawling", "Brawling");
			}
		}
	}
	
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer, Entity attacked) {
		if(pseudoPlayer.isSkillLocked("brawling"))
			return;
		
		double rand1 = Math.random();
		boolean canGain = false;
		if(attacked instanceof Player || attacked instanceof Monster) {
			if(rand1 < .5)
				canGain = true;
		}
		else if(attacked instanceof Animals) {
			if(rand1 < .25)
				canGain = true;
		}
		if(canGain) {
			skillGain(player, pseudoPlayer, .1, "brawling", "Brawling");
		}
	}
}
