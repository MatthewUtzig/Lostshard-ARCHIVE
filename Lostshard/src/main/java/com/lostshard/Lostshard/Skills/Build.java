package com.lostshard.Lostshard.Skills;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;


@Embeddable
@Access(AccessType.FIELD)
public class Build {
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "mining_level")),
		@AttributeOverride(name="locked", column=@Column(name = "mining_locked"))
	})
	private MiningSkill mining = new MiningSkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "blades_level")),
		@AttributeOverride(name="locked", column=@Column(name = "blades_locked"))
	})
	private BladesSkill blades = new BladesSkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "brawling_level")),
		@AttributeOverride(name="locked", column=@Column(name = "brawling_locked"))
	})
	private BrawlingSkill brawling = new BrawlingSkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "blackSmithy_level")),
		@AttributeOverride(name="locked", column=@Column(name = "blackSmithy_locked"))
	})
	private BlackSmithySkill blackSmithy = new BlackSmithySkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "lumberjacking_level")),
		@AttributeOverride(name="locked", column=@Column(name = "lumberjacking_locked"))
	})
	private LumberjackingSkill lumberjacking = new LumberjackingSkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "fishing_level")),
		@AttributeOverride(name="locked", column=@Column(name = "fishing_locked"))
	})
	private FishingSkill fishing = new FishingSkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "survivalism_level")),
		@AttributeOverride(name="locked", column=@Column(name = "survivalism_locked"))
	})
	private SurvivalismSkill survivalism = new SurvivalismSkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "taming_level")),
		@AttributeOverride(name="locked", column=@Column(name = "taming_locked"))
	})
	private TamingSkill taming = new TamingSkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "magery_level")),
		@AttributeOverride(name="locked", column=@Column(name = "magery_locked"))
	})
	private MagerySkill magery = new MagerySkill();
	
	@AttributeOverrides({
		@AttributeOverride(name="lvl", column=@Column(name = "archery_level")),
		@AttributeOverride(name="locked", column=@Column(name = "archery_locked"))
	})
	private ArcherySkill archery = new ArcherySkill();

	public Build() {
	}

	public ArcherySkill getArchery() {
		return this.archery;
	}
	
	public BlackSmithySkill getBlackSmithy() {
		return this.blackSmithy;
	}
	
	public BladesSkill getBlades() {
		return this.blades;
	}
	
	public BrawlingSkill getBrawling() {
		return this.brawling;
	}
	
	public FishingSkill getFishing() {
		return this.fishing;
	}
	
	public LumberjackingSkill getLumberjacking() {
		return this.lumberjacking;
	}
	
	public MagerySkill getMagery() {
		return this.magery;
	}
	
	public MiningSkill getMining() {
		return this.mining;
	}
	
	public SurvivalismSkill getSurvivalism() {
		return this.survivalism;
	}
	
	public TamingSkill getTaming() {
		return this.taming;
	}

	public void setArchery(ArcherySkill archery) {
		this.archery = archery;
	}

	public void setBlackSmithy(BlackSmithySkill blackSmithy) {
		this.blackSmithy = blackSmithy;
	}

	public void setBlades(BladesSkill blades) {
		this.blades = blades;
	}

	public void setBrawling(BrawlingSkill brawling) {
		this.brawling = brawling;
	}

	public void setFishing(FishingSkill fishing) {
		this.fishing = fishing;
	}

	public void setLumberjacking(LumberjackingSkill lumberjacking) {
		this.lumberjacking = lumberjacking;
	}

	public void setMagery(MagerySkill magery) {
		this.magery = magery;
	}

	public void setMining(MiningSkill mining) {
		this.mining = mining;
	}

	public void setSurvivalism(SurvivalismSkill survivalism) {
		this.survivalism = survivalism;
	}

	public void setTaming(TamingSkill taming) {
		this.taming = taming;
	}

	@Transient
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
	
	@Transient
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
		total += this.archery.getLvl();
		return total;
	}
	
	@Transient
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
