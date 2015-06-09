package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.TamingSkill;
import com.lostshard.lostshard.Utils.Output;

public class TamingCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();

	public TamingCommand(Lostshard plugin) {
		plugin.getCommand("mount").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("mount")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			final PseudoPlayer pPlayer = this.pm.getPlayer(player);
			TamingSkill.callMount(player, pPlayer);
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}

}
