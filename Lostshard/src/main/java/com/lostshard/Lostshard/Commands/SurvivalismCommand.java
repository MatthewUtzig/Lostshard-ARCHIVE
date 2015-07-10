package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Skills.SurvivalismSkill;
import com.lostshard.Lostshard.Utils.Output;

public class SurvivalismCommand extends LostshardCommand {

	PlayerManager pm = PlayerManager.getManager();

	public SurvivalismCommand(Lostshard plugin) {
		super(plugin, "track", "camp");
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