package com.lostshard.Plots.Models;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.lostshard.Economy.Wallet;

/**
 * @author Jacob Rosborg
 *
 */
@Entity
@Access(AccessType.PROPERTY)
public class Plot {

	public enum PlotUpgrade {

		TOWN("Town", 100000), DUNGEON("Dungeon", 20000), AUTOKICK("AutoKick", 10000), ARENA("Arena",
				10000), NEUTRALALIGNMENT("Neutral Alignment", 4000);

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
	
	public enum PlotEffect {

		BLACKSMITH,
		VENDOR,
		STAMINA,
		MANA,
		SHIRINE
		;

		public static boolean hasEffect(Clan clan, PlotEffect effect) {
			for (Plot p : PlotManager.getManager().getCapturePoints())
				if(p.getCapturepointData().getOwningClan() == null)
					continue;
				else if(p.getCapturepointData().getOwningClan().equals(clan) && p.getEffects().contains(effect))
					return true;
			return false;
		}
	}
	
	public enum PlotToggleable {
		
		PROTECTION(false),
		EXPLOSIONS(false),
		PRIVATE(false),
		FRIENDBUILD(false),
		TITLE(true),
		NOPVP(true),
		NOMAGIC(true),
		;
		
		private final boolean admin;
		
		private PlotToggleable(boolean admin) {
			this.admin = admin;
		}
		
		public boolean isAdmin() {
			return admin;
		}
		
	}

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();

	// String's
	private String name;

