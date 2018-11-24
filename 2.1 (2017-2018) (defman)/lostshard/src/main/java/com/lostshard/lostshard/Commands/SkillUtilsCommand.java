package com.lostshard.lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Skills.Build;
import com.lostshard.lostshard.Skills.Skill;
import com.lostshard.lostshard.Utils.Output;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;

public class SkillUtilsCommand {

	static PlayerManager pm = PlayerManager.getManager();

	@Command(aliases = { "meditate" }, desc = "Regen mana faster")
	public void playerMeditate(@Sender Player player) {
		Output.positiveMessage(player, "You begin meditating...");
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setMeditating(true);
	}

	@Command(aliases = { "rest" }, desc = "Regens stamina faster")
	public void playerRest(@Sender Player player) {
		Output.positiveMessage(player, "You begin resting...");
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setResting(true);
	}

	@Command(aliases = { "resetallskills" }, desc = "Resets all your skills and increase on to level 50", usage = "<skill>")
	public void resetallskills(@Sender Player player, @Optional String name) {
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (name == null) {
			pPlayer.getBuilds().set(pPlayer.getCurrentBuildId(), new Build());
			Output.positiveMessage(player, "Skills wiped, but you diden chose a skill to increase.");
		} else {
			final Build build = new Build();
			final Skill skill = build.getSkillByName(name);
			if (skill == null) {
				Output.simpleError(player, "You chose a invalid skill to increase.");
				return;
			}
			pPlayer.getBuilds().set(pPlayer.getCurrentBuildId(), build);
			skill.setLvl(500);
			Output.positiveMessage(player, "Skills wiped, " + skill.getName() + " set to 50.0");
		}
	}
}
