package com.lostshard.lostshard.Handlers;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class PseudoPlayerHandler {

	public static PseudoPlayer getPlayer(UUID uuid) {
		for (PseudoPlayer pPlayer : Lostshard.getPlayers())
			if (pPlayer.getPlayerUUID().equals(uuid))
				return pPlayer;
		return null;
	}

	public static PseudoPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}

	public static void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (getPlayer(event.getPlayer()) == null) {
			PseudoPlayer pPlayer = new PseudoPlayer(-1, 0, 0,
					player.getUniqueId(), null, 0, true, 0, false, 0);
//			Database.insertPlayer(pPlayer);
			Lostshard.getPlayers().add(pPlayer);
		}
	}

}
