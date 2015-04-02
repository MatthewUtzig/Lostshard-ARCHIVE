package com.lostshard.lostshard.Spells.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Grass extends RangedSpell {

	public SPL_Grass() {
		setName("Grass");
		setSpellWords("Grassius Maximus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(5);
		addReagentCost(new ItemStack(Material.SEEDS));
		setPage(1);
		setRange(10);
		setCarePlot(true);
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
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		pseudoPlayer.getTimer().cantCastTicks = getCooldown();
		
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

	
}
