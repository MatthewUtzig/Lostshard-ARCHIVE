package com.lostshard.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.Managers;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener extends LostshardListener implements Managers {

	public VoteListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVote(VotifierEvent event) {
		
	}
}
