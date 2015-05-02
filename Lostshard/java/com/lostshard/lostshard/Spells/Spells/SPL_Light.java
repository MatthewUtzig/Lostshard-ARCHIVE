package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;

public class SPL_Light extends RangedSpell {

	public SPL_Light(Scroll scroll) {
		super(scroll);
		this.setRange(10);
		this.setCarePlot(true);
	}

	@Override
	public void doAction(Player player) {
		this.getFoundBlock().setType(Material.TORCH);
	}

	@Override
	public void preAction(Player player) {
	}

}
