package com.lostshard.lostshard.NPC;

import org.bukkit.Location;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.NPCHandler;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.Plot;

/**
 * @author Jacob Rosborg
 *
 */
public class NPC {

	PlotManager ptm = PlotManager.getManager();
	
	private int id;
	private NPCType type;
	private String name;
	private Location location;
	private int plotId;

	/**
	 * @param id
	 * @param type
	 * @param name
	 * @param location
	 * @param plotId
	 */
	public NPC(int id, NPCType type, String name, Location location, int plotId) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.location = location;
		this.plotId = plotId;
	}

	/**
	 * @param type
	 * @param name
	 * @param location
	 * @param plotId
	 */
	public NPC(NPCType type, String name, Location location, int plotId) {
		super();
		this.id = NPCHandler.getNextId();
		this.type = type;
		this.name = name;
		this.location = location;
		this.plotId = plotId;
	}

	/**
	 * @return return npc id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param set
	 *            npc id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return NPCType
	 */
	public NPCType getType() {
		return type;
	}

	/**
	 * @param set
	 *            NPCType
	 */
	public void setType(NPCType type) {
		this.type = type;
		getPlot().update();
	}

	/**
	 * @return name of npc
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param set
	 *            name of npc
	 */
	public void setName(String name) {
		this.name = name;
		getPlot().update();
	}

	/**
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
		getPlot().update();
	}

	/**
	 * @return
	 */
	public int getPlotId() {
		return this.plotId;
	}

	/**
	 * @param plotId
	 */
	public void setPlotId(int plotId) {
		this.plotId = plotId;
	}

	/**
	 * Spawns NPC in
	 */
	public void spawn() {
		 NPCManager.spawnNPC(this);
	}

	/**
	 * @param move
	 *            npc to a location
	 */
	public void move(Location location) {
		 NPCManager.moveNPC(id, location);
	}

	/**
	 * Delete this npc
	 */
	public void fire() {
		 NPCManager.getNPC(id).destroy();
		Plot plot = getPlot();
		plot.getNpcs().remove(this);
		Database.deleteNPC(this);
	}
	
	public Plot getPlot() {
		return ptm.getPlotById(plotId);
	}
	
}
