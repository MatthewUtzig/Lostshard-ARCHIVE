package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Skills.Magery;

public class SPL_FireWalk extends Spell {
	private static final String 	_name = "Fire Walk";
	private static final String 	_spellWords = "Charmanderous Feetius";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 25;
	private static final int[]		_reagentCost = {289,331};
	private static final int 		_minMagery = 600;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 6; }
	//public boolean isWandable()					 	{ return false; }
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
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
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		pseudoPlayer._fireWalkTicks = 150;
		FireWalk fireWalk = new FireWalk(new ArrayList<Block>(),player.getName(),150);
		pseudoPlayer._fireWalk = fireWalk;
		Block fireBlock = player.getLocation().getBlock();
		//Plot plot = PlotHandler.findPlotAt(fireBlock.getLocation());
		//if(plot != null) {
			/*if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName()) || plot.isFriend(player.getName())) {
				if(fireBlock.getType().equals(Material.AIR)) {
					fireBlock.setType(Material.FIRE);
					fireWalk.addBlock(fireBlock);
				}
			}
			else {
				
			}*/
		//}
		//else {
			if(fireBlock.getType().equals(Material.AIR)) {
				fireBlock.setType(Material.FIRE);
				fireWalk.addBlock(fireBlock);
			}
		//}
		
		
		
		Magery.addMagicStructure(fireWalk);
		
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
	}
}
