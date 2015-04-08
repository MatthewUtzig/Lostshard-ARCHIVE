package com.lostshard.lostshard.Handlers;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class InventoryGUIHandler {

	static PlayerManager pm = PlayerManager.getManager();
	
	public static void onInventoryClick(InventoryClickEvent event) {
		for(HumanEntity he : event.getViewers()) {
			Player p = (Player) he;
			PseudoPlayer pP = pm.getPlayer(p);
			if(pP.getGui() != null)
				pP.getGui().inventoryClick(event);
			pP.setGui(null);
		}
	}
	
	public static void onInventoryClose(InventoryCloseEvent event) {
		for(HumanEntity he : event.getViewers()) {
			Player p = (Player) he;
			PseudoPlayer pP = pm.getPlayer(p);
			if(pP.getGui() != null)
				pP.getGui().inventoryClose(event);
			pP.setGui(null);
		}
	}
	
	
}
