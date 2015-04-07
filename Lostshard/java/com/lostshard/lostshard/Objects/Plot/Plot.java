package com.lostshard.lostshard.Objects.Plot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Serializer;

/**
 * @author Jacob Rosborg
 *
 */
public class Plot {

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();
	
	// String's
	private String name;

	// Int's
	private int id;
	private int size = 10;
	private int money = 0;
	private int salePrice = 0;

	// Array's
	private List<UUID> friends = new ArrayList<UUID>();
	private List<UUID> coowners = new ArrayList<UUID>();
	// UUID
	private UUID owner;

	// Boolean's
	private boolean protection = true;
	private boolean allowExplosions = false;
	private boolean privatePlot = true;
	private boolean friendBuild = false;
	private boolean update = false;
	// Upgrade's
	private List<PlotUpgrade> upgrades = new ArrayList<PlotUpgrade>();

	// Location
	private Location location;

	// CapturePoint
	private boolean capturePoint = false;
	private Clan owningClan = null;
	private long lastCaptureDate = 0;
	private UUID capturingPlayer = null;
	private Clan capturingClan = null;
	private List<UUID> recentCaptureFails = new ArrayList<UUID>();
	private int claimSecRemaining = 0;
	private int timeoutSecRemaining = 0;
	private double refractoryPeriod = 0;
	private boolean capturedRecently = false;
	private int recentClaims = 0;

	// NPCS
	private ArrayList<NPC> npcs = new ArrayList<NPC>();

	// Admin
	private boolean allowMagic = true;
	private boolean allowPvp = true;

	public Plot(int id, String name, UUID owner, Location location) {
		super();
		this.name = name;
		this.id = id;
		this.owner = owner;
		this.location = location;
	}

