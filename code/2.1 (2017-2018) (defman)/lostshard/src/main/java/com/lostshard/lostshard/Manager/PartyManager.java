package com.lostshard.lostshard.Manager;

import java.util.LinkedList;
import java.util.List;

import com.lostshard.lostshard.Objects.Groups.Party;

public class PartyManager {

	private static PartyManager manager = new PartyManager();

	public static PartyManager getManager() {
		return manager;
	}

	private List<Party> paries = new LinkedList<Party>();

	private PartyManager() {

	}

	public List<Party> getParies() {
		return this.paries;
	}

	public void setParies(List<Party> paries) {
		this.paries = paries;
	}

}
