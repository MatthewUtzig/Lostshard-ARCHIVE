package com.lostshard.lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lostshard.lostshard.Database.LostshardConnection;
import com.lostshard.lostshard.Main.Lostshard;

public class MessagesMapper implements LostshardConnection {
	
	public static void deleteMessages(UUID uuid) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM offlineMessages WHERE player=?;");
			prep.setString(1, uuid.toString());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[MESSAGES] deleteMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}

	public static List<String> getOfflineMessages(UUID uuid) {
		final List<String> list = new ArrayList<String>();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM offlineMessages WHERE player=?;");
			prep.setString(1, uuid.toString());
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				final String msg = rs.getString("message");
				list.add(msg);
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[MESSAGES] getMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return list;
	}

	public static void insertMessages(UUID uuid, String msg) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("INSERT INTO offlinemessages (player,message) VALUES (?,?);");
			prep.setString(1, uuid.toString());
			prep.setString(2, msg);
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[MESSAGES] insertMessages mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
}
