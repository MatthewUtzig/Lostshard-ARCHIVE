package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract interface GUIClick {
	public abstract void click(Player paramPlayer, PseudoPlayer paramPseudoPlayer, ItemStack paramItemStack,
			ClickType paramClickType, Inventory paramInventory, int paramInt);
}
