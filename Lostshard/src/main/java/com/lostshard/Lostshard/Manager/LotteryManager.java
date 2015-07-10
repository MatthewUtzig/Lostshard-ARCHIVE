package com.lostshard.Lostshard.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.lostshard.Lostshard.Objects.Lottery;

public class LotteryManager {

	public static LotteryManager getManager() {
		return manager;
	}

	static LotteryManager manager = new LotteryManager();

	private Lottery lottery = null;

	private LotteryManager() {
	}

	public Lottery getLottery() {
		return this.lottery;
	}

	public void setLottery(Lottery lottery) {
		this.lottery = lottery;
	}

	public void tick() {
		if (this.getLottery() != null) {
			final Lottery lottery = this.getLottery();
			lottery.timesup();
			this.lottery = null;
		} else {
			this.setLottery(new Lottery());
			Bukkit.broadcastMessage(ChatColor.GREEN
					+ "The lottery have started, go to the nearst bank to bet.");
		}
	}
}
