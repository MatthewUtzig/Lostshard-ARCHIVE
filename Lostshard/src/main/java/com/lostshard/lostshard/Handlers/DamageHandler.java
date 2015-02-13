package com.lostshard.lostshard.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

public class DamageHandler {

	
	static double baseNerf = .9d;
	static double armorNerf = .7d;
	static double magicNerf = 1d;
	static double resistanceNerf = 1d;
	static double hardhatNerf = 1d;
	
	public static void damage(EntityDamageByEntityEvent event) {
		
		event.setDamage(DamageModifier.BASE, event.getDamage(DamageModifier.BASE)*baseNerf);
		
		if(event.getEntity() instanceof Player) {
			if(event.isApplicable(DamageModifier.BLOCKING))
				event.setDamage(DamageModifier.BLOCKING, 0);
			if(event.isApplicable(DamageModifier.ARMOR))
				event.setDamage(DamageModifier.ARMOR, event.getDamage(DamageModifier.ARMOR)*armorNerf);
			if(event.isApplicable(DamageModifier.MAGIC))
				event.setDamage(DamageModifier.MAGIC, event.getDamage(DamageModifier.MAGIC)*magicNerf);
			if(event.isApplicable(DamageModifier.RESISTANCE))
				event.setDamage(DamageModifier.RESISTANCE, event.getDamage(DamageModifier.RESISTANCE)*resistanceNerf);
			if(event.isApplicable(DamageModifier.HARD_HAT))
				event.setDamage(DamageModifier.HARD_HAT, event.getDamage(DamageModifier.HARD_HAT)*hardhatNerf);
			
			Player player = (Player) event.getEntity();
			Bukkit.broadcastMessage(Double.toString(event.getDamage(DamageModifier.BASE)));
			Bukkit.broadcastMessage(Double.toString(event.getDamage(DamageModifier.ARMOR)));
			Bukkit.broadcastMessage(Double.toString(event.getDamage(DamageModifier.MAGIC)));
			Bukkit.broadcastMessage(Double.toString(event.getDamage(DamageModifier.RESISTANCE)));
			Bukkit.broadcastMessage(Double.toString(event.getDamage(DamageModifier.HARD_HAT)));
		}
	}

}
