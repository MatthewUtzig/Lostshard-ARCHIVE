package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.SpellBook;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpellbookGUI extends GUI {
	public SpellbookGUI(PseudoPlayer pPlayer) {
		super("Spellbook", pPlayer, new GUIItem[0]);
		SpellBook spellbook = getPlayer().getSpellbook();
		GUIItem[] items = new GUIItem[9];
		for (int i = 0; i < 9; i++) {
			final int index = i;
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = item.getItemMeta();
			if (spellbook.getSpellsOnPage(i + 1).isEmpty()) {
				itemMeta.setDisplayName(ChatColor.RED + "Spellbook page: " + (i + 1));
			} else {
				itemMeta.setDisplayName(ChatColor.GOLD + "Spellbook page: " + (i + 1));
			}
			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, new GUIClick() {
				public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
						int slot) {
					GUI pageGUI = new SpellbookPageGUI(pPlayer, index + 1);
					pageGUI.openInventory(player);
				}
			});
		}
		setItems(items);
	}
}
