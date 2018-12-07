package com.lostshard.Plots.Models;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPCLib.NPCLibManager;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableLocation;
import com.lostshard.Lostshard.Objects.Store.Store;

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
	@Type(type = "uuid-char")
	private UUID uuid = UUID.randomUUID();
	@Enumerated(EnumType.STRING)
	private NPCType type;
	private String name;
	private SavableLocation location;
	@ManyToOne(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Store store = null;

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

	public void despawn() {
		this.getCitizensNPC().destroy();
	}

	/**
	 * Delete this npc
	 */
	public void fire() {
		final Plot plot = this.getPlot();
		plot.getNpcs().remove(this);
		NPCLibManager.getManager().despawnNPC(this);
	}

	@Transient
	public net.citizensnpcs.api.npc.NPC getCitizensNPC() {
		return NPCLibManager.getManager().getNPC(this.id);
	}

	@Transient
	public String getDisplayName() {
		return (this.getType().equals(NPCType.BANKER) ? "[BANKER] "
				: this.getType().equals(NPCType.GUARD) ? "[GUARD] "
						: this.getType().equals(NPCType.VENDOR) ? "[VENDOR] " : "")
				+ this.getName();
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
		for (final Plot p : PlotManager.getManager().getPlots())
			if (p.getNpcs().contains(this))
				return p;
		return null;
	}

	public SavableLocation getSavableLocation() {
		return this.location;
	}

	public Store getStore() {
		return this.store;
	}

	/**
	 * @return NPCType
	 */
	public NPCType getType() {
		return this.type;
	}

	public UUID getUUID() {
		return this.uuid;
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

	public void setSavableLocation(SavableLocation savableLocation) {
		this.location = savableLocation;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	/**
	 * @param set
	 *            NPCType
	 */
	public void setType(NPCType type) {
		this.type = type;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * Spawns NPC in
	 */
	public void spawn() {
		this.id = NPCLibManager.getManager().spawnNPC(this);
	}

	public void teleport(Location location, TeleportCause reason) {
		NPCLibManager.getManager().teleportNPC(this.id, location, reason);
	}
}
