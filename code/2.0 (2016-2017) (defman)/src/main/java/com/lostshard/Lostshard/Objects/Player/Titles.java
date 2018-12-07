package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Embeddable
@Access(AccessType.FIELD)
public class Titles implements List<String> {

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> titles = new ArrayList<>();
	private int current = -1;

	
	public void setTitle() {
		
	}
	
	@Override
	public Iterator<String> iterator() {
		return titles.iterator();
	}

	@Override
	public int size() {
		return titles.size();
	}

	@Override
	public boolean isEmpty() {
		return titles.isEmpty();
	}

	@Override
	public Object[] toArray() {
		return titles.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return titles.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends String> c) {
		return titles.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends String> c) {
		return addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return retainAll(c);
	}

	@Override
	public void clear() {
		titles.clear();
	}

	@Override
	public String get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String set(int index, String element) {
		return titles.set(index, element);
	}

	@Override
	public void add(int index, String element) {
		titles.add(index, element);
	}

	@Override
	public String remove(int index) {
		return titles.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return titles.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return lastIndexOf(o);
	}

	@Override
	public ListIterator<String> listIterator() {
		return titles.listIterator();
	}

	@Override
	public ListIterator<String> listIterator(int index) {
		return titles.listIterator(index);
	}

	@Override
	public List<String> subList(int fromIndex, int toIndex) {
		return subList(fromIndex, toIndex);
	}
	
	@Override
	public boolean contains(Object o) {
		if(!(o instanceof String))
			return false;
		for(String t : this)
			if (t.equalsIgnoreCase((String) o))
				return true;
		return false;
	}

	@Override
	public boolean add(String e) {
		if(contains(e))
			return false;
		return titles.add(e);
	}

	@Override
	public boolean remove(Object o) {
		if(!(o instanceof String))
			return false;
		Iterator<String> it = iterator();
		String title = (String) o;
		int i = 0;
		while(it.hasNext()) {
			String t = it.next();
			if(t.equalsIgnoreCase(title)) {
				it.remove();
				if(i == current)
					current = -1;
				return true;
			}
			i++;
		}
		return false;
	}
	
	public boolean set(String title) {
		Iterator<String> it = iterator();
		int i = 0;
		while(it.hasNext()) {
			String t = it.next();
			if(t.equalsIgnoreCase(title)) {
				this.current = i;
				return true;
			}
			i++;
		}
		return false;
	}
	
	public String current() {
		return get(current);
	}
}
