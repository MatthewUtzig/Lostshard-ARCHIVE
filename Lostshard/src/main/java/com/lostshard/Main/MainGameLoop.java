package com.lostshard.Main;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MainGameLoop extends BukkitRunnable{

	@SuppressWarnings("unused")
	private final JavaPlugin plugin;
	private long timeElapsed;
	 
    public MainGameLoop(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    
    //One sec. loop. TODO Verify that this is correct. http://wiki.bukkit.org/Scheduler_Programming
	public void run() {
		
	}


	public long getTimeElapsed() {
		return timeElapsed;
	}


	public void setTimeElapsed(long timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

}
