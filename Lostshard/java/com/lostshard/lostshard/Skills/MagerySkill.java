package com.lostshard.lostshard.Skills;


import org.bukkit.Material;

import com.lostshard.lostshard.Manager.SpellManager;

public class MagerySkill extends Skill {

	static SpellManager sm = SpellManager.getManager();
	
	public MagerySkill() {
		super();
		setName("Magery");
		setBaseProb(1);
		setScaleConstant(40);
		setMat(Material.STICK);
	}

	@Override
	public String howToGain() {
		return "You can gain magery by casting spells";
	}
}
