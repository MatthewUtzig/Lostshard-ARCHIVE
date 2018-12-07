package com.lostshard.lostshard.Objects.InventoryGUI;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.plot.Plot;

public class PlotInfoGUI extends GUI {
	public PlotInfoGUI(PseudoPlayer pPlayer, Plot plot) {
		super("plot info: " + plot.getName(), pPlayer, new GUIItem[0]);
	}
}
