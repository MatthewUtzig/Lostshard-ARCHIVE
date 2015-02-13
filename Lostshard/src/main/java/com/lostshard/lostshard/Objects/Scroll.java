package com.lostshard.lostshard.Objects;

import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Spells.Spell;

public class Scroll {
	
	SpellManager sm = SpellManager.getManager();
	
	private int id;
	private String spellName;
	private int playerId;

	public Scroll(int id, Spell spell, int playerId) {
		super();
		this.setId(id);
		this.spellName = spell.getName();
		this.setPlayerId(playerId);
	}
	
	public Scroll(int id, String scroll, int playerId) {
		super();
		this.setId(id);
		this.spellName = scroll;
		this.setPlayerId(playerId);
	}
	
	public String getSpellName() {
		return spellName;
	}

	public void setSpellName(String spellName) {
		this.spellName = spellName;
	}
	
	public Spell getSpell() {
		return sm.getSpellByName(spellName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

}
