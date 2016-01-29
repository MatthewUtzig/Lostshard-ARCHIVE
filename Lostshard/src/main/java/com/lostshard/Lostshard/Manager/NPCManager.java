package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.NPC.NPCType;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Utils.Utils;

public class NPCManager {

	public static NPCManager getManager() {
		return manager;
	}

	private static NPCManager manager = new NPCManager();

	PlotManager ptm = PlotManager.getManager();

	private NPCManager() {

	}

	public NPC getBanker(Location location) {
		for (final NPC npc : this.getBankers())
			if (Utils.isWithin(location, npc.getLocation(),
					Variables.bankRadius))
				return npc;
		return null;
	}

	public List<NPC> getBankers() {
		final List<NPC> result = new ArrayList<NPC>();
		for (final NPC npc : this.getNpcs())
			if (npc.getType().equals(NPCType.BANKER))
				result.add(npc);
		return result;
	}

	public List<NPC> getGuards() {
		final List<NPC> result = new ArrayList<NPC>();
		for (final NPC npc : this.getNpcs())
			if (npc.getType().equals(NPCType.GUARD))
				result.add(npc);
		return result;
	}

	public NPC getNearstNPC(Location location) {
		final double ndis = 9999999;
		NPC rs = null;
		for (final NPC n : this.getNpcs()) {
			final double dis = Utils.fastDistance(n.getLocation(), location);
			if (dis < ndis)
				rs = n;
		}
		return rs;
	}

	public List<NPC> getNpcs() {
		final ArrayList<NPC> rs = new ArrayList<NPC>();
		for (final Plot plot : this.ptm.getPlots())
			for (final NPC npc : plot.getNpcs())
				rs.add(npc);
		return rs;
	}

	public NPC getVendor(Location location) {
		final Plot plot = this.ptm.findPlotAt(location);
		for (final NPC n : plot.getNpcs())
			if (n.getType().equals(NPCType.VENDOR)
					&& Utils.isWithin(location, n.getLocation(), 5))
				return n;
		return null;
	}

	public List<NPC> getVendors() {
		final List<NPC> result = new ArrayList<NPC>();
		for (final NPC npc : this.getNpcs())
			if (npc.getType().equals(NPCType.VENDOR))
				result.add(npc);
		return result;
	}
	
	public NPC getByUUID(UUID uuid) {
		for(NPC npc : getNpcs())
			if(npc.getUuid().equals(uuid))
				return npc;
		return null;
	}
	
	public NPC getByLocation(Location loc) {
		for(NPC npc : getNpcs())
			if(npc.getLocation().equals(loc))
				return npc;
		return null;
	}

	public void interac(PlayerInteractEntityEvent event) {
		if(!event.getRightClicked().hasMetadata("NPC"))
			return;
		Entity e = event.getRightClicked();
		Player player = event.getPlayer();
		NPC npc = getByLocation(e.getLocation());
		if(npc == null)
			return;
		if(npc.getType().equals(NPCType.BANKER)) {
			if(player.getItemInHand().getType().equals(Material.GOLD_INGOT))
				player.performCommand("/tradegold "+player.getItemInHand().getAmount());
			else
				player.performCommand("bank");
			return;
		}
		if(npc.getType().equals(NPCType.VENDOR)) {
			
			return;
		}
	}

	public void spawn() {
		for(Plot plot : ptm.getPlots())
			for(NPC npc : plot.getNpcs())
				npc.spawn();
	}
}
