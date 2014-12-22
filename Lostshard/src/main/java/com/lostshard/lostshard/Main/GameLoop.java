package com.lostshard.lostshard.Main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.EnderdragonHandler;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class GameLoop extends BukkitRunnable {

	public static long tick = 0;

	@SuppressWarnings("unused")
	private final JavaPlugin plugin;

	public GameLoop(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static List<PseudoPlayer> playerUpdates = new ArrayList<PseudoPlayer>();
	public static List<Plot> plotUpdates = new ArrayList<Plot>();
	
	
	public void run() {
		if(Lostshard.isMysqlError())
			Lostshard.setMysqlError(!Database.testDatabaseConnection());
		else {
			EnderdragonHandler.tick();
			//5 sec loop
				if(tick % 10 == 0) {
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
			tick++;
		}
	}
}
