package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class PlayerManager {
	
	static PlayerManager manager = new PlayerManager();

	private List<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
	
	public static PlayerManager getManager() {
		return manager;
	}
	
	public List<PseudoPlayer> getPlayers() {
		return players;
	}
	
	public void setPlayers(List<PseudoPlayer> players) {
		this.players = players;
	}
	
	public PseudoPlayer getPlayer(UUID uuid) {
		for (PseudoPlayer pPlayer : players)
			if (pPlayer.getPlayerUUID().equals(uuid))
					return pPlayer;
		return Database.getPlayer(uuid);
	}

	public PseudoPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}
	
	public PseudoPlayer getPlayer(OfflinePlayer player) {
		return getPlayer(player.getUniqueId());
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		boolean ableTo = addPlayer(player);
		if(!ableTo)
			event.disallow(Result.KICK_OTHER, ChatColor.RED+"Something is wrong. We are working on it.");
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		players.remove(getPlayer(event.getPlayer()));
	}
	
	public void tick(double delta, long tick) {
		for(PseudoPlayer pPlayer : players)
			pPlayer.tick(delta, tick);
	}

//	public PseudoPlayer getPlayer(int id) {
//		for(PseudoPlayer pPlayer : players)
//			if(pPlayer.getId() == id)
//					return pPlayer;
//		return null;
//	}
	
	public boolean addPlayer(Player player) {
		PseudoPlayer pPlayer;
		pPlayer = Database.getPlayer(player.getUniqueId());
		if(pPlayer != null) {
			players.add(pPlayer);
			return true;
		}
		pPlayer = Database.insertPlayer(new PseudoPlayer(player.getUniqueId(), 1));
		if(pPlayer != null) {
			players.add(pPlayer);
			return true;
		}
		return false;
	}
	
}
