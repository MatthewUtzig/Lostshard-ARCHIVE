package com.lostshard.RPG.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Utils.Output;

public class SPL_WallOfStone extends Spell {
	private static final String 	_name = "Wall of Stone";
	private static final String 	_spellWords = "Blockus Rockius";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 15;
	private static final int[]		_reagentCost = {1,331};
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
		
		//check for lapis
		for(int x=_blockFound.getX()-3; x<=_blockFound.getX()+3; x++) {
			for(int y=_blockFound.getY()-3; y<=_blockFound.getY()+3; y++) {
				for(int z=_blockFound.getZ()-3; z<=_blockFound.getZ()+3; z++) {
					if(_blockFound.getWorld().getBlockTypeIdAt(x,y,z) == (Material.LAPIS_BLOCK.getId())) {
						Output.simpleError(player, "Cannot build a wall of stone at a location near Lapis Lazuli blocks.");
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
		
		ArrayList<Block> stonewallBlocks = new ArrayList<Block>();
		//stonewall effect
		Location originLoc = player.getLocation();
		originLoc.setY(originLoc.getY()+1.5);
		Location targetLoc = new Location(_blockFound.getWorld(), _blockFound.getX()+.5, _blockFound.getY()+.5, _blockFound.getZ()+.5);
		
		double slopeX = targetLoc.getX() - originLoc.getX();
		double slopeZ = targetLoc.getZ() - originLoc.getZ();
		double perpSlopeX = slopeZ;
		double perpSlopeZ = slopeX;
		
		double length = Math.sqrt(perpSlopeX*perpSlopeX + perpSlopeZ*perpSlopeZ);
		perpSlopeX/=length;
		perpSlopeZ/=length;
		
		if((perpSlopeX > 0) && (perpSlopeZ > 0)) {
			perpSlopeZ *= -1;
		}
		else if(perpSlopeX < 0)
			perpSlopeX *= -1;
		else if(perpSlopeZ < 0)
			perpSlopeZ *= -1;
		
		for(int i=-4; i<4; i++) {
			Block b = _blockFound.getWorld().getBlockAt((int)Math.floor(targetLoc.getX()+(i*perpSlopeX)), (int)Math.floor(targetLoc.getY())+1, (int)Math.floor((targetLoc.getZ())+(i*perpSlopeZ)));
			for(int z = 0; z<3; z++) {
				Block vert = _blockFound.getWorld().getBlockAt(b.getX(), b.getY()+z, b.getZ());
				if(vert.getType().equals(Material.AIR)) {
					//vert.setType(Material.STONE);
					stonewallBlocks.add(vert);
				}
			}
		}
		
		MagicWall stonewall = new MagicWall(stonewallBlocks, player.getName(), 150);
		Magery.addMagicStructure(stonewall);
	}
}
