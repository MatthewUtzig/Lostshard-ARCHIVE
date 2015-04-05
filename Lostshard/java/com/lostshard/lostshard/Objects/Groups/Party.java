package com.lostshard.lostshard.Objects.Groups;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Party extends Group {

	public void sendMessage(String message) {
		for(UUID member : getMembers()) {
			Player memberPlayer = Bukkit.getPlayer(member);
			if(memberPlayer != null)
				memberPlayer.sendMessage(ChatColor.WHITE+"["+ChatColor.DARK_PURPLE+"Party"+ChatColor.WHITE+"] "+message);
		}
	}
}
