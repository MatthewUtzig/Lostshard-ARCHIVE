package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.SpellBook;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Utils;

public class SpellbookPageGUI extends GUI {

	private int page;

	public SpellbookPageGUI(PseudoPlayer pPlayer, int page) {
		super("Spellbook page: " + page, pPlayer);
		this.page = page;
		this.optionSelector();
	}

	public int getPage() {
		return this.page;
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if (event.getCurrentItem().getItemMeta().hasDisplayName())
			if (event.getCurrentItem().getItemMeta().getDisplayName()
					.equals(ChatColor.GOLD + "Back to Spellbook.")) {
				final GUI spellbookGUI = new SpellbookGUI(this.getPlayer());
				spellbookGUI.openInventory((Player) event.getWhoClicked());
			}
	}

	@Override
	public void optionSelector() {
		final SpellBook spellbook = this.getPlayer().getSpellbook();
		final ItemStack pageBack = new ItemStack(Material.BOOK);
		final ItemMeta pageBackMeta = pageBack.getItemMeta();
		pageBackMeta.setDisplayName(ChatColor.GOLD + "Back to Spellbook.");
		pageBack.setItemMeta(pageBackMeta);
		this.addOption(pageBack);
		for (final Scroll s : spellbook.getSpellsOnPage(this.page)) {
			final ItemStack item = new ItemStack(s.getReagentCost().get(0)
					.getType());
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();
			itemMeta.setDisplayName(ChatColor.GOLD + s.getName());
			final int magery = this.getPlayer().getCurrentBuild().getMagery()
					.getLvl();
			if (magery < s.getMinMagery())
				lore.add(ChatColor.RED + "Minimum magery: "
						+ Utils.scaledIntToString(s.getMinMagery()));
			else
				lore.add(ChatColor.GREEN + "Minimum magery: "
						+ Utils.scaledIntToString(s.getMinMagery()));
			lore.add(ChatColor.BLUE + "Mana cost: " + s.getManaCost());
			lore.add(ChatColor.GOLD + "Reagent Cost");
			for (final ItemStack i : s.getReagentCost())
				lore.add(i.getAmount() + " "
						+ i.getType().name().toLowerCase().replace("_", " "));
			lore.add(ChatColor.GOLD + "Commands");
			lore.add("/cast " + ChatColor.RED + "(spell)");
			lore.add("/bind " + ChatColor.RED + "(spell)");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			this.addOption(item);
		}
	}

	public void setPage(int page) {
		this.page = page;
	}
}
