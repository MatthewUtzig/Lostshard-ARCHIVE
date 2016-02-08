package com.lostshard.Lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.TamingSkill;
import com.sk89q.intake.Command;

public class TamingCommand {

	PlayerManager pm = PlayerManager.getManager();

	@Command(aliases = { "mount" }, desc = "Summons mount")
	public void mount(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		TamingSkill.callMount(player, pPlayer);
	}
}
