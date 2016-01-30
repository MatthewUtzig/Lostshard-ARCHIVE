package com.lostshard.Lostshard.Objects.Store;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bukkit.inventory.ItemStack;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Utils.Serializer;

@Entity
public class Store {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<StoreItem> items = new ArrayList<StoreItem>();
	
	public void addItem(StoreItem item) {
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
	
	public void save() {
		Session s = Lostshard.getSession();
		try {
			Transaction t = s.beginTransaction();
			t.begin();
			s.update(this);
			t.commit();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
	
	public void insert() {
		Session s = Lostshard.getSession();
		try {
			Transaction t = s.beginTransaction();
			t.begin();
			s.save(this);
			t.commit();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
	
	public void delete() {
		Session s = Lostshard.getSession();
		try {
			Transaction t = s.beginTransaction();
			t.begin();
			s.delete(this);
			t.commit();
			s.clear();
			s.close();
		} catch(Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
}
