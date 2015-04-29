package com.lostshard.lostshard.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.lostshard.lostshard.Objects.PseudoPlayer;

public class PlotCreateEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Player player;
	private PseudoPlayer pPlayer;
	private Location locatin;
	private String plotName;
	
	public PlotCreateEvent(Player player, PseudoPlayer pPlayer, Location location, String plotName) {
		this.player = player;
		this.pPlayer = pPlayer;
		this.locatin = location;
		this.plotName = plotName;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PseudoPlayer getpPlayer() {
		return pPlayer;
	}

	public void setpPlayer(PseudoPlayer pPlayer) {
		this.pPlayer = pPlayer;
	}

	public Location getLocatin() {
		return locatin;
	}

	public void setLocatin(Location locatin) {
		this.locatin = locatin;
	}

	public String getPlotName() {
		return plotName;
	}

	public void setPlotName(String plotName) {
		this.plotName = plotName;
	}
}
