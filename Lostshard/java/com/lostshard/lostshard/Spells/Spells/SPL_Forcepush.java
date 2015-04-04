package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lostshard.lostshard.Handlers.PVPHandler;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Forcepush extends Spell {

	public SPL_Forcepush(Scroll scroll) {
		super(scroll);
	}
	
	private ArrayList<LivingEntity> entitiesFound;
	
	@Override
	public boolean verifyCastable(Player player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		entitiesFound = SpellUtils.enemiesInLOS(player, pPlayer, 10);
		if(entitiesFound.size() <= 0) {
			Output.simpleError(player, "No enemy target found.");
			return false;
		}
		return true;
	}

	@Override
	public void preAction(Player player) {
	}

	@Override
	public void doAction(Player player) {
		for(LivingEntity lE : entitiesFound) {
			if(lE != null) {
				if(lE instanceof Player) {
					Player p = (Player)lE;
					PVPHandler.criminalAction(p, player);
				}
				Location aLoc = player.getLocation();
				Location dLoc = lE.getLocation();
				Vector v = new Vector();
				v.setX(dLoc.getX() - aLoc.getX());
				v.setY(dLoc.getY() - aLoc.getY());
				v.setZ(dLoc.getZ() - aLoc.getZ());
				v = v.normalize();
				v = v.multiply(5);
				if(v.getY() > .75)
					v.setY(.75);
				
				lE.setVelocity(v);
			}
		}

	}

}
