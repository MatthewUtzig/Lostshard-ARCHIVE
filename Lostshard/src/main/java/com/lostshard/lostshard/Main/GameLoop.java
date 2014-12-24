package com.lostshard.lostshard.Main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.EnderdragonHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class GameLoop extends BukkitRunnable {

	public static long tick = 0;
	public static long lastTickTime = 0;
	
	@SuppressWarnings("unused")
	private final JavaPlugin plugin;

	public GameLoop(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static List<PseudoPlayer> playerUpdates = new ArrayList<PseudoPlayer>();
	public static List<Plot> plotUpdates = new ArrayList<Plot>();
	
	
	public void run() {
		Date date = new Date();
		double delta = 1;
		if(lastTickTime == 0)
			lastTickTime = date.getTime();
		else {
			double diff = (double)(date.getTime() - lastTickTime);
			delta = diff/100;
			lastTickTime = date.getTime();
		}
		tick++;
		if(Lostshard.isMysqlError())
			Lostshard.setMysqlError(!Database.testDatabaseConnection());
		else {
			EnderdragonHandler.tick();
			PseudoPlayerHandler.tick(delta, tick);
			//5 sec loop
				if(tick % 50 == 0) {
					for(PseudoPlayer p : Lostshard.getRegistry().getPlayers())
						if(p.isUpdate())
							playerUpdates.add(p);
					if(!playerUpdates.isEmpty())
						Database.updatePlayers(playerUpdates);
					playerUpdates.clear();
					for(Plot p : Lostshard.getRegistry().getPlots())
						if(p.isUpdate())
							plotUpdates.add(p);
					if(!plotUpdates.isEmpty())
						Database.updatePlots(plotUpdates);
					plotUpdates.clear();
				}
				if(tick % 18000 == 0){
	 				for(Player p : Bukkit.getOnlinePlayers()) {
	 					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(p);
	 					if(!pseudoPlayer.isSubscriber())
	 						p.sendMessage(ChatColor.GOLD + "Enjoying the server? Consider subscribing for $10 a month. Visit http://www.lostshard.com/donate for information on subscription benefits.");
	 			}
	    	}
		}
	}
}
