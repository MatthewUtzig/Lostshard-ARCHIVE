package com.lostshard.RPG.Spells;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class SPL_DRG_Gluttony extends Spell {
	private static final String 	_name = "Gluttony";
	private static final String 	_spellWords = "Fantasticus Delicious";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 40;
	private static final int[]		_reagentCost = {122, 353};
	private static final int 		_minMagery = 1000;
	private static final int 		_range = 10;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 9; }
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	
	private Block _blockFound;
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
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
		
		Location loc = player.getLocation();
		for(int x=-5; x<=5; x++) {
			for(int z=-5; z<=5; z++) {
				int y = player.getWorld().getHighestBlockYAt(loc.getBlockX()+x, loc.getBlockZ()+z);
				Block block = player.getWorld().getBlockAt(loc.getBlockX()+x,y,loc.getBlockZ()+z);
				if(Utils.isWithin(block.getLocation(), player.getLocation(), 5)) {
					block.setType(Material.CAKE_BLOCK);
					block.getWorld().playEffect(block.getLocation(), Effect.SMOKE, (int)Math.random()*3);
				}
			}
		}
	}
}
