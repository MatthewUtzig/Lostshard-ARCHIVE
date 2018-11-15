package com.lostshard.RPG.Skills;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Damageable;
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

public class Swordsmanship extends Skill{
	public static void handleCommand(Player player, String[] split) {
	}
	
	public static void notEnoughStamina(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough stamina to use "+spell.getName()+".");
	}
	
	public static void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast "+spell.getName()+".");
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer, Entity attacked) {
		if(pseudoPlayer.isSkillLocked("blades"))
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
			skillGain(player, pseudoPlayer, .2, "blades", "Blades");
		}
	}
	
	public static void playerDamagedEntityWithSword(Player player, Entity damagedEntity, EntityDamageByEntityEvent event) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		int swordsSkill = pseudoPlayer.getSkill("blades");
		double damage = event.getDamage();
		int additionalDamage = 0;
		
		ItemStack itemInHand = player.getItemInHand();
		if(itemInHand.getType().equals(Material.GOLD_SWORD)) {
			player.getWorld().strikeLightningEffect(damagedEntity.getLocation());
			damage += 4;
		}
		
		int pierceAmount = 0;
		
		if(swordsSkill >= 1000) {
			additionalDamage = 4;
			/*if(itemInHand.getType().equals(Material.GOLD_SWORD))
				pierceAmount = 2;*/
			/*
			else if(itemInHand.getType().equals(Material.DIAMOND_SWORD))
				pierceAmount = 2;
			else if(itemInHand.getType().equals(Material.IRON_SWORD))
				pierceAmount = 1;*/
		}		
		else if(swordsSkill >= 750)
			additionalDamage = 3;
		else if(swordsSkill >= 500)
			additionalDamage = 2;
		else if(swordsSkill >= 250)
			additionalDamage = 1;
		
		/*if(itemInHand.getDurability() == -5)
			additionalDamage += 4;
		if(itemInHand.getDurability() == -4)
			additionalDamage += 3;
		if(itemInHand.getDurability() == -3)
			additionalDamage += 2;
		if(itemInHand.getDurability() == -2)
			additionalDamage += 2;
		if(itemInHand.getDurability() == -1)
			additionalDamage += 1;*/
		
		double chanceOfEffect = (double)swordsSkill / 1000;
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
		}
		
		if(damagedEntity instanceof Player) {
			Player damagedPlayer = (Player)damagedEntity;
			Damageable damag = damagedPlayer;
			if(damag.getHealth() > pierceAmount)
				damagedPlayer.setHealth(damag.getHealth() - pierceAmount);
		}
		
		damage += additionalDamage;
		event.setDamage(damage);
		
		if(!pseudoPlayer.isSkillLocked("blades")) {
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
				skillGain(player, pseudoPlayer, .2, "blades", "Blades");
			}
		}
	}
}
