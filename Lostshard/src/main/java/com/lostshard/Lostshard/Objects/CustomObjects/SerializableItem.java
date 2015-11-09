package com.lostshard.Lostshard.Objects.CustomObjects;

import java.util.Map;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@Embeddable
public class SerializableItem {
	
	private Material type; 
	private int amount;
	private short damage;
	private byte data;
    @Transient
	private Map<String, Integer> enchantments;
    @Transient
	private SerializableItemMeta meta;
	
	@SuppressWarnings("deprecation")
	public SerializableItem(ItemStack itemStack) {
		this.type = itemStack.getType();
		this.amount = itemStack.getAmount();
		this.damage = itemStack.getDurability();
		this.data = itemStack.getData().getData();
		if(itemStack.hasItemMeta())
			this.meta = new SerializableItemMeta(itemStack.getItemMeta());
		if(itemStack.getItemMeta().hasEnchants()) {
			for(Enchantment e : itemStack.getItemMeta().getEnchants().keySet())
				this.enchantments.put(e.getName(), itemStack.getItemMeta().getEnchants().get(e));
		}	
	}

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public short getDamage() {
		return damage;
	}

	public void setDamage(short damage) {
		this.damage = damage;
	}

	public byte getData() {
		return data;
	}

	public void setData(byte data) {
		this.data = data;
	}

	public Map<String, Integer> getEnchantments() {
		return enchantments;
	}

	public void setEnchantments(Map<String, Integer> enchantments) {
		this.enchantments = enchantments;
	}

	public SerializableItemMeta getMeta() {
		return meta;
	}

	public void setMeta(SerializableItemMeta meta) {
		this.meta = meta;
	}

	public ItemStack getItemStack() {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(this.type, this.amount, this.damage, this.data);
		return item;
	}
}
