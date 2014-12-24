package com.lostshard.lostshard.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Bank;
import com.lostshard.lostshard.Objects.ChatChannel;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.Build;
import com.lostshard.lostshard.Utils.Serializer;
import com.lostshard.lostshard.Database.DataSource;

public class Database {
	
	protected static DataSource connPool = DataSource.getInstance();

	// TODO finish up
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
					boolean town = rs.getBoolean("town");
					boolean dungeon = rs.getBoolean("dungeon");
					boolean neutralAlignment = rs
							.getBoolean("neutralAlignment");
					boolean autoKick = rs.getBoolean("autoKick");
					// Admin stuff
					boolean capturepoint = rs.getBoolean("capturePoint");
					boolean magic = rs.getBoolean("allowMagic");
					boolean pvp = rs.getBoolean("allowPvp");
					// Friend and co-owners and owner
					ArrayList<UUID> friends = (ArrayList<UUID>) Serializer
							.deserializeUUIDList(rs.getString("friends"));
					ArrayList<UUID> coowners = (ArrayList<UUID>) Serializer
							.deserializeUUIDList(rs.getString("coowners"));

					Plot plot = new Plot(id, name, UUID.fromString(owner), location);
					plot.setSize(size);
					plot.setMoney(money);
					plot.setSalePrice(salePrice);
					plot.setProtected(protection);
					plot.setAllowExplosions(allowExplosions);
					plot.setPrivatePlot(privatePlot);
					plot.setFriendBuild(friendBuild);
					plot.setTown(town);
					plot.setDungeon(dungeon);
					plot.setNeutralAlignment(neutralAlignment);
					plot.setAutoKick(autoKick);
					plot.setCapturePoint(capturepoint);
					plot.setAllowMagic(magic);
					plot.setAllowPvp(pvp);
					plot.setFriends(friends);
					plot.setCoowners(coowners);
					
					for(NPC npc : npcs)
						if(npc.getPlotId() == plot.getId())
							plot.getNpcs().add(npc);

