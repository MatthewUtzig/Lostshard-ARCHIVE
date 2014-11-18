package com.lostshard.lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Clan extends Group{

	// String's
	private String name;

	// UUID
	private UUID owner;

	// Array's
	private ArrayList<UUID> leaders = new ArrayList<UUID>();

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
	
	public void promoteMember(UUID uuid) {
		this.leaders.add(uuid);
	}
	
	public void demoteMember(UUID uuid) {
		this.leaders.remove(uuid);
	}
	
	public boolean isLeader(UUID uuid) {
		return this.leaders.contains(uuid);
	}
	

	public ArrayList<UUID> getMembersAndLeders() {
		ArrayList<UUID> result = new ArrayList<UUID>();
		result.addAll(getMembers());
		result.addAll(leaders);
		return result;
	}
	
	public void sendMessage(String message) {
		for(UUID member : getMembersAndLeders()) {
			Player memberPlayer = Bukkit.getPlayer(member);
			if(memberPlayer != null) {
				memberPlayer.sendMessage(message);
			}
		}
	}

	// TODO make clan commands and database

}
