package com.lostshard.Lostshard.Main;

import java.util.logging.Logger;

import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.lostshard.Crates.CrateManager;
import com.lostshard.Crates.CratePlayerListener;
import com.lostshard.Lostshard.Commands.AdminCommand;
import com.lostshard.Lostshard.Commands.BankCommand;
import com.lostshard.Lostshard.Commands.BlackSmithyCommand;
import com.lostshard.Lostshard.Commands.ChatCommand;
import com.lostshard.Lostshard.Commands.ChestRefillCommand;
import com.lostshard.Lostshard.Commands.ClanCommand;
import com.lostshard.Lostshard.Commands.ControlPointsCommand;
import com.lostshard.Lostshard.Commands.FishingCommand;
import com.lostshard.Lostshard.Commands.MageryCommand;
import com.lostshard.Lostshard.Commands.PartyCommands;
import com.lostshard.Lostshard.Commands.PlotCommand;
import com.lostshard.Lostshard.Commands.ReloadCommand;
import com.lostshard.Lostshard.Commands.SkillCommand;
import com.lostshard.Lostshard.Commands.StoreCommand;
import com.lostshard.Lostshard.Commands.SurvivalismCommand;
import com.lostshard.Lostshard.Commands.TamingCommand;
import com.lostshard.Lostshard.Commands.UtilsCommand;
import com.lostshard.Lostshard.Data.Locations;
import com.lostshard.Lostshard.Database.DataSource;
import com.lostshard.Lostshard.Database.Database;
import com.lostshard.Lostshard.Database.Mappers.ChestRefillMapper;
import com.lostshard.Lostshard.Database.Mappers.ClanMapper;
import com.lostshard.Lostshard.Database.Mappers.PermanentGateMapper;
import com.lostshard.Lostshard.Database.Mappers.PlayerMapper;
import com.lostshard.Lostshard.Database.Mappers.PlotMapper;
import com.lostshard.Lostshard.Listener.BlockListener;
import com.lostshard.Lostshard.Listener.EntityListener;
import com.lostshard.Lostshard.Listener.PlayerListener;
import com.lostshard.Lostshard.Listener.ServerListener;
import com.lostshard.Lostshard.Listener.VehicleListener;
import com.lostshard.Lostshard.Listener.VoteListener;
import com.lostshard.Lostshard.Listener.WorldListener;
import com.lostshard.Lostshard.Manager.ConfigManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.NPC.NPCLib.NPCLibManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.PseudoScoreboard;
import com.lostshard.Lostshard.Spells.MagicStructure;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Utils;
import com.lostshard.Whitelist.KeyPlayerListener;

/**
 * @author Jacob Rosborg
 *
 */
public class Lostshard extends JavaPlugin {

	private static final int maxPlayers = 30;
	
	public BukkitTask getGameLoop() {
		return gameLoop;
	}
	public Plugin getPlugin() {
		return plugin;
	}

	public String getVersion() {
		return "1.0";
	}

	public static boolean isDebug() {
		return Lostshard.debug;
	}

	public boolean isMysqlError() {
		return mysqlError;
	}

	public static void mysqlError() {
		mysqlError = true;
		System.out.print("ERROR MYSQL");
		for (final Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server ERROR something went wrong..");
	}

	public static void setDebug(boolean debug) {
		Lostshard.debug = debug;
	}

	public static void setMysqlError(boolean mysqlError) {
		Lostshard.mysqlError = mysqlError;
	}

	public void setPlugin(Plugin plugin) {
		Lostshard.plugin = plugin;
	}

	public void shutdown() {
		for (final Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server rebooting.");
	}

	PlayerManager pm = PlayerManager.getManager();

	public static Logger log;

	private BukkitTask gameLoop;

	private BukkitTask asyncGameLoop;
	
	private static Plugin plugin;

	private static boolean mysqlError = true;

	private static boolean debug = true;

	@Override
	public void onDisable() {
		for(NPC npc : NPCLibManager.getManager().getRegistry().sorted())
			npc.despawn();
		NPCLibManager.getManager().getRegistry().deregisterAll();
		for (final Player p : Bukkit.getOnlinePlayers())
		{
			final Player player = p;
			final PseudoPlayer pPlayer = pm.getPlayer(player);
			PlayerMapper.updatePlayer(pPlayer);
			pm.getPlayers().remove(pPlayer);
		}
		MagicStructure.removeAll();
		CustomSchedule.stopSchedule();
		DataSource.getInstance().closeConnection();
	}

	@Override
	public void onEnable() {
		ItemUtils.addChainMail();
		Bukkit.setIdleTimeout(15);
		this.saveDefaultConfig();

		ConfigManager.getManager().setConfig(this);

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
		new KeyPlayerListener(this);
		new CratePlayerListener(this);
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

		setMysqlError(!Database.testDatabaseConnection());
		
		PermanentGateMapper.getPermanentGates();
		ClanMapper.getClans();
		PlotMapper.getPlots();
		ChestRefillMapper.getChests();
		
		Locations.LAWFULL.getLocation().getWorld().setSpawnLocation((int)Locations.LAWFULL.getLocation().getX(), (int)Locations.LAWFULL.getLocation().getY(), (int)Locations.LAWFULL.getLocation().getZ());
		
		CrateManager.getManager().createCrates();
		
		// GameLoop should run last.
		CustomSchedule.Schedule();
		gameLoop = new GameLoop(this).runTaskTimer(this, 0L, 2L);
		asyncGameLoop = new AsyncGameLoop().runTaskTimerAsynchronously(this, 0L, 100L);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			final PseudoPlayer pPlayer = pm.getPlayer(p);
			pm.getPlayers().add(pPlayer);
			pPlayer.setScoreboard(new PseudoScoreboard(p.getUniqueId()));
			p.setDisplayName(Utils.getDisplayName(p)+ChatColor.RESET);
		}
	}
	
	public static int getMaxPlayers() {
		return maxPlayers;
	}

	public BukkitTask getAsyncGameLoop() {
		return asyncGameLoop;
	}

	public void setAsyncGameLoop(BukkitTask asyncGameLoop) {
		this.asyncGameLoop = asyncGameLoop;
	}
}
