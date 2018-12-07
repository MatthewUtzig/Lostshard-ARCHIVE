package com.lostshard.RPG.Skills;

import java.util.ArrayList;

public class Runebook {
	private ArrayList<Rune> _runes;
	
	public Runebook() {
		_runes = new ArrayList<Rune>();
	}
	
	public void addRune(Rune rune) {
		_runes.add(rune);
	}
	
	public ArrayList<Rune> getRunes() {
		return _runes;
	}
	
	public int getNumRunes() {
		return _runes.size();
	}
	
	public void removeRune(Rune rune) {
		_runes.remove(rune);
	}
}
