package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.SpellBook;

public class SpellbookGUI extends GUI {

	public SpellbookGUI(PseudoPlayer pPlayer) {
		super(9, "Spellbook", pPlayer);
		optionSelector();
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getCurrentItem().getItemMeta().hasDisplayName()) {
			int page = -1;
			try {
				page = Integer.parseInt(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).replace("Spellbook page: ",""));
			}catch(Exception e) {
				
			}
			GUI pageGUI = new SpellbookPageGUI(getPlayer(), page);
			pageGUI.openInventory((Player)event.getWhoClicked());
		}
	}

	@Override
	public void optionSelector() {
		SpellBook spellbook = getPlayer().getSpellbook();
		for(int i=1; i<10; i++) {
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = item.getItemMeta();
			if(spellbook.getSpellsOnPage(i).isEmpty())
				itemMeta.setDisplayName(ChatColor.RED+"Spellbook page: "+i);
			else
				itemMeta.setDisplayName(ChatColor.GOLD+"Spellbook page: "+i);
			item.setItemMeta(itemMeta);
			addOption(item);
		}
	}
}
