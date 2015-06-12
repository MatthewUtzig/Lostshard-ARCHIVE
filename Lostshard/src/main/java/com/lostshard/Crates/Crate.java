package com.lostshard.Crates;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Utils.RandomSelector;

public class Crate {
	
	private final int id;
	private final ItemStack crate;
	private final RandomSelector rs;
	
	public Crate(int id, String name, String[] lore, RandomSelector rs) {
		super();
		this.id = id;
		this.crate = new ItemStack(Material.CHEST);
		ItemMeta crate_meta = this.crate.getItemMeta();
		crate_meta.setDisplayName(name);
		crate_meta.setLore(Arrays.asList(lore));
		crate_meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		crate_meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		crate_meta.addEnchant(Enchantment.DURABILITY, id, true);
		this.crate.setItemMeta(crate_meta);
		this.rs = rs;
	}

	public ItemStack getCrate() {
		return this.crate;
	}
	
	public ItemStack getItem() {
		return (ItemStack) rs.getRandomObject();
	}

	public int getId() {
		return id;
	}

	public RandomSelector getRs() {
		return rs;
	}
}
