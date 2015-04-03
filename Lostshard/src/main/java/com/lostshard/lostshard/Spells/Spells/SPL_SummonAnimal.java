package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Spells.RangedSpell;

public class SPL_SummonAnimal extends RangedSpell {

	public SPL_SummonAnimal() {
		setName("Summon Animal");
		setSpellWords("Magickus Bambicus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(40);
		addReagentCost(new ItemStack(Material.FEATHER));
		addReagentCost(new ItemStack(Material.REDSTONE));
		setMinMagery(480);
		setRange(20);
		setPage(5);
		setCarePlot(false);
	}

	@Override
	public void preAction(Player player) {
	
	}

	@Override
	public void doAction(Player player) {
		EntityType e;
		if(Math.random() <.1) {
			int randInt = (int)Math.floor(Math.random()*2);
			switch(randInt){
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
			int randInt = (int)Math.floor(Math.random()*4);
			
			switch(randInt){
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
