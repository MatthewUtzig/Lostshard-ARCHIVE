package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.lostshard.lostshard.Spells.Spell;

public class SPL_ArrowBlast extends Spell {

	public SPL_ArrowBlast() {
		setName("Arrow Blast");
		setSpellWords("Blastius Projectilus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(20);
		addReagentCost(new ItemStack(Material.ARROW));
		addReagentCost(new ItemStack(Material.SULPHUR));
		setPage(3);
		setMinMagery(240);
	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

	@Override
	public void preAction(Player player) {

	}

	@Override
	public void doAction(Player player) {
		player.getWorld().createExplosion(player.getLocation().add(0, 1, 0), 0);
		for(int i = 0 ; i < 36 ; i++) {
			 
		    int spread = 0;
		 
		    spread = i*10;
		 
		    double pitch = 90 * Math.PI / 180;
		    double yaw  = ((player.getLocation().getYaw() + 90 + spread) * Math.PI) / 180;
		 
		    double z_axis = Math.sin(pitch);
		 
		    double x = z_axis * Math.cos(yaw);
		    double y = z_axis * Math.sin(yaw);
		    double z = Math.cos(pitch);
		 
		    Vector vector = new Vector(x, z, y);
		    Arrow arrow = player.getWorld().spawnArrow(player.getLocation().add(0, 1, 0), vector, (float) 0.6, (float) 0);
		    arrow.setShooter(player);
		    arrow.setVelocity(vector.multiply(2.5));
		}
	}
}
