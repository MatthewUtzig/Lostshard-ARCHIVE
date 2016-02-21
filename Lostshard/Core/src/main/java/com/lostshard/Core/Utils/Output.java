package com.lostshard.Core.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Output {

	public static void simpleError(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_RED + message);
	}
	
	public static void positive(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GOLD + message);
	}
	
	public static void info(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.YELLOW + message);
	}
}
