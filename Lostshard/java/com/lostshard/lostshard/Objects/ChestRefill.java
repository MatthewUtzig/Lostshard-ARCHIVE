package com.lostshard.lostshard.Objects;

import java.util.Date;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class ChestRefill {

	private ItemStack[] items;
	private long rangeMin;
	private long rangeMax;
	private Date refillDate;
	private Location location;

	public ChestRefill(Location loc, long rangeMin, long rangeMax,
			ItemStack[] items) {
		this.location = loc;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.items = items;
		this.refill();
	}

	public void empty() {

	}

	public ItemStack[] getItems() {
		return this.items;
	}

	public Location getLocation() {
		return this.location;
	}

	public long getRangeMax() {
		return this.rangeMax;
	}

	public long getRangeMin() {
		return this.rangeMin;
	}

	public Date getRefillDate() {
		return this.refillDate;
	}

	public void refill() {
		final Block block = this.location.getBlock();
		block.setType(Material.CHEST);
		if (block.getState().getType().equals(Material.CHEST)) {
			final Chest chest = (Chest) block.getState();
			chest.getInventory().clear();
			chest.getInventory().setContents(this.items);
			chest.getBlock()
					.getLocation()
					.getWorld()
					.playEffect(this.location.clone().add(0.5, 0.5, 0.5),
							Effect.EXPLOSION_LARGE, 1);
			Chest schest = null;
			if (chest.getBlock().getRelative(BlockFace.EAST).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.EAST)
						.getState();
			else if (chest.getBlock().getRelative(BlockFace.NORTH).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.NORTH)
						.getState();
			else if (chest.getBlock().getRelative(BlockFace.SOUTH).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.SOUTH)
						.getState();
			else if (chest.getBlock().getRelative(BlockFace.WEST).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.WEST)
						.getState();
			if (schest != null)
				schest.getBlock()
						.getLocation()
						.getWorld()
						.playEffect(this.location.clone().add(0.5, 0.5, 0.5),
								Effect.EXPLOSION_LARGE, 1);
			final long newDate = (long) (this.rangeMin + Math.random()
					* (this.rangeMax - this.rangeMin));
			this.refillDate = new Date(newDate);
		}
	}

	public void setiLocation(Location location) {
		this.location = location;
	}

	public void setItems(ItemStack[] items) {
		this.items = items;
	}

	public void setRangeMax(long rangeMax) {
		this.rangeMax = rangeMax;
	}

	public void setRangeMin(long rangeMin) {
		this.rangeMin = rangeMin;
	}

	public void setRefillDate(Date refill) {
		this.refillDate = refill;
	}

	public void tick() {
		final Date date = new Date();
		if (date.getTime() > this.refillDate.getTime())
			this.refill();
	}
}
