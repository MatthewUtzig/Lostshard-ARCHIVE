package com.lostshard.Lostshard.GameEvents;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Plot.Plot;

public class GhastEvent extends WorldEvent {

	public GhastEvent(Lostshard plugin, Location location, int size, String title, long start, long stop) {
		super(plugin, "Ghast attack", location, size, title, start, stop);
	}

	@Override
	public boolean start() {
		Plot plot = PlotManager.getManager().findPlotAt(getLocation());
		if(plot != null)
			broadcast("Ghasts have begun raiding "+plot.getName()+"!");
		else
			broadcast("Ghasts have begun raiding "+Math.round(getLocation().getBlockX())+","+Math.round(getLocation().getBlockY())+","+Math.round(getLocation().getBlockZ()));
		return true;
	}

	@Override
	public boolean finish() {
		broadcast("The ghast menace seems to have subsided... for now.");
		this.cleanup();
		return true;
	}

	@Override
	public boolean create(Player player, String[] args) {
		try{
			
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean tick(float delta) {
		return false;
	}

	@Override
	public String getDescription() {
		Plot plot = PlotManager.getManager().findPlotAt(getLocation());
		if(plot != null)
			return "Ghasts are attacking "+plot.getName()+".";
		else
			return "Ghasts are attacking "+Math.round(getLocation().getBlockX())+","+Math.round(getLocation().getBlockY())+","+Math.round(getLocation().getBlockZ())+".";
	}

}
