package com.lostshard.lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lostshard.lostshard.Database.LostshardConnection;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Utils.Serializer;

public class ClanMapper implements LostshardConnection {

	static ClanManager cm = ClanManager.getManager();
	
	public static Clan createClanFromResultSet(ResultSet rs) throws SQLException {
		final int id = rs.getInt("id");
		final String name = rs.getString("name");
		final UUID owner = UUID.fromString(rs.getString("owner"));
		final List<UUID> leaders = Serializer
				.deserializeUUIDList(rs.getString("leaders"));
		final List<UUID> members = Serializer
				.deserializeUUIDList(rs.getString("members"));
		final List<UUID> invited = Serializer
				.deserializeUUIDList(rs.getString("invited"));

		final Clan clan = new Clan(name, owner);
		clan.setId(id);
		clan.setLeaders(leaders);
		clan.setMembers(members);
		clan.setInvited(invited);
		return clan;
	}
	
	public static PreparedStatement updateClanPreparedStatement(PreparedStatement prep, Clan clan) throws SQLException {
		prep.setString(1, clan.getName());
		prep.setString(2, clan.getOwner().toString());
		prep.setString(3,
				Serializer.serializeUUIDList(clan.getLeaders()));
		prep.setString(4,
				Serializer.serializeUUIDList(clan.getMembers()));
		prep.setString(5,
				Serializer.serializeUUIDList(clan.getInvited()));
		prep.setInt(6, clan.getId());
		return prep;
	}
	
	public static void deleteClan(Clan clan) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("DELETE FROM clans WHERE id=?;");
			prep.setInt(1, clan.getId());
			prep.execute();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CLAN] deleteClan mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static List<Clan> getClans() {
		final List<Clan> clans = new ArrayList<Clan>();
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM clans");
			prep.execute();
			final ResultSet rs = prep.getResultSet();
			while (rs.next()) {
				Clan clan =  createClanFromResultSet(rs);
				clans.add(clan);
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CLAN] getClans mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		System.out.print("[CLAN] got " + clans.size() + " clans from DB.");
		cm.setClans(clans);
		return clans;
	}
	
	public static void insertClan(Clan clan) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO clans (name,owner,leaders,members,invited) VALUES (?,?,?,?,?);",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, clan.getName());
			prep.setString(2, clan.getOwner().toString());
			prep.setString(3, Serializer.serializeUUIDList(clan.getLeaders()));
			prep.setString(4, Serializer.serializeUUIDList(clan.getMembers()));
			prep.setString(5, Serializer.serializeUUIDList(clan.getInvited()));
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			clan.setId(id);
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[CLAN] inserClan mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void updateClans(List<Clan> clans) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING CLANS!");
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("UPDATE clans SET name=?, owner=?, leaders=?, members=?, invited=? WHERE id=?;");
			for (final Clan clan : clans) {
				clan.setUpdate(false);
				prep.addBatch();
			}
			prep.executeBatch();
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[Clan] updateClans mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
}
