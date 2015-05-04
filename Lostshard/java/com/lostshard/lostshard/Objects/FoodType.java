package com.lostshard.lostshard.Objects;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public enum FoodType {
	// Apple(260, 4, 10, 4, 2.4f),
	MushroomSoup(Material.MUSHROOM_SOUP, 10, 15, 8, 9.6f),
	// Bread(297, 5, 13, 5, 6f),
	/*
	 * RawPorkchop(319, 3, 20, 3, 1.8f), CookedPorkchop(320, 8, 20, 8, 12.8f),
	 */
	GoldenApple(Material.GOLDEN_APPLE, 20, 100, 10, 24f), Cookie(
			Material.COOKIE, 2, 5, 1, .2f),
	/*
	 * CookedFish(350, 5, 13, 5, 6f), RawBeef(363, 3, 20, 3, 1.8f),
	 * CookedBeef(364, 8, 20, 8, 12.8f), RawChicken(365, 3, 13, 3, 1.2f),
	 * CookedChicken(366, 5, 13, 5, 7.2f), RottenFlesh(367, 5, 13, 5, .8f),
	 */
	Melon(Material.MELON, 4, 10, 4, 3f), CookedFish(Material.COOKED_FISH, 3,
			10, 5, 6f);
	// RawFish(349, 2, 5, 2, 1.2f);

	public static FoodType getFoodTypeByMaterial(Material foodType) {
		if (lookupByMaterial.containsKey(foodType))
			return lookupByMaterial.get(foodType);
		return null;
	}

	private static final Map<Material, FoodType> lookupByMaterial = new HashMap<Material, FoodType>();

	static {
		for (final FoodType f : EnumSet.allOf(FoodType.class))
			lookupByMaterial.put(f.getItem(), f);
	}
	private final Material item;
	private final int healAmount;
	private final int staminaCost;
	private final int fullnessAmount;

	private final float saturation;

	private FoodType(Material item, int healAmount, int staminaCost,
			int fullnessAmount, float saturation) {
		this.item = item;
		this.healAmount = healAmount;
		this.staminaCost = staminaCost;
		this.fullnessAmount = fullnessAmount;
		this.saturation = saturation;
	}

	public int getFullnessAmount() {
		return this.fullnessAmount;
	}

	public int getHealAmount() {
		return this.healAmount;
	}

	public Material getItem() {
		return this.item;
	}

	public float getSaturation() {
		return this.saturation;
	}

	public int getStaminaCost() {
		return this.staminaCost;
	}
}
