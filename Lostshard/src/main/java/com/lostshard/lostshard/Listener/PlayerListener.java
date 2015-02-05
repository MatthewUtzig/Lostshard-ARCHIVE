package com.lostshard.lostshard.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lostshard.lostshard.Handlers.ChatHandler;
import com.lostshard.lostshard.Handlers.DeathHandler;
import com.lostshard.lostshard.Handlers.EnderdragonHandler;
import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Handlers.foodHealHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class PlayerListener implements Listener {

	public PlayerListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		EnderdragonHandler.respawnDragonCheck(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		PlotHandler.onButtonPush(event);
		PlotHandler.onPlayerInteract(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		ChatHandler.onPlayerChat(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(Lostshard.isMysqlError()) {
			event.setKickMessage(ChatColor.RED+"Something is wrong. We are working on it.");
			event.setResult(Result.KICK_OTHER);
			return;
		}
		PseudoPlayerHandler.onPlayerLogin(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		PlotHandler.onPlotEnter(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitFill(PlayerBucketFillEvent event) {
		PlotHandler.onBuckitFill(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitEmpty(PlayerBucketEmptyEvent event) {
		PlotHandler.onBuckitEmpty(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if(event.getInventory().getTitle().equals(pPlayer.getBank().getInventory().getTitle()))
			pPlayer.update();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		foodHealHandler.foodHeal(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		DeathHandler.handleDeath(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		PlotHandler.onPlayerInteractEntity(event);
	}
}
