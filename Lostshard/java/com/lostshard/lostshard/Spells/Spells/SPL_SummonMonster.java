package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;

public class SPL_SummonMonster extends RangedSpell {

	public SPL_SummonMonster(Scroll scroll) {
		super(scroll);
		setRange(20);
		setCarePlot(false);
	}

	@Override
	public void preAction(Player player) {
	
	}

	@Override
	public void doAction(Player player) {
		EntityType e;
		
		//Random chance to do a big monster
		if(Math.random() <.03) {
			int randInt = (int)Math.floor(Math.random()*3);
			switch(randInt){
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
		}
		else {
			int randInt = (int)Math.floor(Math.random()*5);

			switch(randInt){
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
		}
		
		player.getWorld().spawnEntity(getFoundBlock().getLocation(), e);
	}
}
