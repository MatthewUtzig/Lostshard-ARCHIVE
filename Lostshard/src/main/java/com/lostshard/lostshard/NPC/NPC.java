package com.lostshard.lostshard.NPC;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.lostshard.lostshard.Database.Mappers.NPCMapper;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.NPC.NPCLib.NPCLibManager;
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
	private UUID uuid = UUID.randomUUID();

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
		final Plot plot = this.getPlot();
		plot.getNpcs().remove(this);
		NPCMapper.deleteNPC(this);
		NPCLibManager.getManager().despawnNPC(this);
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
		NPCLibManager.getManager().moveNPC(this);
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
		NPCLibManager.getManager().spawnNPC(this);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getDisplayName() {
		return (getType().equals(NPCType.BANKER) ? "[BANKER] " : getType().equals(NPCType.GUARD) ? "[GUARD] " : getType().equals(NPCType.VENDOR) ? "[VENDOR] " : "") + getName();
	}

	public void teleport(Location location, TeleportCause reason) {
		NPCLibManager.getManager().teleportNPC(this.id, location, reason);
	}

	public net.citizensnpcs.api.npc.NPC getCitizensNPC() {
		return NPCLibManager.getManager().getNPC(id);
	}
}
