package com.lostshard.Lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Database.LostshardConnection;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.Store.StoreItem;
import com.lostshard.Lostshard.Skills.Build;
import com.lostshard.Lostshard.Utils.Serializer;

public class StoreItemMapper implements LostshardConnection {

	public static StoreItem createStoreItemFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
		final ItemStack item = Serializer.fromJsonToItemStack(rs.getString("item"));
		final int buy = rs.getInt("price");
		final int sale = rs.getInt("sale");
		final int stock = rs.getInt("stock");
		final int maxBuy = rs.getInt("maxbuy");
		final boolean autoResotck = rs.getBoolean("autoRestock");
		final int restockAmount = rs.getInt("restockAmount");
		final int restockTime = rs.getInt("restockTime");
		
		//general store info
		final StoreItem storeItem = new StoreItem(item);
		storeItem.setId(id);
		storeItem.setSalePrice(sale);
		storeItem.setBuyPrice(buy);
		
		//Stock and buy stock
		storeItem.setStock(stock);
		storeItem.setMaxBuyAmount(maxBuy);
		
		//Admin vendors
		storeItem.setAutoResotck(autoResotck);
		storeItem.setRestockAmount(restockAmount);
		storeItem.setRestockTime(restockTime);
		
		return storeItem;
	}
	
	public static PreparedStatement updatePreparedStatement(PreparedStatement prep, StoreItem i) throws SQLException {
		
		return prep;
	}
	
	public static List<StoreItem> getStoreItems(int StoreID) {
		final List<StoreItem> items = new ArrayList<StoreItem>();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM storeItems WHERE storeid=?");
			prep.setInt(1, StoreID);
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				items.add(createStoreItemFromResultSet(rs));
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[STORE] getStoreItem mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		Lostshard.log
				.finest("[STORE] got " + items.size() + " items from DB.");
		return items;
	}
	
	public static void insertBuild(Build build, int playerID) {
		try {
			final Connection conn = ds.getConnection();
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
	
	public static void updateBuilds(List<StoreItem> items) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE builds SET mining=?, miningLock=?, magery=?, mageryLock=?, blades=?, bladesLock=?, brawling=?, brawlingLock=?, blacksmithy=?, blacksmithyLock=?, lumberjacking=?, lumberjackingLock=?, fishing=?, fishingLock=?, survivalism=?, survivalismLock=?, taming=?, tamingLock=?, archery=?, archeryLock=? WHERE id=?;");
			for (final StoreItem i : items) {
				updatePreparedStatement(prep, i);
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
}
