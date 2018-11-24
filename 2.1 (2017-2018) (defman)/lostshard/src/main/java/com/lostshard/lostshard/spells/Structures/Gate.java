package com.lostshard.lostshard.Spells.Structures;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lostshard.lostshard.main.Lostshard;
import com.lostshard.lostshard.Objects.CustomObjects.SavableLocation;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.plot.Plot;
import com.lostshard.lostshard.Spells.MagicStructure;
import com.lostshard.lostshard.Utils.Output;

@MappedSuperclass
@Access(AccessType.PROPERTY)
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
							((PermanentGate) ms).delete();
					} else
						block.setType(Material.AIR);
				else
					Output.simpleError(player, "You can't do that here, the plot is protected.");
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
				targetLoc = new Location(targetLoc.getWorld(), targetLoc.getX() + .5, targetLoc.getY(),
						targetLoc.getZ() + .5);
				final Location curLoc = player.getLocation();
				targetLoc.setPitch(curLoc.getPitch());
				targetLoc.setYaw(curLoc.getYaw());
				pPlayer.getTimer().recentlyTeleportedTicks = 30;
				event.setTo(targetLoc);
			}
		}
	}

	private Block fromBlock;

	private Block toBlock;

	private boolean direction;

	public Gate() {
		super(Arrays.asList(new BlockState[4]), -1);
	}
	
	@SuppressWarnings("deprecation")
	public Gate(List<Block> blocks, UUID uuid, int numTicksTillCleanup, boolean direction) {
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

	public void delete() {
		final Session s = Lostshard.getSession();
		try {
			final Transaction t = s.beginTransaction();
			t.begin();
			s.delete(this);
			t.commit();
			s.clear();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
	}

	@Transient
	public Block getFromBlock() {
		return this.fromBlock;
	}

	@AttributeOverrides({ @AttributeOverride(name = "x", column = @Column(name = "from_x") ),
		@AttributeOverride(name = "y", column = @Column(name = "from_y") ),
		@AttributeOverride(name = "z", column = @Column(name = "from_z") ),
		@AttributeOverride(name = "pitch", column = @Column(name = "from_pitch") ),
		@AttributeOverride(name = "yaw", column = @Column(name = "from_yaw") ),
		@AttributeOverride(name = "world", column = @Column(name = "from_world") )
	})
	public SavableLocation getFromLocation() {
		return new SavableLocation(this.fromBlock.getLocation());
	}

	@Transient
	public Block getToBlock() {
		return this.toBlock;
	}

	@AttributeOverrides({ @AttributeOverride(name = "x", column = @Column(name = "to_x") ),
		@AttributeOverride(name = "y", column = @Column(name = "to_y") ),
		@AttributeOverride(name = "z", column = @Column(name = "to_z") ),
		@AttributeOverride(name = "pitch", column = @Column(name = "to_pitch") ),
		@AttributeOverride(name = "yaw", column = @Column(name = "to_yaw") ),
		@AttributeOverride(name = "world", column = @Column(name = "to_world") )
	})
	public SavableLocation getToLocation() {
		return new SavableLocation(this.toBlock.getLocation());
	}

	public void insert() {
		final Session s = Lostshard.getSession();
		try {
			final Transaction t = s.beginTransaction();
			t.begin();
			s.save(this);
			t.commit();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
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

	public void save() {
		final Session s = Lostshard.getSession();
		try {
			final Transaction t = s.beginTransaction();
			t.begin();
			s.update(this);
			t.commit();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
	}

	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	public void setFromLocation(SavableLocation location) {
		this.fromBlock = location.getLocation().getBlock();
		this.setBlock(this.fromBlock, 0);
		this.fromBlock.setType(Material.PORTAL);
		this.fromBlock.getRelative(0, 1, 0).setType(Material.PORTAL);
		this.setBlock(this.fromBlock.getRelative(0, 1, 0), 2);
	}

	public void setToLocation(SavableLocation location) {
		this.toBlock = location.getLocation().getBlock();
		this.toBlock.setType(Material.PORTAL);
		this.toBlock.getRelative(0, 1, 0).setType(Material.PORTAL);
		this.setBlock(this.toBlock, 1);
		this.setBlock(this.toBlock.getRelative(0, 1, 0), 3);
	}
}
