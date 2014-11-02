package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCType;

public class NPCHandler {

	public static List<NPC> getBankers() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : Lostshard.getNpcs())
			if (npc.getType().equals(NPCType.BANKER))
				result.add(npc);
		return result;
	}

	public static List<NPC> getGuards() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : Lostshard.getNpcs())
			if (npc.getType().equals(NPCType.GUARD))
				result.add(npc);
		return result;
	}

	public static List<NPC> getVendors() {
		List<NPC> result = new ArrayList<NPC>();
		for (NPC npc : Lostshard.getNpcs())
			if (npc.getType().equals(NPCType.VENDOR))
				result.add(npc);
		return result;
	}

	public static int getNextId() {
		int result = 0;
		for (NPC npc : Lostshard.getNpcs())
			if (npc.getId() > 0)
				result = npc.getId() + 1;
		return result;
	}

}
