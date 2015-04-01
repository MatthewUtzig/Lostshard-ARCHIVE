package com.lostshard.lostshard.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Slowfield extends Spell {

	public SPL_Slowfield() {
		setName("Slow Field");
		setSpellWords("Webicus Fieldicus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(15);
		addReagentCost(new ItemStack(Material.STRING));
		setMinMagery(480);
		setPage(5);
	}
	
	int range = 20;
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
		for(int x = foundBlock.getX() - 3; x <= foundBlock.getX()+2; x++) {
			for(int y = foundBlock.getY() - 3; y <= foundBlock.getY()+2; y++) {
				for(int z = foundBlock.getZ() - 3; z <= foundBlock.getZ()+2; z++) {
					Block blockAt = foundBlock.getWorld().getBlockAt(x,y,z);
					if(!blockAt.getType().equals(Material.AIR)) {
						Block blockAbove = foundBlock.getWorld().getBlockAt(x,y+1,z);
						if(blockAbove.getType().equals(Material.AIR) || blockAbove.getType().equals(Material.SNOW)) {
							if(Utils.isWithin(blockAbove.getLocation(), foundBlock.getLocation(), 3))
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
