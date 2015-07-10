package com.lostshard.Lostshard.Objects.InventoryGUI;

import org.bukkit.OfflinePlayer;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Utils.ItemUtils;

public class PlayersGUI extends GUI {

	public PlayersGUI(String name, PseudoPlayer pPlayer,
			OfflinePlayer...players) {
		super(name, pPlayer);
		GUIItem[] items = new GUIItem[players.length];
		for(int i=0; i<items.length; i++)
			items[i] = new GUIItem(ItemUtils.getPlayerHead(players[i].getName()));
		setItems(items);
	}
}
