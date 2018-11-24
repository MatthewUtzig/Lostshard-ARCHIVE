package com.lostshard.lostshard.Utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AimBlock {

	private Location loc;
	private double viewPos;
	private int maxDistance;
	private int[] blockToIgnore;
	private double checkDistance, curDistance;
	private double xRotation, yRotation;
	private Vector targetPos = new Vector();
	private Vector prevPos = new Vector();
	private final Vector offset = new Vector();

	/**
	 * Constructor requiring a location, uses default values
	 *
	 * @param loc
	 *            Location to work with
	 */
	public AimBlock(Location loc) {
		this.setValues(loc, 300, 0, 0.2, null);
	}

	/**
	 * Constructor requiring a location, max distance, and a checking distance
	 *
	 * @param loc
	 *            What location to work with
	 * @param maxDistance
	 *            How far it checks for blocks
	 * @param checkDistance
	 *            How often to check for blocks, the smaller the more precise
	 */
	public AimBlock(Location loc, int maxDistance, double checkDistance) {
		this.setValues(loc, maxDistance, 1.65, checkDistance, null);
	}

	/**
	 * Constructor requiring a location, max distance, checking distance and an
	 * array of blocks to ignore
	 *
	 * @param loc
	 *            What location to work with
	 * @param maxDistance
	 *            How far it checks for blocks
	 * @param checkDistance
	 *            How often to check for blocks, the smaller the more precise
	 * @param blocksToIgnore
	 *            Array of what block ids to ignore while checking for viable
	 *            targets
	 */
	public AimBlock(Location loc, int maxDistance, double checkDistance, ArrayList<String> blocksToIgnore) {
		final int[] bti = this.convertStringArraytoIntArray(blocksToIgnore);
		this.setValues(loc, maxDistance, 0, checkDistance, bti);
	}

	/**
	 * Constructor requiring a location, max distance, checking distance and an
	 * array of blocks to ignore
	 *
	 * @param loc
	 *            What location to work with
	 * @param maxDistance
	 *            How far it checks for blocks
	 * @param checkDistance
	 *            How often to check for blocks, the smaller the more precise
	 * @param blocksToIgnore
	 *            Array of what block ids to ignore while checking for viable
	 *            targets
	 */
	public AimBlock(Location loc, int maxDistance, double checkDistance, int[] blocksToIgnore) {
		this.setValues(loc, maxDistance, 0, checkDistance, blocksToIgnore);
	}

	/**
	 * Constructor requiring a player, uses default values
	 *
	 * @param player
	 *            Player to work with
	 */
	public AimBlock(Player player) {
		this.setValues(player.getLocation(), 300, 1.65, 0.2, null);
	}

	/**
	 * Constructor requiring a player, max distance, and a checking distance
	 *
	 * @param player
	 *            Player to work with
	 * @param maxDistance
	 *            How far it checks for blocks
	 * @param checkDistance
	 *            How often to check for blocks, the smaller the more precise
	 */
	public AimBlock(Player player, int maxDistance, double checkDistance) {
		this.setValues(player.getLocation(), maxDistance, 1.65, checkDistance, null);
	}

	/**
	 * Constructor requiring a player, max distance, checking distance and an
	 * array of blocks to ignore
	 *
	 * @param loc
	 *            What location to work with
	 * @param maxDistance
	 *            How far it checks for blocks
	 * @param checkDistance
	 *            How often to check for blocks, the smaller the more precise
	 * @param blocksToIgnore
	 *            Array of what block ids to ignore while checking for viable
	 *            targets
	 */
	public AimBlock(Player player, int maxDistance, double checkDistance, ArrayList<String> blocksToIgnore) {
		final int[] bti = this.convertStringArraytoIntArray(blocksToIgnore);
		this.setValues(player.getLocation(), maxDistance, 1.65, checkDistance, bti);
	}

	/**
	 * Constructor requiring a player, max distance, checking distance and an
	 * array of blocks to ignore
	 *
	 * @param loc
	 *            What location to work with
	 * @param maxDistance
	 *            How far it checks for blocks
	 * @param checkDistance
	 *            How often to check for blocks, the smaller the more precise
	 * @param blocksToIgnore
	 *            Array of what block ids to ignore while checking for viable
	 *            targets
	 */
	public AimBlock(Player player, int maxDistance, double checkDistance, int[] blocksToIgnore) {
		this.setValues(player.getLocation(), maxDistance, 1.65, checkDistance, blocksToIgnore);
	}

	private boolean blockToIgnoreHasValue(int value) {
		if (this.blockToIgnore != null)
			if (this.blockToIgnore.length > 0)
				for (final int i : this.blockToIgnore)
					if (i == value)
						return true;
		return false;
	}

	private int[] convertStringArraytoIntArray(ArrayList<String> array) {
		if (array != null) {
			final int intarray[] = new int[array.size()];
			for (int i = 0; i < array.size(); i++)
				try {
					intarray[i] = Integer.parseInt(array.get(i));
				} catch (final NumberFormatException nfe) {
					intarray[i] = 0;
				}
			return intarray;
		}
		return null;
	}

	/**
	 * Returns the current block along the line of vision
	 *
	 * @return Block
	 */
	public Block getCurrentBlock() {
		if (this.curDistance > this.maxDistance)
			return null;
		else
			return this.loc.getWorld().getBlockAt(this.targetPos.getBlockX(), this.targetPos.getBlockY(),
					this.targetPos.getBlockZ());
	}

	/**
	 * Gets the distance to a block
	 *
	 * @return double
	 */
	public double getDistanceToBlock() {
		final Vector blockUnderPlayer = new Vector((int) Math.floor(this.loc.getX() + 0.5),
				(int) Math.floor(this.loc.getY() - 0.5), (int) Math.floor(this.loc.getZ() + 0.5));

		final Block blk = this.getTargetBlock();
		final double x = blk.getX() - blockUnderPlayer.getBlockX();
		final double y = blk.getY() - blockUnderPlayer.getBlockY();
		final double z = blk.getZ() - blockUnderPlayer.getBlockZ();

		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	/**
	 * Gets the distance to a block
	 *
	 * @return int The floored value of the distance
	 */
	public int getDistanceToBlockRounded() {
		final Vector blockUnderPlayer = new Vector((int) Math.floor(this.loc.getX() + 0.5),
				(int) Math.floor(this.loc.getY() - 0.5), (int) Math.floor(this.loc.getZ() + 0.5));

		final Block blk = this.getTargetBlock();
		final double x = blk.getX() - blockUnderPlayer.getBlockX();
		final double y = blk.getY() - blockUnderPlayer.getBlockY();
		final double z = blk.getZ() - blockUnderPlayer.getBlockZ();

		return (int) Math.round(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
	}

	/**
	 * Returns the block attached to the face at the cursor, or null if out of
	 * range
	 *
	 * @return Block
	 */
	@SuppressWarnings("deprecation")
	public Block getFaceBlock() {
		while (this.getNextBlock() != null && (this.getCurrentBlock().getTypeId() == 0
				|| this.blockToIgnoreHasValue(this.getCurrentBlock().getTypeId())))
			;
		if (this.getCurrentBlock() != null)
			return this.getPreviousBlock();
		else
			return null;
	}

	/**
	 * Get next block
	 *
	 * @return Block
	 */
	public Block getNextBlock() {
		this.prevPos = this.targetPos.clone();
		do {
			this.curDistance += this.checkDistance;

			final double h = this.curDistance * Math.cos(Math.toRadians(this.yRotation));
			this.offset.setY(this.curDistance * Math.sin(Math.toRadians(this.yRotation)));
			this.offset.setX(h * Math.cos(Math.toRadians(this.xRotation)));
			this.offset.setZ(h * Math.sin(Math.toRadians(this.xRotation)));

			this.targetPos.setX((int) Math.floor(this.offset.getX() + this.loc.getX()));
			this.targetPos.setY((int) Math.floor(this.offset.getY() + this.loc.getY() + this.viewPos));
			this.targetPos.setZ((int) Math.floor(this.offset.getZ() + this.loc.getZ()));

		} while (this.curDistance <= this.maxDistance && this.targetPos.getBlockX() == this.prevPos.getBlockX()
				&& this.targetPos.getBlockY() == this.prevPos.getBlockY()
				&& this.targetPos.getBlockZ() == this.prevPos.getBlockZ());
		if (this.curDistance > this.maxDistance)
			return null;

		return this.loc.getWorld().getBlockAt(this.targetPos.getBlockX(), this.targetPos.getBlockY(),
				this.targetPos.getBlockZ());
	}

	/**
	 * Returns the previous block in the aimed path
	 *
	 * @return Block
	 */
	public Block getPreviousBlock() {
		return this.loc.getWorld().getBlockAt(this.prevPos.getBlockX(), this.prevPos.getBlockY(),
				this.prevPos.getBlockZ());
	}

	/**
	 * Returns the block at the aim, null if out of range or if no viable target
	 * found
	 *
	 * @return Block
	 */
	@SuppressWarnings("deprecation")
	public Block getTargetBlock() {
		while (this.getNextBlock() != null && (this.getCurrentBlock().getTypeId() == 0
				|| this.blockToIgnoreHasValue(this.getCurrentBlock().getTypeId())))
			;
		return this.getCurrentBlock();
	}

	/**
	 * Gets the floored x distance to a block
	 *
	 * @return int The floored value of the distance
	 */
	public int getXDistanceToBlock() {
		this.reset();
		return (int) Math.floor(this.getTargetBlock().getX() - this.loc.getBlockX() + 0.5);
	}

	/**
	 * Gets the floored y distance to a block
	 *
	 * @return double
	 */
	public int getYDistanceToBlock() {
		this.reset();
		return (int) Math.floor(this.getTargetBlock().getY() - this.loc.getBlockY() + 1.65);
	}

	/**
	 * Gets the floored z distance to a block
	 *
	 * @return double
	 */
	public int getZDistanceToBlock() {
		this.reset();
		return (int) Math.floor(this.getTargetBlock().getZ() - this.loc.getBlockZ() + 0.5);
	}

	private void reset() {
		this.targetPos = new Vector((int) Math.floor(this.loc.getX()), (int) Math.floor(this.loc.getY() + this.viewPos),
				(int) Math.floor(this.loc.getZ()));
		this.prevPos = this.targetPos.clone();
		this.curDistance = 0;
	}

	/**
	 * Sets current block type id
	 *
	 * @param type
	 */
	@SuppressWarnings("deprecation")
	public void setCurrentBlock(int type) {
		final Block blk = this.getCurrentBlock();
		if (blk != null)
			blk.setTypeId(type);
	}

	/**
	 * Sets the type of the block attached to the face at the cursor
	 *
	 * @param type
	 */
	@SuppressWarnings("deprecation")
	public void setFaceBlock(int type) {
		if (this.getCurrentBlock() != null) {
			final Block blk = this.loc.getWorld().getBlockAt(this.prevPos.getBlockX(), this.prevPos.getBlockY(),
					this.prevPos.getBlockZ());
			blk.setTypeId(type);
		}
	}

	/**
	 * Sets previous block type id
	 *
	 * @param type
	 */
	@SuppressWarnings("deprecation")
	public void setPreviousBlock(int type) {
		final Block blk = this.getPreviousBlock();
		if (blk != null)
			blk.setTypeId(type);
	}

	/**
	 * Sets the type of the block at the cursor
	 *
	 * @param type
	 *            Id of type to set the block to
	 */
	@SuppressWarnings("deprecation")
	public void setTargetBlock(int type) {
		while (this.getNextBlock() != null && (this.getCurrentBlock().getTypeId() == 0
				|| this.blockToIgnoreHasValue(this.getCurrentBlock().getTypeId())))
			;
		if (this.getCurrentBlock() != null) {
			final Block blk = this.loc.getWorld().getBlockAt(this.targetPos.getBlockX(), this.targetPos.getBlockY(),
					this.targetPos.getBlockZ());
			blk.setTypeId(type);
		}
	}

	/**
	 * Set the values, all constructors uses this function
	 *
	 * @param loc
	 * @param maxDistance
	 *            How far it checks for blocks
	 * @param viewPos
	 *            Where the view is positioned in y-axis
	 * @param checkDistance
	 *            How often to check for blocks, the smaller the more precise
	 * @param blocksToIgnore
	 *            Ids of blocks to ignore while checking for viable targets
	 */
	private void setValues(Location loc, int maxDistance, double viewPos, double checkDistance, int[] blocksToIgnore) {
		this.loc = loc;
		this.maxDistance = maxDistance;
		this.viewPos = viewPos;
		this.checkDistance = checkDistance;
		this.blockToIgnore = blocksToIgnore;
		this.curDistance = 0;
		this.xRotation = (loc.getYaw() + 90) % 360;
		this.yRotation = loc.getPitch() * -1;

		this.targetPos = new Vector((int) Math.floor(loc.getX()), (int) Math.floor(loc.getY() + viewPos),
				(int) Math.floor(loc.getZ()));
		this.prevPos = this.targetPos.clone();
	}
}