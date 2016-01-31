package com.lostshard.Lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Spells.Structures.FireWalk;

public class SPL_FireWalk extends Spell {

	public SPL_FireWalk(Scroll scroll) {
		super(scroll);
	}

	@Override
	public void doAction(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 320, 1));
		player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 320, 1));
		final FireWalk fireWalk = new FireWalk(new ArrayList<Block>(), player.getUniqueId(), 150);
		final Block fireBlock = player.getLocation().getBlock();
		if (fireBlock.getType().equals(Material.AIR)) {
			fireBlock.setType(Material.FIRE);
			fireWalk.addBlock(fireBlock);
		}
	}

	@Override
	public void preAction(Player player) {

	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

}
