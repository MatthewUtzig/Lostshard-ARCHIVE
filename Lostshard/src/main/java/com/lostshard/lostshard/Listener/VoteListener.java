package com.lostshard.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener implements Listener {

	public VoteListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		for (PseudoPlayer player : Lostshard.getPlayers())
			if (player.getPlayer().getName()
					.equalsIgnoreCase(vote.getUsername())) {
				// Set player money
				player.setMoney(player.getMoney() + Variables.voteMoney);
				// Add a notification
				break;
			}
	}

}
