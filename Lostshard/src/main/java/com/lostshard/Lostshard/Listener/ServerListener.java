package com.lostshard.Lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.ServerListPingEvent;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Main.Lostshard;

public class ServerListener extends LostshardListener {

	public ServerListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPing(ServerListPingEvent event) {
		event.setMotd(Variables.motd.replace("&", "�"));
		event.setMaxPlayers(Lostshard.getMaxPlayers());
	}
}
