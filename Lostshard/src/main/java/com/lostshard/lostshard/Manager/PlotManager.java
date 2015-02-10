package com.lostshard.lostshard.Manager;

public class PlotManager {

	static PlotManager manager = new PlotManager();
	
	public PlotManager() {
	}
	
	public static PlotManager getManager() {
		return manager;
	}
	
}
