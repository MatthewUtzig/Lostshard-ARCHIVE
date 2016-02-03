package com.lostshard.Lostshard.Manager;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Objects.Player.OfflineMessage;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Recorders.GoldRecord;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;

public class PlotManager {

	static PlotManager manager = new PlotManager();

	public static PlotManager getManager() {
		return manager;
	}

	private List<Plot> plots = new LinkedList<Plot>();

	public PlotManager() {
	}

	/**
	 * @param location
	 * @return
	 *
	 * 		Find plot at location.
	 */
	public Plot findPlotAt(Location location) {
		return this.findPlotAt(location, 0);
	}

	/**
	 * @param location
	 * @param buffer
	 * @return Plot
	 *
	 *         Find plot at location
	 */
	public Plot findPlotAt(Location location, int buffer) {
		for (final Plot plot : this.plots)
			if (Utils.isWithin(plot.getLocation(), location, plot.getSize() + buffer))
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
		for (final Plot plot : this.plots)
			if (plot.getId() == id)
				return plot;
		return null;
	}

	public Plot getPlot(String name) {
		for (final Plot p : this.plots)
			if (StringUtils.startsWithIgnoreCase(p.getName(), name))
				return p;
		return null;
	}

	public List<Plot> getPlots() {
		return this.plots;
	}

	public void removePlot(Plot plot) {
		this.plots.remove(plot);
		plot.delete();
	}

	public void setPlots(List<Plot> plots) {
		this.plots = plots;
	}

	public void tax() {
		Bukkit.broadcastMessage(ChatColor.GREEN + "Tax have been collected.");
		for (final Plot plot : this.plots)
			if (plot.getMoney() < plot.getTax()) {
				plot.setSize(plot.getSize() - 1);
				final Player player = Bukkit.getPlayer(plot.getOwner());
				if (plot.getSize() > 1) {
					if (player != null)
						Output.simpleError(player, plot.getName() + " have failed to pay tax and have shrunk.");
					else
						new OfflineMessage(plot.getOwner(),
								plot.getName() + " have failed to pay tax and have shrunk.");
				} else {
					plot.disband();
					if (player != null)
						Output.simpleError(player, plot.getName() + " have failed to pay tax and are now disbaneded.");
					else
						new OfflineMessage(plot.getOwner(),
								plot.getName() + " have failed to pay tax and are now disbaneded.");
				}
			} else {
				plot.setMoney(plot.getMoney() - plot.getTax());
				new GoldRecord(plot.getTax(), "plot tax", null, null);
			}
	}

	public List<Plot> getCapturePoints() {
		List<Plot> results = new LinkedList<Plot>(this.getPlots());
		results.removeIf(p -> !p.isCapturepoint());
		return results;
	}
}
