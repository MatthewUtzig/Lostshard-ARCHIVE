package com.lostshard.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Skills.BladesSkill;
import com.lostshard.lostshard.Skills.BrawlingSkill;
import com.lostshard.lostshard.Skills.LumberjackingSkill;

public class EntityListener implements Listener {

	public EntityListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamagedByEntityLow(EntityDamageByEntityEvent event) {
		BladesSkill.playerDamagedEntityWithSword(event);
		LumberjackingSkill.playerDamagedEntityWithAxe(event);
		BrawlingSkill.playerDamagedEntityWithMisc(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {

	}

}
