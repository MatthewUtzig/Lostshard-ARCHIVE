package com.lostshard.Whitelist;

import java.util.UUID;

public class Key {

	private final int id;
	private final UUID player;
	
	public Key(int id, UUID player) {
		this.id = id;
		this.player = player;
	}

	public int getId() {
		return id;
	}

	public UUID getPlayer() {
		return player;
	}	
}
