package com.lostshard.Lostshard.Objects.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.hibernate.criterion.Restrictions;

import com.lostshard.Lostshard.Data.Locations;
import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Objects.ChatChannel;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Groups.Party;
import com.lostshard.Lostshard.Objects.InventoryGUI.GUI;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.PlotEffect;
import com.lostshard.Lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.Lostshard.Skills.Build;
import com.lostshard.Lostshard.Skills.Skill;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;
import com.lostshard.Lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
@Entity
public class PseudoPlayer {

	@Transient
	ClanManager cm = ClanManager.getManager();

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	@Type(type = "uuid-char")
	private UUID playerUUID;
	private int money = 0;
	private int murderCounts = 0;
	private Bank bank = new Bank(this.wasSubscribed());
	private int criminal = 0;
	private boolean globalChat = true;
	private boolean privateChat = true;
	private int subscribeDays = 0;
	private boolean wasSubscribed = false;
	private int plotCreatePoints = 0;
	@Transient
	private Plot testPlot = null;
	@Enumerated(EnumType.STRING)
	private ChatChannel chatChannel = ChatChannel.GLOBAL;
	private int mana = 100;
	private int stamina = 100;
	private int rank = 800;
	@Transient
	private Party party = null;
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Build> builds = new ArrayList<Build>();
	private int currentBuild = 0;
	private int pvpTicks = 0;
	private int engageInCombatTicks = 0;
	@Transient
	private List<RecentAttacker> recentAttackers = new ArrayList<RecentAttacker>();
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Enumerated(EnumType.STRING)
	private List<ChatChannel> disabledChatChannels = new ArrayList<ChatChannel>();
	@Transient
	private UUID lastResiver = null;
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> titels = new ArrayList<String>();
	private int currenttitle = -1;
	@Transient
	private boolean update = false;
	@Transient
	private boolean meditating = false;
	@Transient
	private boolean resting = false;
	private int freeSkillPoints = Variables.playerStartSkillPoints;
	private Runebook runebook = new Runebook();
	private SpellBook spellbook = new SpellBook();
	@Transient
	private int dieLog = 0;
	@Transient
	private boolean friendlyFire = false;
	@Transient
	private Reputation reputation = new Reputation(100, 0, 0, 0);
	private boolean isPrivate = true;
	@Transient
	private boolean isClaming = false;
	@Transient
	private GUI gui = null;
	private boolean allowGui = true;
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Type(type = "uuid-char")
	private List<UUID> ignored = new ArrayList<UUID>();
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Enumerated(EnumType.STRING)
	private List<Scroll> scrolls = new ArrayList<Scroll>();

	@Transient
	private Spell promptedSpell = null;
	@Transient
	private PseudoPlayerTimer timer = new PseudoPlayerTimer(this);
	@Transient
	private PseudoScoreboard scoreboard;

	public PseudoPlayer() {

	}

	public PseudoPlayer(UUID playerUUID) {
		super();
		this.playerUUID = playerUUID;
		this.builds.add(new Build());
	}

	public void addMoney(int money) {
		this.money += money;
		if (this.scoreboard != null)
			this.getScoreboard().updateMoney(this.money);
		this.update();
	}

	public void addMurderCounts(int murderCounts) {
		this.murderCounts += murderCounts;
		if (this.scoreboard != null)
			this.getScoreboard().updateMurderCounts(this.murderCounts);
		this.update();
	}

	public void addRecentAttacker(RecentAttacker recentAttacker) {
		boolean found = false;
		for (final RecentAttacker rA : this.recentAttackers)
			if (rA.getUUID().equals(recentAttacker.getUUID())) {
				rA.resetTicks();
				found = true;
				break;
			}
		if (!found)
			this.recentAttackers.add(recentAttacker);
	}

	public void addScroll(Scroll scroll) {
		this.scrolls.add(scroll);
	}

	public void addSpell(Scroll scroll) {
		this.spellbook.addSpell(scroll);
		this.update();
	}

	public void clearRecentAttackers() {
		this.recentAttackers.clear();
	}

	public void disableChatChannel(ChatChannel channel) {
		this.disabledChatChannels.add(channel);
	}

	public void enableChatChannel(ChatChannel channel) {
		this.disabledChatChannels.remove(channel);
	}

	public Bank getBank() {
		return this.bank;
	}

	public List<Build> getBuilds() {
		return this.builds;
	}

	public ChatChannel getChatChannel() {
		return this.chatChannel;
	}

	public Clan getClan() {
		for (final Clan c : this.cm.getClans())
			if (c.isInClan(this.playerUUID))
				return c;
		return null;
	}

	public String getColoredName() {
		final OfflinePlayer player = Bukkit.getOfflinePlayer(this.playerUUID);
		return this.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED + player.getName()
				: this.isCriminal() ? ChatColor.GRAY + player.getName() : ChatColor.BLUE + player.getName();
	}

	public int getCriminal() {
		return this.criminal;
	}

	public Build getCurrentBuild() {
		return this.getBuilds().get(this.currentBuild);
	}

	public int getCurrentBuildId() {
		return this.currentBuild;
	}

