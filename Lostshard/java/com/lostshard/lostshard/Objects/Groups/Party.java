package com.lostshard.lostshard.Objects.Groups;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;

public class Party extends Group {

	@Override
	public void sendMessage(String message) {
		for(UUID member : getMembers()) {
			Player memberPlayer = Bukkit.getPlayer(member);
			if(memberPlayer != null) {
				Lostshard.log.finest(ChatColor.WHITE+"["+ChatColor.DARK_PURPLE+"Party"+ChatColor.WHITE+"] "+message);
				memberPlayer.sendMessage(ChatColor.WHITE+"["+ChatColor.DARK_PURPLE+"Party"+ChatColor.WHITE+"] "+message);
			}
		}
	}
}
