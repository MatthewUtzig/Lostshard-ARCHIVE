package com.lostshard.Lostshard.Objects.Player;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Objects.CustomObjects.SavableInventory;

@Embeddable
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Access(AccessType.PROPERTY)
public class Bank {

	private Inventory inventory;
	
	public Bank() {
		
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
	
	public SavableInventory getSavableInventory() {
		return new SavableInventory(this.inventory);
	}
	
	public void setSavableInventory(SavableInventory savableInventory) {
		this.inventory = savableInventory.getInventory();
	}
}
