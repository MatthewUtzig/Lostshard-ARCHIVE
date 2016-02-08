package com.lostshard.Lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.SurvivalismSkill;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Text;

public class SurvivalismCommand {
	
	@Command(aliases = { "track" }, desc = "Tracks players or monsters", usage = "<player|monster>")
	public void track(@Sender Player player, @Sender PseudoPlayer pPlayer, @Text String arg) {
		String[] args = arg.split(" ");
		SurvivalismSkill.track(player, args);
	}

	@Command(aliases = { "camp" }, desc = "Summons a camp fire")
	public void camp(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		SurvivalismSkill.camp(player);
	}
}