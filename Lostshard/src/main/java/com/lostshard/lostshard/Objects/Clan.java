package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Clan {

	// String's
	private String name;

	// UUID
	private UUID owner;

	// Array's
	private ArrayList<UUID> leaders = new ArrayList<UUID>();
	private ArrayList<UUID> members = new ArrayList<UUID>();
	private ArrayList<UUID> invited = new ArrayList<UUID>();

	public Clan(String name, UUID owner) {
		super();
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
	}

	public ArrayList<UUID> getLeaders() {
		return leaders;
	}

	public void setLeaders(ArrayList<UUID> leaders) {
		this.leaders = leaders;
	}

	public ArrayList<UUID> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<UUID> members) {
		this.members = members;
	}

	public ArrayList<UUID> getInvited() {
		return invited;
	}

	public void setInvited(ArrayList<UUID> invited) {
		this.invited = invited;
	}

	public ArrayList<UUID> getMembersAndLeders() {
		ArrayList<UUID> result = new ArrayList<UUID>();
		result.addAll(members);
		result.addAll(leaders);
		return result;
	}
	
	public void addMember(UUID member) {
		if(!members.contains(member)) {
			members.add(member);
		}
	}
	
	public void removeMember(UUID member) {
		int numPartyMemberNames = members.size();
		for(int i=numPartyMemberNames-1; i>=0; i--) {
			if(members.get(i).equals(member))
				members.remove(i);
		}
	}
	
	public void addInvited(UUID invite) {
		if(!this.invited.contains(invite))
			this.invited.add(invite);
	}
	
	public void removeInvited(UUID invite) {
		int numInvitedNames = invited.size();
		for(int i=numInvitedNames-1; i>=0; i--) {
			if(invited.get(i).equals(invite))
				invited.remove(i);
		}
	}
	
	public boolean isInvited(UUID invite) {
		if(invited.contains(invite))
			return true;
		return false;
	}
	
	public boolean isMember(UUID member) {
		if(members.contains(member))
			return true;
		return false;
	}
	
	public void sendMessage(String message) {
		for(UUID member : members) {
			Player memberPlayer = Bukkit.getPlayer(member);
			if(memberPlayer != null) {
				memberPlayer.sendMessage(message);
			}
		}
	}

	// TODO make clan commands and database

}
