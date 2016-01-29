package com.lostshard.Lostshard.Objects.Player;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.lostshard.Lostshard.Objects.CustomObjects.SavableInventory;

@Embeddable
@Inheritance(strategy=InheritanceType.JOINED)
@Access(AccessType.PROPERTY)
public class Bank {

	private Inventory inventory;
	
	public Bank() {
		this.setInventory(Bukkit.createInventory(null, 27, "Small bank"));
	}
	
	public Bank(boolean large) {
		super();
		if (large)
			this.setInventory(Bukkit.createInventory(null, 54, "Large bank"));
		else
			this.setInventory(Bukkit.createInventory(null, 27, "Small bank"));
		this.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 32));
		this.getInventory().addItem(new ItemStack(Material.DIAMOND, 3));
		this.getInventory().addItem(new ItemStack(Material.MELON, 10));
	}
	
	@Transient
	public Inventory getInventory() {
		return this.inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	public SavableInventory getSavableInventory() {
		return new SavableInventory(inventory);
	}
	
	public void setSavableInventory(SavableInventory inventory) {
		this.inventory = inventory.getInventory();
	}
}
