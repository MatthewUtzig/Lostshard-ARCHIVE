package com.lostshard.Lostshard.Main;

import java.util.Date;
import java.util.Iterator;

import org.bukkit.scheduler.BukkitRunnable;

import com.lostshard.Lostshard.Handlers.CapturepointHandler;
import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.TaskManager;
import com.lostshard.Lostshard.Objects.Camp;
import com.lostshard.Lostshard.Skills.SurvivalismSkill;
import com.lostshard.Lostshard.Spells.MagicStructure;
import com.lostshard.Plots.PlotManager;

public class GameLoop extends BukkitRunnable {

	public static long tick = 0;
	public static long lastTickTime = 0;
	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	ClanManager cm = ClanManager.getManager();

	ChestRefillManager crm = ChestRefillManager.getManager();
	TaskManager tm = TaskManager.getManager();

	@SuppressWarnings("unused")
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
		this.pm.tick(delta, tick);
		MagicStructure.tickGlobal();
		// 5 sec loop
		if (tick % 600 == 0)
			this.crm.tick();
		Iterator<Camp> camps = SurvivalismSkill.getCamps().iterator();
		while(camps.hasNext()) {
			Camp camp = camps.next();
			camp.tick();
			if(camp.isDead)
				camps.remove();
		}
		if (tick % 10 == 0)
			CapturepointHandler.tick(delta);
		this.tm.tick();
	}
}