package com.lostshard.lostshard.Objects.Store;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Utils.Serializer;

@Embeddable
@Access(AccessType.PROPERTY)
public class StoreItem {

	// Item
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

	public StoreItem() {

	}

	public StoreItem(ItemStack item, int salePrice, int buyPrice, int stock, int maxBuyAmount) {
		this.item = item;
		this.salePrice = salePrice;
		this.buyPrice = buyPrice;
		this.stock = stock;
		this.maxBuyAmount = maxBuyAmount;
	}

	public int getBuyPrice() {
		return this.buyPrice;
	}

	@Transient
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

	@Column(columnDefinition = "text")
	public String getSavableItem() {
		return Serializer.itemTo64(this.item);
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

	public void setSavableItem(String item) {
		this.item = Serializer.itemFrom64(item);
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public boolean equals(ItemStack item) {
		return getItem().isSimilar(item);
	}
}
