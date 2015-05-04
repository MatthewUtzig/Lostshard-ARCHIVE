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
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Spells.MagicStructure;
import com.lostshard.lostshard.Utils.Output;

public class Gate extends MagicStructure {

	public static Location checkPortals(Block block, Player player) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if (pseudoPlayer.getTimer().recentlyTeleportedTicks > 0)
			return null;

		final MagicStructure ms = findMagicStuckture(block);
		if (ms instanceof Gate) {
			final Gate gate = (Gate) ms;
			Location targetLoc = null;
			if (gate.isSourceBlock(block))
				targetLoc = gate.getToBlock().getLocation();
			else if (gate.isDestBlock(block))
				targetLoc = gate.getFromBlock().getLocation();
			return targetLoc;
		}
		return null;
	}

	public static void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.getBlock().getType().equals(Material.PORTAL))
			event.setCancelled(true);
	}

	public static void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			final Block block = event.getClickedBlock();
			final Player player = event.getPlayer();
			if (block.getType().equals(Material.PORTAL)) {
				final Plot plot = ptm.findPlotAt(block.getLocation());
				final MagicStructure ms = findMagicStuckture(block);
				if (plot == null || plot.isFriendOrAbove(player))
					if (ms != null && ms instanceof Gate) {
						ms.cleanUp();
						if (ms instanceof PermanentGate)
							Database.deletePermanentGate((PermanentGate) ms);
					} else
						block.setType(Material.AIR);
				else
					Output.simpleError(player,
							"You can't do that here, the plot is protected.");
			}
		}
	}

	public static void onPlayerMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (!event.getFrom().getBlock().getType().equals(Material.PORTAL)
				&& event.getTo().getBlock().getType().equals(Material.PORTAL)) {
			Location targetLoc = checkPortals(event.getTo().getBlock(), player);
			if (targetLoc != null) {
				targetLoc = new Location(targetLoc.getWorld(),
						targetLoc.getX() + .5, targetLoc.getY(),
						targetLoc.getZ() + .5);
				final Location curLoc = player.getLocation();
				targetLoc.setPitch(curLoc.getPitch());
				targetLoc.setYaw(curLoc.getYaw());
				pPlayer.getTimer().recentlyTeleportedTicks = 30;
				event.setTo(targetLoc);
			}
		}
	}

	private final Block fromBlock;

	private final Block toBlock;

	private boolean direction;

	@SuppressWarnings("deprecation")
	public Gate(ArrayList<Block> blocks, UUID uuid, int numTicksTillCleanup,
			boolean direction) {
		super(blocks, uuid, numTicksTillCleanup);
		this.direction = direction;
		if (!direction)
			for (final Block b : blocks) {
				b.setType(Material.PORTAL);
				b.setData((byte) 2);
			}
		else
			for (final Block b : blocks)
				b.setType(Material.PORTAL);
		this.setBlocks(blocks);
		this.fromBlock = blocks.get(0);
		this.toBlock = blocks.get(1);
	}

	public Block getFromBlock() {
		return this.fromBlock;
	}

	public Block getToBlock() {
		return this.toBlock;
	}

	public boolean isDestBlock(Block block) {
		if (this.toBlock.getLocation().equals(block.getLocation()))
			return true;
		return false;
	}

	public boolean isDirection() {
		return this.direction;
	}

	public boolean isSourceBlock(Block block) {
		if (this.fromBlock.getLocation().equals(block.getLocation()))
			return true;
		return false;
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}
}
