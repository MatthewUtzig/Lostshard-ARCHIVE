package com.lostshard.Whitelist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.lostshard.lostshard.Database.LostshardConnection;
import com.lostshard.lostshard.Main.Lostshard;

public class KeyMapper implements LostshardConnection {
	
	private static Key createKeyFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
		final UUID uuid = UUID.fromString(rs.getString("player_uuid"));
		return new Key(id, uuid);
	}
	
	public static Key getKey(UUID uuid) {
		Lostshard.log.finest("[KEY] Getting Key from DB!");
		Key key = null;
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT id,player_uuid FROM `keys` WHERE player_uuid=?");
			prep.setString(1, uuid.toString());
			prep.execute();
			while(prep.getResultSet().next())
				key = createKeyFromResultSet(prep.getResultSet());
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[KEY] getKey mysql error, "+uuid);
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		if (key != null)
			Lostshard.log.finest("[KEY] got "
					+ Bukkit.getOfflinePlayer(uuid).getName()
					+ " players from DB.");
		return key;
	}
	
	public static Key insertKey(UUID uuid) {
		Key key = null;
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO keys (player_uuid) VALUES (?)",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, uuid.toString());
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			key = new Key(id, uuid);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLAYER] insertPlayer mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return key;
	}
}
