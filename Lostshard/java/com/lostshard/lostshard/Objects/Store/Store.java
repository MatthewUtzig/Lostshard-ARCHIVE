package com.lostshard.lostshard.Objects.Store;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Utils.Serializer;

public class Store {

	private int id = 1;
	private int npcId = 0;
	private List<StoreItem> items = new ArrayList<StoreItem>();
	
	public Store(int npcID) {
		this.npcId = npcID;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}
	
	public List<StoreItem> getItemsForSale() {
		List<StoreItem> rs = new ArrayList<StoreItem>();
		for(StoreItem item : items) {
			if(item.getSalePrice() > 0)
				rs.add(item);
		}
		return rs;
	}
	
	public List<StoreItem> getItemsForBuy() {
		List<StoreItem> rs = new ArrayList<StoreItem>();
		for(StoreItem item : items) {
			if(item.getBuyPrice() > 0)
				rs.add(item);
		}
		return rs;
	}
	
	public List<StoreItem> getItems() {
		return items;
	}
	
	public void setItems(List<StoreItem> items) {
		this.items = items;
	}
	
	public boolean itemsContains(ItemStack item) {
		for(StoreItem si : items) {
			if(si.equals(item))
				return true;
		}
		return false;
	}
	public StoreItem getStoreItem(ItemStack item) {
		for(StoreItem si : items) {
			if(si.equals(item))
				return si;
		}
		return null;
	}
	
	public void update() {
		Database.updateStore(this);
	}
	
	public String getAsJson() {
		return Serializer.gson.toJson(items).toString();
	}

	public void addItem(StoreItem item) {
		int id = 0;
		for(StoreItem si : items)
			id = si.getId()+1;
		item.setId(id);
		items.add(item);
	}

	public StoreItem getStoreItem(int id) {
		for(StoreItem i : items)
			if(id == i.getId())
				return i;
		return null;
	}

	public void removeStoreItem(StoreItem storeItem) {
		items.remove(storeItem);
	}

	public String getItemsAsJson() {
		List<String> rs = new ArrayList<String>();
		for(StoreItem si : items)
			rs.add(si.getAsJson());
		return Serializer.serializeStringArray(rs);
	}
}