	// Getters and Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		update();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		update();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
		update();
	}

	public void expandSize(int size) {
		this.size += size;
		update();
	}

	public void shrinkSize(int size) {
		this.size -= size;
		update();
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
		update();
	}

	public void addMoney(int money) {
		this.money += money;
		update();
	}

	public void subtractMoney(int money) {
		this.money -= money;
		update();
	}

	public int getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
		update();
	}

	public boolean isForSale() {
		return (this.salePrice > 0) ? true : false;
	}

	public List<UUID> getFriends() {
		return friends;
	}

	public void setFriends(List<UUID> friends) {
		this.friends = friends;
	}

	public List<UUID> getCoowners() {
		return coowners;
	}

	public void setCoowners(List<UUID> coowners) {
		this.coowners = coowners;
	}

	public boolean isProtected() {
		return protection;
	}

	public void setProtected(boolean protection) {
		this.protection = protection;
		update();
	}

	public boolean isAllowExplosions() {
		return allowExplosions;
	}

	public void setAllowExplosions(boolean allowExplosions) {
		this.allowExplosions = allowExplosions;
		update();
	}

	public boolean isPrivatePlot() {
		return privatePlot;
	}

	public void setPrivatePlot(boolean privatePlot) {
		this.privatePlot = privatePlot;
		update();
	}

	public boolean isFriendBuild() {
		return friendBuild;
	}

	public void setFriendBuild(boolean friendBuild) {
		this.friendBuild = friendBuild;
		update();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		update();
	}

	public boolean isCapturePoint() {
		return capturePoint;
	}

	public void setCapturePoint(boolean capturePoint) {
		this.capturePoint = capturePoint;
		update();
	}

	public Clan getOwningClan() {
		return owningClan;
	}

	public void setOwningClan(Clan owningClan) {
		this.owningClan = owningClan;
		update();
	}

	public boolean isAllowMagic() {
		return allowMagic;
	}

	public void setAllowMagic(boolean allowMagic) {
		this.allowMagic = allowMagic;
		update();
	}

	public boolean isAllowPvp() {
		return allowPvp;
	}

	public void setAllowPvp(boolean allowPvp) {
		this.allowPvp = allowPvp;
		update();
	}

	// Getting next id
	public int nextId() {
		int nextId = 0;
		for (Plot p : ptm.getPlots())
			if (p.getId() > nextId)
				nextId = p.getId();
		return nextId + 1;
	}

	public boolean isOwner(Player player) {
		return isOwner(player.getUniqueId());
	}

	public boolean isFriend(Player player) {
		return friends.contains(player.getUniqueId());
	}

	public boolean isOwner(UUID uuid) {
		return uuid.equals(owner);
	}

	public boolean isFriend(UUID uuid) {
		return friends.contains(uuid);
	}
	
	// Friends
	public void addFriend(Player player) {
		friends.add(player.getUniqueId());
		update();
	}

	public void removeFriend(Player player) {
		friends.remove(player.getUniqueId());
		update();
	}

	// Coowners
	public boolean isCoowner(Player player) {
		return coowners.contains(player.getUniqueId());
	}
	
	public boolean isCoowner(UUID uuid) {
		return coowners.contains(uuid);
	}

	public void addCoowner(Player player) {
		coowners.add(player.getUniqueId());
		update();
	}

	public void removeCoowner(Player player) {
		coowners.remove(player.getUniqueId());
		update();
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
		update();
	}

	public int getValue() {
		int plotValue = (int) Math.floor(Math.abs((((Math.pow(size-1, 2)+size-1)/2-45)*Variables.plotExpandPrice)));
		if (isUpgrade(PlotUpgrade.TOWN))
			plotValue += Variables.plotTownPrice;
		if (isUpgrade(PlotUpgrade.DUNGEON))
			plotValue += Variables.plotDungeonPrice;
		if (isUpgrade(PlotUpgrade.AUTOKICK))
			plotValue += Variables.plotAutoKickPrice;
		if (isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
			plotValue += Variables.plotNeutralAlignmentPrice;

		if (this.getLocation().getWorld().getEnvironment()
				.equals(Environment.NETHER))
			return plotValue;
		return (int) Math.floor(plotValue * .75);
	}

	public String getPlayerStatusOfPlotString(Player player) {
		return isOwner(player) ? "You are the owner of this plot."
				: isCoowner(player) ? "You are a co-owner of this plot."
						: isFriend(player) ? "You are a friend of this plot."
								: "You are not friend of this plot.";
	}

	public boolean isAllowedToBuild(Player player) {
		return isAllowedToBuild(player.getUniqueId());
	}

	public boolean isFriendOrAbove(Player player) {
		return isFriendOrAbove(player.getUniqueId());
	}

	public boolean isCoownerOrAbove(Player player) {
		return isCoownerOrAbove(player.getUniqueId());
	}

	public boolean isAllowedToBuild(UUID uuid) {
		PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if(pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return  isCoownerOrAbove(uuid) ? true : isFriend(uuid)
				&& isFriendBuild() ? true : !isProtected() ? true : false;
	}

	public boolean isAllowedToInteract(UUID uuid) {
		PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if(pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return  isFriendOrAbove(uuid) ? true : false;
	}
	
	public boolean isFriendOrAbove(UUID uuid) {
		return isOwner(uuid) ? true : isCoowner(uuid) ? true
				: isFriend(uuid) ? true : false;
	}

	public boolean isCoownerOrAbove(UUID uuid) {
		return isOwner(uuid) ? true : isCoowner(uuid) ? true : false;
	}
	
	/**
	 * @author Frank Oliver
	 * @param toSize
	 * @return Expand price from current size to size.
	 */
	public int getExpandPrice(int toSize) {
		return (int) Math.abs((((Math.pow((toSize-1), 2)+toSize-1)/2)-((Math.pow(size, 2)+size)/2))*Variables.plotExpandPrice+100);
	}

	public ArrayList<NPC> getNpcs() {
		return npcs;
	}

	public void setNpcs(ArrayList<NPC> npcs) {
		this.npcs = npcs;
	}

	public int getMaxVendors() {
		return size / 15;
	}

	public void update() {
		this.update = true;
	}
	
	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void disband() {
		for(NPC npc : npcs)
			Database.deleteNPC(npc);
		ptm.removePlot(this);
	}

	public List<PlotUpgrade> getUpgrades() {
		return upgrades;
	}

	public void setUpgrades(List<PlotUpgrade> upgrades) {
		this.upgrades = upgrades;
	}
	
	public void addUpgrade(PlotUpgrade upgrade) {
		this.upgrades.add(upgrade);
		update();
	}
	
	public boolean isUpgrade(PlotUpgrade upgrade) {
		return this.upgrades.contains(upgrade);
	}

	public void removeUpgrade(PlotUpgrade upgrade) {
		this.upgrades.remove(upgrade);
		update();
	}
	
	public String upgradesToJson() {
		List<String> tjson = new ArrayList<String>();
		for(PlotUpgrade upgrade : upgrades)
			tjson.add(upgrade.name());
		return Serializer.serializeStringArray(tjson);
	}

	public int getTax() {
		return size*10;
	}

	public boolean isAllowedToInteract(Player player) {
		return isAllowedToInteract(player.getUniqueId());
	}

	public long getLastCaptureDate() {
		return lastCaptureDate;
	}

	public void setLastCaptureDate(long lastCaptureDate) {
		this.lastCaptureDate = lastCaptureDate;
	}

	public void beginCapture(Player player, PseudoPlayer pPlayer, Clan clan) {
		if(player == null)
			return;
		else if(pPlayer == null || clan == null) {
			Output.simpleError(player,  "Something went wrong.");
		}
		else {
			if(recentCaptureFails.contains(player.getName())) {
				Output.simpleError(player, "You failed to capture "+getName()+" recently, you must wait until the next time it is vulnerable to attempt to capture it again.");
				return;
			}
			capturingPlayer = player.getUniqueId();
			setClaimSecRemaining(120);
			setTimeoutSecRemaining(8*60); // seconds
			capturingClan = clan;
			pPlayer.setClaming(true);
			
			if(getOwningClan() != null)
				owningClan.sendMessage(getName()+" is being claimed by "+player.getName()+", from the clan "+capturingClan.getName()+"!");
			capturingClan.sendMessage(player.getName()+" is claiming "+this.getName()+" for your clan!");
    		player.sendMessage(ChatColor.GOLD+"You must stay alive and within "+this.getName()+" for 120 seconds.");
		}
	}
	
	public void failCaptureDied(Player player) {
		if(!capturingPlayer.equals(player.getUniqueId()))
			return;
		PseudoPlayer capturingPseudoPlayer = pm.getPlayer(player);
		capturingPlayer = null;
		capturingPseudoPlayer.setClaming(false);
		if(owningClan != null)
			owningClan.sendMessage(player.getName()+" died and thus failed to claim "+this.getName()+".");
		capturingClan.sendMessage(player.getName()+" died and thus failed to claim "+this.getName()+".");
		setClaimSecRemaining(0);
		recentCaptureFails.add(player.getUniqueId());
		capturingClan = null;
	}
	
	public void failCaptureLeft(Player player) {
		if(!capturingPlayer.equals(player.getUniqueId()))
			return;
		capturingPlayer = null;
		PseudoPlayer capturingPseudoPlayer = pm.getPlayer(player);
		capturingPseudoPlayer.setClaming(false);
		if(owningClan != null)
			owningClan.sendMessage(player.getName()+" left "+this.getName()+" and thus failed to claim it.");
		capturingClan.sendMessage(player.getName()+" left "+this.getName()+" and thus failed to claim it.");
		claimSecRemaining = 0;
		recentCaptureFails.add(player.getUniqueId());
		capturingClan = null;
	}

	public Clan getCapturingClan() {
		return capturingClan;
	}

	public void setCapturingClan(Clan capturingClan) {
		this.capturingClan = capturingClan;
	}

	public boolean isUnderAttack() {
		return capturingPlayer != null;
	}

	public int getTimeoutSecRemaining() {
		return timeoutSecRemaining;
	}

	public void setTimeoutSecRemaining(int timeoutSecRemaining) {
		this.timeoutSecRemaining = timeoutSecRemaining;
	}

	public int getClaimSecRemaining() {
		return claimSecRemaining;
	}

	public void setClaimSecRemaining(int claimSecRemaining) {
		this.claimSecRemaining = claimSecRemaining;
	}
	
	public void tick(double delta) {
		delta /= 10;
		
		if(refractoryPeriod > 0) {
			refractoryPeriod -= delta;
			if(refractoryPeriod <= 0) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.sendMessage(ChatColor.GREEN+this.getName()+" is now vulnerable to capture.");
				}
				capturedRecently = false;
				recentCaptureFails.clear();
			}
		}
		// Handle the timeout. When the time runs out we clear the recent claims
		if(timeoutSecRemaining > 0) {
			timeoutSecRemaining -= delta;
			if(timeoutSecRemaining <= 0) {
				recentClaims = 0;
			}
		}
		
		if(capturingPlayer != null) {					
			claimSecRemaining -= delta;
			if(claimSecRemaining <= 0) {
				capturingPlayer = null;
				recentClaims++;
				if(recentClaims >= 3) {						
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.sendMessage(ChatColor.GREEN+this.getName()+" has been captured by "+capturingClan.getName()+".");
					}
					recentClaims = 0;
					timeoutSecRemaining = 0;
					Player ocapturingPlayer = Bukkit.getPlayer(capturingPlayer);
					capturingPlayer = null;
					owningClan = capturingClan;
					capturingClan = null;
					
//					if(this.getName().equalsIgnoreCase(GORP_PLOT_NAME))
//						ClanHandler.SetGorpControlClan(_owningClan);
//					if(this.getName().equalsIgnoreCase(BUNT_PLOT_NAME))
//						ClanHandler.SetBuntControlClan(_owningClan);
					
					if(capturingPlayer != null) {
						PseudoPlayer capturingPseudoPlayer = pm.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
					capturedRecently = true;
					refractoryPeriod = 60*60;
					Date date = new Date();
					this.lastCaptureDate = date.getTime();
					Database.updatePlot(this);
				}
				else {
					int timesLeft = 3 - recentClaims;
					if(owningClan != null) {
						if(recentClaims == 1) {
							if(timesLeft == 1)
								owningClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								owningClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
						else {
							if(timesLeft == 1)
								owningClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
							owningClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
					}
					
					if(capturingClan != null) {
						if(recentClaims == 1) {
							if(timesLeft == 1)
								capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
								capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" time. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
						else {
							if(timesLeft == 1)
								capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more time. If it is not claimed again in the next 8 minutes it will reset.");
							else
							capturingClan.sendMessage(this.getName()+" has been claimed "+recentClaims+" times. It will be captured if it is claimed "+timesLeft+" more times. If it is not claimed again in the next 8 minutes it will reset.");
						}
					}
					Player ocapturingPlayer = Bukkit.getPlayer(capturingPlayer);
					if(capturingPlayer != null) {
						PseudoPlayer capturingPseudoPlayer = pm.getPlayer(ocapturingPlayer);
						capturingPseudoPlayer.setClaming(false);
					}
				}
			}
		}
	}

	public double getRefractoryPeriod() {
		return refractoryPeriod;
	}

	public void setRefractoryPeriod(double refractoryPeriod) {
		this.refractoryPeriod = refractoryPeriod;
	}

	public boolean isCapturedRecently() {
		return capturedRecently;
	}

	public void setCapturedRecently(boolean capturedRecently) {
		this.capturedRecently = capturedRecently;
	}

	// Capturepoint stuff

	// TODO Commands and Capture and NPC's
}
