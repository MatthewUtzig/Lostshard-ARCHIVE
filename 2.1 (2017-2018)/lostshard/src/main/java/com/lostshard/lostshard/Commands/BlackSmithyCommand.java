package com.lostshard.lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Skills.BlackSmithySkill;
import com.sk89q.intake.Command;

public class BlackSmithyCommand {

	@Command(aliases = { "enhance" }, desc = "Enhance the tool in your hand")
	public void enhance(@Sender Player player) {
		BlackSmithySkill.enhance(player);
	}

	@Command(aliases = { "repair" }, desc = "Repairs the tool in your hand")
	public void repair(@Sender Player player) {
		BlackSmithySkill.repair(player);
	}

	@Command(aliases = { "smelt" }, desc = "Smelts the tool in your hand into resources")
	public void semt(@Sender Player player) {
		BlackSmithySkill.smelt(player);
	}

}
