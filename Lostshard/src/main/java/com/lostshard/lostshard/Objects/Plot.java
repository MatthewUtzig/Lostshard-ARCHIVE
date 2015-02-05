package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Groups.Clan;

/**
 * @author Jacob Rosborg
 *
 */
public class Plot {

	// String's
	private String name;

	// Int's
	private int id;
	private int size = 10;
	private int money = 0;
	private int salePrice = 0;

	// Array's
	private ArrayList<UUID> friends = new ArrayList<UUID>();
	private ArrayList<UUID> coowners = new ArrayList<UUID>();
	// UUID
	private UUID owner;

	// Boolean's
	private boolean protection = true;
	private boolean allowExplosions = false;
	private boolean privatePlot = true;
	private boolean friendBuild = false;
	private boolean update = false;
	// Upgrade's
	private boolean town = false;
	private boolean dungeon = false;
	private boolean autoKick = false;
	private boolean neutralAlignment = false;

	// Location
	private Location location;

	// CapturePoint
	private boolean capturePoint = false;
	private Clan owningClan = null;

	// NPCS
	private ArrayList<NPC> npcs = new ArrayList<NPC>();

	// Admin
	private boolean allowMagic = true;
	private boolean allowPvp = true;

	public Plot(int id, String name, UUID owner, Location location) {
		super();
		this.name = name;
		this.id = nextId();
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

	public boolean isTown() {
		return town;
	}

	public void setTown(boolean town) {
		this.town = town;
		update();
	}

	public boolean isDungeon() {
		return dungeon;
	}

	public void setDungeon(boolean dungeon) {
		this.dungeon = dungeon;
		update();
	}

	public boolean isAutoKick() {
		return autoKick;
	}

	public void setAutoKick(boolean autoKick) {
		this.autoKick = autoKick;
		update();
	}

	public boolean isNeutralAlignment() {
		return neutralAlignment;
	}

	public void setNeutralAlignment(boolean neutralAlignment) {
		this.neutralAlignment = neutralAlignment;
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
		for (Plot p : Lostshard.getRegistry().getPlots())
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
		int plotValue = Variables.plotExpandPrice
				* (((size - 1) ^ 2 + (size - 1)) / 2)
				- (((Variables.plotStartingSize - 1) ^ 2 + (Variables.plotStartingSize - 1)) / 2);
		if (this.town)
			plotValue += Variables.plotTownPrice;
		if (this.isDungeon())
			plotValue += Variables.plotDungeonPrice;
		if (this.isAutoKick())
			plotValue += Variables.plotAutoKickPrice;
		if (this.isNeutralAlignment())
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
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(uuid);
		if(pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return  isCoownerOrAbove(uuid) ? true : isFriend(uuid)
				&& isFriendBuild() ? true : !isProtected() ? true : false;
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
		return Variables.plotExpandPrice
				* (((toSize - 1) ^ 2 + (toSize - 1)) / 2)
				- (((size - 1) ^ 2 + (size - 1)) / 2);
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

	// Capturepoint stuff

	// TODO Commands and Capture and NPC's
}
