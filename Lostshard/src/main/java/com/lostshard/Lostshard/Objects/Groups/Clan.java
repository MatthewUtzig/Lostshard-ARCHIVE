package com.lostshard.Lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.PlayerListSet;
import com.lostshard.Lostshard.Objects.Player.Bank;

@Entity
public class Clan extends Group {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;

	// String's
	@Column(name = "clan_name")
	private String name;

	// UUID
	@Column(name = "owner")
	@Type(type = "uuid-char")
	private UUID owner;
	
	private PlayerListSet leaders = new PlayerListSet();

	@Transient
	private Bank bank = new Bank(false);

	@Transient
	private boolean update = false;

	public Clan() {

	}

	public Clan(String name, UUID owner) {
		super();
		this.name = name;
		this.owner = owner;
	}

	@Override
	public void addInvited(UUID invite) {
		this.getInvited().add(invite);
		this.update();
	}

	@Override
	public void addMember(UUID member) {
		this.getMembers().add(member);
		this.update();
	}

	public void demoteLeader(UUID uuid) {
		this.leaders.remove(uuid);
		this.update();
	}

	@Transient
	public Bank getBank() {
		return this.bank;
	}

	public int getId() {
		return this.id;
	}

	public PlayerListSet getLeaders() {
		return this.leaders;
	}

	public ArrayList<UUID> getMembersAndLeders() {
		final ArrayList<UUID> result = new ArrayList<UUID>();
		result.addAll(this.getMembers());
		result.addAll(this.leaders);
		result.add(this.owner);
		return result;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public List<Player> getOnlineMembers() {
		final List<Player> rs = new ArrayList<Player>();
		for (final UUID pUUID : this.getMembersAndLeders()) {
			final Player p = Bukkit.getPlayer(pUUID);
			if (p != null)
				rs.add(p);
		}
		return rs;
	}

	public UUID getOwner() {
		return this.owner;
	}

	public boolean isInClan(UUID uuid) {
		if (this.getMembers().contains(uuid) || this.isLeader(uuid) || this.isOwner(uuid))
			return true;
		return false;
	}

	public boolean isLeader(UUID uuid) {
		return this.leaders.contains(uuid);
	}

	public boolean isOwner(OfflinePlayer player) {
		return this.isOwner(player.getUniqueId());
	}

	public boolean isOwner(Player player) {
		return this.isOwner(player.getUniqueId());
	}

	public boolean isOwner(UUID owner) {
		return this.owner.equals(owner);
	}

	public boolean isUpdate() {
		return this.update;
	}

	public void promoteMember(UUID uuid) {
		this.getMembers().remove(uuid);
		this.leaders.add(uuid);
		this.update();
	}

	@Override
	public void sendMessage(String message) {
		for (final UUID member : this.getMembersAndLeders()) {
			final Player memberPlayer = Bukkit.getPlayer(member);
			if (memberPlayer != null) {
				Lostshard.log
						.finest(ChatColor.WHITE + "[" + ChatColor.GREEN + "Clan" + ChatColor.WHITE + "] " + message);
				memberPlayer.sendMessage(
						ChatColor.WHITE + "[" + ChatColor.GREEN + "Clan" + ChatColor.WHITE + "] " + message);
			}
		}
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setLeaders(PlayerListSet leaders) {
		this.leaders = leaders;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
		this.update();
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public void update() {
		this.setUpdate(true);
	}
}