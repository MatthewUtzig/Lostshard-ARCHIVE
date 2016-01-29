package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Embeddable
@Access(AccessType.FIELD)
public class Runebook {
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)

	@CollectionTable
	private List<Rune> runes;
	
	public Runebook() {
		this.runes = new ArrayList<Rune>();
	}

	public void addRune(Rune rune) {
		this.runes.add(rune);
	}

	public int getNumRunes() {
		return this.runes.size();
	}

	public Rune getRune(String runeLabel) {
		for (final Rune r : this.runes)
			if (r.getLabel().equalsIgnoreCase(runeLabel))
				return r;
		return null;
	}

	public List<Rune> getRunes() {
		return this.runes;
	}

	public void removeRune(Rune rune) {
		this.runes.remove(rune);
	}
}
