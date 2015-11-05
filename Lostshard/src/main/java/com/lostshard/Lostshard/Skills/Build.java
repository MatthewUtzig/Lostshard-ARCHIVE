package com.lostshard.Lostshard.Skills;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="skills")
public class Build {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
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

	public Build(int id) {
		this.id = id;
	}
	
	public Skill getArchery() {
		return this.archery;
	}

	public Skill getBlackSmithy() {
		return this.blackSmithy;
	}

	public Skill getBlades() {
		return this.blades;
	}

	public Skill getBrawling() {
		return this.brawling;
	}

	public Skill getFishing() {
		return this.fishing;
	}

	public int getId() {
		return this.id;
	}

	public Skill getLumberjacking() {
		return this.lumberjacking;
	}

	public Skill getMagery() {
		return this.magery;
	}

	public Skill getMining() {
		return this.mining;
	}

	public List<Skill> getSkills() {
		final List<Skill> skills = new ArrayList<Skill>();
		skills.add(this.mining);
		skills.add(this.blades);
		skills.add(this.brawling);
		skills.add(this.blackSmithy);
		skills.add(this.lumberjacking);
		skills.add(this.fishing);
		skills.add(this.survivalism);
		skills.add(this.taming);
		skills.add(this.magery);
		skills.add(this.archery);
		skills.sort((skill1, skill2) -> skill1.getName().compareTo(
				skill2.getName()));
		return skills;
	}

	public Skill getSurvivalism() {
		return this.survivalism;
	}

	public Skill getTaming() {
		return this.taming;
	}

	public int getTotalSkillVal() {
		int total = 0;
		total += this.blackSmithy.getLvl();
		total += this.blades.getLvl();
		total += this.brawling.getLvl();
		total += this.fishing.getLvl();
		total += this.lumberjacking.getLvl();
		total += this.magery.getLvl();
		total += this.mining.getLvl();
		total += this.survivalism.getLvl();
		total += this.taming.getLvl();
		return total;
	}

	public void setArcher(Skill archery) {
		this.archery = archery;
	}

	public void setBlackSmithy(Skill blackSmithy) {
		this.blackSmithy = blackSmithy;
	}

	public void setBlades(Skill blades) {
		this.blades = blades;
	}

	public void setBrawling(Skill brawling) {
		this.brawling = brawling;
	}

	public void setFishing(Skill fishing) {
		this.fishing = fishing;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLumberjacking(Skill lumberjacking) {
		this.lumberjacking = lumberjacking;
	}

	public void setMagery(Skill magery) {
		this.magery = magery;
	}

	public void setMining(Skill mining) {
		this.mining = mining;
	}

	public void setSurvivalism(Skill survivalism) {
		this.survivalism = survivalism;
	}

	public void setTaming(Skill taming) {
		this.taming = taming;
	}

	public Skill getSkillByName(String name) {
		switch(name.toLowerCase()) {
		case "mining": return this.getMining();
		case "lumberjacking": return this.getLumberjacking();
		case "blacksmithy": return this.getBlackSmithy();
		case "blades": return this.getBlades();
		case "archery": return this.getArchery();
		case "brawling": return this.getBrawling();
		case "fishing": return this.getFishing();
		case "survivalism": return this.getSurvivalism();
		case "taming": return this.getTaming();
		case "magery": return this.getMagery();
		default: return null;
		}
	}
}
