package com.lostshard.lostshard.Skills;


import com.lostshard.lostshard.Manager.SpellManager;

public class MagerySkill extends Skill {

	static SpellManager sm = SpellManager.getManager();
	
	public MagerySkill() {
		super();
		setName("Magery");
		setBaseProb(.2);
		setScaleConstant(60);
	}
}
