package com.lostshard.lostshard.Main;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.lostshard.Handlers.EnderdragonHandler;

public class GameLoop extends BukkitRunnable {

	public static long tick = 0;

	@SuppressWarnings("unused")
	private final JavaPlugin plugin;

	public GameLoop(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void run() {
		EnderdragonHandler.tick();

		tick++;
	}
}
