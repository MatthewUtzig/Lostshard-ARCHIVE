package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

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
		return null;
	}

	public PseudoPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}
	
	public PseudoPlayer getPlayer(OfflinePlayer player) {
		return getPlayer(player.getUniqueId());
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (getPlayer(event.getPlayer()) == null) {
			PseudoPlayer pPlayer = new PseudoPlayer(player.getUniqueId(), 1);
			Database.insertPlayer(pPlayer);
		}
	}
	
	public void tick(double delta, long tick) {
		for(PseudoPlayer pPlayer : players)
			pPlayer.tick(delta, tick);
	}
	
}
