package com.lostshard.lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Skills.FishingSkill;
import com.sk89q.intake.Command;

public class FishingCommand {

	
	@Command(aliases = { "boat" }, desc = "Calls a boat to your side")
	public void boat(@Sender Player player) {
		FishingSkill.callBoat(player);
	}
}
