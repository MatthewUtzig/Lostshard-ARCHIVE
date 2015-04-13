package com.lostshard.lostshard.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.ChestRefillManager;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Bank;
import com.lostshard.lostshard.Objects.ChatChannel;
import com.lostshard.lostshard.Objects.ChestRefill;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Objects.Plot.PlotCapturePoint;
import com.lostshard.lostshard.Objects.Plot.PlotUpgrade;
import com.lostshard.lostshard.Objects.Store.Store;
import com.lostshard.lostshard.Skills.Build;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Structures.PermanentGate;
import com.lostshard.lostshard.Utils.Serializer;
import com.lostshard.lostshard.Database.DataSource;

public class Database {
	
	protected static DataSource connPool = DataSource.getInstance();

	static PlayerManager pm = PlayerManager.getManager();
	static PlotManager ptm = PlotManager.getManager();
	static ClanManager cm = ClanManager.getManager();
	static ChestRefillManager crm = ChestRefillManager.getManager();
	// Plot
	
	public static boolean testDatabaseConnection() {
		try {
			Connection conn = connPool.getConnection();
			conn.close();
			Lostshard.log.warning("CONNECTION!");
			return true;
		} catch (Exception e) {
			Lostshard.log.warning("NO CONNECTION!");
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		return false;
	}
	
	public static void test() {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM test");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			prep.close();
			conn.close();
			while (rs.next()) {
				String name = rs.getString("test");
				Bukkit.broadcastMessage(name);
			}
		} catch (Exception e) {
			Lostshard.log.warning("[Test] Test mysql error >> " + e.toString());
		}
	}
	
