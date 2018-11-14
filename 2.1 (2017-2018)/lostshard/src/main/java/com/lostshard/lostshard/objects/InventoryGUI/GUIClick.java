package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;

public abstract interface GUIClick {
	public abstract void click(Player paramPlayer, PseudoPlayer paramPseudoPlayer, ItemStack paramItemStack,
			ClickType paramClickType, Inventory paramInventory, int paramInt);
}
