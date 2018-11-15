package com.lostshard.RPG.Skills;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.Spells.*;
import com.lostshard.RPG.Utils.Output;

public class Archery extends Skill{	
	public static void handleCommand(Player player, String[] split) {
	}
	
	public static void notEnoughStamina(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough stamina to use "+spell.getName()+".");
	}
	
	public static void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast "+spell.getName()+".");
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer, Entity attacked) {
		if(pseudoPlayer.isSkillLocked("archery"))
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
			skillGain(player, pseudoPlayer, .1, "archery", "Archery");
		}
	}
}
