package com.lostshard.Lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Location;

import com.lostshard.Lostshard.Database.LostshardConnection;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.PlotCapturePoint;
import com.lostshard.Lostshard.Objects.Plot.PlotUpgrade;
import com.lostshard.Lostshard.Utils.Serializer;

public class PlotMapper implements LostshardConnection {

	static PlotManager ptm = PlotManager.getManager();
	
	public static Plot createPlotFromResultSet(ResultSet rs) throws SQLException {
		final String name = rs.getString("name");
		final String owner = rs.getString("owner");
		final int id = rs.getInt("id");
		// Location
		final Location location = Serializer.deserializeLocation(rs
				.getString("location"));
		final int size = rs.getInt("size");
		final int money = rs.getInt("money");
		final int salePrice = rs.getInt("salePrice");
		// Toggles
		final boolean protection = rs.getBoolean("protection");
		final boolean allowExplosions = rs
				.getBoolean("allowExplosions");
		final boolean privatePlot = rs.getBoolean("private");
		final boolean friendBuild = rs.getBoolean("friendBuild");
		// Upgrades
		final List<String> upgrades = Serializer
				.deserializeStringArray(rs.getString("upgrades"));
		// Admin stuff
		final boolean capturepoint = rs.getBoolean("capturePoint");
		final boolean magic = rs.getBoolean("allowMagic");
		final boolean pvp = rs.getBoolean("allowPvp");
		final boolean title = rs.getBoolean("title");
		// Friend and co-owners and owner
		final ArrayList<UUID> friends = (ArrayList<UUID>) Serializer
				.deserializeUUIDList(rs.getString("friends"));
		final ArrayList<UUID> coowners = (ArrayList<UUID>) Serializer
				.deserializeUUIDList(rs.getString("coowners"));
		Plot plot;
		if (capturepoint)
			plot = new PlotCapturePoint(id, name,
					UUID.fromString(owner), location);
		else
			plot = new Plot(id, name, UUID.fromString(owner),
					location);
		plot.setSize(size);
		plot.setMoney(money);
		plot.setSalePrice(salePrice);
		plot.setProtected(protection);
		plot.setAllowExplosions(allowExplosions);
		plot.setPrivatePlot(privatePlot);
		plot.setFriendBuild(friendBuild);
		plot.setTitleEntrence(title);
		if (upgrades != null)
			for (final String s : upgrades)
				plot.addUpgrade(PlotUpgrade.valueOf(s));
		plot.setAllowMagic(magic);
		plot.setAllowPvp(pvp);
		plot.setFriends(friends);
		plot.setCoowners(coowners);
		return plot;
	}
	
	public static PreparedStatement updatePreparedStatement(PreparedStatement prep, Plot plot) throws SQLException {
		plot.setUpdate(false);
		prep.setString(1, plot.getName());
		prep.setString(2,
				Serializer.serializeLocation(plot.getLocation()));
		prep.setInt(3, plot.getSize());
		prep.setString(4, plot.getOwner().toString());
		prep.setInt(5, plot.getMoney());
		prep.setInt(6, plot.getSalePrice());
		prep.setBoolean(7, plot.isProtected());
		prep.setBoolean(8, plot.isAllowExplosions());
		prep.setBoolean(9, plot.isPrivatePlot());
		prep.setBoolean(10, plot.isFriendBuild());
		prep.setString(11, plot.upgradesToJson());
		prep.setBoolean(12, plot instanceof PlotCapturePoint);
		prep.setBoolean(13, plot.isAllowMagic());
		prep.setBoolean(14, plot.isAllowPvp());
		prep.setString(15,
				Serializer.serializeUUIDList(plot.getFriends()));
		prep.setString(16,
				Serializer.serializeUUIDList(plot.getCoowners()));
		prep.setBoolean(17, plot.isTitleEntrence());
		prep.setInt(18, plot.getId());
		return prep;
	}
	
	public static void deletePlot(Plot plot) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM plots WHERE id=?;");
			prep.setInt(1, plot.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[PLOT] deletePlot mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void getPlots() {
		System.out.print("GETTING PLOTS!");
		final List<NPC> npcs = NPCMapper.getNPCS();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM plots");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			
			while(rs.next()) {
			
				Plot plot = createPlotFromResultSet(rs);
				
				for (final NPC npc : npcs)
					if (npc.getPlotId() == plot.getId())
						plot.getNpcs().add(npc);
				ptm.getPlots().add(plot);
			}
			conn.close();
			Lostshard.log.info("[PLOT] got "+ptm.getPlots().size());
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING, "[PLOT] getPlots mysql error");
			e.printStackTrace();
		}
		for(NPC npc : npcs)
			npc.spawn();
	}
	
	public static void insertPlot(Plot plot) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO plots "
									+ "(name,location,owner,friends,coowners) VALUES (?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, plot.getName());
			prep.setString(2, Serializer.serializeLocation(plot.getLocation()));
			prep.setString(3, plot.getOwner().toString());
			prep.setString(4, Serializer.serializeUUIDList(plot.getFriends()));
			prep.setString(5, Serializer.serializeUUIDList(plot.getCoowners()));
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			plot.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[PLOT] insertPlot mysql error");
			e.printStackTrace();
		}
	}
	

	public static void updatePlot(Plot plot) {
		if (plot == null)
			return;
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE plot SET "
							+ "name=?, location=?, size=?, owner=?, money=?, salePrice=?, protection=?, "
							+ "allowExplosions=?, private=?, friendBuild=?, upgrades=?, capturePoint=?, allowMagic=?, allowPvp=?, friends=?, coowners=?, title=? WHERE id=?");
			updatePreparedStatement(prep, plot);
			prep.executeUpdate();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLOT] updatePlot mysql error >> " + e.toString());
		}
	}
	
	public static void updatePlots(List<Plot> plots) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING PLOTS!");
		final List<NPC> npcs = new ArrayList<NPC>();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE plots SET "
							+ "name=?, location=?, size=?, owner=?, money=?, salePrice=?, protection=?, "
							+ "allowExplosions=?, private=?, friendBuild=?, upgrades=?, capturePoint=?, allowMagic=?, allowPvp=?, friends=?, coowners=?, title=? WHERE id=?; ");
			for (final Plot plot : plots) {
				updatePreparedStatement(prep, plot);
				prep.addBatch();
				npcs.addAll(plot.getNpcs());
			}
			prep.executeBatch();
			conn.close();
			NPCMapper.updateNPCS(npcs);
		} catch (final Exception e) {
			Lostshard.log.warning("[Plot] updatePlots mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
}
