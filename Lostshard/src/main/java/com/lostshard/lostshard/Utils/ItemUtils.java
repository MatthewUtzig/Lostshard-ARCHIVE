package com.lostshard.lostshard.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

	public static boolean isSword(Material item) {
		if (item.equals(Material.DIAMOND_SWORD)
				|| item.equals(Material.IRON_SWORD)
				|| item.equals(Material.GOLD_SWORD)
				|| item.equals(Material.WOOD_SWORD)
				|| item.equals(Material.STONE_SWORD))
			return true;
		return false;
	}

	public static boolean isSword(ItemStack item) {
		return isSword(item.getType());
	}

	public static boolean isAxe(Material item) {
		if (item.equals(Material.DIAMOND_AXE) 
				|| item.equals(Material.IRON_AXE)
				|| item.equals(Material.GOLD_AXE)
				|| item.equals(Material.WOOD_AXE)
				|| item.equals(Material.STONE_AXE))
			return true;
		return false;
	}

	public static boolean isAxe(ItemStack item) {
		return isAxe(item.getType());
	}

	public static boolean isPickAxe(Material item) {
		if (item.equals(Material.DIAMOND_PICKAXE)
				|| item.equals(Material.GOLD_PICKAXE)
				|| item.equals(Material.IRON_PICKAXE)
				|| item.equals(Material.STONE_PICKAXE)
				|| item.equals(Material.WOOD_PICKAXE))
			return true;
		return false;
	}

	public static boolean isPickAxe(ItemStack item) {
		return isPickAxe(item.getType());
	}
}