	public static void updateScrollOwner(Scroll scroll, int tID, int pID) {
		Lostshard.log.finest("GETTING SCROLLS!");
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("UPDATE scrolls SET playerid=? WHERE playerid=?;");
			prep.setInt(1, tID);
			prep.setInt(1, pID);
			prep.executeUpdate();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[SCROLL] getScrolls mysql error");
			e.printStackTrace();
		}
	}
	
	public static List<Scroll> getScrolls(int playerID) {
		Lostshard.log.finest("GETTING SCROLLS!");
		List<Scroll> scrolls = new ArrayList<Scroll>();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT scroll FROM scrolls WHERE playerid=?;");
			prep.setInt(1, playerID);
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				try {
					String scroll = rs.getString("scroll");
					
					scrolls.add(Scroll.getByString(scroll));
					
				} catch (Exception e) {
					Lostshard.log.log(Level.WARNING,
							"[SCROLL] Exception when generating \"" + playerID
									+ "\" scroll: ");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[SCROLL] getScrolls mysql error");
			e.printStackTrace();
		}
		return scrolls;
	}
	
	public static void insertScroll(Scroll scroll, int playerID) {
		if(Lostshard.isDebug())
			System.out.print("INSERT SCROLL!");
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO scrolls "
							+ "(scroll,playerid) VALUES (?,?)");
			prep.setString(1, scroll.name());
			prep.setInt(2, playerID);
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[SCROLL] deleteScroll mysql error");
			e.printStackTrace();
		}
	}
	
	public static void deleteScroll(Scroll scroll, int playerID) {
		if(Lostshard.isDebug())
			System.out.print("INSERT SCROLL!");
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM scrolls WHERE playerid=? AND scroll=? LIMIT 1;", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setInt(1, playerID);
			prep.setString(2, scroll.name());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[SCROLL] deleteScroll mysql error");
			e.printStackTrace();
		}
	}
	
	public static void getPlots() {
		System.out.print("GETTING PLOTS!");
		List<NPC> npcs = getNPCS();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM plots");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				String name = rs.getString("name");
				try {
					String owner = rs.getString("owner");
					int id = rs.getInt("id");
					// Location
					Location location = Serializer.deserializeLocation(rs
							.getString("location"));
					int size = rs.getInt("size");
					int money = rs.getInt("money");
					int salePrice = rs.getInt("salePrice");
					// Toggles
					boolean protection = rs.getBoolean("protection");
					boolean allowExplosions = rs.getBoolean("allowExplosions");
					boolean privatePlot = rs.getBoolean("private");
					boolean friendBuild = rs.getBoolean("friendBuild");
					// Upgrades
					List<String> upgrades = Serializer.deserializeStringArray(rs.getString("upgrades"));
					// Admin stuff
					boolean capturepoint = rs.getBoolean("capturePoint");
					boolean magic = rs.getBoolean("allowMagic");
					boolean pvp = rs.getBoolean("allowPvp");
					boolean title = rs.getBoolean("title");
					// Friend and co-owners and owner
					ArrayList<UUID> friends = (ArrayList<UUID>) Serializer
							.deserializeUUIDList(rs.getString("friends"));
					ArrayList<UUID> coowners = (ArrayList<UUID>) Serializer
							.deserializeUUIDList(rs.getString("coowners"));
					Plot plot;
					if(capturepoint)
						plot = new PlotCapturePoint(id, name, UUID.fromString(owner), location);
					else
						plot = new Plot(id, name, UUID.fromString(owner), location);
					plot.setSize(size);
					plot.setMoney(money);
					plot.setSalePrice(salePrice);
					plot.setProtected(protection);
					plot.setAllowExplosions(allowExplosions);
					plot.setPrivatePlot(privatePlot);
					plot.setFriendBuild(friendBuild);
					plot.setTitleEntrence(title);
					if(upgrades != null)
						for(String s : upgrades)
							plot.addUpgrade(PlotUpgrade.valueOf(s));
					plot.setAllowMagic(magic);
					plot.setAllowPvp(pvp);
					plot.setFriends(friends);
					plot.setCoowners(coowners);
					
					for(NPC npc : npcs)
						if(npc.getPlotId() == plot.getId())
							plot.getNpcs().add(npc);

					ptm.getPlots().add(plot);
				} catch (Exception e) {
					Lostshard.log.log(Level.WARNING,
							"[PLOT] Exception when generating \"" + name
									+ "\" plot: ");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLOT] getPlots mysql error");
			e.printStackTrace();
		}
	}

	public static void updatePlot(Plot plot) {
		if (plot == null)
			return;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("UPDATE plot SET "
							+ "name=?, location=?, size=?, owner=?, money=?, salePrice=?, protection=?, "
							+ "allowExplosions=?, private=?, friendBuild=?, upgrades=?, capturePoint=?, allowMagic=?, allowPvp=?, friends=?, coowners=?, title=? WHERE id=?");

			prep.setString(1, plot.getName());
			prep.setString(2, Serializer.serializeLocation(plot.getLocation()));
			prep.setInt(3, plot.getSize());
			prep.setString(4, plot.getOwner().toString());
			prep.setInt(5, plot.getMoney());
			prep.setInt(6, plot.getSalePrice());
			prep.setBoolean(7, plot.isProtected());
			prep.setBoolean(8, plot.isAllowExplosions());
			prep.setBoolean(9, plot.isPrivatePlot());
			prep.setBoolean(10, plot.isFriendBuild());
			prep.setString(11, plot.upgradesToJson());
			prep.setBoolean(12, plot instanceof PlotCapturePoint);
			prep.setBoolean(13, plot.isAllowMagic());
			prep.setBoolean(14, plot.isAllowPvp());
			prep.setString(15, Serializer.serializeUUIDList(plot.getFriends()));
			prep.setString(16, Serializer.serializeUUIDList(plot.getCoowners()));
			prep.setBoolean(17, plot.isTitleEntrence());
			prep.setInt(18, plot.getId());

			prep.executeUpdate();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLOT] updatePlot mysql error >> " + e.toString());
		}
	}
	
	public static void updatePlots(List<Plot> plots) {
		if(Lostshard.isDebug())
			System.out.print("UPDATING PLOTS!");
		List<NPC> npcs = new ArrayList<NPC>();
		try {
			Connection conn = connPool.getConnection();
			
			PreparedStatement prep = conn.prepareStatement("UPDATE plots SET "
					+ "name=?, location=?, size=?, owner=?, money=?, salePrice=?, protection=?, "
					+ "allowExplosions=?, private=?, friendBuild=?, upgrades=?, capturePoint=?, allowMagic=?, allowPvp=?, friends=?, coowners=?, title=? WHERE id=?; ");
			for(Plot plot : plots) {
				plot.setUpdate(false);
				prep.setString(1, plot.getName());
				prep.setString(2, Serializer.serializeLocation(plot.getLocation()));
				prep.setInt(3, plot.getSize());
				prep.setString(4, plot.getOwner().toString());
				prep.setInt(5, plot.getMoney());
				prep.setInt(6, plot.getSalePrice());
				prep.setBoolean(7, plot.isProtected());
				prep.setBoolean(8, plot.isAllowExplosions());
				prep.setBoolean(9, plot.isPrivatePlot());
				prep.setBoolean(10, plot.isFriendBuild());
				prep.setString(11, plot.upgradesToJson());
				prep.setBoolean(12, plot instanceof PlotCapturePoint);
				prep.setBoolean(13, plot.isAllowMagic());
				prep.setBoolean(14, plot.isAllowPvp());
				prep.setString(15, Serializer.serializeUUIDList(plot.getFriends()));
				prep.setString(16, Serializer.serializeUUIDList(plot.getCoowners()));
				prep.setBoolean(17, plot.isTitleEntrence());
				prep.setInt(18, plot.getId());
				prep.addBatch();
				npcs.addAll(plot.getNpcs());
			}
			prep.executeBatch();
			prep.close();
			conn.close();
			updateNPCS(npcs);
		} catch (Exception e) {
			Lostshard.log.warning("[Plot] updatePlots mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void insertPlot(Plot plot) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO plots "
							+ "(name,location,size,owner,money,salePrice,protection,allowExplosions,"
							+ "private,friendBuild,upgrades,"
							+ "capturePoint,allowMagic,allowPvp,friends,coowners) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setString(1, plot.getName());
			prep.setString(2, Serializer.serializeLocation(plot.getLocation()));
			prep.setInt(3, plot.getSize());
			prep.setString(4, plot.getOwner().toString());
			prep.setInt(5, plot.getMoney());
			prep.setInt(6, plot.getSalePrice());
			prep.setBoolean(7, plot.isProtected());
			prep.setBoolean(8, plot.isAllowExplosions());
			prep.setBoolean(9, plot.isPrivatePlot());
			prep.setBoolean(10, plot.isFriendBuild());
			prep.setString(11, plot.upgradesToJson());
			prep.setBoolean(12, plot instanceof PlotCapturePoint);
			prep.setBoolean(13, plot.isAllowMagic());
			prep.setBoolean(14, plot.isAllowPvp());
			prep.setString(15, Serializer.serializeUUIDList(plot.getFriends()));
			prep.setString(16, Serializer.serializeUUIDList(plot.getCoowners()));
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			plot.setId(id);
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PLOT] insertPlot mysql error");
			e.printStackTrace();
		}
	}
	
	public static void deletePlot(Plot plot) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM plots WHERE id=?;");
			prep.setInt(1, plot.getId());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PLOT] deletePlot mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void updateNPC(NPC npc) {
		if (npc == null)
			return;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE npcs SET "
					+ "name=? location=?, type=?, plotId=? WHERE id=?");

			prep.setString(1, npc.getName());
			prep.setString(2, Serializer.serializeLocation(npc.getLocation()));
			prep.setString(3, npc.getType().toString());
			prep.setInt(4, npc.getPlotId());
			prep.setInt(5, npc.getId());

			prep.executeUpdate();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[NPC] updateNPC mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static List<Build> getBuilds(int playerID) {
		List<Build> builds = new ArrayList<Build>();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM builds WHERE playerid=?");
			prep.setInt(1, playerID);
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					int mining = rs.getInt("mining");
					boolean miningLock = rs.getBoolean("miningLock");
					int magery = rs.getInt("magery");
					boolean mageryLock = rs.getBoolean("mageryLock");
					int blades = rs.getInt("blades");
					boolean bladesLock = rs.getBoolean("bladesLock");
					int brawling = rs.getInt("brawling");
					boolean brawlingLock = rs.getBoolean("brawlingLock");
					int blacksmithy = rs.getInt("blacksmithy");
					boolean blacksmithyLock = rs.getBoolean("blacksmithyLock");
					int lumberjacking = rs.getInt("lumberjacking");
					boolean lumberjackingLock = rs.getBoolean("lumberjackingLock");
					int fishing = rs.getInt("fishing");
					boolean fishingLock = rs.getBoolean("fishingLock");
					int survivalism = rs.getInt("survivalism");
					boolean survivalismLock = rs.getBoolean("survivalismLock");
					int taming = rs.getInt("taming");
					boolean tamingLock = rs.getBoolean("tamingLock");
					int archery = rs.getInt("archery");
					boolean archeryLock = rs.getBoolean("archeryLock");
					
					
					Build build = new Build();
					build.setId(id);
					build.getMining().setLvl(mining);
					build.getMining().setLocked(miningLock);
					build.getMagery().setLvl(magery);
					build.getMagery().setLocked(mageryLock);
					build.getBlades().setLvl(blades);
					build.getBlades().setLocked(bladesLock);
					build.getBrawling().setLvl(brawling);
					build.getBrawling().setLocked(brawlingLock);
					build.getBlackSmithy().setLvl(blacksmithy);
					build.getBlackSmithy().setLocked(blacksmithyLock);
					build.getLumberjacking().setLvl(lumberjacking);
					build.getLumberjacking().setLocked(lumberjackingLock);
					build.getFishing().setLvl(fishing);
					build.getFishing().setLocked(fishingLock);
					build.getSurvivalism().setLvl(survivalism);
					build.getSurvivalism().setLocked(survivalismLock);
					build.getTaming().setLvl(taming);
					build.getTaming().setLocked(tamingLock);
					build.getArchery().setLvl(archery);
					build.getArchery().setLocked(archeryLock);
					
					builds.add(build);
					
				} catch (Exception e) {
					Lostshard.log.warning("[BUILD] Exception when generating \""+ id + "\" BUILD:");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[BUILD] getBuilds mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		Lostshard.log.finest("[BUILD] got "+builds.size()+" build from DB.");
		return builds;
	}
	
	public static void updateBuilds(List<Build> builds) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE builds SET mining=?, miningLock=?, magery=?, mageryLock=?, blades=?, bladesLock=?, brawling=?, brawlingLock=?, blacksmithy=?, blacksmithyLock=?, lumberjacking=?, lumberjackingLock=?, fishing=?, fishingLock=?, survivalism=?, survivalismLock=?, taming=?, tamingLock=?, archery=?, archeryLock=? WHERE id=?;");
			for(Build build : builds) {
				prep.setInt(1, build.getMining().getLvl());
				prep.setBoolean(2, build.getMining().isLocked());
				prep.setInt(3, build.getMagery().getLvl());
				prep.setBoolean(4, build.getMagery().isLocked());
				prep.setInt(5, build.getBlades().getLvl());
				prep.setBoolean(6, build.getBlades().isLocked());
				prep.setInt(7, build.getBrawling().getLvl());
				prep.setBoolean(8, build.getBrawling().isLocked());
				prep.setInt(9, build.getBlackSmithy().getLvl());
				prep.setBoolean(10, build.getBlackSmithy().isLocked());
				prep.setInt(11, build.getLumberjacking().getLvl());
				prep.setBoolean(12, build.getLumberjacking().isLocked());
				prep.setInt(13, build.getFishing().getLvl());
				prep.setBoolean(14, build.getFishing().isLocked());
				prep.setInt(15, build.getSurvivalism().getLvl());
				prep.setBoolean(16, build.getSurvivalism().isLocked());
				prep.setInt(17, build.getTaming().getLvl());
				prep.setBoolean(18, build.getTaming().isLocked());
				prep.setInt(19, build.getArchery().getLvl());
				prep.setBoolean(20, build.getArchery().isLocked());
				prep.setInt(21, build.getId());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[BUILD] updateBuilds mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void insertBuild(Build build, int playerID) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("INSERT IGNORE INTO builds (playerid) VALUES (?);", PreparedStatement.RETURN_GENERATED_KEYS);
				prep.setInt(1, playerID);
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			build.setId(id);
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[BUILD] insertBuild mysql error");
			e.printStackTrace();
		}
	}


	// Player
	public static PseudoPlayer getPlayer(UUID uuid) {
		Lostshard.log.finest("[PLAYER] Getting Player from DB!");
		PseudoPlayer pPlayer = null;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM players WHERE uuid=?");
			prep.setString(1, uuid.toString());
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					int money = rs.getInt("money");
					int murderCounts = rs.getInt("murderCounts");
					int criminalTick = rs.getInt("criminalTicks");
					boolean globalChat = rs.getBoolean("globalChat");
					boolean privateChat = rs.getBoolean("privateChat");
					int subscriberDays = rs.getInt("subscribeDays");
					boolean wasSubscribed = rs.getBoolean("wasSubscribed");
					Bank bank = new Bank(rs.getString("bank"), wasSubscribed);
					int plotCreationPoints = rs.getInt("plotCreationPoints");
					String chatChannel = rs.getString("chatChannel");
					int mana = rs.getInt("mana");
					int stamina = rs.getInt("stamina");
					int rank = rs.getInt("rank");
					int spawnTick = rs.getInt("spawnTick");
					int currentBuild = rs.getInt("currentBuild");
					List<String> titles = Serializer.deserializeStringArray(rs.getString("titles"));
					int currentTitle = rs.getInt("currentTitle");
					int freeSkillPoints = rs.getInt("freeSkillPoints");
					List<String> spellbook = Serializer.deserializeStringArray(rs.getString("spellbook"));
					boolean gui = rs.getBoolean("gui");
					List<UUID> ignored = Serializer.deserializeUUIDList(rs.getString("ignored"));
					
					pPlayer = new PseudoPlayer(uuid, id);
					
					pPlayer.setMoney(money);
					pPlayer.setMurderCounts(murderCounts);
					pPlayer.setCriminal(criminalTick);
					pPlayer.setGlobalChat(globalChat);
					pPlayer.setPrivateChat(privateChat);
					pPlayer.setSubscribeDays(subscriberDays);
					pPlayer.setWasSubscribed(wasSubscribed);
					pPlayer.setPlotCreatePoints(plotCreationPoints);
					pPlayer.setBank(bank);
					pPlayer.setChatChannel(ChatChannel.valueOf(chatChannel));
					pPlayer.setMana(mana);
					pPlayer.setStamina(stamina);
					pPlayer.setRank(rank);
					pPlayer.getTimer().spawnTicks = spawnTick;
					pPlayer.setCurrentBuildId(currentBuild);
					pPlayer.setTitels(titles);
					pPlayer.setCurrentTitleId(currentTitle);
					pPlayer.setFreeSkillPoints(freeSkillPoints);
					pPlayer.setAllowGui(gui);
					pPlayer.setIgnored(ignored);
					if(spellbook != null)
						for(String s : spellbook)
							pPlayer.getSpellbook().addSpell(Scroll.getByString(s));
					pPlayer.setBuilds(Database.getBuilds(id));
					pPlayer.setRunebook(Database.getRunebook(id));
					pPlayer.setScrools(Database.getScrolls(id));
				} catch (Exception e) {
					Lostshard.log.log(Level.WARNING,
							"[PLAYER] Exception when generating \""
									+ id
									+ "\" player: ");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PLAYER] getPlayers mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		if(pPlayer != null)
			Lostshard.log.finest("[PLAYER] got "+Bukkit.getOfflinePlayer(uuid).getName()+" players from DB.");
		return pPlayer;
	}

	public static PseudoPlayer insertPlayer(PseudoPlayer pPlayer) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO players "
							+ "(uuid,bank,titles,spellbook,ignored) VALUES (?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setString(1, pPlayer.getPlayerUUID().toString());
			prep.setString(2, pPlayer.getBank().Serialize());
			prep.setString(3, Serializer.serializeStringArray(pPlayer.getTitels()));
			prep.setString(4, pPlayer.getSpellbook().toJson());
			prep.setString(5, Serializer.serializeUUIDList(pPlayer.getIgnored()));
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			pPlayer.setId(id);
			prep.close();
			conn.close();
			insertBuild(pPlayer.getCurrentBuild(), pPlayer.getId());
		} catch (Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLAYER] insertPlayer mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		return pPlayer;
	}
	
	public static void updatePlayers(List<PseudoPlayer> pPlayers) {
		if(Lostshard.isDebug())
			System.out.print("UPDATING PLAYERS!");
		List<Build> builds = new ArrayList<Build>();
		try {
			Connection conn = connPool.getConnection();
			
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET money=?, bank=?, murderCounts=?, criminalTicks=?, globalChat=?, privateChat=?, subscribeDays=?, wasSubscribed=?, plotCreationPoints=?, chatChannel=?, mana=?, stamina=?, rank=?, spawnTick=?, currentBuild=?, titles=?, currentTitle=?, freeSkillPoints=?, spellbook=?, private=?, gui=?, ignored=? WHERE id=?; ");
			for(PseudoPlayer pPlayer : pPlayers) {
				pPlayer.setUpdate(false);
				prep.setInt(1, pPlayer.getMoney());
				prep.setString(2, pPlayer.getBank().Serialize());
				prep.setInt(3, pPlayer.getMurderCounts());
				prep.setInt(4, pPlayer.getCriminal());
				prep.setBoolean(5, pPlayer.isGlobalChat());
				prep.setBoolean(6, pPlayer.isPrivateChat());
				prep.setInt(7, pPlayer.getSubscribeDays());
				prep.setBoolean(8, pPlayer.wasSubscribed());
				prep.setInt(9, pPlayer.getPlotCreatePoints());
				prep.setString(10, pPlayer.getChatChannel().toString());
				prep.setInt(11, pPlayer.getMana());
				prep.setInt(12, pPlayer.getStamina());
				prep.setInt(13, pPlayer.getRank());
				prep.setInt(14, pPlayer.getTimer().spawnTicks);
				prep.setInt(15, pPlayer.getCurrentBuildId());
				prep.setString(16, Serializer.serializeStringArray(pPlayer.getTitels()));
				prep.setInt(17, pPlayer.getCurrentTitleId());
				prep.setInt(18, pPlayer.getFreeSkillPoints());
				prep.setString(19, pPlayer.getSpellbook().toJson());
				prep.setBoolean(20, pPlayer.isPrivate());
				prep.setBoolean(21, pPlayer.isAllowGui());
				prep.setInt(22, pPlayer.getId());
				prep.setString(23, Serializer.serializeUUIDList(pPlayer.getIgnored()));
				prep.addBatch();
				builds.addAll(pPlayer.getBuilds());
			}
			prep.executeBatch();
			prep.close();
			conn.close();
			updateBuilds(builds);
		} catch (Exception e) {
			Lostshard.log.warning("[PLAYER] updatePlayers mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static List<NPC> getNPCS() {
		List<NPC> npcs = new ArrayList<NPC>();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM npcs");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					String name = rs.getString("name");
					NPCType type = NPCType.valueOf(rs.getString("type"));
					Location location = Serializer.deserializeLocation(rs.getString("location"));
					int plotId = rs.getInt("plotId");
					
					NPC npc = new NPC(id, type, name, location, plotId);
					npcs.add(npc);
				} catch (Exception e) {
					Lostshard.log.warning("[NPC] Exception when generating \""+ id + "\" npc:");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[NPC] getNPCS mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		System.out.print("[NPC] got "+npcs.size()+" npcs from DB.");
		return npcs;
	}
	
	public static void updateNPCS(List<NPC> npcs) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE npcs SET name=?, type=?, location=?, plotId=? WHERE id=?;");
			for(NPC npc : npcs) {
				prep.setString(1, npc.getName());
				prep.setString(2, npc.getType().toString());
				prep.setString(3, Serializer.serializeLocation(npc.getLocation()));
				prep.setInt(4, npc.getPlotId());
				prep.setInt(5, npc.getId());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[NPC] updateNPC mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void insertNPC(NPC npc) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO npcs (name,location,type,plotId) VALUES (?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setString(1, npc.getName());
			prep.setString(2, Serializer.serializeLocation(npc.getLocation()));
			prep.setString(3, npc.getType().toString());
			prep.setInt(4, npc.getPlotId());
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			npc.setId(id);
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[NPC] insertNPC mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void deleteNPC(NPC npc) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM npcs WHERE id=?;");
			prep.setInt(1, npc.getId());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[NPC] deleteNPC mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static List<Clan> getClans() {
		List<Clan> clans = new ArrayList<Clan>();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM clans");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					String name = rs.getString("name");
					UUID owner = UUID.fromString(rs.getString("owner"));
					List<UUID> leaders = Serializer.deserializeUUIDList(rs.getString("leaders"));
					List<UUID> members = Serializer.deserializeUUIDList(rs.getString("members"));
					List<UUID> invited = Serializer.deserializeUUIDList(rs.getString("invited"));
					
					Clan clan = new Clan(name, owner);
					clan.setId(id);
					clan.setLeaders(leaders);
					clan.setMembers(members);
					clan.setInvited(invited);
					
					clans.add(clan);
				} catch (Exception e) {
					Lostshard.log.warning("[CLAN] Exception when generating \""+ id + "\" clan:");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[CLAN] getClans mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		System.out.print("[CLAN] got "+clans.size()+" clans from DB.");
		cm.setClans(clans);
		return clans;
	}
	
	public static void updateClans(List<Clan> clans) {
		if(Lostshard.isDebug())
			System.out.print("UPDATING CLANS!");
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE clans SET name=?, owner=?, leaders=?, members=?, invited=? WHERE id=?;");
			for(Clan clan : clans) {
				clan.setUpdate(false);
				prep.setString(1, clan.getName());
				prep.setString(2, clan.getOwner().toString());
				prep.setString(3, Serializer.serializeUUIDList(clan.getLeaders()));
				prep.setString(4, Serializer.serializeUUIDList(clan.getMembers()));
				prep.setString(5, Serializer.serializeUUIDList(clan.getInvited()));
				prep.setInt(6, clan.getId());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[Clan] updateClans mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void insertClan(Clan clan) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO clans (name,owner,leaders,members,invited) VALUES (?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setString(1, clan.getName());
			prep.setString(2, clan.getOwner().toString());
			prep.setString(3, Serializer.serializeUUIDList(clan.getLeaders()));
			prep.setString(4, Serializer.serializeUUIDList(clan.getMembers()));
			prep.setString(5, Serializer.serializeUUIDList(clan.getInvited()));
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			clan.setId(id);
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[CLAN] inserClan mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void deleteClan(Clan clan) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM clans WHERE id=?;");
			prep.setInt(1, clan.getId());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[CLAN] deleteClan mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
//	public static List<Store> getStores() {
//		List<Store> stores = new ArrayList<Store>();
//		try {
//			Connection conn = connPool.getConnection();
//			PreparedStatement prep = conn
//					.prepareStatement("SELECT * FROM stores");
//			prep.execute();
//			ResultSet rs = prep.getResultSet();
//			while (rs.next()) {
//				int id = rs.getInt("id");
//				try {
//					int npcID = rs.getInt("npcId");
//					String content = rs.getString("content");
//					
//					Store store = new Store(npcID);
//					store.setId(id);
//					
//					stores.add(store);
//				} catch (Exception e) {
//					Lostshard.log.warning("[STORE] Exception when generating \""+ id + "\" store:");
//					e.printStackTrace();
//				}
//			}
//			prep.close();
//			conn.close();
//		} catch (Exception e) {
//			Lostshard.log.warning("[STORE] getStores mysql error");
//			Lostshard.mysqlError();
//			if(Lostshard.isDebug())
//				e.printStackTrace();
//		}
//		System.out.print("[STORE] got "+stores.size()+" stores from DB.");
////		Lostshard.getRegistry().setClans(clans);
//		return stores;
//	}
	
	public static void updateStore(Store store) {
		if(Lostshard.isDebug())
			System.out.print("UPDATING STORES!");
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE stores SET npcId=?, content=? WHERE id=?;");
			prep.setInt(1, store.getNpcId());
			prep.setString(2, store.getItemsAsJson());
			prep.setInt(3, store.getId());
			prep.executeUpdate();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[STORE] updateStores mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void insertStore(Store store) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO stores (npcId,content) VALUES (?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setInt(1, store.getNpcId());
			prep.setString(2, "");
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			store.setId(id);
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[STORE] inserStore mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void deleteStore(Store store) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM stores WHERE id=?;");
			prep.setInt(1, store.getId());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[STORE] deleteStore mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void saveAll() {

	}

	public static int insertRune(int playerID, String response, Location markLoc) {
		int id = 0;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO runes (location, label, playerid) VALUES (?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setString(1, Serializer.serializeLocation(markLoc));
			prep.setString(2, response);
			prep.setInt(3, playerID);
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			while (rs.next())
				id = rs.getInt(1);
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[RUNES] inserRune mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		return id;
	}
	
	public static void deleteRune(Rune rune) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM runes WHERE id=?;");
			prep.setInt(1, rune.getId());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[RUNES] deleteRune mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static Runebook getRunebook(int playerID) {
		Runebook runebook = new Runebook();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT label, location, id  FROM runes WHERE playerid=?");
			prep.setInt(1, playerID);
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					String label = rs.getString("label");
					Location loc = Serializer.deserializeLocation(rs.getString("location"));
					Rune rune = new Rune(loc, label, id);
					runebook.addRune(rune);
				} catch (Exception e) {
					Lostshard.log.warning("[RUNES] Exception when generating \""+ id + "\" BUILD:");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[RUNES] getRunes mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		Lostshard.log.finest("[RUNES] got "+runebook.getRunes().size()+" runes from DB.");
		return runebook;
	}

	public static void updateRune(PseudoPlayer targetPlayer, Rune foundRune) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("UPDATE runes SET playerid=? WHERE id=?;");
			prep.setInt(1, targetPlayer.getId());
			prep.setInt(2, foundRune.getId());
			prep.executeUpdate();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[RUNES] updateRune mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		
	}
	
	public static int insertPermanentGate(PermanentGate gate) {
		int id = 0;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO permanentgates (fromLocation, toLocation, creator, direction) VALUES (?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setString(1, Serializer.serializeLocation(gate.getFromBlock().getLocation()));
			prep.setString(2, Serializer.serializeLocation(gate.getToBlock().getLocation()));
			prep.setString(3, gate.getCreatorUUID().toString());
			prep.setBoolean(4, gate.isDirection());
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			while (rs.next())
				id = rs.getInt(1);
			gate.setId(id);
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PERMANENTGATE] inserPermanent mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		return id;
	}
	
	public static void deletePermanentGate(PermanentGate gate) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM permanentgates WHERE id=?;");
			prep.setInt(1, gate.getId());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PERMANENTGATE] deletePermanentGate mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void getPermanentGates() {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM permanentgates");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					Location fromLocation = Serializer.deserializeLocation(rs.getString("fromLocation"));
					Location toLocation = Serializer.deserializeLocation(rs.getString("toLocation"));
					boolean direction = rs.getBoolean("direction");
					UUID uuid = UUID.fromString(rs.getString("creator"));
					ArrayList<Block> blocks = new ArrayList<Block>();
					blocks.add(fromLocation.getBlock());
					blocks.add(toLocation.getBlock());
					blocks.add(fromLocation.getBlock().getRelative(0, 1, 0));
					blocks.add(toLocation.getBlock().getRelative(0, 1, 0));
					new PermanentGate(blocks, uuid, id, direction);
				} catch (Exception e) {
					Lostshard.log.warning("[PERMANENTGATE] Exception when generating \""+ id + "\" gate:");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PERMANENTGATE] getPermanentGates mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static List<String> getOfflineMessages(UUID uuid) {
		List<String> list = new ArrayList<String>();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM offlineMessages WHERE player=?;");
			prep.setString(1, uuid.toString());
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				try {
					String msg = rs.getString("message");
					list.add(msg);
				} catch (Exception e) {
					Lostshard.log.warning("[MESSAGES] Exception when generating message for \""+uuid.toString()+ "\":");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[MESSAGES] getMessages mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
		return list;
	}

	public static void insertMessages(UUID uuid, String msg) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT INTO offlinemessages (player,message) VALUES (?,?);");
			prep.setString(1, uuid.toString());
			prep.setString(2, msg);
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[MESSAGES] insertMessages mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void deleteMessages(UUID uuid) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM offlineMessages WHERE player=?;");
			prep.setString(1, uuid.toString());
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[MESSAGES] deleteMessages mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void insertChest(ChestRefill cr) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT INTO chests (location,items,rangeMin,rangeMax) VALUES (?,?,?,?);");
			prep.setString(1, Serializer.serializeLocation(cr.getLocation()));
			prep.setString(2, Serializer.serializeItems(cr.getItems()));
			prep.setLong(3, cr.getRangeMin()/60000);
			prep.setLong(4, cr.getRangeMax()/60000);
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[CHESTREFILL] insertChest mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void deleteChest(ChestRefill cr) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("DELETE FROM chests WHERE location=?;");
			prep.setString(1, Serializer.serializeLocation(cr.getLocation()));
			prep.execute();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[CHESTREFILL] deleteChest mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void getChests() {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM chests;");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					Location location = Serializer.deserializeLocation(rs.getString("location"));
					long rangeMin = rs.getLong("rangeMin");
					long rangeMax = rs.getLong("rangeMax");
					ItemStack[] items = Serializer.deserializeItems(rs.getString("items"));
					crm.getChests().add(new ChestRefill(location, rangeMin*60000, rangeMax*60000, items));
				} catch (Exception e) {
					Lostshard.log.warning("[CHESTREFILL] Exception when generating message for \""+id+ "\":");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[CHESTREFILL] getMessages mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}
}
