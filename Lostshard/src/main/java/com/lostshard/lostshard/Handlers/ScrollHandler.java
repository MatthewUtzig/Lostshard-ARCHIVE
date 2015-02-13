package com.lostshard.lostshard.Handlers;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.SPL_Lightning;

public class ScrollHandler {

	static PlayerManager pm = PlayerManager.getManager();
	
	public static void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity().getKiller() == null)
			return;
		Random rand = new Random();
		if(rand.nextInt(9) > 1)
			return;
		Player player = event.getEntity().getKiller();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Entity entity = event.getEntity();
		EntityType type = entity.getType();
		if(type.equals(EntityType.ZOMBIE)) {
			pPlayer.giveScroll(new SPL_Lightning());
		} else if(type.equals(EntityType.ZOMBIE)) {
			
		} else if(type.equals(EntityType.SKELETON)) {
			
		} else if(type.equals(EntityType.BLAZE)) {
			
		} else if(type.equals(EntityType.CAVE_SPIDER) || type.equals(EntityType.SPIDER)) {
			
		} else if(type.equals(EntityType.CREEPER)) {
			
		} else if(type.equals(EntityType.GHAST)) {
			
		} else if(type.equals(EntityType.ZOMBIE)) {
			
		} else if(type.equals(EntityType.ENDERMAN)) {
			
		} else if(type.equals(EntityType.ENDERMITE)) {
			
		} else if(type.equals(EntityType.GUARDIAN)) {
			
		} else if(type.equals(EntityType.PIG_ZOMBIE)) {
			
		} else if(type.equals(EntityType.WITCH)) {
			
		} else if(type.equals(EntityType.SILVERFISH)) {
			
		} else if(type.equals(EntityType.ENDER_DRAGON)) {
			
		} else if(type.equals(EntityType.WITHER)) {
			
		}
	}
	
}
