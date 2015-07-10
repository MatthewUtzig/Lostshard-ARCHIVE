package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Store.Store;
import com.lostshard.Lostshard.Objects.Store.StoreItem;

public class StoreGUI extends GUI {

	public StoreGUI(PseudoPlayer pPlayer, Store store) {
		super("Store", pPlayer);
		GUIItem[] items = new GUIItem[store.getItems().size()];
		for (int i=0; i<store.getItems().size(); i++) {
			StoreItem si = store.getItems().get(i);
			final ItemStack item = si.getItem().clone();
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();

			if (si.getStock() > 0)
				itemMeta.setDisplayName(ChatColor.RED
						+ item.getItemMeta().getDisplayName());
			else
				itemMeta.setDisplayName(ChatColor.GREEN
						+ si.getItem().getItemMeta().getDisplayName());
			lore.add("Sale price: "
					+ (si.getSalePrice() > 0 ? si.getSalePrice() : "not for sale"));
			lore.add("Buy price: "
					+ (si.getSalePrice() > 0 ? si.getSalePrice() : "not for buy"));
			lore.add("Stock: " + si.getStock());
			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, new GUIClick() {

				@Override
				public void click(Player player, PseudoPlayer pPlayer,
						ItemStack item, ClickType click, Inventory inv, int slot) {
					
				}
			});
		}
		setItems(items);
	}
}
