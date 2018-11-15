package com.lostshard.RPG.Plots;

import me.neodork.npclib.entity.HumanNPC;

import org.bukkit.Location;

//import com.thevoxelbox.bukkit.port.NPC.BasiBasicHumanNpc;

public class PlotNPC extends RPGNPC{
	private int _id;
	private int _plotId;
	private String _additional;
	private int _storeId;
	
	public PlotNPC(int id, int plotId, String name, Location location, String type, HumanNPC npcEntity, String additional) {
		super(name, name, 0, type, location, npcEntity);
		
		_id = id;
		_plotId = plotId;
		_additional = additional;
		
		
		if(type.equalsIgnoreCase("vendor")) {
			try{
				_storeId = Integer.parseInt(_additional);
			}
			catch(Exception e) {
				_storeId = -1;
				System.out.println("Invalid Store Id Found: " + _storeId);
			}
		}
	}
	
	public int getId() {
		return _id;
	}
	
	public int getPlotId() {
		return _plotId;
	}
	
	public String getAdditional() {
		return _additional;
	}
	
	public int getStoreId() {
		return _storeId;
	}
}
