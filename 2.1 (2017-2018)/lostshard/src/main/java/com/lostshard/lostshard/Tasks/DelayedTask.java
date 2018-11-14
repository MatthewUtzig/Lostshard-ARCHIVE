package com.lostshard.lostshard.Tasks;

import com.lostshard.lostshard.Manager.TaskManager;

public abstract class DelayedTask implements Task {

	private final TaskManager tm = TaskManager.getManager();

	private int delay;

	public DelayedTask(int delay) {
		this.delay = delay;
		this.tm.add(this);
	}

	public int getDelayedTask() {
		return this.delay;
	}

	public boolean remove() {
		return this.delay <= 0;
	}

	public void tick() {
		this.delay--;
		if (this.delay <= 0)
			this.run();
	}
}
