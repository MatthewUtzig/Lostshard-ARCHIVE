package com.lostshard.lostshard.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum SpawnLocation {

	LAWFULL(new Location(Bukkit.getWorld("world"), 0, 60, 0)),
	MURDER(new Location(Bukkit.getWorld("world"), 100, 60, 0));
	
	private Location spawn;
	
	private SpawnLocation(Location spawn) {
		this.setSpawn(spawn);
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	
}
