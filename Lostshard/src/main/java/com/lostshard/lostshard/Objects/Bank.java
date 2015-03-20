package com.lostshard.lostshard.Objects;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Utils.Serializer;

public class Bank {

	private Inventory inventory;

	public Bank(String bankData, boolean large) {
		super();
		if (large)
			inventory = Bukkit.createInventory(null, 54, "Large bank");
		else
			inventory = Bukkit.createInventory(null, 27, "Small bank");
		setInventory(bankData);
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public String Serialize() {
		return Serializer.serializeItems(inventory.getContents());
	}
	
	public void setInventory(String string) {
		if (string != null && string != "") {
			ItemStack[] content = Serializer.deserializeItems(string);
			if(content.length > inventory.getSize())
				inventory = Bukkit.createInventory(null, 54, "Large bank");
			this.inventory.setContents(content);
		}
	}

}
