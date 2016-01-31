package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.Rune;
import com.lostshard.Lostshard.Objects.Player.Runebook;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Utils.Output;

public class RunebookGUI extends GUI {
	public RunebookGUI(PseudoPlayer pPlayer) {
		super("Runebook", pPlayer, new GUIItem[0]);
		final List<Rune> runes = new ArrayList<Rune>();
		runes.add(new Rune(null, "random", -1));
		runes.add(new Rune(this.getPlayer().getSpawn(), "spawn", -1));
		runes.addAll(this.getPlayer().getRunebook().getRunes());
		final GUIItem[] items = new GUIItem[runes.size()];
		for (int i = 0; i < runes.size(); i++) {
			final Rune r = runes.get(i);
			final ItemStack item = new ItemStack(Material.PAPER);
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();

			itemMeta.setDisplayName(ChatColor.GOLD + r.getLabel());
			if (r.getLocation() != null) {
				lore.add(ChatColor.YELLOW + "XYZ: " + r.getLocation().getBlockX() + "/" + r.getLocation().getBlockY()
						+ "/" + r.getLocation().getBlockZ());
			} else {
				lore.add(ChatColor.YELLOW + "Random location.");
			}
			lore.add("You can remove the rune by shift right clicking it.");
			lore.add(ChatColor.GOLD + "Commands");
			lore.add("/runebook give " + ChatColor.RED + "(player) (rune)");

			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, (player, pPlayer1, item1, click, inv, slot) -> {
				if (pPlayer1.getPromptedSpell() != null && click.equals(ClickType.LEFT)) {
					final Spell spell = pPlayer1.getPromptedSpell();
					spell.setResponse(ChatColor.stripColor(item1.getItemMeta().getDisplayName()));
					final int castingDelay = spell.getCastingDelay();
					if (castingDelay > 0) {
						spell.preAction(player);
						pPlayer1.getTimer().delayedSpell = spell;
					} else {
						spell.preAction(player);
						pPlayer1.getTimer().cantCastTicks = spell.getCooldown();
						spell.doAction(player);
					}
					pPlayer1.setPromptedSpell(null);
					RunebookGUI.this.forceClose();
				} else if (click.equals(ClickType.SHIFT_RIGHT)) {
					final Runebook runebook = pPlayer1.getRunebook();
					final Rune rune = r;

					runebook.removeRune(rune);
					pPlayer1.update();
					RunebookGUI.this.forceClose();
					Output.positiveMessage(player, "You have removed the rune \""
							+ ChatColor.stripColor(item1.getItemMeta().getDisplayName()) + "\" from your runebook.");
				}
			});
		}
		this.setItems(items);
	}
}
