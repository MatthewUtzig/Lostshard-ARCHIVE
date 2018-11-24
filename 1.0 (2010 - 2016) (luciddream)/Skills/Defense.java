package com.lostshard.RPG.Skills;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;

public class Defense extends Skill {
	public static void playerDamaged(Player player, EntityDamageEvent event) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		int curSkill = pseudoPlayer.getSkill("defense");
		double damageMultiplier = (double)curSkill / 1000; //100 skill == 1, 50 skill == .5, 25 skill == .25 etc
		damageMultiplier *= .5; // 100 skill == .5, 50 == .25, 25 = .125 etc
		damageMultiplier = 1-damageMultiplier; // 100 skill = .5, 50 = .75, 25 = .875 etc
		
		System.out.println("DamageMultiplier: "+damageMultiplier);
		double damage = event.getDamage();
		System.out.println("PreDamage:" + damage);
		if(damage >= 1) {
			damage *= damageMultiplier;
			if(damage < 1)
				damage = 1;
			event.setDamage(damage);
			System.out.println("PostDamage:" + damage);
		}
	}
}
