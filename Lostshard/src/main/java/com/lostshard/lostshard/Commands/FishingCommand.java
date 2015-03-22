package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class FishingCommand implements CommandExecutor, TabCompleter {

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("boat")) {
			
		}
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		return null;
	}

}
