package com.lostshard.lostshard.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;

public class SPL_Heal extends Spell {
	private static final String 	_name = "Heal Other";
	private static final String 	_spellWords = "Buddius Healicus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 20;
	private static final int[]		_reagentCost = {295,287};
	private static final int 		_minMagery = 600;
	private static final int 		_range = 10;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 6; }
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	
	private Player _playerFound;
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		_playerFound = playerInLOS(player, pseudoPlayer, _range);
		if(_playerFound == null) {
			Output.simpleError(player, "No target found.");
			return false;
		}
		player.sendMessage(ChatColor.GREEN + _playerFound.getName());
		/*if(_blockFound.getType().equals(Material.AIR)) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}*/
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
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
		
		if(_playerFound != null) {
			Output.positiveMessage(player, "You have healed "+_playerFound.getName()+".");
			Damageable damag = _playerFound;
			_playerFound.setHealth(Math.min(damag.getHealth()+8, 20));
		}
		
	}
}
