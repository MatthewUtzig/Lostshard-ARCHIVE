package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;

public class MageryCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public MageryCommand(Lostshard plugin) {
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("cast")) {
			
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
	
}
