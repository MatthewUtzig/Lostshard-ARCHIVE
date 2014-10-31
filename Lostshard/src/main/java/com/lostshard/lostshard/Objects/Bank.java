package com.lostshard.lostshard.Objects;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Bank {
	
	private Inventory inventory;

	public Bank(String bankData, boolean hadSubscribed) {
		super();
		if(hadSubscribed)
		inventory = Bukkit.createInventory(null, 54, "Large bank");
		else
		inventory = Bukkit.createInventory(null, 27, "Small bank");
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	//TODO Making bank Commands and data serilizer
	
}
