package com.lostshard.lostshard.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class HelpHandler {

	// TODO hey Frank can u write some help lines

	public static void help(Player player) {
		player.sendMessage(ChatColor.GOLD + "-Help-");
	}

	public static void plotHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "-Plot Help-");
		player.sendMessage(ChatColor.YELLOW + "/plot create");
	}

	public static void plotNpcHelp(Player player) {
		// TODO Auto-generated method stub

	}

}
