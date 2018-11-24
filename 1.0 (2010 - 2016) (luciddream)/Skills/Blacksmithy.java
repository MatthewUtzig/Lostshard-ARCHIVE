package com.lostshard.RPG.Skills;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.CustomContent.CustomArmor;
import com.lostshard.RPG.CustomContent.CustomTools;
import com.lostshard.RPG.CustomContent.CustomArmor.ArmorType;
import com.lostshard.RPG.CustomContent.CustomTools.ToolType;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Utils.Output;

public class Blacksmithy extends Skill{
	private static final int REPAIR_STAMINA_COST = 10;
	private static final int SMELT_STAMINA_COST = 15;
	private static final int SHARPEN_STAMINA_COST = 10;
	private static final int HARDEN_STAMINA_COST = 25;
	private static final int FORTIFY_STAMINA_COST = 25;
	
	public static void handleCommand(Player player, String[] split) {
		//if(split[0].equals("/blacksmithy")) {
			if(split.length == 1) {
				player.sendMessage(ChatColor.GOLD+"-Blacksmith Commands-");
				player.sendMessage(ChatColor.YELLOW+"repair - "+ChatColor.WHITE+"Attempts to repair the item you are holding.");
				player.sendMessage(ChatColor.YELLOW+"smelt - "+ChatColor.WHITE+"Attempts to smelt the item you are holding.");
			}
			else if(split.length > 1) {
				String splitCommand = split[1];
				if(splitCommand.equalsIgnoreCase("repair")) {
					repair(player);
				}
				else if(splitCommand.equalsIgnoreCase("smelt")) {
					smelt(player);
				}
				else if(splitCommand.equalsIgnoreCase("sharpen")) {
					sharpenWeapon(player);
				}
				else if(splitCommand.equalsIgnoreCase("power")) {
					powerWeapon(player);
				}
				else if(splitCommand.equalsIgnoreCase("harden")) {
					hardenArmor(player);
				}
				/*else if(splitCommand.equalsIgnoreCase("fortify")) {
					fortifyTool(player);
				}*/
				else if(splitCommand.equalsIgnoreCase("enhance")) {
					enhanceTool(player);
				}
			}
		//}
	}
	
	public static void powerWeapon(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
			Output.simpleError(player,  "You cannot power while in or shortly after combat.");
			return;
		}
		
		if(pseudoPlayer.getStamina() < SHARPEN_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Power requires "+SHARPEN_STAMINA_COST+".");
			return;
		}
		
