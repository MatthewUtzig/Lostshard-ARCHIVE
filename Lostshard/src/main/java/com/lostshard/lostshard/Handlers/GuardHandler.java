package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Manager.TaskManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Tasks.DelayedTask;
import com.lostshard.lostshard.Tasks.GuardTask;
import com.lostshard.lostshard.Utils.Utils;

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
		final List<Player> criminals = new ArrayList<Player>();
		// Going trough all players and checking for who's in range and who's
		// criminal
		for (final Player pCP : Bukkit.getOnlinePlayers()) {
			if(Utils.isWithin(pCP.getLocation(), player.getLocation(), 10));
			final PseudoPlayer pPCP = pm.getPlayer(pCP);
			if (pPCP.isCriminal() || pPCP.isMurderer())
				criminals.add(pCP);
		}
		// Slaying all criminals that are in range
		guardPlayers(criminals, guard);
	}

	public static void guardPlayers(final List<Player> criminals, final NPC guard) {
		// Slaying all criminals that are in range
		for (int i=0; i<criminals.size(); i++) {
			Player c = criminals.get(i);
			tm.add(new GuardTask(guard, c.getUniqueId(), i*20+10));
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
			
			List<Player> criminals = new ArrayList<Player>();
			criminals.add(player);
			guardPlayers(criminals, guard);
		}
	}
	
	static PlayerManager pm = PlayerManager.getManager();
	static TaskManager tm = TaskManager.getManager();
	static PlotManager ptm = PlotManager.getManager();
}
