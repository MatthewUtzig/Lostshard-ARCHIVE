package com.lostshard.lostshard.Spells.Spells;

import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;

public class SPL_SummonMonster extends RangedSpell {

	public SPL_SummonMonster(Scroll scroll) {
		super(scroll);
		this.setRange(20);
		this.setCarePlot(false);
	}

	@Override
	public void doAction(Player player) {
		this.setFoundBlock(this.getFoundBlock().getRelative(0, 1, 0));
		EntityType e;

		final Random rand = new Random();

		// Random chance to do a big monster
		if (Math.random() < .03)
			switch (rand.nextInt(3)) {
			case 0:
				e = EntityType.GIANT;
				break;
			case 1:
				e = EntityType.GHAST;
				break;
			default:
				e = EntityType.SLIME;
				break;
			}
		else
			switch (rand.nextInt(6)) {
			case 0:
				e = EntityType.PIG_ZOMBIE;
				break;
			case 1:
				e = EntityType.ZOMBIE;
				break;
			case 2:
				e = EntityType.SPIDER;
				break;
			case 3:
				e = EntityType.CREEPER;
				break;
			case 4:
				e = EntityType.CAVE_SPIDER;
				break;
			default:
				e = EntityType.SILVERFISH;
				break;
			}

		player.getWorld().spawnEntity(this.getFoundBlock().getLocation(), e);
	}

	@Override
	public void preAction(Player player) {

	}
}
