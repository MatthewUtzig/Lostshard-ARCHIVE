package com.lostshard.lostshard.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;

public class Spell {

	private String name;
	private String spellWords;
	private int manaCost;
	private ItemStack[] itemCost;
	private int circle;
	private int castingDelay;
	private boolean wand;

	private String[] description = { ChatColor.LIGHT_PURPLE + "No description" };

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpellWords() {
		return spellWords;
	}

	public void setSpellWords(String spellWords) {
		this.spellWords = spellWords;
	}

	public int getManaCost() {
		return manaCost;
	}

	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}

	public ItemStack[] getItemCost() {
		return itemCost;
	}

	public void setItemCost(ItemStack[] itemCost) {
		this.itemCost = itemCost;
	}

	public int getCircle() {
		return circle;
	}

	public void setCircle(int circle) {
		this.circle = circle;
	}

	public int getCastingDelay() {
		return castingDelay;
	}

	public void setCastingDelay(int castingDelay) {
		this.castingDelay = castingDelay;
	}

	public boolean isWand() {
		return wand;
	}

	public void setWand(boolean wand) {
		this.wand = wand;
	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	public boolean verifyCastable(Player player, PseudoPlayer pPlayer) {
		return true;
	}

	public void preAction(Player player) {
		System.out.println("Attempting to preAction Spell");
	}

	public void doAction(Player player) {
		System.out.println("Attempting to doAction Spell");
	}
}
