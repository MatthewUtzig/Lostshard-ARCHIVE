package com.lostshard.Lostshard.Objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Utils.Serializer;

public class Bank {

	private Inventory inventory;

	public Bank(String bankData, boolean large) {
		super();
		if (large)
			this.inventory = Bukkit.createInventory(null, 54, "Large bank");
		else
			this.inventory = Bukkit.createInventory(null, 27, "Small bank");
		this.setInventory(bankData);
	}
	
	public Bank(boolean large) {
		super();
		if (large)
			this.inventory = Bukkit.createInventory(null, 54, "Large bank");
		else
			this.inventory = Bukkit.createInventory(null, 27, "Small bank");
		this.inventory.addItem(new ItemStack(Material.GOLD_INGOT, 32));
		this.inventory.addItem(new ItemStack(Material.DIAMOND, 3));
		this.inventory.addItem(new ItemStack(Material.MELON, 10));
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public String Serialize() {
		return Serializer.serializeItems(this.inventory.getContents());
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setInventory(String string) {
		if (string != null && string != "") {
			final ItemStack[] content = Serializer.deserializeItems(string);
			if (content.length > this.inventory.getSize())
				this.inventory = Bukkit.createInventory(null, 54, "Large bank");
			this.inventory.setContents(content);
		}
	}

}
