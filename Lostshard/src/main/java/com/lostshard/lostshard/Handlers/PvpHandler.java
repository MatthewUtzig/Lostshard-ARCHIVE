package com.lostshard.lostshard.Handlers;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.lostshard.Objects.Plot;

/**
 * @author Jacob
 * Handling entity's hitting entity's.
 */
public class PvpHandler {
	
	/**
	 * @param event
	 * @return Entity can damage Entity.
	 * Checking if they are in clan or other reasons if they should not be able to damage each other.
	 */
	public static boolean canEntityAttackEntity(EntityDamageByEntityEvent event) {
		/**
		 * Define attacker and defender.
		 */
		Entity attacker = event.getDamager();
		Entity defender = event.getEntity();
		
		/**
		 * Checking if its an npc.
		 */
		if(defender.hasMetadata("NPC"))
			return false;
		
		/**
		 * Ensuring that the defender and is a Player.
		 */
		if(!(defender instanceof Player))
			return true;
		
		/**
		 * Handling player hitting with feather.
		 */
		if(attacker instanceof Player && defender instanceof Player && 
				((Player) attacker).getItemInHand().getType().equals(Material.FEATHER)) {
			((Player) defender).sendMessage(ChatColor.GRAY+"x player have ticklet you");
			//TODO
			return false;
		}
		
		/**
		 * Allowing potion to be thrown on all if positive.
		 */
		if(event.getDamager() instanceof ThrownPotion)
			for(PotionEffect pe : ((ThrownPotion) event.getDamager()).getEffects())
				if(pe.getType().equals(PotionEffectType.HEAL) || 
						pe.getType().equals(PotionEffectType.REGENERATION) || 
						pe.getType().equals(PotionEffectType.ABSORPTION) || 
						pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE) || 
						pe.getType().equals(PotionEffectType.FAST_DIGGING) || 
						pe.getType().equals(PotionEffectType.FIRE_RESISTANCE) ||
						pe.getType().equals(PotionEffectType.HEALTH_BOOST) ||
						pe.getType().equals(PotionEffectType.INCREASE_DAMAGE) ||
						pe.getType().equals(PotionEffectType.INVISIBILITY) ||
						pe.getType().equals(PotionEffectType.SPEED) ||
						pe.getType().equals(PotionEffectType.WATER_BREATHING) ||
						pe.getType().equals(PotionEffectType.NIGHT_VISION))
					return true;
		
		/**
		 * Handling attacker if the damage came from an projectile and getting the shooter.
		 */
		if(attacker instanceof Projectile)
			attacker = (Entity) ((Projectile) attacker).getShooter();
		
		/**
		 * Ensuring that the attacker is a Player.
		 */
		if(!(attacker instanceof Player))
			return true;
		
		/**
		 * Checking if the defender is standing in a none pvp plot.
		 */
		Plot plotAtDefender = PlotHandler.findPlotAt(defender.getLocation());
		if(plotAtDefender != null && plotAtDefender.isAllowPvp())
			return false;
		
		/**
		 * Checking if the attacker is standing a none pvp plot.
		 */
		Plot plotAtAttacker = PlotHandler.findPlotAt(attacker.getLocation());
		if(plotAtAttacker != null && plotAtAttacker.isAllowPvp())
			return false;
		
		/**
		 * Allowing players to damage them self.
		 */
		if(attacker == defender)
			return true;
		
		/**
		 * Checking if they are in same clan or not.
		 */
//		if(attacker.clan != null && defender != null && attacker.clan == defender.clan)
//			return false;
		
		/**
		 * Checking if they are in same party or not.
		 */
//		if(attacker.party != null && defender != null && attacker.party == defender.party)
//			return false;
		
		
		return true;
	}

}