		ItemStack itemInHand = player.getInventory().getItemInHand();
		int itemId = itemInHand.getTypeId();
		ToolType toolType = CustomTools.ToolType.getById(itemId);
		if(toolType != null) {
			if(toolType.canPower) {
				int curSkill = pseudoPlayer.getSkill("blacksmithy");
				boolean canGain = false;
				int maxLevel = 5;
				Material requiredMaterial = Material.DIAMOND;
				String resourceName = "diamond";				
				
				Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
				
				Set<Entry<Enchantment, Integer>> enchantmentSet = enchantments.entrySet();
				Entry<Enchantment, Integer> sharpnessEntry = null;
				for(Entry<Enchantment, Integer> entry : enchantmentSet) {
					Enchantment e = entry.getKey();
					if(e.equals(Enchantment.ARROW_DAMAGE)) {
						sharpnessEntry = entry;
					}
				}
				
				int numMaterialRequired = 0;
				int curLevel = 0;
				boolean allowUpgrade = false;
				
				// If the weapon hasn't been sharpened yet
				if(sharpnessEntry == null) {
					numMaterialRequired = 1;
					allowUpgrade = true;
				}
				else {
					int level = sharpnessEntry.getValue();
					
					if(level == 1) {
						curLevel = 1;
						numMaterialRequired = 2;
						allowUpgrade = true;
					}
					else if(level == 2) {
						curLevel = 2;
						numMaterialRequired = 4;
						allowUpgrade = true;
					}
					else if(level == 3) {
						curLevel = 3;
						numMaterialRequired = 8;
						allowUpgrade = true;
					}
					else if(level == 4) {
						curLevel = 4;
						Plot plot = PlotHandler.findPlotAt(player.getLocation());
						if(plot != null && plot.isControlPoint()) 
						{
							if(plot.getName().equals("Bunts Fiddly Bits")) {
								Clan owningClan = plot.getOwningClan();
								if(owningClan != null && owningClan.isInClan(player.getName())) {
									numMaterialRequired = 16;
									allowUpgrade = true;
								}
								else {
									Output.simpleError(player, "You can only power to level 5 in Bunts Fiddly Bits while your clan controls it.");
									return;
								}
							}
							else {
								Output.simpleError(player, "You can only power to level 5 in Bunts Fiddly Bits while your clan controls it.");
								return;
							}
						}
						else {
							Output.simpleError(player, "You can only power to level 5 in Bunts Fiddly Bits while your clan controls it.");
							return;
						}
					}
					else {
						Output.simpleError(player, "You cannot power that any further.");
						return;
					}
				}
				
				if(maxLevel < curLevel + 1) {
					Output.simpleError(player, "You can only power that item to level " + maxLevel);
					return;
				}
				
				if(allowUpgrade) {
					int numMaterial = 0; 
					for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(requiredMaterial.getId()).entrySet()) {
						numMaterial += i.getValue().getAmount();
					}
					
					if(numMaterial < numMaterialRequired) {
						Output.simpleError(player, "You do not have enough "+resourceName+"s to power that weapon, requires "+numMaterialRequired+".");
						return;
					}
					
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
					if(canGain)
						possibleSkillGain(player, pseudoPlayer);
					player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
					
					if(curLevel == 0) {
						itemInHand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
					}
					else {
						itemInHand.removeEnchantment(sharpnessEntry.getKey());
						itemInHand.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, curLevel+1);
					}
					
					Output.positiveMessage(player, "You have powered that item to level " + (curLevel+1)+".");
				}
				
				
				/*if(curSkill < 500) {
					Output.simpleError(player, "You are not skilled enough at blacksmithy to sharpen that.");
				}
				else if(curSkill < 1000) {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot sharpen that, it is already enchanted.");
					}
					else {
						itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
						
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						
						Output.positiveMessage(player, "You sharpen your blade.");
					}
				}
				else  {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot sharpen that, it is already enchanted.");
					}
					else {
						Enchantment ench1 = Enchantment.DAMAGE_ALL;
						itemInHand.addUnsafeEnchantment(ench1, 2);
						
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						
						Output.positiveMessage(player, "You exquisitely sharpen your blade.");
					}
				}*/
			}
			else {
				Output.simpleError(player, "That cannot be sharpened.");
			}
		}
		else {
			Output.simpleError(player, "That cannot be sharpened.");
		}
	}
	
	public static void enhanceTool(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
			Output.simpleError(player,  "You cannot enhance while in or shortly after combat.");
			return;
		}
		
		if(pseudoPlayer.getStamina() < SHARPEN_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Enhance requires "+SHARPEN_STAMINA_COST+".");
			return;
		}
		
		ItemStack itemInHand = player.getInventory().getItemInHand();
		int itemId = itemInHand.getTypeId();
		ToolType toolType = CustomTools.ToolType.getById(itemId);
		if(toolType != null) {
			if(toolType.canEnhance) {
				int curSkill = pseudoPlayer.getSkill("blacksmithy");
				boolean canGain = false;
				int maxLevel = 0;
				
				Material requiredMaterial = null;
				String resourceName = "I AM ERROR";
				if(toolType.getMaterial().equals("wood")) {
					requiredMaterial = Material.WOOD;
					resourceName = "board";
					maxLevel = 0;
					if(curSkill < 250)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("stone")) {
					requiredMaterial = Material.COBBLESTONE;
					resourceName = "cobblestone";
					maxLevel = 1;
					if(curSkill < 500)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("iron")) {
					requiredMaterial = Material.IRON_INGOT;
					resourceName = "iron ingot";
					maxLevel = 2;
					if(curSkill < 750)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("diamond")) {
					requiredMaterial = Material.DIAMOND;
					maxLevel = 5;
					resourceName = "diamond";
					canGain = true;
				}
				else if(toolType.getMaterial().equals("gold")) {
					requiredMaterial = Material.GOLD_INGOT;
					maxLevel = 5;
					resourceName = "gold ingot";
					canGain = true;
				}
				
				if(requiredMaterial == null) {
					Output.simpleError(player, "Error - No material found.");
					return;
				}
				
				
				Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
				
				Set<Entry<Enchantment, Integer>> enchantmentSet = enchantments.entrySet();
				Entry<Enchantment, Integer> unbreakableEntry = null;
				Entry<Enchantment, Integer> efficiencyEntry = null;
				Entry<Enchantment, Integer> fortuneEntry = null;
				for(Entry<Enchantment, Integer> entry : enchantmentSet) {
					Enchantment e = entry.getKey();
					if(e.equals(Enchantment.DURABILITY)) {
						unbreakableEntry = entry;
					}
					else if(e.equals(Enchantment.DIG_SPEED)) {
						efficiencyEntry = entry;
					}
					else if(e.equals(Enchantment.LOOT_BONUS_BLOCKS)) {
						fortuneEntry = entry;
					}
				}
				
				int numMaterialRequired = 0;
				int curLevel = 0;
				boolean allowUpgrade = false;
				
				// If the weapon hasn't been sharpened yet
				if(efficiencyEntry == null) {
					numMaterialRequired = 1;
					allowUpgrade = true;
				}
				else {
					int level = efficiencyEntry.getValue();
					
					if(level == 1) {
						curLevel = 1;
						numMaterialRequired = 2;
						allowUpgrade = true;
					}
					else if(level == 2) {
						curLevel = 2;
						numMaterialRequired = 4;
						allowUpgrade = true;
					}
					else if(level == 3) {
						curLevel = 3;
						numMaterialRequired = 8;
						allowUpgrade = true;
					}
					else if(level == 4) {
						curLevel = 4;
						Plot plot = PlotHandler.findPlotAt(player.getLocation());
						if(plot != null && plot.isControlPoint()) 
						{
							if(plot.getName().equals("Bunts Fiddly Bits")) {
								Clan owningClan = plot.getOwningClan();
								if(owningClan != null && owningClan.isInClan(player.getName())) {
									numMaterialRequired = 16;
									allowUpgrade = true;
								}
								else {
									Output.simpleError(player, "You can only enhance to level 5 in Bunts Fiddly Bits while your clan controls it.");
									return;
								}
							}
							else {
								Output.simpleError(player, "You can only enhance to level 5 in Bunts Fiddly Bits while your clan controls it.");
								return;
							}
						}
						else {
							Output.simpleError(player, "You can only enhance to level 5 in Bunts Fiddly Bits while your clan controls it.");
							return;
						}
					}
					else {
						Output.simpleError(player, "You cannot enhance that any further.");
						return;
					}
				}
				
				if(maxLevel < curLevel + 1) {
					Output.simpleError(player, "You can only enhance that item to level " + maxLevel);
					return;
				}
				
				if(allowUpgrade) {
					int numMaterial = 0; 
					for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(requiredMaterial.getId()).entrySet()) {
						numMaterial += i.getValue().getAmount();
					}
					
					if(numMaterial < numMaterialRequired) {
						Output.simpleError(player, "You do not have enough "+resourceName+"s to enhance that tool, requires "+numMaterialRequired+".");
						return;
					}
					
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
					if(canGain)
						possibleSkillGain(player, pseudoPlayer);
					player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
					
					if(curLevel == 0) {
						try {itemInHand.removeEnchantment(unbreakableEntry.getKey());}
						catch(Exception e) {}
						try {itemInHand.removeEnchantment(efficiencyEntry.getKey());}
						catch(Exception e) {}
						try {itemInHand.removeEnchantment(fortuneEntry.getKey());}
						catch(Exception e) {}
						
						itemInHand.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
						itemInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
					}
					else {
						try {itemInHand.removeEnchantment(unbreakableEntry.getKey());}
						catch(Exception e) {}
						try {itemInHand.removeEnchantment(efficiencyEntry.getKey());}
						catch(Exception e) {}
						try {itemInHand.removeEnchantment(fortuneEntry.getKey());}
						catch(Exception e) {}
						
						if(curLevel == 1) {
							itemInHand.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
						}
						else {
							itemInHand.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
						}
						itemInHand.addUnsafeEnchantment(Enchantment.DIG_SPEED, curLevel+1);
						if(curLevel == 2) {
							itemInHand.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
						}
						else if(curLevel == 3) {
							itemInHand.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
						}
						else if(curLevel == 4) {
							itemInHand.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
						}
					}
					
					Output.positiveMessage(player, "You have enhanced that item to level " + (curLevel+1)+".");
				}
				
				
				/*if(curSkill < 500) {
					Output.simpleError(player, "You are not skilled enough at blacksmithy to sharpen that.");
				}
				else if(curSkill < 1000) {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot sharpen that, it is already enchanted.");
					}
					else {
						itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
						
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						
						Output.positiveMessage(player, "You sharpen your blade.");
					}
				}
				else  {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot sharpen that, it is already enchanted.");
					}
					else {
						Enchantment ench1 = Enchantment.DAMAGE_ALL;
						itemInHand.addUnsafeEnchantment(ench1, 2);
						
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						
						Output.positiveMessage(player, "You exquisitely sharpen your blade.");
					}
				}*/
			}
			else {
				Output.simpleError(player, "That cannot be enhanced.");
			}
		}
		else {
			Output.simpleError(player, "That cannot be sharpened.");
		}
	}
	
	public static void sharpenWeapon(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
			Output.simpleError(player,  "You cannot sharpen while in or shortly after combat.");
			return;
		}
		
		if(pseudoPlayer.getStamina() < SHARPEN_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Sharpen requires "+SHARPEN_STAMINA_COST+".");
			return;
		}
		
		ItemStack itemInHand = player.getInventory().getItemInHand();
		int itemId = itemInHand.getTypeId();
		ToolType toolType = CustomTools.ToolType.getById(itemId);
		if(toolType != null) {
			if(toolType.canSharpen) {
				int curSkill = pseudoPlayer.getSkill("blacksmithy");
				boolean canGain = false;
				int maxLevel = 0;
				
				Material requiredMaterial = null;
				String resourceName = "I AM ERROR";
				if(toolType.getMaterial().equals("wood")) {
					requiredMaterial = Material.WOOD;
					resourceName = "board";
					maxLevel = 0;
					if(curSkill < 250)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("stone")) {
					requiredMaterial = Material.COBBLESTONE;
					resourceName = "cobblestone";
					maxLevel = 1;
					if(curSkill < 500)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("iron")) {
					requiredMaterial = Material.IRON_INGOT;
					resourceName = "iron ingot";
					maxLevel = 2;
					if(curSkill < 750)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("diamond")) {
					requiredMaterial = Material.DIAMOND;
					maxLevel = 5;
					resourceName = "diamond";
					canGain = true;
				}
				else if(toolType.getMaterial().equals("gold")) {
					requiredMaterial = Material.GOLD_INGOT;
					maxLevel = 5;
					resourceName = "gold ingot";
					canGain = true;
				}
				
				if(requiredMaterial == null) {
					Output.simpleError(player, "Error - No material found.");
					return;
				}
				
				
				Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
				
				Set<Entry<Enchantment, Integer>> enchantmentSet = enchantments.entrySet();
				Entry<Enchantment, Integer> sharpnessEntry = null;
				for(Entry<Enchantment, Integer> entry : enchantmentSet) {
					Enchantment e = entry.getKey();
					if(e.equals(Enchantment.DAMAGE_ALL)) {
						sharpnessEntry = entry;
					}
				}
				
				int numMaterialRequired = 0;
				int curLevel = 0;
				boolean allowUpgrade = false;
				
				// If the weapon hasn't been sharpened yet
				if(sharpnessEntry == null) {
					numMaterialRequired = 1;
					allowUpgrade = true;
				}
				else {
					int level = sharpnessEntry.getValue();
					
					if(level == 1) {
						curLevel = 1;
						numMaterialRequired = 2;
						allowUpgrade = true;
					}
					else if(level == 2) {
						curLevel = 2;
						numMaterialRequired = 4;
						allowUpgrade = true;
					}
					else if(level == 3) {
						curLevel = 3;
						numMaterialRequired = 8;
						allowUpgrade = true;
					}
					else if(level == 4) {
						curLevel = 4;
						Plot plot = PlotHandler.findPlotAt(player.getLocation());
						if(plot != null && plot.isControlPoint()) 
						{
							if(plot.getName().equals("Bunts Fiddly Bits")) {
								Clan owningClan = plot.getOwningClan();
								if(owningClan != null && owningClan.isInClan(player.getName())) {
									numMaterialRequired = 16;
									allowUpgrade = true;
								}
								else {
									Output.simpleError(player, "You can only sharpen to level 5 in Bunts Fiddly Bits while your clan controls it.");
									return;
								}
							}
							else {
								Output.simpleError(player, "You can only sharpen to level 5 in Bunts Fiddly Bits while your clan controls it.");
								return;
							}
						}
						else {
							Output.simpleError(player, "You can only sharpen to level 5 in Bunts Fiddly Bits while your clan controls it.");
							return;
						}
					}
					else {
						Output.simpleError(player, "You cannot sharpen that any further.");
						return;
					}
				}
				
				if(maxLevel < curLevel + 1) {
					Output.simpleError(player, "You can only sharpen that item to level " + maxLevel);
					return;
				}
				
				if(allowUpgrade) {
					int numMaterial = 0; 
					for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(requiredMaterial.getId()).entrySet()) {
						numMaterial += i.getValue().getAmount();
					}
					
					if(numMaterial < numMaterialRequired) {
						Output.simpleError(player, "You do not have enough "+resourceName+"s to sharpen that weapon, requires "+numMaterialRequired+".");
						return;
					}
					
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
					if(canGain)
						possibleSkillGain(player, pseudoPlayer);
					player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
					
					if(curLevel == 0) {
						itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
					}
					else {
						itemInHand.removeEnchantment(sharpnessEntry.getKey());
						itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, curLevel+1);
					}
					
					Output.positiveMessage(player, "You have sharpened that item to level " + (curLevel+1)+".");
				}
				
				
				/*if(curSkill < 500) {
					Output.simpleError(player, "You are not skilled enough at blacksmithy to sharpen that.");
				}
				else if(curSkill < 1000) {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot sharpen that, it is already enchanted.");
					}
					else {
						itemInHand.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
						
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						
						Output.positiveMessage(player, "You sharpen your blade.");
					}
				}
				else  {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot sharpen that, it is already enchanted.");
					}
					else {
						Enchantment ench1 = Enchantment.DAMAGE_ALL;
						itemInHand.addUnsafeEnchantment(ench1, 2);
						
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SHARPEN_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						
						Output.positiveMessage(player, "You exquisitely sharpen your blade.");
					}
				}*/
			}
			else {
				Output.simpleError(player, "That cannot be sharpened.");
			}
		}
		else {
			Output.simpleError(player, "That cannot be sharpened.");
		}
	}
	
	public static Enchantment getRandomSharpenEnchantment() {
		double random = Math.random();
		if(random < .25) {
			return Enchantment.DAMAGE_ALL;
		}
		else if(random < .5) {
			return Enchantment.DAMAGE_ARTHROPODS;
		}
		else if(random < .75) {
			return Enchantment.DAMAGE_UNDEAD;
		}
		else {
			return Enchantment.KNOCKBACK;
		}
	}
	
	public static void hardenArmor(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
			Output.simpleError(player,  "You cannot harden while in or shortly after combat.");
			return;
		}
		
		if(pseudoPlayer.getStamina() < HARDEN_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Fortify requires "+HARDEN_STAMINA_COST+".");
			return;
		}
		
		ItemStack itemInHand = player.getInventory().getItemInHand();
		int itemId = itemInHand.getTypeId();
		ArmorType armorType = CustomArmor.ArmorType.getById(itemId);
		if(armorType != null) {
			int curSkill = pseudoPlayer.getSkill("blacksmithy");
			boolean canGain = false;
			
			Material requiredMaterial = null;
			String resourceName = "I AM ERROR";
			if(armorType.getMaterial().equals("leather")) {
				requiredMaterial = Material.DIAMOND;
				resourceName = "diamond";
				if(curSkill < 250)
					canGain = true;
			}
			else if(armorType.getMaterial().equals("chainmail")) {
				requiredMaterial = Material.DIAMOND;
				resourceName = "diamond";
				if(curSkill < 500)
					canGain = true;
			}
			else if(armorType.getMaterial().equals("iron")) {
				requiredMaterial = Material.DIAMOND;
				resourceName = "idiamond";
				if(curSkill < 750)
					canGain = true;
			}
			else if(armorType.getMaterial().equals("diamond")) {
				requiredMaterial = Material.DIAMOND;
				resourceName = "diamond";
				canGain = true;
			}
			else if(armorType.getMaterial().equals("gold")) {
				requiredMaterial = Material.DIAMOND;
				resourceName = "diamond";
				canGain = true;
			}
			
			if(requiredMaterial == null) {
				Output.simpleError(player, "Error - No material found.");
				return;
			}
			
			int numMaterialRequired = 2;
			int numMaterial = 0; 
			for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(requiredMaterial.getId()).entrySet()) {
				numMaterial += i.getValue().getAmount();
			}
			
			if(numMaterial < numMaterialRequired) {
				Output.simpleError(player, "You do not have enough "+resourceName+"s to harden that armor, requires "+numMaterialRequired+".");
				return;
			}
			
			//Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
			if(curSkill < 1000) {
				Output.simpleError(player, "You are not skilled enough at blacksmithy to harden that.");
			}
			else {
				itemInHand.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				
				pseudoPlayer.setStamina(pseudoPlayer.getStamina()-HARDEN_STAMINA_COST);
				if(canGain)
					possibleSkillGain(player, pseudoPlayer);
				player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
				
				Output.positiveMessage(player, "You harden your armor.");
			}
			/*else if(curSkill < 500) {
				if (enchantments.size() > 0) {
					Output.simpleError(player, "You cannot harden that, it is already enchanted.");
				}
				else {
					itemInHand.addEnchantment(getRandomHardenEnchantment(), 1);
					
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-HARDEN_STAMINA_COST);
					if(canGain)
						possibleSkillGain(player, pseudoPlayer);
					player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
					
					Output.positiveMessage(player, "You clumsily harden your armor.");
				}
			}
			else if(curSkill < 750) {
				if (enchantments.size() > 0) {
					Output.simpleError(player, "You cannot harden that, it is already enchanted.");
				}
				else {
					Enchantment ench1 = getRandomHardenEnchantment();
					Enchantment ench2 = ench1;
					// Get a different enchantment for enchant 2
					while(ench2.equals(ench1)) {
						ench2 = getRandomHardenEnchantment();
					}
					itemInHand.addEnchantment(ench1, 1);
					itemInHand.addEnchantment(ench2, 1);
					
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-HARDEN_STAMINA_COST);
					if(canGain)
						possibleSkillGain(player, pseudoPlayer);
					player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
					
					Output.positiveMessage(player, "You harden your armor.");
				}
			}
			else if(curSkill < 1000) {
				if (enchantments.size() > 0) {
					Output.simpleError(player, "You cannot harden that, it is already enchanted.");
				}
				else {
					Enchantment ench1 = getRandomHardenEnchantment();
					Enchantment ench2 = ench1;
					// Get a different enchantment for enchant 2
					while(ench2.equals(ench1)) {
						ench2 = getRandomHardenEnchantment();
					}
					itemInHand.addEnchantment(ench1, 2);
					itemInHand.addEnchantment(ench2, 1);
					
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-HARDEN_STAMINA_COST);
					if(canGain)
						possibleSkillGain(player, pseudoPlayer);
					player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
					
					Output.positiveMessage(player, "You skillfully harden your armor.");
				}
			}
			else if(curSkill >= 1000) {
				if (enchantments.size() > 0) {
					Output.simpleError(player, "You cannot harden that, it is already enchanted.");
				}
				else {
					Enchantment ench1 = getRandomHardenEnchantment();
					Enchantment ench2 = ench1;
					// Get a different enchantment for enchant 2
					while(ench2.equals(ench1)) {
						ench2 = getRandomHardenEnchantment();
					}
					itemInHand.addEnchantment(ench1, 2);
					itemInHand.addEnchantment(ench2, 2);
					
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-HARDEN_STAMINA_COST);
					if(canGain)
						possibleSkillGain(player, pseudoPlayer);
					player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
					
					Output.positiveMessage(player, "You Exquisitely harden your armor.");
				}
			}*/
		}
		else {
			Output.simpleError(player, "That cannot be hardened.");
		}
	}
	
	public static Enchantment getRandomHardenEnchantment() {
		double random = Math.random();
		if(random < .25) {
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		}
		else if(random < .5) {
			return Enchantment.PROTECTION_EXPLOSIONS;
		}
		else if(random < .75) {
			return Enchantment.PROTECTION_FIRE;
		}
		else {
			return Enchantment.PROTECTION_PROJECTILE;
		}
	}
	
	public static void fortifyTool(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
			Output.simpleError(player,  "You cannot fortify while in or shortly after combat.");
			return;
		}
		
		if(pseudoPlayer.getStamina() < FORTIFY_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Fortify requires "+FORTIFY_STAMINA_COST+".");
			return;
		}
		
		ItemStack itemInHand = player.getInventory().getItemInHand();
		int itemId = itemInHand.getTypeId();
		ToolType toolType = CustomTools.ToolType.getById(itemId);
		if(toolType != null) {
			if(toolType.canFortify) {
				int curSkill = pseudoPlayer.getSkill("blacksmithy");
				boolean canGain = false;
				
				Material requiredMaterial = null;
				String resourceName = "I AM ERROR";
				if(toolType.getMaterial().equals("wood")) {
					requiredMaterial = Material.WOOD;
					resourceName = "board";
					if(curSkill < 250)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("stone")) {
					requiredMaterial = Material.COBBLESTONE;
					resourceName = "cobblestone";
					if(curSkill < 500)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("iron")) {
					requiredMaterial = Material.IRON_INGOT;
					resourceName = "iron ingot";
					if(curSkill < 750)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("diamond")) {
					requiredMaterial = Material.DIAMOND;
					resourceName = "diamond";
					canGain = true;
				}
				else if(toolType.getMaterial().equals("gold")) {
					requiredMaterial = Material.GOLD_INGOT;
					resourceName = "gold ingot";
					canGain = true;
				}
				
				if(requiredMaterial == null) {
					Output.simpleError(player, "Error - No material found.");
					return;
				}
				
				int numMaterialRequired = 2;
				int numMaterial = 0; 
				for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(requiredMaterial.getId()).entrySet()) {
					numMaterial += i.getValue().getAmount();
				}
				
				if(numMaterial < numMaterialRequired) {
					Output.simpleError(player, "You do not have enough "+resourceName+"s to fortify that tool, requires "+numMaterialRequired+".");
					return;
				}
				
				Map<Enchantment, Integer> enchantments = itemInHand.getEnchantments();
				if(curSkill < 250) {
					Output.simpleError(player, "You are not skilled enough at blacksmithy to fortify that.");
				}
				else if(curSkill < 750) {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot fortify that, it is already enchanted.");
						/*
						for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
							Enchantment enchantment = entry.getKey();
						}*/
					}
					else {
						itemInHand.addEnchantment(getRandomFortifyEnchantment(), 1);
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-FORTIFY_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						Output.positiveMessage(player, "You clumsily fortify your tool.");
					}
				}
				else {
					if (enchantments.size() > 0) {
						Output.simpleError(player, "You cannot fortify that, it is already enchanted.");
					}
					else {
						itemInHand.addEnchantment(getRandomFortifyEnchantment(), 2);
						pseudoPlayer.setStamina(pseudoPlayer.getStamina()-FORTIFY_STAMINA_COST);
						if(canGain)
							possibleSkillGain(player, pseudoPlayer);
						player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
						Output.positiveMessage(player, "You skillfully fortify your tool.");
					}
				}
			}
			else {
				Output.simpleError(player, "That cannot be fortified.");
			}
		}
		else {
			Output.simpleError(player, "That cannot be fortified.");
		}
	}
	
	public static Enchantment getRandomFortifyEnchantment() {
		return Enchantment.DURABILITY;
	}
	
	public static void repair(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
			Output.simpleError(player,  "You cannot repair while in or shortly after combat.");
			return;
		}
		
		if(pseudoPlayer.getStamina() < REPAIR_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Repair requires "+REPAIR_STAMINA_COST+".");
			return;
		}
		
		ItemStack itemInHand = player.getInventory().getItemInHand();
		int itemId = itemInHand.getTypeId();
		ToolType toolType = CustomTools.ToolType.getById(itemId);
		ArmorType armorType = CustomArmor.ArmorType.getById(itemId);
		if(toolType != null) {
			int curSkill = pseudoPlayer.getSkill("blacksmithy");
			boolean canGain = false;
				
			Material requiredMaterial = null;
			String resourceName = "I AM ERROR";
			if(toolType.getMaterial().equals("wood")) {
				requiredMaterial = Material.WOOD;
				resourceName = "board";
				if(curSkill < 250)
					canGain = true;
			}
			else if(toolType.getMaterial().equals("stone")) {
				requiredMaterial = Material.COBBLESTONE;
				resourceName = "cobblestone";
				if(curSkill < 500)
					canGain = true;
			}
			else if(toolType.getMaterial().equals("iron")) {
				requiredMaterial = Material.IRON_INGOT;
				resourceName = "iron ingot";
				if(curSkill < 750)
					canGain = true;
			}
			else if(toolType.getMaterial().equals("diamond")) {
				requiredMaterial = Material.DIAMOND;
				resourceName = "diamond";
				canGain = true;
			}
			else if(toolType.getMaterial().equals("gold")) {
				requiredMaterial = Material.GOLD_INGOT;
				resourceName = "gold ingot";
				canGain = true;
			}
			else if(toolType.getMaterial().equals("string")) {
				requiredMaterial = Material.STRING;
				resourceName = "string";
				if(curSkill < 750)
					canGain = true;
			}
				
			if(requiredMaterial == null) {
				Output.simpleError(player, "Error - No material found.");
				return;
			}
				
			int numMaterialRequired = 1;
			int numMaterial = 0; 
			for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(requiredMaterial.getId()).entrySet()) {
				numMaterial += i.getValue().getAmount();
			}
				
			if(numMaterial < numMaterialRequired) {
				Output.simpleError(player, "You do not have enough "+resourceName+"s to repair that tool, requires "+numMaterialRequired+".");
				return;
			}
			
			double dblSkillVal = (double)curSkill/1000;
			double rand = Math.random();
			if(dblSkillVal >= rand) {
				itemInHand.setDurability((short)0);
				pseudoPlayer.setStamina(pseudoPlayer.getStamina()-REPAIR_STAMINA_COST);
				if(canGain)
					possibleSkillGain(player, pseudoPlayer);
				player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
				Output.positiveMessage(player, "You repair the item.");
			}
			else {
				if(canGain)
					possibleSkillGain(player, pseudoPlayer);
				
				itemInHand.setDurability((short)((int)itemInHand.getDurability()+(toolType.getDurability()*.5)+2));
				if(itemInHand.getDurability() < (short)toolType.getDurability()) {
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was damaged in the process.");
				}
				else {
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was destroyed in the process.");
				}
			}			
		}
		else if (armorType != null) {
			int curSkill = pseudoPlayer.getSkill("blacksmithy");
			boolean canGain = false;
			int numMaterialRequired = 1;
			Material requiredMaterial = null;
			String resourceName = "I AM ERROR";
			if(armorType.getMaterial().equals("leather")) {
				requiredMaterial = Material.LEATHER;
				resourceName = "leather";
				if(curSkill < 250)
					canGain = true;
			}
			else if(armorType.getMaterial().equals("chainmail")) {
				requiredMaterial = Material.COBBLESTONE;
				resourceName = "cobblestone";
				if(curSkill < 500)
					canGain = true;
			}
			else if(armorType.getMaterial().equals("iron")) {
				requiredMaterial = Material.IRON_INGOT;
				resourceName = "iron ingot";
				if(curSkill < 750)
					canGain = true;
			}
			else if(armorType.getMaterial().equals("diamond")) {
				requiredMaterial = Material.IRON_INGOT;
				numMaterialRequired = 3;
				resourceName = "iron ingot";
				canGain = true;
			}
			else if(armorType.getMaterial().equals("gold")) {
				requiredMaterial = Material.GOLD_INGOT;
				resourceName = "gold ingot";
				canGain = true;
			}
				
			if(requiredMaterial == null) {
				Output.simpleError(player, "Error - No material found.");
				return;
			}
				
			int numMaterial = 0; 
			for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(requiredMaterial.getId()).entrySet()) {
				numMaterial += i.getValue().getAmount();
			}
				
			if(numMaterial < numMaterialRequired) {
				Output.simpleError(player, "You do not have enough "+resourceName+"s to repair that armor, requires "+numMaterialRequired+".");
				return;
			}
			
			double dblSkillVal = (double)curSkill/1000;
			double rand = Math.random();
			if(dblSkillVal >= rand) {
				itemInHand.setDurability((short)0);
				pseudoPlayer.setStamina(pseudoPlayer.getStamina()-REPAIR_STAMINA_COST);
				if(canGain)
					possibleSkillGain(player, pseudoPlayer);
				player.getInventory().removeItem(new ItemStack(requiredMaterial.getId(), numMaterialRequired));
				Output.positiveMessage(player, "You repair the item.");
			}
			else {
				if(canGain)
					possibleSkillGain(player, pseudoPlayer);
				
				if(itemInHand.getDurability() < (short)armorType.getDurability()) {
					itemInHand.setDurability((short)((int)itemInHand.getDurability()+(armorType.getDurability()*.5)+2));
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was damaged in the process.");
				}
				else {
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was destroyed in the process.");
				}
			}
		}
		else {
			Output.simpleError(player, "That cannot be repaired.");
		}
	}
	
	/*public static void repair(Player player) {
		ItemStack itemInHand = player.getInventory().getItemInHand();
		if(itemInHand != null) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			if(pseudoPlayer._pvpTicks > 0 || pseudoPlayer._engageInCombatTicks > 0) {
				Output.simpleError(player,  "You cannot repair while in or shortly after combat.");
				return;
			}
			
			if(pseudoPlayer.getStamina() < REPAIR_STAMINA_COST) {
				Output.simpleError(player, "Not enough stamina - Repair requires "+REPAIR_STAMINA_COST+".");
				return;
			}
			
			int curSkill = pseudoPlayer.getSkill("blacksmithy");
			
			int itemId = itemInHand.getTypeId();
			ToolType toolType = CustomTools.ToolType.getById(itemId);
			ArmorType armorType = CustomArmor.ArmorType.getById(itemId);
			boolean canGain = false;
			int ironRequired = 0;
			boolean validItem = false;
			if(toolType != null) {
				validItem = true;
				// using tool
				//maxDamage = (int)toolType.getDurability();
				if(toolType.getMaterial().equals("gold")) {
					// gold tool
					ironRequired = 3;
					canGain = true;
				}
				else if(toolType.getMaterial().equals("diamond")) {
					// diamond tool
					ironRequired = 2;
					canGain = true;
				}
				else if(toolType.getMaterial().equals("iron")) {
					// iron tool
					ironRequired = 1;
					if(curSkill < 900)
						canGain = true;
				}
				else if(toolType.getMaterial().equals("stone")) {
					// stone tool
					if(curSkill < 600) 
						canGain = true;
				}
				else {
					// wood tool/etc
					if(curSkill < 300)
						canGain = true;
				}
			}
			else if(armorType != null) {
				validItem = true;
				// using armor
				//maxDamage = armorType.getDurability();
				if(armorType.getMaterial().equals("gold")) {
					// gold tool
					ironRequired = 3;
					canGain = true;
				}
				else if(armorType.getMaterial().equals("diamond")) {
					// diamond tool
					ironRequired = 2;
					canGain = true;
				}
				else if(armorType.getMaterial().equals("iron")) {
					// iron tool
					ironRequired = 1;
					if(curSkill < 900)
						canGain = true;
				}
				else if(armorType.getMaterial().equals("chainmail")) {
					// stone tool
					if(curSkill < 600) 
						canGain = true;
				}
				else {
					// wood tool/etc
					if(curSkill < 300)
						canGain = true;
				}
			}
			
			if(validItem) {
				boolean hasEnoughIron = true;
				if(ironRequired > 0) {
					int numIron = 0; 
					for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(265).entrySet()) {
						numIron += i.getValue().getAmount();
					}
					if(numIron < ironRequired)
						hasEnoughIron = false;
				}
				
				if(hasEnoughIron) {
					doRepair(player, pseudoPlayer, curSkill, toolType, itemInHand, ironRequired, armorType, canGain);
					
				}
				else Output.simpleError(player, "Not enough iron ingots to repair, it requires "+ironRequired+".");
			}
			else {
				Output.simpleError(player, "That item cannot be repaired, only ");
				Output.simpleError(player, "tools/weapons/armor may be repaired.");
			}
		}
	}*/
	
	/*public static void doRepair(Player player, PseudoPlayer pseudoPlayer, int curSkill, ToolType toolType, ItemStack itemInHand, int ironRequired, ArmorType armorType, Boolean canGain) {
		double dblSkillVal = (double)curSkill/1000;
		double rand = Math.random();
		if(dblSkillVal >= rand) {
			if(toolType != null) {
				// a tool (maybe a weapon)
				if(toolType.getId() == Material.WOOD_SWORD.getId() ||
					toolType.getId() == Material.STONE_SWORD.getId() ||
					toolType.getId() == Material.IRON_SWORD.getId() ||
					toolType.getId() == Material.DIAMOND_SWORD.getId() ||
					toolType.getId() == Material.GOLD_SWORD.getId() ||
					toolType.getId() == Material.WOOD_AXE.getId() ||
					toolType.getId() == Material.STONE_AXE.getId() ||
					toolType.getId() == Material.IRON_AXE.getId() ||
					toolType.getId() == Material.DIAMOND_AXE.getId() ||
					toolType.getId() == Material.GOLD_AXE.getId()) {
					// is a weapon
					if(curSkill >= 1000) {
						itemInHand.setDurability((short)-5);
						player.sendMessage(ChatColor.GOLD+"You successfully repaired the weapon.");
						player.sendMessage(ChatColor.GOLD+"You sharpened the weapon in the process.");
						player.getInventory().removeItem(new ItemStack(265, ironRequired));
					}
					else {
						itemInHand.setDurability((short)0);
						player.sendMessage(ChatColor.GOLD+"You successfully repaired the weapon.");
						player.getInventory().removeItem(new ItemStack(265, ironRequired));
					}
				}
				else {
					// tool
					if(pseudoPlayer.getSkill("blacksmithy") >= 500) {
						itemInHand.setDurability((short)(0-(toolType.getDurability()*.5)));
						player.sendMessage(ChatColor.GOLD+"You successfully repaired the tool.");
						player.sendMessage(ChatColor.GOLD+"You reinforced the tool in the process.");
						player.getInventory().removeItem(new ItemStack(265, ironRequired));
					}
					else {
						itemInHand.setDurability((short)0);
						player.sendMessage(ChatColor.GOLD+"You successfully repaired the tool.");
						player.getInventory().removeItem(new ItemStack(265, ironRequired));
					}
				}
			}
			else if(armorType != null) {
				// an armor piece
				if(pseudoPlayer.getSkill("blacksmithy") >= 750) {
					itemInHand.setDurability((short)-10);
					player.sendMessage(ChatColor.GOLD+"You successfully repaired the armor.");
					if(armorType.getMaterial().equals("leather"))
						player.sendMessage(ChatColor.GOLD+"You add rivets or something to the armor.");
					else 
						player.sendMessage(ChatColor.GOLD+"You hardened the armor in the process.");
					player.getInventory().removeItem(new ItemStack(265, ironRequired));
				}
				else {
					itemInHand.setDurability((short)0);
					player.sendMessage(ChatColor.GOLD+"You successfully repaired the armor.");
					player.getInventory().removeItem(new ItemStack(265, ironRequired));
				}
			}
			else {
				itemInHand.setDurability((short)0);
				player.sendMessage(ChatColor.GOLD+"You successfully repaired the item.");
				player.getInventory().removeItem(new ItemStack(265, ironRequired));
			}

		}
		else { 
			// fail
			if(toolType != null) {
				//System.out.println(toolType.getDurability());
				itemInHand.setDurability((short)((int)itemInHand.getDurability()+(toolType.getDurability()*.5)+2));
				//itemInHand.setDurability((short)754);
				if(itemInHand.getDurability() < (short)toolType.getDurability()) {
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was damaged in the process.");
				}
				else {
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was destroyed in the process.");
				}
			}
			else if(armorType != null) {
				if(itemInHand.getDurability() < (short)armorType.getDurability()) {
					itemInHand.setDurability((short)((int)itemInHand.getDurability()+(armorType.getDurability()*.5)+2));
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was damaged in the process.");
				}
				else {
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
					player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was destroyed in the process.");
				}
			}
			else {
				player.getInventory().clear(player.getInventory().getHeldItemSlot());
				player.sendMessage(ChatColor.GRAY+"You failed to repair the item, it was destroyed in the process.");
			}
		}
		
		if(canGain) {
			possibleSkillGain(player, pseudoPlayer);
		}
	}*/

	public static void smelt(Player player) {
		ItemStack itemInHand = player.getInventory().getItemInHand();
		if(itemInHand != null) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			if(pseudoPlayer.getStamina() < SMELT_STAMINA_COST) {
				Output.simpleError(player, "Not enough stamina - Smelting requires "+SMELT_STAMINA_COST+".");
				return;
			}
			
			int curSkill = pseudoPlayer.getSkill("blacksmithy");
			
			int itemId = itemInHand.getTypeId();
			ToolType toolType = CustomTools.ToolType.getById(itemId);
			ArmorType armorType = CustomArmor.ArmorType.getById(itemId);
			/*short curDamage = itemInHand.getDurability();
			double maxDamage;*/
			boolean canGain = false;
			boolean validItem = false;
			ItemStack returnedItem = null;
			if(toolType != null) {
				validItem = true;
				// using tool
				//maxDamage = (int)toolType.getDurability();
				if(toolType.getMaterial().equals("gold")) {
					// gold tool
					returnedItem = new ItemStack(Material.GOLD_INGOT, 1);
					canGain = true;
				}
				else if(toolType.getMaterial().equals("diamond")) {
					// diamond tool
					returnedItem = new ItemStack(Material.DIAMOND, 1);
					canGain = true;
				}
				else if(toolType.getMaterial().equals("iron")) {
					// iron tool
					returnedItem = new ItemStack(Material.IRON_INGOT, 1);
					if(curSkill < 900)
						canGain = true;
				}
			}
			else if(armorType != null) {
				validItem = true;
				// using armor
				//maxDamage = armorType.getDurability();
				if(armorType.getMaterial().equals("gold")) {
					// gold tool
					returnedItem = new ItemStack(Material.GOLD_INGOT, 3);
						canGain = true;
				}
				else if(armorType.getMaterial().equals("diamond")) {
					// diamond tool
					returnedItem = new ItemStack(Material.DIAMOND, 3);
						canGain = true;
				}
				else if(armorType.getMaterial().equals("iron")) {
					// iron tool
					returnedItem = new ItemStack(Material.IRON_INGOT, 3);
					if(curSkill < 900)
						canGain = true;
				}
			}
			else if(itemId == Material.BUCKET.getId()) {
				validItem = true;
				returnedItem = new ItemStack(Material.IRON_INGOT, 1);
				if(curSkill < 900)
					canGain = true;
			}
			else if(itemId == Material.FLINT_AND_STEEL.getId()) {
				validItem = true;
				returnedItem = new ItemStack(Material.IRON_INGOT, 1);
				if(curSkill < 900)
					canGain = true;
			}
			/*else if(itemId == Material.IRON_ORE.getId()) {
				if(curSkill >= 500) 
				validItem = true;
				returnedItem = new ItemStack(Material.IRON_INGOT, itemInHand.getAmount());
				if(curSkill < 500)
					canGain = true;
			}
			else if(itemId == Material.GOLD_ORE.getId()) {
				validItem = true;
				returnedItem = new ItemStack(Material.GOLD_INGOT, itemInHand.getAmount());
				if(curSkill < 500)
					canGain = true;
			}*/
			
			if(validItem && returnedItem != null) {		
				pseudoPlayer.setStamina(pseudoPlayer.getStamina()-SMELT_STAMINA_COST);
				double dblSkillVal = (double)curSkill/500;
				double rand = Math.random();
				if(dblSkillVal >= rand) {
					// success, now see if we have the "reagent" required (iron)
					itemInHand.setDurability((short)0);
					//player.getInventory().setItemInHand(new ItemStack(itemInHand.getTypeId(), 1));
					player.sendMessage(ChatColor.GOLD+"You successfully smelted the item.");
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
					player.getInventory().addItem(returnedItem);
				}
				else { 
					// fail
					player.getInventory().clear(player.getInventory().getHeldItemSlot());
					player.sendMessage(ChatColor.GRAY+"You smelted the item but failed to recover any usable material.");
				}
				
				if(canGain) {
					possibleSkillGain(player, pseudoPlayer);
				}
					
			}
			else {
				Output.simpleError(player, "That item cannot be smelted.");
			}
		}
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer) {
		if(pseudoPlayer.isSkillLocked("blacksmithy"))
			return;
		skillGain(player, pseudoPlayer, .35, "blacksmithy", "Blacksmithy");
	}
}

