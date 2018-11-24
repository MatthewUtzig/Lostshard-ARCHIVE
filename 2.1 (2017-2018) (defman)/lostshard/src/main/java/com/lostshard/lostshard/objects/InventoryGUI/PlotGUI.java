package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.Material;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.plot.Plot;

public class PlotGUI extends GUI {
	private Plot plot;

	public PlotGUI(PseudoPlayer pPlayer, Plot plot) {
		super(plot.getName(), pPlayer,
				new GUIItem[] { new GUIItem("plot info", Material.STONE, (player, pPlayer1, item, click, inv, slot) -> {
				}), new GUIItem("Expand", Material.STONE, (player, pPlayer1, item, click, inv, slot) -> {
				}), new GUIItem("Shrink", Material.STONE, (player, pPlayer1, item, click, inv, slot) -> {
				}), new GUIItem("Upgrade", Material.STONE, (player, pPlayer1, item, click, inv, slot) -> {
				}) });
		this.setPlot(plot);
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}
}
