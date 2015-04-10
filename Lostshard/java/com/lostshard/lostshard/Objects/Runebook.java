package com.lostshard.lostshard.Objects;

import java.util.ArrayList;

public class Runebook {
	private ArrayList<Rune> runes;
	
	public Runebook() {
		runes = new ArrayList<Rune>();
	}
	
	public void addRune(Rune rune) {
		runes.add(rune);
	}
	
	public ArrayList<Rune> getRunes() {
		return runes;
	}
	
	public int getNumRunes() {
		return runes.size();
	}
	
	public void removeRune(Rune rune) {
		runes.remove(rune);
	}

	public Rune getRune(String runeLabel) {
		for(Rune r : runes)
			if(r.getLabel().equalsIgnoreCase(runeLabel))
				return r;
		return null;
	}
}
