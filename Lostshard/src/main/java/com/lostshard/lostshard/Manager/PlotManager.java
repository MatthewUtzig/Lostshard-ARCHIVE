package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Utils.Utils;

public class PlotManager {

	static PlotManager manager = new PlotManager();
	
	private List<Plot> plots = new ArrayList<Plot>();
	
	public PlotManager() {
	}
	
	public static PlotManager getManager() {
		return manager;
	}

	public List<Plot> getPlots() {
		return plots;
	}

	public void setPlots(List<Plot> plots) {
		this.plots = plots;
	}

	/**
	 * @param location
	 * @return
	 * 
	 *         Find plot at location.
	 */
	public Plot findPlotAt(Location location) {
		return findPlotAt(location, 0);
	}

	/**
	 * @param location
	 * @param buffer
	 * @return Plot
	 * 
	 *         Find plot at location
	 */
	public Plot findPlotAt(Location location, int buffer) {
		for (Plot plot : plots)
			if (Utils.isWithin(plot.getLocation(), location, plot.getSize()
					+ buffer))
				return plot;
			else
				continue;
		return null;
	}
	
	public void removePlot(Plot plot) {
		plots.remove(plot);
	}
	
	/**
	 * @param id
	 * @return plot
	 * 
	 *         Get plot from id.
	 */
	public Plot getPlotById(int id) {
		for (Plot plot : plots)
			if (plot.getId() == id)
				return plot;
		return null;
	}
	
}
