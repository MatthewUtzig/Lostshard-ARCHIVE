package com.lostshard.RPG.Plots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;
//import com.thevoxelbox.bukkit.port.NPC.BasiNPCEntity;


public class PlotHandler {
	private static final int _startingMoneyCost = 1000;
	private static final int _startingDiamondCost = 1;
	private static final int _startingRadius = 10;
	private static final int _plotNameMaxLength = 20;
	/*private static final int _maxTaxDistance = 500;
	private static final int _maxTaxCostPerBlock = 10;*/
	private static ArrayList<Plot> _plots = null;
	private static ArrayList<Plot> _controlPoints = new ArrayList<Plot>();
	//private static ArrayList<SubPlot> _subPlots = null;
	
	public static void loadPlots() {
		try {
			_plots = Database.getPlots();
			for(Plot p : _plots) {
				if(p.isControlPoint()) {
					_controlPoints.add(p);
				}
			}
			System.out.println("Control Points Loaded: " + _controlPoints.size());
			/*_subPlots = Database.getSubPlots();
			for(SubPlot sP : _subPlots) {
				Plot p = getPlotById(sP.getId());
				p.addSubPlot(sP);
				sP.setOwningPlot(p);
			}
			System.out.println("Loaded "+_plots.size()+" Plots");
			System.out.println("Loaded "+_subPlots.size()+" Sub Plots");*/
		}
		catch (Exception e) {
			System.out.println("(CRITICAL ERROR) Exception loading plots: "+e.toString());
		}
	}
	
	public static Plot getPlotById(int id) {
		for(Plot p : _plots) {
			if(p.getId() == id)
				return p;
		}
		return null;
	}
	
	public static ArrayList<Plot> getControlPoints() {
		return _controlPoints;
	}
	
	public static void addControlPoint(Plot plot) {
		_controlPoints.add(plot);
	}
	
	public static void tick(double delta) {
		for(Plot p : _controlPoints) {
			try {
				p.tick(delta);
			}
			catch(Exception e) {e.printStackTrace();}
		}
	}
	
	public static void handleCommand(Player player, String[] split, RPG plugin) {
		if(split.length >= 2) {
			String plotCommand = split[1];
			if(plotCommand.equalsIgnoreCase("create")) {
				if(split.length >= 3)
					createPlot(player, split);
				else Output.simpleError(player, "Use \"/plot create (plot name)\"");
			}
			else if(plotCommand.equalsIgnoreCase("survey"))
				plotSurvey(player);
			else if(plotCommand.equalsIgnoreCase("info"))
				plotInfo(player);
			/*else if(plotCommand.equalsIgnoreCase("help")) {
				plotHelp(player);
			}*/
			else if(plotCommand.equalsIgnoreCase("friend")) {
				if(split.length == 3) {
					String friendName = split[2];
					plotFriend(player, friendName, plugin);
				}
				else Output.simpleError(player, "Use \"/plot friend (player name)\"");
			}
			else if(plotCommand.equalsIgnoreCase("co-own") || plotCommand.equalsIgnoreCase("coown") || plotCommand.equalsIgnoreCase("co-owner") || plotCommand.equalsIgnoreCase("coowner")) {
				if(split.length == 3) {
					String friendName = split[2];
					plotCoOwn(player, friendName, plugin);
				}
				else Output.simpleError(player, "Use \"/plot co-own (player name)\"");
			}
			else if(plotCommand.equalsIgnoreCase("unfriend")) {
				if(split.length == 3) {
					String friendName = split[2];
					plotUnFriend(player, friendName, plugin);
				}
				else Output.simpleError(player, "Use \"/plot unfriend (player name)\"");
			}
			else if(plotCommand.equalsIgnoreCase("protect"))
				plotProtect(player);
			else if(plotCommand.equalsIgnoreCase("unprotect"))
				plotUnProtect(player);
			else if(plotCommand.equalsIgnoreCase("private"))
				plotLock(player);
			else if(plotCommand.equalsIgnoreCase("public"))
				plotUnLock(player);
			else if(plotCommand.equalsIgnoreCase("expand"))
				plotExpand(player);
			else if(plotCommand.equalsIgnoreCase("deposit"))
				plotDeposit(player, split);
			else if(plotCommand.equalsIgnoreCase("withdraw"))
				plotWithdraw(player, split);
			else if(plotCommand.equalsIgnoreCase("test"))
				plotTest(player);
			else if(plotCommand.equalsIgnoreCase("endtest"))
				plotEndTest(player);
			else if(plotCommand.equalsIgnoreCase("disband"))
				plotDisband(player, split);
			else if(plotCommand.equalsIgnoreCase("list"))
				plotList(player, split);
			else if(plotCommand.equalsIgnoreCase("upgrade") || plotCommand.equalsIgnoreCase("upgrades"))
				plotUpgrade(player, split);
			else if(plotCommand.equalsIgnoreCase("transfer"))
				plotTransfer(player, split);
			else if(plotCommand.equalsIgnoreCase("shrink"))
				plotShrink(player, split);
			else if(plotCommand.equalsIgnoreCase("rename")) {
				if(split.length >= 3) {
					plotRename(player, split);
				}
				else Output.simpleError(player, "Use \"/plot rename (new name)\"");
			}
			else if(plotCommand.equalsIgnoreCase("friendbuild")) {
				plotFriendBuild(player, split);
			}
			else if(plotCommand.equalsIgnoreCase("npc")) {
				plotNPC(player, split);
			}
			else if(plotCommand.equalsIgnoreCase("sell")) {
				plotSell(player, split);
			}
			else if(plotCommand.equalsIgnoreCase("buy")) {
				plotBuy(player, split);
			}
			else if(plotCommand.equalsIgnoreCase("unsell") || plotCommand.equalsIgnoreCase("unlist") || plotCommand.equalsIgnoreCase("un-sell")) {
				plotUnSell(player, split);
			}
			else if(plotCommand.equalsIgnoreCase("explosion")) {
				plotExplosionToggle(player, split);
			}
		}
		else {
			Output.simpleError(player, "Use \"/help land\" for commands.");
		}
	}
	
