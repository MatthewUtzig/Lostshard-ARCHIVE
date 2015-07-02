package com.lostshard.lostshard.Tasks;

public abstract class DelayedTask implements Task {

	private int delay;
	
	public DelayedTask(int delay) {
		this.delay = delay;
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
