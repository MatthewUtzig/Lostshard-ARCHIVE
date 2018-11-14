package com.lostshard.BanManagement.Models;

public enum Reason {

	HACKING(1440, -1),
	EXPLOTING(1440, -1),
	SPAMMING(1, 1440),
	DISRESPECT(1, 60),
	OTHER(1, 14400);
	
	
	private final int min;
	private final int max;
	
	private Reason(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * @return the minimum ban time in minutters
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @return the maximum ban time in minutters
	 */
	public int getMax() {
		return max;
	}
	
}
