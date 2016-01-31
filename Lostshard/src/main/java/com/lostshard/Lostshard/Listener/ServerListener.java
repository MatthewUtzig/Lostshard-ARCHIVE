package com.lostshard.Lostshard.Listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;

import com.lostshard.Lostshard.Main.Lostshard;

public class ServerListener extends LostshardListener {

	public ServerListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPing(ServerListPingEvent event) {
		event.setMaxPlayers(Lostshard.getMaxPlayers());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerConnect(PlayerLoginEvent event) {
		if(event.getPlayer().isOp()) {
			event.setResult(Result.ALLOWED);
			return;
		}
		if(event.getResult() == Result.KICK_BANNED || event.getResult() == Result.KICK_WHITELIST || event.getResult() == Result.KICK_OTHER)
			return;
		if(Bukkit.getOfflinePlayers().length >= Lostshard.getMaxPlayers()) {
			event.setResult(Result.ALLOWED);
		}
	}
}
