package com.lostshard.Main;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.Handlers.EnderdragonHandler;

public class MainGameLoop extends BukkitRunnable{

	public static long tick = 0;

	@SuppressWarnings("unused")
	private final JavaPlugin plugin;
	 
    public MainGameLoop(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void run() {
    	EnderdragonHandler.tick();
    	
    	
    	tick++;
    }
}
