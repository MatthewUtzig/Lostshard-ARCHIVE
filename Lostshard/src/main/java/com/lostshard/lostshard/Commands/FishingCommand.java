package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Skills.FishingSkill;
import com.lostshard.lostshard.Utils.Output;

public class FishingCommand implements CommandExecutor, TabCompleter {

	public FishingCommand(Lostshard plugin) {
		plugin.getCommand("boat").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("boat")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			FishingSkill.callBoat(player);
			return true;
		}
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		return null;
	}

}
