package com.lostshard.Whitelist;

import java.util.UUID;

public class KeyManager {
	
	private static final KeyManager manager = new KeyManager();
	
	private KeyManager() { }

	public static KeyManager getManager() {
		return manager;
	}
	
	public boolean createKey(UUID player) {
		Key key = KeyMapper.insertKey(player);
		return key != null;
	}
	
	public Key getKey(UUID uuid) {
		return KeyMapper.getKey(uuid);
	}
	
	public boolean isWhitelisted(UUID uuid) {
		return getKey(uuid) != null;
	}
}