package com.lostshard.Lostshard.Tasks;

import com.lostshard.Lostshard.Manager.TaskManager;

public abstract class DelayedTask implements Task {

	private TaskManager tm = TaskManager.getManager();
	
	private int delay;
	
	public DelayedTask(int delay) {
		this.delay = delay;
		tm.add(this);
	}
	
	public int getDelayedTask() {
		return this.delay;
	}
	
	public void tick() {
			delay--;
		if(delay<=0)
			run();
	}
	
	public boolean remove() {
		return delay <= 0;
	}
}
