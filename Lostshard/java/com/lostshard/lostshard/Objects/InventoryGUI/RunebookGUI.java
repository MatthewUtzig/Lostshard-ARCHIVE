package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;

public class RunebookGUI extends GUI {

	public RunebookGUI(PseudoPlayer pPlayer) {
		super(pPlayer.getRunebook().getNumRunes(), "Runebook", pPlayer);
		optionSelector();
	}

	@Override
	public void optionSelector() {
		for(Rune r : getPlayer().getRunebook().getRunes()) {
			ItemStack item = new ItemStack(Material.PAPER);
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			
			itemMeta.setDisplayName(ChatColor.GOLD+r.getLabel());
			
			lore.add(ChatColor.GREEN+"Location:");
			lore.add(ChatColor.YELLOW+"x:"+r.getLocation().getX());
			lore.add(ChatColor.YELLOW+"y:"+r.getLocation().getX());
			lore.add(ChatColor.YELLOW+"z:"+r.getLocation().getX());
			lore.add("You can remove a rune by shift clicking it");
			lore.add(ChatColor.GOLD+"Commands");
			lore.add("/runebook give "+ChatColor.RED+"(player) (rune)");
			
			itemMeta.setLore(lore);
			
			item.setItemMeta(itemMeta);
			addOption(item);
		}
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(getPlayer().getPromptedSpell() != null && event.getCurrentItem().getItemMeta().hasDisplayName()) {
			Player player = (Player)event.getWhoClicked();
			Spell spell = getPlayer().getPromptedSpell();
			spell.setResponse(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
			int castingDelay = spell.getCastingDelay();
			if(castingDelay > 0) {
				spell.preAction(player);
				getPlayer().getTimer().delayedSpell = spell;
			}
			else {
				spell.preAction(player);
				getPlayer().getTimer().cantCastTicks = spell.getCooldown();
				spell.doAction(player);
			}
			getPlayer().setPromptedSpell(null);
			forceClose();
		}else if(event.getCurrentItem().getItemMeta().hasDisplayName() && event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			Player player = (Player) event.getWhoClicked();
			Runebook runebook = getPlayer().getRunebook();
			Rune rune = runebook.getRune(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
			
			if(rune == null)
				return;
			
			runebook.removeRune(rune);
			Database.deleteRune(rune);
			forceClose();
			Output.positiveMessage(player, "You have removed the rune \""+ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())+"\" from your runebook.");
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		
	}
}
