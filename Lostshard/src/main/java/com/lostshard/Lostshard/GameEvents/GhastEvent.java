package com.lostshard.Lostshard.GameEvents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lostshard.Plots.PlotManager;
import com.lostshard.Plots.Models.Plot;

public class GhastEvent extends WorldEvent {

	private int spawnedGhasts;
	private int timeout;
	private List<LivingEntity> ghasts;

	public GhastEvent(Location location, int size, String title, long start, long stop) {
		super("Ghast attack", location, size, title, start, stop);
		this.ghasts = new ArrayList<LivingEntity>();
	}

	@Override
	public boolean create(Player player) {
		return true;
	}

	@Override
	public boolean finish() {
		this.broadcast("The ghast menace seems to have subsided... for now.");
		this.cleanup();
		return true;
	}

	@Override
	public String getDescription() {
		final Plot plot = PlotManager.getManager().findPlotAt(this.getLocation());
		if (plot != null)
			return "Ghasts are attacking " + plot.getName() + ".";
		else
			return "Ghasts are attacking " + Math.round(this.getLocation().getBlockX()) + ","
					+ Math.round(this.getLocation().getBlockY()) + "," + Math.round(this.getLocation().getBlockZ())
					+ ".";
	}

	public List<LivingEntity> getGhasts() {
		return this.ghasts;
	}

	public int getSpawnedGhasts() {
		return this.spawnedGhasts;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public void setGhasts(List<LivingEntity> ghasts) {
		this.ghasts = ghasts;
	}

	public void setSpawnedGhasts(int spawnedGhasts) {
		this.spawnedGhasts = spawnedGhasts;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public boolean start() {
		final Plot plot = PlotManager.getManager().findPlotAt(this.getLocation());
		if (plot != null)
			this.broadcast("Ghasts have begun raiding " + plot.getName() + "!");
		else
			this.broadcast("Ghasts have begun raiding " + Math.round(this.getLocation().getBlockX()) + ","
					+ Math.round(this.getLocation().getBlockY()) + "," + Math.round(this.getLocation().getBlockZ()));
		return true;
	}

	@Override
	public boolean tick(float delta) {
		return false;
	}

}
