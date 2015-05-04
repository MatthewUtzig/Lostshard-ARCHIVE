package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Skills.SurvivalismSkill;
import com.lostshard.lostshard.Utils.Output;

public class SurvivalismCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();

	public SurvivalismCommand(Lostshard plugin) {
		plugin.getCommand("track").setExecutor(this);
		plugin.getCommand("camp").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("track")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			SurvivalismSkill.track(player, args);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("camp")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			SurvivalismSkill.camp(player);
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
}