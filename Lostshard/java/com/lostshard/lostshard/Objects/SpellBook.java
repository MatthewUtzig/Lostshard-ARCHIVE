package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Serializer;

public class SpellBook {

	SpellManager sm = SpellManager.getManager();
	
	private List<Scroll> spells = new ArrayList<Scroll>();

	public List<Scroll> getSpells() {
		return spells;
	}
	
	public boolean containSpell(Scroll spellType) {
		return spells.contains(spellType);
	}
	
	public void addSpell(Scroll spell) {
		if(!spells.contains(spell))
			spells.add(spell);
	}
	
	public ArrayList<Scroll> getSpellsOnPage(int pageNumber) {
		ArrayList<Scroll> spellsOnPage = new ArrayList<Scroll>();
		for(Scroll scroll : spells) {
			if(scroll.getPage() == pageNumber)
				spellsOnPage.add(scroll);
		}
		return spellsOnPage;
	}

	public String toJson() {
		List<String> tjson = new ArrayList<String>();
		for(Scroll s : spells)
			tjson.add(s.getName());
		return Serializer.serializeStringArray(tjson);
	}
}
