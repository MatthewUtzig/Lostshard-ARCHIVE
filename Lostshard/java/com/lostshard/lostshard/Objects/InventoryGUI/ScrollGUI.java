package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;

public class ScrollGUI extends GUI {

	SpellManager sm = SpellManager.getManager();

	public ScrollGUI(PseudoPlayer pPlayer) {
		super(36, "Scrolls", pPlayer);
		this.optionSelector();
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if (event.getCurrentItem().getItemMeta().hasDisplayName()
				&& event.getAction().equals(InventoryAction.PICKUP_ALL)) {
			final Player player = (Player) event.getWhoClicked();
			final Scroll scroll = Scroll.getByString(ChatColor.stripColor(event
					.getCurrentItem().getItemMeta().getDisplayName()));
			if (scroll == null)
				return;
			if (this.getPlayer().getSpellbook().containSpell(scroll))
				return;
			this.getPlayer().addSpell(scroll);
			Database.deleteScroll(scroll, this.getPlayer().getId());
			this.getPlayer().update();
			Output.positiveMessage(player,
					"You have transferred " + scroll.getName()
							+ " to your spellbook.");
			this.forceClose();
		} else if (event.getCurrentItem().getItemMeta().hasDisplayName()
				&& event.getAction().equals(
						InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			final Scroll scroll = Scroll.getByString(ChatColor.stripColor(event
					.getCurrentItem().getItemMeta().getDisplayName()));
			final Player player = (Player) event.getWhoClicked();
			if (scroll == null
					|| !this.getPlayer().getScrolls().contains(scroll))
				return;
			if (this.sm.useScroll(player, scroll)) {
				this.getPlayer().getScrolls().remove(scroll);
				Database.deleteScroll(scroll, this.getPlayer().getId());
				this.forceClose();
			}
		}
	}

	@Override
	public void optionSelector() {
		final List<Scroll> scrolls = new ArrayList<Scroll>();
		for (final Scroll s : this.getPlayer().getScrolls())
			if (!scrolls.contains(s))
				scrolls.add(s);
		for (final Scroll s : scrolls) {
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
			this.addOption(item);
		}
	}
}
