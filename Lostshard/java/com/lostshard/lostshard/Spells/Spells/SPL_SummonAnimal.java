package com.lostshard.lostshard.Spells.Spells;

import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;

public class SPL_SummonAnimal extends RangedSpell {

	public SPL_SummonAnimal(Scroll scroll) {
		super(scroll);
		setRange(20);
		setCarePlot(false);
	}

	@Override
	public void preAction(Player player) {
	
	}

	@Override
	public void doAction(Player player) {
		setFoundBlock(getFoundBlock().getRelative(0, 1, 0));
		EntityType e;
		Random rand = new Random();
		if(Math.random() <.1) {
			switch(rand.nextInt(3)){
			case 0:
				e = EntityType.HORSE;
				break;
			case 1:
				e = EntityType.HORSE;
			default:
				e = EntityType.WOLF;
			break;
			}
		}
		else {
			switch(rand.nextInt(5)){
			case 0:
				e = EntityType.PIG;
				break;
			case 1:
				e = EntityType.SHEEP;
				break;
			case 2:
				e = EntityType.COW;
				break;
			case 3:
				e = EntityType.CHICKEN;
				break;
			default:
				e = EntityType.SQUID;
			}
		}		
		
		player.getWorld().spawnEntity(getFoundBlock().getLocation(), e);

	}
}
