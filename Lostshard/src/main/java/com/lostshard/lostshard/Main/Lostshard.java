package com.lostshard.lostshard.Main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.lostshard.lostshard.Commands.BankCommand;
import com.lostshard.lostshard.Commands.ChatCommand;
import com.lostshard.lostshard.Commands.ControlPointsCommand;
import com.lostshard.lostshard.Commands.PlotCommand;
import com.lostshard.lostshard.Commands.UtilsCommand;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Listener.BlockListener;
import com.lostshard.lostshard.Listener.EntityListener;
import com.lostshard.lostshard.Listener.PlayerListener;
import com.lostshard.lostshard.Listener.ServerListener;
import com.lostshard.lostshard.Listener.VehicleListener;
import com.lostshard.lostshard.Listener.VoteListener;
import com.lostshard.lostshard.Listener.WorldListener;
import com.lostshard.lostshard.Objects.Registry;

/**
 * @author Jacob Rosborg
 *
 */
public class Lostshard extends JavaPlugin {

	public static Logger log;
	
	private static Registry registry = new Registry();
	
	private static BukkitTask gameLoop;
	
	@Override
	public void onEnable() {
		log = this.getLogger();
		log.info(ChatColor.GREEN + "Lostshard has invoke.");
		// Lisenters
		new BlockListener(this);
		new EntityListener(this);
		new PlayerListener(this);
		new ServerListener(this);
		new VehicleListener(this);
		if (getServer().getPluginManager().isPluginEnabled("votifier"))
			new VoteListener(this);
		new WorldListener(this);
		// Commands
		new PlotCommand(this);
		new ChatCommand(this);
		new BankCommand(this);
		new ControlPointsCommand(this);
		new UtilsCommand(this);
		// GameLoop should run last.
		gameLoop = new GameLoop(this).runTaskTimer(this, 0L, 20L);
	}

	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server restarting.");
		 Database.saveAll(); 
	}

	public static BukkitTask getGameLoop() {
		return gameLoop;
	}

	public static void shutdown() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server rebooting.");
	}

	public static Registry getRegistry() {
		return registry;
	}

	public static void setRegistry(Registry registry) {
		Lostshard.registry = registry;
	}

}
