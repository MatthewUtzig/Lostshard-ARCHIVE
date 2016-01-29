package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.lostshard.Lostshard.Manager.SpellManager;
import com.lostshard.Lostshard.Spells.Scroll;

@Embeddable
@Inheritance(strategy=InheritanceType.JOINED)
@Access(AccessType.FIELD)
public class SpellBook {

	@Transient
	SpellManager sm = SpellManager.getManager();
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)

	@CollectionTable
	@Enumerated(EnumType.STRING)
	private final List<Scroll> spells = new ArrayList<Scroll>();

	public void addSpell(Scroll spell) {
		if (!this.spells.contains(spell))
			this.spells.add(spell);
	}

	public boolean containSpell(Scroll spellType) {
		return this.spells.contains(spellType);
	}

	public List<Scroll> getSpells() {
		return this.spells;
	}

	public ArrayList<Scroll> getSpellsOnPage(int pageNumber) {
		final ArrayList<Scroll> spellsOnPage = new ArrayList<Scroll>();
		for (final Scroll scroll : this.spells)
			if (scroll.getPage() == pageNumber)
				spellsOnPage.add(scroll);
		return spellsOnPage;
	}
}
