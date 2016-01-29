package com.lostshard.Lostshard.Handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Plot.Capturepoint;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Utils.Utils;

public class CapturepointHandler {

	public static void onPlayerDie(PlayerDeathEvent event) {
		final Player player = event.getEntity();
		for (final Plot plot : ptm.getPlots())
			if (plot.isCapturepoint())
				plot.getCapturepointData().failCaptureLeft(player);
	}

	public static void onPlayerMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final Plot fromPlot = ptm.findPlotAt(event.getFrom());
		if (fromPlot == null)
			return;
		if (!(fromPlot.isCapturepoint()))
			return;
		final Plot toPlot = ptm.findPlotAt(event.getTo());
		if (toPlot == null)
			return;
		if (!(toPlot.isCapturepoint())) {
			fromPlot.getCapturepointData().failCaptureLeft(player);
			return;
		}
		final Capturepoint cp = Capturepoint.getByName(fromPlot.getName());
		if (cp != null) {
			if (!Utils.isWithin(player.getLocation(), new Location(fromPlot
					.getLocation().getWorld(), cp.getPoint().x,
					cp.getPoint().y, cp.getPoint().z), 5))
				fromPlot.getCapturepointData().failCaptureLeft(player);
		} else if (!Utils.isWithin(player.getLocation(),
				fromPlot.getLocation(), 5))
			fromPlot.getCapturepointData().failCaptureLeft(player);
	}

	public static void onPlayerQuit(PlayerQuitEvent event) {
		for (final Plot plot : ptm.getPlots())
			if (plot.isCapturepoint())
				plot.getCapturepointData().failCaptureLeft(event.getPlayer());
	}

	public static void tick(double delta) {
		for (final Plot plot : ptm.getPlots())
			if (plot.isCapturepoint())
				plot.getCapturepointData().tick(delta);
	}

	static PlotManager ptm = PlotManager.getManager();
}
