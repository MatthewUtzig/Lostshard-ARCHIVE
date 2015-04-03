package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PVPHandler;
import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Lightning extends RangedSpell {

	public SPL_Lightning(Scroll scroll) {
		super(scroll);
		setCarePlot(false);
		setRange(20);
	}

	@Override
	public void preAction(Player player) {
		
	}

	@Override
	public void doAction(Player player) {
		Location strikeLoc = getFoundBlock().getLocation();
		strikeLoc.setX(strikeLoc.getX()+.5);
		strikeLoc.setZ(strikeLoc.getZ()+.5);
		getFoundBlock().getWorld().strikeLightning(strikeLoc);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(Utils.isWithin(p.getLocation(), strikeLoc, 5)) {
				//Casting lightning at someone?
				if(PVPHandler.canEntityAttackEntity(player, p)) {
					PVPHandler.criminalAction(p, player);
				}				
			}
		}
	}

}
