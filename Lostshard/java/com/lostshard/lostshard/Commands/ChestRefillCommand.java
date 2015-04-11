package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Utils.Output;

public class ChestRefillCommand implements CommandExecutor, TabCompleter {
	
	public ChestRefillCommand(Lostshard plugin) {
		plugin.getCommand("dc").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dc")) {
			dc(sender, args);
			return true;
		}
		return false;
	}
	
	private void dc(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		if(args.length < 2) {
			Output.simpleError(sender, "/dc (rangeMin) (rangeMax)");
			return;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		return null;
	}
}
