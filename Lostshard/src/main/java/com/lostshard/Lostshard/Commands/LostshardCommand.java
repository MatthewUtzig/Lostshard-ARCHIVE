package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Utils.TabUtils;

public abstract class LostshardCommand implements CommandExecutor, TabCompleter {

	private final Lostshard plugin;

	public LostshardCommand(Lostshard plugin, String... commands) {
		this.plugin = plugin;
		for (final String cmd : commands)
			this.getPlugin().getCommand(cmd).setExecutor(this);
	}

	public Lostshard getPlugin() {
		return this.plugin;
	}

	@Override
	public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return TabUtils.empty();
	}

}
