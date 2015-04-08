package com.lostshard.lostshard.Objects.InventoryGUI;

import com.lostshard.lostshard.Objects.PseudoPlayer;

public enum GUIType {

	RUNEBOOK,
	SPELLBOOK;
	
	public InventoryGUI getGUI(PseudoPlayer pPlayer) {
		switch(this) {
		case RUNEBOOK:
			return new RunebookGUI(pPlayer);
		case SPELLBOOK:
			return new SpellbookGUI(pPlayer);
		default:
			return null;
		}
	}
}
