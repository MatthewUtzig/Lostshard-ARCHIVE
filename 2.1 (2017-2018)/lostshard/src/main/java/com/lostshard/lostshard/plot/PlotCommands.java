package com.lostshard.lostshard.plot;

import org.bukkit.entity.Player;

import com.sk89q.intake.Command;

public class PlotCommands {
	
	
	@Command(aliases={"create"},
			desc="Create's a plot",
			help="Create's a plot with a given name",
			min=1,
			usage="<name>")
	public void create(Player player, String name) {
		
	}
}
