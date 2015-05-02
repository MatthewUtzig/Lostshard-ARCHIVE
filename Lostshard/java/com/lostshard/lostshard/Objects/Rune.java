package com.lostshard.lostshard.Objects;

import org.bukkit.Location;

public class Rune {

	private Location location;
	private String label;
	private int id;

	public Rune(Location location, String name, int id) {
		super();
		this.location = location;
		this.label = name;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public Location getLocation() {
		return location;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
