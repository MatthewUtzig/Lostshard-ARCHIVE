package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Skills.TamingSkill;
import com.lostshard.Lostshard.Utils.Output;

public class TamingCommand extends LostshardCommand {

	PlayerManager pm = PlayerManager.getManager();

	public TamingCommand(Lostshard plugin) {
		super(plugin, "mount");
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
