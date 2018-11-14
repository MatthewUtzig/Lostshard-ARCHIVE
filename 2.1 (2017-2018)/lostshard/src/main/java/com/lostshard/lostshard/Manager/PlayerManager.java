package com.lostshard.lostshard.Manager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.lostshard.lostshard.main.Lostshard;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Objects.Recorders.ConnectionRecord;
import com.lostshard.lostshard.Objects.Recorders.UsernameUUIDRecord;
import com.lostshard.lostshard.Utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class PlayerManager {

	static PlayerManager manager = new PlayerManager();

	public static PlayerManager getManager() {
		return manager;
	}

	private List<PseudoPlayer> players = new LinkedList<PseudoPlayer>();
	
	private Set<UUID> criminals = new HashSet<UUID>();

	public PseudoPlayer getPlayer(OfflinePlayer player) {
		return this.getPlayer(player.getUniqueId());
	}

	public PseudoPlayer getPlayer(OfflinePlayer player, boolean create) {
		return this.getPlayer(player.getUniqueId(), create);
	}

	public PseudoPlayer getPlayer(Player player) {
		return this.getPlayer(player.getUniqueId());
	}

	public PseudoPlayer getPlayer(Player player, boolean create) {
		return this.getPlayer(player.getUniqueId(), create);
	}

	public PseudoPlayer getPlayer(UUID uuid) {
		return this.getPlayer(uuid, false);
	}

	public PseudoPlayer getPlayer(UUID uuid, boolean create) {
		if (uuid == null)
			return null;
		for (final PseudoPlayer pPlayer : this.players)
			if (pPlayer.getPlayerUUID().equals(uuid))
				return pPlayer;
		PseudoPlayer pPlayer = this.getPlayerFromDB(uuid);
		if (pPlayer == null && create) {
			pPlayer = new PseudoPlayer(uuid);
			try {
				pPlayer.insert();
			} catch (final Exception e) {
				Bukkit.getPlayer(uuid).kickPlayer(ChatColor.RED + "Something is wrong. We are working on it.");
				e.printStackTrace();
			}
		}
		return pPlayer;
	}

	public PseudoPlayer getPlayerFromDB(UUID uuid) {
		final Session s = Lostshard.getSession();
		PseudoPlayer pPlayer = null;
		try {
			pPlayer = (PseudoPlayer) s.createCriteria(PseudoPlayer.class).add(Restrictions.eq("playerUUID", uuid))
					.uniqueResult();
			s.close();
		} catch (final Exception e) {
			Bukkit.getPlayer(uuid).kickPlayer(ChatColor.RED + "Something is wrong. We are working on it.");
			e.printStackTrace();
			s.close();
		}
		return pPlayer;
	}

	public List<PseudoPlayer> getPlayers() {
		return this.players;
	}

	public PseudoPlayer onPlayerLogin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = this.getPlayer(player, true);
		this.players.add(pPlayer);
		player.setDisplayName(Utils.getDisplayName(player));
		new UsernameUUIDRecord(player.getUniqueId(), event.getPlayer().getName());
		new ConnectionRecord(player.getUniqueId(), player.getAddress().getAddress().getHostAddress());
		return pPlayer;
	}

	public void onPlayerQuit(Player player) {
		final PseudoPlayer pPlayer = this.getPlayer(player);
		if (pPlayer.getParty() != null) {
			pPlayer.getParty().getMembers().remove(player);
			pPlayer.getParty().sendMessage(player.getName() + " has left the party.");
		}
		pPlayer.save();
		this.players.remove(pPlayer);
		
		Session s = Lostshard.getSession();
		
		try {
			Transaction t = s.beginTransaction();
			t.begin();
			
			SQLQuery q = s.createSQLQuery("UPDATE ConnectionRecord SET left_date = NOW() WHERE player='"+player.getUniqueId().toString()+"' ORDER BY id DESC LIMIT 1");
			
			q.executeUpdate();
			
			t.commit();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
			s.close();
		}
	}

	public void setPlayers(List<PseudoPlayer> players) {
		this.players = players;
	}

	public void tick(double delta, long tick) {
		for (final PseudoPlayer pPlayer : this.players)
			pPlayer.tick(delta, tick);
	}

	public void plotPoints() {
		for(PseudoPlayer p : this.players) {
			p.setPlotCreatePoints(p.getPlotCreatePoints()-1);
		}
		Session s = Lostshard.getSession();
		try {
			Transaction t = s.beginTransaction();
			Query q = s.createQuery("UPDATE PseudoPlayer p SET p.plotCreatePoints = p.plotCreatePoints-1 WHERE p.plotCreatePoints > 0;");
			q.executeUpdate();
			q = s.createQuery("UPDATE PseudoPlayer p SET p.murderCounts = p.murderCounts-1 WHERE p.murderCounts > 0 AND p.murderCounts < 20");
			q.executeUpdate();
			t.commit();
			
			try {
				t = s.beginTransaction();
				q = s.createQuery("SELECT p.playerUUID FROM PseudoPlayer p WHERE p.criminal > 0 OR p.murderCounts >= 5");
				@SuppressWarnings("unchecked")
				List<UUID> results = q.list();
				setCriminals(results);
				t.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			s.close();
			e.printStackTrace();
		}
	}

	/**
	 * @return the criminals
	 */
	public Set<UUID> getCriminals() {
		return criminals;
	}

	/**
	 * @param criminals the criminals to set
	 */
	public void setCriminals(Set<UUID> criminals) {
		this.criminals = criminals;
	}
	
	/**
	 * @param criminals the criminals to set
	 */
	public void setCriminals(List<UUID> criminals) {
		this.criminals = new HashSet<UUID>(criminals);
	}

	public boolean isCriminal(UUID uuid) {
		if(criminals.contains(uuid))
			return true;
		return false;
	}
}
