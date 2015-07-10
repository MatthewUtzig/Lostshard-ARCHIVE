package com.lostshard.Lostshard.Skills;

import org.bukkit.Material;

import com.lostshard.Lostshard.Manager.SpellManager;

public class MagerySkill extends Skill {

	static SpellManager sm = SpellManager.getManager();

	public MagerySkill() {
		super();
		this.setName("Magery");
		this.setMat(Material.STICK);
		this.setBaseProb(2);
		this.setScaleConstant(60);
		this.setMinGain(1);
		this.setMaxGain(10);
	}

	@Override
	public String howToGain() {
		return "You can gain magery by casting spells";
	}
}
