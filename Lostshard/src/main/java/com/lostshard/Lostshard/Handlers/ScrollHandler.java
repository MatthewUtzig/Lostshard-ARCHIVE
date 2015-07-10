package com.lostshard.Lostshard.Handlers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

import com.lostshard.Lostshard.Database.Mappers.ScrollMapper;
import com.lostshard.Lostshard.Factory.ScrollFactory;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Output;

public class ScrollHandler {

	private static ScrollFactory sf = new ScrollFactory();
	
	public static void onEntityDeathEvent(EntityDeathEvent event) {
		if (event.getEntity().getKiller() == null)
			return;
		if (!(event.getEntity().getKiller() instanceof Player))
			return;
		if (Math.random() > .4)
			return;
		final Player player = event.getEntity().getKiller();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Entity entity = event.getEntity();
		final EntityType type = entity.getType();
		final Scroll scroll = sf.getRandomScroll(type);
		if (scroll != null) {
			pPlayer.addScroll(scroll);
			Output.positiveMessage(player, "The " + type.name().toLowerCase().replace("_", " ")
					+ " dropped a scroll of " + scroll.getName() + ".");
			ScrollMapper.insertScroll(scroll, pPlayer.getId());
		}
	}

	static PlayerManager pm = PlayerManager.getManager();
}
