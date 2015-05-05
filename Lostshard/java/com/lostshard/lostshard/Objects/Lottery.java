package com.lostshard.lostshard.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Database.Mappers.PlayerMapper;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Utils.Output;

public class Lottery {

	PlayerManager pm = PlayerManager.getManager();

	private final Map<UUID, Integer> bets = new HashMap<UUID, Integer>();

	public void bet(UUID uuid, int amount) {
		this.bets.put(uuid, amount);
	}

	public int betting(UUID uuid) {
		return this.bets.get(uuid);
	}

	public int getTotalSum() {
		int total = 0;
		for (final int i : this.bets.values())
			total += i;
		return total;
	}

	public UUID getWinner() {
		final Random rand = new Random();
		final int randWinnerNum = rand.nextInt(this.getTotalSum() + 1);
		int curSum = 1;
		for (final Entry<UUID, Integer> b : this.bets.entrySet()) {
			if (curSum >= randWinnerNum
					&& randWinnerNum < curSum + b.getValue())
				return b.getKey();
			curSum += b.getValue();
		}
		return null;
	}

	public int getWinnerSum() {
		return (int) Math.floor(this.getTotalSum() * 0.9);
	}

	public boolean isBetting(UUID uuid) {
		return this.bets.containsKey(uuid);
	}

	public void timesup() {
		if (this.bets.size() >= 5 && this.getTotalSum() >= 1000) {
			final UUID winnerUUID = this.getWinner();
			final OfflinePlayer winner = Bukkit.getOfflinePlayer(winnerUUID);
			final int winnerSum = this.getWinnerSum();
			for (final Entry<UUID, Integer> b : this.bets.entrySet()) {
				final OfflinePlayer op = Bukkit.getOfflinePlayer(b.getKey());
				if (op.getUniqueId().equals(winnerUUID)) {
					if (op.isOnline())
						op.getPlayer().sendMessage(
								ChatColor.GOLD + "Congratulations you won "
										+ winnerSum + " in the lottery.");
					else
						Database.insertMessages(b.getKey(),
								"Congratulations you won " + winnerSum
										+ " in the lottery, while offline.");
					final PseudoPlayer winnerPseudo = this.pm
							.getPlayer(winnerUUID);
					winnerPseudo.setMoney((int) (winnerPseudo.getMoney()
							+ winnerSum - Math.floor(b.getValue() * .9) + b
							.getValue()));
					if (!op.isOnline())
						PlayerMapper.updatePlayer(winnerPseudo);
				} else if (op.isOnline())
					op.getPlayer().sendMessage(
							ChatColor.GOLD + winner.getName() + " won "
									+ winnerSum + "gc.");
				else
					Database.insertMessages(
							b.getKey(),
							"You lost the lottery while offline, "
									+ b.getValue() + "gc.");
			}
		} else
			for (final Entry<UUID, Integer> b : this.bets.entrySet()) {
				final OfflinePlayer op = Bukkit.getOfflinePlayer(b.getKey());
				if (op.isOnline())
					Output.simpleError(op.getPlayer(),
							"The conditions for the lottery where not met, "
									+ b.getValue() + ".");
				else
					Database.insertMessages(b.getKey(),
							"The conditions for the lottery where not met, "
									+ b.getValue() + ".");
				final PseudoPlayer pP = this.pm.getPlayer(b.getKey());
				pP.setMoney(pP.getMoney() + b.getValue());
				if (!op.isOnline())
					PlayerMapper.updatePlayer(pP);
			}
	}
}
