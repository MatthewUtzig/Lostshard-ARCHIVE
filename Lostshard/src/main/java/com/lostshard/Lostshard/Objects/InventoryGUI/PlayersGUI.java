package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.ItemUtils;
import org.bukkit.OfflinePlayer;

public class PlayersGUI extends GUI {
	public PlayersGUI(String name, PseudoPlayer pPlayer, OfflinePlayer... players) {
		super(name, pPlayer, new GUIItem[0]);
		GUIItem[] items = new GUIItem[players.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new GUIItem(ItemUtils.getPlayerHead(players[i].getName()));
		}
		setItems(items);
	}
}
