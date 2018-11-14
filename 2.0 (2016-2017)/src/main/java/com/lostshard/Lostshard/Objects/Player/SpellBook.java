package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Access(AccessType.FIELD)
public class SpellBook implements Set<Scroll> {

	@Transient
	SpellManager sm = SpellManager.getManager();

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Enumerated(EnumType.STRING)
	private final Set<Scroll> spells = new HashSet<Scroll>();

	public ArrayList<Scroll> getSpellsOnPage(int pageNumber) {
		final ArrayList<Scroll> spellsOnPage = new ArrayList<Scroll>();
		for (final Scroll scroll : this.spells)
			if (scroll.getPage() == pageNumber)
				spellsOnPage.add(scroll);
		return spellsOnPage;
	}

	@Override
	public int size() {
		return spells.size();
	}

	@Override
	public boolean isEmpty() {
		return spells.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return spells.contains(o);
	}

	@Override
	public Iterator<Scroll> iterator() {
		return spells.iterator();
	}

	@Override
	public Object[] toArray() {
		return spells.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return spells.toArray(a);
	}

	@Override
	public boolean add(Scroll e) {
		return spells.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return spells.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return spells.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Scroll> c) {
		return spells.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return spells.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return spells.removeAll(c);
	}

	@Override
	public void clear() {
		spells.clear();
	}
}
