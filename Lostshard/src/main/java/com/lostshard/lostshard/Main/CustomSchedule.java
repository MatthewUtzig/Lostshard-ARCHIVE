package com.lostshard.lostshard.Main;

import com.lostshard.lostshard.Handlers.EnderdragonHandler;

import it.sauronsoftware.cron4j.Scheduler;

public class CustomSchedule {

	public static void Schedule() {
		Scheduler s = new Scheduler();
		s.schedule("0 */4 * * *", new Runnable() {
			public void run() {
				EnderdragonHandler.resetDrake();
			}
		});
		s.start();
	}
}
