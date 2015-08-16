package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.PseudoPlayer;

public class PlotInfoGUI extends GUI {
	public PlotInfoGUI(PseudoPlayer pPlayer, Plot plot) {
		super("plot info: " + plot.getName(), pPlayer, new GUIItem[0]);
	}
}
