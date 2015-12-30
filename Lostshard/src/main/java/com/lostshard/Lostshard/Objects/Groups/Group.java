package com.lostshard.Lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;

@Embeddable
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Group {

	@Transient
	public PlayerManager pm = PlayerManager.getManager();

	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable

	private List<UUID> members = new ArrayList<UUID>();
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@CollectionTable

	private List<UUID> invited = new ArrayList<UUID>();

	public void addInvited(UUID invite) {
		if (!this.invited.contains(invite))
			this.invited.add(invite);
	}

	public void addMember(UUID member) {
		this.members.add(member);
	}

	public List<UUID> getInvited() {
		return this.invited;
	}

	public List<UUID> getMembers() {
		return this.members;
	}

	public List<Player> getOnlineMembers() {
		final List<Player> rs = new ArrayList<Player>();
		for (final UUID pUUID : this.members) {
			final Player p = Bukkit.getPlayer(pUUID);
			if (p != null)
				rs.add(p);
		}
		return rs;
	}

	public boolean isDead() {
		for (final UUID member : this.members)
			if (Bukkit.getPlayer(member) == null)
				this.members.remove(member);
		if (this.members.size() <= 0)
			return true;
		return false;
	}

	public boolean isInvited(UUID invite) {
		if (this.invited.contains(invite))
			return true;
		return false;
	}

	public boolean isMember(OfflinePlayer player) {
		return this.isMember(player.getUniqueId());
	}

	public boolean isMember(Player player) {
		return this.isMember(player.getUniqueId());
	}

	public boolean isMember(UUID member) {
		if (this.members.contains(member))
			return true;
		return false;
	}

	public void removeInvited(UUID invite) {
		final int numInvitedNames = this.invited.size();
		for (int i = numInvitedNames - 1; i >= 0; i--)
			if (this.invited.get(i).equals(invite))
				this.invited.remove(i);
	}

	public void removeMember(UUID member) {
		this.members.remove(member);
	}

	public void sendMessage(String message) {
		for (final UUID member : this.members) {
			final Player memberPlayer = Bukkit.getPlayer(member);
			if (memberPlayer != null) {
				Lostshard.log.finest(message);
				memberPlayer.sendMessage(message);
			}
		}
	}

	public void setInvited(List<UUID> invited) {
		this.invited = invited;
	}

	public void setMembers(List<UUID> members) {
		this.members = members;
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
