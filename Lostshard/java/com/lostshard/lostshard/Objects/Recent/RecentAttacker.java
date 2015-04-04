package com.lostshard.lostshard.Objects.Recent;

import java.util.UUID;

public class RecentAttacker {
	private UUID murdererUUID;
	private int ticksRemaining;
	private final int originalTicks;
	private boolean isDead = false;
	private boolean notCrim = false;

	public RecentAttacker(UUID murdererName, int ticksRemaining) {
		this.murdererUUID = murdererName;
		this.originalTicks = ticksRemaining;
		this.ticksRemaining = ticksRemaining;
	}

	public void tick() {
		if (!isDead) {
			ticksRemaining--;
			if (ticksRemaining <= 0)
				isDead = true;
		}
	}

	public UUID getUUID() {
		return murdererUUID;
	}

	public boolean isDead() {
		return isDead;
	}

	public void resetTicks() {
		ticksRemaining = originalTicks;
	}

	public boolean isNotCrim() {
		return notCrim;
	}

	public void setNotCrim(boolean notCrim) {
		this.notCrim = notCrim;
	}
	
	public boolean isNotCrime()	{
		return this.notCrim;
	}
}
