package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Structures.Iceball;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Iceball extends RangedSpell {

	public SPL_Iceball(Scroll scroll) {
		super(scroll);
		setCarePlot(false);
		setRange(15);
	}

	@Override
	public void doAction(Player player) {
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int x = getFoundBlock().getX() - 3; x <= getFoundBlock().getX()+3; x++) {
			for(int y = getFoundBlock().getY() - 3; y <= getFoundBlock().getY()+3; y++) {
				for(int z = getFoundBlock().getZ() - 3; z <= getFoundBlock().getZ()+3; z++) {
					Block blockAt = getFoundBlock().getWorld().getBlockAt(x,y,z);
					if(Utils.isWithin(blockAt.getLocation(), getFoundBlock().getLocation(), 3)) {
						if(blockAt.getType().equals(Material.AIR)) {
							blocks.add(blockAt);
						}
					}
				}
			}
		}
		
		for(Block block : blocks) {
			block.setType(Material.SNOW_BLOCK);
		}
		
		if(blocks.size() > 0) {
			new Iceball(blocks, player.getUniqueId(), 25, false);
		}
	}

	@Override
	public void preAction(Player player) {
		
	}

}
