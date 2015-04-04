package com.lostshard.lostshard.Skills;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;

public class BladesSkill extends Skill {
	
	public BladesSkill() {
		super();
		setName("Bladess");
		setBaseProb(.2);
		setScaleConstant(60);
	}
	
	public static void playerDamagedEntityWithSword(EntityDamageByEntityEvent event) {
		if(event.isCancelled())
			return;
		if(!(event.getDamager() instanceof Player))
			return;
		if(!(event.getEntity() instanceof LivingEntity))
			return;
		Player player = (Player) event.getDamager();
		Material item = player.getItemInHand().getType();
		if(!ItemUtils.isSword(item))
			return;
		Entity damagedEntity = event.getEntity();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Skill skill = pPlayer.getCurrentBuild().getBlades();
		int swordsSkill = skill.getLvl();
		double damage = event.getDamage();
		int additionalDamage = 0;
		
		int pierceAmount = 0;
		
		if(swordsSkill >= 1000) {
			additionalDamage = 4;
		}		
		else if(swordsSkill >= 750)
			additionalDamage = 3;
		else if(swordsSkill >= 500)
			additionalDamage = 2;
		else if(swordsSkill >= 250)
			additionalDamage = 1;
		
		if(swordsSkill >= 250) {
			double chanceOfEffect = (double)swordsSkill / 1000;
			double bleedChance = chanceOfEffect * .2;
			
			if(bleedChance > Math.random()) {
				if(damagedEntity instanceof Player) {
					Player defenderPlayer = (Player)damagedEntity;
					PseudoPlayer defenderPseudoPlayer = pm.getPlayer(defenderPlayer);
					if(defenderPseudoPlayer.getTimer().bleedTick <= 0) {
						defenderPseudoPlayer.getTimer().bleedTick = 10;
						defenderPlayer.sendMessage(ChatColor.GREEN+"You are bleeding!");
						player.sendMessage(ChatColor.GREEN+defenderPlayer.getName()+" is bleeding!");
					}
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
		if(event.isApplicable(DamageModifier.BASE))
		event.setDamage(DamageModifier.BASE, damage);
		
		if(damagedEntity instanceof Monster || damagedEntity instanceof Player || damagedEntity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);
		int gain = pPlayer.getCurrentBuild().getBlades().skillGain(pPlayer);
		Output.gainSkill(player, "Bladess", gain, skill.getLvl());
		if(gain > 0)
			pPlayer.update();
	}
}
