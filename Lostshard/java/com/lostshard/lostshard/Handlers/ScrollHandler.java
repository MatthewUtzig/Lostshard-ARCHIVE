package com.lostshard.lostshard.Handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Factory.ScrollFactory;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;

public class ScrollHandler {

	public static void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntity().getKiller() == null)
			return;
		if(!(event.getEntity().getKiller() instanceof Player))
			return;
		if(Math.random() > .2)
			return;
		Player player = event.getEntity().getKiller();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Entity entity = event.getEntity();
		EntityType type = entity.getType();
		Scroll scroll = new ScrollFactory().getRandomScroll(type);
		if(scroll != null) {
			pPlayer.addScroll(scroll);
			Output.positiveMessage(player, "The "+type.name().toLowerCase()+" dropped a scroll of "+scroll.getName()+".");
			Database.insertScroll(scroll, pPlayer.getId());
		}
	}
	
	static PlayerManager pm = PlayerManager.getManager();
}
