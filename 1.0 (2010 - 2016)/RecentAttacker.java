package com.lostshard.RPG;

public class RecentAttacker {
	private String _murdererName;
	private int _ticksRemaining;
	private final int _originalTicks;
	private boolean _isDead = false;
	public boolean _notCrim = false;
	
	public RecentAttacker(String murdererName, int ticksRemaining) {
		_murdererName = murdererName;
		_originalTicks = _ticksRemaining = ticksRemaining;
	}
	
	public void tick() {
		if(!_isDead) {
			_ticksRemaining--;
			if(_ticksRemaining <= 0)
				_isDead = true;
		}
	}
	
	public String getName() {
		return _murdererName;
	}
	
	public boolean isDead() {
		return _isDead;
	}
	
	public void resetTicks() {
		_ticksRemaining = _originalTicks; 
	}
}
