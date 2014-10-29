package com.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.lostshard.Data.Variables;
import com.lostshard.Main.Lostshard;
import com.lostshard.Objects.PseudoPlayer;
import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener implements Listener {
	
	@EventHandler
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		for(PseudoPlayer player : Lostshard.getPlayers())
			if(player.getPlayer().getName().equalsIgnoreCase(vote.getUsername())) {
				//Set player money
				player.setMoney(player.getMoney()+Variables.getVoteMoney());
				//Add a notification
				break;
			}
	}

}
