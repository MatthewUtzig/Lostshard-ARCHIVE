package com.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.lostshard.Data.Variables;

public class ServerListener implements Listener {
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		event.setMotd(Variables.getMotd());
		event.setMaxPlayers(Variables.getMaxPlayers());
	}
	
}
