package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Spells.Spell;

public class SPL_HealSelf extends Spell {

	public SPL_HealSelf() {
		setName("Heal Self");
		setSpellWords("Selfishius Healicus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(35);
		addReagentCost(new ItemStack(Material.STRING));
		addReagentCost(new ItemStack(Material.REDSTONE));
		setMinMagery(600);
		setPage(6);
	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

	@Override
	public void preAction(Player player) {
		
	}

	@Override
	public void doAction(Player player) {
		double health = player.getHealth();
		health+=10;
		if(health > 20)
			health = 20;
		player.setHealth(health);
	}

}
