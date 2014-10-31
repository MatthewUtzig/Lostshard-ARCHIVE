package com.lostshard.lostshard.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Objects.Plot;

public class Output {
	
	public static void mustBePlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You must be a player!");
	}
	
	public static void plotHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "-Plot Help-");
		player.sendMessage(ChatColor.YELLOW + "/plot create");
		
		//etc
	}
	
	public static void PlotInfo(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		// Show title to everyone
		player.sendMessage(ChatColor.GOLD+"-"+plot.getName()+"'s Plot Info-");
		//Show protected,status,alignment,allow explosions to every one
		player.sendMessage(ChatColor.YELLOW+"Protected: "+ChatColor.WHITE+Utils.booleanToString(plot.isProtected())+ChatColor.YELLOW+
				", Status: "+ChatColor.WHITE+"private/public"+ChatColor.YELLOW+", Alignment: "+ChatColor.WHITE+"Lawfull/Criminal");
		player.sendMessage(ChatColor.YELLOW+"Allow Exploosions: "+ChatColor.WHITE+Utils.booleanToString(plot.isAllowExplosions()));
		//Show your status of the plot
		
		
	}
	
	//TODO Frank can u add some default response
	
	public static void simpelError(Player player, String message) {
		player.sendMessage(ChatColor.DARK_RED+message);
	}
	
	public static void positiveMessage(Player player, String message) {
		player.sendMessage(ChatColor.GOLD+message);
	}
	
	public static void notNumber(Player player) {
		simpelError(player, "Need to be a number.");
	}
	
	public static void plotNotIn(Player player) {
		simpelError(player, "You are not in a plot.");
	}
	
	public static void plotNotFriend(Player player) {
		simpelError(player, "You need to be friend.");
	}
	
	public static void plotNotOwner(Player player) {
		simpelError(player, "You need to be owner.");
	}
	
	public static void plotNotCoowner(Player player) {
		simpelError(player, "You need to be co-owner.");
	}
	
	public static void capturePointsInfo(Player player) {
		player.sendMessage(ChatColor.GOLD+"-Control Points-");
		player.sendMessage(ChatColor.YELLOW+"Hostility: 0, 0, 0");
		//TODO do it perfect, Frank help me with layout plz.
	}
	
	//TODO add some outputs
	
}
