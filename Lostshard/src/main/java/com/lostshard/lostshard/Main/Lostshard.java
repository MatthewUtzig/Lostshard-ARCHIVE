package com.lostshard.lostshard.Main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.lostshard.lostshard.Commands.AdminCommand;
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
	
	private static Plugin plugin;
	
	private static boolean mysqlError = true;
	
	private static boolean debug = true;
	
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
		new AdminCommand(this);
		
		Lostshard.setPlugin(this);
		
		setMysqlError(!Database.testDatabaseConnection());
		
		Database.getPlayers();
		Database.getPlots();
		
		// GameLoop should run last.
		gameLoop = new GameLoop(this).runTaskTimer(this, 0L, 10L);
	}

	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server restarting.");
//		 Database.saveAll(); 
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

	public static Plugin getPlugin() {
		return plugin;
	}

	public static void setPlugin(Plugin plugin) {
		Lostshard.plugin = plugin;
	}

	public static boolean isMysqlError() {
		return mysqlError;
	}

	public static void setMysqlError(boolean mysqlError) {
		Lostshard.mysqlError = mysqlError;
	}
	
	public static void mysqlError() {
		Lostshard.mysqlError = true;
		System.out.print("ERROR MYSQL");
		for(Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED+"Server ERROR something went wrong..");
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		Lostshard.debug = debug;
	}

}
