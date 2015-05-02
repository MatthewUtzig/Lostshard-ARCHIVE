package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Locations;
import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Objects.InventoryGUI.GUI;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.lostshard.Skills.Build;
import com.lostshard.lostshard.Skills.Skill;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class PseudoPlayer {

	ClanManager cm = ClanManager.getManager();
	
	private int id;
	private UUID playerUUID;
	private int money = 0;
	private int murderCounts = 0;
	private Bank bank = new Bank("", wasSubscribed());
	private int criminal = 0;
	private boolean globalChat = true;
	private boolean privateChat = true;
	private int subscribeDays = 0;
	private boolean wasSubscribed = false;
	private int plotCreatePoints = 0;
	private Plot testPlot = null;
	private ChatChannel chatChannel = ChatChannel.GLOBAL;
	private int mana = 100;
	private int stamina = 100;
	private int rank = 800;
	private Party party = null;
	private List<Build> builds = new ArrayList<Build>();
	private int currentBuild = 0;
	private int pvpTicks = 0;
	private int engageInCombatTicks = 0;
	private List<RecentAttacker> recentAttackers = new ArrayList<RecentAttacker>();
	private List<ChatChannel> disabledChatChannels = new ArrayList<ChatChannel>();	
	private UUID lastResiver = null;
	private List<String> titels = new ArrayList<String>();
	private int currenttitle = -1;
	private boolean update = false;
	private int maxMana = 100;
	private int maxStamina = 100;
	private boolean meditating = false;
	private boolean resting = false;
	private int freeSkillPoints = 0;
	private Runebook runebook = new Runebook();
	private SpellBook spellbook = new SpellBook();
	private int dieLog = 0;
	private boolean friendlyFire = false;
	private Reputation reputation = new Reputation(100, 0, 0, 0);
	private boolean isPrivate = true;
	private boolean isClaming = false;
	private GUI gui = null;
	private boolean allowGui = true;
	private List<UUID> ignored = new ArrayList<UUID>();
	
	private List<Scroll> scrolls = new ArrayList<Scroll>();
	
	private Spell promptedSpell = null;
	
	private PseudoPlayerTimer timer = new PseudoPlayerTimer(this);
	private PseudoScoreboard scoreboard;

	public PseudoPlayer(UUID playerUUID, int id) {
		super();
		this.playerUUID = playerUUID;
		this.id = id;
		builds.add(new Build());
	}
	
	public void addMoney(int money) {
		this.money += money;
		if(scoreboard != null)
			getScoreboard().updateMoney(this.money);
		update();
	}
	
	public void addMurderCounts(int murderCounts) {
		this.murderCounts += murderCounts;
		if(scoreboard != null)
			getScoreboard().updateMurderCounts(this.murderCounts);
		update();
	}
	
	public void addRecentAttacker(RecentAttacker recentAttacker) {
		boolean found = false;
		for(RecentAttacker rA : recentAttackers) {
			if(rA.getUUID().equals(recentAttacker.getUUID())) {
				rA.resetTicks();
				found = true;
				break;
			}
		}
		if(!found)
			recentAttackers.add(recentAttacker);
	}

	public void addScroll(Scroll scroll) {
		scrolls.add(scroll);
	}

	public void addSpell(Scroll scroll) {
		this.spellbook.addSpell(scroll);
		update();
	}

	public void clearRecentAttackers() {
		recentAttackers.clear();
	}

	public void disableChatChannel(ChatChannel channel) {
		disabledChatChannels.add(channel);
	}

	public void enableChatChannel(ChatChannel channel) {
		disabledChatChannels.remove(channel);
	}

	public Bank getBank() {
		return bank;
	}

	public int[] getBuildIds() {
		int[] ints = new int[getBuilds().size()];
		for(int i=0; i<getBuilds().size(); i++) {
			Build build = getBuilds().get(i);
			ints[i] = build.getId();
		}
		return ints;
	}

	public List<Build> getBuilds() {
		return builds;
	}

	public ChatChannel getChatChannel() {
		return chatChannel;
	}

	public Clan getClan() {
		for(Clan c : cm.getClans())
			if(c.isInClan(playerUUID))
				return c;
		return null;
	}

	public String getColoredName() {
		OfflinePlayer player = Bukkit.getOfflinePlayer(this.playerUUID);
		return this.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED
				+ player.getName() : this.isCriminal() ? ChatColor.GRAY
				+ player.getName() : ChatColor.BLUE + player.getName();
	}
	
	public int getCriminal() {
		return criminal;
	}
	
	public Build getCurrentBuild() {
		return getBuilds().get(currentBuild);
	}

	public int getCurrentBuildId() {
		return currentBuild;
	}

	public String getCurrentTitle() {
		if(currenttitle < 0)
			return "";
		else
			return titels.get(currenttitle);
	}

	public int getCurrentTitleId() {
		return currenttitle;
	}

	public int getDieLog() {
		return dieLog;
	}

	public List<ChatChannel> getDisabledChatChannels() {
		return disabledChatChannels;
	}

	public String getDisplayName() {
		return Utils.getDisplayName(this.getPlayer());
	}

	public int getEngageInCombatTicks() {
		return engageInCombatTicks;
	}

	public int getFreeSkillPoints() {
		return freeSkillPoints;
	}

	public GUI getGui() {
		return gui;
	}

	public int getId() {
		return id;
	}

	public List<UUID> getIgnored() {
		return ignored;
	}

	public UUID getLastResiver() {
		return lastResiver;
	}

	public int getMana() {
		return mana;
	}

	public int getMaxMana() {
		return maxMana;
	}

	public int getMaxSkillValTotal() {
		return 4000;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public int getMoney() {
		return money;
	}

	public int getMurderCounts() {
		return murderCounts;
	}

	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(this.getPlayerUUID());
	}

	public Party getParty() {
		return party;
	}

	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(this.playerUUID);
	}

	public String getPlayerName() {
		return this.getPlayer().getName();
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public int getPlotCreatePoints() {
		return plotCreatePoints;
	}

	public Spell getPromptedSpell() {
		return promptedSpell;
	}

	public int getPvpTicks() {
		return pvpTicks;
	}

	public int getRank() {
		return rank;
	}

	public List<RecentAttacker> getRecentAttackers() {
		return recentAttackers;
	}

	public Reputation getReputation() {
		return reputation;
	}

	public Runebook getRunebook() {
		return runebook;
	}

	public PseudoScoreboard getScoreboard() {
		return scoreboard;
	}

	public List<Scroll> getScrolls() {
		return scrolls;
	}

	public Skill getSkillByName(String name) {
		Skill skill = null;
		if(name.equalsIgnoreCase("mining"))
			return getCurrentBuild().getMining();
		else if(name.equalsIgnoreCase("lumberjacking"))
			return getCurrentBuild().getLumberjacking();
		else if(name.equalsIgnoreCase("blacksmithy"))
			return getCurrentBuild().getBlackSmithy();
		else if(name.equalsIgnoreCase("blades"))
			return getCurrentBuild().getBlades();
		else if(name.equalsIgnoreCase("archery"))
			return getCurrentBuild().getArchery();
		else if(name.equalsIgnoreCase("brawling"))
			return getCurrentBuild().getBrawling();
		else if(name.equalsIgnoreCase("fishing"))
			return getCurrentBuild().getFishing();
		else if(name.equalsIgnoreCase("lumberjacking"))
			return getCurrentBuild().getLumberjacking();
		else if(name.equalsIgnoreCase("magery"))
			return getCurrentBuild().getMagery();
		else if(name.equalsIgnoreCase("mining"))
			return getCurrentBuild().getMining();
		else if(name.equalsIgnoreCase("survivalism"))
			return getCurrentBuild().getSurvivalism();
		else if(name.equalsIgnoreCase("taming"))
			return getCurrentBuild().getTaming();
		return skill;
	}

	public Location getSpawn() {
		if(this.isMurderer() || this.isCriminal())
			return Locations.CRIMINAL.getLocation();
		else
			return Locations.LAWFULL.getLocation();
	}

	public SpellBook getSpellbook() {
		return spellbook;
	}

	public int getStamina() {
		return stamina;
	}

	public int getSubscribeDays() {
		return subscribeDays;
	}
	
	public Plot getTestPlot() {
		return testPlot;
	}
	
	public PseudoPlayerTimer getTimer() {
		return timer;
	}

	public List<String> getTitels() {
		return titels;
	}

	public boolean isAllowGui() {
		return allowGui;
	}

	public boolean isChatChannelDisabled(ChatChannel channel) {
		return disabledChatChannels.contains(channel);
	}

	public boolean isClaming() {
		return isClaming;
	}

	public boolean isCriminal() {
		return this.criminal > 0;
	}

	public boolean isFriendlyFire() {
		return friendlyFire;
	}
	
	public boolean isGlobalChat() {
		return globalChat;
	}
	
	public boolean isMeditating() {
		return meditating;
	}

	public boolean isMurderer() {
		return this.murderCounts >= Variables.murderPoint;
	}

	public boolean isPrivate() {
		return isPrivate;
	}
	
	public boolean isPrivateChat() {
		return privateChat;
	}
	
	public boolean isResting() {
		return resting;
	}
	
	public boolean isSubscriber() {
		return subscribeDays > 0;
	}

	public boolean isUpdate() {
		return update;
	}

	public void removeScroll(Scroll scroll) {
		scrolls.remove(scroll);
	}

	public void setAllowGui(boolean allowGui) {
		this.allowGui = allowGui;
		update();
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}

	public void setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = chatChannel;
		update();
	}
	
	public void setClaming(boolean isClaming) {
		this.isClaming = isClaming;
	}
	
	public void setCriminal(int criminal) {
		boolean yesupdate = true;
		if(this.criminal > 0)
			yesupdate = false;
		this.criminal = criminal;
		if(getScoreboard() != null)
			if(!isMurderer() && isCriminal() && yesupdate)
				getScoreboard().updateTeams();
		update();
	}

	public void setCurrentBuild(Build build) {
		getBuilds().set(currentBuild, build);
	}

	public void setCurrentBuildId(int currentBuild) {
		this.currentBuild = Math.max(0,currentBuild);
		if(scoreboard != null)
			getScoreboard().updateBuild(this.currentBuild);
		update();
	}

	public void setCurrentTitleId(int currenttitle) {
		this.currenttitle = currenttitle;
		update();
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
		update();
	}
	
	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public void setGlobalChat(boolean global) {
		this.globalChat = global;
		update();
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public void setId(int id) {
		this.id = id;
		update();
	}
	
	public void setIgnored(List<UUID> ignored) {
		this.ignored = ignored;
	}

	public void setLastResiver(UUID lastResiver) {
		this.lastResiver = lastResiver;
	}

	public void setMana(int mana) {
		this.mana = Math.max(mana,0);
		if(scoreboard != null)
			getScoreboard().updateMana(this.mana);
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}

	public void setMeditating(boolean meditating) {
		this.meditating = meditating;
	}

	public void setMoney(int money) {
		this.money = Math.max(0,money);
		if(scoreboard != null)
			getScoreboard().updateMoney(this.money);
		update();
	}

	public void setMurderCounts(int murderCounts) {
		this.murderCounts = Math.max(0,murderCounts);
		if(scoreboard != null) {
			getScoreboard().updateMurderCounts(this.murderCounts);
			getScoreboard().updateTeams();
		}
		update();
	}
	
	public void setParty(Party party) {
		this.party = party;
	}
	
	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
		update();
	}
	
	public void setPlotCreatePoints(int plotCreatePoints) {
		this.plotCreatePoints = plotCreatePoints;
		update();
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setPrivateChat(boolean privateChat) {
		this.privateChat = privateChat;
		update();
	}

	public void setPromptedSpell(Spell promptedSpell) {
		this.promptedSpell = promptedSpell;
	}

	public void setPvpTicks(int pvpTicks) {
		this.pvpTicks = pvpTicks;
	}

	public void setRank(int rank) {
		this.rank = Math.max(0,rank);
		if(scoreboard != null)
			getScoreboard().updateRank(this.rank);
		update();
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

	public void setScrools(List<Scroll> scrolls) {
		this.scrolls = scrolls;
	}

	public void setSpellbook(SpellBook spellbook) {
		this.spellbook = spellbook;
	}

	public void setStamina(int stamina) {
		this.stamina = Math.max(stamina,0);
		if(scoreboard != null)
			getScoreboard().updateStamina(this.stamina);
	}

	public void setSubscribeDays(int subscribe) {
		this.subscribeDays = subscribe;
		update();
	}

	public void setTestPlot(Plot testPlot) {
		this.testPlot = testPlot;
	}

	public void setTimer(PseudoPlayerTimer timer) {
		this.timer = timer;
	}

	public void setTitels(List<String> titels) {
		this.titels = titels;
		update();
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void setWasSubscribed(boolean wasSubscribed) {
		this.wasSubscribed = wasSubscribed;
		update();
	}

	public void subtractMoney(int money) {
		this.money -= money;
		if(scoreboard != null)
			getScoreboard().updateMoney(this.money);
		update();
	}

	public void subtractMurderCounts(int murderCounts) {
		this.murderCounts -= murderCounts;
		if(scoreboard != null)
			getScoreboard().updateMurderCounts(murderCounts);
		update();
	}

	public void tick(double delta, long tick) {
		timer.tick(delta, tick);
	}

	public void update() {
		this.update = true;
	}

	public boolean wasSubscribed() {
		return wasSubscribed;
	}
}
