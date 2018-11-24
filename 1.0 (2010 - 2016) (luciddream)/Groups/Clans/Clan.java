package com.lostshard.RPG.Groups.Clans;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Utils;

public class Clan {
	private String _name;
	private String _ownerName;
	private ArrayList<String> _leaders;
	private ArrayList<String> _members;
	private ArrayList<String> _invited = new ArrayList<String>();
	private String _cloakURL = null;
	public int _maxMana = 100;
	public int _maxStamina = 100;
	
	public Clan(String name, String ownerName, ArrayList<String> leaders, ArrayList<String> members) {
		_name = name;
		_ownerName = ownerName;
		_leaders = leaders;
		_members = members;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getOwnerName() {
		return _ownerName;
	}
	
	public void SetMaxMana(int mana) {
		_maxMana = mana;
		
		ArrayList<Player> onlinePlayers = getOnlineMembers();
		for(Player p : onlinePlayers) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(p.getName());
			if(pseudoPlayer != null) {
				pseudoPlayer._maxMana = mana;
				if(pseudoPlayer.getMana() > mana)
					pseudoPlayer.setMana(mana);
			}
		}
	}
	
	public void SetMaxStamina(int stamina) {
		_maxStamina = stamina;
		
		ArrayList<Player> onlinePlayers = getOnlineMembers();
		for(Player p : onlinePlayers) {
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(p.getName());
			if(pseudoPlayer != null) {
				pseudoPlayer._maxStamina = stamina;
				if(pseudoPlayer.getStamina() > stamina)
					pseudoPlayer.setStamina(stamina);
			}
		}
	}
	
	public ArrayList<String> getLeaders() {
		return _leaders;
	}
	
	public ArrayList<String> getMembers() {
		return _members;
	}
	
	public boolean isOwner(String playerName) {
		if(_ownerName.equalsIgnoreCase(playerName))
			return true;
		return false;
	}
	
	public boolean isLeader(String playerName) {
		for(String lN : _leaders) {
			if(lN.equalsIgnoreCase(playerName))
				return true;
		}
		return false;
	}
	
	public void addLeader(String playerName) {
		_leaders.add(playerName);
	}
	
	public void removeLeader(String playerName) {
		int numLeaders = _leaders.size();
		for(int i=numLeaders-1; i >= 0; i--) {
			if(_leaders.get(i).equalsIgnoreCase(playerName))
				_leaders.remove(i);
		}
	}
	
	public boolean isMember(String playerName) {
		for(String mN : _members) {
			if(mN.equalsIgnoreCase(playerName))
				return true;
		}
		return false;
	}
	
	public void addMember(String playerName) {
		_members.add(playerName);
	}
	
	public void removeMember(String playerName) {
		int numMembers = _members.size();
		for(int i=numMembers-1; i >= 0; i--) {
			if(_members.get(i).equalsIgnoreCase(playerName))
				_members.remove(i);
		}
	}
	
	public void addInvited(String playerName) {
		_invited.add(playerName);
	}
	
	public void removeInvited(String playerName) {
		int numInvited = _invited.size();
		for(int i=numInvited-1; i>=0; i--) {
			if(_invited.get(i).equalsIgnoreCase(playerName))
				_invited.remove(i);
		}
	}
	
	public boolean isInvitedStrict(String playerName) {
		if(_invited.contains(playerName))
			return true;
		return false;
	}
	
	public boolean isInvited(String playerName) {
		for(String iName : _invited) {
			if(iName.equalsIgnoreCase(playerName))
				return true;
		}
		return false;
	}
	
	public boolean isInClan(String name) {
		if(isOwner(name) || isLeader(name) || isMember(name))
			return true;
		return false;
	}
	
	public ArrayList<String> getOnlineMemberNames() {
		ArrayList<String> onlineMemberNames = new ArrayList<String>();
		Player owner = Utils.getPlugin().getServer().getPlayer(_ownerName);
		if(owner!=null)
			onlineMemberNames.add(owner.getName());
		
		for(String leaderName : _leaders) {
			Player leader = Utils.getPlugin().getServer().getPlayer(leaderName);
			if(leader!=null)
				onlineMemberNames.add(leader.getName());
		}
		
		for(String memberName : _members) {
			Player member = Utils.getPlugin().getServer().getPlayer(memberName);
			if(member!=null)
				onlineMemberNames.add(member.getName());
		}
		
		return onlineMemberNames;
	}
	
	public ArrayList<Player> getOnlineMembers() {
		ArrayList<Player> onlineMemberNames = new ArrayList<Player>();
		Player owner = Utils.getPlugin().getServer().getPlayer(_ownerName);
		if(owner!=null)
			onlineMemberNames.add(owner);
		
		for(String leaderName : _leaders) {
			Player leader = Utils.getPlugin().getServer().getPlayer(leaderName);
			if(leader!=null)
				onlineMemberNames.add(leader);
		}
		
		for(String memberName : _members) {
			Player member = Utils.getPlugin().getServer().getPlayer(memberName);
			if(member!=null)
				onlineMemberNames.add(member);
		}
		
		return onlineMemberNames;
	}
	
	public void setOwner(String ownerName) {
		_ownerName = ownerName;
	}
	
	public String getCloakURL() {
		return _cloakURL;
	}
	
	public void setCloakURL(String cloakURL) {
		_cloakURL = cloakURL;
	}
}
