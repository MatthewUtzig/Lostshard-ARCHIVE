package com.lostshard.Lostshard.Objects.Player;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Location;

import com.lostshard.Lostshard.Objects.CustomObjects.SerializableLocation;

@Embeddable
@Access(AccessType.PROPERTY)
public class Rune {

	private int id;
	private Location location;
	private String label;

	public Rune(Location location, String name, int id) {
		super();
		this.location = location;
		this.label = name;
		this.id = id;
	}

	public String getLabel() {
		return this.label;
	}

	@Transient
	public Location getLocation() {
		return this.location;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SerializableLocation getSerializableLocation() {
		return new SerializableLocation(this.location);
	}

	public void setSerializableLocation(SerializableLocation serializableLocation) {
		this.location = serializableLocation.getLocation();
	}
}
