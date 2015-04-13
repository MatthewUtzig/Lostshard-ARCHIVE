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
	
	public ChestRefill(Location loc, long rangeMin, long rangeMax, ItemStack[] items) {
		this.location = loc;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.items = items;
		refill();
	}

	public ItemStack[] getItems() {
		return items;
	}
	
	public void setItems(ItemStack[] items) {
		this.items = items;
	}
	
	public long getRangeMin() {
		return rangeMin;
	}
	
	public void setRangeMin(long rangeMin) {
		this.rangeMin = rangeMin;
	}
	
	public long getRangeMax() {
		return rangeMax;
	}

	public void setRangeMax(long rangeMax) {
		this.rangeMax = rangeMax;
	}

	public Location getLocation() {
		return location;
	}

	public void setiLocation(Location location) {
		this.location = location;
	}
	
	public Date getRefillDate() {
		return refillDate;
	}

	public void setRefillDate(Date refill) {
		this.refillDate = refill;
	}
	
	public void tick() {
		Date date = new Date();
		if(date.getTime() > refillDate.getTime()) {
			refill();
		}
	}
	
	public void refill() {
		Block block = location.getBlock();
		block.setType(Material.CHEST);
		if(block.getState().getType().equals(Material.CHEST)){
			Chest chest = (Chest) block.getState();
			chest.getInventory().clear();
			chest.getInventory().setContents(items);
			chest.getBlock().getLocation().getWorld().playEffect(location.clone().add(0.5, 0.5, 0.5), Effect.EXPLOSION_LARGE, 1);
			Chest schest = null;
			if(chest.getBlock().getRelative(BlockFace.EAST).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.EAST).getState();
			else if(chest.getBlock().getRelative(BlockFace.NORTH).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.NORTH).getState();
			else if(chest.getBlock().getRelative(BlockFace.SOUTH).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.SOUTH).getState();
			else if(chest.getBlock().getRelative(BlockFace.WEST).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.WEST).getState();
			if(schest != null)
				schest.getBlock().getLocation().getWorld().playEffect(location.clone().add(0.5, 0.5, 0.5), Effect.EXPLOSION_LARGE, 1);
			long newDate = (long) (rangeMin+Math.random()*(rangeMax-rangeMin));
			refillDate = new Date(newDate);
		}
	}
	
	public void empty() {
		
	}
}
