package com.lostshard.Lostshard.Handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;

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
		if (pP.getGui() != null)
			pP.getGui().inventoryClose(event);
	}

	static PlayerManager pm = PlayerManager.getManager();
}
