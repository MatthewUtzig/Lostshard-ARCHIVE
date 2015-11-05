package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name="runebooks")
public class Runebook {
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="runebook_runes", joinColumns=@JoinColumn(name="runebook_id"))
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
