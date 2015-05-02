package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Grass extends RangedSpell {

	public SPL_Grass(Scroll scroll) {
		super(scroll);
		setRange(10);
		setCarePlot(true);
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	@Override
	public void doAction(Player player) {
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int x = getFoundBlock().getX() - 2; x <= getFoundBlock().getX()+2; x++) {
			for(int y = getFoundBlock().getY() - 2; y <= getFoundBlock().getY()+2; y++) {
				for(int z = getFoundBlock().getZ() - 2; z <= getFoundBlock().getZ()+2; z++) {
					Block blockAt = getFoundBlock().getWorld().getBlockAt(x,y,z);
					if(blockAt.getType().equals(Material.DIRT) || blockAt.getType().equals(Material.SOIL)) {
						Block blockAbove = getFoundBlock().getRelative(0,1,0);
						if(blockAbove.getType().equals(Material.AIR)) {
							if(Utils.isWithin(blockAbove.getLocation(), getFoundBlock().getLocation(), 2))
								blocks.add(blockAt);
						}
					}
				}
			}
		}
		
		for(Block block : blocks) {
			block.setType(Material.GRASS);
		}
		
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	@Override
	public void preAction(Player player) {
		
	}

	
}
