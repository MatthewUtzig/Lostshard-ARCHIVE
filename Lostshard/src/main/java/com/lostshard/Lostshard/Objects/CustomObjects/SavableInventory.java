package com.lostshard.Lostshard.Objects.CustomObjects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Embeddable
@Access(AccessType.PROPERTY)
public class SavableInventory {
	
	private Inventory inventory;

	public SavableInventory() {
		
	}
	
	public SavableInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public String getName() {
		return this.inventory.getName();
	}
	
	public List<SavableItem> getContents() {
		List<SavableItem> contents = new ArrayList<SavableItem>(this.inventory.getSize());
		for(ItemStack i : this.inventory.getContents()) {
			contents.add(new SavableItem(i));
		}
		return contents;
	}
	
	public void setContents(List<SavableItem> contents) {
		this.inventory = Bukkit.createInventory(null, contents.size(), getName());
		for(SavableItem i : contents) {
			this.inventory.addItem(i.getItemStack());
		}
	}
}
