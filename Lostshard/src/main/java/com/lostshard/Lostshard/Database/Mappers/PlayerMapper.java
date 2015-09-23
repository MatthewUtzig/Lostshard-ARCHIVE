package com.lostshard.Lostshard.Database.Mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.lostshard.Lostshard.Database.LostshardConnection;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.ChatChannel;
import com.lostshard.Lostshard.Objects.Player.Bank;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Serializer;

public class PlayerMapper implements LostshardConnection {

	public static PseudoPlayer createPlayerFromResultSet(ResultSet rs) throws SQLException {
		PseudoPlayer result;
		
		final int id = rs.getInt("id");
		final UUID uuid = UUID.fromString(rs.getString("uuid"));
		final int money = rs.getInt("money");
		final int murderCounts = rs.getInt("murderCounts");
		final int criminalTick = rs.getInt("criminalTicks");
		final boolean globalChat = rs.getBoolean("globalChat");
		final boolean privateChat = rs.getBoolean("privateChat");
		final int subscriberDays = rs.getInt("subscribeDays");
		final boolean wasSubscribed = rs
				.getBoolean("wasSubscribed");
		final Bank bank = new Bank(rs.getString("bank"),
				wasSubscribed);
		final int plotCreationPoints = rs
				.getInt("plotCreationPoints");
		final String chatChannel = rs.getString("chatChannel");
		final int mana = rs.getInt("mana");
		final int stamina = rs.getInt("stamina");
		final int rank = rs.getInt("rank");
		final int spawnTick = rs.getInt("spawnTick");
		final int currentBuild = rs.getInt("currentBuild");
		final List<String> titles = Serializer
				.deserializeStringArray(rs.getString("titles"));
		final int currentTitle = rs.getInt("currentTitle");
		final int freeSkillPoints = rs.getInt("freeSkillPoints");
		final List<String> spellbook = Serializer
				.deserializeStringArray(rs.getString("spellbook"));
		final boolean gui = rs.getBoolean("gui");
		final List<UUID> ignored = Serializer
				.deserializeUUIDList(rs.getString("ignored"));

		result = new PseudoPlayer(uuid, id);
		
		result.setMoney(money);
		result.setMurderCounts(murderCounts);
		result.setCriminal(criminalTick);
		result.setGlobalChat(globalChat);
		result.setPrivateChat(privateChat);
		result.setSubscribeDays(subscriberDays);
		result.setWasSubscribed(wasSubscribed);
		result.setPlotCreatePoints(plotCreationPoints);
		result.setBank(bank);
		result.setChatChannel(ChatChannel.valueOf(chatChannel));
		result.setMana(mana);
		result.setStamina(stamina);
		result.setRank(rank);
		result.getTimer().spawnTicks = spawnTick;
		result.setCurrentBuildId(currentBuild);
		result.setTitels(titles);
		result.setCurrentTitleId(currentTitle);
		result.setFreeSkillPoints(freeSkillPoints);
		result.setAllowGui(gui);
		result.setIgnored(ignored);
		if (spellbook != null)
			for (final String s : spellbook)
				result.getSpellbook().addSpell(
						Scroll.getByString(s));
		result.setBuilds(BuildMapper.getBuilds(id));
		result.setRunebook(RuneMapper.getRunebook(id));
		result.setScrools(ScrollMapper.getScrolls(id));
		
		return result;
	}
	
	public static PreparedStatement updatePreparedStatement(PreparedStatement prep, PseudoPlayer pPlayer) throws SQLException {
		prep.setInt(1, pPlayer.getMoney());
		prep.setString(2, pPlayer.getBank().Serialize());
		prep.setInt(3, pPlayer.getMurderCounts());
		prep.setInt(4, pPlayer.getCriminal());
		prep.setBoolean(5, pPlayer.isGlobalChat());
		prep.setBoolean(6, pPlayer.isPrivateChat());
		prep.setInt(7, pPlayer.getSubscribeDays());
		prep.setBoolean(8, pPlayer.wasSubscribed());
		prep.setInt(9, pPlayer.getPlotCreatePoints());
		prep.setString(10, pPlayer.getChatChannel().toString());
		prep.setInt(11, pPlayer.getMana());
		prep.setInt(12, pPlayer.getStamina());
		prep.setInt(13, pPlayer.getRank());
		prep.setInt(14, pPlayer.getTimer().spawnTicks);
		prep.setInt(15, pPlayer.getCurrentBuildId());
		prep.setString(16,
				Serializer.serializeStringArray(pPlayer.getTitels()));
		prep.setInt(17, pPlayer.getCurrentTitleId());
		prep.setInt(18, pPlayer.getFreeSkillPoints());
		prep.setString(19, pPlayer.getSpellbook().toJson());
		prep.setBoolean(20, pPlayer.isPrivate());
		prep.setBoolean(21, pPlayer.isAllowGui());
		prep.setString(22,
				Serializer.serializeUUIDList(pPlayer.getIgnored()));
		prep.setInt(23, pPlayer.getId());
		return prep;
	}
	
