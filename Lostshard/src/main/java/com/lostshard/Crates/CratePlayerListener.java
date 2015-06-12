package com.lostshard.Crates;

import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Utils.Output;

public class CratePlayerListener implements Listener {

	CrateManager cm = CrateManager.getManager();
	
	public CratePlayerListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		if((action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && player.getItemInHand() != null) {
			Crate crate = cm.getCrate(player.getItemInHand());
			if(crate != null) {
				player.getLocation().getWorld().playEffect(player.getLocation().add(0, 2, 0), Effect.HEART, 0);
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				ItemStack key = player.getItemInHand();
				if(key.getAmount() < 2)
					player.getInventory().remove(key);
				else
					key.setAmount(key.getAmount()-1);
				ItemStack item = crate.getItem();
				player.getLocation().getWorld().dropItem(player.getLocation(), item);
				Output.positiveMessage(player, "You have received "+item.getAmount()+" "+item.getType().name().toLowerCase().replace("_", " ")+"'s.");
				event.setCancelled(true);
			}
		}
	}
	
}
