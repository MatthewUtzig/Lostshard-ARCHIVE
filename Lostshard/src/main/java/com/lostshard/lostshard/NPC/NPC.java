package com.lostshard.lostshard.NPC;

import org.bukkit.Location;

import com.lostshard.lostshard.Handlers.NPCHandler;

public class NPC {

	private int id;
	private NPCType type;
	private String name;
	private Location location;
	
	public NPC(int id, NPCType type, String name, Location location) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.location = location;
	}
	public NPC(NPCType type, String name, Location location) {
		super();
		this.id = NPCHandler.getNextId();
		this.type = type;
		this.name = name;
		this.location = location;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public NPCType getType() {
		return type;
	}
	public void setType(NPCType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
}
