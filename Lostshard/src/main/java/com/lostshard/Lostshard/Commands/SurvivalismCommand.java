package com.lostshard.Lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Skills.SurvivalismSkill;
import com.sk89q.intake.Command;

public class SurvivalismCommand {
	
	@Command(aliases = { "track" }, desc = "Tracks players or monsters", usage = "<player|monster>")
	public void track(@Sender Player player, String arg) {
		SurvivalismSkill.track(player, arg);
	}

	@Command(aliases = { "camp" }, desc = "Summons a camp fire")
	public void camp(@Sender Player player) {
		SurvivalismSkill.camp(player);
	}
}