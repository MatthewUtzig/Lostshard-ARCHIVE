package com.lostshard.lostshard.NPC;

import org.bukkit.Location;

import com.lostshard.lostshard.Handlers.NPCHandler;
import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Objects.Plot;

public class NPC {

	private int id;
	private NPCType type;
	private String name;
	private Location location;
	private int plotId;
	
	public NPC(int id, NPCType type, String name, Location location, int plotId) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.location = location;
		this.plotId = plotId;
	}
	public NPC(NPCType type, String name, Location location, int plotId) {
		super();
		this.id = NPCHandler.getNextId();
		this.type = type;
		this.name = name;
		this.location = location;
		this.plotId = plotId;
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
	public int getPlotId() {
		return this.plotId;
	}
	public void setPlotId(int plotId) {
		this.plotId = plotId;
	}
	public void spawn() {
		NPCManager.spawnNPC(this);
	}
	public void move(Location location) {
		this.location = location;
		NPCManager.moveNPC(id, location);
	}
	public void fire() {
		Plot plot = PlotHandler.getPlotById(plotId);
		plot.getNpcs().remove(this);
	}
}
