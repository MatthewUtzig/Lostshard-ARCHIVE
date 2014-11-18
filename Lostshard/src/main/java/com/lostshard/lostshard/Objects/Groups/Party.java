package com.lostshard.lostshard.Objects.Groups;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

public class Party {

	private List<UUID> members = new ArrayList<UUID>();
	private List<UUID> invited = new ArrayList<UUID>();
	
	public Party(UUID creater, UUID invite) {
		super();
		members.add(creater);
		invited.add(invite);
	}
	
	public boolean isDead() {
		for(UUID member : members)
			if(Bukkit.getPlayer(member) == null)
				members.remove(member);
		if(members.size() <= 0)
			return true;
		return false;
	}
}
