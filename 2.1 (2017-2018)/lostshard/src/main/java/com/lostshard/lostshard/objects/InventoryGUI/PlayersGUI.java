package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.OfflinePlayer;

import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Utils.ItemUtils;

public class PlayersGUI extends GUI {
	public PlayersGUI(String name, PseudoPlayer pPlayer, OfflinePlayer... players) {
		super(name, pPlayer, new GUIItem[0]);
		final GUIItem[] items = new GUIItem[players.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new GUIItem(ItemUtils.getPlayerHead(players[i].getName()));
		}
		this.setItems(items);
	}
}
