package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.UUID;

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

	// TODO make clan commands and database

}
