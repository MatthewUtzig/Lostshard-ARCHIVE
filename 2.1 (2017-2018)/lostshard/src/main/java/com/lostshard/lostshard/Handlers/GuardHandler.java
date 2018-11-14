package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lostshard.lostshard.main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.TaskManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.plot.Plot;
import com.lostshard.lostshard.Tasks.DelayedTask;
import com.lostshard.lostshard.Tasks.GuardTask;
import com.lostshard.lostshard.Utils.Utils;

public class GuardHandler {

	static PlayerManager pm = PlayerManager.getManager();

	static TaskManager tm = TaskManager.getManager();

	static PlotManager ptm = PlotManager.getManager();

	public static void Guard(Player player) {
		// Checking if the player are inside a plot
		final Plot plot = ptm.findPlotAt(player.getLocation());
		if (plot == null)
			return;
		// Checking to see if the player himself are criminal
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (pPlayer.isCriminal() || pPlayer.isMurderer())
			return;

		if (player.hasMetadata("npc"))
			return;

		NPC guard = null;
		final int closests = 100;
		for (final NPC npc : plot.getNpcs()) {
			if (npc.getType().equals(NPCType.GUARD)
					&& Utils.fastDistance(npc.getLocation(), player.getLocation()) < closests)
				guard = npc;
		}

		if (guard == null)
			return;

		if (!guard.getLocation().equals(guard.getCitizensNPC().getEntity().getLocation()))
			return;

		for (final DelayedTask dt : TaskManager.getManager().getTasks())
			if (dt instanceof GuardTask) {
				GuardTask gt = (GuardTask) dt;
				if (player != null && gt != null && gt.getTarget() != null && gt.getTarget().equals(player.getUniqueId()))
					return;
			}

		// Finding the nearest guard, on the same plot.
		// Check if the plot is guarded
		// Storing all criminals in range of the player
		final List<UUID> criminals = new ArrayList<UUID>();
		// Going trough all players and checking for who's in range and who's
		// criminal
		for (final Player pCP : Bukkit.getOnlinePlayers()) {
			if (Utils.isWithin(pCP.getLocation(), player.getLocation(), 10))
				;
			if (Lostshard.isVanished(pCP))
				continue;
			final PseudoPlayer pPCP = pm.getPlayer(pCP);
			if (pPCP.isCriminal() || pPCP.isMurderer())
				criminals.add(pCP.getUniqueId());
		}
		if (criminals.isEmpty())
			return;
		// Slaying all criminals that are in range
		guardPlayers(criminals, guard);
	}

	public static void guardPlayers(final List<UUID> criminals, final NPC guard) {
		// Slaying all criminals that are in range
		for (int i = 0; i < criminals.size(); i++) {
			final UUID c = criminals.get(i);
			new GuardTask(guard, c, i * 20 + 10);
		}
		new GuardTask(guard, null, criminals.size() * 20 + 20);
	}

	public static void move(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (pPlayer.isMurderer() || pPlayer.isCriminal()) {
			final Plot plot = ptm.findPlotAt(player.getLocation());
			if (plot == null)
				return;
			NPC guard = null;
			final int closests = 49;
			for (final NPC npc : plot.getNpcs()) {
				if (npc.getType().equals(NPCType.GUARD)
						&& Utils.fastDistance(npc.getLocation(), player.getLocation()) < closests)
					guard = npc;
			}
			if (guard == null)
				return;

			final List<UUID> criminals = new ArrayList<UUID>();
			criminals.add(player.getUniqueId());
			guardPlayers(criminals, guard);
		}
	}
}
