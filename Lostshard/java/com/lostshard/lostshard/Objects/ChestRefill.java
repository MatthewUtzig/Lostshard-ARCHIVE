package com.lostshard.lostshard.Objects;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ChestRefill {

	private ItemStack[] items;
	private long rangeMin;
	private long rangeMax;
	private Location iLocation;
	
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

	public Location getiLocation() {
		return iLocation;
	}

	public void setiLocation(Location iLocation) {
		this.iLocation = iLocation;
	}
}
