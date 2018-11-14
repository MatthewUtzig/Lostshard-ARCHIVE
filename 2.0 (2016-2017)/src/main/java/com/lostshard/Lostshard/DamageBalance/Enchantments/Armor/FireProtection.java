package com.lostshard.Lostshard.DamageBalance.Enchantments.Armor;

import org.bukkit.enchantments.Enchantment;

public enum FireProtection {

	LEVEL1(Enchantment.DAMAGE_ALL, 1, 0),
	LEVEL2(Enchantment.DAMAGE_ALL, 2, 0),
	LEVEL3(Enchantment.DAMAGE_ALL, 3, 0),
	LEVEL4(Enchantment.DAMAGE_ALL, 4, 0),
	;
	
	private final Enchantment enchantment;
	private final int level;
	private final double modifier;
	
	private FireProtection(Enchantment enchantment, int level, double modifier) {
		this.enchantment = enchantment;
		this.level = level;
		this.modifier = modifier;
	}

	/**
	 * @return the enchantment
	 */
	public Enchantment getEnchantment() {
		return enchantment;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the modifier
	 */
	public double getModifier() {
		return modifier;
	}
	
}
