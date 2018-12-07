package com.lostshard.lostshard.Manager;

import java.util.LinkedList;
import java.util.List;

import com.lostshard.lostshard.Objects.Groups.Clan;

public class ClanManager {

	private static ClanManager manager = new ClanManager();

	public static ClanManager getManager() {
		return manager;
	}

	private List<Clan> clans = new LinkedList<Clan>();

	private ClanManager() {

	}

	public List<Clan> getClans() {
		return this.clans;
	}

	public void setClans(List<Clan> clans) {
		this.clans = clans;
	}
}