					Lostshard.getRegistry().getPlots().add(plot);
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
							+ "allowExplosions=?, private=?, friendBuild=?, town=?, dungeon=?, "
							+ "neutralAlignment=?, autoKick=?, capturePoint=?, allowMagic=?, allowPvp=?, friends=?, coowners=? WHERE id=?");

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
			prep.setBoolean(11, plot.isTown());
			prep.setBoolean(12, plot.isDungeon());
			prep.setBoolean(13, plot.isNeutralAlignment());
			prep.setBoolean(14, plot.isAutoKick());
			prep.setBoolean(15, plot.isCapturePoint());
			prep.setBoolean(16, plot.isAllowMagic());
			prep.setBoolean(17, plot.isAllowPvp());
			prep.setString(18, Serializer.serializeUUIDList(plot.getFriends()));
			prep.setString(19, Serializer.serializeUUIDList(plot.getCoowners()));
			prep.setInt(20, plot.getId());

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
					+ "allowExplosions=?, private=?, friendBuild=?, town=?, dungeon=?, "
					+ "neutralAlignment=?, autoKick=?, capturePoint=?, allowMagic=?, allowPvp=?, friends=?, coowners=? WHERE id=?; ");
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
				prep.setBoolean(11, plot.isTown());
				prep.setBoolean(12, plot.isDungeon());
				prep.setBoolean(13, plot.isNeutralAlignment());
				prep.setBoolean(14, plot.isAutoKick());
				prep.setBoolean(15, plot.isCapturePoint());
				prep.setBoolean(16, plot.isAllowMagic());
				prep.setBoolean(17, plot.isAllowPvp());
				prep.setString(18, Serializer.serializeUUIDList(plot.getFriends()));
				prep.setString(19, Serializer.serializeUUIDList(plot.getCoowners()));
				prep.setInt(20, plot.getId());
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
							+ "private,friendBuild,town,dungeon,neutralAlignment,autoKick,"
							+ "capturePoint,allowMagic,allowPvp,friends,coowners) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
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
			prep.setBoolean(11, plot.isTown());
			prep.setBoolean(12, plot.isDungeon());
			prep.setBoolean(13, plot.isNeutralAlignment());
			prep.setBoolean(14, plot.isAutoKick());
			prep.setBoolean(15, plot.isCapturePoint());
			prep.setBoolean(16, plot.isAllowMagic());
			prep.setBoolean(17, plot.isAllowPvp());
			prep.setString(18, Serializer.serializeUUIDList(plot.getFriends()));
			prep.setString(19, Serializer.serializeUUIDList(plot.getCoowners()));
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
	
	public static Map<Integer, Build> getBuilds() {
		Map<Integer, Build> builds = new HashMap<Integer, Build>();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM builds");
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
					
					builds.put(id, build);
					
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
		System.out.print("[BUILD] got "+builds.size()+" build from DB.");
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
	
	public static void insertBuild(Build build) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("INSERT IGNORE INTO builds (mining, miningLock, magery, mageryLock, blades, bladesLock, brawling, brawlingLock, blacksmithy, blacksmithyLock, lumberjacking, lumberjackingLock, fishing, fishingLock, survivalism, survivalismLock, taming, tamingLock, archery, archeryLock) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);
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
				prep.addBatch();
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
	public static List<PseudoPlayer> getPlayers() {
		System.out.print("[PLAYER] Getting Players from DB!");
		ArrayList<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
		Map<Integer, Build> builds = Database.getBuilds();
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM players");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					UUID uuid = UUID.fromString(rs.getString("uuid"));
					int money = rs.getInt("money");
					int murderCounts = rs.getInt("murderCounts");
					// Bank
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
					Location customSpawn = Serializer.deserializeLocation(rs.getString("customSpawn"));
					int spawnTick = rs.getInt("spawnTick");
					int[] buildsIds = Serializer.deserializeIntegerArray(rs.getString("builds"));
					int currentBuild = rs.getInt("currentBuild");
					List<String> titles = Serializer.deserializeStringArray(rs.getString("titles"));
					int currentTitle = rs.getInt("currentTitle");
					
					PseudoPlayer pPlayer = new PseudoPlayer(uuid, id);
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
					pPlayer.setCustomSpawn(customSpawn);
					pPlayer.setSpawnTick(spawnTick);
					pPlayer.setCurrentBuildId(currentBuild);
					pPlayer.setTitels(titles);
					pPlayer.setCurrentTitleId(currentTitle);
					if(builds.containsKey(buildsIds[0])) {
						pPlayer.getBuilds().clear();
						for(int i : buildsIds) {
							pPlayer.getBuilds().add(builds.get(i));
						}
					}
					players.add(pPlayer);
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
		Lostshard.getRegistry().setPlayers(players);
		System.out.print("[PLAYER] got "+players.size()+" players from DB.");
		return players;
	}

	public static void updatePlayer(PseudoPlayer pPlayer) {
		if (pPlayer == null)
			return;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("UPDATE players SET "
							+ "money=?, bank=?, murderCounts=?, criminalTicks=?, "
							+ "globalChat=?, privateChat=?, subscribeDays=?, wasSubscribed=?,"
							+ " plotCreationPoints=?, chatChannel=?, mana=?, stamina=?, rank=?, customSpawn=?, spawnTick=?, builds=?, currentBuild=?, titles=?, currentTitle=? WHERE id=?");
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
			prep.setString(14, Serializer.serializeLocation(pPlayer.getCustomSpawn()));
			System.out.print(Serializer.serializeIntegerArray(pPlayer.getBuildIds()));
			prep.setInt(15, pPlayer.getSpawnTick());
			prep.setString(16, Serializer.serializeIntegerArray(pPlayer.getBuildIds()));
			prep.setInt(17, pPlayer.getCurrentBuildId());
			prep.setString(18, Serializer.serializeStringArray(pPlayer.getTitels()));
			prep.setInt(19, pPlayer.getCurrentTitleId());
			prep.setInt(20, pPlayer.getId());

			prep.executeUpdate();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PLAYER] updatePlayer mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static void insertPlayer(PseudoPlayer pPlayer) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO players "
							+ "(uuid,money,bank,murderCounts,criminalTicks,globalChat,privateChat,subscribeDays,wasSubscribed,"
							+ "plotCreationPoints,chatChannel,mana,stamina,rank,customSpawn,spawnTick,builds,currentBuild,titles,currentTitle) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
			prep.setString(1, pPlayer.getPlayerUUID().toString());
			prep.setInt(2, pPlayer.getMoney());
			prep.setString(3, pPlayer.getBank().Serialize());
			prep.setInt(4, pPlayer.getMurderCounts());
			prep.setInt(5, pPlayer.getCriminal());
			prep.setBoolean(6, pPlayer.isGlobalChat());
			prep.setBoolean(7, pPlayer.isPrivateChat());
			prep.setInt(8, pPlayer.getSubscribeDays());
			prep.setBoolean(9, pPlayer.wasSubscribed());
			prep.setInt(10, pPlayer.getPlotCreatePoints());
			prep.setString(11, pPlayer.getChatChannel().toString());
			prep.setInt(12, pPlayer.getMana());
			prep.setInt(13, pPlayer.getStamina());
			prep.setInt(14, pPlayer.getRank());
			prep.setString(15, Serializer.serializeLocation(pPlayer.getCustomSpawn()));
			insertBuild(pPlayer.getCurrentBuild());
			prep.setString(16, Serializer.serializeIntegerArray(pPlayer.getBuildIds()));
			prep.setInt(17, pPlayer.getCurrentBuildId());
			prep.setString(18, Serializer.serializeStringArray(pPlayer.getTitels()));
			prep.setInt(19, pPlayer.getCurrentTitleId());
			prep.setInt(20, pPlayer.getId());
			
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			pPlayer.setId(id);
			Lostshard.getRegistry().getPlayers().add(pPlayer);
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLAYER] insertPlayer mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static PseudoPlayer getPlayer(int id) {
		System.out.print("[PLAYER] Getting Player from DB!");
		PseudoPlayer pPlayer = null;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM players WHERE id=?");
			prep.setInt(1, id);
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
					UUID uuid = UUID.fromString(rs.getString("uuid"));
					int money = rs.getInt("money");
					int murderCounts = rs.getInt("murderCounts");
					// Bank
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
					Location customSpawn = Serializer.deserializeLocation(rs.getString("customSpawn"));
					int spawnTick = rs.getInt("spawnTick");
					int currentBuild = rs.getInt("currentBuild");
					List<String> titles = Serializer.deserializeStringArray(rs.getString("titles"));
					int currentTitle = rs.getInt("currentTitle");

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
					pPlayer.setCustomSpawn(customSpawn);
					pPlayer.setSpawnTick(spawnTick);
					pPlayer.setCurrentBuildId(currentBuild);
					pPlayer.setTitels(titles);
					pPlayer.setCurrentTitleId(currentTitle);
					return pPlayer;
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PLAYER] getPlayers mysql error");
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
			
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET money=?, bank=?, murderCounts=?, criminalTicks=?, globalChat=?, privateChat=?, subscribeDays=?, wasSubscribed=?, plotCreationPoints=?, chatChannel=?, mana=?, stamina=?, rank=?, customSpawn=?, spawnTick=?, builds=?, currentBuild=?, titles=?, currentTitle=? WHERE id=?; ");
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
				prep.setString(14, Serializer.serializeLocation(pPlayer.getCustomSpawn()));
				prep.setInt(15, pPlayer.getSpawnTick());
				prep.setString(16, Serializer.serializeIntegerArray(pPlayer.getBuildIds()));
				prep.setInt(17, pPlayer.getCurrentBuildId());
				prep.setString(18, Serializer.serializeStringArray(pPlayer.getTitels()));
				prep.setInt(19, pPlayer.getCurrentTitleId());
				prep.setInt(20, pPlayer.getId());
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
			Lostshard.log.warning("[NPC] updateNPC mysql error");
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
	
	public static void saveAll() {

	}
}
