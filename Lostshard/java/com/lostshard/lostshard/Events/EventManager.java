package com.lostshard.lostshard.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class EventManager {

	public static void callEvent(Event event) {
		Bukkit.getPluginManager().callEvent(event);
	}
	
}
