package com.lostshard.lostshard.Handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class InventoryGUIHandler {

	static PlayerManager pm = PlayerManager.getManager();
	
	public static void onInventoryClick(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();
		PseudoPlayer pP = pm.getPlayer(p);
		if(pP.getGui() != null)
			pP.getGui().inventoryClick(event);
	}
	
	public static void onInventoryInteract(InventoryInteractEvent event) {
		Player p = (Player) event.getWhoClicked();
		PseudoPlayer pP = pm.getPlayer(p);
		if(pP.getGui() != null)
			pP.getGui().inventoryInteract(event);
	}
	
	
	public static void onInventoryClose(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();
		PseudoPlayer pP = pm.getPlayer(p);
		if(pP.getGui() != null && event.getInventory().equals(pP.getGui().getGUI()))
			pP.getGui().inventoryClose(event);
	}
}
