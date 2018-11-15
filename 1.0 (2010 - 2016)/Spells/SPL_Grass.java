package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class SPL_Grass extends Spell {
	private static final String 	_name = "Grass";
	private static final String 	_spellWords = "Grassius Maximus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 5;
	private static final int[]		_reagentCost = {295};
	private static final int 		_minMagery = 0;
	private static final int 		_range = 10;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 1; }
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	
	private Block _blockFound;
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		_blockFound = blockInLOS(player, _range);
		if(_blockFound == null) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		if(_blockFound.getType().equals(Material.AIR)) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		Plot plot = PlotHandler.findPlotAt(_blockFound.getLocation());
		if(plot != null) {
			if(plot.isProtected()) {
				if(!plot.isMember(player.getName())) {
					Output.simpleError(player, "You cannot cast "+_name+" there, that plot is protected.");
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
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int x = _blockFound.getX() - 2; x <= _blockFound.getX()+2; x++) {
			for(int y = _blockFound.getY() - 2; y <= _blockFound.getY()+2; y++) {
				for(int z = _blockFound.getZ() - 2; z <= _blockFound.getZ()+2; z++) {
					Block blockAt = _blockFound.getWorld().getBlockAt(x,y,z);
					if(blockAt.getType().equals(Material.DIRT) || blockAt.getType().equals(Material.SOIL)) {
						Block blockAbove = _blockFound.getWorld().getBlockAt(x,y+1,z);
						if(blockAbove.getType().equals(Material.AIR)) {
							if(Utils.isWithin(blockAbove.getLocation(), _blockFound.getLocation(), 2))
								blocks.add(blockAt);
						}
					}
				}
			}
		}
		
		// check all the blocks to see if there is a line of sight from the player to the block
		//blocks = removeCantSee(player, blocks);
		
		for(Block block : blocks) {
			block.setType(Material.GRASS);
		}
		
		/*if(blocks.size() > 0) {
			MagicStructure fireField = new MagicStructure(blocks, player.getName(), 200);
			Magery.addMagicStructure(fireField);
		}*/
	}
}
