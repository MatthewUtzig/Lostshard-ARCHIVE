package com.lostshard.lostshard.Objects.Store;

import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Utils.Serializer;

public class StoreItem {

	//Item
	private ItemStack item;
	private int salePrice = 0;
	private int buyPrice = 0;
	private int stock = 0;
	
	//Buyer
	private int maxBuyAmount = 0;
	
	//Restock
	private boolean autoResotck = false;
	private int restockTime = 0;
	private int restockAmount = 0;

	public StoreItem(ItemStack item) {
		this.item = item;
	}
	
	public String getAsJson() {
		return Serializer.gson.toJson(this);
	}
	
	public static StoreItem fromJson(String string) {
		return Serializer.gson.fromJson(string, StoreItem.class);
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(int price) {
		this.salePrice = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getMaxBuyAmount() {
		return maxBuyAmount;
	}

	public void setMaxBuyAmount(int maxBuyAmount) {
		this.maxBuyAmount = maxBuyAmount;
	}

	public boolean isAutoResotck() {
		return autoResotck;
	}

	public void setAutoResotck(boolean autoResotck) {
		this.autoResotck = autoResotck;
	}

	public int getRestockTime() {
		return restockTime;
	}

	public void setRestockTime(int restockTime) {
		this.restockTime = restockTime;
	}

	public int getRestockAmount() {
		return restockAmount;
	}

	public void setRestockAmount(int restockAmount) {
		this.restockAmount = restockAmount;
	}

	public int getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}
}
