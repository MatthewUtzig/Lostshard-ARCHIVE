package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.Lostshard.Objects.Groups.Party;

public class PartyManager {

	private static PartyManager manager = new PartyManager();

	public static PartyManager getManager() {
		return manager;
	}

	private List<Party> paries = new ArrayList<Party>();

	private PartyManager() {

	}

	public List<Party> getParies() {
		return this.paries;
	}

	public void setParies(List<Party> paries) {
		this.paries = paries;
	}

}
