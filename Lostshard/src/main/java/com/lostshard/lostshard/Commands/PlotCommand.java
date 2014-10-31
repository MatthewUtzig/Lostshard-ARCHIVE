package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.TabUtils;

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
			String plotCommand = args[0];
			if(plotCommand.equalsIgnoreCase("create"))
				createPlot(player, args);
			else if(plotCommand.equalsIgnoreCase("survey"))
				plotSurvey(player);
			else if(plotCommand.equalsIgnoreCase("info"))
				plotInfo(player);
			else if(plotCommand.equalsIgnoreCase("friend"))
				plotFriend(player, args);
			else if(plotCommand.equalsIgnoreCase("co-own") || plotCommand.equalsIgnoreCase("coown") || plotCommand.equalsIgnoreCase("co-owner") || plotCommand.equalsIgnoreCase("coowner"))
				plotCoOwn(player, args);
			else if(plotCommand.equalsIgnoreCase("unfriend"))
				plotUnFriend(player, args);
			else if(plotCommand.equalsIgnoreCase("protect"))
				plotProtect(player);
			else if(plotCommand.equalsIgnoreCase("unprotect"))
				plotUnProtect(player);
			else if(plotCommand.equalsIgnoreCase("private"))
				plotLock(player);
			else if(plotCommand.equalsIgnoreCase("public"))
				plotUnLock(player);
			else if(plotCommand.equalsIgnoreCase("expand"))
				plotExpand(player, args);
			else if(plotCommand.equalsIgnoreCase("deposit"))
				plotDeposit(player, args);
			else if(plotCommand.equalsIgnoreCase("withdraw"))
				plotWithdraw(player, args);
			else if(plotCommand.equalsIgnoreCase("test"))
				plotTest(player);
			else if(plotCommand.equalsIgnoreCase("endtest"))
				plotEndTest(player);
			else if(plotCommand.equalsIgnoreCase("disband"))
				plotDisband(player);
			else if(plotCommand.equalsIgnoreCase("list"))
				plotList(player, args);
			else if(plotCommand.equalsIgnoreCase("upgrade") || plotCommand.equalsIgnoreCase("upgrades"))
				plotUpgrade(player, args);
			else if(plotCommand.equalsIgnoreCase("downgrade"))
				plotDowngrade(player, args);
			else if(plotCommand.equalsIgnoreCase("transfer"))
				plotTransfer(player, args);
			else if(plotCommand.equalsIgnoreCase("shrink"))
				plotShrink(player, args);
			else if(plotCommand.equalsIgnoreCase("rename"))
				plotRename(player, args);
			else if(plotCommand.equalsIgnoreCase("friendbuild"))
				plotFriendBuildToggle(player);
			else if(plotCommand.equalsIgnoreCase("npc"))
				plotNPC(player, args);
			else if(plotCommand.equalsIgnoreCase("sell"))
				plotSell(player, args);
			else if(plotCommand.equalsIgnoreCase("buy"))
				plotBuy(player);
			else if(plotCommand.equalsIgnoreCase("unsell") || plotCommand.equalsIgnoreCase("unlist") || plotCommand.equalsIgnoreCase("un-sell"))
				plotUnSell(player);
			else if(plotCommand.equalsIgnoreCase("explosion"))
				plotExplosionToggle(player);
			return true;
		}	
		return false;
	}
		
	private void plotExplosionToggle(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(!plot.isCoownerOrAbove(player)) {
			Output.plotNotCoowner(player);
			return;
		}
		if(plot.isAllowExplosions()) {
			plot.setAllowExplosions(false);
			Output.positiveMessage(player, "You have disabled explosions on your plot.");
		}
		else {
			plot.setAllowExplosions(true);
			Output.positiveMessage(player, "You have enabled explosions on your plot.");
		}
	}

	private void plotUnSell(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(!plot.isOwner(player)) {
			Output.plotNotCoowner(player);
			return;
		}
		plot.setSalePrice(0);
		Database.updatePlot(plot);
		Output.positiveMessage(player, "This plot is no longer for sale.");
	}

	private void plotBuy(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotSell(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotNPC(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotFriendBuildToggle(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotRename(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotShrink(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotTransfer(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotDowngrade(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotUpgrade(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotList(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotEndTest(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotWithdraw(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotTest(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotExpand(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotUnLock(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotLock(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotUnProtect(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotProtect(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotUnFriend(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotCoOwn(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotFriend(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotInfo(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void plotSurvey(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void createPlot(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	//TODO add some messages and global static messages.
	public void plotDisband(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null)
			Output.simpleError(player, "Must be inside plot.");
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
	
	public void plotDeposit(Player player, String[] args) {
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
				Output.simpleError(player, "ERROR dont have enugh money");
				return;				
			}
		}else{
			//TODO static msg Out put error if not owner, Frank u might be able to correct me on some words, plz do. 
			Output.plotNotFriend(player);
		}
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
