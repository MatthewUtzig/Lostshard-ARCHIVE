package com.lostshard.lostshard.Handlers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.FoodType;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;

public class foodHealHandler {

	static PlayerManager pm = PlayerManager.getManager();
	
	public static void foodHeal(PlayerInteractEvent event) {
		if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
			return;
		final Block blockClicked = event.getClickedBlock();
		final Player player = event.getPlayer();
		if (blockClicked != null && (blockClicked.getType().equals(Material.CHEST)
				|| blockClicked.getType().equals(Material.FURNACE)
				|| blockClicked.getType().equals(Material.STONE_BUTTON) || blockClicked.getType().equals(Material.LEVER)
				|| blockClicked.getType().equals(Material.WOOD_DOOR)
				|| blockClicked.getType().equals(Material.TRAP_DOOR)))
			return;
		final ItemStack itemInHand = player.getItemInHand();
		final FoodType foodType = FoodType.getFoodTypeByMaterial(player.getItemInHand().getType());
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (foodType == null)
			return;
		final int stamCost = foodType.getStaminaCost();
		final int curStam = pPlayer.getStamina();
		if (curStam < stamCost) {
			Output.simpleError(player, "You do not have enough stamina to eat that, it requires " + stamCost + ".");
			return;
		}
		pPlayer.setStamina(curStam - stamCost);
		final Damageable damag = player;
		double curHealth = damag.getHealth();
		curHealth += foodType.getHealAmount();

		if (foodType.getItem() == Material.PORK) {
			final int curSkill = pPlayer.getCurrentBuild().getLumberjacking().getLvl();
			if (curSkill > 500)
				curHealth += 5;
		} else if (foodType.getItem() == Material.COOKED_FISH) {
			final int curSkill = pPlayer.getCurrentBuild().getFishing().getLvl();
			if (curSkill > 750)
				curHealth += 2;
		}
		if (curHealth > 20)
			curHealth = 20;
		player.setHealth(curHealth);

		if (foodType.getItem() == Material.ROTTEN_FLESH)
			player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 1));

		int curFullness = player.getFoodLevel();
		double modFoodLevel = foodType.getFullnessAmount();
		final int survSkill = pPlayer.getCurrentBuild().getSurvivalism().getLvl();
		final double multiplier = 1 + (double) survSkill / 1000;
		modFoodLevel *= multiplier;

		curFullness += (int) modFoodLevel;
		if (curFullness > 20)
			curFullness = 20;
		player.setFoodLevel(curFullness);

		float curSaturation = player.getSaturation();
		curSaturation += foodType.getSaturation();
		if (curSaturation > 20)
			curSaturation = 20;

		player.setSaturation(curSaturation);

		if (itemInHand.getAmount() > 1)
			itemInHand.setAmount(itemInHand.getAmount() - 1);
		else if (itemInHand.getType().equals(Material.MUSHROOM_SOUP))
			itemInHand.setType(Material.BOWL);
		else
			player.getInventory().clear(player.getInventory().getHeldItemSlot());
		event.setCancelled(true);
	}
}
