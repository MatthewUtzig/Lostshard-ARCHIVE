package com.lostshard.lostshard.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.EnderdragonHandler;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

import it.sauronsoftware.cron4j.Scheduler;

public class CustomSchedule {
	static Scheduler s = new Scheduler();
	static Scheduler tax = new Scheduler();
	static Scheduler serviceMessage = new Scheduler();
	static PlotManager ptm = PlotManager.getManager();
	static PlayerManager pm = PlayerManager.getManager();
	public static void Schedule() {
		s.schedule("0 */4 * * *", new Runnable() {
			public void run() {
				EnderdragonHandler.resetDrake();
			}
		});
		s.start();
		tax.schedule("0 0 * * *", new Runnable() {
			public void run() {
				ptm.tax();
			}
		});
		tax.start();
		serviceMessage.schedule("30 */2 * * *", new Runnable() {
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					PseudoPlayer pseudoPlayer = pm.getPlayer(p);
					if(!pseudoPlayer.isSubscriber()) {
						Lostshard.log.finest(ChatColor.GOLD + "Enjoying the server? Consider subscribing for $10 a month. Visit "+ChatColor.UNDERLINE+"http://www.lostshard.com/donate"+ChatColor.RESET+ChatColor.GOLD+" for information on subscription benefits.");
						p.sendMessage(ChatColor.GOLD + "Enjoying the server? Consider subscribing for $10 a month. Visit "+ChatColor.UNDERLINE+"http://www.lostshard.com/donate"+ChatColor.RESET+ChatColor.GOLD+" for information on subscription benefits.");
					}
				}
			}
		});
		serviceMessage.start();
	}
	public static void stopSchedule() {
		if(s.isStarted())
			s.stop();
		if(tax.isStarted())
			tax.stop();
		if(serviceMessage.isStarted())
			serviceMessage.stop();
	}
}
