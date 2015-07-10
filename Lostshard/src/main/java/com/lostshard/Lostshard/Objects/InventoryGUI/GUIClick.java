package com.lostshard.Lostshard.Objects.InventoryGUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Objects.PseudoPlayer;

public interface GUIClick {
	
	public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv, int slot);
	
}
