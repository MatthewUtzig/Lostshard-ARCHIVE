package com.lostshard.lostshard.NPC;

import org.bukkit.Location;

import com.lostshard.lostshard.Database.Mappers.NPCMapper;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.Plot.Plot;

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
		this.id = -1;
		this.type = type;
		this.name = name;
		this.location = location;
		this.plotId = plotId;
	}

	/**
	 * Delete this npc
	 */
	public void fire() {
		// NPCManager.getNPC(id).destroy();
		final Plot plot = this.getPlot();
		plot.getNpcs().remove(this);
		NPCMapper.deleteNPC(this);
	}

	/**
	 * @return return npc id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
	 * @return name of npc
	 */
	public String getName() {
		return this.name;
	}

	public Plot getPlot() {
		return this.ptm.getPlot(this.plotId);
	}

	/**
	 * @return
	 */
	public int getPlotId() {
		return this.plotId;
	}

	/**
	 * @return NPCType
	 */
	public NPCType getType() {
		return this.type;
	}

	/**
	 * @param move
	 *            npc to a location
	 */
	public void move(Location location) {
		// NPCManager.moveNPC(id, location);
	}

	/**
	 * @param set
	 *            npc id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
		this.getPlot().update();
	}

	/**
	 * @param set
	 *            name of npc
	 */
	public void setName(String name) {
		this.name = name;
		this.getPlot().update();
	}

	/**
	 * @param plotId
	 */
	public void setPlotId(int plotId) {
		this.plotId = plotId;
	}

	/**
	 * @param set
	 *            NPCType
	 */
	public void setType(NPCType type) {
		this.type = type;
		this.getPlot().update();
	}

	/**
	 * Spawns NPC in
	 */
	public void spawn() {
		// NPCManager.spawnNPC(this);
	}

}
