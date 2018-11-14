package com.lostshard.lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Skills.TamingSkill;
import com.sk89q.intake.Command;

public class TamingCommand {

	PlayerManager pm = PlayerManager.getManager();

	@Command(aliases = { "mount" }, desc = "Summons mount")
	public void mount(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		TamingSkill.callMount(player, pPlayer);
	}
}
