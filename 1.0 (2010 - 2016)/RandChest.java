package com.lostshard.RPG;

import java.util.Date;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R1.block.CraftChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class RandChest {
	private int _id;
	private Location _location;
	private ArrayList<ItemStack[]> _allContents;
	private long _nextFill;
	private int _minRand;
	private int _maxRand;
	public boolean _dead = false;
	
	// loading one from the database
	public RandChest(int id, Location location, ArrayList<ItemStack[]> allContents, long nextFill, int minRand, int maxRand) {
		_id = id;
		_location = location;
		_allContents = allContents;
		_nextFill = nextFill;
		_minRand = minRand;
		_maxRand = maxRand;
	}
	
	// creating a new one
	public RandChest(Location location, ItemStack[] contents, int minRand, int maxRand) {
		_location = location;
		_allContents = new ArrayList<ItemStack[]>();
		_allContents.add(contents);
		_minRand = minRand;
		_maxRand = maxRand;
		updateNextFill();
	}
	
	public int getId() {
		return _id;
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public Location getLocation() {
		return _location;
	}
	
	public void setLocation(Location location) {
		_location = location;
	}
	
	public long getNextFill() {
		return _nextFill;
	}
	
	public void setNextFill(long nextFill) {
		_nextFill = nextFill;
	}
	
	public ArrayList<ItemStack[]> getContents() {
		return _allContents;
	}
	
	public void setContents(ArrayList<ItemStack[]> contents) {
		_allContents = contents;
	}
	
	public int getMinRand() {
		return _minRand;
	}
	
	public void setMinRand(int minRand) {
		_minRand = minRand;
	}
	
	public int getMaxRand() {
		return _maxRand;
	}
	
	public void setMaxRand(int maxRand) {
		_maxRand = maxRand;
	}
	
	public void clearContents() {
		_allContents.clear();
	}
	
	public void addContents(ItemStack[] contents) {
		_allContents.add(contents);
	}
	
	public ItemStack[] getRandomPage() {
		if(_allContents.size() <= 0)
			return null;
		else {
			int numPages = _allContents.size();
			int rand = (int)Math.floor(Math.random()*numPages);
			return _allContents.get(rand);
		}	
	}
	
	public void fill(Player player, CraftChest chest) {
		ItemStack[] randPage = getRandomPage();
		if(randPage == null) {
			if(player != null)
				Output.simpleError(player, "No random page to fill with, chest cleared.");
			chest.getInventory().clear();
			return;
		}
		
		chest.getInventory().clear();
		int numItemsOnPage = randPage.length;
		for(int j=0; j<numItemsOnPage; j++) {
			int count = 0;
			boolean found = false;
			while(count < 10 && !found) {
				int slot = (int)Math.floor(Math.random()*27);
				if(chest.getInventory().getItem(slot) == null || chest.getInventory().getItem(slot).getType().equals(Material.AIR)) {
					found = true;
					ItemStack itemStack = randPage[j];
					if(itemStack != null) {
						int amount = itemStack.getAmount();
						if(amount <= 0)
							amount = 1;
						ItemStack newItem = new ItemStack(itemStack.getType(), amount);
						newItem.setDurability(itemStack.getDurability());
						chest.getInventory().setItem(slot, newItem);
					}
				}
				count++;
			}
		}
		updateNextFill();
		Database.updateRandChestFillTime(this);
	}
	
	public void updateNextFill() {
		Date date = new Date();
		_nextFill = (long)(date.getTime() + _minRand + (int)Math.ceil(Math.random()*(_maxRand-_minRand)));
	}
	
	public void tick5Minutes() {
		Date date = new Date();
		//System.out.println("Time:" + date.getTime());
		//System.out.println("Next Fill:"+ _nextFill);
		if(date.getTime() > _nextFill) {
			// time to fill
			//Utils.loadChunkAtLocation(_location);
			if(_location.getBlock().getType().equals(Material.CHEST)) {
				fill(null, new CraftChest(_location.getBlock()));
				Output.sendToAdminIRC(null, "DC_Debug", "Dungeon Chest at ("+_location.getX()+","+_location.getBlockY()+","+_location.getBlockZ()+") filling.");
			}
			else {
				Output.sendToAdminIRC(null, "DC_Debug", "Dungeon Chest at ("+_location.getX()+","+_location.getBlockY()+","+_location.getBlockZ()+") no longer a chest, removed from DB.");
				Database.removeRandChest(this);
				_dead = true;
			}
		}
	}
}
