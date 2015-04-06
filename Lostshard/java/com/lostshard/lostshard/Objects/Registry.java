package com.lostshard.lostshard.Objects;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Objects.Store.Store;

public class Registry {

	private List<Clan> clans = new ArrayList<Clan>();
	private List<Party> parties = new ArrayList<Party>();
	private List<Store> stores = new ArrayList<Store>();

	PlotManager ptm = PlotManager.getManager();
	
	public ArrayList<NPC> getNpcs() {
		ArrayList<NPC> rs = new ArrayList<NPC>();
		for (Plot plot : ptm.getPlots())
			for (NPC npc : plot.getNpcs())
				rs.add(npc);
		return rs;
	}

	public List<Clan> getClans() {
		return clans;
	}

	public void setClans(List<Clan> clans) {
		this.clans = clans;
	}

	public List<Party> getParties() {
		return parties;
	}

	public void setParties(List<Party> parties) {
		this.parties = parties;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}
	
}
