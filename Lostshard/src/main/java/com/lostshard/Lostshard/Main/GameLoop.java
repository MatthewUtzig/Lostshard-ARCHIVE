package com.lostshard.Lostshard.Main;

import java.util.Date;

import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.Lostshard.Database.Database;
import com.lostshard.Lostshard.Handlers.CapturepointHandler;
import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Manager.TaskManager;
import com.lostshard.Lostshard.Objects.Camp;
import com.lostshard.Lostshard.Skills.SurvivalismSkill;
import com.lostshard.Lostshard.Spells.MagicStructure;

public class GameLoop extends BukkitRunnable {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	ClanManager cm = ClanManager.getManager();
	ChestRefillManager crm = ChestRefillManager.getManager();
	TaskManager tm = TaskManager.getManager();
	
	public static long tick = 0;
	public static long lastTickTime = 0;

	private final Lostshard plugin;

	public GameLoop(Lostshard plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		
		final Date date = new Date();
		double delta = 1;
		if (lastTickTime == 0)
			lastTickTime = date.getTime();
		else {
			final double diff = date.getTime() - lastTickTime;
			delta = diff / 100;
			lastTickTime = date.getTime();
		}
		tick++;
		if (plugin.isMysqlError())
			Lostshard.setMysqlError(!Database.testDatabaseConnection());
		else {
			this.pm.tick(delta, tick);
			MagicStructure.tickGlobal();
			// 5 sec loop
			if (tick % 600 == 0)
				this.crm.tick();
			for (final Camp camp : SurvivalismSkill.getCamps())
				camp.tick();
			if (tick % 10 == 0)
				CapturepointHandler.tick(delta);
			tm.tick();
		}
	}
}