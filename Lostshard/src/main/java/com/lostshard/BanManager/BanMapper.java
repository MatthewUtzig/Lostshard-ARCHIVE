package com.lostshard.BanManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.lostshard.Lostshard.Database.LostshardConnection;
import com.lostshard.Lostshard.Main.Lostshard;

import java.sql.PreparedStatement;
import java.util.UUID;

public class BanMapper implements LostshardConnection {

	public static int insertBan(UUID player, UUID admin, String reason, long time, long duration) {
		int id = -1;
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn.prepareStatement("INSERT INTO `bans` (`player`,`admin`,`reason`,`time`,`duration`) VALUES (?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, player.toString());
			prep.setString(2, admin.toString());
			prep.setString(3, reason);
			prep.setLong(4, time);
			prep.setLong(5, duration);
			final ResultSet rs = prep.getGeneratedKeys();
			while (rs.next())
				id = rs.getInt(1);
			prep.execute();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[BAN] insertBan mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return id;
	}
	
	public static int insertBan(Ban ban) {
		return insertBan(ban.getPlayer(), ban.getAdmin(), ban.getReason(), ban.getTime(), ban.getDuration());
	}
	
	public static Ban getBan(UUID player) {
		
		return null;
	}
	
	public static boolean isBanned() {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn.prepareStatement("SELECT FROM `bans` ");
			prep.execute();
			conn.close();
		} catch (Exception e) {
			Lostshard.log.warning("[BAN] isBanned mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return false;
	}
}
