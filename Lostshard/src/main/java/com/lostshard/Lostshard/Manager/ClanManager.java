package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.Lostshard.Objects.Groups.Clan;

public class ClanManager {

	private static ClanManager manager = new ClanManager();

	public static ClanManager getManager() {
		return manager;
	}

	private List<Clan> clans = new ArrayList<Clan>();

	private ClanManager() {

	}

	public List<Clan> getClans() {
		return this.clans;
	}

	public void setClans(List<Clan> clans) {
		this.clans = clans;
	}
}
