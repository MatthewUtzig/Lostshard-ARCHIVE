package com.lostshard.lostshard.Data;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum Locations {

	LAWFULL(new Location(Bukkit.getWorld("world"), 0, 60, 0)),
	CRIMINAL(new Location(Bukkit.getWorld("world"), 100, 60, 0)),
	BUILDCHANGLAWFULL(new Location(Bukkit.getWorld("world"), 0, 60, 0)),
	BUILDCHANGECRIMINAL(new Location(Bukkit.getWorld("world"), 0, 60, 0)),;
	
	private Location location;
	
	private Locations(Location location) {
		this.setLocation(location);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
