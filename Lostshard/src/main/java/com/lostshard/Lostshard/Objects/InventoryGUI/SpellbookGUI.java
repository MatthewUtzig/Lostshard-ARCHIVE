package com.lostshard.Lostshard.Objects.InventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.SpellBook;

public class SpellbookGUI extends GUI {

	public SpellbookGUI(PseudoPlayer pPlayer) {
		super("Spellbook", pPlayer);
		final SpellBook spellbook = this.getPlayer().getSpellbook();
		GUIItem[] items = new GUIItem[spellbook.getSpells().size()];
		for (int i = 1; i < 10; i++) {
			int index = i;
			final ItemStack item = new ItemStack(Material.PAPER);
			final ItemMeta itemMeta = item.getItemMeta();
			if (spellbook.getSpellsOnPage(i).isEmpty())
				itemMeta.setDisplayName(ChatColor.RED + "Spellbook page: " + i);
			else
				itemMeta.setDisplayName(ChatColor.GOLD + "Spellbook page: " + i);
			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, new GUIClick() {
				
				@Override
				public void click(Player player, PseudoPlayer pPlayer, ItemStack item,
						ClickType click, Inventory inv, int slot) {
					final GUI pageGUI = new SpellbookPageGUI(pPlayer, index);
					pageGUI.openInventory(player);
				}
			});
		}
	}
}