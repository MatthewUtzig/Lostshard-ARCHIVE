package com.lostshard.lostshard.Spells.Spells;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;

public class SPL_CreateFood extends RangedSpell {

	public SPL_CreateFood(Scroll scroll) {
		super(scroll);
		this.setRange(10);
	}

	@Override
	public void doAction(Player player) {
		final Random rand = new Random();
		Material mat;
		switch (rand.nextInt(4)) {
		case 0:
			mat = Material.APPLE;
			break;
		case 1:
			mat = Material.PORK;
			break;
		case 2:
			mat = Material.WHEAT;
			break;
		case 3:
			mat = Material.RAW_FISH;
			break;
		case 4:
			mat = Material.RED_MUSHROOM;
			break;
		default:
			mat = Material.BROWN_MUSHROOM;
			break;
		}
		player.getWorld().dropItem(this.getFoundBlock().getLocation(), new ItemStack(mat, 1));

	}

	@Override
	public void preAction(Player player) {

	}

}
