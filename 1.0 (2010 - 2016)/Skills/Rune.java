package com.lostshard.RPG.Skills;

import org.bukkit.Location;

public class Rune {
	private String _label;
	private Location _loc;
	private int _id;
	
	public Rune(String label, Location loc, int id) {
		_label = label;
		_loc = loc;
		_id = id;
	}
	
	public String getLabel() {
		return _label;
	}
	
	public Location getLocation() {
		return _loc;
	}
	
	public int getId() {
		return _id;
	}
}
