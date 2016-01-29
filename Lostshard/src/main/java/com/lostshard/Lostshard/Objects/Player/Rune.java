package com.lostshard.Lostshard.Objects.Player;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Location;

import com.lostshard.Lostshard.Objects.CustomObjects.SavableLocation;

@Embeddable
@Access(AccessType.PROPERTY)
public class Rune {

	private int id;
	private SavableLocation location;
	private String label;

	public Rune() {
		
	}
	
	public Rune(Location location, String name, int id) {
		super();
		this.location = new SavableLocation(location);
		this.label = name;
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	@Transient
	public Location getLocation() {
		return this.location.getLocation();
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLocation(Location location) {
		this.location = new SavableLocation(location);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SavableLocation getSavableLocation() {
		return this.location;
	}

	public void setSavableLocation(SavableLocation savableLocation) {
		this.location = savableLocation;
	}
}
