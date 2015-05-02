package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class PlotManager {

	public static PlotManager getManager() {
		return manager;
	}
	
	static PlotManager manager = new PlotManager();
	
	private List<Plot> plots = new ArrayList<Plot>();
	
	public PlotManager() {
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

	/**
	 * @param id
	 * @return plot
	 * 
	 *         Get plot from id.
	 */
	public Plot getPlot(int id) {
		for (Plot plot : plots)
			if (plot.getId() == id)
				return plot;
		return null;
	}

	public Plot getPlot(String name) {
		for(Plot p : plots)
			if(StringUtils.startsWithIgnoreCase(p.getName(), name))
				return p;
		return null;
	}
	
	public List<Plot> getPlots() {
		return plots;
	}
	
	public void removePlot(Plot plot) {
		plots.remove(plot);
		Database.deletePlot(plot);
	}
	
	public void setPlots(List<Plot> plots) {
		this.plots = plots;
	}

	public void tax() {
		Bukkit.broadcastMessage(ChatColor.GREEN+"Tax have been collected.");
		for(Plot plot : plots) {
			if(plot.getMoney() < plot.getTax()) {
				plot.setSize(plot.getSize()-1);
				Player player = Bukkit.getPlayer(plot.getOwner());
				if(plot.getSize() > 1) {
					if(player != null) {
						Output.simpleError(player, plot.getName()+" have failed to pay tax and have shurnk.");
					}else{
						Database.insertMessages(plot.getOwner(), plot.getName()+" have failed to pay tax and have shurnk.");
					}
				}else{
					plot.disband();
					if(player != null) {
						Output.simpleError(player, plot.getName()+" have failed to pay tax and are now disbaneded.");
					}else{
						Database.insertMessages(plot.getOwner(), plot.getName()+" have failed to pay tax and are now disbaneded.");
					}
				}
			}else{
				plot.setMoney(plot.getMoney()-plot.getTax());
			}
		}
	}
}
