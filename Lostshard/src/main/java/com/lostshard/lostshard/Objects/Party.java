package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Party {

	private List<UUID> members = new ArrayList<UUID>();
	private List<UUID> invited = new ArrayList<UUID>();
	
	public Party(UUID creater, UUID invite) {
		super();
		members.add(creater);
		invited.add(invite);
	}
	
	public List<UUID> getMembers() {
		return members;
	}
	
	public void setMembers(List<UUID> members) {
		this.members = members;
	}
	
	public List<UUID> getInvited() {
		return invited;
	}
	
	public void setInvited(List<UUID> invited) {
		this.invited = invited;
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
	
	public List<UUID> getPartyMemberNames() {
		return members;
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
		for(UUID partyMemberName : members) {
			Player partyMember = Bukkit.getPlayer(partyMemberName);
			if(partyMember != null) {
				partyMember.sendMessage(message);
			}
			else removeMember(partyMemberName);
		}
	}
	
	public boolean isDead() {
		if(members.size() <= 0)
			return true;
		return false;
	}
}
