package com.lostshard.RPG.Spells;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;

public class SPL_SummonMonster extends Spell {
	private static final String 	_name = "Summon Monster";
	private static final String 	_spellWords = "Magickus Tradjicus"; // Magickus Tradjicus
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 60;
	private static final int[]		_reagentCost = {352,287,289,331};
	private static final int 		_minMagery = 840;
	private static final int 		_range = 20;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 8; }
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	
	private Block _blockFound;
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		_blockFound = faceBlockInLOS(player, _range);
		if(_blockFound == null) {
			Output.simpleError(player, "Invalid target or too far away.");
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
		
		CreatureType creatureType;
		
		//Random chance to do a big monster
		if(Math.random() <.03) {
			int randInt = (int)Math.floor(Math.random()*3);
			
			if(randInt == 0)
				creatureType = CreatureType.GIANT;
			else if(randInt == 1)
				creatureType = CreatureType.GHAST;
			else
				creatureType = CreatureType.SLIME;
		}
		else {
			int randInt = (int)Math.floor(Math.random()*4);

			if(randInt == 0)
				creatureType = CreatureType.PIG_ZOMBIE;
			else if(randInt == 1)
				creatureType = CreatureType.ZOMBIE;
			else if(randInt == 2)
				creatureType = CreatureType.SPIDER;
			else 
				creatureType = CreatureType.CREEPER;
		}
		
		Location loc = new Location(_blockFound.getWorld(), _blockFound.getX()+.5, _blockFound.getY()+.5, _blockFound.getZ()+.5);
		player.getWorld().spawnCreature(loc, creatureType);//.dropItem(_blockFound.getLocation(), new ItemStack(itemToGive, 1));
	}
}
