package com.lostshard.lostshard.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class EnderdragonHandler {

	private static boolean spawnDrake = false;

	public static void respawnDragonCheck(PlayerChangedWorldEvent event) {
		// Check if its The End
		if (!event.getPlayer().getLocation().getWorld().getEnvironment()
				.equals(Environment.THE_END))
			return;
		if (!spawnDrake)
			return;
		event.getPlayer()
				.getLocation()
				.getWorld()
				.spawnEntity(event.getPlayer().getLocation().add(0, 40, 0),
						EntityType.ENDER_DRAGON);
		spawnDrake = false;
	}
	
	public static void resetDrake() {
		spawnDrake = true;
		Bukkit.broadcastMessage(ChatColor.GREEN
				+ "The Enderdragon has returned to The End");
	}
}
