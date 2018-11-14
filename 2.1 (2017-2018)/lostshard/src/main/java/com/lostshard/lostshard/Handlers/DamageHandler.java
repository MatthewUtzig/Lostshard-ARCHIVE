package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.Recorders.DamageRecord;

public class DamageHandler {

	public static double base = .9d;

	public static double armor = .7d;

	public static double magic = 1d;
	public static double resistance = 1d;
	public static double hardhat = 1d;

	public static double arrow = 1d;
	public static double hand = 1d;
	public static double swords = 1d;
	public static double diamondSword = 1d;
	public static double ironSword = 1d;
	public static double goldSword = 2d;
	public static double stoneSword = 1d;
	public static double woodSword = 1d;
	
	public static double axes = 1d;
	public static double diamondAxe = 1d;
	public static double ironAxe = 1d;
	public static double goldAxe = 2d;
	public static double stoneAxe = 1d;
	public static double woodAxe = 1d;

	public static List<UUID> players = new ArrayList<UUID>();
	
	public static void damage(EntityDamageByEntityEvent event) {

		double weapon = 1d;

		final Entity attacker = event.getDamager();

		if (attacker instanceof Arrow) {
			if (((Arrow) attacker).getShooter() instanceof Player)
				weapon = arrow;
		} else if (attacker instanceof Player) {
			final Player aPlayer = (Player) attacker;
			final Material wep = aPlayer.getItemInHand().getType();
			if (wep == Material.AIR)
				weapon = hand;
			else if (wep == Material.DIAMOND_SWORD)
				weapon = diamondSword * swords;
			else if (wep == Material.IRON_SWORD)
				weapon = ironSword * swords;
			else if (wep == Material.GOLD_SWORD) {
				weapon = goldSword * swords;
				event.getEntity().getLocation().getWorld().strikeLightningEffect(event.getEntity().getLocation());
			} else if (wep == Material.STONE_SWORD)
				weapon = stoneSword * swords;
			else if (wep == Material.WOOD_SWORD)
				weapon = woodSword * swords;
			else if (wep == Material.DIAMOND_AXE)
				weapon = diamondAxe * axes;
			else if (wep == Material.IRON_AXE)
				weapon = ironAxe * axes;
			else if (wep == Material.GOLD_AXE)
				weapon = goldAxe * axes;
			else if (wep == Material.STONE_AXE)
				weapon = stoneAxe * axes;
			else if (wep == Material.WOOD_AXE)
				weapon = woodAxe * axes;
		}

		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if (players.contains(player.getUniqueId())) {
				player.sendMessage("Base: " + event.getDamage(DamageModifier.BASE));
				player.sendMessage("Armor: " + event.getDamage(DamageModifier.ARMOR));
				player.sendMessage("Magic: " + event.getDamage(DamageModifier.MAGIC));
				player.sendMessage("Resistance: " + event.getDamage(DamageModifier.RESISTANCE));
			}
			
			
			if(attacker instanceof Player || (attacker instanceof Arrow && ((Arrow) attacker).getShooter() instanceof Player)) {
				ItemStack weaponInHand;
				if(attacker instanceof Player)
					weaponInHand = ((Player) attacker).getItemInHand();
				else {
					Player playerAttacker = ((Player) ((Arrow) attacker).getShooter());
					weaponInHand = playerAttacker.getInventory().getItem(playerAttacker.getInventory().first(Material.BOW));
				}
				new DamageRecord(event.getDamage(DamageModifier.BASE), event.getDamage(DamageModifier.ARMOR), event.getDamage(DamageModifier.MAGIC), 
						event.getDamage(DamageModifier.RESISTANCE), event.getFinalDamage(), weaponInHand , player.getInventory());
			}
			if (event.isApplicable(DamageModifier.BASE))
				event.setDamage(DamageModifier.BASE, event.getDamage(DamageModifier.BASE) * base * weapon);
			if (event.isApplicable(DamageModifier.ARMOR))
				event.setDamage(DamageModifier.ARMOR, event.getDamage(DamageModifier.ARMOR) * armor);
			if (event.isApplicable(DamageModifier.MAGIC))
				event.setDamage(DamageModifier.MAGIC, event.getDamage(DamageModifier.MAGIC) * magic);
			if (event.isApplicable(DamageModifier.RESISTANCE))
				event.setDamage(DamageModifier.RESISTANCE, event.getDamage(DamageModifier.RESISTANCE) * resistance);
			if (event.isApplicable(DamageModifier.HARD_HAT))
				event.setDamage(DamageModifier.HARD_HAT, event.getDamage(DamageModifier.HARD_HAT) * hardhat);
			
			if (players.contains(player.getUniqueId())) {
				player.sendMessage("--------------------------");
				player.sendMessage("Base: " + event.getDamage(DamageModifier.BASE));
				player.sendMessage("Armor: " + event.getDamage(DamageModifier.ARMOR));
				player.sendMessage("Magic: " + event.getDamage(DamageModifier.MAGIC));
				player.sendMessage("Resistance: " + event.getDamage(DamageModifier.RESISTANCE));
				player.sendMessage("Weapon multiplier: " + weapon);
				player.sendMessage("Final damage: " + event.getFinalDamage());
			}
		}
	}

	public static void goldArmor(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			final ItemStack helmet = player.getInventory().getHelmet();
			final ItemStack chest = player.getInventory().getChestplate();
			final ItemStack legs = player.getInventory().getLeggings();
			final ItemStack boots = player.getInventory().getBoots();
			if (event.getCause().equals(DamageCause.DROWNING)) {
				if (helmet != null && helmet.getType().equals(Material.GOLD_HELMET))
					event.setCancelled(true);
			} else if (event.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				if (chest != null && chest.getType().equals(Material.GOLD_CHESTPLATE))
					event.setCancelled(true);
			} else if (event.getCause().equals(DamageCause.LAVA) || event.getCause().equals(DamageCause.FIRE)
					|| event.getCause().equals(DamageCause.FIRE_TICK)) {
				if (legs != null && legs.getType().equals(Material.GOLD_LEGGINGS))
					event.setCancelled(true);
			} else if (event.getCause().equals(DamageCause.FALL))
				if (boots != null && boots.getType().equals(Material.GOLD_BOOTS))
					event.setCancelled(true);
		}
	}
}
