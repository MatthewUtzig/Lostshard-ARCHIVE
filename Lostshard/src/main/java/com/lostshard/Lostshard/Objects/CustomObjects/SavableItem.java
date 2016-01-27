package com.lostshard.Lostshard.Objects.CustomObjects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Embeddable
@Access(AccessType.PROPERTY)
public class SavableItem {
	
	private ItemStack item;
	
	public SavableItem(ItemStack item) {
		if(item == null)
			item = new ItemStack(Material.AIR);
		this.item = item;
	}
	
	@Transient
	public ItemStack getItem() {
		return item;
	}

	@Transient
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public SavableItem() {
		this.item = new ItemStack(Material.AIR);
	}

	public String getType() {
		return this.item.getType().name();
	}

	public void setType(String type) {
		this.item.setType(Material.getMaterial(type));;
	}

	public int getAmount() {
		return this.item.getAmount();
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
