package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Store.Store;
import com.lostshard.Lostshard.Objects.Store.StoreItem;

public class StoreGUI extends GUI {

	private final Store store;

	public StoreGUI(PseudoPlayer pPlayer, Store store) {
		super("Store", pPlayer);
		this.store = store;
		this.optionSelector();
	}

	@Override
	public void onClick(InventoryClickEvent event) {

	}

	@Override
	public void optionSelector() {
		for (final StoreItem i : this.store.getItems()) {
			final ItemStack item = i.getItem().clone();
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();

			if (i.getStock() > 0)
				itemMeta.setDisplayName(ChatColor.RED
						+ item.getItemMeta().getDisplayName());
			else
				itemMeta.setDisplayName(ChatColor.GREEN
						+ i.getItem().getItemMeta().getDisplayName());
			lore.add("Sale price: "
					+ (i.getSalePrice() > 0 ? i.getSalePrice() : "not for sale"));
			lore.add("Buy price: "
					+ (i.getSalePrice() > 0 ? i.getSalePrice() : "not for buy"));
			lore.add("Stock: " + i.getStock());
			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
			super.addOption(item, new GUIClick() {

				@Override
				public void click(Player player, PseudoPlayer pPlayer,
						ItemStack item, ClickType click, Inventory inv, int slot) {
					
				}
			});
		}
	}
}
