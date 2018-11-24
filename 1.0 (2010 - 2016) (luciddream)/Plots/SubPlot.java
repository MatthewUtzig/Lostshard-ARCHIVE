package com.lostshard.RPG.Plots;

import org.bukkit.Location;

public class SubPlot {
	int _id;
	String _name;
	Location _location;
	int _radius;
	String _ownerName;
	int _owningPlotId;
	String _type;
	Plot _owningPlot;
	
	public SubPlot(String name, Location location, int radius, String ownerName, int owningPlotId, String type) {
		_name = name;
		_location = location;
		_radius = radius;
		_ownerName = ownerName;
		_owningPlotId = owningPlotId;
		_type = type;
	}
	
	public void setOwningPlot(Plot plot) {
		_owningPlot = plot;
	}
	
	public Plot getOwningPlot() {
		return _owningPlot;
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public int getId() {
		return _id;
	}
}
