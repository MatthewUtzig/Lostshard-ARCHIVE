package com.lostshard.Skyland;

import org.bukkit.WorldCreator;

public class SkyLand {
	
//	private SkyStoneGenerator skyland;
	
	private WorldCreator worldCreator;
	
	public SkyLand(String worldName, String worldUUID) {
//		skyland = new SkyStoneGenerator(worldName, worldUUID);
//		worldCreator = new WorldCreator(worldName);
//		worldCreator.generator(getSkyland());
//		worldCreator.createWorld();
	}

//	public ChunkGenerator getSkyland() {
//		return skyland;
//	}
//
//
//	public void setSkyland(SkyStoneGenerator skyland) {
//		this.skyland = skyland;
//	}

	public WorldCreator getWorldCreator() {
		return worldCreator;
	}

	public void setWorldCreator(WorldCreator worldCreator) {
		this.worldCreator = worldCreator;
	}
}