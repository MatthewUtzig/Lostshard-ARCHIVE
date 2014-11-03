package com.lostshard.lostshard.Main;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.lostshard.lostshard.Commands.BankCommand;
import com.lostshard.lostshard.Commands.ControlPointsCommand;
import com.lostshard.lostshard.Commands.PlotCommand;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;

/**
 * @author Jacob Rosborg
 *
 */
public class Lostshard extends JavaPlugin {

	public static final Logger logger = Logger.getLogger("Lostshard");

	private static ArrayList<Plot> plots = new ArrayList<Plot>();
	private static ArrayList<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
	private static BukkitTask gameLoop;

	@Override
	public void onEnable() {
		//Commands
		new PlotCommand(this);
		new BankCommand(this);
		new ControlPointsCommand(this);
		// GameLoop should run last.
		gameLoop = new MainGameLoop(this).runTaskTimer(this, 0L, 20L);
	}

	@Override
	public void onDisable() {
		Database.saveAll();
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
		ArrayList<NPC> rs = new ArrayList<NPC>();
		for (Plot plot : plots)
			for (NPC npc : plot.getNpcs())
				rs.add(npc);
		return rs;
	}

	public static BukkitTask getGameLoop() {
		return gameLoop;
	}

	public static void shutdown() {
		for(Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED+"Server rebooting.");
	}

}
