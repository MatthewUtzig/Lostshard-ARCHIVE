package com.lostshard.lostshard.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemUtils {

	public static void removeItem(PlayerInventory inv, Material type, int amount) {
        for (ItemStack is : inv.getContents()) {
            if (is != null && is.getType() == type) {
                int newamount = is.getAmount() - amount;
                if (newamount > 0) {
                    is.setAmount(newamount);
                    break;
                } else {
                    inv.remove(is);
                    amount = -newamount;
                    if (amount == 0) break;
                }
            }
        }
    }
	
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
	
	public static boolean isDiamondArmor(Material item) {
		if(item.equals(Material.DIAMOND_HELMET)
				|| item.equals(Material.DIAMOND_CHESTPLATE)
				|| item.equals(Material.DIAMOND_LEGGINGS)
				|| item.equals(Material.DIAMOND_BOOTS))
			return true;
		return false;
	}
	
	public static boolean isDiamondArmor(ItemStack	item) {
		return isDiamondArmor(item.getType());
	}
	
	public static boolean isIronArmor(Material item) {
		if(item.equals(Material.IRON_HELMET)
				|| item.equals(Material.IRON_CHESTPLATE)
				|| item.equals(Material.IRON_LEGGINGS)
				|| item.equals(Material.IRON_BOOTS))
			return true;
		return false;
	}
	
	public static boolean isIronArmor(ItemStack	item) {
		return isIronArmor(item.getType());
	}
	
	public static boolean isGoldArmor(Material item) {
		if(item.equals(Material.GOLD_HELMET)
				|| item.equals(Material.GOLD_CHESTPLATE)
				|| item.equals(Material.GOLD_LEGGINGS)
				|| item.equals(Material.DIAMOND_BOOTS))
			return true;
		return false;
	}
	
	public static boolean isGoldArmor(ItemStack	item) {
		return isGoldArmor(item.getType());
	}
	
	public static boolean isLeatherArmor(Material item) {
		if(item.equals(Material.LEATHER_HELMET)
				|| item.equals(Material.LEATHER_CHESTPLATE)
				|| item.equals(Material.LEATHER_LEGGINGS)
				|| item.equals(Material.LEATHER_BOOTS))
			return true;
		return false;
	}
	
	public static boolean isLeatherArmor(ItemStack	item) {
		return isLeatherArmor(item.getType());
	}
	
	public static boolean isDiamond(Material item) {
		if(item.equals(Material.DIAMOND_AXE)
				|| item.equals(Material.DIAMOND_BOOTS)
				|| item.equals(Material.DIAMOND_CHESTPLATE)
				|| item.equals(Material.DIAMOND_HELMET)
				|| item.equals(Material.DIAMOND_HOE)
				|| item.equals(Material.DIAMOND_LEGGINGS)
				|| item.equals(Material.DIAMOND_SPADE)
				|| item.equals(Material.DIAMOND_SWORD))
			return true;
		return false;
	}
	
	public static boolean isDiamond(ItemStack item) {
		return isDiamond(item.getType());
	}
	
	public static boolean isIron(Material item) {
		if(item.equals(Material.IRON_AXE)
				|| item.equals(Material.IRON_BOOTS)
				|| item.equals(Material.IRON_CHESTPLATE)
				|| item.equals(Material.IRON_HELMET)
				|| item.equals(Material.IRON_HOE)
				|| item.equals(Material.IRON_LEGGINGS)
				|| item.equals(Material.IRON_PICKAXE)
				|| item.equals(Material.IRON_SPADE)
				|| item.equals(Material.IRON_SWORD))
			return true;
		return false;
	}
	
	public static boolean isIron(ItemStack item) {
		return isIron(item.getType());
	}
	
	public static boolean isGold(Material item) {
		if(item.equals(Material.GOLD_AXE)
				|| item.equals(Material.GOLD_BOOTS)
				|| item.equals(Material.GOLD_CHESTPLATE)
				|| item.equals(Material.GOLD_HELMET)
				|| item.equals(Material.GOLD_HOE)
				|| item.equals(Material.GOLD_LEGGINGS)
				|| item.equals(Material.GOLD_PICKAXE)
				|| item.equals(Material.GOLD_SPADE)
				|| item.equals(Material.GOLD_SWORD))
			return true;
		return false;
	}
	
	public static boolean isGold(ItemStack item) {
		return isGold(item.getType());
	}
	
	public static boolean isStone(Material item) {
		if(item.equals(Material.STONE_AXE)
				|| item.equals(Material.STONE_HOE)
				|| item.equals(Material.STONE_PICKAXE)
				|| item.equals(Material.STONE_SPADE)
				|| item.equals(Material.STONE_SWORD))
			return true;
		return false;
	}
	
	public static boolean isStone(ItemStack item) {
		return isStone(item.getType());
	}
	
	public static boolean isWood(Material item) {
		if(item.equals(Material.WOOD_AXE)
				|| item.equals(Material.WOOD_HOE)
				|| item.equals(Material.WOOD_PICKAXE)
				|| item.equals(Material.WOOD_SPADE)
				|| item.equals(Material.WOOD_SWORD))
			return true;
		return false;
	}
	
	public static boolean isWood(ItemStack item) {
		return isWood(item.getType());
	}
}
