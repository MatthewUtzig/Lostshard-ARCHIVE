package com.lostshard.lostshard.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.lostshard.lostshard.plot.Plot;

public class PlotProtectEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private boolean cancelled;
	private Event protectEvent;

	private Plot plot;

	public PlotProtectEvent(Event event, Plot plot) {
		this.protectEvent = event;
		this.plot = plot;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Plot getPlot() {
		return this.plot;
	}

	public Event getProtectEvent() {
		return this.protectEvent;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	public void setProtectEvent(Event protectEvent) {
		this.protectEvent = protectEvent;
	}
}
