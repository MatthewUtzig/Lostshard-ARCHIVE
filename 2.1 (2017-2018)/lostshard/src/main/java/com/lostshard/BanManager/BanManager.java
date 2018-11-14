package com.lostshard.BanManager;

public class BanManager {

	public static BanManager manager = new BanManager();

	public static BanManager getManager() {
		return manager;
	}

	private BanManager() {
	}
}
