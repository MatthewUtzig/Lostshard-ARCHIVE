package com.lostshard.lostshard.Skills;

import org.bukkit.Material;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public abstract class Skill {

	static PlayerManager pm = PlayerManager.getManager();
	static PlotManager ptm = PlotManager.getManager();
	
	private double baseProb = 0.2;
	private double scaleConstant = 25;
	private int minGain = 0;
	private int maxGain = 5;
	private Material mat;
	
	private String name = "none";
	
	private int lvl = 0; // 0-1000 and displayed as level/10
	private boolean locked = false;

	public double getBaseProb() {
		return baseProb;
	}

	public int getLvl() {
		return lvl;
	}

	public Material getMat() {
		return mat;
	}

	public int getMaxGain() {
		return maxGain;
	}

	public int getMinGain() {
		return minGain;
	}

	public String getName() {
		return name;
	}

	public double getScaleConstant() {
		return scaleConstant;
	}

	abstract public String howToGain();
	
	public boolean isLocked() {
		return locked;
	}

	public void setBaseProb(double baseProb) {
		this.baseProb = baseProb;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}

	public void setMat(Material mat) {
		this.mat = mat;
	}

	public void setMaxGain(int maxGain) {
		this.maxGain = maxGain;
	}

	public void setMinGain(int minGain) {
		this.minGain = minGain;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScaleConstant(double scaleConstant) {
		this.scaleConstant = scaleConstant;
	}

	public int skillGain(PseudoPlayer pPlayer) {
		if(locked)
			return 0;
		if(pPlayer.getCurrentBuild().getTotalSkillVal() >= pPlayer.getMaxSkillValTotal())
			return 0;
		if(lvl >= 1000)
			return 0;
		double RND = Math.random();
		int gain = RND <= baseProb * Math.exp(-lvl/10/scaleConstant) ? ((Double)Math.ceil(Math.random()*maxGain+minGain)).intValue() : 0;
		lvl += gain;
		if(lvl > 1000) {
			gain -= lvl - 1000;
			lvl = 1000;
		}
		return gain;
	}
}
