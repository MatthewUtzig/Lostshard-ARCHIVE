package com.lostshard.Crates;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Crate {
	
	private final ItemStack crate;
	private final List<CrateItem> items;
	
	public Crate(String name, String[] lore, List<CrateItem> loot) {
		super();
		this.crate = new ItemStack(Material.CHEST);
		ItemMeta crate_meta = this.crate.getItemMeta();
		crate_meta.setDisplayName(name);
		crate_meta.setLore(Arrays.asList(lore));
		this.crate.setItemMeta(crate_meta);
		this.items = loot;
	}

	public ItemStack getCrate() {
		return this.crate;
	}
	
	public ItemStack getItem() {
		CrateItem item = null;
		double total_weight = 0;
		for(CrateItem i : items)
			total_weight += i.getWeight();
		double weight = Math.random()*total_weight;
		double currentWeight = 0;
		for(CrateItem i : items) {
			if(i.getWeight()+currentWeight >= weight) {
				item = i;
				break;
			}
			currentWeight += i.getWeight();
		}
		return item.getItem();
	}
}
