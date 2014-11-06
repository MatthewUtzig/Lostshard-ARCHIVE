package com.lostshard.lostshard.Objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.lostshard.lostshard.Data.Variables;

/**
 * @author Jacob Rosborg
 *
 */
public class PseudoPlayer {

	private int id = 0;
	private int money = 0;
	private int murderCounts = 0;
	private UUID playerUUID;
	private Bank bank;
	private int criminal = 0;
	private boolean globalChat = true;
	private int subscribeDays = 0;
	private boolean wasSubscribed = false;
	private int plotCreatePoints = 0;
	private Plot testPlot = null;
	private ChatChannel chatChannel = ChatChannel.GLOBAL;
	private int mana = 100;
	private int stamina = 100;
	private int rank = 800;
	private Clan clan = null;
	private Location customSpawn;
	private int spawnTick = 0;

	public PseudoPlayer(int id, int money, int murderCounts, UUID playerUUID,
			Bank bank, int criminal, boolean globalChat, int subscribeDays,
			boolean wasSubscribed, int plotCreatePoints) {
		super();
		this.id = id;
		this.money = money;
		this.murderCounts = murderCounts;
		this.playerUUID = playerUUID;
		this.bank = bank;
		this.criminal = criminal;
		this.globalChat = globalChat;
		this.subscribeDays = subscribeDays;
		this.wasSubscribed = wasSubscribed;
		this.plotCreatePoints = plotCreatePoints;
	}

	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(this.playerUUID);
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

	public int getMurderCounts() {
		return murderCounts;
	}

	public void setMurderCounts(int murderCounts) {
		this.murderCounts = murderCounts;
	}

	public boolean isMurder() {
		return this.murderCounts >= Variables.murderPoint;
	}

	public void addMurderCounts(int murderCounts) {
		this.murderCounts += murderCounts;
	}

	public void subtractMurderCounts(int murderCounts) {
		this.murderCounts -= murderCounts;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public void setPlayerUUID(UUID playerUUID) {
		this.playerUUID = playerUUID;
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
	}

	public boolean isCriminal() {
		return this.criminal > 0;
	}

	public boolean isGlobalChat() {
		return globalChat;
	}

	public void setGlobalChat(boolean global) {
		this.globalChat = global;
	}

	public int getSubscribeDays() {
		return subscribeDays;
	}

	public void setSubscribeDays(int subscribe) {
		this.subscribeDays = subscribe;
	}

	public boolean isSubscriber() {
		return subscribeDays > 0;
	}

	public boolean wasSubscribed() {
		return wasSubscribed;
	}

	public void setWasSubscribed(boolean wasSubscribed) {
		this.wasSubscribed = wasSubscribed;
	}

	public int getPlotCreatePoints() {
		return plotCreatePoints;
	}

	public void setPlotCreatePoints(int plotCreatePoints) {
		this.plotCreatePoints = plotCreatePoints;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
				+ player.getName()
				: this.isCriminal() ? ChatColor.GRAY + player.getName()
						: ChatColor.BLUE + player.getName();
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Clan getClan() {
		return clan;
	}

	public void setClan(Clan clan) {
		this.clan = clan;
	}

	public Location getCustomSpawn() {
		return customSpawn;
	}

	public void setCustomSpawn(Location customSpawn) {
		this.customSpawn = customSpawn;
	}

	public int getSpawnTick() {
		return spawnTick;
	}

	public void setSpawnTick(int spawnTick) {
		this.spawnTick = spawnTick;
	}
	
}
