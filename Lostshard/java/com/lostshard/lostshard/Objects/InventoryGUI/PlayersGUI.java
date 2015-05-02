package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.ItemUtils;

public class PlayersGUI extends GUI {

	private List<OfflinePlayer> players;
	
	public PlayersGUI(String name, PseudoPlayer pPlayer, List<OfflinePlayer> players) {
		super(players.size(), name, pPlayer);
		this.players = players;
	}

	public List<OfflinePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<OfflinePlayer> players) {
		this.players = players;
	}

	@Override
	public void onClick(InventoryClickEvent event) {

	}

	@Override
	public void optionSelector() {
		for(OfflinePlayer p : players) {
			addOption(ItemUtils.getPlayerHead(p.getName()));
		}
	}

}
