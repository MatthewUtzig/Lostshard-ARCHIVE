package com.lostshard.lostshard.GameEvents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class EventManager {

	private final static EventManager manager = new EventManager();

	public static EventManager getManager() {
		return manager;
	}

	private List<WorldEvent> registredEvents;

	private List<WorldEvent> events;

	private EventManager() {
		this.setEvents(new ArrayList<WorldEvent>());
	}

	public WorldEvent createEvent(String name, Player player, int size, String title, long start,
			long stop) {
		WorldEvent event = null;

		if (name.equalsIgnoreCase("ghast"))
			event = new GhastEvent(player.getLocation(), size, title, start, stop);

		if (event != null) {
			event.create(player);
			this.events.add(event);
		}
		return event;
	}

	public List<WorldEvent> getActiveEvents() {
		final List<WorldEvent> activeEvents = new ArrayList<WorldEvent>(this.events);
		activeEvents.removeIf(e -> e.isActive() || e.isPaused());
		return activeEvents;
	}

	public List<WorldEvent> getEvents() {
		return this.events;
	}

	public List<WorldEvent> getRegistredEvents() {
		return this.registredEvents;
	}

	public void removeEvent(WorldEvent worldEvent) {
		this.events.remove(worldEvent);
	}

	public void setEvents(List<WorldEvent> events) {
		this.events = events;
	}

	public void setRegistredEvents(List<WorldEvent> registredEvents) {
		this.registredEvents = registredEvents;
	}

	public void tick(float delta) {
		for (final WorldEvent e : this.getActiveEvents())
			e.tick(delta);
	}
}