	public String getCurrentTitle() {
		if (this.currenttitle < this.titels.size() && this.currenttitle >= 0)
			return this.titels.get(this.currenttitle);
		return "";
	}

	public int getCurrentTitleId() {
		return this.currenttitle;
	}

	public int getDieLog() {
		return this.dieLog;
	}

	public List<ChatChannel> getDisabledChatChannels() {
		return this.disabledChatChannels;
	}

	public String getDisplayName() {
		return Utils.getDisplayName(this.getPlayer());
	}

	public int getEngageInCombatTicks() {
		return this.engageInCombatTicks;
	}

	public int getFreeSkillPoints() {
		return this.freeSkillPoints;
	}

	public GUI getGui() {
		return this.gui;
	}

	public int getId() {
		return this.id;
	}

	public List<UUID> getIgnored() {
		return this.ignored;
	}

	public UUID getLastResiver() {
		return this.lastResiver;
	}

	public int getMana() {
		return this.mana;
	}

	public int getMaxMana() {
		return PlotEffect.hasEffect(this.getClan(), PlotEffect.MANA) ? 110 : 100;
	}

	public int getMaxSkillValTotal() {
		return 4000;
	}

	public int getMaxStamina() {
		return PlotEffect.hasEffect(this.getClan(), PlotEffect.STAMINA) ? 110 : 100;
	}

	public int getMoney() {
		return this.money;
	}

	public int getMurderCounts() {
		return this.murderCounts;
	}

