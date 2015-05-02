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

	private void enhance(Player player) {
		BlackSmithySkill.enhance(player);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("repair")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.repair(player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("smelt")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.semt(player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("enhance")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.enhance(player);
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}

	private void repair(Player player) {
		BlackSmithySkill.repair(player);
	}

	private void semt(Player player) {
		BlackSmithySkill.smelt(player);
	}

}
