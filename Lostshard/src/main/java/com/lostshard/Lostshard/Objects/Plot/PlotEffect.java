package com.lostshard.Lostshard.Objects.Plot;

import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;

public enum PlotEffect {

	BLACKSMITH,
	VENDOR,
	STAMINA,
	MANA,
	SHIRINE
	;

	public static boolean hasEffect(Clan clan, PlotEffect effect) {
		for (Plot p : PlotManager.getManager().getCapturePoints())
			if(p.getCapturepointData().getOwningClan() == null)
				continue;
			else if(p.getCapturepointData().getOwningClan().equals(clan) && p.getEffects().contains(effect))
				return true;
		return false;
	}
	
}
