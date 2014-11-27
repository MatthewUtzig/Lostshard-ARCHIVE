package com.lostshard.lostshard.Skills;
	
public class Build {
	
	private Skill mining = new MiningSkill();
	private Skill blade = new BladesSkill();
	private Skill brawling = new BrawlingSkill();
	private Skill blackSmithy = new BlackSmithySkill();
	private Skill lumberjacking = new LumberjackingSkill();
	private Skill fishing = new FishingSkill();
	private Skill survivalism = new SurvivalismSkill();
	private Skill taming = new TamingSkill();
	private Skill Magery = new MagerySkill();
	
	public Skill getMining() {
		return mining;
	}
	
	public void setMining(Skill mining) {
		this.mining = mining;
	}
	
	public Skill getBlade() {
		return blade;
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
		return Magery;
	}

	public void setMagery(Skill magery) {
		Magery = magery;
	}

	public void setBlade(Skill blade) {
		this.blade = blade;
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
}
