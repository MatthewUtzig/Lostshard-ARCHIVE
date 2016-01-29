package com.lostshard.Lostshard.Objects.Store;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Utils.Serializer;

public class Store {

	private int id = 1;
	private int npcId = 0;
	private List<StoreItem> items = new ArrayList<StoreItem>();

	public Store(int npcID) {
		this.npcId = npcID;
	}

	public Store(int npcID, String content) {
		// TODO Auto-generated constructor stub
	}

	public void addItem(StoreItem item) {
		int id = 0;
		for (final StoreItem si : this.items)
			id = si.getId() + 1;
		item.setId(id);
		this.items.add(item);
	}

	public String getAsJson() {
		return Serializer.gson.toJson(this.items).toString();
	}

	public int getId() {
		return this.id;
	}

	public List<StoreItem> getItems() {
		return this.items;
	}

	public List<StoreItem> getItemsForBuy() {
		final List<StoreItem> rs = new ArrayList<StoreItem>();
		for (final StoreItem item : this.items)
			if (item.getBuyPrice() > 0)
				rs.add(item);
		return rs;
	}

	public List<StoreItem> getItemsForSale() {
		final List<StoreItem> rs = new ArrayList<StoreItem>();
		for (final StoreItem item : this.items)
			if (item.getSalePrice() > 0)
				rs.add(item);
		return rs;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public StoreItem getStoreItem(int id) {
		for (final StoreItem i : this.items)
			if (id == i.getId())
				return i;
		return null;
	}

	public StoreItem getStoreItem(ItemStack item) {
		for (final StoreItem si : this.items)
			if (si.equals(item))
				return si;
		return null;
	}

	public boolean itemsContains(ItemStack item) {
		for (final StoreItem si : this.items)
			if (si.equals(item))
				return true;
		return false;
	}

	public void removeStoreItem(StoreItem storeItem) {
		this.items.remove(storeItem);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setItems(List<StoreItem> items) {
		this.items = items;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public void update() {
		
	}
}
