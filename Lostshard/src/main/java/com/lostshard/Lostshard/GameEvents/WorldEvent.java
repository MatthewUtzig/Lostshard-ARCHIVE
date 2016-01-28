package com.lostshard.Lostshard.GameEvents;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Utils.Output;

public abstract class WorldEvent implements Listener {

	private final String name;
	
	private final String title;
	
	private final Location location;
	private final int size;
	
	private final long start;
	private final long stop;
	
	private boolean active;
	private boolean paused;

	private final Lostshard plugin;
	
	public WorldEvent(Lostshard plugin, String name, Location location, int range, String title, long start, long stop) {
		this.name = name;
		this.title = title;
		
		this.location = location;
		this.size = range;
		
		this.start = start;
		this.stop = stop;
		this.plugin = plugin;
		
		this.setPaused(false);
		this.setActive(false);
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public abstract boolean start();
	public abstract boolean finish();
	public boolean cleanup() {
		EventManager.getManager().removeEvent(this);
		return true;
	}
	public abstract String getDescription();
	public abstract boolean create(Player player, String[] args);
	public abstract boolean tick(float delta);
	
	public int broadcast(String message) {
		return Output.broadcast(message);
	}
	
	public int broadcastArea(String message, int size) {
		return Output.broadcastArea(message, getLocation(), getRange());
	}
	
	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public Location getLocation() {
		return location;
	}

	public int getRange() {
		return size;
	}

	public long getStart() {
		return start;
	}

	public long getStop() {
		return stop;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public Lostshard getPlugin() {
		return plugin;
	}
}
