package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_DetectHidden extends Spell {

	public SPL_DetectHidden(Scroll scroll) {
		super(scroll);
	}

	@Override
	public void doAction(Player player) {
		for (final Player p : Bukkit.getOnlinePlayers()) {
			if (p == player)
				continue;
			if (Utils.isWithin(player.getLocation(), p.getLocation(), 10))
				if (p.hasPotionEffect(PotionEffectType.INVISIBILITY))
					p.removePotionEffect(PotionEffectType.INVISIBILITY);
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
