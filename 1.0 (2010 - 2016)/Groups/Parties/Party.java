package com.lostshard.RPG.Groups.Parties;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.lostshard.RPG.Utils.Utils;

public class Party {
	private ArrayList<String> _partyMemberNames = new ArrayList<String>();
	private ArrayList<String> _invitedNames = new ArrayList<String>();
	
	public Party() {
		
	}
	
	public void addMember(String memberName) {
		if(!_partyMemberNames.contains(memberName)) {
			_partyMemberNames.add(memberName);
		}
	}
	
	public void removeMember(String memberName) {
		int numPartyMemberNames = _partyMemberNames.size();
		for(int i=numPartyMemberNames-1; i>=0; i--) {
			if(_partyMemberNames.get(i).equalsIgnoreCase(memberName))
				_partyMemberNames.remove(i);
		}
	}
	
	public ArrayList<String> getPartyMemberNames() {
		return _partyMemberNames;
	}
	
	public void addInvited(String invitedName) {
		if(!_invitedNames.contains(invitedName))
			_invitedNames.add(invitedName);
	}
	
	public void removeInvited(String invitedName) {
		int numInvitedNames = _invitedNames.size();
		for(int i=numInvitedNames-1; i>=0; i--) {
			if(_invitedNames.get(i).equalsIgnoreCase(invitedName))
				_invitedNames.remove(i);
		}
	}
	
	public boolean isInvited(String invitedName) {
		if(_invitedNames.contains(invitedName))
			return true;
		return false;
	}
	
	public boolean isMember(String memberName) {
		if(_partyMemberNames.contains(memberName))
			return true;
		return false;
	}
	
	public void sendMessage(String message) {
		for(String partyMemberName : _partyMemberNames) {
			Player partyMember = Utils.getPlugin().getServer().getPlayer(partyMemberName);
			if(partyMember != null) {
				partyMember.sendMessage(message);
			}
			else removeMember(partyMemberName);
		}
	}
	
	public boolean isDead() {
		if(_partyMemberNames.size() <= 0)
			return true;
		return false;
	}
}
