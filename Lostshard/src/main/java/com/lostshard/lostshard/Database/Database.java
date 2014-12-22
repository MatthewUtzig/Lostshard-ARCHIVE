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
			}
			prep.executeBatch();
			prep.close();
			conn.close();
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

	// NPC
	public static void getNPCS() {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM plots");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<NPC> npcs = new ArrayList<NPC>();
			prep.close();
			conn.close();
			while (rs.next()) {
				String name = rs.getString("name");
				try {
					int id = rs.getInt("id");
					Location location = Serializer.deserializeLocation(rs
							.getString("location"));
					NPCType type = NPCType.valueOf(rs.getString("type"));
					int plotId = rs.getInt("plotId");
					NPC npc = new NPC(id, type, name, location, plotId);
					npcs.add(npc);
				} catch (Exception e) {
					Lostshard.log.log(Level.WARNING,
							"[NPC] Exception when generating \"" + name
									+ "\" NPC >> " + e.toString());
				}
			}
		} catch (Exception e) {
			Lostshard.log.warning("[NPC] getNPCS mysql error");
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

	public static void insertNPC(NPC npc) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO npcs (name,location,type,plotId) VALUES (?,?,?,?)");
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

	// Player
	public static List<PseudoPlayer> getPlayers() {
		System.out.print("[PLAYER] Getting Players from DB!");
		ArrayList<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
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
					players.add(pPlayer);
				} catch (Exception e) {
					Lostshard.log.log(Level.WARNING,
							"[PLAYER] Exception when generating \""
									+ id
									+ "\" player: " + e.toString());
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
							+ " plotCreationPoints=?, chatChannel=?, mana=?, stamina=?, rank=?, customSpawn=?, spawnTick=?, currentBuild=?, titles=?, currentTitle=? WHERE id=?");
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
			prep.setInt(16, pPlayer.getCurrentBuildId());
			prep.setString(17, Serializer.serializeStringArray(pPlayer.getTitels()));
			prep.setInt(18, pPlayer.getCurrentTitleId());
			prep.setInt(19, pPlayer.getId());

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
							+ "plotCreationPoints,chatChannel,mana,stamina,rank,customSpawn,spawnTick,currentBuild,titles,currentTitle) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
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
			prep.setInt(16, pPlayer.getSpawnTick());
			prep.setInt(17, pPlayer.getCurrentBuildId());
			prep.setString(18, Serializer.serializeStringArray(pPlayer.getTitels()));
			prep.setInt(19, pPlayer.getCurrentTitleId());
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
		try {
			Connection conn = connPool.getConnection();
			
			PreparedStatement prep = conn.prepareStatement("UPDATE players SET money=?, bank=?, murderCounts=?, criminalTicks=?, globalChat=?, privateChat=?, subscribeDays=?, wasSubscribed=?, plotCreationPoints=?, chatChannel=?, mana=?, stamina=?, rank=?, customSpawn=?, spawnTick=?, currentBuild=?, titles=?, currentTitle=? WHERE id=?; ");
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
				prep.setInt(16, pPlayer.getCurrentBuildId());
				prep.setString(17, Serializer.serializeStringArray(pPlayer.getTitels()));
				prep.setInt(18, pPlayer.getCurrentTitleId());
				prep.setInt(19, pPlayer.getId());
				prep.addBatch();
			}
			prep.executeBatch();
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[PLAYER] updatePlayers mysql error");
			Lostshard.mysqlError();
			if(Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	
	public static void saveAll() {

	}
}
