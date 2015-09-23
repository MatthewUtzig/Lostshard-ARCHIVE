package com.lostshard.Lostshard.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.Lostshard.Handlers.DamageHandler;
import com.lostshard.Lostshard.Handlers.DeathHandler;
import com.lostshard.Lostshard.Handlers.EnderdragonHandler;
import com.lostshard.Lostshard.Handlers.PVPHandler;
import com.lostshard.Lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.Lostshard.Handlers.ScrollHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.Managers;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.ArcherySkill;
import com.lostshard.Lostshard.Skills.BladesSkill;
import com.lostshard.Lostshard.Skills.BrawlingSkill;
import com.lostshard.Lostshard.Skills.LumberjackingSkill;
import com.lostshard.Lostshard.Skills.SurvivalismSkill;
import com.lostshard.Lostshard.Skills.TamingSkill;
import com.lostshard.Lostshard.Utils.Output;

public class EntityListener extends LostshardListener implements Managers {

	public EntityListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void monitorEntityAttackEntity(EntityDamageByEntityEvent event) {
		if(event.getEntity().hasMetadata("NPC")) return;
		BladesSkill.playerDamagedEntityWithSword(event);
		LumberjackingSkill.playerDamagedEntityWithAxe(event);
		BrawlingSkill.playerDamagedEntityWithMisc(event);
		ArcherySkill.EntityDamageByEntityEvent(event);
		DamageHandler.damage(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		PlotProtectionHandler.onEntityChangeBlock(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamaged(EntityDamageEvent event) {
		if(event.getEntity().hasMetadata("NPC")) return;
		
		if (event.getCause().equals(DamageCause.FALL))
			if (event.getEntity().getLocation().subtract(0, 1, 0).getBlock()
					.getType() == Material.WOOL)
				event.setCancelled(true);
		SurvivalismSkill.onPlayerDamage(event);
		DamageHandler.goldArmor(event);
		sm.damage(event);
		restDamage(event);
	}

	private void restDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			PseudoPlayer pPlayer = pm.getPlayer(player);
			if(pPlayer.isResting() && pPlayer.isMeditating()) {
				pPlayer.setMeditating(false);
				pPlayer.setResting(false);
				Output.simpleError(player, "You have been damage and stopped resting and meditating.");
			}else if(pPlayer.isResting()) {
				pPlayer.setResting(false);
				Output.simpleError(player, "You have been damage and stopped resting.");
			}else if(pPlayer.isMeditating()) {
				pPlayer.setMeditating(false);
				Output.simpleError(player, "You have been damage and stopped meditating.");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamagedByEntityHighest(EntityDamageByEntityEvent event) {
		final Entity defender = event.getEntity();
		if (defender.hasMetadata("NPC"))
			return;
		PlotProtectionHandler.onEntityDamageByEntity(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamagedByEntityLow(EntityDamageByEntityEvent event) {
		final Entity attacker = event.getDamager();
		final Entity defender = event.getEntity();
		if (defender.hasMetadata("NPC"))
			return;
		event.setCancelled(!PVPHandler
				.canEntityAttackEntity(attacker, defender));
		PVPHandler.Attack(event);
		TamingSkill.onDamage(event);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity().hasMetadata("NPC"))
			return;
		DeathHandler.handleDeath(event);
		ScrollHandler.onEntityDeathEvent(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		PlotProtectionHandler.onBlockExplode(event);
	}

	@EventHandler
	public void onMonsterSpawn(EntitySpawnEvent event) {
		PlotProtectionHandler.onMonsterSpawn(event);
	}
	
	@EventHandler
	public void onEntityPortalCreate(EntityCreatePortalEvent event) {
		EnderdragonHandler.onPortalCreate(event);
	}

	@EventHandler
	public void onPortalCreate(PortalCreateEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPotion(PotionSplashEvent event) {
		for (final PotionEffect pe : event.getPotion().getEffects())
			if (pe.getType().equals(PotionEffectType.HEAL)
					|| pe.getType().equals(PotionEffectType.REGENERATION)
					|| pe.getType().equals(PotionEffectType.ABSORPTION)
					|| pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)
					|| pe.getType().equals(PotionEffectType.FAST_DIGGING)
					|| pe.getType().equals(PotionEffectType.FIRE_RESISTANCE)
					|| pe.getType().equals(PotionEffectType.HEALTH_BOOST)
					|| pe.getType().equals(PotionEffectType.INCREASE_DAMAGE)
					|| pe.getType().equals(PotionEffectType.INVISIBILITY)
					|| pe.getType().equals(PotionEffectType.SPEED)
					|| pe.getType().equals(PotionEffectType.WATER_BREATHING)
					|| pe.getType().equals(PotionEffectType.NIGHT_VISION))
				return;
		if (event.getEntity().getShooter() instanceof Player) {
			final Player attacker = (Player) event.getEntity().getShooter();
			for (final LivingEntity e : event.getAffectedEntities())
				if (e instanceof Player)
					if (!PVPHandler.canEntityAttackEntity(attacker, e))
						event.setIntensity(e, 0d);
		}
	}

}
