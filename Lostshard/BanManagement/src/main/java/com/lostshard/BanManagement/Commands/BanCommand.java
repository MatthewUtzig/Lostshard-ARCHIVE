package com.lostshard.BanManagement.Commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.lostshard.BanManagement.Models.Reason;
import com.lostshard.CommandManager.Annotations.Sender;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Text;

public class BanCommand {

	@Command(aliases = { "ban" }, desc = "Bans a given player, only temporarary youll have to finish the ban online")
	public void ban(@Sender Player player, OfflinePlayer target, Reason reason, @Text String other) {
		
	}
	
	@Command(aliases = { "baninfo" }, desc = "Displays all ban related information")
	public void info() {
		
	}
}
