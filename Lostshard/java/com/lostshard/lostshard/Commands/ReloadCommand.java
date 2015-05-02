package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.ConfigManager;
import com.lostshard.lostshard.Utils.Output;

public class ReloadCommand implements CommandExecutor, TabCompleter {

	ConfigManager cm = ConfigManager.getManager();

	Lostshard plugin;

	public ReloadCommand(Lostshard plugin) {
		plugin.getCommand("lostshardreload").setExecutor(this);
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("lostshardreload")) {
			this.cm.reload();
			Output.positiveMessage(sender, "Reload complete.");
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
