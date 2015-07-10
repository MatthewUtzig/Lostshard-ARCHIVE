package com.lostshard.Lostshard.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.lostshard.Lostshard.Objects.PseudoPlayer;

public class PlotCreateEvent extends Event implements Cancellable {

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Player player;
	private PseudoPlayer pPlayer;
	private Location locatin;

	private String plotName;

	public PlotCreateEvent(Player player, PseudoPlayer pPlayer,
			Location location, String plotName) {
		this.player = player;
		this.pPlayer = pPlayer;
		this.locatin = location;
		this.plotName = plotName;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Location getLocatin() {
		return this.locatin;
	}

	public Player getPlayer() {
		return this.player;
	}

	public String getPlotName() {
		return this.plotName;
	}

	public PseudoPlayer getpPlayer() {
		return this.pPlayer;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public void setLocatin(Location locatin) {
		this.locatin = locatin;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setPlotName(String plotName) {
		this.plotName = plotName;
	}

	public void setpPlayer(PseudoPlayer pPlayer) {
		this.pPlayer = pPlayer;
	}
}
