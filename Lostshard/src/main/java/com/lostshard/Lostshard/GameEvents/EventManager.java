package com.lostshard.Lostshard.GameEvents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;

public class EventManager {

	private final static EventManager manager = new EventManager();
	
	private List<WorldEvent> registredEvents;
	
	private List<WorldEvent> events;
	
	private EventManager() {
		this.setEvents(new ArrayList<WorldEvent>());
	}
	
	public static EventManager getManager() {
		return manager;
	}

	public List<WorldEvent> getRegistredEvents() {
		return registredEvents;
	}

	public void setRegistredEvents(List<WorldEvent> registredEvents) {
		this.registredEvents = registredEvents;
	}

	public List<WorldEvent> getEvents() {
		return events;
	}

	public void setEvents(List<WorldEvent> events) {
		this.events = events;
	}
	
	public List<WorldEvent> getActiveEvents() {
		List<WorldEvent> activeEvents = new ArrayList<WorldEvent>(this.events);
		activeEvents.removeIf(e -> e.isActive() || e.isPaused());
		return activeEvents;
	}
	
	public WorldEvent createEvent(Lostshard plugin, String name, Player player, int size, String title, long start, long stop, String[] args) {
		WorldEvent event = null;
		
		if (name.equalsIgnoreCase("ghast"))
			event = new GhastEvent(plugin, player.getLocation(), size, title, start, stop);
		
		if (event != null) {
			event.create(player, args);
			this.events.add(event);
		}
		return event;
	}
	
	public void tick(float delta) {
		for(WorldEvent e : getActiveEvents())
			e.tick(delta);
	}

	public void removeEvent(WorldEvent worldEvent) {
		this.events.remove(worldEvent);
	}
}
