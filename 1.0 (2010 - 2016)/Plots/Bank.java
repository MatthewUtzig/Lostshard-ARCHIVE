package com.lostshard.RPG.Plots;

import org.bukkit.Location;

public class Bank {
	int _id;
	Location _location;
	int _size;
	
	public Bank(int id, Location location, int size) {
		_id = id;
		_location = location;
		_size = size;
	}
	
	public int getId() {
		return _id;
	}
	
	public Location getLocation() {
		return _location;
	}
	
	public int getRadius() {
		return _size;
	}
}
