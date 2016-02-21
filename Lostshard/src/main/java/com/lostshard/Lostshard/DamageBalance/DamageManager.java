package com.lostshard.Lostshard.DamageBalance;

import java.util.EnumSet;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.lostshard.Lostshard.DamageBalance.Armor.ArmorBoots;
import com.lostshard.Lostshard.DamageBalance.Armor.ArmorChestplate;
import com.lostshard.Lostshard.DamageBalance.Armor.ArmorHelmet;
import com.lostshard.Lostshard.DamageBalance.Armor.ArmorLeggings;
import com.lostshard.Lostshard.DamageBalance.Enchantments.Armor.BlastProtection;
import com.lostshard.Lostshard.DamageBalance.Enchantments.Armor.FetherFalling;
import com.lostshard.Lostshard.DamageBalance.Enchantments.Armor.FireProtection;
import com.lostshard.Lostshard.DamageBalance.Enchantments.Armor.ProjectileProtection;
import com.lostshard.Lostshard.DamageBalance.Enchantments.Armor.Protection;

public class DamageManager {
	
	private EnumSet<DamageCause> trueDamage = EnumSet.of(
			DamageCause.FIRE_TICK, DamageCause.SUFFOCATION, DamageCause.DROWNING, DamageCause.STARVATION,
			DamageCause.FALL, DamageCause.VOID, DamageCause.MAGIC, DamageCause.LIGHTNING);
	
	private EnumSet<DamageModifier> disabledModifiers = EnumSet.of(
			DamageModifier.BLOCKING, DamageModifier.MAGIC, DamageModifier.ABSORPTION, 
			DamageModifier.RESISTANCE, DamageModifier.HARD_HAT);
	
	public void handle(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player) event.getEntity();
		
		double enchantmentModifier = getEnchantmentsModifier(player.getInventory(), event.getCause());
		
		enchantmentModifier = Math.min(1, enchantmentModifier);
		
		double enchantmentReduction = event.getDamage(DamageModifier.BASE);
		enchantmentReduction *= enchantmentModifier;
		
		/*
		 * If true damage, remove all damage modification but the base damage.
		 */
		if(trueDamage.contains(event.getCause())) {
			for(DamageModifier m : DamageModifier.values())
				if(!m.equals(DamageModifier.BASE))
					event.setDamage(m, 0);
			
			event.setDamage(DamageModifier.MAGIC, -enchantmentReduction);
			
			return;
		}
		
		/*
		 * Set the damage of all disabled modifiers to 0.
		 */
		for(DamageModifier m : disabledModifiers)
			event.setDamage(m, 0);
		
		double armorModifier = getArmorModifier(player.getInventory());
		
		enchantmentReduction = Math.min(1-armorModifier, enchantmentReduction);
		
		double armorReduction = event.getDamage(DamageModifier.BASE);
		
		armorReduction *= armorModifier;
			
		event.setDamage(DamageModifier.ARMOR, -armorReduction);
		
		event.setDamage(DamageModifier.MAGIC, -enchantmentReduction);
	}
	
	private double getEnchantmentsModifier(PlayerInventory inv, DamageCause cause) {
		int modifier = 0;
		
		if(cause.equals(DamageCause.FALL)) {
			int level = inv.getBoots().getItemMeta().getEnchantLevel(Enchantment.PROTECTION_FALL);
			for(FetherFalling f : FetherFalling.values())
				if(f.getLevel() == level)
					modifier += f.getModifier();
		} else if(cause.equals(DamageCause.ENTITY_EXPLOSION) || cause.equals(DamageCause.BLOCK_EXPLOSION)) {
			for(ItemStack i : inv.getArmorContents()) {
				if(i == null)
					continue;
				int level = inv.getBoots().getItemMeta().getEnchantLevel(Enchantment.PROTECTION_EXPLOSIONS);
				for(BlastProtection e : BlastProtection.values())
					if(e.getLevel() == level)
						modifier += e.getModifier();
			}
		} else if(cause.equals(DamageCause.FIRE) || cause.equals(DamageCause.FIRE_TICK)) {
			for(ItemStack i : inv.getArmorContents()) {
				if(i == null)
					continue;
				int level = inv.getBoots().getItemMeta().getEnchantLevel(Enchantment.PROTECTION_FIRE);
				for(FireProtection f : FireProtection.values())
					if(f.getLevel() == level)
						modifier += f.getModifier();
			}
		} else if(cause.equals(DamageCause.PROJECTILE)) {
			for(ItemStack i : inv.getArmorContents()) {
				if(i == null)
					continue;
				int level = inv.getBoots().getItemMeta().getEnchantLevel(Enchantment.PROTECTION_PROJECTILE);
				for(ProjectileProtection p : ProjectileProtection.values())
					if(p.getLevel() == level)
						modifier += p.getModifier();
			}
		} else {
			for(ItemStack i : inv.getArmorContents()) {
				if(i == null)
					continue;
				int level = inv.getBoots().getItemMeta().getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
				for(Protection p : Protection.values())
					if(p.getLevel() == level)
						modifier += p.getModifier();
			}
		}
		
		return modifier;
	}

	private double getArmorModifier(PlayerInventory inv) {
		ItemStack helmet = inv.getHelmet();
		ItemStack chestplate = inv.getChestplate();
		ItemStack leggings = inv.getLeggings();
		ItemStack boots = inv.getBoots();
		
		double modifier = 0;
		
		
		/*
		 * Handle armor modification, loop trough all armor, 
		 * and find the reduction modifier for it,
		 * and add it to the total reduction.
		 */
		for(ArmorHelmet h : ArmorHelmet.values())
			if(h.getType().equals(helmet.getType())) {
				modifier += h.getModifier();
				break;
			}
		
		for(ArmorChestplate c : ArmorChestplate.values())
			if(c.getType().equals(chestplate.getType())) {
				modifier += c.getModifier();
				break;
			}
		
		for(ArmorLeggings l : ArmorLeggings.values())
			if(l.getType().equals(leggings.getType())) {
				modifier += l.getModifier();
				break;
			}
		
		for(ArmorBoots b : ArmorBoots.values())
			if(b.getType().equals(boots.getType())) {
				modifier += b.getModifier();
				break;
			}
		
		return modifier;	
	}
}