package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Plots.Models.Plot;

public class PlotInfoGUI extends GUI {
	public PlotInfoGUI(PseudoPlayer pPlayer, Plot plot) {
		super("plot info: " + plot.getName(), pPlayer, new GUIItem[0]);
	}
}
