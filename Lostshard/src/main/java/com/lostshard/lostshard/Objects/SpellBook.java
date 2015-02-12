package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Spells.Spell;

public class SpellBook {

	SpellManager sm = SpellManager.getManager();
	
	private List<String> spells = new ArrayList<String>();

	public List<String> getSpells() {
		return spells;
	}

	public void setSpells(List<String> spells) {
		this.spells = spells;
	}
	
	public boolean containSpell(String name) {
		for(String s : spells)
			if(s.trim().replace(" ", "").equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public void addSpell(Spell spell) {
		spells.add(spell.getName());
	}
	
}