	// Int's
	private int id;
	private int size = 10;
	@ManyToOne(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private final Wallet wallet = new Wallet();
	private int salePrice = 0;

	// Array's
	private PlayerListSet friends = new PlayerListSet();
	private PlayerListSet coowners = new PlayerListSet();
	// UUID
	private UUID owner;
	
	// Upgrade's
	private Set<PlotUpgrade> upgrades = new HashSet<>();
	private Set<PlotEffect> effects = new HashSet<>();
	private Set<PlotToggleable> toggleables = new HashSet<>();
	
	// Location
	private SavableLocation location;

	// NPCS
	private Set<NPC> npcs = new LinkedHashSet<NPC>();

	private PlotCapturePoint capturepointData = null;

	public Plot() {

	}

	public Plot(String name, UUID owner, Location location) {
		this.name = name;
		this.owner = owner;
		this.location = new SavableLocation(location);
		this.toggleables.add(PlotToggleable.PROTECTION);
		this.toggleables.add(PlotToggleable.PRIVATE);
	}

	public void disband() {
		PlotManager.getManager().getPlots().remove(this);
		for (final NPC npc : this.getNpcs())
			npc.despawn();
	}

	public PlotCapturePoint getCapturepointData() {
		if (this.capturepointData == null)
			this.capturepointData = new PlotCapturePoint(this);
		return this.capturepointData;
	}

	/**
	 * @author Defman
	 * @param toSize
	 * @return Expand price from current size to size.
	 */
	@Transient
	public int getExpandPrice(int toSize) {
		return Variables.plotExpandPrice
				* (-ExtraMath.Triangular(this.size) + ExtraMath.Triangular(toSize) + this.size - toSize);
	}

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	public int getId() {
		return this.id;
	}

	@Transient
	public Location getLocation() {
		return this.location.getLocation();
	}

	@Transient
	public int getMaxVendors() {
		return this.size / 15;
	}

	@Column(unique = true)
	public String getName() {
		return this.name;
	}

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	public Set<NPC> getNpcs() {
		return this.npcs;
	}

	@Type(type = "uuid-char")
	public UUID getOwner() {
		return this.owner;
	}

	@Transient
	public String getPlayerStatusOfPlotString(Player player) {
		return this.isOwner(player) ? "You are the owner of this plot."
				: this.coowners.contains(player) ? "You are a co-owner of this plot."
						: this.friends.contains(player) ? "You are a friend of this plot." : "You are not friend of this plot.";
	}

	public SavableLocation getSavableLocation() {
		return this.location;
	}

	@Transient
	public int getTax() {
		return this.size * 10;
	}

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Enumerated(EnumType.STRING)
	public Set<PlotUpgrade> getUpgrades() {
		return this.upgrades;
	}

	@Transient
	public int getValue() {
		int plotValue = Variables.plotExpandPrice * (-55 + ExtraMath.Triangular(this.getSize()) + 10 - this.getSize());
		if (this.upgrades.contains(PlotUpgrade.TOWN))
			plotValue += Variables.plotTownPrice;
		if (this.upgrades.contains(PlotUpgrade.DUNGEON))
			plotValue += Variables.plotDungeonPrice;
		if (this.upgrades.contains(PlotUpgrade.AUTOKICK))
			plotValue += Variables.plotAutoKickPrice;
		if (this.upgrades.contains(PlotUpgrade.NEUTRALALIGNMENT))
			plotValue += Variables.plotNeutralAlignmentPrice;

		if (this.getLocation().getWorld().getEnvironment().equals(Environment.NETHER))
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
		return this.isCoownerOrAbove(uuid) ? true
				: this.friends.contains(uuid) && this.toggleables.contains(PlotToggleable.FRIENDBUILD) ? true : !this.toggleables.contains(PlotToggleable.PROTECTION) ? true : false;
	}

	@Transient
	public boolean isAllowedToInteract(Player player) {
		return this.isAllowedToInteract(player.getUniqueId());
	}

	@Transient
	public boolean isAllowedToInteract(UUID uuid) {
		if (!this.toggleables.contains(PlotToggleable.PRIVATE))
			return true;
		final PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if (pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return this.isFriendOrAbove(uuid) ? true : false;
	}

	@Transient
	public boolean isCapturepoint() {
		return this.getCapturepointData().isCapturePoint();
	}

	@Transient
	public boolean isCoownerOrAbove(Player player) {
		return this.isCoownerOrAbove(player.getUniqueId());
	}

	@Transient
	public boolean isCoownerOrAbove(UUID uuid) {
		return this.isOwner(uuid) ? true : this.coowners.contains(uuid) ? true : false;
	}

	@Transient
	public boolean isForSale() {
		return this.salePrice > 0 ? true : false;
	}

	@Transient
	public boolean isFriendOrAbove(Player player) {
		return this.isFriendOrAbove(player.getUniqueId());
	}

	@Transient
	public boolean isFriendOrAbove(UUID uuid) {
		return this.isOwner(uuid) ? true : this.coowners.contains(uuid) ? true : this.friends.contains(uuid) ? true : false;
	}

	@Transient
	public boolean isOwner(Player player) {
		return this.isOwner(player.getUniqueId());
	}

	@Transient
	public boolean isOwner(UUID uuid) {
		return uuid.equals(this.owner);
	}

	@Transient
	public void setCapturepoint(boolean isCapturepoint) {
		this.getCapturepointData().setCapturePoint(isCapturepoint);
	}

	public void setCapturepointData(PlotCapturePoint capturepoint) {
		this.capturepointData = capturepoint;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLocation(Location location) {
		this.location = new SavableLocation(location);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNpcs(Set<NPC> npcs) {
		this.npcs = npcs;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	public void setSavableLocation(SavableLocation savableLocation) {
		this.location = savableLocation;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setUpgrades(Set<PlotUpgrade> upgrades) {
		this.upgrades = upgrades;
	}
	
	@Transient
	public boolean isLawfull() {
		return !pm.isCriminal(owner);
	}
	
	@Transient
	public String getDisplayName() {
		return (this.upgrades.contains(PlotUpgrade.NEUTRALALIGNMENT) ? ChatColor.GREEN : 
			!isLawfull() ? ChatColor.RED : ChatColor.BLUE) + this.name;
	}

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Enumerated(EnumType.STRING)
	public Set<PlotEffect> getEffects() {
		return effects;
	}

	public void setEffects(Set<PlotEffect> effects) {
		this.effects = effects;
	}

	/**
	 * @return the wallet
	 */
	public Wallet getWallet() {
		return this.wallet;
	}

	/**
	 * @return the friends
	 */
	@AttributeOverrides({ @AttributeOverride(name = "players", column = @Column(name = "friends") )})
	public PlayerListSet getFriends() {
		return this.friends;
	}

	/**
	 * @param friends the friends to set
	 */
	public void setFriends(PlayerListSet friends) {
		this.friends = friends;
	}

	/**
	 * @return the coowners
	 */
	@AttributeOverrides({ @AttributeOverride(name = "players", column = @Column(name = "coowners") )})
	public PlayerListSet getCoowners() {
		return this.coowners;
	}

	/**
	 * @param coowners the coowners to set
	 */
	public void setCoowners(PlayerListSet coowners) {
		this.coowners = coowners;
	}

	/**
	 * @return the toggleables
	 */
	public Set<PlotToggleable> getToggleables() {
		return this.toggleables;
	}

	/**
	 * @param toggleables the toggleables to set
	 */
	public void setToggleables(Set<PlotToggleable> toggleables) {
		this.toggleables = toggleables;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * @return the salePrice
	 */
	public int getSalePrice() {
		return this.salePrice;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(SavableLocation location) {
		this.location = location;
	}
	
	public void insert() {
		final Session s = Lostshard.getSession();
		try {
			final Transaction t = s.beginTransaction();
			t.begin();
			s.save(this);
			t.commit();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
	}

	public void update() {
		final Session s = Lostshard.getSession();
		try {
			final Transaction t = s.beginTransaction();
			t.begin();
			s.update(this);
			t.commit();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
	
	public void delete() {
		final Session s = Lostshard.getSession();
		try {
			final Transaction t = s.beginTransaction();
			t.begin();
			s.delete(this);
			s.clear();
			t.commit();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
}
