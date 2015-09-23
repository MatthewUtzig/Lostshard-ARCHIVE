package com.lostshard.Lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Location;

import com.lostshard.Lostshard.Database.LostshardConnection;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.Rune;
import com.lostshard.Lostshard.Objects.Player.Runebook;
import com.lostshard.Lostshard.Utils.Serializer;

public class RuneMapper implements LostshardConnection {

	public static Rune createRuneFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
		final String label = rs.getString("label");
		final Location loc = Serializer.deserializeLocation(rs
				.getString("location"));
		return new Rune(loc, label, id);
	}
	
	public static PreparedStatement updateRunePreparedStatement(PreparedStatement prep, Rune rune, PseudoPlayer pPlayer) throws SQLException {
		prep.setInt(1, pPlayer.getId());
		prep.setInt(2, rune.getId());
		return prep;
	}
	
	public static void deleteRune(Rune rune) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM runes WHERE id=?;");
			prep.setInt(1, rune.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] deleteRune mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static int insertRune(int playerID, String response, Location markLoc) {
		int id = 0;
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO runes (location, label, player_id) VALUES (?,?,?);",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, Serializer.serializeLocation(markLoc));
			prep.setString(2, response);
			prep.setInt(3, playerID);
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			while (rs.next())
				id = rs.getInt(1);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] inserRune mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return id;
	}
	
	public static void updateRune(PseudoPlayer targetPlayer, Rune foundRune) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE runes SET player_id=? WHERE id=?;");
			updateRunePreparedStatement(prep, foundRune, targetPlayer);
			prep.executeUpdate();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] updateRune mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static Runebook getRunebook(int playerID) {
		final Runebook runebook = new Runebook();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT label, location, id  FROM runes WHERE player_id=?");
			prep.setInt(1, playerID);
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				runebook.addRune(createRuneFromResultSet(rs));
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[RUNES] getRunes mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		Lostshard.log.finest("[RUNES] got " + runebook.getRunes().size()
				+ " runes from DB.");
		return runebook;
	}
}
