package com.lostshard.lostshard.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCManager;
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
			ArrayList<Plot> plots = new ArrayList<Plot>();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					int id = rs.getInt("id");
					//Location
					Location location = Serializer.deserializeLocation(rs.getString("location"));
					//Upgrades
					boolean town = rs.getBoolean("town");
					boolean dungeon = rs.getBoolean("dungeon");
					boolean neutralAlignment = rs.getBoolean("neutralalignment");
					boolean autoKick = rs.getBoolean("autokick");
					//Admin stuff
					boolean capturepoint = rs.getBoolean("capturepoint");
					boolean magic = rs.getBoolean("magic");
					boolean pvp = rs.getBoolean("pvp");
					//Friend and co-owners and owner
					
				} catch(Exception e) {
					Lostshard.logger.log(Level.WARNING, "[PLOT] Exception when generating \""+name+"\" plot: "+e.toString());
				}
			}
		} catch(Exception e) {
			Lostshard.logger.log(Level.WARNING, "[PLOT] getPlots mysql error >> "+e.toString());
		}
	}
	
	public static void updatePlot(Plot plot) {
		
	}
	
	public static void insertPlot(Plot plot) {
		
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
			prep.setInt(1, npc.getId());
			
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
					
					
					PseudoPlayer pPlayer = new PseudoPlayer(id, id, id, null, null, id, false, id, false, id);
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
