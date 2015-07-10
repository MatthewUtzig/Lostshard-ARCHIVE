package com.lostshard.Lostshard.Spells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Handlers.PVPHandler;
import com.lostshard.Lostshard.Spells.RangedSpell;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Utils;

public class SPL_Lightning extends RangedSpell {

	public SPL_Lightning(Scroll scroll) {
		super(scroll);
		this.setCarePlot(false);
		this.setRange(20);
	}

	@Override
	public void doAction(Player player) {
		final Location strikeLoc = this.getFoundBlock().getLocation();
		strikeLoc.setX(strikeLoc.getX() + .5);
		strikeLoc.setZ(strikeLoc.getZ() + .5);
		this.getFoundBlock().getWorld().strikeLightning(strikeLoc);

		for (final Player p : Bukkit.getOnlinePlayers())
			if (Utils.isWithin(p.getLocation(), strikeLoc, 5))
				// Casting lightning at someone?
				if (PVPHandler.canEntityAttackEntity(player, p))
					PVPHandler.criminalAction(p, player);
	}

	@Override
	public void preAction(Player player) {

	}

}
