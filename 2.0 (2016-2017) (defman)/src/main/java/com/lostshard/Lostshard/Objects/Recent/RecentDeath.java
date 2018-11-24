package com.lostshard.Lostshard.Objects.Recent;

import org.bukkit.entity.Entity;

public class RecentDeath {
	private final Entity entity;
	private final int ticksRemaining;
	private int originalTicks;

	public RecentDeath(Entity entity, int ticksRemaining) {
		this.entity = entity;
		this.ticksRemaining = ticksRemaining;
		this.setOriginalTicks(ticksRemaining);
	}

	public Entity getEntity() {
		return this.entity;
	}

	public int getOriginalTicks() {
		return this.originalTicks;
	}

	public int getTicksRemaining() {
		return this.ticksRemaining;
	}

	public void setOriginalTicks(int originalTicks) {
		this.originalTicks = originalTicks;
	}
}
