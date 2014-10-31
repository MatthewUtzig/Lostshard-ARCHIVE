package com.lostshard.lostshard.Handlers;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class PseudoPlayerHandler {

	public static PseudoPlayer getPlayer(UUID uuid) {
		for(PseudoPlayer pPlayer : Lostshard.getPlayers())
			if(pPlayer.getPlayerUUID().equals(uuid))
				return pPlayer;
		return null;
	}
	
	public static PseudoPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}
	
}
