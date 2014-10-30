package com.lostshard.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.Handlers.PlotHandler;
import com.lostshard.Main.Lostshard;
import com.lostshard.Objects.Plot;
import com.lostshard.Objects.PseudoPlayer;
import com.lostshard.Utils.Output;
import com.lostshard.Utils.TabUtils;

public class PlotCommand implements CommandExecutor, TabCompleter {

	public PlotCommand(Lostshard plugin) {
		plugin.getCommand("plot").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("plot")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			if(args.length < 1)
				player.sendMessage(ChatColor.GOLD + "use /help plot");
			else if(args[0].equalsIgnoreCase("info"))
				Output.PlotInfo(player);
			return true;
		}	
		return false;
	}
	
	public void plotCreate(Player player, Command cmd, String string, String[] args) {
		
	}
	
	//TODO add some messages and global static messages.
	public void plotDisband(Player player, Command cmd, String string, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null)
			Output.simpelError(player, "Must be inside plot.");
		else if(plot.isOwner(player)) {
			PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
			pPlayer.setMoney(pPlayer.getMoney()+plot.getValue());
			//Output positive message that plot has bin disbanded and the value of the plot.
			Output.positiveMessage(player, "You have disbanded "+plot.getName()+", and got "+plot.getValue()+"gc.");
			PlotHandler.removePlot(plot);
		}else{
			//Out put error if not owner, Frank u might be able to correct me on some words, plz do. 
			Output.plotNotOwner(player);
		}
	}
	
	public void plotDeposit(Player player, Command cmd, String string, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null)
			//TODO static msg
			Output.plotNotIn(player);
		else if(plot.isFriendOrAbove(player)) {
			int amount;
			try{
				amount = Integer.parseInt(args[1]);
			}catch(Exception e) {
				//TODO static msg
				Output.notNumber(player);
				return;
			}
			PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
			if(pPlayer.getMoney() >= amount) {
				plot.setMoney(plot.getMoney()+amount);
				pPlayer.setMoney(pPlayer.getMoney()-amount);
				//TODO x amount msg
				Output.positiveMessage(player, "Deposit x amount of money.");
			}else{
				//TODO static msg
				Output.simpelError(player, "ERROR dont have enugh money");
				return;				
			}
		}else{
			//TODO static msg Out put error if not owner, Frank u might be able to correct me on some words, plz do. 
			Output.plotNotFriend(player);
		}
	}
	
	public void plotWithdraw(Player player, Command cmd, String string, String[] args) {
		
	}
	
	public void plotExpand(Player player, Command cmd, String string, String[] args) {
		
	}

	public void plotShrink(Player player, Command cmd, String string, String[] args) {
	
	}
	
	public void plotNPCHire(Player player, Command cmd, String string, String[] args) {
		
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("plot"))
			if(args.length == 1)
				return TabUtils.StringTab(args, new String[] {"info","create","deposit","expand","rename","coowner","unfriend",
						"shrink","withdraw","friendbuild","sell","unsell","buy","list","preotect","unprotect","private",
						"public","explosions","upgrade","downgrade"});
		return null;
	}

}
