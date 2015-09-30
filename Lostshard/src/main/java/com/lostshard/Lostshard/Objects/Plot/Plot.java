package com.lostshard.Lostshard.Objects.Plot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Database.Mappers.NPCMapper;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Serializer;
import com.lostshard.Utils.ExtraMath;

/**
 * @author Jacob Rosborg
 *
 */
public class Plot {
	
	public enum PlotUpgrade {

		TOWN("Town", 100000), DUNGEON("Dungeon", 20000), AUTOKICK("AutoKick", 10000), ARENA(
				"Arena", 10000), NEUTRALALIGNMENT("Neutral Alignment", 4000);

		public static PlotUpgrade getByName(String name) {
			for (final PlotUpgrade upgrade : values())
				if (StringUtils.startsWithIgnoreCase(upgrade.getName(), name))
					return upgrade;
			return null;
		}

		private int price;

		private String name;

		private PlotUpgrade(String name, int price) {
			this.setName(name);
			this.setPrice(price);
		}

		public String getName() {
			return this.name;
		}

		public int getPrice() {
			return this.price;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPrice(int price) {
			this.price = price;
		}

	}

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
	private boolean titleEntrence = false;
	// Upgrade's
	private List<PlotUpgrade> upgrades = new ArrayList<PlotUpgrade>();

	// Location
	private Location location;

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

	public void addCoowner(Player player) {
		this.coowners.add(player.getUniqueId());
		this.update();
	}

	// Friends
	public void addFriend(Player player) {
		this.friends.add(player.getUniqueId());
		this.update();
	}

	public void addMoney(int money) {
		this.money += money;
		this.update();
	}

	public void addUpgrade(PlotUpgrade upgrade) {
		this.upgrades.add(upgrade);
		this.update();
	}

	public void disband() {
		for (final NPC npc : this.npcs)
			NPCMapper.deleteNPC(npc);
		this.ptm.removePlot(this);
	}

	public void expandSize(int size) {
		this.size += size;
		this.update();
	}

	public List<UUID> getCoowners() {
		return this.coowners;
	}

	/**
	 * @author Frank Oliver
	 * @param toSize
	 * @return Expand price from current size to size.
	 */
	public int getExpandPrice(int toSize) {
		return Variables.plotExpandPrice*(-ExtraMath.Triangular(size)+ExtraMath.Triangular(toSize)+size-toSize);
	}

	public List<UUID> getFriends() {
		return this.friends;
	}

	public int getId() {
		return this.id;
	}

	public Location getLocation() {
		return this.location;
	}

	public int getMaxVendors() {
		return this.size / 15;
	}

