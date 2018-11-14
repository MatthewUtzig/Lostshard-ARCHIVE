package com.lostshard.lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Text;
import com.sk89q.intake.parametric.annotation.Validate;

public class TitleCommand {

	PlayerManager pm = PlayerManager.getManager();
	
	@Command(aliases = { "", "list", "set" }, desc = "List all titles, or sets the title", usage = "<title>")
	public void title(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Text String title) {
		PseudoPlayer tpPlayer = pm.getPlayer(player);
		
		if(title != null && tpPlayer.getTitles().set(title)) {
			Output.positiveMessage(player, "You have successfully selected the title \""+title+"\".");
		} else {
			if(title != null)
				Output.simpleError(player, "Your titles does not contain\""+title+"\".");
			Output.displayTitles(player, player);
		}
	}
	
	@Command(aliases = { "remove" }, desc = "Removes a title for a given player", usage = "<player> <title>")
	@Require("lostshard.admin.title.remove")
	public void remove(@Sender Player player, Player target, @Optional @Text String title) {
		PseudoPlayer tpPlayer = pm.getPlayer(target);
		
		if(title != null && tpPlayer.getTitles().contains(title)) {
			Output.positiveMessage(player, "You have successfully removed the title \""+title+"\" from \""+target.getName()+"\".");
			tpPlayer.setCurrentTitleId(-1);
			tpPlayer.getTitles().remove(title);
		} else {
			if(title != null)
				Output.simpleError(player, "\""+target.getName()+"\" titles does not contain\""+title+"\".");
			Output.displayTitles(player, target);
		}
	}
	
	@Command(aliases = { "give" }, desc = "Removes a title for a given player", usage = "<player> <title>")
	@Require("lostshard.admin.title.give")
	public void give(@Sender Player player, Player target, @Optional @Validate(regex="\\w{2,10}") @Text String title) {
		PseudoPlayer tpPlayer = pm.getPlayer(target);
		
		if(title != null && !tpPlayer.getTitles().contains(title)) {
			Output.positiveMessage(player, "You have successfully added the title \""+title+"\" from \""+target.getName()+"\".");
			tpPlayer.setCurrentTitleId(-1);
			tpPlayer.getTitles().add(title);
		} else {
			if(title != null)
				Output.simpleError(player, "\""+target.getName()+"\" titles already contain\""+title+"\".");
			Output.displayTitles(player, target);
		}
	}
}
