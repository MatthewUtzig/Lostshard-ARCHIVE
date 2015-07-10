package com.lostshard.Lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lostshard.Lostshard.Database.LostshardConnection;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Skills.Build;

public class BuildMapper implements LostshardConnection {
	
	public static Build createBuildFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
		final int mining = rs.getInt("mining");
		final boolean miningLock = rs.getBoolean("miningLock");
		final int magery = rs.getInt("magery");
		final boolean mageryLock = rs.getBoolean("mageryLock");
		final int blades = rs.getInt("blades");
		final boolean bladesLock = rs.getBoolean("bladesLock");
		final int brawling = rs.getInt("brawling");
		final boolean brawlingLock = rs.getBoolean("brawlingLock");
		final int blacksmithy = rs.getInt("blacksmithy");
		final boolean blacksmithyLock = rs.getBoolean("blacksmithyLock");
		final int lumberjacking = rs.getInt("lumberjacking");
		final boolean lumberjackingLock = rs.getBoolean("lumberjackingLock");
		final int fishing = rs.getInt("fishing");
		final boolean fishingLock = rs.getBoolean("fishingLock");
		final int survivalism = rs.getInt("survivalism");
		final boolean survivalismLock = rs.getBoolean("survivalismLock");
		final int taming = rs.getInt("taming");
		final boolean tamingLock = rs.getBoolean("tamingLock");
		final int archery = rs.getInt("archery");
		final boolean archeryLock = rs.getBoolean("archeryLock");

		final Build build = new Build();
		build.setId(id);
		build.getMining().setLvl(mining);
		build.getMining().setLocked(miningLock);
		build.getMagery().setLvl(magery);
		build.getMagery().setLocked(mageryLock);
		build.getBlades().setLvl(blades);
		build.getBlades().setLocked(bladesLock);
		build.getBrawling().setLvl(brawling);
		build.getBrawling().setLocked(brawlingLock);
		build.getBlackSmithy().setLvl(blacksmithy);
		build.getBlackSmithy().setLocked(blacksmithyLock);
		build.getLumberjacking().setLvl(lumberjacking);
		build.getLumberjacking().setLocked(lumberjackingLock);
		build.getFishing().setLvl(fishing);
		build.getFishing().setLocked(fishingLock);
		build.getSurvivalism().setLvl(survivalism);
		build.getSurvivalism().setLocked(survivalismLock);
		build.getTaming().setLvl(taming);
		build.getTaming().setLocked(tamingLock);
		build.getArchery().setLvl(archery);
		build.getArchery().setLocked(archeryLock);
		return build;
	}
	
	public static PreparedStatement updatePreparedStatement(PreparedStatement prep, Build build) throws SQLException {
		prep.setInt(1, build.getMining().getLvl());
		prep.setBoolean(2, build.getMining().isLocked());
		prep.setInt(3, build.getMagery().getLvl());
		prep.setBoolean(4, build.getMagery().isLocked());
		prep.setInt(5, build.getBlades().getLvl());
		prep.setBoolean(6, build.getBlades().isLocked());
		prep.setInt(7, build.getBrawling().getLvl());
		prep.setBoolean(8, build.getBrawling().isLocked());
		prep.setInt(9, build.getBlackSmithy().getLvl());
		prep.setBoolean(10, build.getBlackSmithy().isLocked());
		prep.setInt(11, build.getLumberjacking().getLvl());
		prep.setBoolean(12, build.getLumberjacking().isLocked());
		prep.setInt(13, build.getFishing().getLvl());
		prep.setBoolean(14, build.getFishing().isLocked());
		prep.setInt(15, build.getSurvivalism().getLvl());
		prep.setBoolean(16, build.getSurvivalism().isLocked());
		prep.setInt(17, build.getTaming().getLvl());
		prep.setBoolean(18, build.getTaming().isLocked());
		prep.setInt(19, build.getArchery().getLvl());
		prep.setBoolean(20, build.getArchery().isLocked());
		prep.setInt(21, build.getId());
		return prep;
	}
	
	public static List<Build> getBuilds(int playerID) {
		final List<Build> builds = new ArrayList<Build>();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM builds WHERE player_id=?");
			prep.setInt(1, playerID);
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				builds.add(createBuildFromResultSet(rs));
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[BUILD] getBuilds mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		Lostshard.log
				.finest("[BUILD] got " + builds.size() + " build from DB.");
		return builds;
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
	
	public static void updateBuilds(List<Build> builds) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE builds SET mining=?, miningLock=?, magery=?, mageryLock=?, blades=?, bladesLock=?, brawling=?, brawlingLock=?, blacksmithy=?, blacksmithyLock=?, lumberjacking=?, lumberjackingLock=?, fishing=?, fishingLock=?, survivalism=?, survivalismLock=?, taming=?, tamingLock=?, archery=?, archeryLock=? WHERE id=?;");
			for (final Build build : builds) {
				updatePreparedStatement(prep, build);
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
