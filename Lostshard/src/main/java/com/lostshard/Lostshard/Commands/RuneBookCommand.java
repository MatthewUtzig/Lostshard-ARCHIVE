package com.lostshard.Lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.CommandManager.Annotations.Sender;
import com.lostshard.CommandManager.Annotations.Vanish;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.Rune;
import com.lostshard.Lostshard.Objects.Player.Runebook;
import com.lostshard.Lostshard.Utils.Output;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;
import com.sk89q.intake.parametric.annotation.Text;

public class RuneBookCommand {

	PlayerManager pm = PlayerManager.getManager();
	
	@Command(aliases = { "page" }, desc = "")
	public void page(@Sender Player player, @Optional(value = "0") @Range(min = 0) int page) {
		Output.outputRunebook(player, page);
	}
	
	public void give(@Sender Player player, @Sender PseudoPlayer pPlayer, @Vanish Player target, @Text String rune) {
		final PseudoPlayer tpPlayer = this.pm.getPlayer(target);
		final Runebook runebook = pPlayer.getRunebook();
		final Runebook targetRunebook = tpPlayer.getRunebook();
		
		if (!target.isOp()
				&& !((tpPlayer.wasSubscribed() && targetRunebook.size() < 16)
				|| targetRunebook.size() < 8)) {
			Output.simpleError(player, target.getName() + " has too many runes.");
			return;
		}
		
		if(targetRunebook.contains(rune)) {
			Output.simpleError(player,
					target.getName() + " already has a rune with that label.");
			return;
		}
		
		Rune foundRune = runebook.get(rune);
		
		if(rune == null) {
			Output.simpleError(player, "Could not find a rune with that label.");
			return;
		}
		
		runebook.remove(foundRune);
		targetRunebook.add(foundRune);
		Output.positiveMessage(player, "You have given the rune " + foundRune.getLabel()
				+ " to " + target.getName());
		Output.positiveMessage(target,
				player.getName() + " has given you the rune " + foundRune.getLabel() + ".");
		
	}
	
	public void remove(@Sender Player player, @Sender PseudoPlayer pPlayer, @Text String rune) {
		final Runebook runebook = pPlayer.getRunebook();
		final Rune foundRune = runebook.get(rune);
		
		if(rune == null) {
			Output.simpleError(player, "Could not find a rune with that label.");
			return;
		}
		
		runebook.remove(foundRune);
		Output.positiveMessage(player, "You have removed the rune " + foundRune.getLabel());
	}
}
