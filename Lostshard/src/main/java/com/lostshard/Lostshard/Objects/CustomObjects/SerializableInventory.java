package com.lostshard.Lostshard.Objects.CustomObjects;

import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@Embeddable
public class SerializableInventory {

	@ElementCollection
	@CollectionTable
	private Map<Integer, SerializableItem> items;
	private int size;
	private String title;
	
	public SerializableInventory(Inventory inventory) {
		for(int slot=0; slot<inventory.getContents().length; slot++) {
			items.put(slot, new SerializableItem(inventory.getContents()[slot]));
		}
	}

	public Map<Integer, SerializableItem> getItems() {
		return items;
	}

	public void setItems(Map<Integer, SerializableItem> items) {
		this.items = items;
	}

	@Transient
	public Inventory getInventory() {
		Inventory inv = Bukkit.createInventory(null, this.size, this.title);
		for(int slot : items.keySet())
			inv.setItem(slot, items.get(slot).getItemStack());
		return inv;
	}
	
}
