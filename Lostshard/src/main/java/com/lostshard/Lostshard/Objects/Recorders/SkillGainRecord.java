package com.lostshard.Lostshard.Objects.Recorders;

public class SkillGainRecord extends Record {

	private String skill;
	private int currentLevel;
	private int gain;
	private int finalLevel;
	
	
	/**
	 * @param skill
	 * @param currentLevel
	 * @param gain
	 * @param finalLevel
	 */
	public SkillGainRecord(String skill, int currentLevel, int gain, int finalLevel) {
		super();
		this.skill = skill;
		this.currentLevel = currentLevel;
		this.gain = gain;
		this.finalLevel = finalLevel;
	}
	
	/**
	 * @return the skill
	 */
	public String getSkill() {
		return skill;
	}
	
	/**
	 * @param skill the skill to set
	 */
	public void setSkill(String skill) {
		this.skill = skill;
	}
	
	/**
	 * @return the currentLevel
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	/**
	 * @param currentLevel the currentLevel to set
	 */
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	
	/**
	 * @return the gain
	 */
	public int getGain() {
		return gain;
	}
	
	/**
	 * @param gain the gain to set
	 */
	public void setGain(int gain) {
		this.gain = gain;
	}
	
	/**
	 * @return the finalLevel
	 */
	public int getFinalLevel() {
		return finalLevel;
	}
	
	/**
	 * @param finalLevel the finalLevel to set
	 */
	public void setFinalLevel(int finalLevel) {
		this.finalLevel = finalLevel;
	}
	
}
