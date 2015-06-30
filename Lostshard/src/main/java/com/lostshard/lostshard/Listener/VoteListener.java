package com.lostshard.lostshard.Listener;

//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
//import com.lostshard.lostshard.Objects.PseudoPlayer;
//import com.vexsoftware.votifier.model.Vote;
//import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener extends LostshardListener {

	public VoteListener(Lostshard plugin) {
		super(plugin);
	}

	PlayerManager pm = PlayerManager.getManager();

//	@EventHandler(priority = EventPriority.HIGHEST)
//	public void onVote(VotifierEvent event) {
//		final Vote vote = event.getVote();
//		for (final PseudoPlayer player : this.pm.getPlayers())
//			if (player.getPlayer().getName()
//					.equalsIgnoreCase(vote.getUsername())) {
//				// Set player money
//				player.setMoney(player.getMoney() + Variables.voteMoney);
//				// Add a notification
//				break;
//			}
//	}

}
