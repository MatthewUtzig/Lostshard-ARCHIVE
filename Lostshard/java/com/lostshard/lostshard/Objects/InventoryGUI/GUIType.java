package com.lostshard.lostshard.Objects.InventoryGUI;

import com.lostshard.lostshard.Objects.PseudoPlayer;

public enum GUIType {

	RUNEBOOK,
	SPELLBOOK,
	SPELLBOOKPAGE,
	SCROLLS,
	SKILLS;
	
	public InventoryGUI getGUI(PseudoPlayer pPlayer) {
		switch(this) {
		case RUNEBOOK:
			return new RunebookGUI(pPlayer);
		case SPELLBOOK:
			return new SpellbookGUI(pPlayer);
		case SCROLLS:
			return new ScrollGUI(pPlayer);
		case SKILLS:
			return new SkillsGUI(pPlayer);
		default:
			return null;
		}
	}
}
