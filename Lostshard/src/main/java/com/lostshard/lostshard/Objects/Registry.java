package com.lostshard.lostshard.Objects;

import java.util.ArrayList;

import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;

public class Registry {

	private ArrayList<Plot> plots = new ArrayList<Plot>();
	private ArrayList<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
	private ArrayList<Clan> clans = new ArrayList<Clan>();
	private ArrayList<Party> parties = new ArrayList<Party>();
	
	public ArrayList<Plot> getPlots() {
		return plots;
	}

	public void setPlots(ArrayList<Plot> plots) {
		this.plots = plots;
	}

	public ArrayList<PseudoPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<PseudoPlayer> players) {
		this.players = players;
	}

	public ArrayList<NPC> getNpcs() {
		ArrayList<NPC> rs = new ArrayList<NPC>();
		for (Plot plot : plots)
			for (NPC npc : plot.getNpcs())
				rs.add(npc);
		return rs;
	}

	public ArrayList<Clan> getClans() {
		return clans;
	}

	public void setClans(ArrayList<Clan> clans) {
		this.clans = clans;
	}

	public ArrayList<Party> getParties() {
		return parties;
	}

	public void setParties(ArrayList<Party> parties) {
		this.parties = parties;
	}
	
}
