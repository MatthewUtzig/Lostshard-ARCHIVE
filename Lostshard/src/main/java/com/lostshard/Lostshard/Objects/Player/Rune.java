package com.lostshard.Lostshard.Objects.Player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.bukkit.Location;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;


@Audited
@Entity
@Table(name="runes")
public class Rune {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@Transient
	private Location location;
	private String label;

	public Rune(Location location, String name, int id) {
		super();
		this.location = location;
		this.label = name;
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public String getLabel() {
		return this.label;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
