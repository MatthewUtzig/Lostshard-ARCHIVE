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
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Utils.Output;

public class Lottery {
	
	PlayerManager pm = PlayerManager.getManager();
	
	private Map<UUID, Integer> bets = new HashMap<UUID, Integer>();
	
	public void bet(UUID uuid, int amount) {
		bets.put(uuid, amount);
	}
	
	public boolean isBetting(UUID uuid) {
		return bets.containsKey(uuid);
	}
	
	public int betting(UUID uuid) {
		return bets.get(uuid);
	}
	
	public int getTotalSum() {
		int total = 0;
		for(int i : bets.values())
			total += i;
		return total;
	}
	
	public UUID getWinner() {
		Random rand = new Random();
		int randWinnerNum = rand.nextInt(getTotalSum()+1);
		int curSum = 1;
		for(Entry<UUID, Integer> b : bets.entrySet()) {
			if(curSum >= randWinnerNum && randWinnerNum < curSum+b.getValue())
				return b.getKey();
			curSum += b.getValue();
		}
		return null;
	}
	
	public int getWinnerSum() {
		return (int) Math.floor(getTotalSum()*0.9);
	}

	public void timesup() {
		if(bets.size() >= 5 && getTotalSum() >= 1000) {
			UUID winnerUUID = getWinner();
			OfflinePlayer winner = Bukkit.getOfflinePlayer(winnerUUID);
			int winnerSum = getWinnerSum();
			for(Entry<UUID, Integer> b : bets.entrySet()) {
				OfflinePlayer op = Bukkit.getOfflinePlayer(b.getKey());
				if(op.getUniqueId().equals(winnerUUID)) {
					if(op.isOnline())
						op.getPlayer().sendMessage(ChatColor.GOLD+"Congratulations you won "+winnerSum+" in the lottery.");
					else
						Database.insertMessages(b.getKey(), "Congratulations you won "+winnerSum+" in the lottery, while offline.");
					PseudoPlayer winnerPseudo = pm.getPlayer(winnerUUID);
					winnerPseudo.setMoney((int) (winnerPseudo.getMoney()+winnerSum-Math.floor(b.getValue()*.9)+b.getValue()));
					if(!op.isOnline())
						Database.updatePlayer(winnerPseudo);
				}else{
					if(op.isOnline())
						op.getPlayer().sendMessage(ChatColor.GOLD+winner.getName()+" won "+winnerSum+"gc.");
					else
						Database.insertMessages(b.getKey(), "You lost the lottery while offline, "+b.getValue()+"gc.");
				}
			}
		}else{
			for(Entry<UUID, Integer> b : bets.entrySet()) {
				OfflinePlayer op = Bukkit.getOfflinePlayer(b.getKey());
				if(op.isOnline())
					Output.simpleError(op.getPlayer(), "The conditions for the lottery where not met, "+b.getValue()+".");
				else
					Database.insertMessages(b.getKey(), "The conditions for the lottery where not met, "+b.getValue()+".");
				PseudoPlayer pP = pm.getPlayer(b.getKey());
				pP.setMoney(pP.getMoney()+b.getValue());
				if(!op.isOnline())
					Database.updatePlayer(pP);
			}
		}
	}
}
