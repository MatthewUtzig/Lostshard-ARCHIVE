package com.lostshard.lostshard.Objects;

import org.bukkit.Location;

public class Rune {

	private Location location;
	private String name;
	private int id;
	
	public Rune(Location location, String name, int id) {
		super();
		this.location = location;
		this.name = name;
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
}
