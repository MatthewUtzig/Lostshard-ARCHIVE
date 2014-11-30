package com.lostshard.lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Group {

	private ArrayList<UUID> members = new ArrayList<UUID>();
	private ArrayList<UUID> invited = new ArrayList<UUID>();

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
			if(memberPlayer != null)
				memberPlayer.sendMessage(message);
		}
	}
	
	public boolean isDead() {
		for(UUID member : members)
			if(Bukkit.getPlayer(member) == null)
				members.remove(member);
		if(members.size() <= 0)
			return true;
		return false;
	}
}
