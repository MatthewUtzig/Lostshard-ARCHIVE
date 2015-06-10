package com.lostshard.Crates;

import org.bukkit.inventory.ItemStack;

public class CrateItem {

	private final double weight;
	private final ItemStack item;
	
	public CrateItem(double weight, ItemStack item) {
		this.weight = weight;
		this.item = item;
	}

	public double getWeight() {
		return weight;
	}

	public ItemStack getItem() {
		return item;
	}
}
