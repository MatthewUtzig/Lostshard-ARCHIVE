package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;

public class UtilsCommand implements CommandExecutor, TabCompleter {

	/**
	 * @param Lostshard as plugin
	 */
	public UtilsCommand(Lostshard plugin) {
		plugin.getCommand("stats").setExecutor(this);
		plugin.getCommand("who").setExecutor(this);
		plugin.getCommand("whois").setExecutor(this);
		plugin.getCommand("spawn").setExecutor(this);
		plugin.getCommand("resetspawn").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("stats")) {
			Output.displayStats(sender);
		}else if(cmd.getName().equalsIgnoreCase("who")) {
			Output.outputPlayerlist(sender);
		}else if(cmd.getName().equalsIgnoreCase("whois")) {
			Output.displayWho(sender, args);
		}else if(cmd.getName().equalsIgnoreCase("spawn")) {
			playerSpawn(sender);
		}else if(cmd.getName().equalsIgnoreCase("resetspawn")) {
			playerResetspawn(sender);
		}else if(cmd.getName().equalsIgnoreCase("rules")) {
			Output.displayRules(sender);
		}
		return true;
	}
	
	private void playerResetspawn(CommandSender sender) {
		if(!(sender instanceof Player)) {
			Output.simpleError(sender, "Only players may perform this command.");
			return;
		}
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer((Player)sender);
		pPlayer.setCustomSpawn(null);
    	if(pPlayer.isMurder())
    		Output.positiveMessage(sender, "You have reset your spawn to Chaos.");
    	else
    		Output.positiveMessage(sender, "You have reset your spawn to Order.");
    	return;
	}

	private void playerSpawn(CommandSender sender) {
		if(!(sender instanceof Player)) {
			Output.simpleError(sender, "Only players may perform this command.");
			return;
		}
		Player player = (Player) sender;
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if(pPlayer.getSpawnTick() <= 0) {
        		pPlayer.setSpawnTick(72000);
        		Output.positiveMessage(player, "Returning to spawn in 10 seconds.");
//    		player.sendMessage("Cannot go to spawn, "+(seconds/60)+" minutes, "+(seconds%60)+" seconds remaining.");
    	}
    	return;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		return null;
	}	
}
