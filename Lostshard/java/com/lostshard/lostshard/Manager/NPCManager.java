package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Utils.Utils;

public class NPCManager {
	
	private static NPCManager manager = new NPCManager();
	PlotManager ptm = PlotManager.getManager();
	
	private NPCManager() {
	
	}

	public static NPCManager getManager() {
		return manager;
	}

	public List<NPC> getNpcs() {
		ArrayList<NPC> rs = new ArrayList<NPC>();
		for (Plot plot : ptm.getPlots())
			for (NPC npc : plot.getNpcs())
				rs.add(npc);
		return rs;
	}
	
	public NPC getVendor(Location location) {
		Plot plot = ptm.findPlotAt(location);
		for(NPC n : plot.getNpcs())
			if(n.getType().equals(NPCType.VENDOR) && Utils.isWithin(location, n.getLocation(), 5))
				return n;
		return null;
	}
	
	public NPC getNearstNPC(Location location) {
		double ndis = 9999999;
		NPC rs = null;
		for(NPC n : getNpcs()) {
			double dis = Utils.fastDistance(n.getLocation(), location);
			if(dis < ndis)
				rs = n;
		}
		return rs;
	}
	
	public List<NPC> getBankers() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : getNpcs())
			if (npc.getType().equals(NPCType.BANKER))
				result.add(npc);
		return result;
	}

	public List<NPC> getGuards() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : getNpcs())
			if (npc.getType().equals(NPCType.GUARD))
				result.add(npc);
		return result;
	}

	public List<NPC> getVendors() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : getNpcs())
			if (npc.getType().equals(NPCType.VENDOR))
				result.add(npc);
		return result;
	}
}
