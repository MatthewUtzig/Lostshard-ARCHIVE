package com.lostshard.Lostshard.Objects.CustomObjects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy=InheritanceType.JOINED)
@Audited
public class SavableInventory {
	
	private int id;
	private String title;
	private List<SavableItem> contents;

	public SavableInventory() {
		
	}
	
	public SavableInventory(Inventory inventory) {
		this.title = inventory.getName();
		List<SavableItem> contents = new ArrayList<SavableItem>(inventory.getSize());
		for(ItemStack i : inventory.getContents()) {
			contents.add(new SavableItem(i));
		}
		this.contents = contents;
	}
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInventory(Inventory inventory) {
		this.title = inventory.getName();
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
	
	@Transient
	public Inventory getInventory() {
		Inventory inventory;
		inventory = Bukkit.createInventory(null, contents.size(), this.title);
		for(SavableItem i : contents)
			inventory.addItem(i.getItemStack());
		return inventory;
	}
	
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	public List<SavableItem> getContents() {
		List<SavableItem> list = new ArrayList<SavableItem>(contents.size());
		for(SavableItem i : contents) {
			list.add(i);
		}
		return list;
	}
	
	public void setContents(List<SavableItem> contents) {
		this.contents = contents;
	}
}
