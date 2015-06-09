package com.lostshard.lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.lostshard.lostshard.Database.LostshardConnection;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Spells.Structures.PermanentGate;
import com.lostshard.lostshard.Utils.Serializer;

public class PermanentGateMapper implements LostshardConnection {

	public static PermanentGate createPermanentGateFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
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
		return new PermanentGate(blocks, uuid, id, direction);
	}
	
	public static void deletePermanentGate(PermanentGate gate) {
		try {
			final Connection conn = ds.getConnection();
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
	
	public static void getPermanentGates() {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM permanentgates");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				
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
	
	public static int insertPermanentGate(PermanentGate gate) {
		int id = 0;
		try {
			final Connection conn = ds.getConnection();
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
}
