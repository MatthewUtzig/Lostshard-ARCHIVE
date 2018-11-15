package com.lostshard.RPG.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Listeners.RPGEntityListener;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class SPL_MeteorStorm extends Spell {
	private static final String 	_name = "Meteor Storm";
	private static final String 	_spellWords = "Deepius Impacto";
	private static final int 		_castingDelay = 20;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 50;
	private static final int[]		_reagentCost = {289,331};
	private static final int 		_minMagery = 600;
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
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
		
		Location strikeLoc = _blockFound.getLocation();
		strikeLoc.setX(strikeLoc.getX()+.5);
		strikeLoc.setZ(strikeLoc.getZ()+.5);
		
		Fireball fb = player.getWorld().spawn(new Location(strikeLoc.getWorld(), strikeLoc.getX(), 127, strikeLoc.getZ()), Fireball.class);
		fb.setShooter(player);
		fb.setDirection(new Vector(0,-1,0));
		//_blockFound.getWorld().strikeLightning(strikeLoc);
		
		Player[] players = Utils.getPlugin().getServer().getOnlinePlayers();
		for(Player p : players) {
			if(Utils.isWithin(p.getLocation(), strikeLoc, 5)) {
				//Casting lightning at someone?
				if(RPGEntityListener.canPlayerDamagePlayer(player, p)) {
					RPGEntityListener.criminalAction(p, player);
				}				
			}
		}
	}
}