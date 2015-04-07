package com.lostshard.lostshard.Handlers;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.Plot.Capturepoint;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Objects.Plot.PlotCapturePoint;
import com.lostshard.lostshard.Utils.Utils;

public class CapturepointHandler {

	static PlotManager ptm = PlotManager.getManager();
	
	public static void tick(double delta) {
		for(Plot plot : ptm.getPlots())
			if(plot instanceof PlotCapturePoint)
				((PlotCapturePoint)plot).tick(delta);
	}
	
	public static void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Plot plot = ptm.findPlotAt(player.getLocation());
		if(plot instanceof PlotCapturePoint) {
			Capturepoint cp = Capturepoint.getByName(plot.getName());
			if(cp != null) {
			    for(Plot cP : ptm.getPlots()) {
			    	if(cP instanceof PlotCapturePoint) {
						if(!Utils.isWithin(player.getLocation(), new Location(plot.getLocation().getWorld(), cp.getPoint().x, cp.getPoint().y, cp.getPoint().z), 10)) {
			    			((PlotCapturePoint)cP).failCaptureLeft(player);
			    		}
			    	}
		    	}
			}else{
				for(Plot cP : ptm.getPlots()) {
			    	if(!(cP instanceof PlotCapturePoint))
					if(!Utils.isWithin(player.getLocation(), plot.getLocation(), 10)) {
		    			((PlotCapturePoint)cP).failCaptureLeft(player);
		    		}
			    }
		    }
		}
	}

	public static void onPlayerQuit(PlayerQuitEvent event) {
		for(Plot plot : ptm.getPlots())
			if(plot instanceof PlotCapturePoint)
				((PlotCapturePoint)plot).failCaptureLeft(event.getPlayer());
	}
}
