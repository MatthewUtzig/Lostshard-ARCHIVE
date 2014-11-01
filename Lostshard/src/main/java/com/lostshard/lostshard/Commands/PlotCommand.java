package com.lostshard.lostshard.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.HelpHandler;
import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.TabUtils;
import com.lostshard.lostshard.Utils.Utils;

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
				plotTestToggle(player);
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
			Output.simpleError(player, "Only the owner and co-owners may toggle explosions.");
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
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		
		int salePrice = plot.getSalePrice();
		if(salePrice <= 0) {
			Output.simpleError(player, "This plot is not for sale.");
			return;
		}
		
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		
		if(pseudoPlayer.getMoney() < salePrice) {
			Output.simpleError(player, "Cannot afford to buy plot, cost: "+salePrice+".");
			return;
		}
		
		pseudoPlayer.setMoney(pseudoPlayer.getMoney() - salePrice);
		//Database.updatePlayerByPseudoPlayer(pseudoPlayer);
		
		UUID lastOwner = plot.getOwner();
		plot.setOwner(player.getUniqueId());
		plot.setSalePrice(0);
		plot.getCoowners().clear();
		plot.getFriends().clear();
		Database.updatePlot(plot);
		
		Output.positiveMessage(player, "You have purchased the plot "+plot.getName()+" from "+Bukkit.getOfflinePlayer(lastOwner).getName()+" for "+salePrice+".");
		Player sellerPlayer = player.getServer().getPlayer(lastOwner);
		if(sellerPlayer != null) {
			PseudoPlayer sellerPseudoPlayer = PseudoPlayerHandler.getPlayer(sellerPlayer);
			sellerPseudoPlayer.setMoney(sellerPseudoPlayer.getMoney() + salePrice);
			//Database.updatePlayerByPseudoPlayer(sellerPseudoPlayer);
			sellerPlayer.sendMessage("You have sold the plot "+plot.getName()+" to "+player.getName()+" for "+salePrice+".");
		}
		else {
			//PseudoPlayer sellerPseudoPlayer = Database.createPseudoPlayer(lastOwner);
			//sellerPseudoPlayer.setMoney(sellerPseudoPlayer.getMoney() + salePrice);
			//Database.updatePlayerByPseudoPlayer(sellerPseudoPlayer);
			//PseudoPlayerHandler.removed(lastOwner);
			//Database.addOfflineMessage(lastOwner, "You have sold the plot "+plot.getName()+" to "+player.getName()+" for "+salePrice+".");
		}
	}

	private void plotSell(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may sell the plot.");
			return;
		}
		if(args.length < 2) {
			player.sendMessage(ChatColor.GRAY+"Use /plot sell (amount)");
			return;
		}
		int amount = 0;
		try {
			amount = Integer.parseInt(args[1]);
		}
		catch(Exception e) {
			player.sendMessage(ChatColor.GRAY+"Use /plot sell (amount)");
			return;
		}
		
		if(amount < 1) {
			Output.simpleError(player, "Invalid price, must be 1 or greater");
			return;
		}
		
		if(amount > 9999999) {
			Output.simpleError(player, "Max sale price is 9999999");
			return;
		}
		
		plot.setSalePrice(amount);
		Database.updatePlot(plot);
		Output.positiveMessage(player, "You have set this plot for sale at "+amount+"gc.");
	}

	private void plotNPC(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(args.length >= 4) {
			Output.simpleError(player, "/plot npc hire (Banker|Vendor) (name)");
			return;
		}
		if(!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may manage plot NPCs");
			return;
		}
		
		if(args[1].equalsIgnoreCase("hire")) {
			String name;
			if(args[2].equalsIgnoreCase("banker")) {
				if(!plot.isTown()){
					Output.simpleError(player, "You can only place a banker in a town.");
					return;
				}
				name = args[3];
				name.trim();
				if(name.length() > 7) {
					Output.simpleError(player, "Banker name must be 7 characters or less.");
					return;
				}
			
				if(name.length() < 2) {
					Output.simpleError(player, "Banker name must be more 1 character or more.");
					return;
				}
				
				if(name.contains("\"") || name.contains("'") || name.contains(" ")) {
					Output.simpleError(player, "Cannot use \" or spaces in NPC names.");
					return;
				}
				
				Plot npcPlot = null;
				
				for(NPC npc : Lostshard.getNpcs()) {
					if(!npc.getType().equals(NPCType.BANKER))
						continue;
					if(npc.getName().equalsIgnoreCase(name)) {
						Output.simpleError(player, "An NPC with that name already exists.");
						return;
					}
					npcPlot = PlotHandler.findPlotAt(npc.getLocation());
					if(npcPlot == plot && !player.isOp())
						Output.simpleError(player, "You may only have 1 banker per plot.");
						return;
				}
		
				NPC npc = new NPC(NPCType.BANKER, name, player.getLocation());
				plot.getNpcs().add(npc);
				npc.spawn();
								
				Output.positiveMessage(player, "You have hired a banker named "+name+".");
		}	
		else if(args[2].equalsIgnoreCase("vendor")) {
				name = args[3];
				name.trim();
				if(name.length() > 7) {
					Output.simpleError(player, "Vendor name must be 7 characters or less.");
					return;
				}
			
				if(name.length() < 2) {
					Output.simpleError(player, "Vendor name must be more 1 character or more.");
					return;
				}
				
				if(name.contains("\"") || name.contains("'") || name.contains(" ")) {
					Output.simpleError(player, "Cannot use \" or spaces in NPC names.");
					return;
				}
				
				int currentAmountOfVendors = 1;
				
				for(NPC npc : plot.getNpcs()) {
					if(!npc.getType().equals(NPCType.VENDOR))
						continue;
					if(npc.getName().equalsIgnoreCase(name)) {
						Output.simpleError(player, "An NPC with that name already exists.");
						return;
					}
					currentAmountOfVendors++;
				}
				
				if(currentAmountOfVendors < plot.getMaxVendors() && !player.isOp()) {
					Output.simpleError(player, "You may only have "+plot.getMaxVendors()+" vendors in your plot.");
					return;
				}
		
				NPC npc = new NPC(NPCType.VENDOR, name, player.getLocation());
				plot.getNpcs().add(npc);
				npc.spawn();
								
				Output.positiveMessage(player, "You have hired a vendor named "+name+".");
			}
		}
		else if(args[2].equalsIgnoreCase("move")) {
			if(args.length < 3) {
				Output.simpleError(player, "Use /plot npc move (npc name)");
				return;
			}
			
			String npcName = args[2];
			npcName = npcName.trim();
			
			for(NPC npc : plot.getNpcs())
				if(npc.getName().equalsIgnoreCase(npcName)) {
					npc.move(player.getLocation());
					Output.positiveMessage(player, "You have moved "+npcName+".");
					return;
				}
			Output.simpleError(player, "Cannot find an NPC named "+npcName+" on this plot.");
		}
		else if(args[1].equalsIgnoreCase("fire")) {
			if(args.length < 3) {					
					Output.simpleError(player, "/plot npc fire (name)");
					return;
				}
			String name = args[2];

			for(NPC npc : plot.getNpcs())
				if(npc.getName().equalsIgnoreCase(name)) {
					npc.fire();
					Output.positiveMessage(player, "You have fired "+npc.getName()+".");
					return;
				}
		}
		else if(args[1].equalsIgnoreCase("list")) {
			player.sendMessage(ChatColor.GOLD+"-"+plot.getName()+"'s NPCs-");
			ArrayList<NPC> npcs = plot.getNpcs();
			int numOfNpcs = npcs.size();
			
			if(numOfNpcs <= 0) {
				player.sendMessage(ChatColor.RED+"No NPCs");
				return;
			}
			for(NPC npc : npcs) 
					player.sendMessage(ChatColor.YELLOW+npc.getName()+" ("+npc.getType().toString()+")");
		return;
		}
		HelpHandler.plotNpcHelp(player);
	}

	private void plotFriendBuildToggle(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may toggle friendbuild.");
			return;
		}
		if(plot.isFriendBuild()) {
			plot.setFriendBuild(false);
			Output.positiveMessage(player, "You have turned on friend build for "+plot.getName()+".");
			return;
		}
	}

	private void plotRename(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may rename the plot.");
			return;
		}
		//Verify that we can afford the rename.
		int plotMoney = plot.getMoney();
		if(plotMoney >= 1000) {
			Output.simpleError(player, "Not enough in the plot treasury to rename. "+Variables.plotRenamePrice+"gc.");
			return;
		}
		// figure out the name that the player input
		int splitNameLength = args.length;
		String plotName = "";
		for(int i=1; i<splitNameLength; i++) {
			plotName += args[i];
			if(i < splitNameLength-1)
				plotName+= " ";
		}
		
		plotName = plotName.trim();
		
		boolean nameValid = true;
		if(plotName.contains("\"") || plotName.contains("'")) {
			Output.simpleError(player, "Cannot use \" in plot name.");
			nameValid = false;
		}
		
		int nameLength = plotName.length();
		if(nameLength > Variables.plotMaxNameLength && nameValid) {
			Output.simpleError(player, "Name is invalid or too long, limit is 20 characters.");
			return;
		}
			
		for(Plot p : Lostshard.getPlots()) {
			if(p.getName().equalsIgnoreCase(plotName)) {
				Output.simpleError(player, "Cannot use that name, it is taken.");
				return;
			}
		}
		// we are good to go
		plot.setMoney(plot.getMoney()-Variables.plotRenamePrice);
		plot.setName(plotName);
		Database.updatePlot(plot);
		Output.positiveMessage(player, "You have renamed the plot to "+plotName+".");
	}

	private void plotShrink(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotTransfer(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotDowngrade(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotUpgrade(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	@SuppressWarnings("deprecation")
	private void plotList(Player player, String[] args) {
		if(args.length >=2 && player.isOp()) {
		String name = args[1];
		Output.positiveMessage(player, "-"+name+"'s Plots-");
		for(Plot plot : Lostshard.getPlots()) {
			if(plot.getOwner().equals(Bukkit.getOfflinePlayer(name).getUniqueId())) {
				player.sendMessage(" - "+plot.getName()+" ("+plot.getSize()+") @("+plot.getLocation().getBlockX()+","+plot.getLocation().getBlockY()+","+plot.getLocation().getBlockZ()+")");
			}
		}
	} else {
		Output.positiveMessage(player, "-"+player.getName()+"'s Plots-");
		boolean foundOne = false;
		for(Plot plot : Lostshard.getPlots()) {
			if(plot.isOwner(player)) {
				foundOne = true;
				player.sendMessage(" - "+plot.getName()+" ("+plot.getSize()+") @("+plot.getLocation().getBlockX()+","+plot.getLocation().getBlockY()+","+plot.getLocation().getBlockZ()+")");
			}
		}
	
		if(!foundOne) 
			Output.simpleError(player, "You do not currently own any plots.");
	}
		
	}

	private void plotWithdraw(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotTestToggle(Player player) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotExpand(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotUnLock(Player player) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotLock(Player player) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotUnProtect(Player player) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotProtect(Player player) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotUnFriend(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotCoOwn(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void plotFriend(Player player, String[] args) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotInfo(Player player) {
		// TODO Auto-generated method stub
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
	}

	private void plotSurvey(Player player) {
		// TODO Auto-generated method stub
		
	}

	private void createPlot(Player player, String[] args) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		int curMoney = pseudoPlayer.getMoney();
		
		//get recently purchased plots
		int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
		int plotsThisWeek = 0;
		if(plotCreatePoints > 0) {
			plotsThisWeek = (plotCreatePoints/7);
		}
		
		if(player.isOp())
			plotsThisWeek = 0;
		
		int plotMoneyCost = Variables.plotCreatePrice;
		ItemStack plotDiamondCost = Variables.plotCreateItemPrice;
		// for each owned plot, double the price
		for(int i=0; i<plotsThisWeek; i++) {
			plotMoneyCost *= 2;
			plotDiamondCost.setAmount(plotDiamondCost.getAmount()*2);
		}
		// make sure the player has enough money and diamonds
		if(player.isOp() || ((curMoney >= plotMoneyCost) && player.getInventory().containsAtLeast(plotDiamondCost, plotDiamondCost.getAmount()))) {
			Output.simpleError(player, "Cannot afford to create a plot, cost: "+plotMoneyCost+" gold & "+plotDiamondCost.getAmount()+" diamonds.");
			return;
		}
			// verify that this plot wouldn't intersect with an existing plot
			ArrayList<Plot> intersectingRegions = new ArrayList<Plot>();
			
			// figure out the name that the player input
			int splitNameLength = args.length;
			String plotName = "";
			for(int i=1; i<splitNameLength; i++) {
				plotName += args[i];
				if(i < splitNameLength-1)
					plotName+= " ";
			}
			
			plotName = plotName.trim();
			
			if(plotName.contains("\"") || plotName.contains("'")) {
				Output.simpleError(player, "Cannot use \" in plot name.");
				return;
			}
			
			int nameLength = plotName.length();
			if(nameLength > Variables.plotMaxNameLength) {
				Output.simpleError(player, "Error: Name is invalid or too long, limit is 20 characters.");
				return;
			}
			// find intersecting regions and check names to make sure there isn't a region with that name already
			Location curLoc = player.getLocation().getBlock().getLocation();
			for(Plot plot : Lostshard.getPlots()) {
				if(plot.getName().equalsIgnoreCase(plotName)) {
					Output.simpleError(player, "A plot with that name already exists, please choose another.");
					return;
				}
				if(curLoc.getWorld().equals(plot.getLocation().getWorld())) {
					int sphereOfInfluence;
					if(plot.isCoownerOrAbove(player))
						sphereOfInfluence = plot.getSize();
					else {
						if(plot.isTown())
							sphereOfInfluence = plot.getSize()*2;
						else
							sphereOfInfluence = (int)Math.ceil(plot.getSize()*1.5);
					}
					
					if(Utils.isWithin(curLoc, plot.getLocation(), sphereOfInfluence+Variables.plotStartingSize))
						intersectingRegions.add(plot);
				}
			}
			
			if(intersectingRegions.size() == 0) {
				// money/diamonds verified, placement verified, name verified: good to go
				// first, remove the money/diamonds
				curMoney -= plotMoneyCost;
				pseudoPlayer.setMoney(curMoney);
				pseudoPlayer.setPlotCreatePoints(pseudoPlayer.getPlotCreatePoints()+7);
				//TODO Database.updatePlayerByPseudoPlayer(pseudoPlayer);
				// debited money successfully, now remove the proper amount of diamonds
				player.getInventory().remove(plotDiamondCost);		
				// costs paid, create the plot
				
				Plot plot = new Plot(plotName, player.getUniqueId(), curLoc);
				Lostshard.getPlots().add(plot);
				Database.insertPlot(plot);
			} else {
				player.sendMessage(ChatColor.DARK_RED+"Cannot create a plot there, too close to the following plots:");
				int maxDisplay = 6;
				if(intersectingRegions.size() < maxDisplay)
					maxDisplay = intersectingRegions.size();
				for(int i=0; i<maxDisplay; i++)
					player.sendMessage(ChatColor.DARK_RED+"-"+intersectingRegions.get(i).getName());
			}
	}

	//TODO add some messages and global static messages.
	public void plotDisband(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(!plot.isOwner(player)) {
			Output.simpleError(player, "Only the owner may disband "+plot.getName()+".");
			return;
		}
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		pPlayer.setMoney(pPlayer.getMoney()+plot.getValue());
		//Output positive message that plot has bin disbanded and the value of the plot.
		Output.positiveMessage(player, "You have disbanded "+plot.getName()+", and got "+plot.getValue()+"gc.");
		PlotHandler.removePlot(plot);
	}
	
	public void plotDeposit(Player player, String[] args) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null) {
			Output.plotNotIn(player);
			return;
		}
		if(!plot.isFriendOrAbove(player)) {
		Output.simpleError(player, "Only friends may deposit money into "+plot.getName()+".");
			return;
		}
		int amount;
		try{
			amount = Integer.parseInt(args[1]);
		}catch(Exception e) {
			Output.notNumber(player);
			return;
		}
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if(pPlayer.getMoney() >= amount) {
			plot.setMoney(plot.getMoney()+amount);
			pPlayer.setMoney(pPlayer.getMoney()-amount);
			Output.positiveMessage(player, "You have deposited "+amount+" gold coins into the plot fund.");
		}else{
			Output.simpleError(player, "You do not have that much money.");
			return;				
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
