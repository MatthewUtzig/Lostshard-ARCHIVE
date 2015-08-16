package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlotGUI extends GUI {
	private Plot plot;

	public PlotGUI(PseudoPlayer pPlayer, Plot plot) {
		super(plot.getName(), pPlayer, new GUIItem[] { new GUIItem("Plot info", Material.STONE, new GUIClick() {
			public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
					int slot) {
			}
		}), new GUIItem("Expand", Material.STONE, new GUIClick() {
			public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
					int slot) {
			}
		}), new GUIItem("Shrink", Material.STONE, new GUIClick() {
			public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
					int slot) {
			}
		}), new GUIItem("Upgrade", Material.STONE, new GUIClick() {
			public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
					int slot) {
			}
		}) });
		setPlot(plot);
	}

	public Plot getPlot() {
		return this.plot;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}
}
