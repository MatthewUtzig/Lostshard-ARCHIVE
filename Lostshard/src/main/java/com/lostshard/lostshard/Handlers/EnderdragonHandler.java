package com.lostshard.lostshard.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

public class EnderdragonHandler {

	public static void resetDrake() {
		spawnDrake = true;
		Bukkit.broadcastMessage(ChatColor.GREEN
				+ "The Enderdragon has returned to The End.");
	}

	public static void respawnDragonCheck(PlayerChangedWorldEvent event) {
		// Check if its The End
		if (!event.getPlayer().getLocation().getWorld().getEnvironment()
				.equals(Environment.THE_END))
			return;
		if (!spawnDrake)
			return;
		for(Entity e : event.getPlayer().getLocation().getWorld().getEntities())
			if(e instanceof EnderDragon)
				e.remove();
		event.getPlayer()
		.getLocation()
		.getWorld()
		.spawnEntity(event.getPlayer().getLocation().add(0, 40, 0),
				EntityType.ENDER_DRAGON);
		spawnDrake = false;
	}
	
	public static void onPortalCreate(EntityCreatePortalEvent event) {
		Entity e = event.getEntity();
		if(e instanceof EnderDragon) {
			event.setCancelled(true);
			e.getWorld().dropItem(e.getLocation(), new ItemStack(Material.DRAGON_EGG));
		}
	}

	private static boolean spawnDrake = false;
}
