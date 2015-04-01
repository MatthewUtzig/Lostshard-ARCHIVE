package com.lostshard.lostshard.Spells;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Teleport extends Spell {

	private Block foundBlock;
	int range = 25;
	
	public SPL_Teleport() {
		super();
		setName("Teleport");
		setSpellWords("Nearius Porticus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(20);
		setMinMagery(240);
		setPage(3);
		addReagentCost(new ItemStack(Material.FEATHER));
	}

	public Block getFoundBlock() {
		return foundBlock;
	}

	public void setFoundBlock(Block foundBlock) {
		this.foundBlock = foundBlock;
	}
	
	public boolean verifyCastable(Player player) {
		Block blockAt = player.getLocation().getBlock();
		if(!blockAt.getType().equals(Material.IRON_DOOR_BLOCK)) {
			List<Block> lastTwoBlocks = player.getLastTwoTargetBlocks(SpellUtils.invisibleBlocks, range);
			
			if(lastTwoBlocks.size() < 2) {
				Output.simpleError(player, "Invalid target.");
				return false;
			}
			
			boolean ceiling = false;
			Block blockAboveFace = lastTwoBlocks.get(0).getRelative(0,1,0);
			if(!SpellUtils.invisibleBlocks.contains(blockAboveFace.getType()) && SpellUtils.invisibleBlocks.contains(blockAboveFace.getRelative(0,-1,0))) {
				foundBlock = blockAboveFace.getRelative(0,-3,0);
				ceiling = true;
			}
			boolean wall = false;
			if(!ceiling) {
				if(!isRoom(lastTwoBlocks.get(1))) {
					wall=true;
					foundBlock = lastTwoBlocks.get(0);
				}
			}
			
			if(!ceiling && !wall)
				foundBlock = SpellUtils.blockInLOS(player, range);
				
			if(foundBlock == null) {
				Output.simpleError(player, "That location is too far away.");
				return false;
			}
			
			if(!SpellUtils.invisibleBlocks.contains(foundBlock.getRelative(0, 1, 0).getType()) &&
			   !SpellUtils.invisibleBlocks.contains(foundBlock.getRelative(0, 2, 0))) {
				Output.simpleError(player, "There is not enough room to teleport there.");
				return false;
			}
			
			//check for lapis
			for(int x=foundBlock.getX()-3; x<=foundBlock.getX()+3; x++) {
				for(int y=foundBlock.getY()-3; y<=foundBlock.getY()+3; y++) {
					for(int z=foundBlock.getZ()-3; z<=foundBlock.getZ()+3; z++) {
						if(foundBlock.getWorld().getBlockAt(x,y,z).getType().equals(Material.LAPIS_BLOCK)) {
							Output.simpleError(player, "Cannot teleport to a location near Lapis Lazuli blocks.");
							return false;
						}
					}
				}
			}

			/*Block faceBlock = lastTwoBlocks.get(0);
			if(faceBlock != null) {
				if((faceBlock.getX() == _blockFound.getX()) && (faceBlock.getY() == _blockFound.getY()-1) && (faceBlock.getZ() == _blockFound.getZ())) {
					// If the block adjacent to the one we clicked is directly beneath the target block we are trying to
					//	teleport through the ceiling
					Output.simpleError(player, "Cannot teleport through the bottom of a block.");
					return false;
				}
			}
			else {
				Output.simpleError(player, "Invalid target.");
			}*/
			/*if(!hasLOSTo(player, _blockFound)) {
				Output.simpleError(player, "Invalid target. (los)");
				return false;
			}*/
			return true;
		}
		else Output.simpleError(player, "Cannot teleport from an iron door.");
		return false;
	}

	private boolean isRoom(Block block) {
		if(!SpellUtils.invisibleBlocks.contains(block.getRelative(0, 1, 0)) ||
				!SpellUtils.invisibleBlocks.contains(block.getRelative(0, 2, 0))) {
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
		Location teleportTo = new Location(player.getWorld(), (double)foundBlock.getX()+.5, (double)foundBlock.getY()+1, (double)foundBlock.getZ()+.5);
		teleportTo.setPitch(player.getLocation().getPitch());
		teleportTo.setYaw(player.getLocation().getYaw());
		player.teleport(teleportTo);
	}

	
}
