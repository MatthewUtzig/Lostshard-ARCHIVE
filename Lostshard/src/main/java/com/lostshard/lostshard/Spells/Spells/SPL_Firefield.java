package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.MagicStructure;
import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Firefield extends RangedSpell {

	public SPL_Firefield() {
		setName("Fire Field");
		setSpellWords("Charmanderous Fieldicus");
		setCarePlot(false);
		setManaCost(15);
		setCooldown(20);
		setCastingDelay(0);
		setPage(5);
		setRange(20);
		setMinMagery(480);
	}

	@Override
	public void preAction(Player player) {
		
	}

	@Override
	public void doAction(Player player) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int x = getFoundBlock().getX() - 2; x <= getFoundBlock().getX()+2; x++) {
			for(int y = getFoundBlock().getY() - 2; y <= getFoundBlock().getY()+2; y++) {
				for(int z = getFoundBlock().getZ() - 2; z <= getFoundBlock().getZ()+2; z++) {
					Block blockAt = getFoundBlock().getWorld().getBlockAt(x,y,z);
					if(!blockAt.getType().equals(Material.AIR)) {
						Block blockAbove = getFoundBlock().getWorld().getBlockAt(x,y+1,z);
						if(blockAbove.getType().equals(Material.AIR) || blockAbove.getType().equals(Material.SNOW)) {
							if(Utils.isWithin(blockAbove.getLocation(), getFoundBlock().getLocation(), 2))
								blocks.add(blockAbove);
						}
					}
				}
			}
		}
		
		for(Block block : blocks) {
			block.setType(Material.FIRE);
		}
		
		if(blocks.size() > 0) {
			new MagicStructure(blocks, player.getUniqueId(), 200);
		}

	}

}
