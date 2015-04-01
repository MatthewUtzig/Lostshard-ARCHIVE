package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Spells.Spell.SpellType;

public class SpellBook {

	SpellManager sm = SpellManager.getManager();
	
	private List<SpellType> spells = new ArrayList<SpellType>();

	public List<SpellType> getSpells() {
		return spells;
	}

	public void setSpells(List<SpellType> spells) {
		this.spells = spells;
	}
	
	public boolean containSpell(SpellType spellType) {
		return spells.contains(spellType);
	}
	
	public void addSpell(SpellType spell) {
		spells.add(spell);
	}
	
	public ArrayList<Spell> getSpellsOnPage(int pageNumber) {
		ArrayList<Spell> spellsOnPage = new ArrayList<Spell>();
		for(SpellType spell : spells) {
			Spell s = spell.getSpell();
			if(s.getPage() == pageNumber)
				spellsOnPage.add(s);
		}
		return spellsOnPage;
	}
	
}