	public int getMoney() {
		return this.money;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<NPC> getNpcs() {
		return this.npcs;
	}

	public UUID getOwner() {
		return this.owner;
	}

	public String getPlayerStatusOfPlotString(Player player) {
		return this.isOwner(player) ? "You are the owner of this plot." : this
				.isCoowner(player) ? "You are a co-owner of this plot." : this
				.isFriend(player) ? "You are a friend of this plot."
				: "You are not friend of this plot.";
	}

	public int getSalePrice() {
		return this.salePrice;
	}

	public int getSize() {
		return this.size;
	}

	public int getTax() {
		return this.size * 10;
	}

	public List<PlotUpgrade> getUpgrades() {
		return this.upgrades;
	}

	public int getValue() {
		int plotValue = Variables.plotExpandPrice*(-55+ExtraMath.Triangular(getSize())+10-getSize());
		if (this.isUpgrade(PlotUpgrade.TOWN))
			plotValue += Variables.plotTownPrice;
		if (this.isUpgrade(PlotUpgrade.DUNGEON))
			plotValue += Variables.plotDungeonPrice;
		if (this.isUpgrade(PlotUpgrade.AUTOKICK))
			plotValue += Variables.plotAutoKickPrice;
		if (this.isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
			plotValue += Variables.plotNeutralAlignmentPrice;
		
		if (this.getLocation().getWorld().getEnvironment()
				.equals(Environment.NETHER))
			return plotValue;
		return (int) Math.floor(plotValue * .75);
	}

	public boolean isAllowedToBuild(Player player) {
		return this.isAllowedToBuild(player.getUniqueId());
	}

	public boolean isAllowedToBuild(UUID uuid) {
		final PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if (pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return this.isCoownerOrAbove(uuid) ? true : this.isFriend(uuid)
				&& this.isFriendBuild() ? true : !this.isProtected() ? true
				: false;
	}

	public boolean isAllowedToInteract(Player player) {
		return this.isAllowedToInteract(player.getUniqueId());
	}

	public boolean isAllowedToInteract(UUID uuid) {
		if(!this.isPrivatePlot())
			return true;
		final PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if (pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return this.isFriendOrAbove(uuid) ? true : false;
	}

	public boolean isAllowExplosions() {
		return this.allowExplosions;
	}

	public boolean isAllowMagic() {
		return this.allowMagic;
	}

	public boolean isAllowPvp() {
		return this.allowPvp;
	}

	// Coowners
	public boolean isCoowner(Player player) {
		return this.coowners.contains(player.getUniqueId());
	}

	public boolean isCoowner(UUID uuid) {
		return this.coowners.contains(uuid);
	}

	public boolean isCoownerOrAbove(Player player) {
		return this.isCoownerOrAbove(player.getUniqueId());
	}

	public boolean isCoownerOrAbove(UUID uuid) {
		return this.isOwner(uuid) ? true : this.isCoowner(uuid) ? true : false;
	}

	public boolean isForSale() {
		return this.salePrice > 0 ? true : false;
	}

	public boolean isFriend(Player player) {
		return this.friends.contains(player.getUniqueId());
	}

	public boolean isFriend(UUID uuid) {
		return this.friends.contains(uuid);
	}

	public boolean isFriendBuild() {
		return this.friendBuild;
	}

	public boolean isFriendOrAbove(Player player) {
		return this.isFriendOrAbove(player.getUniqueId());
	}

	public boolean isFriendOrAbove(UUID uuid) {
		return this.isOwner(uuid) ? true : this.isCoowner(uuid) ? true : this
				.isFriend(uuid) ? true : false;
	}

	public boolean isOwner(Player player) {
		return this.isOwner(player.getUniqueId());
	}

	public boolean isOwner(UUID uuid) {
		return uuid.equals(this.owner);
	}

	public boolean isPrivatePlot() {
		return this.privatePlot;
	}

	public boolean isProtected() {
		return this.protection;
	}

	public boolean isTitleEntrence() {
		return this.titleEntrence;
	}

	public boolean isUpdate() {
		return this.update;
	}

	public boolean isUpgrade(PlotUpgrade upgrade) {
		return this.upgrades.contains(upgrade);
	}

	// Getting next id
	public int nextId() {
		int nextId = 0;
		for (final Plot p : this.ptm.getPlots())
			if (p.getId() > nextId)
				nextId = p.getId();
		return nextId + 1;
	}

	public void removeCoowner(Player player) {
		this.coowners.remove(player.getUniqueId());
		this.update();
	}

	public void removeFriend(Player player) {
		this.friends.remove(player.getUniqueId());
		this.update();
	}

	public void removeUpgrade(PlotUpgrade upgrade) {
		this.upgrades.remove(upgrade);
		this.update();
	}

	public void setAllowExplosions(boolean allowExplosions) {
		this.allowExplosions = allowExplosions;
		this.update();
	}

	public void setAllowMagic(boolean allowMagic) {
		this.allowMagic = allowMagic;
		this.update();
	}

	public void setAllowPvp(boolean allowPvp) {
		this.allowPvp = allowPvp;
		this.update();
	}

	public void setCoowners(List<UUID> coowners) {
		this.coowners = coowners;
	}

	public void setFriendBuild(boolean friendBuild) {
		this.friendBuild = friendBuild;
		this.update();
	}

	public void setFriends(List<UUID> friends) {
		this.friends = friends;
	}

	public void setId(int id) {
		this.id = id;
		this.update();
	}

	public void setLocation(Location location) {
		this.location = location;
		this.update();
	}

	public void setMoney(int money) {
		this.money = money;
		this.update();
	}

	public void setName(String name) {
		this.name = name;
		this.update();
	}

	public void setNpcs(ArrayList<NPC> npcs) {
		this.npcs = npcs;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
		this.update();
	}

	public void setPrivatePlot(boolean privatePlot) {
		this.privatePlot = privatePlot;
		this.update();
	}

	public void setProtected(boolean protection) {
		this.protection = protection;
		this.update();
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
		this.update();
	}

	public void setSize(int size) {
		this.size = size;
		this.update();
	}

	public void setTitleEntrence(boolean titleEntrence) {
		this.titleEntrence = titleEntrence;
		this.update();
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void setUpgrades(List<PlotUpgrade> upgrades) {
		this.upgrades = upgrades;
	}

	public void shrinkSize(int size) {
		this.size -= size;
		this.update();
	}

	public void subtractMoney(int money) {
		this.money -= money;
		this.update();
	}

	public void update() {
		this.update = true;
	}

	public String upgradesToJson() {
		final List<String> tjson = new ArrayList<String>();
		for (final PlotUpgrade upgrade : this.upgrades)
			tjson.add(upgrade.name());
		return Serializer.serializeStringArray(tjson);
	}
}
