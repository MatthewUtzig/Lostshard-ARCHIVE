package com.lostshard.RPG.Plots;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

public class OrderCastleVault extends VaultPlot{

	public OrderCastleVault(String name, Location location, int radius,
			String owner, ArrayList<String> coOwners,
			ArrayList<String> friends, int money, boolean protect,
			boolean locked, boolean city,
			HashMap<String, LockedBlock> lockedBlocks, boolean friendBuild,
			ArrayList<PlotNPC> plotNPCs, int forSaleAmount,
			boolean kickUpgrade, boolean neutral, boolean explosionAllowed) {
		super(name, location, radius, owner, coOwners, friends, money, protect, locked,
				city, lockedBlocks, friendBuild, plotNPCs, forSaleAmount, kickUpgrade,
				neutral, explosionAllowed);

		_murdererStartString = "The Order Castle vault has been left open by an unattentive guard, the gold is vulnerable!";
		_murdererEndBadString = "The namby-pamby law abiders protected ";
	}

}
