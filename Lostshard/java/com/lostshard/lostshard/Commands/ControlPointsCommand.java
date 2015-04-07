package com.lostshard.lostshard.Commands;

import java.util.Date;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Plot.Capturepoint;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class ControlPointsCommand implements CommandExecutor, TabCompleter {

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();
	
	public ControlPointsCommand(Lostshard plugin) {
		plugin.getCommand("capturepoints").setExecutor(this);
		plugin.getCommand("claim").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("capturepoints")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			Output.capturePointsInfo(player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("claim")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			claim(player);
			return true;
		}
		return false;
	}

	private void claim(Player player) {
		Plot plot = ptm.findPlotAt(player.getLocation());
		if(plot == null || !plot.isCapturePoint()) {
			Output.simpleError(player, "This is not a capturepoint.");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Clan clan = pPlayer.getClan();
		if(clan == null) {
			Output.simpleError(player, "You may only claim "+plot.getName()+" if you are in a clan.");
			return;
			
		}
		
		Capturepoint cp = Capturepoint.getByName(plot.getName());
		if(cp == null && !Utils.isWithin(player.getLocation(), plot.getLocation(), 10) || cp != null && !Utils.isWithin(player.getLocation(), new Location(plot.getLocation().getWorld(), cp.getPoint().x, cp.getPoint().y, cp.getPoint().z), 10)) {
			Output.simpleError(player, "You may only claim "+plot.getName()+" if you are within the claim range.");
			return;
		}
		
		long lastCaptureTime = plot.getLastCaptureDate();
		Date date = new Date();
		long curTime = date.getTime();
		long diff = (curTime - lastCaptureTime);
		if(diff > 1000*60*60*1) {
			if(plot.isUnderAttack()) {
				Output.simpleError(player, plot.getName()+" is already under attack.");
				return;
				
			}
			if(clan.equals(plot.getOwningClan())) {
				Output.simpleError(player, "Your clan already owns "+plot.getName());
				return;
				
			}
    		plot.beginCapture(player, pPlayer, clan);
		}
		else {
			diff = 1000*60*60*1 - diff;
			int numHours = (int)((double)diff/(1000*60*60));
			diff -= numHours*60*60*1000;
			int numMinutes = (int)((double)diff/(1000*60));
			diff -= numMinutes*60*1000;
			int numSeconds = (int)((double)diff/(1000));
			Output.simpleError(player, "Cannot claim "+plot.getName()+" yet, "+numHours+" hours, "+numMinutes+" minutes and "+numSeconds+" seconds remaining.");
		}
    	return;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}

}
