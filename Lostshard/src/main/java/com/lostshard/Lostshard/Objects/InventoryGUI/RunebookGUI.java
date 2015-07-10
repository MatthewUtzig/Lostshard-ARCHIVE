package com.lostshard.Lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.Lostshard.Database.Mappers.RuneMapper;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Rune;
import com.lostshard.Lostshard.Objects.Runebook;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Utils.Output;

public class RunebookGUI extends GUI {

	public RunebookGUI(PseudoPlayer pPlayer) {
		super("Runebook", pPlayer);
		List<Rune> runes = new ArrayList<Rune>();
		runes.add(new Rune(null, "random", -1));
		runes.add(new Rune(this.getPlayer().getSpawn(), "spawn", -1));
		runes.addAll(this.getPlayer().getRunebook().getRunes());
		GUIItem[] items = new GUIItem[runes.size()];
		for (int i=0; runes.size()<0; i++) {
			Rune r = runes.get(i);
			final ItemStack item = new ItemStack(Material.PAPER);
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();

			itemMeta.setDisplayName(ChatColor.GOLD + r.getLabel());
			if(r.getLocation() != null)
				lore.add(ChatColor.YELLOW + "XYZ: " + (r.getLocation().getBlockX())+"/"+(r.getLocation().getBlockY())+"/"+ (r.getLocation().getBlockZ()));
			else
				lore.add(ChatColor.YELLOW+"Random location.");
			lore.add("You can remove the rune by shift right clicking it.");
			lore.add(ChatColor.GOLD + "Commands");
			lore.add("/runebook give " + ChatColor.RED + "(player) (rune)");

			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, new GUIClick() {
				
				@Override
				public void click(Player player, PseudoPlayer pPlayer, ItemStack item,
						ClickType click, Inventory inv, int slot) {
					if (pPlayer.getPromptedSpell() != null && click.equals(ClickType.LEFT)) {
						final Spell spell = pPlayer.getPromptedSpell();
						spell.setResponse(ChatColor.stripColor(item.getItemMeta().
								getDisplayName()));
						final int castingDelay = spell.getCastingDelay();
						if (castingDelay > 0) {
							spell.preAction(player);
							pPlayer.getTimer().delayedSpell = spell;
						} else {
							spell.preAction(player);
							pPlayer.getTimer().cantCastTicks = spell.getCooldown();
							spell.doAction(player);
						}
						pPlayer.setPromptedSpell(null);
						forceClose();
					} else if (click.equals(ClickType.SHIFT_RIGHT)) {
						Runebook runebook = pPlayer.getRunebook();
						Rune rune = r;
						
						runebook.removeRune(rune);
						RuneMapper.deleteRune(rune);
						forceClose();
						Output.positiveMessage(
								player,
								"You have removed the rune \""
										+ ChatColor.stripColor(item
												.getItemMeta().getDisplayName())
										+ "\" from your runebook.");
					}
				}
			});
		}
	}
}
