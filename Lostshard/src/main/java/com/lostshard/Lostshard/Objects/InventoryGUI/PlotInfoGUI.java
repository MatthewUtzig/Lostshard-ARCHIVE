package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;

public class PlotInfoGUI extends GUI {

	public PlotInfoGUI(PseudoPlayer pPlayer, Plot plot) {
		super("plot info: " + plot.getName(), pPlayer);
	}
}
