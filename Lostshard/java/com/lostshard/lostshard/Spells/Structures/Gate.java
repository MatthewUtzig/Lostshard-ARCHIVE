package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.MagicStructure;
import com.lostshard.lostshard.Utils.Output;

public class Gate extends MagicStructure {

	private Block fromBlock;
	private Block toBlock;
	
	public Gate(ArrayList<Block> blocks, UUID uuid, int numTicksTillCleanup) {
		super(blocks, uuid, numTicksTillCleanup);
		for(Block b : blocks)
			b.setType(Material.PORTAL);
		setBlocks(blocks);
		fromBlock = blocks.get(0);
		toBlock = blocks.get(1);
	}
	
	public boolean isSourceBlock(Block block) {
		if(fromBlock.getLocation().equals(block.getLocation()))
			return true;
		return false;
	}
	
	public boolean isDestBlock(Block block) {
		if(toBlock.getLocation().equals(block.getLocation()))
			return true;
		return false;
	}
	
	public Block getFromBlock() {
		return fromBlock;
	}
	
	public Block getToBlock() {
		return toBlock;
	}

	public static void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			Player player = event.getPlayer();
			if(block.getType().equals(Material.PORTAL)) {
				Plot plot = ptm.findPlotAt(block.getLocation());
				MagicStructure ms = findMagicStuckture(block);
				if(plot == null || plot.isFriendOrAbove(player))
					if(ms != null && ms instanceof Gate) {
						ms.cleanUp();
						if(ms instanceof PermanentGate)
							Database.deletePermanentGate((PermanentGate)ms);
					}else
						block.setType(Material.AIR);
				else
					Output.simpleError(player, "You cannot do that here, the plot is protected.");
			}
		}
	}

	public static void onBlockPhysics(BlockPhysicsEvent event) {
		if(event.getBlock().getType().equals(Material.PORTAL))
			event.setCancelled(true);
	}

	public static void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
    	if(!event.getFrom().getBlock().getType().equals(Material.PORTAL) && event.getTo().getBlock().getType().equals(Material.PORTAL)) {
    		Location targetLoc = checkPortals(event.getTo().getBlock(),player);
			if(targetLoc != null) {
				targetLoc = new Location(targetLoc.getWorld(), targetLoc.getX()+.5, targetLoc.getY(), targetLoc.getZ()+.5);
				Location curLoc = player.getLocation();
				targetLoc.setPitch(curLoc.getPitch());
				targetLoc.setYaw(curLoc.getYaw());
				pPlayer.getTimer().recentlyTeleportedTicks = 30;
				event.setTo(targetLoc);
			}
    	}
	}
	
   public static Location checkPortals(Block block, Player player) {
    	PseudoPlayer pseudoPlayer = pm.getPlayer(player);
    	if(pseudoPlayer.getTimer().recentlyTeleportedTicks > 0)
    		return null;
		
		MagicStructure ms = findMagicStuckture(block);
		if(ms instanceof Gate) {
			Gate gate = (Gate) ms;
			Location targetLoc = null;
			if(gate.isSourceBlock(block)) {
				targetLoc = gate.getToBlock().getLocation();
			}
			else if(gate.isDestBlock(block)) {
				targetLoc = gate.getFromBlock().getLocation();
			}
			return targetLoc;
		}
		return null;
	}   
}
