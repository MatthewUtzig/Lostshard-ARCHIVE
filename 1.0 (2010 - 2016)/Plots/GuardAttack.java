package com.lostshard.RPG.Plots;

import me.neodork.npclib.entity.HumanNPC;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class GuardAttack {
	HumanNPC _guard;
	Player _target;
	Location _originalLocation;
	int _ticksRemaining;
	boolean _isDead = false;;
	
	public GuardAttack(HumanNPC guard, Player target, Location originalLocation, int ticks) {
		_guard = guard;
		_target = target;
		_originalLocation = originalLocation;
		_ticksRemaining = ticks;
	}
	
	public void tick() {
		if(_ticksRemaining > 0) {
			_ticksRemaining--;
			if(_ticksRemaining <= 0)
				_isDead = true;
		}
	}
	
	public boolean isDead() {
		return _isDead;
	}
	
	public int getTicksRemaining() {
		return _ticksRemaining;
	}
	
	public Player getTargetPlayer() {
		return _target;
	}
	
	public HumanNPC getGuard() {
		return _guard;
	}
	
	public Location getOriginalLocation() {
		return _originalLocation;
	}
}
