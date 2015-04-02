package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Spells.Scroll;

public class SpellBook {

	SpellManager sm = SpellManager.getManager();
	
	private List<Scroll> spells = new ArrayList<Scroll>();

	public List<Scroll> getSpells() {
		return spells;
	}

	public void setSpells(List<Scroll> spells) {
		this.spells = spells;
	}
	
	public boolean containSpell(Scroll spellType) {
		return spells.contains(spellType);
	}
	
	public void addSpell(Scroll spell) {
		spells.add(spell);
	}
	
	public ArrayList<Spell> getSpellsOnPage(int pageNumber) {
		ArrayList<Spell> spellsOnPage = new ArrayList<Spell>();
		for(Scroll spell : spells) {
			Spell s = spell.getSpell();
			if(s.getPage() == pageNumber)
				spellsOnPage.add(s);
		}
		return spellsOnPage;
	}
	
}
