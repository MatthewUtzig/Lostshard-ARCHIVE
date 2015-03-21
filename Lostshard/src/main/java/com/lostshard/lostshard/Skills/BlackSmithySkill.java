package com.lostshard.lostshard.Skills;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;

public class BlackSmithySkill extends Skill {

	public BlackSmithySkill() {
		super();
		setName("Blacksmithy");
		setBaseProb(.2);
		setScaleConstant(60);
	}
	
	public static void repair(Player player) {
		ItemStack item = player.getItemInHand();
		if(canRepair(item)) {
			return;
		}
		Material cost = null;
		int costAmount = 1;
		if(ItemUtils.isDiamond(item))
			cost = Material.DIAMOND;
		else if(ItemUtils.isGold(item))
			cost = Material.GOLD_INGOT;
		else if(ItemUtils.isIron(item))
			cost = Material.IRON_INGOT;
		else if(ItemUtils.isStone(item))
			cost = Material.COBBLESTONE;
		else if(ItemUtils.isWood(item))
			cost = Material.WOOD;
		if(cost == null) {
			player.sendMessage(ChatColor.RED+"ERROR: NO COST");
			return;
		}
		
		if(!player.getInventory().contains(cost, costAmount)) {
			Output.simpleError(player, "You do not have enough "+StringUtils.lowerCase(cost.name())+"s to repair that tool, requires "+costAmount+".");
			return;
		}
	}
	
	public static void smelt(Player player) {
		
	}
	
	public static void enhance(Player player) {
		
	}
	
	public static boolean canRepair(Material item) {
		if(
				//Gold
				item.equals(Material.DIAMOND_AXE)
				|| item.equals(Material.DIAMOND_BOOTS)
				|| item.equals(Material.DIAMOND_CHESTPLATE)
				|| item.equals(Material.DIAMOND_HELMET)
				|| item.equals(Material.DIAMOND_HOE)
				|| item.equals(Material.DIAMOND_LEGGINGS)
				|| item.equals(Material.DIAMOND_SPADE)
				|| item.equals(Material.DIAMOND_SWORD)
				//Iron
				|| item.equals(Material.IRON_AXE)
				|| item.equals(Material.IRON_BOOTS)
				|| item.equals(Material.IRON_CHESTPLATE)
				|| item.equals(Material.IRON_HELMET)
				|| item.equals(Material.IRON_HOE)
				|| item.equals(Material.IRON_LEGGINGS)
				|| item.equals(Material.IRON_PICKAXE)
				|| item.equals(Material.IRON_SPADE)
				|| item.equals(Material.IRON_SWORD)
				//Gold
				|| item.equals(Material.GOLD_AXE)
				|| item.equals(Material.GOLD_BOOTS)
				|| item.equals(Material.GOLD_CHESTPLATE)
				|| item.equals(Material.GOLD_HELMET)
				|| item.equals(Material.GOLD_HOE)
				|| item.equals(Material.GOLD_LEGGINGS)
				|| item.equals(Material.GOLD_PICKAXE)
				|| item.equals(Material.GOLD_SPADE)
				|| item.equals(Material.GOLD_SWORD)
				//Stone
				|| item.equals(Material.STONE_AXE)
				|| item.equals(Material.STONE_HOE)
				|| item.equals(Material.STONE_PICKAXE)
				|| item.equals(Material.STONE_SPADE)
				|| item.equals(Material.STONE_SWORD)
				//Wood
				||item.equals(Material.WOOD_AXE)
				|| item.equals(Material.WOOD_HOE)
				|| item.equals(Material.WOOD_PICKAXE)
				|| item.equals(Material.WOOD_SPADE)
				|| item.equals(Material.WOOD_SWORD)
				)
			return true;
		return false;
	}
	
	public static boolean canRepair(ItemStack item) {
		return canRepair(item.getType());
	}
}
