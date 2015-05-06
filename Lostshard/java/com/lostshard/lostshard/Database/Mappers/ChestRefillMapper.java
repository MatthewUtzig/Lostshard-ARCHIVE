package com.lostshard.lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Database.LostshardConnection;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.ChestRefillManager;
import com.lostshard.lostshard.Objects.ChestRefill;
import com.lostshard.lostshard.Utils.Serializer;

public class ChestRefillMapper implements LostshardConnection {

	static ChestRefillManager crm = ChestRefillManager.getManager();
	
	public static ChestRefill createChestFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
		final Location location = Serializer.deserializeLocation(rs.getString("location"));
		final long rangeMin = rs.getLong("rangeMin");
		final long rangeMax = rs.getLong("rangeMax");
		final ItemStack[] items = Serializer.deserializeItems(rs.getString("items"));
		return new ChestRefill(location, rangeMin * 60000,
				rangeMax * 60000, items, id);
	}
	
	public static void deleteChest(ChestRefill cr) {
		try {
			final Connection conn = ds.getConnection();
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
	
	public static void getChests() {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM chests;");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				crm.getChests().add(createChestFromResultSet(rs));
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CHESTREFILL] getMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void insertChest(ChestRefill cr) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("INSERT INTO chests (location,items,rangeMin,rangeMax) VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, Serializer.serializeLocation(cr.getLocation()));
			prep.setString(2, Serializer.serializeItems(cr.getItems()));
			prep.setLong(3, cr.getRangeMin() / 60000);
			prep.setLong(4, cr.getRangeMax() / 60000);
			prep.execute();
			ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1); 
			cr.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CHESTREFILL] insertChest mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
}
