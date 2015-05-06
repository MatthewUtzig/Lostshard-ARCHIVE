package com.lostshard.lostshard.Database;

import java.sql.Connection;

import com.lostshard.lostshard.Main.Lostshard;

public class Database {

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

	protected static DataSource connPool = DataSource.getInstance();
}
