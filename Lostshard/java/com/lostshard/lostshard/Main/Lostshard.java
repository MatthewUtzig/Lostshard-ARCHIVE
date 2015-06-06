package com.lostshard.lostshard.Main;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.lostshard.Skyland.SkyLand;
import com.lostshard.Whitelist.KeyPlayerListener;
import com.lostshard.lostshard.Commands.AdminCommand;
import com.lostshard.lostshard.Commands.BankCommand;
import com.lostshard.lostshard.Commands.BlackSmithyCommand;
import com.lostshard.lostshard.Commands.ChatCommand;
import com.lostshard.lostshard.Commands.ChestRefillCommand;
import com.lostshard.lostshard.Commands.ClanCommand;
import com.lostshard.lostshard.Commands.ControlPointsCommand;
import com.lostshard.lostshard.Commands.FishingCommand;
import com.lostshard.lostshard.Commands.MageryCommand;
import com.lostshard.lostshard.Commands.PartyCommands;
import com.lostshard.lostshard.Commands.PlotCommand;
import com.lostshard.lostshard.Commands.ReloadCommand;
import com.lostshard.lostshard.Commands.SkillCommand;
import com.lostshard.lostshard.Commands.StoreCommand;
import com.lostshard.lostshard.Commands.SurvivalismCommand;
import com.lostshard.lostshard.Commands.TamingCommand;
import com.lostshard.lostshard.Commands.UtilsCommand;
import com.lostshard.lostshard.Database.DataSource;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Database.Mappers.ChestRefillMapper;
import com.lostshard.lostshard.Database.Mappers.ClanMapper;
import com.lostshard.lostshard.Database.Mappers.PermanentGateMapper;
import com.lostshard.lostshard.Database.Mappers.PlotMapper;
import com.lostshard.lostshard.Listener.BlockListener;
import com.lostshard.lostshard.Listener.CitizensLisenter;
import com.lostshard.lostshard.Listener.EntityListener;
import com.lostshard.lostshard.Listener.PlayerListener;
import com.lostshard.lostshard.Listener.ServerListener;
import com.lostshard.lostshard.Listener.VehicleListener;
import com.lostshard.lostshard.Listener.VoteListener;
import com.lostshard.lostshard.Listener.WorldListener;
import com.lostshard.lostshard.Manager.ConfigManager;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Spells.MagicStructure;
import com.lostshard.lostshard.Utils.ItemUtils;

/**
 * @author Jacob Rosborg
 *
 */
public class Lostshard extends JavaPlugin {

	private SkyLand skyland;
	
	public static BukkitTask getGameLoop() {
		return gameLoop;
	}

	public static Lostshard getLostshard() {
		return lostshard;
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static String getVersion() {
		return "1.0";
	}

	public static boolean isDebug() {
		return debug;
	}

	public static boolean isMysqlError() {
		return mysqlError;
	}

	public static void mysqlError() {
		Lostshard.mysqlError = true;
		System.out.print("ERROR MYSQL");
		for (final Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server ERROR something went wrong..");
	}

	public static void setDebug(boolean debug) {
		Lostshard.debug = debug;
	}

	public static void setLostshard(Lostshard lostshard) {
		Lostshard.lostshard = lostshard;
	}

	public static void setMysqlError(boolean mysqlError) {
		Lostshard.mysqlError = mysqlError;
	}

	public static void setPlugin(Plugin plugin) {
		Lostshard.plugin = plugin;
	}

	public static void shutdown() {
		for (final Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server rebooting.");
	}

	PlayerManager pm = PlayerManager.getManager();

	public static Logger log;

	private static BukkitTask gameLoop;

	private static Plugin plugin;

	private static boolean mysqlError = true;

	private static boolean debug = true;

	private static Lostshard lostshard;

	@Override
	public void onDisable() {
		for (final Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server restarting.");
		MagicStructure.removeAll();
		// Database.saveAll();
		CustomSchedule.stopSchedule();
		DataSource.getInstance().closeConnection();
	}

	@Override
	public void onEnable() {
		this.saveDefaultConfig();

		ConfigManager.getManager().setConfig(this);

		setLostshard(this);

		log = this.getLogger();
		log.info(ChatColor.GREEN + "Lostshard has invoke.");

		// Lisenters
		new BlockListener(this);
		new EntityListener(this);
		new PlayerListener(this);
		new ServerListener(this);
		new VehicleListener(this);
		if (this.getServer().getPluginManager().isPluginEnabled("votifier"))
			new VoteListener(this);
		new WorldListener(this);
		new CitizensLisenter(this);
		new KeyPlayerListener(this);
		// Commands
		new PlotCommand(this);
		new ChatCommand(this);
		new BankCommand(this);
		new ControlPointsCommand(this);
		new UtilsCommand(this);
		new AdminCommand(this);
		new SkillCommand(this);
		new ClanCommand(this);
		new MageryCommand(this);
		new PartyCommands(this);
		new ReloadCommand(this);
		new FishingCommand(this);
		new BlackSmithyCommand(this);
		new TamingCommand(this);
		new SurvivalismCommand(this);
		new StoreCommand(this);
		new ChestRefillCommand(this);
		ItemUtils.addChainMail();

		Lostshard.setPlugin(this);

		setMysqlError(!Database.testDatabaseConnection());

		PermanentGateMapper.getPermanentGates();
		ClanMapper.getClans();
		PlotMapper.getPlots();
		ChestRefillMapper.getChests();

		skyland = new SkyLand("Skyland", "1e8e7f4c-6293-40fd-91fb-1d828d59cc26");
		
		// GameLoop should run last.
		CustomSchedule.Schedule();
		gameLoop = new GameLoop(this).runTaskTimer(this, 0L, 2L);
	}

	public SkyLand getSkyland() {
		return skyland;
	}

	public void setSkyland(SkyLand skyland) {
		this.skyland = skyland;
	}

}
