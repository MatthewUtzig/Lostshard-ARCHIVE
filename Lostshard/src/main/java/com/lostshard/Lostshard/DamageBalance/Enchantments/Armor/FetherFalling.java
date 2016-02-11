package com.lostshard.Lostshard.DamageBalance.Enchantments.Armor;

import org.bukkit.enchantments.Enchantment;

public enum FetherFalling {

	LEVEL1(Enchantment.DAMAGE_ALL, 1, .10/4),
	LEVEL2(Enchantment.DAMAGE_ALL, 2, .30/4),
	LEVEL3(Enchantment.DAMAGE_ALL, 3, .50/4),
	LEVEL4(Enchantment.DAMAGE_ALL, 4, .80/4),
	;
	
	private final Enchantment enchantment;
	private final int level;
	private final double modifier;
	
	private FetherFalling(Enchantment enchantment, int level, double modifier) {
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
