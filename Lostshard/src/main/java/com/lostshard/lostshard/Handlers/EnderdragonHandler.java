package com.lostshard.lostshard.Handlers;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class EnderdragonHandler {

	private static boolean spawnDrake = true;
	private static int lastSpawnHoure = 0;
	
	public static void respawnDragonCheck(PlayerChangedWorldEvent event) {
		//Check if its The End
		if(!event.getPlayer().getLocation().getWorld().getEnvironment().equals(Environment.THE_END))
			return;
		if(!spawnDrake)
			return;
		event.getPlayer().getLocation().getWorld().spawnEntity(event.getPlayer().getLocation().add(0, 40, 0), EntityType.ENDER_DRAGON);
		spawnDrake = false;
		//TODO update add fancy message
		Bukkit.broadcastMessage(ChatColor.GREEN+"The Enderdragon has returned to The End");
	}
	
	public static void tick() {
		Calendar time = Calendar.getInstance();
		if(time.get(Calendar.HOUR_OF_DAY) % 4 == 0 && time.get(Calendar.HOUR_OF_DAY) != lastSpawnHoure) {
			spawnDrake = true;
			lastSpawnHoure = time.get(Calendar.HOUR_OF_DAY);
		}
	}

}
