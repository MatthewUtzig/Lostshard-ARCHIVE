package com.lostshard.Lostshard.Objects.Plot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Main.Lostshard;
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
@Audited
@Entity
@Table(name="plots")
@Access(AccessType.PROPERTY)
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
	private List<NPC> npcs = new ArrayList<NPC>();

	// Admin
	private boolean allowMagic = true;
	private boolean allowPvp = true;

	public Plot() {
		
	}
	
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
		delete();
	}

	public void expandSize(int size) {
		this.size += size;
		this.update();
	}
	
	@ElementCollection
	@CollectionTable
	@Type(type="uuid-char")
	public List<UUID> getCoowners() {
		return this.coowners;
	}

	/**
	 * @author Defman
	 * @param toSize
	 * @return Expand price from current size to size.
	 */
	@Transient
	public int getExpandPrice(int toSize) {
		return Variables.plotExpandPrice*(-ExtraMath.Triangular(size)+ExtraMath.Triangular(toSize)+size-toSize);
	}
	@ElementCollection
	@CollectionTable
	@Type(type="uuid-char")
	public List<UUID> getFriends() {
		return this.friends;
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	public int getId() {
		return this.id;
	}

	@Transient
	public Location getLocation() {
		return this.location;
	}

	@Transient
	public int getMaxVendors() {
		return this.size / 15;
	}

	public int getMoney() {
		return this.money;
	}

	@Column(unique=true)
	public String getName() {
		return this.name;
	}

	@ElementCollection
	@CollectionTable
	public List<NPC> getNpcs() {
		return this.npcs;
	}

	@Type(type="uuid-char")
	public UUID getOwner() {
		return this.owner;
	}

	@Transient
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

	@Transient
	public int getTax() {
		return this.size * 10;
	}

	@ElementCollection
	@CollectionTable
	@Enumerated(EnumType.STRING)
	public List<PlotUpgrade> getUpgrades() {
		return this.upgrades;
	}

	@Transient
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

	@Transient
	public boolean isAllowedToBuild(Player player) {
		return this.isAllowedToBuild(player.getUniqueId());
	}

	@Transient
	public boolean isAllowedToBuild(UUID uuid) {
		final PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if (pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return this.isCoownerOrAbove(uuid) ? true : this.isFriend(uuid)
				&& this.isFriendBuild() ? true : !this.isProtected() ? true
				: false;
	}

	@Transient
	public boolean isAllowedToInteract(Player player) {
		return this.isAllowedToInteract(player.getUniqueId());
	}

	@Transient
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
	@Transient
	public boolean isCoowner(Player player) {
		return this.coowners.contains(player.getUniqueId());
	}

	@Transient
	public boolean isCoowner(UUID uuid) {
		return this.coowners.contains(uuid);
	}

	@Transient
	public boolean isCoownerOrAbove(Player player) {
		return this.isCoownerOrAbove(player.getUniqueId());
	}

	@Transient
	public boolean isCoownerOrAbove(UUID uuid) {
		return this.isOwner(uuid) ? true : this.isCoowner(uuid) ? true : false;
	}

	@Transient
	public boolean isForSale() {
		return this.salePrice > 0 ? true : false;
	}

	@Transient
	public boolean isFriend(Player player) {
		return this.friends.contains(player.getUniqueId());
	}

	@Transient
	public boolean isFriend(UUID uuid) {
		return this.friends.contains(uuid);
	}

	public boolean isFriendBuild() {
		return this.friendBuild;
	}

	@Transient
	public boolean isFriendOrAbove(Player player) {
		return this.isFriendOrAbove(player.getUniqueId());
	}

	@Transient
	public boolean isFriendOrAbove(UUID uuid) {
		return this.isOwner(uuid) ? true : this.isCoowner(uuid) ? true : this
				.isFriend(uuid) ? true : false;
	}

	@Transient
	public boolean isOwner(Player player) {
		return this.isOwner(player.getUniqueId());
	}

	@Transient
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

	@Transient
	public boolean isUpdate() {
		return this.update;
	}

	@Transient
	public boolean isUpgrade(PlotUpgrade upgrade) {
		return this.upgrades.contains(upgrade);
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

	public void setNpcs(List<NPC> npcs) {
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
	
	public void save() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.update(this);
		t.commit();
		s.close();
	}
	
	public void insert() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.save(this);
		t.commit();
		s.close();
	}
	
	public void delete() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.delete(this);
		t.commit();
		s.close();
	}
}
