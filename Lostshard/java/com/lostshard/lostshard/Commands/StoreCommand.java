package com.lostshard.lostshard.Commands;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Utils.Output;

public class StoreCommand implements CommandExecutor, TabCompleter {
	
	public StoreCommand(Lostshard plugin) {
//		plugin.getCommand("buy").setExecutor(this);
//		plugin.getCommand("sell").setExecutor(this);
//		plugin.getCommand("store").setExecutor(this);
//		plugin.getCommand("vendor").setExecutor(this);		
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("vendor")) {
			if(!(sender instanceof Player)){
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			vendor(player, args);
			return true;
		}
		return false;
	}
	
	private void vendor(Player player, String[] args) {
		if(args.length < 1) {
			Output.simpleError(player, "Use \"/vendor help\" commands.");
			return;
		}
		String subCmd = args[0];
		if(subCmd.equalsIgnoreCase("help")) {
			player.sendMessage(ChatColor.GOLD+"-Vendor help-");
			player.sendMessage(ChatColor.YELLOW+"/vendor additem (sellprice) (buyprice) (amount)");
			return;
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
}
