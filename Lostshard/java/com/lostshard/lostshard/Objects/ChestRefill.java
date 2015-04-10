package com.lostshard.lostshard.Objects;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ChestRefill {

	private ItemStack[] items;
	private long rangeMin;
	private long rangeMax;
	private Location location;
	
	public ItemStack[] getItems() {
		return items;
	}
	
	public void setItems(ItemStack[] items) {
		this.items = items;
	}
	
	public long getRangeMin() {
		return rangeMin;
	}
	
	public void setRangeMin(long rangeMin) {
		this.rangeMin = rangeMin;
	}
	
	public long getRangeMax() {
		return rangeMax;
	}

	public void setRangeMax(long rangeMax) {
		this.rangeMax = rangeMax;
	}

	public Location getLocation() {
		return location;
	}

	public void setiLocation(Location location) {
		this.location = location;
	}
}
