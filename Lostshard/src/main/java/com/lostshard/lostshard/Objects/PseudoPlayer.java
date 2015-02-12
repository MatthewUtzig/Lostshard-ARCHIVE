package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.lostshard.Skills.Build;
import com.lostshard.lostshard.Skills.Skill;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class PseudoPlayer {

	private int id;
	private int money = 0;
	private int murderCounts = 0;
	private UUID playerUUID;
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
	private Clan clan = null;
	private Party party = null;
	private Location customSpawn = new Location(Bukkit.getWorlds().get(0),0,0,0);
	private int spawnTicks = 0;
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
	private int cantCastTicks = 0;
	private Runebook runebook = new Runebook();
	private SpellBook spellbook = new SpellBook();
	private int dieLog = 0;
	
	private List<Scroll> scrools = new ArrayList<Scroll>();
	
	private Spell promptedSpell = null;
	
	// Effects
	private int bleedTick = 0;
	private int stunTick = 0;
	
	private long lastDeath = 0;
	public int goToSpawnTicks = 0;

	public PseudoPlayer(UUID playerUUID, int id) {
		super();
		this.playerUUID = playerUUID;
		this.id = id;
		builds.add(new Build());
	}
	
	public int getBleedTick() {
		return bleedTick;
	}
	
	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(this.getPlayerUUID());
	}

	public void tick(double delta, long tick) {
		if(getOnlinePlayer() == null)
			return;
		if(tick % 10 == 0) { // one second passed 
			updateMana(delta);
			updateStamina(delta);
			bleed();
		}
		if(cantCastTicks > 0)
			cantCastTicks--;
		recentAttackersTick();
		if(spawnTicks > 0)
			setSpawnTicks(getSpawnTicks()-1);
		if(criminal > 0)
			setCriminal(getCriminal()-1);
		spawn();
	}
	
	private void recentAttackersTick() {
		for(RecentAttacker ra : recentAttackers) {
			ra.tick();
		}
		recentAttackers.removeIf(ra ->  ra.isDead());
	}

	private void spawn() {
		if(goToSpawnTicks > 0) {
			goToSpawnTicks--;
			if(goToSpawnTicks == 0) {
				Player player = getOnlinePlayer();
				player.getWorld().strikeLightningEffect(player.getLocation());
				if(isCriminal())
					player.teleport(Variables.criminalSpawn);
				else 
					player.teleport(Variables.lawfullSpawn);
	        	setSpawnTicks(36000);
	        	setMana(0);
				setStamina(0);
				player.getWorld().strikeLightningEffect(player.getLocation());
				player.sendMessage(ChatColor.GRAY+"Teleporting without a rune has exausted you.");
			}
			else if(goToSpawnTicks % 10 == 0) {
				Player player = getOnlinePlayer();
				Output.simpleError(player, "Returning to spawn in "+(goToSpawnTicks/10)+" seconds.");
			}
		}
	}

	private void bleed() {
		if(bleedTick > 0) {
			Player p = getOnlinePlayer();
			bleedTick--;
			
			if(bleedTick <= 0) {
				p.sendMessage("Your bleeding has stopped.");
				bleedTick = 0;
			}
			else
			{
				double newHealth = p.getHealth() - 1;
				if(newHealth > 20)
					newHealth = 20;
				if(newHealth < 0)
					newHealth = 0;
				p.setHealth(newHealth);
			}
		}
	}

	private void updateMana(double delta) {
		if(mana < maxMana) {
			double manaRegenMultiplier = 2; //Meditation.getManaRegenMultiplier(this);
			if(isMeditating())
				mana+=(2*manaRegenMultiplier*delta);
			else
				mana+=(1*manaRegenMultiplier*delta);
			if(mana >= maxMana) {
				mana = maxMana;
				// just reached max...
				Player p = Bukkit.getPlayer(playerUUID);
				if(p != null) {
					Output.positiveMessage(p, "Your mana has fully regenerated.");
				}
			}
		}
	}
	
	private void updateStamina(double delta) {
		if(stamina < maxStamina) {
			double staminaRegenMultiplier = 1;
			if(isResting())
				stamina+=(2*staminaRegenMultiplier*delta);
			else
				stamina+=(1*staminaRegenMultiplier*delta);
			if(stamina >= maxStamina) {
				stamina = maxStamina;
				// just reached max...
				Player p = Bukkit.getPlayer(playerUUID);
				if(p != null) {
					Output.positiveMessage(p, "Your stamina has fully regenerated.");
				}
			}
		}
	}
	
	public void setBleedTick(int bleedTick) {
		this.bleedTick = bleedTick;
	}

	public int getStunTick() {
		return stunTick;
	}

	public void setStunTick(int stunTick) {
		this.stunTick = stunTick;
	}
	
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(this.playerUUID);
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

	public int getMurderCounts() {
		return murderCounts;
	}

	public void setMurderCounts(int murderCounts) {
		this.murderCounts = murderCounts;
		update();
	}

	public boolean isMurderer() {
		return this.murderCounts >= Variables.murderPoint;
	}

	public void addMurderCounts(int murderCounts) {
		this.murderCounts += murderCounts;
		update();
	}

	public void subtractMurderCounts(int murderCounts) {
		this.murderCounts -= murderCounts;
		update();
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
		update();
	}
	
	public String getPlayerName() {
		return this.getPlayer().getName();
	}
	
	public String getDisplayName() {
		return Utils.getDisplayName(this.getPlayer());
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public int getCriminal() {
		return criminal;
	}

	public void setCriminal(int criminal) {
		this.criminal = criminal;
		update();
	}

	public boolean isCriminal() {
		return this.criminal > 0;
	}

	public boolean isGlobalChat() {
		return globalChat;
	}

	public void setGlobalChat(boolean global) {
		this.globalChat = global;
		update();
	}

	public int getSubscribeDays() {
		return subscribeDays;
	}

	public void setSubscribeDays(int subscribe) {
		this.subscribeDays = subscribe;
		update();
	}

	public boolean isSubscriber() {
		return subscribeDays > 0;
	}

	public boolean wasSubscribed() {
		return wasSubscribed;
	}

	public void setWasSubscribed(boolean wasSubscribed) {
		this.wasSubscribed = wasSubscribed;
		update();
	}

	public int getPlotCreatePoints() {
		return plotCreatePoints;
	}

	public void setPlotCreatePoints(int plotCreatePoints) {
		this.plotCreatePoints = plotCreatePoints;
		update();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		update();
	}

	public Plot getTestPlot() {
		return testPlot;
	}

	public void setTestPlot(Plot testPlot) {
		this.testPlot = testPlot;
	}

	public ChatChannel getChatChannel() {
		return chatChannel;
	}

	public void setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = chatChannel;
		update();
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public String getColoredName() {
		OfflinePlayer player = Bukkit.getOfflinePlayer(this.playerUUID);
		return this.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED
				+ player.getName() : this.isCriminal() ? ChatColor.GRAY
				+ player.getName() : ChatColor.BLUE + player.getName();
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
		update();
	}

	public Clan getClan() {
		return clan;
	}

	public void setClan(Clan clan) {
		this.clan = clan;
		update();
	}

	public Location getCustomSpawn() {
		return customSpawn;
	}

	public void setCustomSpawn(Location customSpawn) {
		this.customSpawn = customSpawn;
		update();
	}

	public int getSpawnTicks() {
		return spawnTicks;
	}

	public void setSpawnTicks(int spawnTicks) {
		this.spawnTicks = spawnTicks;
	}

	public Party getParty() {
		return party;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public List<Build> getBuilds() {
		return builds;
	}

	public void setBuilds(List<Build> builds) {
		this.builds = builds;
	}

	public boolean isPrivateChat() {
		return privateChat;
	}

	public void setPrivateChat(boolean privateChat) {
		this.privateChat = privateChat;
		update();
	}

	public int getCurrentBuildId() {
		return currentBuild;
	}

	public void setCurrentBuildId(int currentBuild) {
		this.currentBuild = currentBuild;
		update();
	}
	
	public Build getCurrentBuild() {
		return getBuilds().get(currentBuild);
	}
	
	public void setCurrentBuild(Build build) {
		getBuilds().set(currentBuild, build);
	}

	public int getPvpTicks() {
		return pvpTicks;
	}

	public void setPvpTicks(int pvpTicks) {
		this.pvpTicks = pvpTicks;
	}

	public int getEngageInCombatTicks() {
		return engageInCombatTicks;
	}

	public void setEngageInCombatTicks(int engageInCombatTicks) {
		this.engageInCombatTicks = engageInCombatTicks;
	}

	public List<RecentAttacker> getRecentAttackers() {
		return recentAttackers;
	}

	public void setRecentAttackers(ArrayList<RecentAttacker> recentAttackers) {
		this.recentAttackers = recentAttackers;
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
	
	public void clearRecentAttackers() {
		recentAttackers.clear();
	}
	
	public boolean isLastDeathOlder(long ms) {
		return new Date().getTime() > lastDeath+ms;
	}

	public long getLastDeath() {
		return lastDeath;
	}

	public void setLastDeath(long lastDeath) {
		this.lastDeath = lastDeath;
	}

	public List<ChatChannel> getDisabledChatChannels() {
		return disabledChatChannels;
	}

	public void setDisabledChatChannels(ArrayList<ChatChannel> disabledChatChannels) {
		this.disabledChatChannels = disabledChatChannels;
	}
	
	public boolean isChatChannelDisabled(ChatChannel channel) {
		return disabledChatChannels.contains(channel);
	}
	
	public void disableChatChannel(ChatChannel channel) {
		disabledChatChannels.add(channel);
	}
	
	public void enableChatChannel(ChatChannel channel) {
		disabledChatChannels.remove(channel);
	}

	public UUID getLastResiver() {
		return lastResiver;
	}

	public void setLastResiver(UUID lastResiver) {
		this.lastResiver = lastResiver;
	}

	public List<String> getTitels() {
		return titels;
	}

	public void setTitels(List<String> titels) {
		this.titels = titels;
		update();
	}

	public int getCurrentTitleId() {
		return currenttitle;
	}

	public void setCurrentTitleId(int currenttitle) {
		this.currenttitle = currenttitle;
		update();
	}
	
	public String getCurrentTitle() {
		if(currenttitle < 0)
			return "";
		else
			return titels.get(currenttitle);
	}
	
	public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}

	public boolean isMeditating() {
		return meditating;
	}

	public void setMeditating(boolean meditating) {
		this.meditating = meditating;
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
	
	public void reload() {
		PlayerManager.getManager().getPlayers().remove(this);
		PlayerManager.getManager().getPlayers().add(Database.getPlayer(id));
	}

	public boolean isResting() {
		return resting;
	}

	public void setResting(boolean resting) {
		this.resting = resting;
	}

	public int[] getBuildIds() {
		int[] ints = new int[getBuilds().size()];
		for(int i=0; i<getBuilds().size(); i++) {
			Build build = getBuilds().get(i);
			ints[i] = build.getId();
		}
		return ints;
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

	public int getMaxSkillValTotal() {
		return 4000;
	}

	public int getFreeSkillPoints() {
		return freeSkillPoints;
	}

	public void setFreeSkillPoints(int freeSkillPoints) {
		this.freeSkillPoints = freeSkillPoints;
		update();
	}

	public int getCantCastTicks() {
		return cantCastTicks;
	}

	public void setCantCastTicks(int cantCastTicks) {
		this.cantCastTicks = cantCastTicks;
	}

	public Runebook getRunebook() {
		return runebook;
	}

	public void setRunebook(Runebook runebook) {
		this.runebook = runebook;
	}

	public SpellBook getSpellbook() {
		return spellbook;
	}

	public void setSpellbook(SpellBook spellbook) {
		this.spellbook = spellbook;
	}
	
	public void setPromptedSpell(Spell promptedSpell) {
		this.promptedSpell = promptedSpell;
	}
	
	public Spell getPromptedSpell() {
		return promptedSpell;
	}

	public int getDieLog() {
		return dieLog;
	}

	public void setDieLog(int dieLog) {
		this.dieLog = dieLog;
	}

	public List<Scroll> getScrools() {
		return scrools;
	}

	public void setScrools(List<Scroll> scrools) {
		this.scrools = scrools;
	}
}
