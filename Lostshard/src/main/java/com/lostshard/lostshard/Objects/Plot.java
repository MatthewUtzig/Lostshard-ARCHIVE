package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Main.Lostshard;

public class Plot {
	
	//String's
	private String name;
	
	//Int's
	private int id;
	private int size = 10;
	private int money = 0;
	private int salePrice = 0;
	
	//Array's
	private ArrayList<UUID> friends = new ArrayList<UUID>();
	private ArrayList<UUID> coowners = new ArrayList<UUID>();
	//UUID
	private UUID owner;
	
	//Boolean's
	private boolean protection = true;
	private boolean allowExplosions = false;
	private boolean privatePlot = true;
	private boolean friendBuild = false;
	//Upgrade's
	private boolean town = false;
	private boolean dungeon = false;
	private boolean autoKick = false;
	private boolean neutralAlignment = false;
	
	//Location
	private Location location;
	
	//CapturePoint
	private boolean capturePoint = false;
	private Clan owningClan = null;
	
	
	//Admin
	private boolean allowMagic = true;
	private boolean allowPvp = true;
	
	public Plot(String name, UUID owner) {
		super();
		this.name = name;
		this.id = nextId();
		this.owner = owner;
	}

	public Plot(String name, int id, int size, int money, int salePrice,
			ArrayList<UUID> friends, ArrayList<UUID> coowners,
			boolean protection, boolean allowExplosions, boolean publicPlot,
			boolean friendBuild, boolean town, boolean dungeon,
			boolean autoKick, boolean neutralAlignment, Location location,
			boolean capturePoint, Clan owningClan, boolean allowMagic,
			boolean allowPvp) {
		super();
		this.name = name;
		this.id = id;
		this.size = size;
		this.money = money;
		this.salePrice = salePrice;
		this.friends = friends;
		this.coowners = coowners;
		this.protection = protection;
		this.allowExplosions = allowExplosions;
		this.privatePlot = publicPlot;
		this.friendBuild = friendBuild;
		this.town = town;
		this.dungeon = dungeon;
		this.autoKick = autoKick;
		this.neutralAlignment = neutralAlignment;
		this.location = location;
		this.capturePoint = capturePoint;
		this.owningClan = owningClan;
		this.allowMagic = allowMagic;
		this.allowPvp = allowPvp;
	}
	
	//Getters and Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public void expandSize(int size) {
		this.size += size;
	}
	
	public void shrinkSize(int size) {
		this.size -= size;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void addMoney(int money) {
		this.money += money;
	}
	
	public void subtractMoney(int money) {
		this.money -= money;
	}
	
	public int getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}
	
	public boolean isForSale() {
		return (this.salePrice > 0) ? true : false;
	}

	public ArrayList<UUID> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<UUID> friends) {
		this.friends = friends;
	}

	public ArrayList<UUID> getCoowners() {
		return coowners;
	}

	public void setCoowners(ArrayList<UUID> coowners) {
		this.coowners = coowners;
	}

	public boolean isProtected() {
		return protection;
	}

	public void setProtected(boolean protection) {
		this.protection = protection;
	}

	public boolean isAllowExplosions() {
		return allowExplosions;
	}

	public void setAllowExplosions(boolean allowExplosions) {
		this.allowExplosions = allowExplosions;
	}

	public boolean isPrivatePlot() {
		return privatePlot;
	}

	public void setPrivatePlot(boolean privatePlot) {
		this.privatePlot = privatePlot;
	}

	public boolean isFriendBuild() {
		return friendBuild;
	}

	public void setFriendBuild(boolean friendBuild) {
		this.friendBuild = friendBuild;
	}

	public boolean isTown() {
		return town;
	}

	public void setTown(boolean town) {
		this.town = town;
	}

	public boolean isDungeon() {
		return dungeon;
	}

	public void setDungeon(boolean dungeon) {
		this.dungeon = dungeon;
	}

	public boolean isAutoKick() {
		return autoKick;
	}

	public void setAutoKick(boolean autoKick) {
		this.autoKick = autoKick;
	}

	public boolean isNeutralAlignment() {
		return neutralAlignment;
	}

	public void setNeutralAlignment(boolean neutralAlignment) {
		this.neutralAlignment = neutralAlignment;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isCapturePoint() {
		return capturePoint;
	}

	public void setCapturePoint(boolean capturePoint) {
		this.capturePoint = capturePoint;
	}

	public Clan getOwningClan() {
		return owningClan;
	}

	public void setOwningClan(Clan owningClan) {
		this.owningClan = owningClan;
	}

	public boolean isAllowMagic() {
		return allowMagic;
	}

	public void setAllowMagic(boolean allowMagic) {
		this.allowMagic = allowMagic;
	}

	public boolean isAllowPvp() {
		return allowPvp;
	}

	public void setAllowPvp(boolean allowPvp) {
		this.allowPvp = allowPvp;
	}
	//Getting next id
	public int nextId() {
		int nextId = 0;
		for(Plot p : Lostshard.getPlots())
			if(p.getId() > nextId)
				nextId = p.getId() + 1;
		return nextId;
	}
	
	public boolean isOwner(Player player) {
		return player.isOp() || player.getUniqueId() == owner;
	}
	
	public boolean isFriend(Player player) {
		return player.isOp() || friends.contains(player.getUniqueId());
	}
	
	//Friends
	public void addFriend(Player player) {
		friends.add(player.getUniqueId());
	}
	
	public void removeFriend(Player player) {
		friends.remove(player.getUniqueId());
	}
	//Coowners
	public boolean isCoowner(Player player) {
		return player.isOp() || coowners.contains(player.getUniqueId());
	}
	
	public void addCoowner(Player player) {
		coowners.add(player.getUniqueId());
	}
	
	public void removeCoowner(Player player) {
		coowners.remove(player.getUniqueId());
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	
	public int getValue() {
		int plotValue = 0;
		int adjustedPlotSize = this.size - 10;
		for(int i=0; i<adjustedPlotSize; i++) {
			int curSizeValue = (i+10)*10;
			plotValue+=curSizeValue;
		}
		if(this.town)
			plotValue += Variables.plotTownPrice;
		if(this.isDungeon())
			plotValue += Variables.plotDungeonPrice;
		if(this.isAutoKick())
			plotValue += Variables.plotAutoKickPrice;
		if(this.isNeutralAlignment())
			plotValue += Variables.plotNeutralAlignmentPrice;
		
		if(this.getLocation().getWorld().getEnvironment().equals(Environment.NETHER))
			return plotValue;
		return (int) Math.floor(plotValue*.75);
	}
	
	public String getPlayerStatusOfPlotString(Player player) {
		return isOwner(player) ? "You are the owner of this plot." : isCoowner(player) ? "You are a co-owner of this plot."
				: isFriend(player) ? "You are a friend of this plot." : "You are not friend of this plot.";
	}
	
	public boolean isAllowedToBuild(Player player) {
		return isCoownerOrAbove(player) ? true : isFriend(player) && isFriendBuild() ? true : !isProtected() ? true : false;
	}
	
	public boolean isFriendOrAbove(Player player) {
		return isOwner(player) ? true : isCoowner(player) ? true : isFriend(player) ? true : false;
	}
	
	public boolean isCoownerOrAbove(Player player) {
		return isOwner(player) ? true : isCoowner(player) ? true : false;
	}
	
	//Capturepoint stuff
	
	
	//TODO Commands and Capture and NPC's
}
