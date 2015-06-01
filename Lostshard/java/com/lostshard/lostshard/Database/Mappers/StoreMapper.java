package com.lostshard.lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Database.LostshardConnection;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.Store.Store;

public class StoreMapper implements LostshardConnection {

	public static void insertStore(Store store) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn.prepareStatement(
					"INSERT IGNORE INTO stores (npcId,content) VALUES (?,?);",
					Statement.RETURN_GENERATED_KEYS);
			prep.setInt(1, store.getNpcId());
			prep.setString(2, store.getAsJson());
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

	public static List<Store> getStores() {
		List<Store> stores = new ArrayList<Store>();
		try {
			Connection conn = ds.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM stores");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				int id = rs.getInt("id");
				try {
					int npcID = rs.getInt("npcId");
					String content = rs.getString("content");

					Store store = new Store(npcID, content);
					store.setId(id);

					stores.add(store);
				} catch (Exception e) {
					Lostshard.log
							.warning("[STORE] Exception when generating \""
									+ id + "\" store:");
					e.printStackTrace();
				}
			}
			prep.close();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[STORE] getStores mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		System.out.print("[STORE] got " + stores.size() + " stores from DB.");
		// Lostshard.getRegistry().setClans(clans);
		return stores;
	}

	public static void updateStore(Store store) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING STORES!");
		try {
			final Connection conn = ds.getConnection();
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

	public static void deleteStore(Store store) {
		try {
			final Connection conn = ds.getConnection();
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
}
