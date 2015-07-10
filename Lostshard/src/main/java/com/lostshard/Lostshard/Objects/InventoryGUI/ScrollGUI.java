package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Database.Mappers.ScrollMapper;
import com.lostshard.Lostshard.Manager.SpellManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Output;

public class ScrollGUI extends GUI {

	SpellManager sm = SpellManager.getManager();

	public ScrollGUI(PseudoPlayer pPlayer) {
		super("Scrolls", pPlayer);
		final List<Scroll> scrolls = new ArrayList<Scroll>();
		for (final Scroll s : this.getPlayer().getScrolls())
			if (!scrolls.contains(s))
				scrolls.add(s);
		GUIItem[] items = new GUIItem[scrolls.size()];
		for (int i=0; i<scrolls.size(); i++) {
			Scroll s = scrolls.get(i);
			final int amount = Collections.frequency(scrolls, s);
			final ItemStack item = new ItemStack(s.getReagentCost().get(0)
					.getType(), amount);
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();

			if (this.getPlayer().getSpellbook().containSpell(s))
				itemMeta.setDisplayName(ChatColor.GREEN + s.getName());
			else
				itemMeta.setDisplayName(ChatColor.RED + s.getName());

			lore.add(ChatColor.GOLD + "Amount: " + amount);

			lore.add(ChatColor.BLUE + "Mana cost: " + s.getManaCost());
			lore.add("You can add the scroll to your spellbook by clicking it");
			lore.add("You can use the scroll by shift clicking it");
			lore.add(ChatColor.GOLD + "Commands");
			lore.add("/scrolls use " + ChatColor.RED + "(scroll)");
			lore.add("/scrolls give " + ChatColor.RED + "(scroll)");
			lore.add("/scrolls spellbook " + ChatColor.RED + "(scroll)");
			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, new GUIClick() {
				
				@Override
				public void click(Player player, PseudoPlayer pPlayer, ItemStack item,
						ClickType click, Inventory inv, int slot) {
					if (click.equals(ClickType.LEFT)) {
						final Scroll scroll = s;
						if (pPlayer.getSpellbook().containSpell(scroll))
							return;
						pPlayer.addSpell(scroll);
						pPlayer.getScrolls().remove(scroll);
						ScrollMapper.deleteScroll(scroll, pPlayer.getId());
						pPlayer.update();
						Output.positiveMessage(player,
								"You have transferred " + scroll.getName()
										+ " to your spellbook.");
						openInventory(player);
					} else if (click.equals(ClickType.SHIFT_LEFT)) {
						final Scroll scroll = s;
						if (scroll == null || !pPlayer.getScrolls().contains(scroll))
							return;
						if (sm.useScroll(player, scroll)) {
							pPlayer.getScrolls().remove(scroll);
							ScrollMapper.deleteScroll(scroll, pPlayer.getId());
							forceClose();
						}
					}
				}
			});
		}
		setItems(items);
	}
}
