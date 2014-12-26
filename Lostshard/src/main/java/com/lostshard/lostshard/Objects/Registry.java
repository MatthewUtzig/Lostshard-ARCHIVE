package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Objects.Store.Store;

public class Registry {

	private List<Plot> plots = new ArrayList<Plot>();
	private List<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
	private List<Clan> clans = new ArrayList<Clan>();
	private List<Party> parties = new ArrayList<Party>();
	private List<Store> stores = new ArrayList<Store>();
	
	public List<Plot> getPlots() {
		return plots;
	}

	public void setPlots(ArrayList<Plot> plots) {
		this.plots = plots;
	}

	public List<PseudoPlayer> getPlayers() {
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

	public List<Clan> getClans() {
		return clans;
	}

	public void setClans(ArrayList<Clan> clans) {
		this.clans = clans;
	}

	public List<Party> getParties() {
		return parties;
	}

	public void setParties(ArrayList<Party> parties) {
		this.parties = parties;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
	
}
