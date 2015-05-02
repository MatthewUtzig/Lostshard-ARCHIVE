package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;

public class PlotGUI extends GUI {

	private Plot plot;

	public PlotGUI(PseudoPlayer pPlayer, Plot plot) {
		super(9, plot.getName(), pPlayer);
		this.setPlot(plot);
	}

	public Plot getPlot() {
		return this.plot;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if (!event.getCurrentItem().getItemMeta().hasDisplayName())
			return;
		final ItemStack item = event.getCurrentItem();
		final String option = ChatColor.stripColor(item.getItemMeta()
				.getDisplayName());
		final Player player = (Player) event.getWhoClicked();
		if (option.equalsIgnoreCase("Plot info")) {
			final PlotInfoGUI plotInfoGui = new PlotInfoGUI(this.getPlayer(),
					this.getPlot());
			plotInfoGui.openInventory(player);
		} else if (option.equalsIgnoreCase("Expand")) {

		} else if (option.equalsIgnoreCase("Shrink")) {

		} else if (option.equalsIgnoreCase("Upgrade")) {

		}
	}

	@Override
	public void optionSelector() {
		this.addOption("Plot info", Material.STONE);
		this.addOption("Expand", Material.STONE);
		this.addOption("Shrink", Material.STONE);
		this.addOption("Upgrade", Material.STONE);
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}
}
