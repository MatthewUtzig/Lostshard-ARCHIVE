package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Listeners.RPGEntityListener;
import com.lostshard.RPG.Utils.Output;

public class SPL_ForcePush extends Spell {
	private static final String 	_name = "Force Push";
	private static final String 	_spellWords = "Fus Ro Dah!";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 15;
	private static final int[]		_reagentCost = {288,331};
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
	
	private ArrayList<LivingEntity> _entitiesFound;
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		_entitiesFound = enemiesInLOS(player, pseudoPlayer, _range);
		if(_entitiesFound.size() <= 0) {
			Output.simpleError(player, "No enemy target found.");
			return false;
		}
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
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
		
		for(LivingEntity lE : _entitiesFound) {
			if(lE != null) {
				if(lE instanceof Player) {
					Player p = (Player)lE;
					RPGEntityListener.criminalAction(p, player);
				}
				Location aLoc = player.getLocation();
				Location dLoc = lE.getLocation();
				Vector v = new Vector();
				v.setX(dLoc.getX() - aLoc.getX());
				v.setY(dLoc.getY() - aLoc.getY());
				v.setZ(dLoc.getZ() - aLoc.getZ());
				v = v.normalize();
				v = v.multiply(5);
				if(v.getY() > .75)
					v.setY(.75);
				
				lE.setVelocity(v);
			}
		}
		
	}
}
