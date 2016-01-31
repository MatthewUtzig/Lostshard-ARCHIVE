package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class PlayerManager {

	static PlayerManager manager = new PlayerManager();

	public static PlayerManager getManager() {
		return manager;
	}

	private List<PseudoPlayer> players = new ArrayList<PseudoPlayer>();

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
		return pPlayer;
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		final PseudoPlayer pPlayer = this.getPlayer(event.getPlayer());
		if (pPlayer.getParty() != null) {
			pPlayer.getParty().removeMember(event.getPlayer().getUniqueId());
			pPlayer.getParty().sendMessage(event.getPlayer().getName() + " has left the party.");
		}
		System.out.println("updateing on quit");
		pPlayer.save();
		System.out.println("updated on quit");
		this.players.remove(pPlayer);
	}

	public void setPlayers(List<PseudoPlayer> players) {
		this.players = players;
	}

	public void tick(double delta, long tick) {
		for (final PseudoPlayer pPlayer : this.players)
			pPlayer.tick(delta, tick);
	}
}
