package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.ConfigManager;
import com.lostshard.Lostshard.Utils.Output;

public class ReloadCommand extends LostshardCommand {

	ConfigManager cm = ConfigManager.getManager();

	Lostshard plugin;

	public ReloadCommand(Lostshard plugin) {
		super(plugin, "lostshardreload");
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
