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

	public WorldEvent(Lostshard plugin, String name, Location location, int range, String title, long start,
			long stop) {
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

	public int broadcast(String message) {
		return Output.broadcast(message);
	}

	public int broadcastArea(String message, int size) {
		return Output.broadcastArea(message, this.getLocation(), this.getRange());
	}

	public boolean cleanup() {
		EventManager.getManager().removeEvent(this);
		return true;
	}

	public abstract boolean create(Player player, String[] args);

	public abstract boolean finish();

	public abstract String getDescription();

	public Location getLocation() {
		return this.location;
	}

	public String getName() {
		return this.name;
	}

	public Lostshard getPlugin() {
		return this.plugin;
	}

	public int getRange() {
		return this.size;
	}

	public long getStart() {
		return this.start;
	}

	public long getStop() {
		return this.stop;
	}

	public String getTitle() {
		return this.title;
	}

	public boolean isActive() {
		return this.active;
	}

	public boolean isPaused() {
		return this.paused;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public abstract boolean start();

	public abstract boolean tick(float delta);
}
