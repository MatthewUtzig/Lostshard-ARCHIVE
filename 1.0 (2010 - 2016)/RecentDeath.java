package com.lostshard.RPG;

import org.bukkit.entity.Entity;

public class RecentDeath {
	private Entity _entity;
	private int _ticksRemaining;
	
	public RecentDeath(Entity entity, int ticksRemaining) {
		_entity = entity;
		_ticksRemaining = ticksRemaining;
	}
	
	public Entity getEntity() {
		return _entity;
	}
	
	public int getTicksRemaining() {
		return _ticksRemaining;
	}
}
