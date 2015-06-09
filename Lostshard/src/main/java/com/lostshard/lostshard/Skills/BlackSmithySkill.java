package com.lostshard.lostshard.Skills;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Capturepoint;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;

public class BlackSmithySkill extends Skill {

	public static void anvilProtect(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				&& event.getClickedBlock().getType() == Material.ANVIL) {
			final Plot plot = ptm.findPlotAt(event.getClickedBlock()
					.getLocation());
			final Player player = event.getPlayer();
			if (plot != null)
				if (!plot.isFriendOrAbove(player)) {
					Output.simpleError(player,
							"You can't use this here, the plot is protected.");
					event.setCancelled(true);
					return;
				}
			final PseudoPlayer pPlayer = pm.getPlayer(player);
			if (!(pPlayer.getCurrentBuild().getBlackSmithy().getLvl() >= 1000)) {
				Output.simpleError(player,
						"You must master the ways of Blacksmithy before using an anvil.");
				event.setCancelled(true);
			}
		}
	}

	public static boolean canRepair(ItemStack item) {
		return canRepair(item.getType());
	}

	public static boolean canRepair(Material item) {
		return
		// Diamond
		item.equals(Material.DIAMOND_AXE)
				|| item.equals(Material.DIAMOND_PICKAXE)
				|| item.equals(Material.DIAMOND_BOOTS)
				|| item.equals(Material.DIAMOND_CHESTPLATE)
				|| item.equals(Material.DIAMOND_HELMET)
				|| item.equals(Material.DIAMOND_HOE)
				|| item.equals(Material.DIAMOND_LEGGINGS)
				|| item.equals(Material.DIAMOND_SPADE)
				|| item.equals(Material.DIAMOND_SWORD)
				// Iron
				|| item.equals(Material.IRON_AXE)
				|| item.equals(Material.IRON_BOOTS)
				|| item.equals(Material.IRON_CHESTPLATE)
				|| item.equals(Material.IRON_HELMET)
				|| item.equals(Material.IRON_HOE)
				|| item.equals(Material.IRON_LEGGINGS)
				|| item.equals(Material.IRON_PICKAXE)
				|| item.equals(Material.IRON_SPADE)
				|| item.equals(Material.IRON_SWORD)
				// Gold
				|| item.equals(Material.GOLD_AXE)
				|| item.equals(Material.GOLD_BOOTS)
				|| item.equals(Material.GOLD_CHESTPLATE)
				|| item.equals(Material.GOLD_HELMET)
				|| item.equals(Material.GOLD_HOE)
				|| item.equals(Material.GOLD_LEGGINGS)
				|| item.equals(Material.GOLD_PICKAXE)
				|| item.equals(Material.GOLD_SPADE)
				|| item.equals(Material.GOLD_SWORD)
				// Stone
				|| item.equals(Material.STONE_AXE)
				|| item.equals(Material.STONE_HOE)
				|| item.equals(Material.STONE_PICKAXE)
				|| item.equals(Material.STONE_SPADE)
				|| item.equals(Material.STONE_SWORD)
				// Wood
				|| item.equals(Material.WOOD_AXE)
				|| item.equals(Material.WOOD_HOE)
				|| item.equals(Material.WOOD_PICKAXE)
				|| item.equals(Material.WOOD_SPADE)
				|| item.equals(Material.WOOD_SWORD)
				// Other stuff, like bow
				|| item.equals(Material.BOW);
	}

	public static void Enchanting(PrepareItemEnchantEvent event) {
		final int[] levels = event.getExpLevelCostsOffered();
		final Player player = (Player) event.getViewers().get(0);
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (pPlayer.getCurrentBuild().getBlackSmithy().getLvl() >= 1000) {

		} else if (pPlayer.getCurrentBuild().getBlackSmithy().getLvl() >= 500)
			levels[2] = 0;
		else if (pPlayer.getCurrentBuild().getBlackSmithy().getLvl() >= 250) {
			levels[1] = 0;
			levels[2] = 0;
		} else {
			levels[0] = 0;
			levels[1] = 0;
			levels[2] = 0;
		}
	}

	public static void enhance(Player player) {
		final ItemStack item = player.getItemInHand();
		if (item == null) {
			Output.simpleError(player, "You can't enhance air.");
			return;
		}
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Skill skill = pPlayer.getCurrentBuild().getBlackSmithy();
		final int lvl = skill.getLvl();
		if (pPlayer.getPvpTicks() > 0) {
			Output.simpleError(player,
					"You can't enhance while in or shortly after combat.");
			return;
		}
		if (!canRepair(item) || item.getType().equals(Material.DIAMOND_HOE)
				|| item.getType().equals(Material.IRON_HOE)
				|| item.getType().equals(Material.GOLD_HOE)
				|| item.getType().equals(Material.STONE_HOE)) {
			Output.simpleError(player, "You can't enhance "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}
		if (lvl < 250) {
			Output.simpleError(player,
					"You are not skilled enough to enhance this tool.");
			return;
		}

		int costAmount = 2;
		Material cost = null;
		if (ItemUtils.isDiamond(item))
			cost = Material.DIAMOND;
		else if (ItemUtils.isGold(item))
			cost = Material.GOLD_INGOT;
		else if (ItemUtils.isIron(item))
			cost = Material.IRON_INGOT;
		else if (ItemUtils.isStone(item))
			cost = Material.COBBLESTONE;
		else if (item.getType().equals(Material.BOW))
			cost = Material.DIAMOND;

		if (cost == null) {
			Output.simpleError(player, "You can't enhance "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}

		boolean isTool = false;
		boolean isSword = false;
		boolean isBow = false;
		boolean isArmor = false;
		if (ItemUtils.isArmor(item))
			isArmor = true;
		else if (ItemUtils.isSword(item) || ItemUtils.isAxe(item))
			isSword = true;
		else if (ItemUtils.isTool(item))
			isTool = true;
		else if (item.getType().equals(Material.BOW))
			isBow = true;
		else {
			Output.simpleError(player, "You can't enhance "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}

		if (isTool) {
			costAmount = item.getItemMeta().getEnchantLevel(
					Enchantment.DIG_SPEED) + 2;
			if (costAmount > 6) {
				Output.simpleError(player,
						"You can't enhance this tool above lvl 5.");
				return;
			}
			final Plot plot = ptm.findPlotAt(player.getLocation());
			if (costAmount > 5 && plot == null || costAmount > 5
					&& !plot.getName().equals(Capturepoint.HOST.getPlotName())) {
				Output.simpleError(player, "Only the blacksmith at "
						+ Capturepoint.HOST.getPlotName()
						+ " can enhance your tool to lvl 5.");
				return;
			}
			if (!player.getInventory().contains(cost, costAmount)) {
				Output.simpleError(player, "You do not have enough "
						+ StringUtils.lowerCase(cost.name()).replace("_", " ")
						+ " to enhance that tool, requires " + costAmount + ".");
				return;
			}
			pPlayer.setStamina(pPlayer.getStamina() - ENHANCE_STAMINA_COST);
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			if (costAmount > 4) {
				final ItemMeta meta = item.getItemMeta();
				meta.addEnchant(Enchantment.DIG_SPEED, costAmount - 1, true);
				item.setItemMeta(meta);
			} else {
				final ItemMeta meta = item.getItemMeta();
				meta.addEnchant(Enchantment.DIG_SPEED, costAmount - 1, true);
				meta.addEnchant(Enchantment.DURABILITY, costAmount - 1, true);
				item.setItemMeta(meta);
			}
			Output.positiveMessage(player, "You have enhanced "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ " to lvl " + (costAmount - 1));
		} else if (isSword) {
			costAmount = item.getItemMeta().getEnchantLevel(
					Enchantment.DAMAGE_ALL) + 2;
			if (costAmount > 6) {
				Output.simpleError(player,
						"You can't sharpen this weapon above lvl 5.");
				return;
			}
			final Plot plot = ptm.findPlotAt(player.getLocation());
			if (costAmount > 5 && plot == null || costAmount > 5
					&& !plot.getName().equals(Capturepoint.HOST.getPlotName())) {
				Output.simpleError(player, "Only the blacksmith at "
						+ Capturepoint.HOST.getPlotName()
						+ " can sharpen your weapon to lvl 5.");
				return;
			}
			if (!player.getInventory().contains(cost, costAmount)) {
				Output.simpleError(player, "You do not have enough "
						+ StringUtils.lowerCase(cost.name()).replace("_", " ")
						+ " to sharpen that weapon, requires " + costAmount
						+ ".");
				return;
			}
			pPlayer.setStamina(pPlayer.getStamina() - ENHANCE_STAMINA_COST);
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			final ItemMeta meta = item.getItemMeta();
			meta.addEnchant(Enchantment.DAMAGE_ALL, costAmount - 1, true);
			item.setItemMeta(meta);
			Output.positiveMessage(player, "You have sharpend "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ " to lvl " + (costAmount - 1));
		} else if (isBow) {
			costAmount = item.getItemMeta().getEnchantLevel(
					Enchantment.ARROW_DAMAGE) + 2;
			if (costAmount > 6) {
				Output.simpleError(player,
						"You can't power this weapon above lvl 5.");
				return;
			}
			final Plot plot = ptm.findPlotAt(player.getLocation());
			if (costAmount > 5 && plot == null || costAmount > 5
					&& !plot.getName().equals(Capturepoint.HOST.getPlotName())) {
				Output.simpleError(player, "Only the blacksmith at "
						+ Capturepoint.HOST.getPlotName()
						+ " can power your weapon to lvl 5.");
				return;
			}
			if (!player.getInventory().contains(cost, costAmount)) {
				Output.simpleError(player, "You do not have enough "
						+ StringUtils.lowerCase(cost.name()).replace("_", " ")
						+ " to power that weapon, requires " + costAmount + ".");
				return;
			}
			pPlayer.setStamina(pPlayer.getStamina() - ENHANCE_STAMINA_COST);
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			final ItemMeta meta = item.getItemMeta();
			meta.addEnchant(Enchantment.ARROW_DAMAGE, costAmount - 1, true);
			item.setItemMeta(meta);
			Output.positiveMessage(player, "You have powerd "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ " to lvl " + (costAmount - 1));
		} else if (isArmor) {
			costAmount = item.getItemMeta().getEnchantLevel(
					Enchantment.PROTECTION_ENVIRONMENTAL) + 2;
			if (costAmount > 3) {
				Output.simpleError(player,
						"You can't reinforce this pice of armor above lvl 2.");
				return;
			}
			if (!player.getInventory().contains(cost, costAmount)) {
				Output.simpleError(player, "You do not have enough "
						+ StringUtils.lowerCase(cost.name()).replace("_", " ")
						+ " to reinforce that pice of armor, requires "
						+ costAmount + ".");
				return;
			}
			pPlayer.setStamina(pPlayer.getStamina() - ENHANCE_STAMINA_COST);
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			final ItemMeta meta = item.getItemMeta();
			meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL,
					costAmount - 1, true);
			item.setItemMeta(meta);
			Output.positiveMessage(player, "You have reinforced "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ " to lvl " + (costAmount - 1));
		} else {
			Output.simpleError(player, "You can't reinforce "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}
	}

	public static void repair(Player player) {
		final ItemStack item = player.getItemInHand();
		if (item == null || item.getType().equals(Material.AIR)) {
			Output.simpleError(player, "You can't repair air.");
			return;
		}
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (pPlayer.getPvpTicks() > 0) {
			Output.simpleError(player,
					"You can't repair while in or shortly after combat.");
			return;
		}
		if (pPlayer.getStamina() < REPAIR_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Repair requires "
					+ REPAIR_STAMINA_COST + ".");
			return;
		}
		final Skill skill = pPlayer.getCurrentBuild().getBlackSmithy();
		final int lvl = skill.getLvl();
		if (!canRepair(item)) {
			Output.simpleError(player, "You can't repair "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}
		Material cost = null;
		boolean cangain = false;
		final int costAmount = 1;
		if (ItemUtils.isDiamond(item)) {
			cost = Material.DIAMOND;
			cangain = true;
		} else if (ItemUtils.isGold(item)) {
			cost = Material.GOLD_INGOT;
			cangain = true;
		} else if (ItemUtils.isIron(item)) {
			cost = Material.IRON_INGOT;
			if (lvl < 750)
				cangain = true;
		} else if (ItemUtils.isStone(item)) {
			cost = Material.COBBLESTONE;
			if (lvl < 500)
				cangain = true;
		} else if (ItemUtils.isWood(item)) {
			cost = Material.WOOD;
			if (lvl < 250)
				cangain = true;
		}

		if (cost == null) {
			Output.simpleError(player, "You can't repair "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}

		if (!player.getInventory().contains(cost, costAmount)) {
			Output.simpleError(player, "You do not have enough "
					+ StringUtils.lowerCase(cost.name()).replace("_", " ")
					+ " to repair that tool, requires " + costAmount + ".");
			return;
		}

		final double dblSkillVal = (double) skill.getLvl() / 1000;
		final double rand = Math.random();
		if (dblSkillVal >= rand) {
			item.setDurability((short) 0);
			pPlayer.setStamina(pPlayer.getStamina() - REPAIR_STAMINA_COST);
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			Output.positiveMessage(player, "You repair the item.");
		} else {
			item.setDurability((short) (item.getDurability()
					+ item.getDurability() * .5 + 2));
			if (item.getDurability() < item.getDurability())
				player.sendMessage(ChatColor.GRAY + "You failed to repair the "
						+ item.getType().name().toLowerCase().replace("_", " ")
						+ ", it was damaged in the process.");
			else {
				player.getInventory().clear(
						player.getInventory().getHeldItemSlot());
				player.sendMessage(ChatColor.GRAY + "You failed to repair the "
						+ item.getType().name().toLowerCase().replace("_", " ")
						+ ", it was destroyed in the process.");
			}
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
		}
		if (cangain) {
			final int gain = skill.skillGain(pPlayer);
			Output.gainSkill(player, "Blacksmithy", gain, skill.getLvl());
			if (gain > 0)
				pPlayer.update();
		}
	}

	public static void smelt(Player player) {
		final ItemStack item = player.getItemInHand();
		if (item == null) {
			Output.simpleError(player, "You can't smelt air.");
			return;
		}
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Skill skill = pPlayer.getCurrentBuild().getBlackSmithy();
		final int lvl = skill.getLvl();
		if (pPlayer.getPvpTicks() > 0) {
			Output.simpleError(player,
					"You can't smelt while in or shortly after combat.");
			return;
		}
		if (!canRepair(item)) {
			Output.simpleError(player, "You can't smelt "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}
		int amount = 1;
		Material cost = null;
		boolean cansmelt = false;
		if (ItemUtils.isDiamond(item)) {
			cost = Material.DIAMOND;
			if (lvl >= 750)
				cansmelt = true;
		} else if (ItemUtils.isGold(item)) {
			cost = Material.GOLD_INGOT;
			if (lvl >= 500)
				cansmelt = true;
		} else if (ItemUtils.isIron(item)) {
			cost = Material.IRON_INGOT;
			if (lvl >= 250)
				cansmelt = true;
		}

		if (cost == null) {
			Output.simpleError(player, "You can't smelt "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ ".");
			return;
		}
		if (ItemUtils.isArmor(cost))
			amount = 3;
		player.getInventory().setItemInHand(null);
		pPlayer.setStamina(pPlayer.getStamina() - SMELT_STAMINA_COST);
		if (cansmelt) {
			player.getWorld().dropItem(player.getLocation(),
					new ItemStack(cost, amount));
			Output.positiveMessage(player, "You have smeltet your "
					+ item.getType().name().toLowerCase().replace("_", " ")
					+ " into " + cost.name().toLowerCase() + ".");
		} else
			Output.simpleError(
					player,
					"You have smeltet your "
							+ item.getType().name().toLowerCase()
									.replace("_", " ")
							+ " but failed to recover any use full resources from the smelting.");
	}

	private static final int REPAIR_STAMINA_COST = 10;

	private static final int SMELT_STAMINA_COST = 15;

	private static final int ENHANCE_STAMINA_COST = 25;

	public BlackSmithySkill() {
		super();
		this.setName("Blacksmithy");
		this.setBaseProb(.2);
		this.setScaleConstant(30);
		this.setMaxGain(15);
		this.setMinGain(5);
		this.setMat(Material.ANVIL);
	}

	@Override
	public String howToGain() {
		return "You can gain blacksmity by repairing armor and tools";
	}
}
