package com.lostshard.Lostshard.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Skills.FishingSkill;
import com.lostshard.Lostshard.Utils.Output;

public class FishingCommand extends LostshardCommand {

	public FishingCommand(Lostshard plugin) {
		super(plugin, "boat");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("boat")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			FishingSkill.callBoat(player);
			return true;
		}
		return false;
	}
}
