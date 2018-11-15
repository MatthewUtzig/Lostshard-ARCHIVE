package com.lostshard.RPG.Plots;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Clans.ClanHandler;
import com.lostshard.RPG.Utils.Output;

public class Plot {
	public static String GORP_PLOT_NAME = "Gorps Magic Shit";
	public static String BUNT_PLOT_NAME = "Bunts Fiddly Bits";
	
	private int _id;
	private String _name;
	private Location _location;
	private int _radius;
	private String _owner;
	private ArrayList<String> _coOwners;
	private ArrayList<String> _friends;
	private int _money;
	private boolean _protect;
	private boolean _locked;
	private boolean _city;
	private boolean _dungeon;
	private boolean _kickUpgrade;
	private boolean _neutral;
	private HashMap<String, LockedBlock> _lockedBlocks = new HashMap<String, LockedBlock>();
	private boolean _friendBuild;
	private ArrayList<PlotNPC> _plotNPCs = new ArrayList<PlotNPC>();
	private int _forSaleAmount = 0;
	private boolean _explosionAllowed = false;
	private Clan _owningClan = null;
	private boolean _isControlPoint = false;
	private long _lastCaptureTime = 0;
	private boolean _capturing = false;
	public String _capturingPlayerName = "";
	private Clan _capturingClan = null;
	private ArrayList<SubPlot> _subPlots = new ArrayList<SubPlot>();
	private ArrayList<String> _recentCaptureFailNames = null;
	
	private double _claimSecRemaining = 0;
	private int _recentClaims = 0;
	private double _timeoutSecRemaining = 0;
	
	private double _refractoryPeriod = 0;
	public boolean _capturedRecently = false;
	
	public boolean _isNoMagicPlot = false;
	public boolean _isNoPvPPlot = false;
	
	public Plot(String name, Location location, int radius, String owner, ArrayList<String> coOwners, ArrayList<String> friends, int money, boolean protect, boolean locked, boolean city, HashMap<String, LockedBlock> lockedBlocks, boolean friendBuild, ArrayList<PlotNPC> plotNPCs, int forSaleAmount, boolean kickUpgrade, boolean neutral, boolean explosionAllowed) {
		_name = name;
		_location = location;
		_radius = radius;
		_owner = owner;
		_coOwners = coOwners;
		_friends = friends;
		_money = money;
		_protect = protect;
		_locked = locked;
		_city = city;
		_lockedBlocks = lockedBlocks;
		_friendBuild = friendBuild;
		_plotNPCs = plotNPCs;
		_forSaleAmount = forSaleAmount;
		_kickUpgrade = kickUpgrade;
		_neutral = neutral;
		_explosionAllowed = explosionAllowed;
	}
	
	public double getRefractoryPeriodRemaining() {
		return _refractoryPeriod;
	}
	
