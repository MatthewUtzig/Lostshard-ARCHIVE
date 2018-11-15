package com.lostshard.RPG.Events;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;

import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Utils.Utils;

public class GhastAttackEvent extends WorldEvent{
	private Location _location;
	private int _numberOfGhasts;
	private int _range;
	private int _timeout;
	private int _numberOfspawnedGhasts;
	private int _ticksTillNextGhast = 0;
	private ArrayList<LivingEntity> _spawnedGhasts = new ArrayList<LivingEntity>();
	
	public GhastAttackEvent(Location location, int numberOfGhasts, int range, int timeout) {
		_location = location;
		_numberOfGhasts = numberOfGhasts;
		_range = range;
		_timeout = timeout*60*10;
	}
	
	public ArrayList<LivingEntity> getGhasts() {
		return _spawnedGhasts;
	}
	
	@Override
	public String getDescription() {
		Plot plot = PlotHandler.findPlotAt(_location);
		if(plot != null)
			return "Ghasts are attacking "+plot.getName()+".";
		else
			return "Ghasts are attacking "+_location.toString();
	}
	
	@Override
	public void start() {
		Plot plot = PlotHandler.findPlotAt(_location);
		if(plot != null)
			broadcast("Ghasts have begun raiding "+plot.getName()+"!");
		else
			broadcast("Ghasts have begun raiding "+_location.toString()+"!");
	}
	
	@Override
	public void finish() {
		broadcast("The ghast menace seems to have subsided... for now.");
		cleanUp();
	}
	
	@Override
	public void tick() {
		_ticksTillNextGhast--;
		if(_ticksTillNextGhast <= 0) {
			if(_numberOfspawnedGhasts < _numberOfGhasts) {
				_ticksTillNextGhast = ((int)Math.floor((Math.random()*100))) + 100; // 10-20 seconds
				
				boolean valid = false;
				int tries = 0;
				while(valid != true && tries < 100) {
					valid = true;
					double randX = Math.random()*_range;
					if(Math.random()<.5)
						randX = -randX;
					randX = _location.getX()+randX;
					double randZ = Math.random()*_range;
					if(Math.random()<.5)
						randZ = -randZ;
					randZ = _location.getZ()+randZ;
					//double y = _location.getWorld().getHighestBlockYAt((int)randX, (int)randZ);
					double randY = Math.random()*_range;
					randY = _location.getY()+randY;
					Location spawnAtLocation = new Location(_location.getWorld(), randX, randY, randZ);
					World w = spawnAtLocation.getWorld();
					for(int x=-1; x<=1; x++) {
						for(int y=-1; y<=1; y++) {
							for(int z=-1; z<=1; z++) {
								if(w.getBlockTypeIdAt(spawnAtLocation.getBlockX()+x,spawnAtLocation.getBlockY()+y,spawnAtLocation.getBlockZ()+z) != 0) {
									valid = false;
									break;
								}
							}
						}
					}
					if(!valid) {
						tries++;
						continue;
					}
					
					_spawnedGhasts.add(_location.getWorld().spawnCreature(spawnAtLocation, CreatureType.GHAST));
					//System.out.println("Spawning a ghast, total ghasts spawned: " + _spawnedGhasts.size());
					_numberOfspawnedGhasts++;
				}
			}
		}
		
		int numSpawnedGhasts = _spawnedGhasts.size();
		for(int i=numSpawnedGhasts-1; i>=0; i--) {
			LivingEntity le = _spawnedGhasts.get(i);
			if(le.getLocation().getY() > 200 || le.getLocation().getY() < 0) {
				le.damage(100);
			}
			if(Utils.distance(le.getLocation(), _location) > 200) {
				le.damage(100);
			}
			
			Damageable damag = le;
			
			if(le == null || le.isDead() || damag.getHealth() <= 0) {
				_spawnedGhasts.remove(i);
			}
		}
		
		if(_spawnedGhasts.size() <= 0 && _numberOfspawnedGhasts >= _numberOfGhasts) {
			finish();
		}
		
		_timeout--;
		if(_timeout <= 0) {
			finish();
		}
		
	}

}
