package com.lostshard.RPG;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RoboExplosion {
	private boolean _isDead = false;
	private String _playerName;
	private Location _location;
	private int _ticksRemaining;
	
	public RoboExplosion(Player player, int ticksRemaining) {
		_playerName = player.getName();
		_location = player.getLocation();
		_ticksRemaining = ticksRemaining;
	}
	
	public void tick() {
		if(!_isDead) {
			if(_ticksRemaining > 0)
				_ticksRemaining--;
			if(_ticksRemaining <= 0)
				_isDead = true;
		}
	}
	
	public String getName() {
		return _playerName;
	}
	
	public Location getLocation() {
		return _location;
	}
	
	public boolean isDead() {
		return _isDead;
	}
	
}
