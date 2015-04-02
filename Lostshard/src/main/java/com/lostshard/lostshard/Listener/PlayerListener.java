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
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lostshard.lostshard.Handlers.ChatHandler;
import com.lostshard.lostshard.Handlers.DeathHandler;
import com.lostshard.lostshard.Handlers.EnderdragonHandler;
import com.lostshard.lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.lostshard.Handlers.foodHealHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.FishingSkill;
import com.lostshard.lostshard.Spells.Structures.Gate;
import com.lostshard.lostshard.Utils.Output;

public class PlayerListener implements Listener {

	PlayerManager pm = PlayerManager.getManager();
	
	public PlayerListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerFishEvent(PlayerFishEvent event) {
		FishingSkill.onFish(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		EnderdragonHandler.respawnDragonCheck(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		PlotProtectionHandler.onButtonPush(event);
		PlotProtectionHandler.onPlayerInteract(event);
		Gate.onPlayerInteractEvent(event);
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
		pm.onPlayerLogin(event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Output.displayLoginMessages(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		PlotProtectionHandler.onPlotEnter(event);
		SpellManager.move(event);
		Gate.onPlayerMove(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitFill(PlayerBucketFillEvent event) {
		PlotProtectionHandler.onBuckitFill(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitEmpty(PlayerBucketEmptyEvent event) {
		PlotProtectionHandler.onBuckitEmpty(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
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
		PlotProtectionHandler.onPlayerInteractEntity(event);
	}
}
