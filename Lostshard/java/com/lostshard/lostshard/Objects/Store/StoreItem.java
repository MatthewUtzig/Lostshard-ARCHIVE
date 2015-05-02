package com.lostshard.lostshard.Objects.Store;

import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Utils.Serializer;

public class StoreItem {

	public static StoreItem fromJson(String string) {
		return Serializer.gson.fromJson(string, StoreItem.class);
	}

	// Item
	private int id = 0;
	private ItemStack item;
	private int salePrice = 0;
	private int buyPrice = 0;

	private int stock = 0;

	// Buyer
	private int maxBuyAmount = 0;
	// Restock
	private boolean autoResotck = false;
	private int restockTime = 0;

	private int restockAmount = 0;

	public StoreItem(ItemStack item) {
		this.item = item;
	}

	public String getAsJson() {
		return Serializer.gson.toJson(this);
	}

	public int getBuyPrice() {
		return this.buyPrice;
	}

	public int getId() {
		return this.id;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public int getMaxBuyAmount() {
		return this.maxBuyAmount;
	}

	public int getRestockAmount() {
		return this.restockAmount;
	}

	public int getRestockTime() {
		return this.restockTime;
	}

	public int getSalePrice() {
		return this.salePrice;
	}

	public int getStock() {
		return this.stock;
	}

	public boolean isAutoResotck() {
		return this.autoResotck;
	}

	public void setAutoResotck(boolean autoResotck) {
		this.autoResotck = autoResotck;
	}

	public void setBuyPrice(int buyPrice) {
		this.buyPrice = buyPrice;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void setMaxBuyAmount(int maxBuyAmount) {
		this.maxBuyAmount = maxBuyAmount;
	}

	public void setRestockAmount(int restockAmount) {
		this.restockAmount = restockAmount;
	}

	public void setRestockTime(int restockTime) {
		this.restockTime = restockTime;
	}

	public void setSalePrice(int price) {
		this.salePrice = price;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
}
