package com.lostshard.RPG.Spells;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;

public class SPL_Chronoport extends Spell {
	private static final String 	_name = "Chronoport";
	private static final String 	_spellWords = "Rubberus Bandius";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 25;
	private static final int[]		_reagentCost = {288, 331};
	private static final int 		_minMagery = 480;
	private static final int 		_range = 25;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 5; }
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	
	private Location _chronoPortLocation;
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		Block blockAt = player.getWorld().getBlockAt(player.getLocation());
		
		/*if(!Spell.getInvisibleBlocks().contains((byte)player.getWorld().getBlockTypeIdAt(blockAt.getX(), blockAt.getY(), blockAt.getZ())) ||
			!Spell.getInvisibleBlocks().contains((byte)player.getWorld().getBlockTypeIdAt(blockAt.getX(), blockAt.getY()+1, blockAt.getZ()))) {
			Output.simpleError(player, "There is not enough room to Chronoport here.");
			return false;
		}*/
			
		//check for lapis
		for(int x=blockAt.getX()-3; x<=blockAt.getX()+3; x++) {
			for(int y=blockAt.getY()-3; y<=blockAt.getY()+3; y++) {
				for(int z=blockAt.getZ()-3; z<=blockAt.getZ()+3; z++) {
					if(blockAt.getWorld().getBlockTypeIdAt(x,y,z) == (Material.LAPIS_BLOCK.getId())) {
						Output.simpleError(player, "Cannot Chronoport from a location near Lapis Lazuli blocks.");
						return false;
					}
				}
			}
		}
		
		_chronoPortLocation = player.getLocation();
		
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

		pseudoPlayer._chronoPortLocation = _chronoPortLocation;
		pseudoPlayer._chronoPortTicks = 50;
	}
}
