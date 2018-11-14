package com.lostshard.lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import com.google.common.collect.ImmutableSet;
import com.lostshard.lostshard.main.Lostshard;
import com.lostshard.lostshard.Objects.PlayerSet;
import com.lostshard.lostshard.Objects.Player.Bank;

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
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Type(type = "uuid-char")
	private PlayerSet leaders = new PlayerSet();

	@Transient
	private Bank bank = new Bank(false);

	public Clan() {

	}

	public Clan(String name, UUID owner) {
		super();
		this.name = name;
		this.owner = owner;
	}

	@Transient
	public Bank getBank() {
		return this.bank;
	}

	public int getId() {
		return this.id;
	}

	public PlayerSet getLeaders() {
		return this.leaders;
	}

	public ImmutableSet<UUID> getMembersAndLeders() {
		return ImmutableSet.<UUID>builder().addAll(getMembers()).addAll(getLeaders()).build();
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

	public boolean inClan(UUID uuid) {
		if (this.getMembers().contains(uuid) || this.isLeader(uuid) || this.isOwner(uuid))
			return true;
		return false;
	}
	
	public boolean isLeader(UUID uuid) {
		return this.leaders.contains(uuid);
	}
	
	public boolean isLeader(Player player) {
		return this.leaders.contains(player);
	}
	
	public boolean isLeader(OfflinePlayer player) {
		return this.leaders.contains(player);
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

	public void setLeaders(PlayerSet leaders) {
		this.leaders = leaders;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	
	public void setOwner(Player owner) {
		this.owner = owner.getUniqueId();
	}
	
	public void setOwner(OfflinePlayer owner) {
		this.owner = owner.getUniqueId();
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