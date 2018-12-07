package com.lostshard.Lostshard.Handlers;

import java.util.List;

import com.lostshard.Lostshard.Manager.NPCManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.Plots.NPC;
import com.lostshard.Plots.NPCType;

public class RankHandler {

	static PlayerManager pm = PlayerManager.getManager();

	public static void rank(PseudoPlayer pPlayer) {
		final List<RecentAttacker> recentAttackers = pPlayer.getRecentAttackers();
		if (recentAttackers.size() >= 1 && pPlayer.getTimer().isLastDeathOlder(300000)) {
			for (final RecentAttacker ra : recentAttackers) {
				final NPC guard = NPCManager.getManager().getByUUID(ra.getUUID());
				if (guard.getType().equals(NPCType.GUARD))
					return;
			}
			double attackerSUM = 0;
			final int rankFLOOR = 200;
			final int rankCELING = 3000;
			final int kFACTOR = 8;
			final int rangeFACTOR = 1600;

			for (final RecentAttacker recentAttacker : recentAttackers) {
				final PseudoPlayer recentAttackerPseudoPlayer = pm.getPlayer(recentAttacker.getUUID());
				attackerSUM += recentAttackerPseudoPlayer.getRank();
			}

			for (final RecentAttacker recentAttacker : recentAttackers) {
				final PseudoPlayer recentAttackerPseudoPlayer = pm.getPlayer(recentAttacker.getUUID());
				final int rank = recentAttackerPseudoPlayer.getRank();
				double ATTACKERNEWRANK = rank
						+ kFACTOR * (1 + (recentAttackers.size() * pPlayer.getRank() - attackerSUM)
								/ (recentAttackers.size() * rangeFACTOR)) / recentAttackers.size();

				if (ATTACKERNEWRANK < rankFLOOR)
					ATTACKERNEWRANK = rankFLOOR;

				if (ATTACKERNEWRANK > rankCELING)
					ATTACKERNEWRANK = rankCELING;

				ATTACKERNEWRANK = Math.round(ATTACKERNEWRANK);

				recentAttackerPseudoPlayer.setRank((int) ATTACKERNEWRANK);
			}

			double LOSERNEWRANK = pPlayer.getRank()
					+ kFACTOR * (-1 + (attackerSUM - recentAttackers.size() * pPlayer.getRank())
							/ (recentAttackers.size() * rangeFACTOR));

			if (LOSERNEWRANK < rankFLOOR)
				LOSERNEWRANK = rankFLOOR;

			if (LOSERNEWRANK > rankCELING)
				LOSERNEWRANK = rankCELING;

			LOSERNEWRANK = Math.round(LOSERNEWRANK);

			pPlayer.setRank((int) LOSERNEWRANK);
		}
	}

}
