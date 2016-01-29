package com.lostshard.Lostshard.NPC;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPCLib.NPCLibManager;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableLocation;
import com.lostshard.Lostshard.Objects.Plot.Plot;

/**
 * @author Jacob Rosborg
 *
 */
@Embeddable
@Access(AccessType.FIELD)
public class NPC {

	@Transient
	PlotManager ptm = PlotManager.getManager();

	@Transient
	private int id;
	@Enumerated(EnumType.STRING)
	private NPCType type;
	private String name;
	private SavableLocation location;

	/**
	 * Default constructor
	 */
	public NPC() {
		
	}
	
	/**
	 * @param type
	 * @param name
	 * @param location
	 * @param plotId
	 */
	public NPC(NPCType type, String name, Location location, int plotId) {
		super();
		this.type = type;
		this.name = name;
		this.location = new SavableLocation(location);
	}

	/**
	 * Delete this npc
	 */
	public void fire() {
		final Plot plot = this.getPlot();
		plot.getNpcs().remove(this);
		NPCLibManager.getManager().despawnNPC(this);
	}

	/**
	 * @return return npc id
	 */
	@Transient
	public int getId() {
		return this.id;
	}

	/**
	 * @return
	 */
	@Transient
	public Location getLocation() {
		return this.location.getLocation();
	}

	/**
	 * @return name of npc
	 */
	public String getName() {
		return this.name;
	}

	@Transient
	public Plot getPlot() {
		for(Plot p : PlotManager.getManager().getPlots())
			if(p.getNpcs().contains(this))
				return p;
		return null;
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
		this.location = new SavableLocation(location);
	}

	/**
	 * @param set
	 *            name of npc
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param set
	 *            NPCType
	 */
	public void setType(NPCType type) {
		this.type = type;
	}

	/**
	 * Spawns NPC in
	 */
	public void spawn() {
		this.id = NPCLibManager.getManager().spawnNPC(this);
	}

	@Transient
	public String getDisplayName() {
		return (getType().equals(NPCType.BANKER) ? "[BANKER] " : getType().equals(NPCType.GUARD) ? "[GUARD] " : getType().equals(NPCType.VENDOR) ? "[VENDOR] " : "") + getName();
	}

	public void teleport(Location location, TeleportCause reason) {
		NPCLibManager.getManager().teleportNPC(this.id, location, reason);
	}

	@Transient
	public net.citizensnpcs.api.npc.NPC getCitizensNPC() {
		return NPCLibManager.getManager().getNPC(id);
	}
	
	public SavableLocation getSavableLocation() {
		return this.location;
	}

	public void setSavableLocation(SavableLocation savableLocation) {
		this.location = savableLocation;
	}
	
	public UUID getUUID() {
		return NPCLibManager.getManager().getNPC(id).getUniqueId();
	}

	public void despawn() {
		getCitizensNPC().destroy();
	}
}
