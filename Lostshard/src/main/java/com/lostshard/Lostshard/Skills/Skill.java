package com.lostshard.Lostshard.Skills;

import org.bukkit.Material;

import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;

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
		return this.baseProb;
	}

	public int getLvl() {
		return this.lvl;
	}

	public Material getMat() {
		return this.mat;
	}

	public int getMaxGain() {
		return this.maxGain;
	}

	public int getMinGain() {
		return this.minGain;
	}

	public String getName() {
		return this.name;
	}

	public double getScaleConstant() {
		return this.scaleConstant;
	}

	abstract public String howToGain();

	public boolean isLocked() {
		return this.locked;
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
		if (this.locked)
			return 0;
		if (pPlayer.getCurrentBuild().getTotalSkillVal() >= pPlayer
				.getMaxSkillValTotal())
			return 0;
		if (this.lvl >= 1000)
			return 0;
		final double RND = Math.random();
		int gain = RND <= this.baseProb
				* Math.exp(-this.lvl / 10 / this.scaleConstant) ? ((Double) Math
				.ceil(Math.random() * this.maxGain + this.minGain)).intValue()
				: 0;
		this.lvl += gain;
		if (this.lvl > 1000) {
			gain -= this.lvl - 1000;
			this.lvl = 1000;
		}
		return gain;
	}
}
