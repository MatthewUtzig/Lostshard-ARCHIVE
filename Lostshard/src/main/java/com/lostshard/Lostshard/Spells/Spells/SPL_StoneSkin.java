package com.lostshard.Lostshard.Spells.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;

public class SPL_StoneSkin extends Spell {

	public SPL_StoneSkin(Scroll scroll) {
		super(scroll);
	}

	@Override
	public void doAction(Player player) {
		player.sendMessage(ChatColor.GRAY + "Your skin turns to rock");
		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, -3));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 3));
		player.addPotionEffect(new PotionEffect(
				PotionEffectType.DAMAGE_RESISTANCE, 300, 2));

	}

	@Override
	public void preAction(Player player) {

	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

}
