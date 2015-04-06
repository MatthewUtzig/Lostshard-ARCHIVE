package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Skills.BlackSmithySkill;
import com.lostshard.lostshard.Utils.Output;

public class BlackSmithyCommand implements CommandExecutor, TabCompleter {

	public BlackSmithyCommand(Lostshard plugin) {
		plugin.getCommand("repair").setExecutor(this);
		plugin.getCommand("smelt").setExecutor(this);
		plugin.getCommand("enhance").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("repair")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			repair(player);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("smelt")){
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			semt(player);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("enhance")){
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			enhance(player);
			return true;
		}
		return false;
	}
	
	private void enhance(Player player) {
		BlackSmithySkill.enhance(player);
	}

	private void semt(Player player) {
		BlackSmithySkill.smelt(player);
	}

	private void repair(Player player) {
		BlackSmithySkill.repair(player);
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
	
}
