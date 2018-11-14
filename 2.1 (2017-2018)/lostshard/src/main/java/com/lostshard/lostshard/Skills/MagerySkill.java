package com.lostshard.lostshard.Skills;

import javax.persistence.Embeddable;

import org.bukkit.Material;

import com.lostshard.lostshard.Manager.SpellManager;

@Embeddable
public class MagerySkill extends Skill {

	static SpellManager sm = SpellManager.getManager();

	public MagerySkill() {
		super();
		this.setName("Magery");
		this.setMat(Material.STICK);
		this.setBaseProb(1);
		this.setScaleConstant(40);
		this.setMinGain(1);
		this.setMaxGain(10);
	}

	public MagerySkill(int lvl, boolean locked) {
		super(lvl, locked);
	}

	@Override
	public String howToGain() {
		return "You can gain magery by casting spells";
	}
}
