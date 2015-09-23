package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;

public class Runebook {
	private final ArrayList<Rune> runes;

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

	public ArrayList<Rune> getRunes() {
		return this.runes;
	}

	public void removeRune(Rune rune) {
		this.runes.remove(rune);
	}
}
