package com.lostshard.lostshard.Main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.CapturepointHandler;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.Camp;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Skills.SurvivalismSkill;
import com.lostshard.lostshard.Spells.MagicStructure;

public class GameLoop extends BukkitRunnable {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	ClanManager cm = ClanManager.getManager();
	
	public static long tick = 0;
	public static long lastTickTime = 0;
	
	@SuppressWarnings("unused")
	private final JavaPlugin plugin;

	public GameLoop(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static List<PseudoPlayer> playerUpdates = new ArrayList<PseudoPlayer>();
	public static List<Plot> plotUpdates = new ArrayList<Plot>();
	public static List<Clan> clanUpdates = new ArrayList<Clan>();
	
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
			pm.tick(delta, tick);
			MagicStructure.tickGlobal();
			//5 sec loop
			if(tick % 50 == 0) {
				for(PseudoPlayer p : pm.getPlayers())
					if(p.isUpdate())
						playerUpdates.add(p);
				if(!playerUpdates.isEmpty())
					Database.updatePlayers(playerUpdates);
				playerUpdates.clear();
				for(Plot p : ptm.getPlots())
					if(p.isUpdate())
						plotUpdates.add(p);
				if(!plotUpdates.isEmpty())
					Database.updatePlots(plotUpdates);
				plotUpdates.clear();
				for(Clan c : cm.getClans())
					if(c.isUpdate())
						clanUpdates.add(c);
				if(!clanUpdates.isEmpty())
					Database.updateClans(clanUpdates);
				clanUpdates.clear();
			}
			for(Camp camp : SurvivalismSkill.getCamps())
				camp.tick();
			if(tick % 10 == 0)
				CapturepointHandler.tick(delta);
		}
	}
}