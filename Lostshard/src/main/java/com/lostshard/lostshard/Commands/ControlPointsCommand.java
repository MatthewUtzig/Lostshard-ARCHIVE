package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Utils.Output;

public class ControlPointsCommand implements CommandExecutor, TabCompleter {

	public ControlPointsCommand(Lostshard plugin) {
		plugin.getCommand("capturepoints").setExecutor(this);
		plugin.getCommand("claim").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("capturepoints")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			Output.capturePointsInfo(player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("claim")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			// TODO add claim function
			// Player player = (Player) sender;
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}

}
