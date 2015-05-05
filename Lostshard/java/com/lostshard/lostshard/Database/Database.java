package com.lostshard.lostshard.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

public class Database {

	public static void deleteChest(ChestRefill cr) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM chests WHERE location=?;");
			prep.setString(1, Serializer.serializeLocation(cr.getLocation()));
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CHESTREFILL] deleteChest mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void deleteClan(Clan clan) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM clans WHERE id=?;");
			prep.setInt(1, clan.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CLAN] deleteClan mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void deleteMessages(UUID uuid) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM offlineMessages WHERE player=?;");
			prep.setString(1, uuid.toString());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[MESSAGES] deleteMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void deleteNPC(NPC npc) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM npcs WHERE id=?;");
			prep.setInt(1, npc.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[NPC] deleteNPC mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void deletePermanentGate(PermanentGate gate) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM permanentgates WHERE id=?;");
			prep.setInt(1, gate.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log
					.warning("[PERMANENTGATE] deletePermanentGate mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void deletePlot(Plot plot) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM plots WHERE id=?;");
			prep.setInt(1, plot.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[PLOT] deletePlot mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void deleteRune(Rune rune) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM runes WHERE id=?;");
			prep.setInt(1, rune.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] deleteRune mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void deleteStore(Store store) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM stores WHERE id=?;");
			prep.setInt(1, store.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[STORE] deleteStore mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static List<Build> getBuilds(int playerID) {
		final List<Build> builds = new ArrayList<Build>();
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM builds WHERE player_id=?");
			prep.setInt(1, playerID);
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final int id = rs.getInt("id");
				try {
					final int mining = rs.getInt("mining");
					final boolean miningLock = rs.getBoolean("miningLock");
					final int magery = rs.getInt("magery");
					final boolean mageryLock = rs.getBoolean("mageryLock");
					final int blades = rs.getInt("blades");
					final boolean bladesLock = rs.getBoolean("bladesLock");
					final int brawling = rs.getInt("brawling");
					final boolean brawlingLock = rs.getBoolean("brawlingLock");
					final int blacksmithy = rs.getInt("blacksmithy");
					final boolean blacksmithyLock = rs
							.getBoolean("blacksmithyLock");
					final int lumberjacking = rs.getInt("lumberjacking");
					final boolean lumberjackingLock = rs
							.getBoolean("lumberjackingLock");
					final int fishing = rs.getInt("fishing");
					final boolean fishingLock = rs.getBoolean("fishingLock");
					final int survivalism = rs.getInt("survivalism");
					final boolean survivalismLock = rs
							.getBoolean("survivalismLock");
					final int taming = rs.getInt("taming");
					final boolean tamingLock = rs.getBoolean("tamingLock");
					final int archery = rs.getInt("archery");
					final boolean archeryLock = rs.getBoolean("archeryLock");

					final Build build = new Build();
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

				} catch (final Exception e) {
					Lostshard.log
							.warning("[BUILD] Exception when generating \""
									+ id + "\" BUILD:");
					e.printStackTrace();
				}
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[BUILD] getBuilds mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		Lostshard.log
				.finest("[BUILD] got " + builds.size() + " build from DB.");
		return builds;
	}

	public static void getChests() {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM chests;");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final int id = rs.getInt("id");
				try {
					final Location location = Serializer.deserializeLocation(rs
							.getString("location"));
					final long rangeMin = rs.getLong("rangeMin");
					final long rangeMax = rs.getLong("rangeMax");
					final ItemStack[] items = Serializer.deserializeItems(rs
							.getString("items"));
					crm.getChests().add(
							new ChestRefill(location, rangeMin * 60000,
									rangeMax * 60000, items));
				} catch (final Exception e) {
					Lostshard.log
							.warning("[CHESTREFILL] Exception when generating message for \""
									+ id + "\":");
					e.printStackTrace();
				}
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CHESTREFILL] getMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static List<Clan> getClans() {
		final List<Clan> clans = new ArrayList<Clan>();
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM clans");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final int id = rs.getInt("id");
				try {
					final String name = rs.getString("name");
					final UUID owner = UUID.fromString(rs.getString("owner"));
					final List<UUID> leaders = Serializer
							.deserializeUUIDList(rs.getString("leaders"));
					final List<UUID> members = Serializer
							.deserializeUUIDList(rs.getString("members"));
					final List<UUID> invited = Serializer
							.deserializeUUIDList(rs.getString("invited"));

					final Clan clan = new Clan(name, owner);
					clan.setId(id);
					clan.setLeaders(leaders);
					clan.setMembers(members);
					clan.setInvited(invited);

					clans.add(clan);
				} catch (final Exception e) {
					Lostshard.log.warning("[CLAN] Exception when generating \""
							+ id + "\" clan:");
					e.printStackTrace();
				}
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CLAN] getClans mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		System.out.print("[CLAN] got " + clans.size() + " clans from DB.");
		cm.setClans(clans);
		return clans;
	}

	public static List<NPC> getNPCS() {
		final List<NPC> npcs = new ArrayList<NPC>();
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM npcs");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final int id = rs.getInt("id");
				try {
					final String name = rs.getString("name");
					final NPCType type = NPCType.valueOf(rs.getString("type"));
					final Location location = Serializer.deserializeLocation(rs
							.getString("location"));
					final int plotId = rs.getInt("plotId");

					final NPC npc = new NPC(id, type, name, location, plotId);
					npcs.add(npc);
				} catch (final Exception e) {
					Lostshard.log.warning("[NPC] Exception when generating \""
							+ id + "\" npc:");
					e.printStackTrace();
				}
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[NPC] getNPCS mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		System.out.print("[NPC] got " + npcs.size() + " npcs from DB.");
		return npcs;
	}

	public static List<String> getOfflineMessages(UUID uuid) {
		final List<String> list = new ArrayList<String>();
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM offlineMessages WHERE player=?;");
			prep.setString(1, uuid.toString());
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next())
				try {
					final String msg = rs.getString("message");
					list.add(msg);
				} catch (final Exception e) {
					Lostshard.log
							.warning("[MESSAGES] Exception when generating message for \""
									+ uuid.toString() + "\":");
					e.printStackTrace();
				}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[MESSAGES] getMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return list;
	}

	public static void getPermanentGates() {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM permanentgates");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final int id = rs.getInt("id");
				try {
					final Location fromLocation = Serializer
							.deserializeLocation(rs.getString("fromLocation"));
					final Location toLocation = Serializer
							.deserializeLocation(rs.getString("toLocation"));
					final boolean direction = rs.getBoolean("direction");
					final UUID uuid = UUID.fromString(rs.getString("creator"));
					final ArrayList<Block> blocks = new ArrayList<Block>();
					blocks.add(fromLocation.getBlock());
					blocks.add(toLocation.getBlock());
					blocks.add(fromLocation.getBlock().getRelative(0, 1, 0));
					blocks.add(toLocation.getBlock().getRelative(0, 1, 0));
					new PermanentGate(blocks, uuid, id, direction);
				} catch (final Exception e) {
					Lostshard.log
							.warning("[PERMANENTGATE] Exception when generating \""
									+ id + "\" gate:");
					e.printStackTrace();
				}
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log
					.warning("[PERMANENTGATE] getPermanentGates mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void getPlots() {
		System.out.print("GETTING PLOTS!");
		final List<NPC> npcs = getNPCS();
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM plots");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final String name = rs.getString("name");
				try {
					final String owner = rs.getString("owner");
					final int id = rs.getInt("id");
					// Location
					final Location location = Serializer.deserializeLocation(rs
							.getString("location"));
					final int size = rs.getInt("size");
					final int money = rs.getInt("money");
					final int salePrice = rs.getInt("salePrice");
					// Toggles
					final boolean protection = rs.getBoolean("protection");
					final boolean allowExplosions = rs
							.getBoolean("allowExplosions");
					final boolean privatePlot = rs.getBoolean("private");
					final boolean friendBuild = rs.getBoolean("friendBuild");
					// Upgrades
					final List<String> upgrades = Serializer
							.deserializeStringArray(rs.getString("upgrades"));
					// Admin stuff
					final boolean capturepoint = rs.getBoolean("capturePoint");
					final boolean magic = rs.getBoolean("allowMagic");
					final boolean pvp = rs.getBoolean("allowPvp");
					final boolean title = rs.getBoolean("title");
					// Friend and co-owners and owner
					final ArrayList<UUID> friends = (ArrayList<UUID>) Serializer
							.deserializeUUIDList(rs.getString("friends"));
					final ArrayList<UUID> coowners = (ArrayList<UUID>) Serializer
							.deserializeUUIDList(rs.getString("coowners"));
					Plot plot;
					if (capturepoint)
						plot = new PlotCapturePoint(id, name,
								UUID.fromString(owner), location);
					else
						plot = new Plot(id, name, UUID.fromString(owner),
								location);
					plot.setSize(size);
					plot.setMoney(money);
					plot.setSalePrice(salePrice);
					plot.setProtected(protection);
					plot.setAllowExplosions(allowExplosions);
					plot.setPrivatePlot(privatePlot);
					plot.setFriendBuild(friendBuild);
					plot.setTitleEntrence(title);
					if (upgrades != null)
						for (final String s : upgrades)
							plot.addUpgrade(PlotUpgrade.valueOf(s));
					plot.setAllowMagic(magic);
					plot.setAllowPvp(pvp);
					plot.setFriends(friends);
					plot.setCoowners(coowners);

					for (final NPC npc : npcs)
						if (npc.getPlotId() == plot.getId())
							plot.getNpcs().add(npc);

					ptm.getPlots().add(plot);
				} catch (final Exception e) {
					Lostshard.log.log(Level.WARNING,
							"[PLOT] Exception when generating \"" + name
							+ "\" plot: ");
					e.printStackTrace();
				}
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING, "[PLOT] getPlots mysql error");
			e.printStackTrace();
		}
	}

	public static Runebook getRunebook(int playerID) {
		final Runebook runebook = new Runebook();
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT label, location, id  FROM runes WHERE player_id=?");
			prep.setInt(1, playerID);
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final int id = rs.getInt("id");
				try {
					final String label = rs.getString("label");
					final Location loc = Serializer.deserializeLocation(rs
							.getString("location"));
					final Rune rune = new Rune(loc, label, id);
					runebook.addRune(rune);
				} catch (final Exception e) {
					Lostshard.log
							.warning("[RUNES] Exception when generating \""
									+ id + "\" BUILD:");
					e.printStackTrace();
				}
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] getRunes mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		Lostshard.log.finest("[RUNES] got " + runebook.getRunes().size()
				+ " runes from DB.");
		return runebook;
	}

	public static List<Scroll> getScrolls(int playerID) {
		Lostshard.log.finest("GETTING SCROLLS!");
		final List<Scroll> scrolls = new ArrayList<Scroll>();
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT scroll FROM scrolls WHERE player_id=?;");
			prep.setInt(1, playerID);
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next())
				try {
					final String scroll = rs.getString("scroll");

					scrolls.add(Scroll.getByString(scroll));

				} catch (final Exception e) {
					Lostshard.log.log(Level.WARNING,
							"[SCROLL] Exception when generating \"" + playerID
							+ "\" scroll: ");
					e.printStackTrace();
				}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING, "[SCROLL] getScrolls mysql error");
			e.printStackTrace();
		}
		return scrolls;
	}

	public static void insertBuild(Build build, int playerID) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn.prepareStatement(
					"INSERT IGNORE INTO builds (player_id) VALUES (?);",
					Statement.RETURN_GENERATED_KEYS);
			prep.setInt(1, playerID);
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			build.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[BUILD] insertBuild mysql error");
			e.printStackTrace();
		}
	}

	public static void insertChest(ChestRefill cr) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("INSERT INTO chests (location,items,rangeMin,rangeMax) VALUES (?,?,?,?);");
			prep.setString(1, Serializer.serializeLocation(cr.getLocation()));
			prep.setString(2, Serializer.serializeItems(cr.getItems()));
			prep.setLong(3, cr.getRangeMin() / 60000);
			prep.setLong(4, cr.getRangeMax() / 60000);
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CHESTREFILL] insertChest mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void insertClan(Clan clan) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO clans (name,owner,leaders,members,invited) VALUES (?,?,?,?,?);",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, clan.getName());
			prep.setString(2, clan.getOwner().toString());
			prep.setString(3, Serializer.serializeUUIDList(clan.getLeaders()));
			prep.setString(4, Serializer.serializeUUIDList(clan.getMembers()));
			prep.setString(5, Serializer.serializeUUIDList(clan.getInvited()));
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			clan.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CLAN] inserClan mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void insertMessages(UUID uuid, String msg) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("INSERT INTO offlinemessages (player,message) VALUES (?,?);");
			prep.setString(1, uuid.toString());
			prep.setString(2, msg);
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[MESSAGES] insertMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void insertNPC(NPC npc) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO npcs (name,location,type,plot_id) VALUES (?,?,?,?);",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, npc.getName());
			prep.setString(2, Serializer.serializeLocation(npc.getLocation()));
			prep.setString(3, npc.getType().toString());
			prep.setInt(4, npc.getPlotId());
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			npc.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[NPC] insertNPC mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static int insertPermanentGate(PermanentGate gate) {
		int id = 0;
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO permanentgates (fromLocation, toLocation, creator, direction) VALUES (?,?,?,?);",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, Serializer.serializeLocation(gate.getFromBlock()
					.getLocation()));
			prep.setString(2, Serializer.serializeLocation(gate.getToBlock()
					.getLocation()));
			prep.setString(3, gate.getCreatorUUID().toString());
			prep.setBoolean(4, gate.isDirection());
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			while (rs.next())
				id = rs.getInt(1);
			gate.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[PERMANENTGATE] inserPermanent mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return id;
	}

	public static void insertPlot(Plot plot) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO plots "
									+ "(name,location,size,owner,money,salePrice,protection,allowExplosions,"
									+ "private,friendBuild,upgrades,"
									+ "capturePoint,allowMagic,allowPvp,friends,coowners) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
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
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			plot.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[PLOT] insertPlot mysql error");
			e.printStackTrace();
		}
	}

	public static int insertRune(int playerID, String response, Location markLoc) {
		int id = 0;
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO runes (location, label, player_id) VALUES (?,?,?);",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, Serializer.serializeLocation(markLoc));
			prep.setString(2, response);
			prep.setInt(3, playerID);
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			while (rs.next())
				id = rs.getInt(1);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] inserRune mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return id;
	}

	public static void insertStore(Store store) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn.prepareStatement(
					"INSERT IGNORE INTO stores (npcId,content) VALUES (?,?);",
					Statement.RETURN_GENERATED_KEYS);
			prep.setInt(1, store.getNpcId());
			prep.setString(2, "");
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			store.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[STORE] inserStore mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void saveAll() {

	}

	// public static List<Store> getStores() {
	// List<Store> stores = new ArrayList<Store>();
	// try {
	// Connection conn = connPool.getConnection();
	// PreparedStatement prep = conn
	// .prepareStatement("SELECT * FROM stores");
	// prep.execute();
	// ResultSet rs = prep.getResultSet();
	// while (rs.next()) {
	// int id = rs.getInt("id");
	// try {
	// int npcID = rs.getInt("npcId");
	// String content = rs.getString("content");
	//
	// Store store = new Store(npcID);
	// store.setId(id);
	//
	// stores.add(store);
	// } catch (Exception e) {
	// Lostshard.log.warning("[STORE] Exception when generating \""+ id +
	// "\" store:");
	// e.printStackTrace();
	// }
	// }
	// prep.close();
	// conn.close();
	// } catch (Exception e) {
	// Lostshard.log.warning("[STORE] getStores mysql error");
	// Lostshard.mysqlError();
	// if(Lostshard.isDebug())
	// e.printStackTrace();
	// }
	// System.out.print("[STORE] got "+stores.size()+" stores from DB.");
	// // Lostshard.getRegistry().setClans(clans);
	// return stores;
	// }

	public static void test() {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM test");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			conn.close();
			while (rs.next()) {
				final String name = rs.getString("test");
				Bukkit.broadcastMessage(name);
			}
		} catch (final Exception e) {
			Lostshard.log.warning("[Test] Test mysql error >> " + e.toString());
		}
	}

	public static boolean testDatabaseConnection() {
		try {
			final Connection conn = connPool.getConnection();
			conn.close();
			Lostshard.log.warning("CONNECTION!");
			return true;
		} catch (final Exception e) {
			Lostshard.log.warning("NO CONNECTION!");
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return false;
	}

	public static void updateBuilds(List<Build> builds) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE builds SET mining=?, miningLock=?, magery=?, mageryLock=?, blades=?, bladesLock=?, brawling=?, brawlingLock=?, blacksmithy=?, blacksmithyLock=?, lumberjacking=?, lumberjackingLock=?, fishing=?, fishingLock=?, survivalism=?, survivalismLock=?, taming=?, tamingLock=?, archery=?, archeryLock=? WHERE id=?;");
			for (final Build build : builds) {
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
		} catch (final Exception e) {
			Lostshard.log.warning("[BUILD] updateBuilds mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void updateClans(List<Clan> clans) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING CLANS!");
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE clans SET name=?, owner=?, leaders=?, members=?, invited=? WHERE id=?;");
			for (final Clan clan : clans) {
				clan.setUpdate(false);
				prep.setString(1, clan.getName());
				prep.setString(2, clan.getOwner().toString());
				prep.setString(3,
						Serializer.serializeUUIDList(clan.getLeaders()));
				prep.setString(4,
						Serializer.serializeUUIDList(clan.getMembers()));
				prep.setString(5,
						Serializer.serializeUUIDList(clan.getInvited()));
				prep.setInt(6, clan.getId());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[Clan] updateClans mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void updateNPC(NPC npc) {
		if (npc == null)
			return;
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE npcs SET "
							+ "name=? location=?, type=?, plot_id=? WHERE id=?");

			prep.setString(1, npc.getName());
			prep.setString(2, Serializer.serializeLocation(npc.getLocation()));
			prep.setString(3, npc.getType().toString());
			prep.setInt(4, npc.getPlotId());
			prep.setInt(5, npc.getId());

			prep.executeUpdate();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[NPC] updateNPC mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void updateNPCS(List<NPC> npcs) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE npcs SET name=?, type=?, location=?, plotId=? WHERE id=?;");
			for (final NPC npc : npcs) {
				prep.setString(1, npc.getName());
				prep.setString(2, npc.getType().toString());
				prep.setString(3,
						Serializer.serializeLocation(npc.getLocation()));
				prep.setInt(4, npc.getPlotId());
				prep.setInt(5, npc.getId());
				prep.addBatch();
			}
			prep.executeBatch();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[NPC] updateNPC mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void updatePlot(Plot plot) {
		if (plot == null)
			return;
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
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
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLOT] updatePlot mysql error >> " + e.toString());
		}
	}

	public static void updatePlots(List<Plot> plots) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING PLOTS!");
		final List<NPC> npcs = new ArrayList<NPC>();
		try {
			final Connection conn = connPool.getConnection();

			final PreparedStatement prep = conn
					.prepareStatement("UPDATE plots SET "
							+ "name=?, location=?, size=?, owner=?, money=?, salePrice=?, protection=?, "
							+ "allowExplosions=?, private=?, friendBuild=?, upgrades=?, capturePoint=?, allowMagic=?, allowPvp=?, friends=?, coowners=?, title=? WHERE id=?; ");
			for (final Plot plot : plots) {
				plot.setUpdate(false);
				prep.setString(1, plot.getName());
				prep.setString(2,
						Serializer.serializeLocation(plot.getLocation()));
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
				prep.setString(15,
						Serializer.serializeUUIDList(plot.getFriends()));
				prep.setString(16,
						Serializer.serializeUUIDList(plot.getCoowners()));
				prep.setBoolean(17, plot.isTitleEntrence());
				prep.setInt(18, plot.getId());
				prep.addBatch();
				npcs.addAll(plot.getNpcs());
			}
			prep.executeBatch();
			conn.close();
			updateNPCS(npcs);
		} catch (final Exception e) {
			Lostshard.log.warning("[Plot] updatePlots mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void updateRune(PseudoPlayer targetPlayer, Rune foundRune) {
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE runes SET player_id=? WHERE id=?;");
			prep.setInt(1, targetPlayer.getId());
			prep.setInt(2, foundRune.getId());
			prep.executeUpdate();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] updateRune mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}

	}

	public static void updateStore(Store store) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING STORES!");
		try {
			final Connection conn = connPool.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE stores SET npcId=?, content=? WHERE id=?;");
			prep.setInt(1, store.getNpcId());
			prep.setString(2, store.getItemsAsJson());
			prep.setInt(3, store.getId());
			prep.executeUpdate();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[STORE] updateStores mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	protected static DataSource connPool = DataSource.getInstance();

	static PlayerManager pm = PlayerManager.getManager();

	static PlotManager ptm = PlotManager.getManager();

	static ClanManager cm = ClanManager.getManager();

	static ChestRefillManager crm = ChestRefillManager.getManager();
	// Plot
}
