package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Listeners.RPGEntityListener;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Utils.Utils;

public class SPL_Blizzard extends Spell {
	private static final String 	_name = "Blizzard";
	private static final String 	_spellWords = "Freezius Stormicus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 25;
	private static final int[]		_reagentCost = {288,287};
	private static final int 		_minMagery = 600;
	private static final int 		_range = 5;
	private static final int 		_damage = 6;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 6; }
	
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
		
		Block block = player.getLocation().getBlock();
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(int x = block.getX() - _range; x <= block.getX()+_range; x++) {
			for(int y = block.getY() - _range; y <= block.getY()+_range; y++) {
				for(int z = block.getZ() - _range; z <= block.getZ()+_range; z++) {
					Block blockAt = block.getWorld().getBlockAt(x,y,z);
					if(Utils.isWithin(blockAt.getLocation(), block.getLocation(), _range)) {
						if(blockAt.getType().equals(Material.AIR) && !(blockAt.getRelative(BlockFace.DOWN).getType().equals(Material.AIR))) {
							blocks.add(blockAt);
						}
					}
				}
			}
		}
		
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();
		for(Player p : onlinePlayers) {
			if(p.equals(player))
				continue;
			
			if(Utils.isWithin(p.getLocation(), player.getLocation(), _range)) {
				PseudoPlayer pP = PseudoPlayerHandler.getPseudoPlayer(p.getName());
				if(!pP._convenient) {
					if(RPGEntityListener.canPlayerDamagePlayer(player, p)) {
						p.damage(_damage);
						RPGEntityListener.criminalAction(p, player);
					}
				}
			}
		}
		
		/*// check all the blocks to see if there is a line of sight from the player to the block
		int numBlocks = blocks.size();
		IntPoint playerIntPoint = new IntPoint((int)Math.round(player.getLocation().getX()), (int)Math.round(player.getLocation().getY())+1, (int)Math.round(player.getLocation().getZ()));
		for(int i=numBlocks-1; i>=0; i--) {
			Block block = blocks.get(i);
			ArrayList<IntPoint> losPoints = Bresenham.bresenham3d(block.getX(), block.getY(), block.getZ(), playerIntPoint.x, playerIntPoint.y, playerIntPoint.z);
			int numLosPoints = losPoints.size();
			for(int f=1; f<numLosPoints; f++) {
				IntPoint intPoint = losPoints.get(f);
				int blockTypeId = block.getWorld().getBlockTypeIdAt(intPoint.x, intPoint.y, intPoint.z);
				if(blockTypeId != Material.AIR.getId()) {
					blocks.remove(i);
					break;
				}
			}
		}*/
		
		for(Block b : blocks) {
			if(Math.random() < .2)
				b.setType(Material.SNOW_BLOCK);
			else
				b.setType(Material.SNOW);
		}
		block.setType(Material.SNOW);
		
		if(blocks.size() > 0) {
			Blizzard blizzard = new Blizzard(blocks, player.getName(), 75);
			Magery.addMagicStructure(blizzard);
		}
	}
}
