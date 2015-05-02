package com.lostshard.lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
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

	@Override
	public void addInvited(UUID invite) {
		this.getInvited().add(invite);
		update();
	}

	@Override
	public void addMember(UUID member) {
		this.getMembers().add(member);
		update();
	}

	public void demoteLeader(UUID uuid) {
		this.leaders.remove(uuid);
		update();
	}

	public Bank getBank() {
		return bank;
	}
	
	public int getId() {
		return id;
	}

	public List<UUID> getLeaders() {
		return leaders;
	}
	
	public ArrayList<UUID> getMembersAndLeders() {
		ArrayList<UUID> result = new ArrayList<UUID>();
		result.addAll(getMembers());
		result.addAll(leaders);
		result.add(owner);
		return result;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public List<Player> getOnlineMembers() {
		List<Player> rs = new ArrayList<Player>();
		for(UUID pUUID : getMembersAndLeders()) {
			Player p = Bukkit.getPlayer(pUUID);
			if(p != null)
				rs.add(p);
		}
		return rs;	
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public boolean isInClan(UUID uuid) {
		if(getMembers().contains(uuid) || isLeader(uuid) || isOwner(uuid))
			return true;
		return false;
	}
	
	public boolean isLeader(UUID uuid) {
		return this.leaders.contains(uuid);
	}
	

	public boolean isOwner(OfflinePlayer player) {
		return isOwner(player.getUniqueId());
	}
	
	public boolean isOwner(Player player) {
		return isOwner(player.getUniqueId());
	}

	public boolean isOwner(UUID owner) {
		return this.owner.equals(owner);
	}

	public boolean isUpdate() {
		return update;
	}

	public void promoteMember(UUID uuid) {
		this.leaders.add(uuid);
		update();
	}

	@Override
	public void removeInvited(UUID invite) {
		int numInvitedNames = getInvited().size();
		for(int i=numInvitedNames-1; i>=0; i--) {
			if(getInvited().get(i).equals(invite))
				getInvited().remove(i);
		}
		update();
	}

	@Override
	public void removeMember(UUID member) {
		int numPartyMemberNames = getMembers().size();
		for(int i=numPartyMemberNames-1; i>=0; i--) {
			if(getMembers().get(i).equals(member))
				getMembers().remove(i);
		}
		update();
	}

	@Override
	public void sendMessage(String message) {
		for(UUID member : getMembersAndLeders()) {
			Player memberPlayer = Bukkit.getPlayer(member);
			if(memberPlayer != null) {
				Lostshard.log.finest(ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"] "+message);
				memberPlayer.sendMessage(ChatColor.WHITE+"["+ChatColor.GREEN+"Clan"+ChatColor.WHITE+"] "+message);
			}
		}
	}
	
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setLeaders(List<UUID> leaders) {
		this.leaders = leaders;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setOwner(UUID owner) {
		this.owner = owner;
		update();
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	
	public void update() {
		setUpdate(true);
	}
}