package com.lostshard.Lostshard.Objects.Player;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Location;


@Embeddable
public class Rune {

	private int id;
	@Transient
	private Location location;
	private String label;

	public Rune(Location location, String name, int id) {
		super();
		this.location = location;
		this.label = name;
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