	public void tick(double delta) {
		delta /= 10;
		
		if(_refractoryPeriod > 0) {
			_refractoryPeriod -= delta;
			if(_refractoryPeriod <= 0) {
				Player[] players = Bukkit.getOnlinePlayers();
				for(Player p : players) {
					p.sendMessage(ChatColor.GREEN+this.getName()+" is now vulnerable to capture.");
				}
				_capturedRecently = false;
				_recentCaptureFailNames.clear();
			}
		}
		// Handle the timeout. When the time runs out we clear the recent claims
		if(_timeoutSecRemaining > 0) {
			_timeoutSecRemaining -= delta;
			if(_timeoutSecRemaining <= 0) {
				_recentClaims = 0;
			}
		}
		
		if(_capturing) {					
			_claimSecRemaining -= delta;
			if(_claimSecRemaining <= 0) {
				_capturing = false;
				_recentClaims++;
				if(_recentClaims >= 3) {						
					Player[] players = Bukkit.getOnlinePlayers();
					for(Player p : players) {
						p.sendMessage(ChatColor.GREEN+this.getName()+" has been captured by "+_capturingClan.getName()+".");
					}
					_recentClaims = 0;
					_timeoutSecRemaining = 0;
					_capturing = false;
					_owningClan = _capturingClan;
					_capturingClan = null;
					
					if(this.getName().equalsIgnoreCase(GORP_PLOT_NAME))
						ClanHandler.SetGorpControlClan(_owningClan);
					if(this.getName().equalsIgnoreCase(BUNT_PLOT_NAME))
						ClanHandler.SetBuntControlClan(_owningClan);
					
					Player capturingPlayer = Bukkit.getPlayer(_capturingPlayerName);
					if(capturingPlayer != null) {
						PseudoPlayer capturingPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(capturingPlayer.getName());
						capturingPseudoPlayer._claiming = false;
					}
					_capturedRecently = true;
					_refractoryPeriod = 60*60;
					Date date = new Date();
					this.setLastCaptureTime(date.getTime());
					this._capturingPlayerName = "";
					Database.updatePlot(this);
				}
				else {
					int timesLeft = 3 - _recentClaims;
					if(_owningClan != null) {
						if(_recentClaims == 1) {
							if(timesLeft == 1)
								Output.clanMessage(_owningClan, this.getName()+" has been claimed "+_recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								Output.clanMessage(_owningClan, this.getName()+" has been claimed "+_recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
						else {
							if(timesLeft == 1)
								Output.clanMessage(_owningClan, this.getName()+" has been claimed "+_recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
							Output.clanMessage(_owningClan, this.getName()+" has been claimed "+_recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
					}
					
					if(_capturingClan != null) {
						if(_recentClaims == 1) {
							if(timesLeft == 1)
								Output.clanMessage(_capturingClan, this.getName()+" has been claimed "+_recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								Output.clanMessage(_capturingClan, this.getName()+" has been claimed "+_recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
						else {
							if(timesLeft == 1)
								Output.clanMessage(_capturingClan, this.getName()+" has been claimed "+_recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
							Output.clanMessage(_capturingClan, this.getName()+" has been claimed "+_recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
					}
					Player capturingPlayer = Bukkit.getPlayer(_capturingPlayerName);
					if(capturingPlayer != null) {
						PseudoPlayer capturingPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(capturingPlayer.getName());
						capturingPseudoPlayer._claiming = false;
					}
					//Output.clanMessage(_capturingClan, this.getName()+" has been claimed "+_recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 5 minutes it will reset.");
				}
				
				
			}
			
			/*_captureTime += delta;
			
			
			if(!notified4minute && _captureTime > 60) {
				notified4minute = true;
				if(_owningClan != null)
					Output.clanMessage(_owningClan, getName()+" is under attack by "+_capturingPlayerName+", It will be captured in 4 minutes!");
				Output.clanMessage(_capturingClan, _capturingPlayerName+" is capturing "+this.getName()+" for your clan, in 4 minutes it will be captured !");
			}
			if(!notified3minute && _captureTime > 120) {
				notified3minute = true;
				if(_owningClan != null)
					Output.clanMessage(_owningClan, getName()+" is under attack by "+_capturingPlayerName+", It will be captured in 3 minutes!");
				Output.clanMessage(_capturingClan, _capturingPlayerName+" is capturing "+this.getName()+" for your clan, It will be captured in 3 minutes!");
			}
			if(!notified2minute && _captureTime > 180) {
				notified2minute = true;
				if(_owningClan != null)
					Output.clanMessage(_owningClan, getName()+" is under attack by "+_capturingPlayerName+", It will be captured in 2 minutes!");
				Output.clanMessage(_capturingClan, _capturingPlayerName+" is capturing "+this.getName()+" for your clan, It will be captured in 2 minutes!");
			}
			if(!notified1minute && _captureTime > 240) {
				notified1minute = true;
				if(_owningClan != null)
					Output.clanMessage(_owningClan, getName()+" is under attack by "+_capturingPlayerName+", It will be captured in 1 minute!");
				Output.clanMessage(_capturingClan, _capturingPlayerName+" is capturing "+this.getName()+" for your clan, It will be captured in 1 minutes!");
			}
			if(_captureTime > _captureTimeLength) {
				Player capturingPlayer = Bukkit.getPlayer(_capturingPlayerName);
				if(capturingPlayer == null) {
					
				}
				if(capturingPlayer.getLocation().getWorld().equals(getLocation().getWorld()) && Utils.distance(capturingPlayer.getLocation(), this.getLocation())  <= this.getRadius()) {
					if(_owningClan != null)
						Output.clanMessage(_owningClan, getName()+" has been captured!");
					Output.clanMessage(_capturingClan, _capturingPlayerName+" has captured "+getName()+" for your clan!");
					
					Player[] players = Bukkit.getOnlinePlayers();
					for(Player p : players) {
						p.sendMessage(ChatColor.GREEN+_capturingClan.getName()+" has captured "+this.getName()+".");
					}
					
					Date date = new Date();
					setLastCaptureTime(date.getTime());
					_owningClan = _capturingClan;
					_capturingClan = null;
					Database.updatePlot(this);
				}
				else {
					if(_owningClan != null)
						Output.clanMessage(_owningClan, _capturingClan.getName()+" has failed to capture "+this.getName()+".");
					Output.clanMessage(_capturingClan, "Your clan has failed to capture "+getName()+"!");
				}
					
				_capturing = false;
				_captureTime = 0;
				notified1minute = false;
				notified2minute = false;
				notified3minute = false;
				notified4minute = false;
			}*/
		}
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public int getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public Location getLocation() {
		return _location;
	}
	
	public int getRadius() {
		return _radius;
	}
	
	public void setRadius(int radius) {
		_radius = radius;
	}
	
	// Begin owner stuff
	public boolean isOwner(String playerName) {
		if(_owner.equalsIgnoreCase(playerName))
			return true;
		return false;
	}
	
	public String getOwner() {
		return _owner;
	}
	
	public void setOwner(String owner) {
		_owner = owner;
	}
	// End owner stuff
	
	// Begin co owner stuff
	public void addCoOwner(String playerName) {
		_coOwners.add(playerName);
	}
	
	public void removeCoOwner(String playerName) {
		// see how many co-owners there are
		int numCoOwners = _coOwners.size(); 
		
		// remove all co-owners that match playerName
		for(int i=numCoOwners-1; i>=0; i--) {
			if(_coOwners.get(i).equalsIgnoreCase(playerName))
				_coOwners.remove(i);
		}
	}
	
	public boolean isCoOwner(String playerName) {
		for(String name : _coOwners)
			if(name.equalsIgnoreCase(playerName))
				return true;
		return false;
	}
	
	public String getProperCoOwnerName(String playerName) {
		for(String name : _coOwners)
			if(name.equalsIgnoreCase(playerName))
				return name;
		return null;
	}
	
	public ArrayList<String> getCoOwners() {
		return _coOwners;
	}
	// End co owner stuff
	
	// Begin friend stuff
	public void addFriend(String playerName) {
		_friends.add(playerName);
	}
	
	public void removeFriend(String playerName) {
		// see how many friends there are
		int numFriends = _friends.size(); 
		
		// remove all friends that match playerName
		for(int i=numFriends-1; i>=0; i--) {
			if(_friends.get(i).equalsIgnoreCase(playerName))
				_friends.remove(i);
		}
	}
	
	public boolean isFriend(String playerName) {
		for(String name : _friends)
			if(name.equalsIgnoreCase(playerName))
				return true;
		return false;
	}
	
	public String getProperFriendName(String playerName) {
		for(String name : _friends)
			if(name.equalsIgnoreCase(playerName))
				return name;
		return null;
	}
	
	public boolean isMember(String playerName) {
		if(isOwner(playerName) || isCoOwner(playerName) || isFriend(playerName))
			return true;
		return false;
	}
	
	public ArrayList<String> getFriends() {
		return _friends;
	}
	// End friend stuff
	
	public int getMoney() {
		return _money;
	}
	
	public void setMoney(int money) {
		_money = money;
	}
	
	public boolean isProtected() {
		return _protect;
	}
	
	public void setProtect(boolean protect) {
		_protect = protect;
	}
	
	public boolean isLocked() {
		return _locked;
	}
	
	public void setLocked(boolean locked) {
		_locked = locked;
	}
	
	public boolean isCity() {
		return _city;
	}
	
	public void setIsCity(boolean isCity) {
		_city = isCity;
	}
	
	public int getValue() {
		int plotSize = this.getRadius();
		int adjustedPlotSize = plotSize - 10;
		int plotValue = 0;
		for(int i=0; i<adjustedPlotSize; i++) {
			int curSizeValue = (i+10)*10;
			plotValue+=curSizeValue;
		}
		if(this.isCity())
			plotValue += 50000;
		if(this.isDungeon())
			plotValue += 10000;
		
		if(this.getLocation().getWorld().getName().equalsIgnoreCase("nether"))
			return plotValue;
		
		return (int)Math.floor(plotValue * .75);
	}
	
	public HashMap<String, LockedBlock> getLockedBlocks() {
		return _lockedBlocks;
	}
	
	public void setIsDungeon(boolean isDungeon) {
		_dungeon = isDungeon;
	}
	
	public boolean isDungeon() {
		return _dungeon;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setFriendBuild(boolean friendBuildStatus) {
		_friendBuild = friendBuildStatus;
	}
	
	public boolean isFriendBuild() {
		return _friendBuild;
	}
	
	public ArrayList<PlotNPC> getPlotNPCs() {
		return _plotNPCs;
	}
	
	public void setForSale(int amount) {
		_forSaleAmount = amount;
	}
	
	public int getSaleCost() {
		return _forSaleAmount;
	}
	
	public boolean hasKickUpgrade() {
		return _kickUpgrade;
	}
	
	public void setKickUpgrade(boolean kickUpgrade) {
		_kickUpgrade = kickUpgrade;
	}
	
	public boolean isNeutral() {
		return _neutral;
	}
	
	public void setNeutral(boolean neutral) {
		_neutral = neutral;
	}
	
	public boolean isExplosionAllowed() {
		return _explosionAllowed;
	}
	
	public void setExplosionAllowed(boolean explosionAllowed) {
		_explosionAllowed = explosionAllowed;
	}
	
	public void setOwningClan(Clan clan) {
		_owningClan = clan;
	}
	
	public Clan getOwningClan() {
		return _owningClan;
	}
	
	public boolean isPlayerInOwnedClan(Player player, PseudoPlayer pseudoPlayer) {
		if(_owningClan == null)
			return false;
		if(pseudoPlayer.getClan() == null)
			return false;
		if(pseudoPlayer.getClan().equals(_owningClan))
			return true;
		return false;
	}
	
	public void setIsControlPoint(boolean isControlPoint) {
		_isControlPoint = isControlPoint;
		_recentCaptureFailNames = new ArrayList<String>();
	}
	
	public boolean isControlPoint() {
		return _isControlPoint;
	}
	
	public void setLastCaptureTime(long lastCaptureTime) {
		_lastCaptureTime = lastCaptureTime;
	}
	
	public long getLastCaptureTime() {
		return _lastCaptureTime;
	}
	
	public void beginCapture(Player player, PseudoPlayer pseudoPlayer, Clan clan) {
		if(player == null)
			return;
		else if(pseudoPlayer == null || clan == null) {
			Output.simpleError(player,  "Something went wrong.");
		}
		else {
			if(_recentCaptureFailNames.contains(player.getName())) {
				Output.simpleError(player, "You failed to capture "+getName()+"recently, you must wait until the next time it is vulnerable to attempt to capture it again.");
				return;
			}
			_capturing = true;
			_claimSecRemaining = 120;
			_timeoutSecRemaining = 8*60; // seconds
			_capturingPlayerName = player.getName();
			_capturingClan = clan;
			pseudoPlayer._claiming = true;
			if(_owningClan != null)
				Output.clanMessage(_owningClan, getName()+" is being claimed by "+_capturingPlayerName+", from the clan "+_capturingClan.getName()+"!");
			Output.clanMessage(_capturingClan, _capturingPlayerName+" is claiming "+this.getName()+" for your clan!");
		
			/*if(_owningClan != null)
				Output.clanMessage(_owningClan, getName()+" is under attack "+_capturingPlayerName+", It will be captured in 5 minutes!");
			Output.clanMessage(_capturingClan, _capturingPlayerName+" is capturing "+this.getName()+" for your clan, It will be captured in 5 minutes!");*/
		}
	}
	
	public void failCaptureDied(Player player) {
		_capturing = false;
		PseudoPlayer capturingPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		capturingPseudoPlayer._claiming = false;
		if(_owningClan != null)
			Output.clanMessage(_owningClan, _capturingPlayerName+" died and thus failed to claim "+this.getName()+".");
		Output.clanMessage(_capturingClan, _capturingPlayerName+" died and thus failed to claim "+this.getName()+".");
		_claimSecRemaining = 0;
		_recentCaptureFailNames.add(_capturingPlayerName);
		_capturingPlayerName = "";
		_capturingClan = null;
	}
	
	public void failCaptureLeft(Player player) {
		_capturing = false;
		PseudoPlayer capturingPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		capturingPseudoPlayer._claiming = false;
		if(_owningClan != null)
			Output.clanMessage(_owningClan, _capturingPlayerName+" left "+this.getName()+" and thus failed to claim it.");
		Output.clanMessage(_capturingClan, _capturingPlayerName+" left "+this.getName()+" and thus failed to claim it.");
		_claimSecRemaining = 0;
		_recentCaptureFailNames.add(_capturingPlayerName);
		_capturingPlayerName = "";
		_capturingClan = null;
	}
	
	public boolean isUnderAttack() {
		return _capturing;
	}
	
	public void addSubPlot(SubPlot subPlot) {
		_subPlots.add(subPlot);
	}
	
	public ArrayList<SubPlot> getSubPlots() {
		return _subPlots;
	}
	
	public boolean isNoMagicPlot() {
		return _isNoMagicPlot;
	}
	
	public void setIsNoMagicPlot(boolean isNoMagicPlot) {
		_isNoMagicPlot = isNoMagicPlot;
	}
	
	public boolean isNoPvPPlot() {
		return _isNoPvPPlot;
	}
	
	public void setIsNoPvPPlot(boolean isNoPvPPlot) {
		_isNoPvPPlot = isNoPvPPlot;
	}
}
