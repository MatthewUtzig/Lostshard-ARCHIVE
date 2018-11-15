package com.lostshard.RPG.Spells;

import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;

public class SPL_MoonJump extends Spell {
	private static final String 	_name = "Moon Jump";
	private static final String 	_spellWords = "Hypnoticus Astronauticus";
	private static final int 		_castingDelay = 20;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 30;
	private static final int[]		_reagentCost = {288,331};
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
		////spout
		/*SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
		if(!sPlayer.isSpoutCraftEnabled()) {
			Output.simpleError(player, "You can only use this spell if you are using the Spout client.");
			return false;
		}*/
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Moon Jump...");
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		////spout
		/*Output.positiveMessage(player, "You suddenly feel lighter... ");
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		pseudoPlayer._moonJumpTicks = 300;
		////SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
		sPlayer.setGravityMultiplier(.1);
		sPlayer.setCanFly(true);
		pseudoPlayer.setCantCastTicks(_cooldownTicks);*/
	}
}
