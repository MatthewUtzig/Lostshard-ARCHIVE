package com.lostshard.Lostshard.Commands;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Capturepoint;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class ControlPointsCommand extends LostshardCommand {

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();

	public ControlPointsCommand(Lostshard plugin) {
		super(plugin, "capturepoints", "claim");
	}

	private void claim(Player player) {
		final Plot plot = this.ptm.findPlotAt(player.getLocation());
		if (plot == null || !plot.isCapturepoint()) {
			Output.simpleError(player, "This is not a capturepoint.");
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		final Clan clan = pPlayer.getClan();
		if (clan == null) {
			Output.simpleError(player, "You may only claim " + plot.getName()
					+ " if you are in a clan.");
			return;

		}

		if (clan.equals(plot.getCapturepointData().getOwningClan())) {
			Output.simpleError(player,
					"Your clan already owns " + plot.getName());
			return;

		}

		final Capturepoint cp = Capturepoint.getByName(plot.getName());
		if (cp == null
				&& !Utils.isWithin(player.getLocation(), plot.getLocation(), 5)
				|| cp != null
				&& !Utils
						.isWithin(
								player.getLocation(),
								new Location(plot.getLocation().getWorld(), cp
										.getPoint().x, cp.getPoint().y, cp
										.getPoint().z), 5)) {
			Output.simpleError(player, "You may only claim " + plot.getName()
					+ " if you are within the claim range.");
			return;
		}

		final long lastCaptureTime = plot.getCapturepointData().getLastCaptureDate();
		final Date date = new Date();
		final long curTime = date.getTime();
		long diff = curTime - lastCaptureTime;
		if (diff > 1000 * 60 * 60 * 1) {
			if (plot.getCapturepointData().isUnderAttack()) {
				Output.simpleError(player, plot.getName()
						+ " is already under attack.");
				return;

			}
			if (clan.equals(plot.getCapturepointData().getOwningClan())) {
				Output.simpleError(player,
						"Your clan already owns " + plot.getName());
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
			Output.simpleError(player, "can't claim " + plot.getName()
					+ " yet, " + numHours + " hours, " + numMinutes
					+ " minutes and " + numSeconds + " seconds remaining.");
		}
		return;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("capturepoints")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			Output.capturePointsInfo(player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("claim")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.claim(player);
			return true;
		}
		return false;
	}
}
