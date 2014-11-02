package com.lostshard.banManager.Handlers;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.lostshard.banManager.Objects.Ban;

public class BanHandler {

	public static ArrayList<Ban> bans = new ArrayList<Ban>();

	public static Ban banPlayer(OfflinePlayer player, Player by,
			long timeLength, String reason) {
		Ban ban = null;
		ban = new Ban(player.getUniqueId(), timeLength, reason,
				by == null ? null : by.getUniqueId());
		return ban;
	}

	public static void unban(OfflinePlayer player) {
		for (int i = 0; i < bans.size(); i++) {
			Ban ban = bans.get(i);
			if (ban.getBanned() == player.getUniqueId())
				bans.remove(ban);
		}
	}
}
