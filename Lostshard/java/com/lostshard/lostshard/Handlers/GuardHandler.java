package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;

public class GuardHandler {

	public static void Guard(Player player) {
		// Checking if the player are inside a plot
		final Plot plot = ptm.findPlotAt(player.getLocation());
		if (plot == null)
			return;
		// Checking to see if the player himself are criminal
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (pPlayer.isCriminal() || pPlayer.isMurderer())
			return;
		// Finding the nearest guard, on the same plot.
		NPC guard = null;
		for (final NPC g : plot.getNpcs()) {
			final Plot gP = ptm.findPlotAt(g.getLocation());
			if (guard == null
					|| gP.getLocation().distance(player.getLocation()) < guard
					.getLocation().distance(player.getLocation()))
				guard = g;
		}
		// Check if the plot is guarded
		if (guard == null)
			return;
		// Storing all criminals in range of the player
		final List<Player> criminals = new ArrayList<Player>();
		// Going trough all players and checking for who's in range and who's
		// criminal
		for (final Player pCP : Bukkit.getOnlinePlayers()) {
			final PseudoPlayer pPCP = pm.getPlayer(pCP);
			if (pPCP.isCriminal() || pPCP.isMurderer())
				criminals.add(pCP);
		}
		// // Slaying all criminals that are in range
		// for (Player c : criminals) {
		// NPCManager.getNPC(guard.getId()).teleport(c.getLocation(),
		// TeleportCause.PLUGIN);
		// c.damage(0d);
		// c.setHealth(0d);
		// }
		// NPCManager.getNPC(guard.getId()).teleport(guard.getLocation(),
		// TeleportCause.PLUGIN);
	}

	static PlayerManager pm = PlayerManager.getManager();

	static PlotManager ptm = PlotManager.getManager();

}
