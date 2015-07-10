package com.lostshard.Lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lostshard.Lostshard.Handlers.PVPHandler;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;

public class SPL_Forcepush extends Spell {

	private ArrayList<LivingEntity> entitiesFound;

	public SPL_Forcepush(Scroll scroll) {
		super(scroll);
	}

	@Override
	public void doAction(Player player) {
		for (final LivingEntity lE : this.entitiesFound)
			if (lE != null) {
				if (lE instanceof Player) {
					final Player p = (Player) lE;
					PVPHandler.criminalAction(p, player);
				}
				final Location aLoc = player.getLocation();
				final Location dLoc = lE.getLocation();
				Vector v = new Vector();
				v.setX(dLoc.getX() - aLoc.getX());
				v.setY(dLoc.getY() - aLoc.getY());
				v.setZ(dLoc.getZ() - aLoc.getZ());
				v = v.normalize();
				v = v.multiply(5);
				if (v.getY() > .75)
					v.setY(.75);

				lE.setVelocity(v);
			}

	}

	@Override
	public void preAction(Player player) {
	}

	@Override
	public boolean verifyCastable(Player player) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		this.entitiesFound = SpellUtils.enemiesInLOS(player, pPlayer, 10);
		if (this.entitiesFound.size() <= 0) {
			Output.simpleError(player, "No enemy target found.");
			return false;
		}
		return true;
	}

}
