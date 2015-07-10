package com.lostshard.Lostshard.Spells.Spells;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;

public class SPL_ArrowBlast extends Spell {

	public SPL_ArrowBlast(Scroll scroll) {
		super(scroll);
	}

	@Override
	public void doAction(Player player) {
		player.getWorld().createExplosion(player.getLocation().add(0, 1, 0), 0);
		for (int i = 0; i < 36; i++) {

			int spread = 0;

			spread = i * 10;

			final double pitch = 90 * Math.PI / 180;
			final double yaw = (player.getLocation().getYaw() + 90 + spread)
					* Math.PI / 180;

			final double z_axis = Math.sin(pitch);

			final double x = z_axis * Math.cos(yaw);
			final double y = z_axis * Math.sin(yaw);
			final double z = Math.cos(pitch);

			final Vector vector = new Vector(x, z, y);
			final Arrow arrow = player.getWorld().spawnArrow(
					player.getLocation().add(0, 1, 0), vector, (float) 0.6, 0);
			arrow.setShooter(player);
			arrow.setVelocity(vector.multiply(2.5));
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
