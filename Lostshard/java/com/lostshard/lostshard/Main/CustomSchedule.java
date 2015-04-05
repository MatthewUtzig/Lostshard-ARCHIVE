package com.lostshard.lostshard.Main;

import com.lostshard.lostshard.Handlers.EnderdragonHandler;
import com.lostshard.lostshard.Manager.PlotManager;

import it.sauronsoftware.cron4j.Scheduler;

public class CustomSchedule {
	static Scheduler s = new Scheduler();
	static Scheduler tax = new Scheduler();
	static PlotManager ptm = PlotManager.getManager();
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
	}
	public static void stopSchedule() {
		if(s.isStarted())
			s.stop();
		if(tax.isStarted())
			tax.stop();
	}
}
