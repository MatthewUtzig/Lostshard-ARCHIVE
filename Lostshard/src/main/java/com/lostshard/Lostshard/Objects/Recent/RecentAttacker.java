package com.lostshard.Lostshard.Objects.Recent;

import java.util.UUID;

public class RecentAttacker {
	private final UUID murdererUUID;
	private int ticksRemaining;
	private final int originalTicks;
	private boolean isDead = false;
	private boolean notCrim = false;

	public RecentAttacker(UUID murdererName, int ticksRemaining) {
		this.murdererUUID = murdererName;
		this.originalTicks = ticksRemaining;
		this.ticksRemaining = ticksRemaining;
	}

	public UUID getUUID() {
		return this.murdererUUID;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public boolean isNotCrim() {
		return this.notCrim;
	}

	public boolean isNotCrime() {
		return this.notCrim;
	}

	public void resetTicks() {
		this.ticksRemaining = this.originalTicks;
	}

	public void setNotCrim(boolean notCrim) {
		this.notCrim = notCrim;
	}

	public void tick() {
		if (!this.isDead) {
			this.ticksRemaining--;
			if (this.ticksRemaining <= 0)
				this.isDead = true;
		}
	}
}
