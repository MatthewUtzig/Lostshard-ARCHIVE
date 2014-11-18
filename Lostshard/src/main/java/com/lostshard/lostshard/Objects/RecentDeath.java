package com.lostshard.lostshard.Objects;

import org.bukkit.entity.Entity;

public class RecentDeath {
	private Entity entity;
	private int ticksRemaining;
	private int originalTicks;

	public RecentDeath(Entity entity, int ticksRemaining) {
		this.entity = entity;
		this.ticksRemaining = ticksRemaining;
		this.setOriginalTicks(ticksRemaining);
	}

	public Entity getEntity() {
		return entity;
	}

	public int getTicksRemaining() {
		return ticksRemaining;
	}

	public int getOriginalTicks() {
		return originalTicks;
	}

	public void setOriginalTicks(int originalTicks) {
		this.originalTicks = originalTicks;
	}
}
