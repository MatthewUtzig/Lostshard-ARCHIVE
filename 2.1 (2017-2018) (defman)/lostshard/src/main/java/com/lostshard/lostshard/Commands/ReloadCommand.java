package com.lostshard.lostshard.Commands;

import org.bukkit.command.CommandSender;

import com.lostshard.lostshard.Manager.ConfigManager;
import com.lostshard.lostshard.Utils.Output;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;

public class ReloadCommand {

	ConfigManager cm = ConfigManager.getManager();

	@Command(aliases = { "lostshardreload" }, desc = "Reloads the lostshard config")
	@Require("losthsard.reload")
	public void reload(CommandSender sender) {
		this.cm.reload();
		Output.positiveMessage(sender, "Reload complete.");
	}
}
