package com.lostshard.lostshard.Handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.Plot.Capturepoint;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Objects.Plot.PlotCapturePoint;
import com.lostshard.lostshard.Utils.Utils;

public class CapturepointHandler {

	public static void onPlayerDie(PlayerDeathEvent event) {
		Player player = event.getEntity();
		for(Plot plot : ptm.getPlots())
			if(plot instanceof PlotCapturePoint)
				((PlotCapturePoint)plot).failCaptureLeft(player);
	}
	
	public static void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Plot fromPlot = ptm.findPlotAt(event.getFrom());
		if(fromPlot == null)
			return;
		if(!(fromPlot instanceof PlotCapturePoint))
			return;
		Plot toPlot = ptm.findPlotAt(event.getTo());
		if(toPlot == null)
			return;
		if(!(toPlot instanceof PlotCapturePoint)) {
			((PlotCapturePoint)fromPlot).failCaptureLeft(player);
			return;
		}
		Capturepoint cp = Capturepoint.getByName(fromPlot.getName());
		if(cp != null) {
			if(!Utils.isWithin(player.getLocation(), new Location(fromPlot.getLocation().getWorld(), cp.getPoint().x, cp.getPoint().y, cp.getPoint().z), 5)) {
    			((PlotCapturePoint)fromPlot).failCaptureLeft(player);
	    	}
		}else{
			if(!Utils.isWithin(player.getLocation(), fromPlot.getLocation(), 5)) {
    			((PlotCapturePoint)fromPlot).failCaptureLeft(player);
    		}
	    }
	}
	
	public static void onPlayerQuit(PlayerQuitEvent event) {
		for(Plot plot : ptm.getPlots())
			if(plot instanceof PlotCapturePoint)
				((PlotCapturePoint)plot).failCaptureLeft(event.getPlayer());
	}

	public static void tick(double delta) {
		for(Plot plot : ptm.getPlots())
			if(plot instanceof PlotCapturePoint)
				((PlotCapturePoint)plot).tick(delta);
	}
	
	static PlotManager ptm = PlotManager.getManager();
}
