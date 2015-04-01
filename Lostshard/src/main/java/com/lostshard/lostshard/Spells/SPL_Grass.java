package com.lostshard.lostshard.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Grass extends Spell {

	public SPL_Grass() {
		setName("Grass");
		setSpellWords("Grassius Maximus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(5);
		addReagentCost(new ItemStack(Material.SEEDS));
		setPage(1);
	}
	
	int range = 10;
	Block foundBlock;
	

	public boolean verifyCastable(Player player) {
		foundBlock = SpellUtils.blockInLOS(player, range);
		if(foundBlock == null) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		if(foundBlock.getType().equals(Material.AIR)) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		Plot plot = ptm.findPlotAt(foundBlock.getLocation());
		if(plot != null) {
			if(plot.isProtected()) {
				if(!plot.isFriendOrAbove(player)) {
					Output.simpleError(player, "You cannot cast "+getName()+" there, that plot is protected.");
					return false;
				}
			}
		}
		return true;
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
		for(int x = foundBlock.getX() - 2; x <= foundBlock.getX()+2; x++) {
			for(int y = foundBlock.getY() - 2; y <= foundBlock.getY()+2; y++) {
				for(int z = foundBlock.getZ() - 2; z <= foundBlock.getZ()+2; z++) {
					Block blockAt = foundBlock.getWorld().getBlockAt(x,y,z);
					if(blockAt.getType().equals(Material.DIRT) || blockAt.getType().equals(Material.SOIL)) {
						Block blockAbove = foundBlock.getRelative(0,1,0);
						if(blockAbove.getType().equals(Material.AIR)) {
							if(Utils.isWithin(blockAbove.getLocation(), foundBlock.getLocation(), 2))
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
