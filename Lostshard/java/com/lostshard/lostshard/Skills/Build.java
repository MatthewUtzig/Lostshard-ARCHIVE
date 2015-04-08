package com.lostshard.lostshard.Skills;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
	
public class Build {
	
	private int id;
	private Skill mining = new MiningSkill();
	private Skill blades = new BladesSkill();
	private Skill brawling = new BrawlingSkill();
	private Skill blackSmithy = new BlackSmithySkill();
	private Skill lumberjacking = new LumberjackingSkill();
	private Skill fishing = new FishingSkill();
	private Skill survivalism = new SurvivalismSkill();
	private Skill taming = new TamingSkill();
	private Skill magery = new MagerySkill();
	private Skill archery = new ArcherySkill();
	
	public Skill getMining() {
		return mining;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMining(Skill mining) {
		this.mining = mining;
	}
	
	public Skill getBlades() {
		return blades;
	}
	
	public Skill getLumberjacking() {
		return lumberjacking;
	}

	public void setLumberjacking(Skill lumberjacking) {
		this.lumberjacking = lumberjacking;
	}

	public Skill getFishing() {
		return fishing;
	}

	public void setFishing(Skill fishing) {
		this.fishing = fishing;
	}

	public Skill getSurvivalism() {
		return survivalism;
	}

	public void setSurvivalism(Skill survivalism) {
		this.survivalism = survivalism;
	}

	public Skill getTaming() {
		return taming;
	}

	public void setTaming(Skill taming) {
		this.taming = taming;
	}

	public Skill getMagery() {
		return magery;
	}

	public void setMagery(Skill magery) {
		this.magery = magery;
	}

	public void setBlades(Skill blades) {
		this.blades = blades;
	}

	public Skill getBrawling() {
		return brawling;
	}

	public void setBrawling(Skill brawling) {
		this.brawling = brawling;
	}

	public Skill getBlackSmithy() {
		return blackSmithy;
	}

	public void setBlackSmithy(Skill blackSmithy) {
		this.blackSmithy = blackSmithy;
	}
	
	public Skill getArchery() {
		return archery;
	}
	
	public void setArcher(Skill archery) {
		this.archery = archery;
	}
	
	public List<Skill> getSkills() {
		List<Skill> skills = new ArrayList<Skill>();
		skills.add(mining);
		skills.add(blades);
		skills.add(brawling);
		skills.add(blackSmithy);
		skills.add(lumberjacking);
		skills.add(fishing);
		skills.add(survivalism);
		skills.add(taming);
		skills.add(magery);
		skills.add(archery);
		skills.sort(new Comparator<Skill>()
                {
            public int compare(Skill skill1, Skill skill2)
            {
                return skill1.getName().compareTo(skill2.getName());
            }        
        });
		return skills;
	}

	public int getTotalSkillVal() {
		int total = 0;
		total+=blackSmithy.getLvl();
		total+=blades.getLvl();
		total+=brawling.getLvl();
		total+=fishing.getLvl();
		total+=lumberjacking.getLvl();
		total+=magery.getLvl();
		total+=mining.getLvl();
		total+=survivalism.getLvl();
		total+=taming.getLvl();
		return total;
	}
}
