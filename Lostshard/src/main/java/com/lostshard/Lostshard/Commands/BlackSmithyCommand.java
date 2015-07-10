package com.lostshard.Lostshard.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Skills.BlackSmithySkill;
import com.lostshard.Lostshard.Utils.Output;

public class BlackSmithyCommand extends LostshardCommand {

	public BlackSmithyCommand(Lostshard plugin) {
		super(plugin, "repair", "smelt", "enhance");
	}

	private void enhance(Player player) {
		BlackSmithySkill.enhance(player);
	}

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
	
	private void repair(Player player) {
		BlackSmithySkill.repair(player);
	}

	private void semt(Player player) {
		BlackSmithySkill.smelt(player);
	}

}
