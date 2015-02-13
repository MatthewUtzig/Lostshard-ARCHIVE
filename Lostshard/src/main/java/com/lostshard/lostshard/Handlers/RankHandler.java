package com.lostshard.lostshard.Handlers;

import java.util.List;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Recent.RecentAttacker;

public class RankHandler {

	static PlayerManager pm = PlayerManager.getManager();
	
	public static void rank(PseudoPlayer pPlayer) {
		List<RecentAttacker> recentAttackers = pPlayer.getRecentAttackers();
		if(recentAttackers.size() >= 1 && pPlayer.getTimer().isLastDeathOlder(300000)) {
		
			float attackerSUM = 0;
			int rankFLOOR = 200;
			int rankCELING = 3000;
			int kFACTOR = 8;
			int rangeFACTOR = 1600;
			
			for(RecentAttacker recentAttacker : recentAttackers) {
				PseudoPlayer recentAttackerPseudoPlayer = pm.getPlayer(recentAttacker.getUUID());
				attackerSUM += recentAttackerPseudoPlayer.getRank();
			}
					
			for(RecentAttacker recentAttacker : recentAttackers) {
				PseudoPlayer recentAttackerPseudoPlayer = pm.getPlayer(recentAttacker.getUUID());
				int rank = recentAttackerPseudoPlayer.getRank();
				float ATTACKERNEWRANK = rank + (kFACTOR * (1 + (recentAttackers.size() * pPlayer.getRank() - attackerSUM) / (recentAttackers.size() * rangeFACTOR)))/recentAttackers.size();
				
				if (ATTACKERNEWRANK < rankFLOOR)
					ATTACKERNEWRANK = rankFLOOR;
						
				if (ATTACKERNEWRANK > rankCELING)
					ATTACKERNEWRANK = rankCELING;
				
				Math.round(ATTACKERNEWRANK);
				
				recentAttackerPseudoPlayer.setRank((int) ATTACKERNEWRANK);
			}
			
			float LOSERNEWRANK = pPlayer.getRank() + kFACTOR * (-1 + (attackerSUM - recentAttackers.size() * pPlayer.getRank()) / (recentAttackers.size() * rangeFACTOR));
			
			if (LOSERNEWRANK < rankFLOOR)
				LOSERNEWRANK = rankFLOOR;
			
			if (LOSERNEWRANK > rankCELING)
				LOSERNEWRANK = rankCELING;
			
			Math.round(LOSERNEWRANK);
				
			pPlayer.setRank((int)LOSERNEWRANK);
		}
	}
	
}
