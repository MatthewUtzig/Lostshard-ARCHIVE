package com.lostshard.Whitelist;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.lostshard.Lostshard.Main.Lostshard;

public class KeyPlayerListener implements Listener {
	
	KeyManager km = KeyManager.getManager();
	
	public KeyPlayerListener(Lostshard ls) {
		ls.getServer().getPluginManager().registerEvents(this, ls);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerLoginEvent event) {
		final boolean allowed = km.isWhitelisted(event.getPlayer().getUniqueId());
		if(!(allowed || event.getPlayer().isWhitelisted())) {
			event.disallow(Result.KICK_WHITELIST, ChatColor.RED+"You are not a beta tester \n "+ChatColor.GOLD+"http://lostshard.com/beta-tester");
		}
	}	
}
