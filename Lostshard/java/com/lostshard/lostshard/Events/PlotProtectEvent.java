package com.lostshard.lostshard.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.lostshard.lostshard.Objects.Plot.Plot;

public class PlotProtectEvent extends Event implements Cancellable {
		private static final HandlerList handlers = new HandlerList();
		private boolean cancelled;
		
		private Event protectEvent;
		private Plot plot;
		
		public PlotProtectEvent(Event event, Plot plot) {
			this.protectEvent = event;
			this.plot = plot;
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

		public Event getProtectEvent() {
			return protectEvent;
		}

		public void setProtectEvent(Event protectEvent) {
			this.protectEvent = protectEvent;
		}

		public Plot getPlot() {
			return plot;
		}

		public void setPlot(Plot plot) {
			this.plot = plot;
		}
}
