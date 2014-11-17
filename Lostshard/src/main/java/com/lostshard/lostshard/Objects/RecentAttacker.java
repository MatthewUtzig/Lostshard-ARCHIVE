package com.lostshard.lostshard.Objects;

public class RecentAttacker {
	private String murdererUUID;
	private int ticksRemaining;
	private final int originalTicks;
	private boolean isDead = false;
	public boolean notCrim = false;
	
	public RecentAttacker(String murdererName, int ticksRemaining) {
		this.murdererUUID = murdererName;
		this.originalTicks = ticksRemaining;
		this.ticksRemaining = ticksRemaining;
	}
	
	public void tick() {
		if(!isDead) {
			ticksRemaining--;
			if(ticksRemaining <= 0)
				isDead = true;
		}
	}
	
	public String getName() {
		return murdererUUID;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public void resetTicks() {
		ticksRemaining = originalTicks; 
	}
}
