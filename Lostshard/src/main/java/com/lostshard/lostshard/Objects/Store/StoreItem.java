package com.lostshard.lostshard.Objects.Store;

import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Utils.Serializer;

public class StoreItem {

	//Item
	private ItemStack item;
	private int price;
	private int stock;
	
	//Buyer
	private int maxBuyAmount;
	
	//Restock
	private boolean autoResotck;
	private int restockTime;
	private int restockAmount;

	public String getAsJson() {
		return Serializer.gson.toJson(this);
	}
	
	public static StoreItem fromJson(String string) {
		return Serializer.gson.fromJson(string, StoreItem.class);
	}
}
