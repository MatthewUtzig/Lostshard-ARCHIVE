package com.lostshard.RPG.Events;

import org.bukkit.ChatColor;

import com.lostshard.RPG.RPG;
import com.lostshard.RPG.Utils.Utils;

public class WorldEvent {
	public WorldEvent() {
		
	}
	
	public String getDescription() {
		return "DESCRIPTION";
	}
	
	public void start() {
		broadcast("EVENT START");
	}
	
	public void finish() {
		broadcast("EVENT END");
	}
	
	public void tick() {
		
	}
	
	public void cleanUp() {
		RPG._worldEvent = null;
	}
	
	protected void broadcast(String message) {
		Utils.getPlugin().getServer().broadcastMessage(ChatColor.DARK_GREEN+message);
	}
}
