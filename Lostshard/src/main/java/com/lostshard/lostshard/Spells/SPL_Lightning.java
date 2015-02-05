package com.lostshard.lostshard.Spells;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PVPHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Lightning extends Spell {
	private static final String 	_name = "Lightning";
	private static final String 	_spellWords = "Zeusius Simliaricus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 15;
	private static final int[]		_reagentCost = {287,331};
	private static final int 		_minMagery = 720;
	private static final int 		_range = 20;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 7; }
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
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
		
		Location strikeLoc = _blockFound.getLocation();
		strikeLoc.setX(strikeLoc.getX()+.5);
		strikeLoc.setZ(strikeLoc.getZ()+.5);
		_blockFound.getWorld().strikeLightning(strikeLoc);
		
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for(Player p : players) {
			if(Utils.isWithin(p.getLocation(), strikeLoc, 5)) {
				//Casting lightning at someone?
				if(PVPHandler.canEntityAttackEntity(player, p)) {
					PVPHandler.criminalAction(p, player);
				}				
			}
		}
	}
}