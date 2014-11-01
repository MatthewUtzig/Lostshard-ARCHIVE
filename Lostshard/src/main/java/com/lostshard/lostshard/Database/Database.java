package com.lostshard.lostshard.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Serializer;

public class Database {

	protected static ConnectionPool connPool = new ConnectionPool();
	
	//TODO finish up
	//Plot
	public static void getPlots() {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM plots");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					int id = rs.getInt("id");
					//Location
					Location location = Serializer.deserializeLocation(rs.getString("location"));
					int size = rs.getInt("size");
					int money = rs.getInt("money");
					int salePrice = rs.getInt("salePrice");
					//Toggles
					boolean protection = rs.getBoolean("protection");
					boolean allowExplosions = rs.getBoolean("allowExplosions");
					boolean privatePlot = rs.getBoolean("private");
					boolean friendBuild = rs.getBoolean("friendBuild");
					//Upgrades
					boolean town = rs.getBoolean("town");
					boolean dungeon = rs.getBoolean("dungeon");
					boolean neutralAlignment = rs.getBoolean("neutralAlignment");
					boolean autoKick = rs.getBoolean("autoKick");
					//Admin stuff
					boolean capturepoint = rs.getBoolean("capturePoint");
					boolean magic = rs.getBoolean("allowMagic");
					boolean pvp = rs.getBoolean("allowPvp");
					//Friend and co-owners and owner
					ArrayList<UUID> friends = (ArrayList<UUID>) Serializer.deserializeUUIDList(rs.getString("friends"));
					ArrayList<UUID> coowners = (ArrayList<UUID>) Serializer.deserializeUUIDList(rs.getString("coowners"));
					
					Plot plot = new Plot(name, id, size, money, salePrice, friends, coowners, protection, allowExplosions, privatePlot, friendBuild, town, dungeon, autoKick, neutralAlignment, location, capturepoint, null, magic, pvp);
					
					Lostshard.getPlots().add(plot);
					
				} catch(Exception e) {
					Lostshard.logger.log(Level.WARNING, "[PLOT] Exception when generating \""+name+"\" plot: "+e.toString());
				}
			}
		} catch(Exception e) {
			Lostshard.logger.log(Level.WARNING, "[PLOT] getPlots mysql error >> "+e.toString());
		}
	}
	
	public static void updatePlot(Plot plot) {
		if(plot == null)
			return;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE plot SET "
					+ "name=?, location=?, size=?, money=?, salePrice=?, protection=?, "
					+ "allowExplosions=?, private=?, friendBuild=?, town=?, dungeon=?, "
					+ "neutralAlignment=?, autoKick=?, capturePoint=?, allowMagic=?, allowPvp=? WHERE id=?");
			
			prep.setString(1, plot.getName());
			prep.setString(2, Serializer.serializeLocation(plot.getLocation()));
			prep.setInt(3, plot.getSize());
			prep.setInt(4, plot.getMoney());
			prep.setInt(5, plot.getSalePrice());
			prep.setBoolean(6, plot.isProtected());
			prep.setBoolean(7, plot.isAllowExplosions());
			prep.setBoolean(8, plot.isPrivatePlot());
			prep.setBoolean(9, plot.isFriendBuild());
			prep.setBoolean(10, plot.isTown());
			prep.setBoolean(11, plot.isDungeon());
			prep.setBoolean(12, plot.isNeutralAlignment());
			prep.setBoolean(13, plot.isAutoKick());
			prep.setBoolean(14, plot.isCapturePoint());
			prep.setBoolean(15, plot.isAllowMagic());
			prep.setBoolean(16, plot.isAllowPvp());
			prep.setInt(17, plot.getId());
			
			prep.executeUpdate();
			} catch (Exception e) {
				Lostshard.logger.log(Level.WARNING, "[PLOT] updatePlot mysql error >> "+e.toString());
			}
	}
	
	public static void insertPlot(Plot plot) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("INSERT IGNORE INTO plots "
					+ "(name,location,size,money,salePrice,protection,allowExplosions,"
					+ "private,friendBuild,town,dungeon,neutralAlignment,autoKick,"
					+ "capturePoint,allowMagic,allowPvp) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			prep.setString(1, plot.getName());
			prep.setString(2, Serializer.serializeLocation(plot.getLocation()));
			prep.setInt(3, plot.getSize());
			prep.setInt(4, plot.getMoney());
			prep.setInt(5, plot.getSalePrice());
			prep.setBoolean(6, plot.isProtected());
			prep.setBoolean(7, plot.isAllowExplosions());
			prep.setBoolean(8, plot.isPrivatePlot());
			prep.setBoolean(9, plot.isFriendBuild());
			prep.setBoolean(10, plot.isTown());
			prep.setBoolean(11, plot.isDungeon());
			prep.setBoolean(12, plot.isNeutralAlignment());
			prep.setBoolean(13, plot.isAutoKick());
			prep.setBoolean(14, plot.isCapturePoint());
			prep.setBoolean(15, plot.isAllowMagic());
			prep.setBoolean(16, plot.isAllowPvp());
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while(rs.next())
				id = rs.getInt(1);
			plot.setId(id);
			} catch (Exception e) {
				Lostshard.logger.log(Level.WARNING, "[PLOT] insertPlot mysql error >> "+e.toString());
			}
	}
	
	//NPC
	public static void getNPCS() {
		try {
		Connection conn = connPool.getConnection();
		PreparedStatement prep = conn.prepareStatement("SELECT * FROM plots");
		prep.execute();
		ResultSet rs = prep.getResultSet();
		ArrayList<NPC> npcs = new ArrayList<NPC>();
		while(rs.next()) {
			String name = rs.getString("name");
			try {
				int id = rs.getInt("id");
				Location location = Serializer.deserializeLocation(rs.getString("location"));
				NPCType type = NPCType.valueOf(rs.getString("type"));
				NPC npc = new NPC(id, type, name, location);
				npcs.add(npc);
			} catch(Exception e) {
				Lostshard.logger.log(Level.WARNING, "[PLOT] Exception when generating \""+name+"\" NPC: "+e.toString());
			}
		}
		} catch (Exception e) {
			Lostshard.logger.log(Level.WARNING, "[NPC] getNPCS mysql error >> "+e.toString());
		}
	}
	
	public static void updateNPC(NPC npc) {
		if(npc == null)
			return;
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("UPDATE npcs SET "
					+ "name=? location=?, type=? WHERE id=?");
			
			prep.setString(1, npc.getName());
			prep.setString(2, Serializer.serializeLocation(npc.getLocation()));
			prep.setString(3, npc.getType().toString());
			prep.setInt(4, npc.getId());
			
			prep.executeUpdate();
			} catch (Exception e) {
				Lostshard.logger.log(Level.WARNING, "[NPC] updateNPC mysql error >> "+e.toString());
			}
	}
	
	public static void insertNPC(NPC npc) {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("INSERT IGNORE INTO npcs (name,location,type) VALUES (?,?,?)");
			prep.setString(1, npc.getName());
			prep.setString(2, Serializer.serializeLocation(npc.getLocation()));
			prep.setString(3, npc.getType().toString());
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while(rs.next())
				id = rs.getInt(1);
			npc.setId(id);
			} catch (Exception e) {
				Lostshard.logger.log(Level.WARNING, "[NPC] updateNPC mysql error >> "+e.toString());
			}
	}
	
	//Player
		public static List<PseudoPlayer> getPlayers() {
			try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM players");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<PseudoPlayer> players = new ArrayList<PseudoPlayer>();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					int id = rs.getInt("id");
					int money = rs.getInt("money");
					int murderCounts = rs.getInt("murderCounts");
					UUID uuid = UUID.fromString(rs.getString("uuid"));
					//Bank
					int criminalTick = rs.getInt("criminalTick");
					boolean globalChat = rs.getBoolean("globalChat");
					int subscriberDays = rs.getInt("subscriberDays");
					boolean wasSubscribed = rs.getBoolean("wasSubscribed");
					int plotCreationPoints = rs.getInt("plotCreationPoints");
					
					PseudoPlayer pPlayer = new PseudoPlayer(id, money, murderCounts, uuid,
							null, criminalTick, globalChat, subscriberDays, wasSubscribed, plotCreationPoints);
					Lostshard.getPlayers().add(pPlayer);
				} catch(Exception e) {
					Lostshard.logger.log(Level.WARNING, "[PLOT] Exception when generating \""+name+"\" NPC: "+e.toString());
				}
				return players;
			}
			} catch (Exception e) {
				Lostshard.logger.log(Level.WARNING, "[NPC] getNPCS mysql error >> "+e.toString());
			}
			return null;
		}
		
		public static void updatePlayer(PseudoPlayer pPlayer) {
			if(pPlayer == null)
				return;
			try {
				Connection conn = connPool.getConnection();
				PreparedStatement prep = conn.prepareStatement("UPDATE players SET "
						+ "name=? location=?, type=? WHERE id=?");
				prep.setInt(1, pPlayer.getId());
				
				prep.executeUpdate();
				} catch (Exception e) {
					Lostshard.logger.log(Level.WARNING, "[NPC] updateNPC mysql error >> "+e.toString());
				}
		}
		
		public static void insertPlayer(PseudoPlayer pPlayer) {
			try {
				Connection conn = connPool.getConnection();
				PreparedStatement prep = conn.prepareStatement("INSERT IGNORE INTO npcs (name,location,type) VALUES (?,?,?)");
				prep.setString(1, "");
				prep.execute();
				ResultSet rs = prep.getGeneratedKeys();
				int id = 0;
				while(rs.next())
					id = rs.getInt(1);
				pPlayer.setId(id);
				} catch (Exception e) {
					Lostshard.logger.log(Level.WARNING, "[NPC] updateNPC mysql error >> "+e.toString());
				}
		}
}
