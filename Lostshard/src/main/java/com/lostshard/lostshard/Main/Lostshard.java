package com.lostshard.lostshard.Main;

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
import com.lostshard.lostshard.Data.Locations;
import com.lostshard.lostshard.Database.DataSource;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Database.Mappers.ChestRefillMapper;
import com.lostshard.lostshard.Database.Mappers.ClanMapper;
import com.lostshard.lostshard.Database.Mappers.PermanentGateMapper;
import com.lostshard.lostshard.Database.Mappers.PlayerMapper;
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
import com.lostshard.lostshard.NPC.NPCLib.NPCLibManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.PseudoScoreboard;
import com.lostshard.lostshard.Spells.MagicStructure;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Utils;

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
		new CitizensLisenter(this);
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
