package com.lostshard.RPG.Events;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;

import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Utils.Utils;

public class MeteorLandEvent extends WorldEvent{
	private Location _location;
	private int _numberOfGhasts;
	private int _size;
	
	private ArrayList<LivingEntity> _spawnedGhasts = new ArrayList<LivingEntity>();
	
	public MeteorLandEvent(Location location, int size) {
		_location = location;
		_size = size;
	}
	
	public ArrayList<LivingEntity> getGhasts() {
		return _spawnedGhasts;
	}
	
	@Override
	public String getDescription() {
		return "A meteor has landed.";
	}
	
	@Override
	public void start() {
		broadcast("A meteor has landed.");
		Location loc = _location;
		for(int x=loc.getBlockX()-_size; x<loc.getBlockX()+_size; x++) {
			for(int y=loc.getBlockY()-_size; y<loc.getBlockY()+_size; y++) {
				for(int z=loc.getBlockZ()-_size; z<loc.getBlockZ()+_size; z++) {
					Block b = loc.getWorld().getBlockAt(x,y,z);
					if(Utils.isWithin(loc,  b.getLocation(), _size-1)) {
						loc.getWorld().getBlockAt(x,y,z).setType(Material.AIR);
					}
				}
			}
		}
		loc.setY(loc.getY()-2);
		for(int x=loc.getBlockX()-(_size-2); x<loc.getBlockX()+(_size-2); x++) {
			for(int y=loc.getBlockY()-(_size-2); y<loc.getBlockY()+(_size-2); y++) {
				for(int z=loc.getBlockZ()-(_size-2); z<loc.getBlockZ()+(_size-2); z++) {
					Block b = loc.getWorld().getBlockAt(x,y,z);
					if(Utils.isWithin(loc,  b.getLocation(), _size-3)) {
						loc.getWorld().getBlockAt(x,y,z).setType(Material.OBSIDIAN);
					}
				}
			}
		}
		cleanUp();
	}
	
	@Override
	public void finish() {
	}
	
	@Override
	public void tick() {		
	}

}