package com.lostshard.Lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableLocation;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Spells.MagicStructure;
import com.lostshard.Lostshard.Utils.Output;

@Embeddable
@Access(AccessType.PROPERTY)
public class Gate extends MagicStructure {


	private Block fromBlock;

	private Block toBlock;

	private boolean direction;
	
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
							((PermanentGate) ms).delete();
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

	@Transient
	public Block getFromBlock() {
		return this.fromBlock;
	}

	@Transient
	public Block getToBlock() {
		return this.toBlock;
	}

	@Transient
	public boolean isDestBlock(Block block) {
		if (this.toBlock.getLocation().equals(block.getLocation()))
			return true;
		return false;
	}

	public boolean isDirection() {
		return this.direction;
	}

	@Transient
	public boolean isSourceBlock(Block block) {
		if (this.fromBlock.getLocation().equals(block.getLocation()))
			return true;
		return false;
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}
	
	public SavableLocation getFromLocation() {
		return new SavableLocation(this.fromBlock.getLocation());
	}
	
	public  void setFromLocation(SavableLocation location) {
		this.fromBlock = location.getLocation().getBlock();
	}
	
	public SavableLocation getToLocation() {
		return new SavableLocation(this.toBlock.getLocation());
	}
	
	public  void setToLocation(SavableLocation location) {
		this.toBlock = location.getLocation().getBlock();
	}
	
	public void save() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.update(this);
		t.commit();
		s.close();
	}
	
	public void insert() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.save(this);
		t.commit();
		s.close();
	}
	
	public void delete() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.delete(this);
		t.commit();
		s.close();
	}
}
