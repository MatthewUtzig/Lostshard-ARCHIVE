package com.lostshard.lostshard.Handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class InventoryGUIHandler {

	public static void onInventoryClick(InventoryClickEvent event) {
		final Player p = (Player) event.getWhoClicked();
		final PseudoPlayer pP = pm.getPlayer(p);
		if (pP.getGui() != null)
			pP.getGui().inventoryClick(event);
	}

	public static void onInventoryClose(InventoryCloseEvent event) {
		final Player p = (Player) event.getPlayer();
		final PseudoPlayer pP = pm.getPlayer(p);
		if (pP.getGui() != null
				&& event.getInventory().equals(pP.getGui().getGUI()))
			pP.getGui().inventoryClose(event);
	}

	public static void onInventoryInteract(InventoryInteractEvent event) {
		final Player p = (Player) event.getWhoClicked();
		final PseudoPlayer pP = pm.getPlayer(p);
		if (pP.getGui() != null)
			pP.getGui().inventoryInteract(event);
	}

	static PlayerManager pm = PlayerManager.getManager();
}
