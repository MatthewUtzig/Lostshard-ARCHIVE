package com.lostshard.lostshard.Skills;

public class Skill {

	private double baseProb = 0.2;
	private double scaleConstant = 25;
	
	private String name = "none";
	
	private int lvl = 0; // 0-1000 and displayed as level/10
	private boolean locked = false;

	public double getBaseProb() {
		return baseProb;
	}

	public void setBaseProb(double baseProb) {
		this.baseProb = baseProb;
	}

	public double getScaleConstant() {
		return scaleConstant;
	}

	public void setScaleConstant(double scaleConstant) {
		this.scaleConstant = scaleConstant;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int skillGain() {
		if(locked)
			return 0;
		if(lvl >= 1000)
			return 0;
		double RND = Math.random();
		int gain = RND <= baseProb * Math.exp(-lvl/10/scaleConstant) ? ((Double)Math.ceil(Math.random()*5)).intValue() : 0;
		lvl += gain;
		if(lvl > 1000) {
			gain -= lvl - 1000;
			lvl = 1000;
		}
		return gain;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
