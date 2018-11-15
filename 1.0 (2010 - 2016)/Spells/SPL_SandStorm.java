package com.lostshard.RPG.Spells;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Utils.Output;

public class SPL_SandStorm extends Spell {
	private static final String 	_name = "Sand Storm";
	private static final String 	_spellWords = "Sandius Stormus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 1;
	private static final int[]		_reagentCost = {40,331};//{12,40,331};
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
	
	private Player _playerFound;
	private Block _blockFound;
	private int _range = 15;
	private static HashSet<Integer> _fallThroughBlocks = new HashSet<Integer>();
	
	public SPL_SandStorm() {
		_fallThroughBlocks.add(0);
		_fallThroughBlocks.add(8);
		_fallThroughBlocks.add(9);
		_fallThroughBlocks.add(10);
		_fallThroughBlocks.add(11);
	}
	
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		_playerFound = playerInLOS(player, pseudoPlayer, _range);
		if(_playerFound == null) {
			// check land 
			_blockFound = blockInLOS(player, _range);
			if(_blockFound == null) {
				Output.simpleError(player, "Invalid target.");
				return false;
			}
		}
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Permanent Gate Travel...");
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		//System.out.println("RSPNS: "+_response);
		Location buildLocation;
		if(_playerFound != null) {
			Damageable damag = _playerFound;
			if(damag.getHealth() <= 0) {
				return;
			}
			else {
				// build sand above _playerFound
				buildLocation = _playerFound.getLocation();
				buildLocation.setY(buildLocation.getY()+3);
			}
		}
		else if(_blockFound != null) {
			// build sand above _blockFound
			buildLocation = _blockFound.getLocation();
			buildLocation.setY(buildLocation.getY()+4);
		}
		else {
			return;
		}
		
		Plot plot;		
		if(_playerFound != null) {
			plot = PlotHandler.findPlotAt(_playerFound.getLocation());
		}
		else if(_blockFound != null) {
			plot = PlotHandler.findPlotAt(_blockFound.getLocation());
		}
		else {
			return;
		}
		if(plot != null) {
			if(plot.isProtected()) {
				if(!plot.isMember(player.getName())) {
					Output.simpleError(player, "You cannot cast "+_name+" there, that plot is protected.");
					return;
				}
			}
		}
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		ArrayList<Block> cblocks = new ArrayList<Block>();
		// start from the ground
		int total = 0;
		for(int y = buildLocation.getBlockY(); y <= buildLocation.getY()+1; y++) {
			for(int x = buildLocation.getBlockX() - 3; x <= buildLocation.getX()+3; x++) {
				for(int z = buildLocation.getBlockZ() - 3; z <= buildLocation.getZ()+3; z++) {
					total++;
					Block blockAt = buildLocation.getWorld().getBlockAt(x,y,z);
					Block blockAtFinalPosition = findBlockAtRestingPosition(blockAt, blocks);
					if(blockAtFinalPosition != null) {
						cblocks.add(blockAt);
						blocks.add(blockAtFinalPosition);
					}
					else {
						System.out.println("NULL");
					}
				}
			}
		}
		
		int numCBlocks = cblocks.size();
		for(int i=0; i<numCBlocks; i++) {
			cblocks.get(i).setType(Material.SAND);
		}
		
		System.out.println("total:" + total);
		System.out.println("num blocks: " + blocks.size());
		
		if(blocks.size() > 0) {
			SandStorm sandStorm = new SandStorm(blocks, player.getName(), 60);
			Magery.addMagicStructure(sandStorm);
		}
	}
	
	public Block findBlockAtRestingPosition(Block block, ArrayList<Block> blocksSoFar) {
		World world = block.getWorld();
		int curHeight = block.getY();
		for(int i=curHeight-1; i>= 0; i--) {
			int blockTypeId = world.getBlockTypeIdAt(block.getX(), i, block.getZ());
			// determine if the current location would allow the sand block to keep falling
			if(_fallThroughBlocks.contains(blockTypeId)) {
				int numBlocksSoFar = blocksSoFar.size();
				for(int l=0; l<numBlocksSoFar; l++) {
					Block b = blocksSoFar.get(l);
					if((b.getX() == block.getX()) && (b.getY() == i) && (b.getZ() == block.getZ())) {
						System.out.println("f");
						return world.getBlockAt(block.getX(), i+1, block.getZ());
					}
				}
				
				continue;
			}
			else {
				return world.getBlockAt(block.getX(), i+1, block.getZ());
			}
				
		}
		
		return null;
	}
}
