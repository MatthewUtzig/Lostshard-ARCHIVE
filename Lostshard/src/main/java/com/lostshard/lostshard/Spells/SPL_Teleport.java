//package com.lostshard.lostshard.Spells;
//
//import java.util.List;
//
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Player;
//
//public class SPL_Teleport extends Spell {
//	private static final String 	_name = "Teleport";
//	private static final String 	_spellWords = "Nearius Porticus";
//	private static final int 		_castingDelay = 0;
//	private static final int 		_cooldownTicks = 20;
//	private static final int		_manaCost = 20;
//	private static final int[]		_reagentCost = {288};
//	private static final int 		_minMagery = 240;
//	private static final int 		_range = 25;
//	
//	public String getName() 		{ return _name; }
//	public String getSpellWords() 	{ return _spellWords; }
//	public int getCastingDelay() 	{ return _castingDelay; }
//	public int getCooldownTicks()	{ return _cooldownTicks; }
//	public int getManaCost() 		{ return _manaCost; }
//	public int[] getReagentCost() 	{ return _reagentCost; }
//	public int getMinMagery() 		{ return _minMagery; }
//	
//	public int getPageNumber()		{ return 3; }
//	
//	/* Used to confirm that the spell can be cast, so, for example, if you were
//	 * attempting to teleport to some location that was blocked, we would figure
//	 * that out here and cancel the spell.
//	 */
//	
//	private Block _blockFound;
//	
//	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
//		Block blockAt = player.getLocation().getBlock();
//		if(!blockAt.getType().equals(Material.IRON_DOOR_BLOCK)) {
//			/*AimBlock aimHit = new AimBlock(player, _range, .5);
//			_blockFound = aimHit.getTargetBlock();*/
//			List<Block> lastTwoBlocks = getLastTwoTargetBlocks(player, _range);
//			
//			if(lastTwoBlocks.size() < 2) {
//				Output.simpleError(player, "Invalid target.");
//				return false;
//			}
//			
//			boolean ceiling = false;
//			Block blockAboveFace = lastTwoBlocks.get(0).getRelative(0,1,0);
//			if(!Spell.getInvisibleBlocks().contains((byte)blockAboveFace.getType().getId()) && Spell.getInvisibleBlocks().contains((byte)blockAboveFace.getRelative(0,-1,0).getTypeId())) {
//				_blockFound = blockAboveFace.getRelative(0,-3,0);
//				ceiling = true;
//			}
//			boolean wall = false;
//			if(!ceiling) {
//				if(!isRoom(lastTwoBlocks.get(1))) {
//					wall=true;
//					_blockFound = lastTwoBlocks.get(0);
//				}
//			}
//			
//			if(!ceiling && !wall)
//				_blockFound = blockInLOS(player, _range);
//				
//			if(_blockFound == null) {
//				Output.simpleError(player, "That location is too far away.");
//				return false;
//			}
//			/*if(_blockFound.getType().equals(Material.AIR)) {
//				Output.simpleError(player, "That location is too far away.");
//				return false;
//			}*/
//			if(!Spell.getInvisibleBlocks().contains((byte)player.getWorld().getBlockTypeIdAt(_blockFound.getX(), _blockFound.getY()+1, _blockFound.getZ())) ||
//			   !Spell.getInvisibleBlocks().contains((byte)player.getWorld().getBlockTypeIdAt(_blockFound.getX(), _blockFound.getY()+2, _blockFound.getZ()))) {
//				Output.simpleError(player, "There is not enough room to teleport there.");
//				return false;
//			}
//			
//			/*//check for lapis below you
//			if(player.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
//			   player.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
//				Output.simpleError(player, "Cannot teleport from a Lapis Lazuli block.");
//				return false;
//			}*/
//			
//			//check for lapis
//			for(int x=_blockFound.getX()-3; x<=_blockFound.getX()+3; x++) {
//				for(int y=_blockFound.getY()-3; y<=_blockFound.getY()+3; y++) {
//					for(int z=_blockFound.getZ()-3; z<=_blockFound.getZ()+3; z++) {
//						if(_blockFound.getWorld().getBlockTypeIdAt(x,y,z) == (Material.LAPIS_BLOCK.getId())) {
//							Output.simpleError(player, "Cannot teleport to a location near Lapis Lazuli blocks.");
//							return false;
//						}
//					}
//				}
//			}
//
//			/*Block faceBlock = lastTwoBlocks.get(0);
//			if(faceBlock != null) {
//				if((faceBlock.getX() == _blockFound.getX()) && (faceBlock.getY() == _blockFound.getY()-1) && (faceBlock.getZ() == _blockFound.getZ())) {
//					// If the block adjacent to the one we clicked is directly beneath the target block we are trying to
//					//	teleport through the ceiling
//					Output.simpleError(player, "Cannot teleport through the bottom of a block.");
//					return false;
//				}
//			}
//			else {
//				Output.simpleError(player, "Invalid target.");
//			}*/
//			/*if(!hasLOSTo(player, _blockFound)) {
//				Output.simpleError(player, "Invalid target. (los)");
//				return false;
//			}*/
//			return true;
//		}
//		else Output.simpleError(player, "Cannot teleport from an iron door.");
//		return false;
//	}
//	
//	private boolean isRoom(Block block) {
//		if(!Spell.getInvisibleBlocks().contains((byte)block.getWorld().getBlockTypeIdAt(block.getX(), block.getY()+1, block.getZ())) ||
//				!Spell.getInvisibleBlocks().contains((byte)block.getWorld().getBlockTypeIdAt(block.getX(), block.getY()+2, block.getZ()))) {
//			return false;
//		}
//		return true;
//	}
//	
//	/* Used for anything that must be handled as soon as the spell is cast,
//	 * for example targeting a location for a delayed spell.
//	 */
//	public void preAction(Player player) {
//		
//	}
//	
//	/* The meat of the spell code, this is what happens when the spell is
//	 * actually activated and should be doing something.
//	 */
//	public void doAction(Player player) {
//		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
//		pseudoPlayer.setCantCastTicks(_cooldownTicks);
//		Location teleportTo = new Location(player.getWorld(), (double)_blockFound.getX()+.5, (double)_blockFound.getY()+1, (double)_blockFound.getZ()+.5);
//		teleportTo.setPitch(player.getLocation().getPitch());
//		teleportTo.setYaw(player.getLocation().getYaw());
//		player.teleport(teleportTo);
//	}
//}
