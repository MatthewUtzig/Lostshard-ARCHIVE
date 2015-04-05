package com.lostshard.lostshard.Skills;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;

public class BlackSmithySkill extends Skill {

	private static final int REPAIR_STAMINA_COST = 10;
	private static final int SMELT_STAMINA_COST = 15;
//	private static final int SHARPEN_STAMINA_COST = 10;
//	private static final int HARDEN_STAMINA_COST = 25;
//	private static final int FORTIFY_STAMINA_COST = 25;
	
	public BlackSmithySkill() {
		super();
		setName("Blacksmithy");
		setBaseProb(.2);
		setScaleConstant(60);
	}
	
	public static void repair(Player player) {
		ItemStack item = player.getItemInHand();
		if(item == null || item.getType().equals(Material.AIR)) {
			Output.simpleError(player, "You cannot repair air.");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.getStamina() < REPAIR_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Repair requires "+REPAIR_STAMINA_COST+".");
			return;
		}
		Skill skill = pPlayer.getCurrentBuild().getBlackSmithy();
		if(!canRepair(item)) {
			Output.simpleError(player, "You cannot repair that item.");
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
			Output.simpleError(player, "You cannot repair that item.");
			return;
		}
		
		if(!player.getInventory().contains(cost, costAmount)) {
			Output.simpleError(player, "You do not have enough "+StringUtils.lowerCase(cost.name())+"s to repair that tool, requires "+costAmount+".");
			return;
		}
		
		
		double dblSkillVal = (double)skill.getLvl()/1000;
		double rand = Math.random();
		if(dblSkillVal >= rand) {
			item.setDurability((short)0);
			pPlayer.setStamina(pPlayer.getStamina()-REPAIR_STAMINA_COST);
			int gain = skill.skillGain(pPlayer);
			Output.gainSkill(player, "Blacksmithy", gain, skill.getLvl());
			if(gain > 0)
				pPlayer.update();
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			Output.positiveMessage(player, "You repair the item.");
		}
		else {
			int gain = skill.skillGain(pPlayer);
			Output.gainSkill(player, "Blacksmithy", gain, skill.getLvl());
			if(gain > 0)
				pPlayer.update();
			item.setDurability((short)((int)item.getDurability()+(item.getDurability()*.5)+2));
			if(item.getDurability() < (short)item.getDurability()) {
				player.sendMessage(ChatColor.GRAY+"You failed to repair the "+item.getType().name().toLowerCase().replace("_", " ")+", it was damaged in the process.");
			}
			else {
				player.getInventory().clear(player.getInventory().getHeldItemSlot());
				player.sendMessage(ChatColor.GRAY+"You failed to repair the "+item.getType().name().toLowerCase().replace("_", " ")+", it was destroyed in the process.");
			}
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
		}
	}
	
	public static void smelt(Player player) {
		ItemStack item = player.getItemInHand();
		if(item == null) {
			Output.simpleError(player, "You cannot repair air.");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(!canRepair(item)) {
			Output.simpleError(player, "You cannot smelt that item.");
			return;
		}
		int amount = 1;
		Material cost = null;
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
			Output.simpleError(player, "You cannot smelt that item.");
			return;
		}
		
		if(ItemUtils.isArmor(cost))
			amount = 3;
		player.getInventory().setItemInHand(null);
		pPlayer.setStamina(pPlayer.getStamina()-SMELT_STAMINA_COST);
		player.getWorld().dropItem(player.getLocation(), new ItemStack(cost, amount)); 
		Output.positiveMessage(player, "You have smeltet your "+item.getType().name().toLowerCase().replace("_", " ")+" into "+cost.name().toLowerCase()+".");
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
