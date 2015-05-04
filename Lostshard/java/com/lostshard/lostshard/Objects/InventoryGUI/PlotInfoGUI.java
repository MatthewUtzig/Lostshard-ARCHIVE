package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.event.inventory.InventoryClickEvent;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;

public class PlotInfoGUI extends GUI {

	public PlotInfoGUI(PseudoPlayer pPlayer, Plot plot) {
		super(9, "plot info: " + plot.getName(), pPlayer);
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void optionSelector() {
		// TODO Auto-generated method stub

	}

}
