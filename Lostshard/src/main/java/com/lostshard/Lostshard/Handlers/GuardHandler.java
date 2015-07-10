package com.lostshard.Lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Manager.TaskManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.NPC.NPCType;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Tasks.DelayedTask;
import com.lostshard.Lostshard.Tasks.GuardTask;
import com.lostshard.Lostshard.Utils.Utils;

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
		
		NPC guard = null;
		double closests = 999999;
		for(NPC npc : plot.getNpcs()) {
			if(npc.getType().equals(NPCType.GUARD) && Utils.fastDistance(npc.getLocation(), player.getLocation()) < closests)
				guard = npc;
		}
		
		if(guard == null)
			return;
		
		if(!guard.getLocation().equals(guard.getCitizensNPC().getEntity().getLocation()))
			return;
	
		for(DelayedTask dt : TaskManager.getManager().getTasks())
			if(dt instanceof GuardTask)
				if(((GuardTask)dt).getTarget().equals(player.getUniqueId()))
					return;
		
		// Finding the nearest guard, on the same plot.
		// Check if the plot is guarded
		// Storing all criminals in range of the player
		final List<UUID> criminals = new ArrayList<UUID>();
		// Going trough all players and checking for who's in range and who's
		// criminal
		for (final Player pCP : Bukkit.getOnlinePlayers()) {
			if(Utils.isWithin(pCP.getLocation(), player.getLocation(), 10));
			final PseudoPlayer pPCP = pm.getPlayer(pCP);
			if (pPCP.isCriminal() || pPCP.isMurderer())
				criminals.add(pCP.getUniqueId());
		}
		if(criminals.isEmpty())
			return;
		// Slaying all criminals that are in range
		guardPlayers(criminals, guard);
	}

	public static void guardPlayers(final List<UUID> criminals, final NPC guard) {
		// Slaying all criminals that are in range
		for (int i=0; i<criminals.size(); i++) {
			UUID c = criminals.get(i);
			tm.add(new GuardTask(guard, c, i*20+10));
		 }
		tm.add(new GuardTask(guard, null, criminals.size()*20+20));
	}
	
	public static void move(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.isMurderer() || pPlayer.isCriminal()) {
			Plot plot = ptm.findPlotAt(player.getLocation());
			if(plot == null)
				return;
			NPC guard = null;
			double closests = 26;
			for(NPC npc : plot.getNpcs()) {
				if(npc.getType().equals(NPCType.GUARD) && Utils.fastDistance(npc.getLocation(), player.getLocation()) < closests)
					guard = npc;
			}
			if(guard == null)
				return;
			
			List<UUID> criminals = new ArrayList<UUID>();
			criminals.add(player.getUniqueId());
			guardPlayers(criminals, guard);
		}
	}
	
	static PlayerManager pm = PlayerManager.getManager();
	static TaskManager tm = TaskManager.getManager();
	static PlotManager ptm = PlotManager.getManager();
}
