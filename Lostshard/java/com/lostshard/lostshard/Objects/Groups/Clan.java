package com.lostshard.lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.Bank;

public class Clan extends Group {

	private int id = 0;
	
	// String's
	private String name;

	// UUID
	private UUID owner;

	// Array's
	private List<UUID> leaders = new ArrayList<UUID>();
	
	private Bank bank = new Bank(null, false);

	private boolean update = false;
	
	public Clan(String name, UUID owner) {
		super();
		this.name = name;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
		update();
	}
	
	public boolean isOwner(UUID owner) {
		return this.owner.equals(owner);
	}

	public boolean isOwner(Player player) {
		return isOwner(player.getUniqueId());
	}
	
	public boolean isOwner(OfflinePlayer player) {
		return isOwner(player.getUniqueId());
	}
	
	public List<UUID> getLeaders() {
		return leaders;
	}

	public void setLeaders(List<UUID> leaders) {
		this.leaders = leaders;
	}
	
	public void promoteMember(UUID uuid) {
		this.leaders.add(uuid);
		update();
	}
	
	public void demoteLeader(UUID uuid) {
		this.leaders.remove(uuid);
		update();
	}
	
	public boolean isLeader(UUID uuid) {
		return this.leaders.contains(uuid);
	}
	

	public ArrayList<UUID> getMembersAndLeders() {
		ArrayList<UUID> result = new ArrayList<UUID>();
		result.addAll(getMembers());
		result.addAll(leaders);
		result.add(owner);
		return result;
	}
	
	public void sendMessage(String message) {
		for(UUID member : getMembersAndLeders()) {
			Player memberPlayer = Bukkit.getPlayer(member);
			if(memberPlayer != null) {
				memberPlayer.sendMessage(ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"] "+message);
			}
		}
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public void update() {
		setUpdate(true);
	}
	
	public void addMember(UUID member) {
		this.getMembers().add(member);
		update();
	}

	public void removeMember(UUID member) {
		int numPartyMemberNames = getMembers().size();
		for(int i=numPartyMemberNames-1; i>=0; i--) {
			if(getMembers().get(i).equals(member))
				getMembers().remove(i);
		}
		update();
	}
	
	public void addInvited(UUID invite) {
		this.getInvited().add(invite);
		update();
	}
	
	public void removeInvited(UUID invite) {
		int numInvitedNames = getInvited().size();
		for(int i=numInvitedNames-1; i>=0; i--) {
			if(getInvited().get(i).equals(invite))
				getInvited().remove(i);
		}
		update();
	}

	public boolean isInClan(UUID uuid) {
		if(getMembers().contains(uuid) || isLeader(uuid) || isOwner(uuid))
			return true;
		return false;
	}
	
	
	public List<Player> getOnlineMembers() {
		List<Player> rs = new ArrayList<Player>();
		for(UUID pUUID : getMembersAndLeders()) {
			Player p = Bukkit.getPlayer(pUUID);
			if(p != null)
				rs.add(p);
		}
		return rs;	
	}
	
	// TODO make clan commands and database

}
