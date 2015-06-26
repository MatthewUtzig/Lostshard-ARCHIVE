package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Database.Mappers.RuneMapper;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;

public class RunebookGUI extends GUI {

	public RunebookGUI(PseudoPlayer pPlayer) {
		super(pPlayer.getRunebook().getNumRunes(), "Runebook", pPlayer);
		this.optionSelector();
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if (this.getPlayer().getPromptedSpell() != null
				&& event.getCurrentItem().getItemMeta().hasDisplayName()) {
			final Player player = (Player) event.getWhoClicked();
			final Spell spell = this.getPlayer().getPromptedSpell();
			spell.setResponse(ChatColor.stripColor(event.getCurrentItem()
					.getItemMeta().getDisplayName()));
			final int castingDelay = spell.getCastingDelay();
			if (castingDelay > 0) {
				spell.preAction(player);
				this.getPlayer().getTimer().delayedSpell = spell;
			} else {
				spell.preAction(player);
				this.getPlayer().getTimer().cantCastTicks = spell.getCooldown();
				spell.doAction(player);
			}
			this.getPlayer().setPromptedSpell(null);
			this.forceClose();
		} else if (event.getCurrentItem().getItemMeta().hasDisplayName()
				&& event.getAction().equals(
						InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			final Player player = (Player) event.getWhoClicked();
			final Runebook runebook = this.getPlayer().getRunebook();
			final Rune rune = runebook.getRune(ChatColor.stripColor(event
					.getCurrentItem().getItemMeta().getDisplayName()));

			if (rune == null)
				return;

			runebook.removeRune(rune);
			RuneMapper.deleteRune(rune);
			this.forceClose();
			Output.positiveMessage(
					player,
					"You have removed the rune \""
							+ ChatColor.stripColor(event.getCurrentItem()
									.getItemMeta().getDisplayName())
							+ "\" from your runebook.");
		}
	}

	@Override
	public void optionSelector() {
		List<Rune> runes = new ArrayList<Rune>();
		runes.add(new Rune(null, "random", -1));
		runes.add(new Rune(this.getPlayer().getSpawn(), "spawn", -1));
		runes.addAll(this.getPlayer().getRunebook().getRunes());
		for (final Rune r : runes) {
			final ItemStack item = new ItemStack(Material.PAPER);
			final ItemMeta itemMeta = item.getItemMeta();
			final List<String> lore = new ArrayList<String>();

			itemMeta.setDisplayName(ChatColor.GOLD + r.getLabel());
			if(r.getLocation() != null)
				lore.add(ChatColor.YELLOW + "XYZ: " + (r.getLocation().getX()+0.5)+"/"+(r.getLocation().getY()+0.5)+"/"+ (r.getLocation().getZ()+0.5));
			else
				lore.add(ChatColor.YELLOW+"Random location.");
			lore.add("You can remove the rune by shift clicking it");
			lore.add(ChatColor.GOLD + "Commands");
			lore.add("/runebook give " + ChatColor.RED + "(player) (rune)");

			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
			this.addOption(item);
		}
	}
}
