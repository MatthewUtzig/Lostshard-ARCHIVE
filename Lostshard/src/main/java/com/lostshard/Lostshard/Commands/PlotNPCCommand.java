package com.lostshard.Lostshard.Commands;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.NPC.NPCType;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.Plot.PlotUpgrade;
import com.lostshard.Lostshard.Utils.Output;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Validate;

public class PlotNPCCommand {

	@Command(aliases = { "" }, desc = "")
	public void npc() {
		
	}
	
	@Command(aliases = { "list" }, desc = "")
	public void list(@Sender Player player, @Sender Plot plot) {
		player.sendMessage(ChatColor.GOLD + "-" + plot.getName() + "'s NPCs-");
		final Set<NPC> npcs = plot.getNpcs();
		final int numOfNpcs = npcs.size();

		if (numOfNpcs <= 0) {
			player.sendMessage(ChatColor.RED + "No NPCs");
			return;
		}
		for (final NPC npc : npcs)
			player.sendMessage(ChatColor.YELLOW + npc.getName() + " (" + npc.getType().toString() + ")");
		return;
	}
	
	
	@Command(aliases = { "hire" }, desc = "")
	public void hire(@Sender Player player, @Sender Plot plot, NPCType type, @Validate(regex="\\w{2,7}") String name) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may manage plot NPCs");
			return;
		}
		
		if(!(type.equals(NPCType.BANKER) && plot.getUpgrades().contains(PlotUpgrade.TOWN))) {
			Output.simpleError(player, "You can only place a banker in a town.");
			return;
		}
		
		if(!(type.equals(NPCType.GUARD) && player.isOp())) {
			Output.simpleError(player, "Sry can't do that");
			return;
		}
		
		int vendors = 0;
		
		for (final NPC npc : plot.getNpcs()) {
			if (npc.getName().equalsIgnoreCase(name)) {
				Output.simpleError(player, "An NPC with that name already exists.");
				return;
			}
			
			if (npc.getType().equals(NPCType.BANKER) && !player.isOp()) {
				Output.simpleError(player, "You may only have 1 banker per plot.");
				return;
			}
			
			if(npc.getType().equals(NPCType.VENDOR))
				vendors++;
		}
		
		if(vendors >= plot.getMaxVendors() && !player.isOp()) {
			Output.simpleError(player, "You may only have " + plot.getMaxVendors() + " vendors in your plot.");
			return;
		}
			
		
		final NPC npc = new NPC(type, name, player.getLocation(), plot.getId());
		plot.getNpcs().add(npc);
		plot.update();
		npc.spawn();
		Output.positiveMessage(player, "You have hired a "+type.name().toLowerCase()+" named " + name + ".");
	}
	
	@Command(aliases = { "fire" }, desc = "")
	public void fire(@Sender Player player, @Sender Plot plot, @Validate(regex="\\w{2,7}") String name) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may manage plot NPCs");
			return;
		}
		for (final NPC npc : plot.getNpcs())
			if (npc.getName().equalsIgnoreCase(name)) {
				npc.fire();
				Output.positiveMessage(player, "You have fired " + npc.getName() + ".");
				return;
			}
		Output.simpleError(player, "Can't find an NPC named " + name + " on this plot.");
	}
	
	@Command(aliases = { "move" }, desc = "")
	public void move(@Sender Player player, @Sender Plot plot, @Validate(regex="\\w{2,7}") String name) {
		if (!plot.isCoownerOrAbove(player)) {
			Output.simpleError(player, "Only the owner and co-owner may manage plot NPCs");
			return;
		}
		for (final NPC npc : plot.getNpcs())
			if (npc.getName().equalsIgnoreCase(name)) {
				npc.setLocation(player.getLocation());
				npc.move(player.getLocation());
				Output.positiveMessage(player, "You have moved " + name + ".");
				return;
			}
		Output.simpleError(player, "Can't find an NPC named " + name + " on this plot.");
	}
}