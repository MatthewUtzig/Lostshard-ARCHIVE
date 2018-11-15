package com.lostshard.RPG.Listeners;

import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;
import org.getspout.spoutapi.event.spout.SpoutListener;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.lostshard.RPG.PlayerHUD;
import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;

public class SpoutRPGListener extends SpoutListener {
	
	@Override
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		SpoutPlayer spoutPlayer = event.getPlayer();
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(spoutPlayer.getName());
		
		pseudoPlayer._playerHud = new PlayerHUD(pseudoPlayer, spoutPlayer);
		pseudoPlayer._playerHud.init();
		if(spoutPlayer.isOp())
			spoutPlayer.setCanFly(true);
	}
}
