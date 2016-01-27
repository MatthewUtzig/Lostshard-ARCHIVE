package com.lostshard.Lostshard.Objects.CustomObjects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Embeddable
@Access(AccessType.PROPERTY)
public class SavableInventory {
	
	private String title;
	private List<SavableItem> contents = new ArrayList<SavableItem>();
	private int size;

	public SavableInventory() {
		
	}
	
	public SavableInventory(Inventory inventory) {
		this.title = inventory.getName();
		this.size = inventory.getSize();
		List<SavableItem> contents = new ArrayList<SavableItem>(inventory.getSize());
		for(ItemStack i : inventory.getContents()) {
			contents.add(new SavableItem(i));
		}
		this.contents = contents;
	}

	public void setInventory(Inventory inventory) {
		this.title = inventory.getName();
		this.size = inventory.getSize();
		List<SavableItem> contents = new ArrayList<SavableItem>(inventory.getSize());
		for(ItemStack i : inventory.getContents()) {
			contents.add(new SavableItem(i));
		}
		this.contents = contents;
	}
	
	public String getName() {
		return this.title;
	}
	
	public void setName(String name) {
		this.title = name;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	@Transient
	public Inventory getInventory() {
		Inventory inventory;
		inventory = Bukkit.createInventory(null, size, this.title);
		for(SavableItem i : contents)
			if(i == null)
				continue;
			else
				inventory.addItem(i.getItem());
		return inventory;
	}
	
	@Transient
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<SavableItem> getContents() {
		return this.contents;
	}
	
	public void setContents(List<SavableItem> contents) {
		this.contents = contents;
	}
}
