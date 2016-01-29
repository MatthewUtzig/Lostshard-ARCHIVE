package com.lostshard.Lostshard.Objects.Player;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Utils.Serializer;

@Embeddable
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
	
	@Column(columnDefinition="text")
	public String getBankContents() {
		return Serializer.serializeContents(this.inventory.getContents());
	}
	
	public void setBankContents(String contents) {
		ItemStack[] content = Serializer.deserializeContents(contents);
		if (content.length > 27)
			this.setInventory(Bukkit.createInventory(null, 54, "Large bank"));
		else
			this.setInventory(Bukkit.createInventory(null, 27, "Small bank"));
		this.inventory.setContents(content);
	}
}
