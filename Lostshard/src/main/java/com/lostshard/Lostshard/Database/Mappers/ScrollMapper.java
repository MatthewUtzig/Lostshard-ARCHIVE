package com.lostshard.Lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.lostshard.Lostshard.Database.LostshardConnection;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Spells.Scroll;

public class ScrollMapper implements LostshardConnection {
	
	public static Scroll createScrollFromResultSet(ResultSet rs) throws SQLException {
		final String scroll = rs.getString("scroll");
		return Scroll.getByString(scroll);
	}
	
	public static void deleteScroll(Scroll scroll, int playerID) {
		if (Lostshard.isDebug())
			System.out.print("DELETE SCROLL!");
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"DELETE FROM scrolls WHERE player_id=? AND scroll=? LIMIT 1;",
							Statement.RETURN_GENERATED_KEYS);
			prep.setInt(1, playerID);
			prep.setString(2, scroll.name());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[SCROLL] deleteScroll mysql error");
			e.printStackTrace();
		}
	}
	
	public static List<Scroll> getScrolls(int playerID) {
		Lostshard.log.finest("GETTING SCROLLS!");
		final List<Scroll> scrolls = new ArrayList<Scroll>();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT scroll FROM scrolls WHERE player_id=?;");
			prep.setInt(1, playerID);
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while(rs.next())
			scrolls.add(createScrollFromResultSet(rs));
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING, "[SCROLL] getScrolls mysql error");
			e.printStackTrace();
		}
		return scrolls;
	}
	
	public static void insertScroll(Scroll scroll, int playerID) {
		if (Lostshard.isDebug())
			System.out.print("INSERT SCROLL!");
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("INSERT IGNORE INTO scrolls "
							+ "(scroll,player_id) VALUES (?,?)");
			prep.setString(1, scroll.name());
			prep.setInt(2, playerID);
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[SCROLL] deleteScroll mysql error");
			e.printStackTrace();
		}
	}
	
	public static void updateScrollOwner(Scroll scroll, int tID, int pID) {
		Lostshard.log.finest("UPDATE SCROLLS OWNER!");
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE scrolls SET player_id=? WHERE player_id=?;");
			prep.setInt(1, tID);
			prep.setInt(1, pID);
			prep.executeUpdate();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING, "[SCROLL] updateScrollOwner mysql error");
			e.printStackTrace();
		}
	}
	
}
