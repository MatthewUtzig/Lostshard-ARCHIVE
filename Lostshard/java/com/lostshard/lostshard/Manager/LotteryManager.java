package com.lostshard.lostshard.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.lostshard.lostshard.Objects.Lottery;

public class LotteryManager {

	static LotteryManager manager = new LotteryManager();
	private Lottery lottery = null;
	
	private LotteryManager() { }
	
	public static LotteryManager getManager() {
		return manager;
	}
	
	public Lottery getLottery() {
		return lottery;
	}

	public void setLottery(Lottery lottery) {
		this.lottery = lottery;
	}
	
	public void tick() {
		if(getLottery() != null) {
			Lottery lottery = getLottery();
			lottery.timesup();
		}else{
			setLottery(new Lottery());
			Bukkit.broadcastMessage(ChatColor.GREEN+"The lottery have started, go to the nearst bank to bet money.");
		}
	}
}
