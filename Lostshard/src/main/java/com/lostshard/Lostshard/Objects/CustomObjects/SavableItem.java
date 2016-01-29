package com.lostshard.Lostshard.Objects.CustomObjects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

@Entity
@Access(AccessType.PROPERTY)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Audited
public class SavableItem {
	
	private int id;
	private ItemStack item;
	
	public SavableItem() {
		this.item = new ItemStack(Material.AIR);
	}
	
	public SavableItem(ItemStack item) {
		if(item == null)
			item = new ItemStack(Material.AIR);
		this.item = item;
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public String getType() {
		return this.item.getType().name();
	}

	public void setType(String type) {
		this.item.setType(Material.getMaterial(type));;
	}
	
	public int getAmount() {
		return 42;
	}

	public void setAmount(int amount) {
		this.item.setAmount(amount);;
	}

	public short getDamage() {
		return this.item.getDurability();
	}

	public void setDamage(short damage) {
		this.item.setDurability(damage);
	}

	@SuppressWarnings("deprecation")
	public byte getData() {
		return this.item.getData().getData();
	}

	public void setData(byte data) {
		return;
	}

	@Transient
	public ItemStack getItemStack() {
		return this.item;
	}
}
