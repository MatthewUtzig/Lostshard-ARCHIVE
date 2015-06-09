package com.lostshard.BanManager;

public class BanManager {

	public static BanManager manager = new BanManager();
	
	private BanManager() {}
	
	public static BanManager getManager() {
		return manager;
	}
}
