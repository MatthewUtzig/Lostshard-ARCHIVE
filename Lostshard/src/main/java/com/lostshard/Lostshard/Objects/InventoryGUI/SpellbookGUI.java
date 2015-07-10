package com.lostshard.Lostshard.Objects.InventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.SpellBook;

public class SpellbookGUI extends GUI {

	public SpellbookGUI(PseudoPlayer pPlayer) {
		super("Spellbook", pPlayer);
		this.optionSelector();
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if (event.getCurrentItem().getItemMeta().hasDisplayName()) {
			int page = -1;
			try {
				page = Integer.parseInt(ChatColor.stripColor(
						event.getCurrentItem().getItemMeta().getDisplayName())
						.replace("Spellbook page: ", ""));
			} catch (final Exception e) {

			}
			final GUI pageGUI = new SpellbookPageGUI(this.getPlayer(), page);
			pageGUI.openInventory((Player) event.getWhoClicked());
		}
	}

	@Override
	public void optionSelector() {
		final SpellBook spellbook = this.getPlayer().getSpellbook();
		for (int i = 1; i < 10; i++) {
			final ItemStack item = new ItemStack(Material.PAPER);
			final ItemMeta itemMeta = item.getItemMeta();
			if (spellbook.getSpellsOnPage(i).isEmpty())
				itemMeta.setDisplayName(ChatColor.RED + "Spellbook page: " + i);
			else
				itemMeta.setDisplayName(ChatColor.GOLD + "Spellbook page: " + i);
			item.setItemMeta(itemMeta);
			this.addOption(item);
		}
	}
}