	public static PseudoPlayer getPlayer(UUID uuid) {
		Lostshard.log.warning("[PLAYER] Getting Player from DB!");
		PseudoPlayer pPlayer = null;
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement("SELECT * FROM players WHERE uuid=?");
			prep.setString(1, uuid.toString());
			prep.execute();
			while(prep.getResultSet().next()) {
				pPlayer = PlayerMapper.createPlayerFromResultSet(prep.getResultSet());
			}
			conn.close();
		} catch (final Exception e) {
			Lostshard.log.warning("[PLAYER] getPlayer mysql error, "+uuid);
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		if (pPlayer != null)
			Lostshard.log.finest("[PLAYER] got "
					+ Bukkit.getOfflinePlayer(uuid).getName()
					+ " players from DB.");
		return pPlayer;
	}
	
	public static void updatePlayer(PseudoPlayer pPlayer) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING PLAYER!");
		try {
			Connection conn = ds.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("UPDATE players SET money=?, bank=?, murderCounts=?, criminalTicks=?, globalChat=?, privateChat=?, subscribeDays=?, wasSubscribed=?, plotCreationPoints=?, chatChannel=?, mana=?, stamina=?, rank=?, spawnTick=?, currentBuild=?, titles=?, currentTitle=?, freeSkillPoints=?, spellbook=?, private=?, gui=?, ignored=? WHERE id=?; ");
			updatePreparedStatement(prep, pPlayer);
			prep.executeUpdate();
			conn.close();
			BuildMapper.updateBuilds(pPlayer.getBuilds());
			pPlayer.setUpdate(false);
		} catch (final Exception e) {
			Lostshard.log.warning("[PLAYER] updatePlayers mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static void updatePlayers(List<PseudoPlayer> pPlayers) {
		if (Lostshard.isDebug())
			System.out.print("UPDATING PLAYERS!");
		try {
			Connection conn = ds.getConnection();
			PreparedStatement prep = conn
					.prepareStatement("UPDATE players SET money=?, bank=?, murderCounts=?, criminalTicks=?, globalChat=?, privateChat=?, subscribeDays=?, wasSubscribed=?, plotCreationPoints=?, chatChannel=?, mana=?, stamina=?, rank=?, spawnTick=?, currentBuild=?, titles=?, currentTitle=?, freeSkillPoints=?, spellbook=?, private=?, gui=?, ignored=? WHERE id=?; ");
			for(PseudoPlayer pPlayer : pPlayers) {
				updatePreparedStatement(prep, pPlayer);
				prep.addBatch();
			}
			prep.executeBatch();
			conn.close();
			for(PseudoPlayer pPlayer : pPlayers) {
				BuildMapper.updateBuilds(pPlayer.getBuilds());
				pPlayer.setUpdate(false);
			}
		} catch (SQLException e) {
			Lostshard.log.warning("[PLAYER] updatePlayers mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
	}
	
	public static PseudoPlayer insertPlayer(PseudoPlayer pPlayer) {
		try {
			final Connection conn = ds.getConnection();
			final PreparedStatement prep = conn
					.prepareStatement(
							"INSERT IGNORE INTO players "
									+ "(uuid,bank,titles,spellbook,ignored) VALUES (?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			prep.setString(1, pPlayer.getPlayerUUID().toString());
			prep.setString(2, pPlayer.getBank().Serialize());
			prep.setString(3, Serializer.serializeStringArray(pPlayer.getTitels()));
			prep.setString(4, pPlayer.getSpellbook().toJson());
			prep.setString(5, Serializer.serializeUUIDList(pPlayer.getIgnored()));
			prep.execute();
			final ResultSet rs = prep.getGeneratedKeys();
			int id = 0;
			while (rs.next())
				id = rs.getInt(1);
			pPlayer.setId(id);
			conn.close();
			BuildMapper.insertBuild(pPlayer.getCurrentBuild(), pPlayer.getId());
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING,
					"[PLAYER] insertPlayer mysql error");
			Lostshard.mysqlError();
			if (Lostshard.isDebug())
				e.printStackTrace();
		}
		return pPlayer;
	}
}
