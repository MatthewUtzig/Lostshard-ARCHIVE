package com.lostshard.lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.lostshard.lostshard.Database.LostshardConnection;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Utils.Serializer;

public class NPCMapper implements LostshardConnection {
	
	public static NPC createNPCFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
		final String name = rs.getString("name");
		final NPCType type = NPCType.valueOf(rs.getString("type"));
		final Location location = Serializer.deserializeLocation(rs
				.getString("location"));
		final int plotId = rs.getInt("plot_id");

		final NPC npc = new NPC(id, type, name, location, plotId);
		return npc;
	}
	
	public static void deleteNPC(NPC npc) {
		try {
			final Connection conn = ds.getConnection();
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
	
	public static void insertNPC(NPC npc) {
		try {
			final Connection conn = ds.getConnection();
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
	
	public static void updateNPC(NPC npc) {
		if (npc == null)
			return;
		try {
			final Connection conn = ds.getConnection();
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
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE npcs SET name=?, type=?, location=?, plot_id=? WHERE id=?;");
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
	
	public static List<NPC> getNPCS() {
		final List<NPC> npcs = new ArrayList<NPC>();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM npcs");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				npcs.add(createNPCFromResultSet(rs));
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[NPC] getNPCS mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return npcs;
	}
}
