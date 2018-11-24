package com.lostshard.RPG;

public class TickObject {
	private int _ticksRemaining;
	private boolean _isDead = false;
	
	public TickObject(int ticksRemaining) {
		_ticksRemaining = ticksRemaining;
	}
	
	public void tick() {
		tickAction();
		if(!_isDead) {
			_ticksRemaining--;
			if(_ticksRemaining <= 0) {
				_isDead = true;
				died();
			}
		}
	}
	
	protected void tickAction() {
		
	}
	
	protected void died() {
		
	}
	
	public boolean isDead() {
		return _isDead;
	}
}
