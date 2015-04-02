package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Structures.WebTrap;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Slowfield extends RangedSpell {

	public SPL_Slowfield() {
		setName("Slow Field");
		setSpellWords("Webicus Fieldicus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(15);
		addReagentCost(new ItemStack(Material.STRING));
		setMinMagery(480);
		setPage(5);
		setRange(20);
		setCarePlot(false);
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
		
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int x = getFoundBlock().getX() - 3; x <= getFoundBlock().getX()+2; x++) {
			for(int y = getFoundBlock().getY() - 3; y <= getFoundBlock().getY()+2; y++) {
				for(int z = getFoundBlock().getZ() - 3; z <= getFoundBlock().getZ()+2; z++) {
					Block blockAt = getFoundBlock().getWorld().getBlockAt(x,y,z);
					if(!blockAt.getType().equals(Material.AIR)) {
						Block blockAbove = getFoundBlock().getWorld().getBlockAt(x,y+1,z);
						if(blockAbove.getType().equals(Material.AIR) || blockAbove.getType().equals(Material.SNOW)) {
							if(Utils.isWithin(blockAbove.getLocation(), getFoundBlock().getLocation(), 3))
								blocks.add(blockAbove);
						}
					}
				}
			}
		}

		for(Block block : blocks) {
			block.setType(Material.WEB);
		}
		
		if(blocks.size() > 0) {
			new WebTrap(blocks, player.getUniqueId(), 50);
		}
	}
	
}
