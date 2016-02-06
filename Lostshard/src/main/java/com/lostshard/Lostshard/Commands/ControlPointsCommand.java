package com.lostshard.Lostshard.Commands;

import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.sk89q.intake.Command;

/**
 * @author Jacob Rosborg
 *
 */
public class ControlPointsCommand {
	
	@Command(aliases = { "claim" }, desc = "Claims the capturepoint")
	public void claim(@Sender Player player, @Sender PseudoPlayer pPlayer, @Sender Plot plot) {
		if (!plot.isCapturepoint()) {
			Output.simpleError(player, "This is not a capturepoint.");
			return;
		}
		final Clan clan = pPlayer.getClan();
		if (clan == null) {
			Output.simpleError(player, "You may only claim " + plot.getName() + " if you are in a clan.");
			return;

		}

		if (clan.equals(plot.getCapturepointData().getOwningClan())) {
			Output.simpleError(player, "Your clan already owns " + plot.getName());
			return;

		}

		if (!Utils.isWithin(player.getLocation(), plot.getCapturepointData().getCapZone().getLocation(), plot.getCapturepointData().getRange())) {
			Output.simpleError(player, "You may only claim " + plot.getName() + " if you are within the claim range.");
			return;
		}

		final long lastCaptureTime = plot.getCapturepointData().getLastCaptureDate();
		final Date date = new Date();
		final long curTime = date.getTime();
		long diff = curTime - lastCaptureTime;
		if (diff > 1000 * 60 * 60 * 1) {
			if (plot.getCapturepointData().isUnderAttack()) {
				Output.simpleError(player, plot.getName() + " is already under attack.");
				return;

			}
			if (clan.equals(plot.getCapturepointData().getOwningClan())) {
				Output.simpleError(player, "Your clan already owns " + plot.getName());
				return;

			}
			plot.getCapturepointData().beginCapture(player, pPlayer, clan);
		} else {
			diff = 1000 * 60 * 60 * 1 - diff;
			final int numHours = (int) ((double) diff / (1000 * 60 * 60));
			diff -= numHours * 60 * 60 * 1000;
			final int numMinutes = (int) ((double) diff / (1000 * 60));
			diff -= numMinutes * 60 * 1000;
			final int numSeconds = (int) ((double) diff / 1000);
			Output.simpleError(player, "can't claim " + plot.getName() + " yet, " + numHours + " hours, " + numMinutes
					+ " minutes and " + numSeconds + " seconds remaining.");
		}
		return;
	}

	@Command(aliases = { "capturepoints" }, desc = "Shows a list of all capturepoints")
	public void listCapturepoints(CommandSender sender) {
		Output.capturePointsInfo(sender);
	}
}
