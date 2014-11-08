package com.lostshard.lostshard.Skills;

public class Skill {

	private int lvl = 0;//0-1000 and displayed as lvl/10
	
	public int skillGain() {
		//Some xp code that makes it harder.
		return lvl/1000*2+1;
	}

	public int getLvl() {
		return lvl;
	}

	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	
}
