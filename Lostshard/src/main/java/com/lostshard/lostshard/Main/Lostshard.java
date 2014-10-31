package com.lostshard.lostshard.Main; 

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class Lostshard extends JavaPlugin {
	
	public static final Logger logger = Logger.getLogger("Lostshard");
	
	private static ArrayList<Plot> plots = new ArrayList<Plot>();
	private static ArrayList<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
	private static ArrayList<NPC> npcs = new ArrayList<NPC>();
	private static BukkitTask gameLoop;
	
	@Override
	public void onEnable() {
		
		
		//GameLoop should run last.
		setGameLoop(new MainGameLoop(this).runTaskTimer(this, 0L, 20L));
	}
	
	@Override
	public void onDisable() {
		
	}

	public static ArrayList<Plot> getPlots() {
		return plots;
	}

	public static void setPlots(ArrayList<Plot> plots) {
		Lostshard.plots = plots;
	}

	public static ArrayList<PseudoPlayer> getPlayers() {
		return players;
	}

	public static void setPlayers(ArrayList<PseudoPlayer> players) {
		Lostshard.players = players;
	}

	public static ArrayList<NPC> getNpcs() {
		return npcs;
	}

	public static void setNpcs(ArrayList<NPC> npcs) {
		Lostshard.npcs = npcs;
	}

	public static BukkitTask getGameLoop() {
		return gameLoop;
	}

	public static void setGameLoop(BukkitTask gameLoop) {
		Lostshard.gameLoop = gameLoop;
	}
	
}
