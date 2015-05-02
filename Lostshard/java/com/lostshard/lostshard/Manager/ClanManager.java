package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Objects.Groups.Clan;

public class ClanManager {

	public static ClanManager getManager() {
		return manager;
	}
	private static ClanManager manager = new ClanManager();
	
	private List<Clan> clans = new ArrayList<Clan>();

	private ClanManager() {
	
	}

	public List<Clan> getClans() {
		return clans;
	}

	public void setClans(List<Clan> clans) {
		this.clans = clans;
	}
}
