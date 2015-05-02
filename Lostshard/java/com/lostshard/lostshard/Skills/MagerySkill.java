package com.lostshard.lostshard.Skills;

import org.bukkit.Material;

import com.lostshard.lostshard.Manager.SpellManager;

public class MagerySkill extends Skill {

	static SpellManager sm = SpellManager.getManager();

	public MagerySkill() {
		super();
		this.setName("Magery");
		this.setBaseProb(1);
		this.setScaleConstant(40);
		this.setMat(Material.STICK);
	}

	@Override
	public String howToGain() {
		return "You can gain magery by casting spells";
	}
}
