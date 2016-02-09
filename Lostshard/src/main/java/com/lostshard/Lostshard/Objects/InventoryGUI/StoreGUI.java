package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Store.Store;
import com.lostshard.Lostshard.Objects.Store.StoreItem;

public class StoreGUI extends GUI {
	public StoreGUI(PseudoPlayer pseudoPlayer, Store store) {
		super("Store", pseudoPlayer, new GUIItem[0]);
		final GUIItem[] items = new GUIItem[store.getItems().size()];
		for (int i = 0; i < store.getItems().size(); i++) {
			final StoreItem si = store.getItems().get(i);
			final ItemStack itemStack = si.getItem().clone();
			final ItemMeta itemMeta = itemStack.getItemMeta();
			final List<String> lore = new ArrayList<String>();
			if (si.getStock() < 0) {
				itemMeta.setDisplayName(ChatColor.RED + (itemStack.getItemMeta().hasDisplayName()
						? itemStack.getItemMeta().getDisplayName() : itemStack.getType().name()));
			} else {
				itemMeta.setDisplayName(ChatColor.GREEN + (itemStack.getItemMeta().hasDisplayName()
						? itemStack.getItemMeta().getDisplayName() : itemStack.getType().name()));
			}
			lore.add("Sale price: " + (si.getSalePrice() > 0 ? Integer.valueOf(si.getSalePrice()) : "not for sale"));
			lore.add("Buy price: " + (si.getSalePrice() > 0 ? Integer.valueOf(si.getSalePrice()) : "not for buy"));
			lore.add("Stock: " + si.getStock());
			lore.add("ID: " + (i + 1));
			itemMeta.setLore(lore);

			itemStack.setItemMeta(itemMeta);
			items[i] = new GUIItem(itemStack, (player, pPlayer, item, click, inv, slot) -> {
				if(pPlayer.getWallet().contains(si.getSalePrice()) && si.getStock() > si.getItem().getAmount()) {
					pPlayer.getWallet().subtract(null, si.getSalePrice(), "Vendor buy");
					si.setStock(si.getStock()-si.getItem().getAmount());
					player.getWorld().dropItem(player.getLocation(), si.getItem());
				}
			});
		}
		this.setItems(items);
	}
}
