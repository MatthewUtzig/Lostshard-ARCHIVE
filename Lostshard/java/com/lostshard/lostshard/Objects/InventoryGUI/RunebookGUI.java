package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;

public class RunebookGUI extends InventoryGUI {

	public RunebookGUI(PseudoPlayer pPlayer) {
		super((int)Math.ceil(pPlayer.getRunebook().getNumRunes()/9)*9, "Runebook", GUIType.RUNEBOOK, pPlayer);
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
			
			itemMeta.setLore(lore);
			
			item.setItemMeta(itemMeta);
			addOption(item);
		}
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(getPlayer().getPromptedSpell() != null && event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().hasDisplayName()) {
			getPlayer().getPromptedSpell().setResponse(event.getCurrentItem().getItemMeta().getDisplayName());
		}else if(event.getCurrentItem() != null){
			close();
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		
	}

}
