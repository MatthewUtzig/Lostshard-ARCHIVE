package com.lostshard.RPG.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;

public class SPL_StoneSkin extends Spell {
	private static final String 	_name = "Stone Skin";
	private static final String 	_spellWords = "Rockius Polymorhpus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 50;
	private static final int[]		_reagentCost = {1,331};
	private static final int 		_minMagery = 720;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 7; }
	//public boolean isWandable()					 	{ return false; }
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		Output.simpleError(player, "Sorry, this spell is disabled until we put spout back on the server.");
		return false;
		////spout
		/*SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
		if(!sPlayer.isSpoutCraftEnabled()) {
			Output.simpleError(player, "You can only use this spell if you are using the Spout client.");
			return false;
		}*/
		//return true;
		
		//return false;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
		//Output.positiveMessage(player, "You begin casting Stone Skin...");
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		player.sendMessage(ChatColor.GRAY+"Your skin turns to rock");
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		pseudoPlayer._stoneSkinTicks = 150;
		////spout
		/*SpoutPlayer sPlayer = SpoutManager.getPlayer(player);
		sPlayer.setGravityMultiplier(1.5);
		sPlayer.setWalkingMultiplier(.3);
		sPlayer.setJumpingMultiplier(.5);
		sPlayer.setAirSpeedMultiplier(.8);
		SpoutManager.getAppearanceManager().setGlobalSkin(sPlayer, "http://www.lostshard.com/stoneskin4.png");
		pseudoPlayer.setCantCastTicks(_cooldownTicks);*/
	}
}