	private static void createPlot(Player player, String[] split) {
		
		if(!player.isOp() && (player.getWorld() == RPG._theEndWorld2 || player.getWorld() == RPG._hungryWorld)) {
			Output.simpleError(player, "You can't buy a plot here apparently!");
			return;
		}
		
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		int curMoney = pseudoPlayer.getMoney();
		// determine the number of owned plots so we can increase the cost accordingly
		//int curOwnedRegions = getNumOwnedPlots(player.getName());
		
		//get recently purchased plots
		int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
		int plotsThisWeek = 0;
		if(plotCreatePoints > 0) {
			plotsThisWeek = (plotCreatePoints/7);
		}
		
		if(player.isOp())
			plotsThisWeek = 0;
		
		//TEMPORARY
		/*if(curOwnedRegions > 1) {
			Output.simpleError(player, "You can only own 1 region (Restriction will be lifted on Thursday)");
		}*/
		
		
		int plotMoneyCost = _startingMoneyCost;
		int plotDiamondCost = _startingDiamondCost;
		// for each owned plot, double the price
		for(int i=0; i<plotsThisWeek; i++) {
			plotMoneyCost *= 2;
			plotDiamondCost *= 2;
		}
		int numDiamonds = 0; 
		for (Map.Entry<Integer,? extends ItemStack> i: player.getInventory().all(264).entrySet()) {
			numDiamonds += i.getValue().getAmount();
		}
		// make sure the player has enough money and diamonds
		if(player.isOp() || ((curMoney >= plotMoneyCost) && (numDiamonds >= plotDiamondCost))) {
			// verify that this plot wouldn't intersect with an existing plot
			ArrayList<Plot> intersectingRegions = new ArrayList<Plot>();
			
			// figure out the name that the player input
			int splitNameLength = split.length;
			String plotName = "";
			for(int i=2; i<splitNameLength; i++) {
				plotName += split[i];
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
			if(nameLength <= _plotNameMaxLength && nameValid) {
				// find intersecting regions and check names to make sure there isn't a region with that name already
				Location curLoc = new Location(player.getWorld(), player.getLocation().getBlockX()+.5, player.getLocation().getBlockY()+.5, player.getLocation().getBlockZ()+.5);
				for(Plot plot : _plots) {
					if(plot.getName().equalsIgnoreCase(plotName)) {
						Output.simpleError(player, "A plot with that name already exists, please choose another.");
						return;
					}
					if(curLoc.getWorld().equals(plot.getLocation().getWorld())) {
						int sphereOfInfluence;
						if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName()))
							sphereOfInfluence = plot.getRadius();
						else {
							if(plot.isCity())
								sphereOfInfluence = plot.getRadius()*2;
							else
								sphereOfInfluence = (int)Math.ceil(plot.getRadius()*1.5);
						}
						
						if(Utils.isWithin(curLoc, plot.getLocation(), sphereOfInfluence+_startingRadius))
							intersectingRegions.add(plot);
					}
				}
				
				if(intersectingRegions.size() == 0) {
					// money/diamonds verified, placement verified, name verified: good to go
					// first, remove the money/diamonds
					curMoney -= plotMoneyCost;
					pseudoPlayer.setMoney(curMoney);
					pseudoPlayer.setPlotCreatePoints(pseudoPlayer.getPlotCreatePoints()+7);
					Database.updatePlayerByPseudoPlayer(pseudoPlayer);
					// debited money successfully, now remove the proper amount of diamonds
					player.getInventory().removeItem(new ItemStack(264, plotDiamondCost));
							
					// costs paid, create the plot
					Plot plot = new Plot(plotName, curLoc, _startingRadius, player.getName(), new ArrayList<String>(), new ArrayList<String>(), 0, true, true, false, new HashMap<String, LockedBlock>(), false, new ArrayList<PlotNPC>(), 0, false, false, false);
					boolean addedToDatabase = false;
					try {
						plot.setId(Database.addPlot(plot));
						addedToDatabase = true;
					}
					catch(Exception e) {
						Output.simpleError(player, "Server Error: Your plot was not created, contact admin if possible.");
						System.out.println("(ERROR) Failed to create a plot - name:"+player.getName()+", loc:"+curLoc.toString()+", moneyprice:"+plotMoneyCost+", diamondcost:"+plotDiamondCost);
					}
					if(addedToDatabase) {
						_plots.add(plot);
						Output.positiveMessage(player, "You have created the plot \""+plot.getName()+"\", it cost $"+plotMoneyCost+" and "+plotDiamondCost+" diamonds.");
					}
				}
				else {
					player.sendMessage(ChatColor.DARK_RED+"Cannot create a plot there, too close to the following plots:");
					int maxDisplay = 6;
					if(intersectingRegions.size() < maxDisplay)
						maxDisplay = intersectingRegions.size();
					for(int i=0; i<maxDisplay; i++)
						player.sendMessage(ChatColor.DARK_RED+"-"+intersectingRegions.get(i).getName());
				}
			}
			else Output.simpleError(player, "Error: Name is invalid or too long, limit is 20 characters.");
		}
		else Output.simpleError(player, "Cannot afford to create a plot, cost: "+plotMoneyCost+" gold & "+plotDiamondCost+" diamonds.");
	}
	
	private static void plotInfo(Player player) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			Output.displayPlotInfo(player, plot);
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	private static void plotCoOwn(Player player, String coOwnName, RPG plugin) {
		if(!player.getName().equalsIgnoreCase(coOwnName)) {
			Plot plot = findPlotAt(player.getLocation());
			if(plot != null) {
				if(plot.isOwner(player.getName()) || player.isOp()) {
					if(!plot.isCoOwner(coOwnName)) {
						Player coOwnPlayer = plugin.getServer().getPlayer(coOwnName);
						// If the player is already a friend we can promote him while offline
						if(plot.isFriend(coOwnName)) {
							String properName = plot.getProperFriendName(coOwnName);
							Output.positiveMessage(player, properName+" is now a co-owner of this plot.");
							if(coOwnPlayer != null)
								Output.positiveMessage(coOwnPlayer, "You are now a co-owner of "+plot.getName()+".");
							plot.removeFriend(properName);
							plot.addCoOwner(properName);
							Database.updatePlot(plot);
						}
						// However, if he is not a friend but he is online we will add him
						else if(coOwnPlayer != null) {
							Output.positiveMessage(player, coOwnPlayer.getName()+" is now a co-owner of this plot.");
							Output.positiveMessage(coOwnPlayer, "You are now a co-owner of "+plot.getName()+".");
							plot.removeFriend(coOwnPlayer.getName());
							plot.addCoOwner(coOwnPlayer.getName());
							Database.updatePlot(plot);
						}
						// But if he isn't online and isn't already a friend we won't co-own him
						else {
							Output.simpleError(player, "Cannot co-own that person, they are not online.");
						}
					}
					else Output.simpleError(player, coOwnName+" is already a co-owner of this plot.");
				}
				else Output.simpleError(player, "Only the owner of the plot may add co-owners.");
			}
			else Output.notInPlot(player);
		}
		else Output.simpleError(player, "You may not co-own yourself to this plot.");
	}
	
	private static void plotFriend(Player player, String friendName, RPG plugin) {
		if(!player.getName().equalsIgnoreCase(friendName)) {
			Plot plot = findPlotAt(player.getLocation());
			if(plot != null) {
				if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
					if(!plot.isFriend(friendName)) {
						Player friendPlayer = plugin.getServer().getPlayer(friendName);
						if(plot.isCoOwner(friendName)) {
							String properName = plot.getProperCoOwnerName(friendName);
							Output.positiveMessage(player, properName+" is now a friend of this plot.");
							if(friendPlayer != null)
								Output.positiveMessage(friendPlayer, "You are now a friend of "+plot.getName()+".");
							plot.removeCoOwner(properName);
							plot.addFriend(properName);
							Database.updatePlot(plot);
						}
						else if(friendPlayer != null) {
							Output.positiveMessage(player, friendPlayer.getName()+" is now a friend of this plot.");
							Output.positiveMessage(friendPlayer, "You are now a friend of "+plot.getName()+".");
							plot.removeCoOwner(friendPlayer.getName());
							plot.addFriend(friendPlayer.getName());
							Database.updatePlot(plot);
						}
						else {
							Output.simpleError(player, "Cannot friend that person, they are not online.");
						}
					}
					else Output.simpleError(player, friendName+" is already a friend of this plot.");
				}
				else Output.simpleError(player, "Only the owner or co-owner of a may add plot friends.");
			}
			else Output.notInPlot(player);
		}
		else Output.simpleError(player, "You may not friend yourself to this plot.");
	}
	
	private static void plotUnFriend(Player player, String friendName, RPG plugin) {
		if(!player.getName().equalsIgnoreCase(friendName)) {
			Plot plot = findPlotAt(player.getLocation());
			if(plot != null) {
				// Owners and co-owners can remove friends
				if(plot.isOwner(player.getName())) {
					// make sure we are trying to remove someone who is actually a friend
					if(plot.isFriend(friendName) || plot.isCoOwner(friendName)) {
						Player friendPlayer = plugin.getServer().getPlayer(friendName);
						if(friendPlayer == null) { // if the friend is not online
							String properName;
							if(plot.isFriend(friendName))
								properName = plot.getProperFriendName(friendName);
							else if(plot.isCoOwner(friendName))
								properName = plot.getProperCoOwnerName(friendName);
							else
								properName = friendName;
							
							Output.positiveMessage(player, properName+" is no longer a friend of this plot.");
							plot.removeFriend(friendName);
							plot.removeCoOwner(friendName);
							Database.updatePlot(plot);
						}
						else { // if the friend is online
							Output.positiveMessage(player, friendPlayer.getName()+" is no longer a friend of this plot.");
							Output.positiveMessage(friendPlayer, "You are no longer a friend of "+plot.getName()+".");
							plot.removeFriend(friendPlayer.getName());
							plot.removeCoOwner(friendPlayer.getName());
							Database.updatePlot(plot);
						}
					}
					else Output.simpleError(player, friendName+" is not a friend or co-owner of this plot.");
				}
				else if(plot.isCoOwner(player.getName())) {
					// make sure we are trying to remove someone who is actually a friend
					if(plot.isFriend(friendName)) {
						Player friendPlayer = plugin.getServer().getPlayer(friendName);
						if(friendPlayer == null) { // if the friend is not online
							Output.positiveMessage(player, friendName+" is no longer a friend of this plot.");
							plot.removeFriend(friendName);
							plot.removeCoOwner(friendName);
							Database.updatePlot(plot);
						}
						else { // if the friend is online
							Output.positiveMessage(player, friendName+" is no longer a friend of this plot.");
							Output.positiveMessage(friendPlayer, "You are no longer a friend of "+plot.getName()+".");
							plot.removeFriend(friendPlayer.getName());
							plot.removeCoOwner(friendPlayer.getName());
							Database.updatePlot(plot);
						}
					}
					else if(plot.isCoOwner(friendName)) {
						Output.simpleError(player, "Only the owner may unfriend a co-owner.");
					}
					else Output.simpleError(player, friendName+" is not a friend of this plot.");
				}
				else Output.simpleError(player, "Only the owner or co-owner of a may remove plot friends.");
			}
			else Output.notInPlot(player);
		}
		else Output.simpleError(player, "You may not unfriend yourself from this plot.");
	}

	/*private static void plotInvite(Player player, String inviteeName, RPG plugin) {
		Player invitedPlayer = plugin.getServer().getPlayer(inviteeName);
		if(invitedPlayer != null) {
			Plot plot = findPlotAt(player.getLocation());
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				if(!plot.isInvited(invitedPlayer.getName())) {
					plot.invite(invitedPlayer.getName());
					Output.positiveMessage(player, "You have invited "+invitedPlayer.getName()+" to the plot as a friend.");
				}
				else Output.simpleError(player, "\""+invitedPlayer.getName()+"\" has already been invited to the plot.");
			}
			else Output.simpleError(player, "Only the owner or co-owner of a plot may invite.");
		}
		else Output.simpleError(player, "Player \""+inviteeName+"\" is not currently online.");
	}*/
	
	private static void plotProtect(Player player) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				if(!plot.isProtected()) {
					plot.setProtect(true);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have turned protection on for the plot.");
				}
				else Output.simpleError(player, "This plot is already protected");
			}
			else Output.simpleError(player, "Only the owner or co-owners may turn protection on.");
		}
		else Output.notInPlot(player);
	}
	
	private static void plotUnProtect(Player player) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				if(plot.isProtected()) {
					plot.setProtect(false);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have turned protection off for the plot.");
				}
				else Output.simpleError(player, "This plot is already unprotected.");
			}
			else Output.simpleError(player, "Only the owner or co-owners may turn protection off.");
		}
		else Output.notInPlot(player);
	}
	
	private static void plotLock(Player player) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				if(!plot.isLocked()) {
					plot.setLocked(true);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have made the plot private.");
				}
				else {
					plot.setLocked(true);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "This plot is already private.");
				}
			}
			else Output.simpleError(player, "Only the owner or co-owners may make the plot private.");
		}
		else Output.notInPlot(player);
	}
	
	private static void plotUnLock(Player player) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				if(plot.isLocked()) {
					plot.setLocked(false);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have made the plot public.");
				}
				else {
					plot.setLocked(false);
					Database.updatePlot(plot);
					Output.simpleError(player, "This plot is already public.");
				}
			}
			else Output.simpleError(player, "Only the owner or co-owners may make the plot public.");
		}
		else Output.notInPlot(player);
	}
	
	private static void plotExpand(Player player) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				// see if we would expand into an existing region
				for(Plot p : _plots) {
					if(p.getLocation().getWorld().equals(player.getWorld())) {
						if(p != plot) {
							int sphereOfInfluence;
							if(p.isOwner(player.getName()) || p.isCoOwner(player.getName())) {
								sphereOfInfluence = p.getRadius();
							}
							else {
								if(p.isCity())
									sphereOfInfluence = p.getRadius()*2;
								else
									sphereOfInfluence = (int)Math.ceil(p.getRadius()*1.5);
							}
								
							if(Utils.isWithin(p.getLocation(), plot.getLocation(), sphereOfInfluence+plot.getRadius()+1)) {
								Output.simpleError(player, "Cannot expand, "+p.getName()+" is too close.");
								return;
							}
						}
					}
				}
				
				// verify that we can afford the expansion
				int plotMoney = plot.getMoney();
				int expansionCost = plot.getRadius()*10;
				if(plotMoney >= expansionCost) {
					// we are good to go
					plot.setMoney(plot.getMoney()-expansionCost);
					plot.setRadius(plot.getRadius()+1);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have expanded the plot to size "+plot.getRadius()+".");
				}
				else Output.simpleError(player, "Not enough money in the plot treasury to expand.");
			}
			else Output.simpleError(player, "Only the owner or co-owners may expand the plot.");
		}
		else Output.notInPlot(player);
	}
	
	private static void plotRename(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName())) {
				// verify that we can afford the rename
				int plotMoney = plot.getMoney();
				if(plotMoney >= 1000) {
					// figure out the name that the player input
					int splitNameLength = split.length;
					String plotName = "";
					for(int i=2; i<splitNameLength; i++) {
						plotName += split[i];
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
					if(nameLength <= _plotNameMaxLength && nameValid) {
						int numPlots = _plots.size();
						for(int i=0; i<numPlots; i++) {
							if(_plots.get(i).getName().equalsIgnoreCase(plotName)) {
								Output.simpleError(player, "Cannot use that name, it is taken.");
								return;
							}
						}
						// we are good to go
						plot.setMoney(plot.getMoney()-1000);
						plot.setName(plotName);
						Database.updatePlot(plot);
						Output.positiveMessage(player, "You have renamed the plot to "+plotName+".");
					}
					else Output.simpleError(player, "Error: Name is invalid or too long, limit is 20 characters.");
				}
				else Output.simpleError(player, "Not enough in the plot treasury to rename. $1000");
			}
			else Output.simpleError(player, "Only the owner may rename the plot.");
		}
		else Output.notInPlot(player);
	}
	
	private static void plotDeposit(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(!player.isOp() && (plot.getName().equalsIgnoreCase("Trinsic") || plot.getName().equalsIgnoreCase("buccaneers den"))) {
				Output.simpleError(player, "You cannot deposit money into "+plot.getName());
				return;
			}
			int amount = -1;
			try { amount=Integer.parseInt(split[2]); }
			catch(Exception e) {e.printStackTrace();}
			if(amount > 0) {
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
				if(pseudoPlayer.getMoney() >= amount) {
					pseudoPlayer.setMoney(pseudoPlayer.getMoney()-amount);
					plot.setMoney(plot.getMoney()+amount);
					Database.updatePlayerByPseudoPlayer(pseudoPlayer);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have deposited "+amount+" gold coins into the plot fund.");
				}
				else Output.simpleError(player, "You do not have that much money.");
			}
			else Output.simpleError(player, "Invalid amount.");
		}
		else Output.notInPlot(player);
	}
	
	private static void plotWithdraw(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				int amount = -1;
				try { amount=Integer.parseInt(split[2]); }
				catch(Exception e) {e.printStackTrace();}
				if(amount > 0) {
					PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
					if(plot.getMoney() >= amount) {
						plot.setMoney(plot.getMoney()-amount);
						pseudoPlayer.setMoney(pseudoPlayer.getMoney()+amount);
						Database.updatePlot(plot);
						Database.updatePlayerByPseudoPlayer(pseudoPlayer);
						Output.positiveMessage(player, "You have withdrawn "+amount+" gold coins from the plot fund.");
					}
					else Output.simpleError(player, "The plot does not have that many gold coins.");
				}
				else Output.simpleError(player, "Invalid amount.");
			}
			else Output.simpleError(player, "Only the owner or co-owners may withdraw from the plot funds.");
		}
		else Output.notInPlot(player);
	}
	
	// finds the closest plot to the location
	public static Plot findPlotAt(Location location) {
		if(_plots == null)
			return null;
		for(Plot plot : _plots) {
			if(plot == null || plot.getLocation() == null || plot.getLocation().getWorld() == null)
				continue;
			if(location == null)
				continue;
			if(location.getWorld() == null)
				continue;
			if(plot.getLocation().getWorld().equals(location.getWorld())) {
				if(Utils.isWithin(location, plot.getLocation(), plot.getRadius())) {
					return plot;
				}
			}
		}
		return null;
	}
	
	public static Plot findPlotAt(Location location, int buffer) {
		if(_plots == null)
			return null;
		for(Plot plot : _plots) {
			if(plot.getLocation().getWorld().equals(location.getWorld())) {
				if(Utils.isWithin(location, plot.getLocation(), plot.getRadius()+buffer)) {
					return plot;
				}
			}
		}
		return null;
	}
	
	private static void plotSurvey(Player player) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		ArrayList<Plot> plots = PlotHandler.getPlots();
		int numPlots = plots.size();
		
		// First, determine if we are currently in a plot
		boolean inPlot = false;
		if(plot != null) {
			inPlot = true;
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(ChatColor.YELLOW+"You "+ChatColor.RED+"cannot"+ChatColor.YELLOW+" create a plot here.");
			player.sendMessage(ChatColor.YELLOW+"You are currently in the plot \""+plot.getName()+"\".");
			
			//return;
		}
		
		// Determine the max range
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		int miningSkill = pseudoPlayer.getSkill("mining");
		double percent = (double)miningSkill/1000;
		int range = (int)Math.ceil(200*percent)+100;
		
		Location curLoc = new Location(player.getWorld(), player.getLocation().getBlockX()+.5, player.getLocation().getBlockY()+.5, player.getLocation().getBlockZ()+.5);
		
		// If we are not in a plot, determine if there are any plots within range
		ArrayList<Plot> plotsInRange = new ArrayList<Plot>();
		for(int i=0; i<numPlots; i++) {
			Plot p = plots.get(i);
			if(!p.getLocation().getWorld().equals(player.getWorld()))
				continue;
			if(plot != null && p.getName().equalsIgnoreCase(plot.getName()))
				continue;
			int border;
			if(p.isOwner(player.getName()) || p.isCoOwner(p.getName())) {
				border = p.getRadius();
			}
			else {
				if(p.isCity())
					border = p.getRadius() * 2;
				else
					border = (int)Math.ceil(p.getRadius() * 1.5);
			}
			
			// If a plot's border is within range of the player
			if(Utils.isWithin(curLoc, p.getLocation(), range+border)) {
				plotsInRange.add(p);
				//System.out.println("In range - "+p.getName());
			}
			else {
				//System.out.println("Out of range - "+p.getName());
			}
		}
		
		if(!inPlot) {
			// If there are no plots within range
			if(plotsInRange.size() == 0) {
				Output.positiveMessage(player, "-Plot Survey Results-");
				player.sendMessage(ChatColor.YELLOW+"You "+ChatColor.GOLD+"can"+ChatColor.YELLOW+" create a plot here.");
				player.sendMessage(ChatColor.YELLOW+"There are no plots within "+range+" blocks of here.");
				
				int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
				int plotsThisWeek = 0;
				if(plotCreatePoints > 0) {
					plotsThisWeek = (plotCreatePoints/7);
				}
				int plotMoneyCost = _startingMoneyCost;
				int plotDiamondCost = _startingDiamondCost;
				// for each owned plot, double the price
				for(int i=0; i<plotsThisWeek; i++) {
					plotMoneyCost *= 2;
					plotDiamondCost *= 2;
				}
				
				player.sendMessage(ChatColor.YELLOW+"It would cost $"+plotMoneyCost+" and "+plotDiamondCost+" diamonds to create a size 10 plot here.");
				return;
			}
		}
		
		// If there ARE plots within range
		double closestDist = 99999;
		Plot closestPlot = null;
		int numPlotsInRange = plotsInRange.size();
		for(int i=0; i<numPlotsInRange; i++) {
			Plot p = plotsInRange.get(i);
			int border;
			if(p.isOwner(player.getName()) || p.isCoOwner(p.getName())) {
				border = p.getRadius();
			}
			else {
				if(p.isCity())
					border = p.getRadius() * 2;
				else
					border = (int)Math.ceil(p.getRadius() * 1.5);
			}
				
			// Determine the distance from the player to the border of the plot
			double dist = Utils.distance(curLoc, p.getLocation()) - border;
			//System.out.println("Dist to "+p.getName()+" - "+dist);
			
			if(dist < closestDist) {
				closestDist = dist;
				closestPlot = p;
			}
		}
		
		if(!inPlot) {
			// Pretty much can't happen, but I'm checking for it anyway
			if(closestPlot == null) {
				Output.simpleError(player, "Something went wrong, tell an admin =(");
				return;
			}
			
			// Determine whether the closest plot border is within 10 blocks of the player's position (plot minimum size)
			if(closestDist <= _startingRadius) {
				Output.positiveMessage(player, "-Plot Survey Results-");
				player.sendMessage(ChatColor.YELLOW+"You "+ChatColor.RED+"cannot"+ChatColor.YELLOW+" create a plot here.");
				player.sendMessage(ChatColor.YELLOW+"This location is too close to the plot");
				player.sendMessage(ChatColor.YELLOW+"\""+closestPlot.getName()+"\".");
				return;
			}
			
			// We are not within the min plot size of an existing plot
			Output.positiveMessage(player, "-Plot Survey Results-");
			player.sendMessage(ChatColor.YELLOW+"You "+ChatColor.GOLD+"can"+ChatColor.YELLOW+" create a plot here.");
			player.sendMessage(ChatColor.YELLOW+"The nearest plot is \""+closestPlot.getName()+"\"");
			player.sendMessage(ChatColor.YELLOW+"it is "+(_startingRadius+(int)closestDist-10)+" blocks from here. If you created a");
			player.sendMessage(ChatColor.YELLOW+"plot here, you could expand it to about size "+(_startingRadius+(int)(closestDist-10)));
			
			int plotCreatePoints = pseudoPlayer.getPlotCreatePoints();
			int plotsThisWeek = 0;
			if(plotCreatePoints > 0) {
				plotsThisWeek = (plotCreatePoints/7);
			}
			int plotMoneyCost = _startingMoneyCost;
			int plotDiamondCost = _startingDiamondCost;
			// for each owned plot, double the price
			for(int i=0; i<plotsThisWeek; i++) {
				plotMoneyCost *= 2;
				plotDiamondCost *= 2;
			}
			
			player.sendMessage(ChatColor.YELLOW+"It would cost $"+plotMoneyCost+" and "+plotDiamondCost+" diamonds to create a size 10 plot here.");
			return;
		}
		else {
			if(plot != null) {
				if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
					if(closestPlot != null) {
						player.sendMessage(ChatColor.YELLOW+"The nearest plot is \""+closestPlot.getName()+"\"");
						player.sendMessage(ChatColor.YELLOW+"it is "+(_startingRadius+(int)closestDist-10)+" blocks from here.");
					}
					
					if(closestDist > range)
						closestDist = range;
					
					player.sendMessage(ChatColor.YELLOW+"If you expanded this plot, you could expand it to at least size "+(_startingRadius+(int)(closestDist-10)));
				}
			}
		}
	}
	
	public static void doTax() {
		System.out.println("starting tax");
		int numPlots = _plots.size();
		for(int i=numPlots-1; i>=0; i--) {
			Plot plot = _plots.get(i);
			
			//skip nether plots
			if(plot.getLocation().getWorld().getName().equalsIgnoreCase("nether"))
				continue;
			
			try {
				PseudoPlayer pseudoPlayer = Database.createPseudoPlayer(plot.getOwner());
				if(pseudoPlayer.isPremium())
					continue;
			}
			catch(Exception e) {
				System.out.println("Failed creating pseudoplayer for tax: " + e.toString());
			}
			
			int taxRate = 10;//taxRateAtLocation(plot.getLocation());
			int tax = taxRate * plot.getRadius();
			int plotMoney = plot.getMoney();
			if(plotMoney >= tax) {
				plot.setMoney(plot.getMoney()-tax);
				Database.updatePlot(plot);
			}
			else {
				if(plot.getRadius() > 1) {
					plot.setRadius(plot.getRadius()-1);
					Database.updatePlot(plot);
					Player owner = Utils.getPlugin().getServer().getPlayer(plot.getOwner());
					if(owner != null) {
						Output.simpleError(owner, "Your plot "+plot.getName()+" cannot afford its tax.");
						Output.simpleError(owner, "Its radius has shrunk to "+plot.getRadius()+".");
					}
					else Database.addOfflineMessage(plot.getOwner(), "Failed to pay tax on "+plot.getName()+", its radius has shrunk to "+plot.getRadius()+".");
				}
				else {
					Player owner = Utils.getPlugin().getServer().getPlayer(plot.getOwner());
					if(owner != null) {
						Output.simpleError(owner, "Your plot "+plot.getName()+" cannot afford its tax.");
						Output.simpleError(owner, "It has shrunk to 0 and has been removed.");
					}
					else Database.addOfflineMessage(plot.getOwner(), "Failed to pay tax on "+plot.getName()+", it has shrunk to 0 and has been disbanded.");
					_plots.remove(i);
					Database.removePlot(plot);
				}
			}
		}
		Database.resetGlobals();
		System.out.println("finished tax");
	}
	
	public static void plotTest(Player player) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			pseudoPlayer._plottest = plot;
			Output.positiveMessage(player, "You are currently testing "+plot.getName()+".");
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	public static void plotEndTest(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Plot plot = pseudoPlayer._plottest;
		if(plot != null) {
			Output.positiveMessage(player, "You are no longer testing "+plot.getName()+".");
			pseudoPlayer._plottest = null;
		}
		else Output.simpleError(player, "You are not testing a plot.");
	}
	
	public static void plotDisband(Player player, String[] split) {
		Plot plotFound = null;
		if(split.length <= 2) {
			plotFound = findPlotAt(player.getLocation());
		}
		else {
			
			int splitNameLength = split.length;
			String plotName = "";
			for(int i=2; i<splitNameLength; i++) {
				plotName += split[i];
				if(i < splitNameLength-1)
					plotName+= " ";
			}
			
			plotName = plotName.trim();
			
			for(Plot plot : _plots) {
				if(plot.getName().equalsIgnoreCase(plotName)) {
					plotFound = plot;
					break;
				}
			}
		}
		if(plotFound != null) {
			if(plotFound.isOwner(player.getName())) {
				_plots.remove(plotFound);
				Database.removePlot(plotFound);
				Output.positiveMessage(player, "You have disbanded "+plotFound.getName()+".");
				int plotMoney = plotFound.getMoney();
				/*int plotSize = plotFound.getRadius();
				int adjustedPlotSize = plotSize - 10;
				int plotValue = 0;
				for(int i=0; i<adjustedPlotSize; i++) {
					int curSizeValue = (i+10)*10;
					plotValue+=curSizeValue;
				}
				int adjustedPlotValue = (int)Math.floor(plotValue * .75);*/
				int plotValue = plotFound.getValue();
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
				pseudoPlayer.setMoney(pseudoPlayer.getMoney()+plotValue+plotMoney);
				Database.updatePlayerByPseudoPlayer(pseudoPlayer);
				Output.positiveMessage(player, "You have been refunded 75% of the plot value ($"+plotValue+")");
				Output.positiveMessage(player, "You have been given the "+plotFound.getName()+" funds ("+plotMoney+")");
			}
			else Output.simpleError(player, "Only the plot owner may disband the plot.");
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	public static void plotList(Player player, String[] split) {
		if(split.length == 3 && player.isOp()) {
			String name = split[2];
			Output.positiveMessage(player, "-"+name+"'s Plots-");
			for(Plot plot : _plots) {
				if(plot.isOwner(name)) {
					player.sendMessage(" - "+plot.getName()+" ("+plot.getRadius()+") @("+plot.getLocation().getBlockX()+","+plot.getLocation().getBlockY()+","+plot.getLocation().getBlockZ()+")");
				}
			}
		}
		else {
			Output.positiveMessage(player, "-"+player.getName()+"'s Plots-");
			boolean foundOne = false;
			for(Plot plot : _plots) {
				if(plot.isOwner(player.getName())) {
					foundOne = true;
					player.sendMessage(" - "+plot.getName()+" ("+plot.getRadius()+") @("+plot.getLocation().getBlockX()+","+plot.getLocation().getBlockY()+","+plot.getLocation().getBlockZ()+")");
				}
			}
		
			if(!foundOne) 
				Output.simpleError(player, "You do not currently own any plots.");
		}
	}
	
	private static void plotUpgrade(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			int numAvail = 0;
			Output.positiveMessage(player, plot.getName()+"'s Upgrades:");
			
			if(plot.isCity()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW+"- Town");
			}
			
			if(plot.isDungeon()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW+"- Dungeon");
			}
			
			if(plot.hasKickUpgrade()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW+"- AutoKick (non-friend login auto eject)");
			}
			
			if(plot.isNeutral()) {
				numAvail++;
				player.sendMessage(ChatColor.YELLOW+"- Neutral");
			}
			
			if(numAvail <= 0) {
				Output.simpleError(player, plot.getName()+" has not been upgraded.");
			}
			
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				if(split.length == 2) {// someone only typed /plot upgrade
					Output.positiveMessage(player, "-Plot Upgrades Available-)");
					numAvail = 0;
					if(!plot.isCity()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW+"- Town [$100,000]");
					}
					
					if(!plot.isDungeon()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW+"- Dungeon [$20,000]");
					}
					
					if(!plot.hasKickUpgrade()) {
						numAvail++;
						player.sendMessage(ChatColor.YELLOW+"- AutoKick [$10,000]");
					}
					
					if(plot.isCity()) {
						if(!plot.isNeutral()) {
							numAvail++;
							player.sendMessage(ChatColor.YELLOW+"- Neutral Alignment [$4,000]");
						}
					}					
				}
				else if(split.length >= 3) { // someone typed /plot upgrade (something)
					if(split[2].equalsIgnoreCase("town")) {
						if(!plot.isCity()) {
							if(plot.getMoney() >= 100000) {
								plot.setMoney(plot.getMoney()-100000);
								plot.setIsCity(true);
								Database.updatePlot(plot);
								Output.positiveMessage(player, plot.getName()+" upgraded to a town.");
							}
							else Output.simpleError(player, "Not enough money in plot funds. ($100,000)");
						}
						else Output.simpleError(player, plot.getName()+" is already a town.");
					}
					else if(split[2].equalsIgnoreCase("dungeon")) {
						if(!plot.isDungeon()) {
							if(plot.getMoney() >= 20000) {
								plot.setMoney(plot.getMoney()-20000);
								plot.setIsDungeon(true);
								Database.updatePlot(plot);
								Output.positiveMessage(player, plot.getName()+" upgraded to a dungeon.");
							}
							else Output.simpleError(player, "Not enough money in plot funds. ($20,000)");
						}
						else Output.simpleError(player, plot.getName()+" is already a dungeon.");
					}
					else if(split[2].equalsIgnoreCase("autokick")) {
						if(!plot.hasKickUpgrade()) {
							if(plot.getMoney() >= 10000) {
								plot.setMoney(plot.getMoney()-10000);
								plot.setKickUpgrade(true);
								Database.updatePlot(plot);
								Output.positiveMessage(player, plot.getName()+" upgraded with AutoKick.");
							}
							else Output.simpleError(player, "Not enough money in plot funds. ($10,000)");
						}
						else Output.simpleError(player, plot.getName()+" already has the AutoKick upgrade.");
					}
					else if(split[2].equalsIgnoreCase("neutral")) {
						if(plot.isCity()) {
							if(!plot.isNeutral()) {
								if(plot.getMoney() >= 4000) {
									plot.setMoney(plot.getMoney()-4000);
									plot.setNeutral(true);
									Database.updatePlot(plot);
									Output.positiveMessage(player, plot.getName()+" upgraded to Neutral Alignment.");
								}
								else Output.simpleError(player, "Not enough money in plot funds. ($4,000)");
							}
							else Output.simpleError(player, plot.getName()+" already has the Neutral Alignment upgrade.");
						}
						else Output.simpleError(player, plot.getName()+" must be a town to purchase this upgrade.");
					}
					else Output.simpleError(player, split[2]+" upgrade is not available.");
				}
			}
			else Output.simpleError(player, "Only the owner and co-owners may upgrade the plot.");
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	private static void plotTransfer(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName()) || player.isOp()) {
				if(split.length == 2) {// someone only typed /plot upgrade
					player.sendMessage(ChatColor.GRAY+"/plot transfer (player name) will transfer a plot");
					player.sendMessage(ChatColor.GRAY+"to the player named.");
				}
				else if(split.length >= 3) { // someone typed /plot upgrade (something)
					String targetName = split[2];
					Player targetPlayer = player.getServer().getPlayer(targetName);
					if(targetPlayer != null) {
						plot.removeCoOwner(targetPlayer.getName());
						plot.removeFriend(targetPlayer.getName());
						plot.setOwner(targetPlayer.getName());
						Database.updatePlot(plot);
						Output.positiveMessage(player, "Transferred plot \""+plot.getName()+"\" to "+targetPlayer.getName());
						Output.positiveMessage(targetPlayer, player.getName()+" transferred plot \""+plot.getName()+"\" to you.");
					}
					else Output.simpleError(player, targetName+" not found.");
				}
			}
			else Output.simpleError(player, "Only the owner may transfer the plot.");
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	private static void plotShrink(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName())) {
				if(split.length == 2) {
					player.sendMessage(ChatColor.GRAY+"/plot shrink (amount) will shrink the plot");
				}
				else if(split.length >= 3) {
					int amount = -1;
					try {
						amount = Integer.parseInt(split[2]);
					}
					catch(Exception e) {
					}
					
					if(amount < 1) {
						Output.simpleError(player, "Cannot shrink less than 1 block.");
						return;
					}
					
					if(plot.getRadius() <= amount) {
						Output.simpleError(player, "Cannot shrink that much.");
						return;
					}
					
					plot.setRadius(plot.getRadius()-amount);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have shrunk the plot by "+amount+" blocks.");
				}
			}
			else Output.simpleError(player, "Only the owner may shrink the plot.");
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	public static void plotFriendBuild(Player player, String[] split) {
		if(split.length == 3) {
			Plot plot = PlotHandler.findPlotAt(player.getLocation());
			if(plot != null) {
				if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
					if(split[2].equalsIgnoreCase("on")) {
						if(!plot.isFriendBuild()) {
							plot.setFriendBuild(true);
							Database.updatePlot(plot);
							Output.positiveMessage(player, "You have turned on friend build for "+plot.getName()+".");
						}
						else Output.simpleError(player, plot.getName()+" is already set to allow friend build.");
					}
					else if(split[2].equalsIgnoreCase("off")) {
						if(plot.isFriendBuild()) {
							plot.setFriendBuild(false);
							Database.updatePlot(plot);
							Output.positiveMessage(player, "You have turned off friend build for "+plot.getName()+".");
						}
						else Output.simpleError(player, plot.getName()+" is already set to not allow friend build.");
					}
					else Output.simpleError(player, "Use /plot friendbuild on/off");
				}
			}
		}
		else Output.simpleError(player, "Use /plot friendbuild on/off");
	}
	
	public static void plotNPC(Player player, String[] split) {
		if(split.length >= 3) {
			Plot plot = PlotHandler.findPlotAt(player.getLocation());
			if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
				// co/owner of the plot
				if(split[2].equalsIgnoreCase("hire")) {
					if(split[3].equalsIgnoreCase("banker")) {
						if(!plot.isCity()){
							Output.simpleError(player, "You can only place a banker in a town.");
							return;
						}
						if(split.length == 5) {
							String bankerName = split[4];
							bankerName = bankerName.trim();
							if(bankerName.length() > 7) {
								Output.simpleError(player, "Banker name must be 7 characters or less.");
								return;
							}
							
							if(bankerName.length() < 2) {
								Output.simpleError(player, "Banker name must be more 1 character or more.");
								return;
							}
							
							if(bankerName.contains("\"") || bankerName.contains("'") || bankerName.contains(" ")) {
								Output.simpleError(player, "Cannot use \" or spaces in NPC names.");
								return;
							}
							
							bankerName = "[Banker] "+bankerName;
							
							ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
							int numPlotNPCs = plotNPCs.size();
							
							for(int i=0; i<numPlotNPCs; i++) {
								if(plotNPCs.get(i).getJob().equalsIgnoreCase("banker")) {
									Output.simpleError(player, "You may only have 1 banker per plot.");
									return;
								}
							}
							
							if(NPCHandler._npcManager.getNPC(bankerName) != null) {
								Output.simpleError(player, "An NPC with that name already exists.");
								return;
							}
							
							// must be a unique name and there must not be any other bankers
							int npcId = Database.addPlotNPC(plot.getId(), bankerName, player.getLocation(), "banker", "");
							//BasicHumanNpc npc = NPCHandler._npcManager.spawnNPC(bankerName, player.getLocation(), bankerName);
							PlotNPC plotNPC = new PlotNPC(npcId, plot.getId(), bankerName, player.getLocation(), "banker", null, "");
							plotNPCs.add(plotNPC);
							
							
							//NPCHandler.addPlotNPC(plotNPC);
							
							/*Chunk chunk = plotNPC.getLocation().getWorld().getChunkAt(plotNPC.getLocation());
							if(!plotNPC.getLocation().getWorld().isChunkLoaded(chunk)) {
								plotNPC.getLocation().getWorld().loadChunk(chunk);
							}
							String str = chunk.toString();
							if(!RPG._permChunks.contains(str))
								RPG._permChunks.add(str);*/
							
							Output.positiveMessage(player, "You have created a banker named "+bankerName+".");
						}
						else Output.simpleError(player, "Use /plot npc hire banker (banker name)");
					}
					else if(split[3].equalsIgnoreCase("vendor")) {
						if(split.length == 5) {
							String vendorName = split[4];
							vendorName = vendorName.trim();
							if(vendorName.length() > 7) {
								Output.simpleError(player, "Vendor name must be 7 characters or less.");
								return;
							}
							
							if(vendorName.length() < 2) {
								Output.simpleError(player, "Vendor name must be more 1 character or more.");
								return;
							}
							
							if(vendorName.contains("\"") || vendorName.contains("'") || vendorName.contains(" ")) {
								Output.simpleError(player, "Cannot use \" or spaces in NPC names.");
								return;
							}
							
							vendorName = "[VEND] "+vendorName;
							
							ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
							int numPlotNPCs = plotNPCs.size();
							
							int numVendors = 0;
							for(int i=0; i<numPlotNPCs; i++) {
								if(plotNPCs.get(i).getJob().equalsIgnoreCase("vendor")) {
									numVendors++;
								}
							}
							
							if(numVendors >= plot.getRadius() / 15) {
								Output.simpleError(player, "You may only have 1 vendor per 15 size, this plot can have "+(plot.getRadius()/15)+" vendors and you currently have "+numVendors+".");
								return;
							}
							
							if(NPCHandler._npcManager.getNPC(vendorName) != null) {
								Output.simpleError(player, "An NPC with that name already exists.");
								return;
							}
							
							// must be a unique name and there must not be any other bankers
							Store store = new Store(vendorName+"'s Store", 5, player.getLocation());
							store.setType(1);
							int storeId = Database.addStore(store);
							store.setId(storeId);
							int npcId = Database.addPlotNPC(plot.getId(), vendorName, player.getLocation(), "vendor", ""+storeId);
							//BasicHumanNpc npc = NPCHandler._npcManager.spawnNPC(vendorName, player.getLocation());
							PlotNPC plotNPC = new PlotNPC(npcId, plot.getId(), vendorName, player.getLocation(), "vendor", null, ""+storeId);
							plotNPCs.add(plotNPC);
							RPG._stores.add(store);
							/*Chunk chunk = plotNPC.getLocation().getWorld().getChunkAt(plotNPC.getLocation());
							if(!plotNPC.getLocation().getWorld().isChunkLoaded(chunk)) {
								plotNPC.getLocation().getWorld().loadChunk(chunk);
							}
							String str = chunk.toString();
							if(!RPG._permChunks.contains(str))
								RPG._permChunks.add(str);*/
							
							Output.positiveMessage(player, "You have created a vendor named "+vendorName+".");
						}
						else Output.simpleError(player, "Use /plot npc hire vendor (vendor name)");
					}
					/*else if(split[3].equalsIgnoreCase("guard")) {
						
					}*/
					else Output.simpleError(player, "You can only create a banker or vendor at this time.");
				}
				else if(split[2].equalsIgnoreCase("move")) {
					if(split.length >= 4) {
						int splitNameLength = split.length;
						String npcName = "";
						for(int i=3; i<splitNameLength; i++) {
							npcName += split[i];
							if(i < splitNameLength-1)
								npcName+= " ";
						}
						npcName = npcName.trim();
						
						ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
						int numPlotNPCs = plotNPCs.size();
						
						boolean found = false;
						
						for(int i=0; i<numPlotNPCs; i++) {
							PlotNPC npc = plotNPCs.get(i);
							if(npc.getName().equalsIgnoreCase(npcName)) {
								//NPCHandler._npcManager.moveNPC(npcName, player.getLocation());
								npc.setLocation(player.getLocation());
								Database.updatePlotNPC(npc);
								//Chunk chunk = player.getWorld().getChunkAt(player.getLocation());
								//NPCHandler._NPCChunkHashSet.add(chunk.toString());
								if(npc.getJob().equalsIgnoreCase("vendor")) {
									// move store as well
									for(int j=0; j<RPG._stores.size(); j++) {
										if(RPG._stores.get(j).getId() == npc.getStoreId()) {
											RPG._stores.get(j).setLocation(npc.getLocation());
											Database.updateStore(RPG._stores.get(j));
											break;
										}
									}
								}
								Output.positiveMessage(player, "You have moved "+npcName+".");
								found=true;
								break;
							}
						}
						
						if(!found)
							Output.simpleError(player, "Cannot find an NPC named "+npcName+" on this plot.");
					}
					else Output.simpleError(player, "Use /plot npc move (npc name)");
				}
				else if(split[2].equalsIgnoreCase("fire")) {
					if(split.length >= 4) {					
						int splitNameLength = split.length;
						String npcName = "";
						for(int i=3; i<splitNameLength; i++) {
							npcName += split[i];
							if(i < splitNameLength-1)
								npcName+= " ";
						}
						npcName = npcName.trim();

						ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
						int numPlotNPCs = plotNPCs.size();
						
						boolean found = false;
						
						for(int i=0; i<numPlotNPCs; i++) {
							PlotNPC npc = plotNPCs.get(i);
							if(npc.getName().equals(npcName)) {
								NPCHandler._npcManager.despawn(npcName);
								plotNPCs.remove(i);
								Database.removePlotNPC(npc.getId());
								Output.positiveMessage(player, "You have fired "+npcName+".");
								if(npc.getJob().equalsIgnoreCase("vendor")) {
									// We are firing a vendor, remove the associated store
									for(int j=0; j<RPG._stores.size(); j++) {
										if(RPG._stores.get(j).getId() == npc.getStoreId()) {
											RPG._stores.remove(j);
											break;
										}
									}
									Database.removeStore(npc.getStoreId());
								}
								found=true;
								break;
							}
						}
						
						if(!found)
							Output.simpleError(player, "Cannot find an NPC named "+npcName+" on this plot. Case sensitive.");
						
					}
					else Output.simpleError(player, "Use /plot npc fire (npc name)");
				}
				else if(split[2].equalsIgnoreCase("list")) {
					player.sendMessage(ChatColor.GOLD+"-"+plot.getName()+"'s NPCs-");
					ArrayList<PlotNPC> plotNPCs = plot.getPlotNPCs();
					int numPlotNPCs = plotNPCs.size();
					
					if(numPlotNPCs <= 0) {
						player.sendMessage(ChatColor.RED+"No plot NPCs");
					}
					else {
						for(int i=0; i<numPlotNPCs; i++) {
							player.sendMessage(ChatColor.YELLOW+plotNPCs.get(i).getName()+" ("+plotNPCs.get(i).getJob()+")");
						}
					}
				}
			}
			else Output.simpleError(player, "Only the owner and co-owner may manage plot NPCs");
		}
		else Output.simpleError(player, "Invalid Syntax - wiki.lostshard.com for help.");
	}
	
	public static void plotSell(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName())) {
				if(split.length == 2) {
					player.sendMessage(ChatColor.GRAY+"Use /plot sell (amount)");
				}
				else if(split.length >= 3) {
					int amount = -1;
					try {
						amount = Integer.parseInt(split[2]);
					}
					catch(Exception e) {}
					
					if(amount < 1) {
						Output.simpleError(player, "Invalid price, must be 1 or greater");
						return;
					}
					
					if(amount > 9999999) {
						Output.simpleError(player, "Max sale price is 9999999");
						return;
					}
					
					plot.setForSale(amount);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "You have set this plot for sale at $"+amount+".");
				}
			}
			else Output.simpleError(player, "Only the owner may sell the plot.");
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	public static void plotUnSell(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isOwner(player.getName())) {
				if(split.length == 2) {
					plot.setForSale(0);
					Database.updatePlot(plot);
					Output.positiveMessage(player, "This plot is no longer for sale.");
				}
			}
			else Output.simpleError(player, "Only the owner may unsell the plot.");
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	public static void plotBuy(Player player, String[] split) {
		Plot plot = findPlotAt(player.getLocation());
		if(plot != null) {
			int salePrice = plot.getSaleCost();
			if(salePrice <= 0) {
				Output.simpleError(player, "This plot is not for sale.");
				return;
			}
			
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			
			if(pseudoPlayer.getMoney() < salePrice) {
				Output.simpleError(player, "Cannot afford to buy plot, cost: "+salePrice+".");
				return;
			}
			
			pseudoPlayer.setMoney(pseudoPlayer.getMoney() - salePrice);
			Database.updatePlayerByPseudoPlayer(pseudoPlayer);
			
			String lastOwner = plot.getOwner();
			plot.setOwner(player.getName());
			plot.setForSale(0);
			plot.getCoOwners().clear();
			plot.getFriends().clear();
			Database.updatePlot(plot);
			
			Output.positiveMessage(player, "You have purchased the plot "+plot.getName()+" from "+lastOwner+" for "+salePrice+".");
			Player sellerPlayer = player.getServer().getPlayer(lastOwner);
			if(sellerPlayer != null) {
				PseudoPlayer sellerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(sellerPlayer.getName());
				sellerPseudoPlayer.setMoney(sellerPseudoPlayer.getMoney() + salePrice);
				Database.updatePlayerByPseudoPlayer(sellerPseudoPlayer);
				sellerPlayer.sendMessage("You have sold the plot "+plot.getName()+" to "+player.getName()+" for "+salePrice+".");
			}
			else {
				PseudoPlayer sellerPseudoPlayer = Database.createPseudoPlayer(lastOwner);
				sellerPseudoPlayer.setMoney(sellerPseudoPlayer.getMoney() + salePrice);
				Database.updatePlayerByPseudoPlayer(sellerPseudoPlayer);
				PseudoPlayerHandler.removed(lastOwner);
				Database.addOfflineMessage(lastOwner, "You have sold the plot "+plot.getName()+" to "+player.getName()+" for "+salePrice+".");
			}
			
		}
		else Output.simpleError(player, "You are not currently in a plot.");
	}
	
	public static void plotExplosionToggle(Player player, String[] split) {
		if(split.length >= 2) {
			Plot plot = findPlotAt(player.getLocation());
			if(plot != null) {
				if(plot.isOwner(player.getName()) || plot.isCoOwner(player.getName())) {
					if(plot.isExplosionAllowed()) {
						plot.setExplosionAllowed(false);
						Output.positiveMessage(player, "You have disabled explosions on your plot.");
					}
					else {
						plot.setExplosionAllowed(true);
						Output.positiveMessage(player, "You have enabled explosions on your plot.");
					}
					Database.updatePlot(plot);
				}
				else Output.simpleError(player, "Only the owner or co-owner of a plot may toggle explosion permission.");
			}
			else Output.simpleError(player, "You are not currently in a plot.");
		}
		else Output.simpleError(player, "Use /plot explosion to toggle explosion permission.");
	}
	
	public static ArrayList<Plot> getPlots() {
		return _plots;
	}

}
