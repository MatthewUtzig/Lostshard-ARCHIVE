package com.lostshard.Lostshard.GameEvents;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import com.lostshard.Lostshard.Main.Lostshard;

public abstract class Event implements Listener {

	private final String name;
	
	private final String title;
	
	private final long start;
	private final long stop;

	private final Lostshard plugin;
	
	public Event(Lostshard plugin, String name, String title, long start, long stop) {
		this.name = name;
		this.title = title;
		this.start = start;
		this.stop = stop;
		this.plugin = plugin;
	}

	public abstract boolean start();
	public abstract boolean stop();
	public abstract boolean create(CommandSender sender, String[] args);
	public abstract boolean tick(float delta);
	
	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public long getStart() {
		return start;
	}

	public long getStop() {
		return stop;
	}

	public Lostshard getPlugin() {
		return plugin;
	}
}
