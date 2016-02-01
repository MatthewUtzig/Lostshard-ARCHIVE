package com.lostshard.Lostshard.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
import com.lostshard.Lostshard.Database.Hibernate;
import com.lostshard.Lostshard.Listener.BlockListener;
import com.lostshard.Lostshard.Listener.EntityListener;
import com.lostshard.Lostshard.Listener.PlayerListener;
import com.lostshard.Lostshard.Listener.ServerListener;
import com.lostshard.Lostshard.Listener.VehicleListener;
import com.lostshard.Lostshard.Listener.VoteListener;
import com.lostshard.Lostshard.Listener.WorldListener;
import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Manager.ClanManager;
import com.lostshard.Lostshard.Manager.ConfigManager;
import com.lostshard.Lostshard.Manager.NPCManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPCLib.NPCLibManager;
import com.lostshard.Lostshard.Objects.ChestRefill;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.PseudoScoreboard;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Spells.MagicStructure;
import com.lostshard.Lostshard.Spells.Structures.PermanentGate;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Utils;

import net.citizensnpcs.api.npc.NPC;

/**
 * @author Jacob Rosborg
 *
 */
public class Lostshard extends JavaPlugin {

	private static final int maxPlayers = 30;
	private static Hibernate hibernate;

	public static Logger log;
	private static Plugin plugin;

	private static boolean mysqlError = false;

	private static boolean debug = true;

	public static int getMaxPlayers() {
		return maxPlayers;
	}

	public static Session getSession() {
		return hibernate.getSession();
	}

	public static boolean isDebug() {
		return Lostshard.debug;
	}

	public static boolean isVanished(Player player) {
		return false;
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

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();

	ClanManager cm = ClanManager.getManager();

	private BukkitTask gameLoop;

	private BukkitTask asyncGameLoop;

	public BukkitTask getAsyncGameLoop() {
		return this.asyncGameLoop;
	}

	public BukkitTask getGameLoop() {
		return this.gameLoop;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public String getVersion() {
		return "1.0";
	}

	public boolean isMysqlError() {
		return mysqlError;
	}

	@SuppressWarnings("unchecked")
	private void loadFromDB() {
		final Session s = getSession();
		try {
			Transaction t = s.beginTransaction();
			try {
				t.begin();
				ClanManager.getManager().setClans(s.createCriteria(Clan.class).list());
				t.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				t = s.beginTransaction();
				t.begin();
				PlotManager.getManager().setPlots(s.createCriteria(Plot.class).list());
				t.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				t = s.beginTransaction();
				t.begin();
				ChestRefillManager.getManager().setChests(s.createCriteria(ChestRefill.class).list());
				t.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				t = s.beginTransaction();
				t.begin();
				MagicStructure.getMagicStructures().addAll(s.createCriteria(PermanentGate.class).list());
				t.commit();
				Bukkit.broadcastMessage(""+MagicStructure.magicstructures.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
			s.close();
			loadPermanentGates();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
	}
	
	@SuppressWarnings("unused")
	public void loadPermanentGates() {
		final Session s = getSession();
		try {
			String hql = "SELECT * FROM PermanentGate";
			SQLQuery query = s.createSQLQuery(hql);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			Map<String,Object> row = null;
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> data = query.list();
			for(Map<String, Object> map : data) {
				Location from = new Location(Bukkit.getWorld((String) map.get("from_world")), 
						(Double) map.get("from_x"), (Double) map.get("from_y"), (Double) map.get("from_z"), 
						(Float) map.get("from_yaw"), (Float) map.get("from_pitch"));
				
				Location to = new Location(Bukkit.getWorld((String) map.get("to_world")), 
						(Double) map.get("to_x"), (Double) map.get("to_y"), (Double) map.get("to_z"), 
						(Float) map.get("to_yaw"), (Float) map.get("to_pitch"));
				
				boolean direction = (Boolean) map.get("direction");
				
				UUID uuid = UUID.fromString((String) map.get("creatorUUID"));
				
				int id = (Integer) map.get("id");
				
				final Block destBlock = to.getBlock();
				final Block srcBlock = from.getBlock();
				final Block extraDestBlock = to.getBlock().getRelative(0, 1, 0);
				final Block extraSrcBlock = from.getBlock().getRelative(0, 1, 0);
				
				final ArrayList<Block> blocks = new ArrayList<Block>();
				blocks.add(srcBlock);
				blocks.add(destBlock);
				blocks.add(extraSrcBlock);
				blocks.add(extraDestBlock);
				
				new PermanentGate(id, blocks, uuid, direction);
			}
		} catch (Exception e) {
			e.printStackTrace();
			s.close();
		}
		s.close();
	}

	@Override
	public void onDisable() {
		final NPCLibManager npcLibManager = NPCLibManager.getManager();
		if (npcLibManager != null && npcLibManager.getRegistry() != null) {
			for (final NPC npc : npcLibManager.getRegistry())
				npc.despawn();
			npcLibManager.getRegistry().deregisterAll();
		}
		final Session s = Lostshard.getSession();
		try {
			final Transaction t = s.beginTransaction();
			t.begin();
			for (final PseudoPlayer p : this.pm.getPlayers())
				s.update(p);
			for (final Plot p : this.ptm.getPlots())
				s.update(p);
			for (final Clan c : this.cm.getClans())
				s.update(c);
			t.commit();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
			s.close();
		}
		MagicStructure.removeAll();
		CustomSchedule.stopSchedule();
	}

	@Override
	public void onEnable() {
		ItemUtils.addChainMail();
		Bukkit.setIdleTimeout(15);
		Bukkit.setSpawnRadius(0);
		Bukkit.setDefaultGameMode(GameMode.SURVIVAL);
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

		hibernate = new Hibernate();

		this.loadFromDB();

		Locations.LAWFULL.getLocation().getWorld().setSpawnLocation((int) Locations.LAWFULL.getLocation().getX(),
				(int) Locations.LAWFULL.getLocation().getY(), (int) Locations.LAWFULL.getLocation().getZ());

		CrateManager.getManager().createCrates();

		// GameLoop should run last.
		CustomSchedule.Schedule();
		this.gameLoop = new GameLoop(this).runTaskTimer(this, 0L, 2L);
		this.asyncGameLoop = new AsyncGameLoop().runTaskTimerAsynchronously(this, 0L, 100L);

		for (final Player p : Bukkit.getOnlinePlayers()) {
			final PseudoPlayer pPlayer = this.pm.getPlayer(p);
			this.pm.getPlayers().add(pPlayer);
			pPlayer.setScoreboard(new PseudoScoreboard(p.getUniqueId()));
			p.setDisplayName(Utils.getDisplayName(p) + ChatColor.RESET);
		}

		NPCManager.getManager().spawn();
	}

	public void setAsyncGameLoop(BukkitTask asyncGameLoop) {
		this.asyncGameLoop = asyncGameLoop;
	}

	public void setPlugin(Plugin plugin) {
		Lostshard.plugin = plugin;
	}

	public void shutdown() {
		for (final Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server rebooting.");
	}
}
