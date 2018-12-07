package com.lostshard.RPG.Skills;
import com.lostshard.RPG.Spells.*;

import java.util.ArrayList;

public class Spellbook {
	private ArrayList<Spell> _spells;
	
	public Spellbook() {
		_spells = new ArrayList<Spell>();
	}
	
	public boolean hasSpellByName(String spellName) {
		for(Spell spell : _spells) {
			if(spell.getName().equalsIgnoreCase(spellName))
				return true;
		}
		return false;
	}
	
	public Spell getSpellByName(String spellName) {
		for(Spell spell : _spells) {
			if(spell.getName().equalsIgnoreCase(spellName))
				return spell;
		}
		return null;
	}
	
	public ArrayList<Spell> getSpells() {
		return _spells;
	}
	
	public void addSpell(Spell spell) {
		_spells.add(spell);
	}
	
	public ArrayList<Spell> getSpellsOnPage(int pageNumber) {
		ArrayList<Spell> spellsOnPage = new ArrayList<Spell>();
		for(Spell spell : _spells) {
			if(spell.getPageNumber() == pageNumber)
				spellsOnPage.add(spell);
		}
		return spellsOnPage;
	}
}