	public List<OfflineMessage> getOfflineMessages() {
		final Session s = Lostshard.getSession();
		try {
			@SuppressWarnings("unchecked")
			final List<OfflineMessage> messages = s.createCriteria(OfflineMessage.class)
					.add(Restrictions.eq("player", this.playerUUID)).list();
			s.close();
			return messages;
		} catch (final Exception e) {
			s.close();
			e.printStackTrace();
		}
		return new ArrayList<OfflineMessage>();
	}

	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(this.getPlayerUUID());
	}

	public Party getParty() {
		return this.party;
	}

	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(this.playerUUID);
	}

	public String getPlayerName() {
		return this.getPlayer().getName();
	}

	public UUID getPlayerUUID() {
		return this.playerUUID;
	}

	public int getPlotCreatePoints() {
		return this.plotCreatePoints;
	}

	public Spell getPromptedSpell() {
		return this.promptedSpell;
	}

	public int getPvpTicks() {
		return this.pvpTicks;
	}

	public int getRank() {
		return this.rank;
	}

	public List<RecentAttacker> getRecentAttackers() {
		return this.recentAttackers;
	}

	public Reputation getReputation() {
		return this.reputation;
	}

	public Runebook getRunebook() {
		return this.runebook;
	}

	public PseudoScoreboard getScoreboard() {
		return this.scoreboard;
	}

	public List<Scroll> getScrolls() {
		return this.scrolls;
	}

	public Skill getSkillByName(String name) {
		return this.getCurrentBuild().getSkillByName(name);
	}

	public Location getSpawn() {
		if (this.isMurderer() || this.isCriminal())
			return Locations.CRIMINAL.getLocation();
		else
			return Locations.LAWFULL.getLocation();
	}

	public SpellBook getSpellbook() {
		return this.spellbook;
	}

	public int getStamina() {
		return this.stamina;
	}

	public int getSubscribeDays() {
		return this.subscribeDays;
	}

	public Plot getTestPlot() {
		return this.testPlot;
	}

	public PseudoPlayerTimer getTimer() {
		return this.timer;
	}

	public List<String> getTitles() {
		return this.titels;
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

	public boolean isAllowGui() {
		return this.allowGui;
	}

	public boolean isChatChannelDisabled(ChatChannel channel) {
		return this.disabledChatChannels.contains(channel);
	}

	public boolean isClaming() {
		return this.isClaming;
	}

	public boolean isCriminal() {
		return this.criminal > 0;
	}

	public boolean isFriendlyFire() {
		return this.friendlyFire;
	}

	public boolean isGlobalChat() {
		return this.globalChat;
	}

	public boolean isMeditating() {
		return this.meditating;
	}

	public boolean isMurderer() {
		return this.murderCounts >= Variables.murderPoint;
	}

	public boolean isPrivate() {
		return this.isPrivate;
	}

	public boolean isPrivateChat() {
		return this.privateChat;
	}

	public boolean isResting() {
		return this.resting;
	}

	public boolean isSubscriber() {
		return this.subscribeDays > 0;
	}

	public boolean isUpdate() {
		return this.update;
	}

	public void removeScroll(Scroll scroll) {
		this.scrolls.remove(scroll);
	}

	public void save() {
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

	public void setAllowGui(boolean allowGui) {
		this.allowGui = allowGui;
		this.update();
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}

	public void setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = chatChannel;
		this.update();
	}

	public void setClaming(boolean isClaming) {
		this.isClaming = isClaming;
	}

	public void setCriminal(int criminal) {
		this.criminal = criminal;
		if (this.getOnlinePlayer() != null && this.scoreboard != null)
			this.getOnlinePlayer()
					.setDisplayName((this.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED
							: this.isCriminal() ? ChatColor.GRAY : ChatColor.BLUE) + this.getOnlinePlayer().getName()
							+ ChatColor.RESET);
		if (this.scoreboard != null)
			this.getScoreboard().updateTeams();
		this.update();
	}

	public void setCurrentBuild(Build build) {
		this.getBuilds().set(this.currentBuild, build);
	}

	public void setCurrentBuildId(int currentBuild) {
		this.currentBuild = Math.max(0, currentBuild);
		if (this.scoreboard != null)
			this.getScoreboard().updateBuild(this.currentBuild);
		this.update();
	}

	public void setCurrentTitleId(int currenttitle) {
		this.currenttitle = currenttitle;
		this.update();
	}

	public void setDieLog(int dieLog) {
		this.dieLog = dieLog;
	}

	public void setDisabledChatChannels(ArrayList<ChatChannel> disabledChatChannels) {
		this.disabledChatChannels = disabledChatChannels;
	}

	public void setEngageInCombatTicks(int engageInCombatTicks) {
		this.engageInCombatTicks = engageInCombatTicks;
	}

	public void setFreeSkillPoints(int freeSkillPoints) {
		this.freeSkillPoints = freeSkillPoints;
		this.update();
	}

	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public void setGlobalChat(boolean global) {
		this.globalChat = global;
		this.update();
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIgnored(List<UUID> ignored) {
		this.ignored = ignored;
	}

	public void setLastResiver(UUID lastResiver) {
		this.lastResiver = lastResiver;
	}

	public void setMana(int mana) {
		this.mana = Math.max(mana, 0);
		if (this.scoreboard != null)
			this.getScoreboard().updateMana(this.mana);
	}

	public void setMeditating(boolean meditating) {
		this.meditating = meditating;
	}

	public void setMoney(int money) {
		this.money = Math.max(0, money);
		if (this.scoreboard != null)
			this.getScoreboard().updateMoney(this.money);
		this.update();
	}

	public void setMurderCounts(int murderCounts) {
		this.murderCounts = Math.max(0, murderCounts);
		if (this.scoreboard != null) {
			this.getScoreboard().updateMurderCounts(this.murderCounts);
			if (this.getOnlinePlayer() != null)
				this.getOnlinePlayer().setDisplayName(Utils.getDisplayName(this.getOnlinePlayer()) + ChatColor.RESET);
			this.getScoreboard().updateTeams();
		}
		this.update();
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
		this.update();
	}

	public void setPlotCreatePoints(int plotCreatePoints) {
		this.plotCreatePoints = plotCreatePoints;
		this.update();
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setPrivateChat(boolean privateChat) {
		this.privateChat = privateChat;
		this.update();
	}

	public void setPromptedSpell(Spell promptedSpell) {
		this.promptedSpell = promptedSpell;
	}

	public void setPvpTicks(int pvpTicks) {
		this.pvpTicks = pvpTicks;
	}

	public void setRank(int rank) {
		this.rank = Math.max(0, rank);
		if (this.scoreboard != null)
			this.getScoreboard().updateRank(this.rank);
		this.update();
	}

	public void setRecentAttackers(ArrayList<RecentAttacker> recentAttackers) {
		this.recentAttackers = recentAttackers;
	}

	public void setReputation(Reputation reputation) {
		this.reputation = reputation;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
	}

	public void setRunebook(Runebook runebook) {
		this.runebook = runebook;
	}

	public void setScoreboard(PseudoScoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}

	public void setScrolls(List<Scroll> scrolls) {
		this.scrolls = scrolls;
	}

	public void setSpellbook(SpellBook spellbook) {
		this.spellbook = spellbook;
	}

	public void setStamina(int stamina) {
		this.stamina = Math.max(stamina, 0);
		if (this.scoreboard != null)
			this.getScoreboard().updateStamina(this.stamina);
	}

	public void setSubscribeDays(int subscribe) {
		this.subscribeDays = subscribe;
		this.update();
	}

	public void setTestPlot(Plot testPlot) {
		this.testPlot = testPlot;
	}

	public void setTimer(PseudoPlayerTimer timer) {
		this.timer = timer;
	}

	public void setTitels(List<String> titels) {
		this.titels = titels;
		this.update();
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void setWasSubscribed(boolean wasSubscribed) {
		this.wasSubscribed = wasSubscribed;
		this.update();
	}

	public void subtractMoney(int money) {
		this.money -= money;
		if (this.scoreboard != null)
			this.getScoreboard().updateMoney(this.money);
		this.update();
	}

	public void subtractMurderCounts(int murderCounts) {
		this.murderCounts -= murderCounts;
		if (this.scoreboard != null)
			this.getScoreboard().updateMurderCounts(murderCounts);
		this.update();
	}

	public void tick(double delta, long tick) {
		this.timer.tick(delta, tick);
	}

	public void update() {
		this.update = true;
	}

	public boolean wasSubscribed() {
		return this.wasSubscribed;
	}
}