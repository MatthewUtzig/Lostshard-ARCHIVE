package com.lostshard.lostshard.Main;

import com.lostshard.lostshard.Handlers.EnderdragonHandler;

import it.sauronsoftware.cron4j.Scheduler;

public class CustomSchedule {
	static Scheduler s = new Scheduler();
	public static void Schedule() {
		s.schedule("0 */4 * * *", new Runnable() {
			public void run() {
				EnderdragonHandler.resetDrake();
			}
		});
		s.start();
	}
	public static void stopSchedule() {
		if(s.isStarted())
			s.stop();
	}
}
