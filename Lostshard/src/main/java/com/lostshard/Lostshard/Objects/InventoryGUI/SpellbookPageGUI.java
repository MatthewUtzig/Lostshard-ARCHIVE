package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.SpellBook;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpellbookPageGUI extends GUI {
	public SpellbookPageGUI(PseudoPlayer pPlayer, int page) {
		super("Spellbook page: " + page, pPlayer, new GUIItem[0]);
		SpellBook spellbook = getPlayer().getSpellbook();
		ItemStack pageBack = new ItemStack(Material.BOOK);
		ItemMeta pageBackMeta = pageBack.getItemMeta();
		pageBackMeta.setDisplayName(ChatColor.GOLD + "Back to Spellbook.");
		pageBack.setItemMeta(pageBackMeta);
		List<Scroll> scrolls = spellbook.getSpellsOnPage(page);
		GUIItem[] items = new GUIItem[scrolls.size() + 1];
		items[0] = new GUIItem(pageBack, new GUIClick() {
			public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
					int slot) {
				GUI spellbookGUI = new SpellbookGUI(pPlayer);
				spellbookGUI.openInventory(player);
			}
		});
		for (int i = 0; i < scrolls.size(); i++) {
			Scroll s = (Scroll) scrolls.get(i);
			ItemStack item = new ItemStack(((ItemStack) s.getReagentCost().get(0)).getType());
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			itemMeta.setDisplayName(ChatColor.GOLD + s.getName());
			int magery = getPlayer().getCurrentBuild().getMagery().getLvl();
			if (magery < s.getMinMagery()) {
				lore.add(ChatColor.RED + "Minimum magery: " + Utils.scaledIntToString(s.getMinMagery()));
			} else {
				lore.add(ChatColor.GREEN + "Minimum magery: " + Utils.scaledIntToString(s.getMinMagery()));
			}
			lore.add(ChatColor.BLUE + "Mana cost: " + s.getManaCost());
			lore.add(ChatColor.GOLD + "Reagent Cost");
			for (ItemStack it : s.getReagentCost()) {
				lore.add(it.getAmount() + " " + it.getType().name().toLowerCase().replace("_", " "));
			}
			lore.add(ChatColor.GOLD + "Commands");
			lore.add("/cast " + ChatColor.RED + "(spell)");
			lore.add("/bind " + ChatColor.RED + "(spell)");
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			items[(i + 1)] = new GUIItem(item);
		}
		setItems(items);
	}
}
