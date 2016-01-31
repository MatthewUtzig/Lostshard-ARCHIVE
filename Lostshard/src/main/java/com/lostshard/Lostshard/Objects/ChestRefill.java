package com.lostshard.Lostshard.Objects;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableLocation;
import com.lostshard.Lostshard.Utils.Serializer;

@Entity
@Access(AccessType.PROPERTY)
public class ChestRefill {

	private int id;
	private ItemStack[] items;
	private long rangeMin;
	private long rangeMax;
	private Date refillDate = new Date();
	private Location location;

	public ChestRefill() {

	}

	public ChestRefill(Location loc, long rangeMin, long rangeMax, ItemStack[] items, int id) {
		this.location = loc;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.items = items;
		this.setId(id);
		this.refill();
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

	public void empty() {

	}

	@Column(columnDefinition = "text")
	public String getContents() {
		return Serializer.serializeContents(this.items);
	}

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public int getId() {
		return this.id;
	}

	@Transient
	public ItemStack[] getItems() {
		return this.items;
	}

	@Transient
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

	public SavableLocation getSavableLocation() {
		return new SavableLocation(this.location);
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

	public void refill() {
		final Block block = this.location.getBlock();
		if (!block.getType().equals(Material.CHEST))
			block.setType(Material.CHEST);
		if (block.getState().getType().equals(Material.CHEST)) {
			final Chest chest = (Chest) block.getState();
			chest.getInventory().clear();
			chest.getInventory().setContents(this.items);
			chest.getBlock().getLocation().getWorld().playEffect(this.location.clone().add(0.5, 0.5, 0.5),
					Effect.EXPLOSION_LARGE, 1);
			Chest schest = null;
			if (chest.getBlock().getRelative(BlockFace.EAST).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.EAST).getState();
			else if (chest.getBlock().getRelative(BlockFace.NORTH).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.NORTH).getState();
			else if (chest.getBlock().getRelative(BlockFace.SOUTH).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.SOUTH).getState();
			else if (chest.getBlock().getRelative(BlockFace.WEST).getState() instanceof Chest)
				schest = (Chest) chest.getBlock().getRelative(BlockFace.WEST).getState();
			if (schest != null)
				schest.getBlock().getLocation().getWorld().playEffect(this.location.clone().add(0.5, 0.5, 0.5),
						Effect.EXPLOSION_LARGE, 1);
			final long now = new Date().getTime();
			final long newDate = (long) (this.rangeMin + Math.random() * (this.rangeMax - this.rangeMin));
			this.refillDate = new Date(now + newDate);
		}
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

	public void setContents(String contents) {
		this.items = Serializer.deserializeContents(contents);
	}

	public void setId(int id) {
		this.id = id;
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

	public void setSavableLocation(SavableLocation location) {
		this.location = location.getLocation();
	}

	public void tick() {
		final Date date = new Date();
		if (date.getTime() > this.refillDate.getTime())
			this.refill();
	}
}
