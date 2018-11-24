package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Utils.Bresenham;
import com.lostshard.RPG.Utils.IntPoint;
import com.lostshard.RPG.Utils.Output;

public class SPL_Bridge extends Spell {
	private static final String 	_name = "Bridge";
	private static final String 	_spellWords = "An Bridgius";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 10;
	private static final int		_manaCost = 20;
	private static final int[]		_reagentCost = {338};
	private static final int 		_minMagery = 360;
	private static final int 		_range = 20;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 4; }
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
		
		//check for lapis below you
		if(player.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
		   player.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
			Output.simpleError(player, "Cannot bridge from a Lapis Lazuli block.");
			return false;
		}
		
		//check for lapis
		for(int x=_blockFound.getX()-3; x<=_blockFound.getX()+3; x++) {
			for(int y=_blockFound.getY()-3; y<=_blockFound.getY()+3; y++) {
				for(int z=_blockFound.getZ()-3; z<=_blockFound.getZ()+3; z++) {
					if(_blockFound.getWorld().getBlockTypeIdAt(x,y,z) == (Material.LAPIS_BLOCK.getId())) {
						Output.simpleError(player, "Cannot bridge to a location near Lapis Lazuli blocks.");
						return false;
					}
				}
			}
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
		
		IntPoint srcPoint = new IntPoint(player.getLocation().getBlockX(), player.getLocation().getBlockY()-1, player.getLocation().getBlockZ());
		IntPoint destPoint = new IntPoint(_blockFound.getX(), _blockFound.getY(), _blockFound.getZ());
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		ArrayList<IntPoint> intPoints = Bresenham.bresenham3d(srcPoint.x, srcPoint.y, srcPoint.z, destPoint.x, destPoint.y, destPoint.z);
		for(IntPoint intPoint : intPoints) {
			Block b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z+1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x, intPoint.y, intPoint.z-1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z+1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x+1, intPoint.y, intPoint.z-1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z-1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
			b = player.getWorld().getBlockAt(intPoint.x-1, intPoint.y, intPoint.z+1);
			if(b.getType().equals(Material.AIR) || b.getType().equals(Material.SNOW) || b.getType().equals(Material.FIRE)) { blocks.add(b); }
		}
		
		/*for(Block block : blocks) {
			block.setType(Material.STONE);
		}*/
		
		if(blocks.size() > 0) {
			Bridge bridge = new Bridge(blocks, player.getName(), 150);
			Magery.addMagicStructure(bridge);
		}
	}
}
