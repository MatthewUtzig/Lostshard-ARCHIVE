package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Spells.RangedSpell;

public class SPL_Light extends RangedSpell {
	
	public SPL_Light() {
		setName("Light");
		setSpellWords("Lightus Flingicus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(10);
		addReagentCost(new ItemStack(Material.SUGAR_CANE));
		setMinMagery(120);
		setRange(10);
		setPage(2);
		setCarePlot(true);
	}

	@Override
	public void preAction(Player player) {
	}

	@Override
	public void doAction(Player player) {
		getFoundBlock().setType(Material.TORCH);
	}

}
