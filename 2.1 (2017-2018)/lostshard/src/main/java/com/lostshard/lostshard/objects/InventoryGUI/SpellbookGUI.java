package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Objects.Player.SpellBook;

public class SpellbookGUI extends GUI {
	public SpellbookGUI(PseudoPlayer pPlayer) {
		super("Spellbook", pPlayer, new GUIItem[0]);
		final SpellBook spellbook = this.getPlayer().getSpellbook();
		final GUIItem[] items = new GUIItem[9];
		for (int i = 0; i < 9; i++) {
			final int index = i;
			final ItemStack item = new ItemStack(Material.PAPER);
			final ItemMeta itemMeta = item.getItemMeta();
			if (spellbook.getSpellsOnPage(i + 1).isEmpty()) {
				itemMeta.setDisplayName(ChatColor.RED + "Spellbook page: " + (i + 1));
			} else {
				itemMeta.setDisplayName(ChatColor.GOLD + "Spellbook page: " + (i + 1));
			}
			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, (player, pPlayer1, item1, click, inv, slot) -> {
				final GUI pageGUI = new SpellbookPageGUI(pPlayer1, index + 1);
				pageGUI.openInventory(player);
			});
		}
		this.setItems(items);
	}
}
