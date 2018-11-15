package com.lostshard.RPG.Spells;

import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.Utils.Output;

public class SPL_Day extends Spell {
	private static final String 	_name = "Day";
	private static final String 	_spellWords = "Morningus Erupticus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 0;
	private static final int[]		_reagentCost = {348,331};
	private static final int 		_minMagery = 840;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 8; }
	//public boolean isWandable()					 	{ return false; }
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		if(player.getWorld().equals(RPG._normalWorld))
			return true;
		Output.simpleError(player, "You can only cast day in the normal world.");
		return false;
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
		if(!RPG._castingDayPlayers.contains(player.getName())) {
			Output.positiveMessage(player, "You begin chanelling day...");
			RPG._castingDayPlayers.add(player.getName());
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			pseudoPlayer._isCastingDay = true;
			pseudoPlayer.setCantCastTicks(_cooldownTicks);
		}
	}
}