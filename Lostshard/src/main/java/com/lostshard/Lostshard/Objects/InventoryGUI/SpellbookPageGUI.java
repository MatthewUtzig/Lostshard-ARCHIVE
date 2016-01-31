package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.SpellBook;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Utils;

public class SpellbookPageGUI extends GUI {
	public SpellbookPageGUI(PseudoPlayer pPlayer, int page) {
		super("Spellbook page: " + page, pPlayer, new GUIItem[0]);
		final SpellBook spellbook = this.getPlayer().getSpellbook();
		final ItemStack pageBack = new ItemStack(Material.BOOK);
		final ItemMeta pageBackMeta = pageBack.getItemMeta();
		pageBackMeta.setDisplayName(ChatColor.GOLD + "Back to Spellbook.");
		pageBack.setItemMeta(pageBackMeta);
		final List<Scroll> scrolls = spellbook.getSpellsOnPage(page);
		final GUIItem[] items = new GUIItem[scrolls.size() + 1];
		items[0] = new GUIItem(pageBack, (player, pPlayer1, item, click, inv, slot) -> {
			final GUI spellbookGUI = new SpellbookGUI(pPlayer1);
			spellbookGUI.openInventory(player);
		});
		for (int i = 0; i < scrolls.size(); i++) {
			final Scroll s = scrolls.get(i);
			final ItemStack item = new ItemStack(s.getReagentCost().get(0).getType());
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();
			itemMeta.setDisplayName(ChatColor.GOLD + s.getName());
			final int magery = this.getPlayer().getCurrentBuild().getMagery().getLvl();
			if (magery < s.getMinMagery()) {
				lore.add(ChatColor.RED + "Minimum magery: " + Utils.scaledIntToString(s.getMinMagery()));
			} else {
				lore.add(ChatColor.GREEN + "Minimum magery: " + Utils.scaledIntToString(s.getMinMagery()));
			}
			lore.add(ChatColor.BLUE + "Mana cost: " + s.getManaCost());
			lore.add(ChatColor.GOLD + "Reagent Cost");
			for (final ItemStack it : s.getReagentCost()) {
				lore.add(it.getAmount() + " " + it.getType().name().toLowerCase().replace("_", " "));
			}
			lore.add(ChatColor.GOLD + "Commands");
			lore.add("/cast " + ChatColor.RED + "(spell)");
			lore.add("/bind " + ChatColor.RED + "(spell)");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			items[i + 1] = new GUIItem(item);
		}
		this.setItems(items);
	}
}
