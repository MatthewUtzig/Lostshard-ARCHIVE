package com.lostshard.Lostshard.Objects.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Embeddable
@Access(AccessType.FIELD)
public class Runebook implements Set<Rune>{

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private final Set<Rune> runes = new HashSet<>();

	@Override
	public int size() {
		return runes.size();
	}

	@Override
	public boolean isEmpty() {
		return runes.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return runes.contains(o);
	}
	
	public boolean contains(String label) {
		for(Rune r : runes)
			if(r.getLabel().equalsIgnoreCase(label))
				return true;
		return false;
	}

	@Override
	public Iterator<Rune> iterator() {
		return runes.iterator();
	}

	@Override
	public Object[] toArray() {
		return runes.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return runes.toArray(a);
	}

	@Override
	public boolean add(Rune e) {
		return runes.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return runes.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return runes.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Rune> c) {
		return runes.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return runes.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return runes.removeAll(c);
	}

	@Override
	public void clear() {
		runes.clear();
	}
	
	public Rune get(String label) {
		for(Rune r : runes)
			if (r.getLabel().equalsIgnoreCase(label))
				return r;
		return null;
	}
}
