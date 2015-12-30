package com.lostshard.Lostshard.Objects.CustomObjects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.Location;

@Embeddable
@Access(AccessType.PROPERTY)
public class SavableLocation {
	
	private Location location;
	
	public SavableLocation(Location location) {
		this.location = location;
	}
	
	public SavableLocation() {
		this.location = new Location(null, 0, 0, 0);
	}
	
	public String getWorld() {
		return this.location.getWorld().getName();
	}
	
	public void setWorld(String world) {
		this.location.setWorld(Bukkit.getWorld(world));
	}
	
	public double getX() {
		return this.location.getX();
	}
	
	public void setX(double x) {
		this.location.setX(x);
	}
	
	public double getY() {
		return this.location.getY();
	}
	
	public void setY(double y) {
		this.location.setY(y);
	}
	
	public double getZ() {
		return this.location.getZ();
	}
	
	public void setZ(double z) {
		this.location.setZ(z);
	}
	
	public float getYaw() {
		return this.location.getYaw();
	}
	
	public void setYaw(float yaw) {
		this.location.setYaw(yaw);
	}
	
	public float getPitch() {
		return this.location.getPitch();
	}
	
	public void setPitch(float pitch) {
		this.location.setPitch(pitch);
	}

	@Transient
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
