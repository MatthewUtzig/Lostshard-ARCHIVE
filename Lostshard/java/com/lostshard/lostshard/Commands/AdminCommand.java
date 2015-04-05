package com.lostshard.lostshard.Commands;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class AdminCommand implements CommandExecutor, TabCompleter {

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public AdminCommand(Lostshard plugin) {
		plugin.getCommand("admin").setExecutor(this);
		plugin.getCommand("test").setExecutor(this);
		plugin.getCommand("tpplot").setExecutor(this);
		plugin.getCommand("tpworld").setExecutor(this);
		plugin.getCommand("setmurders").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("admin")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			if (!player.isOp()) {
				Output.simpleError(player, "Only operator may perform this command.");
				return true;
			}
			if (args.length < 1) {
				Output.simpleError(player, "/admin (subCommand)");
				return true;
			}
			String subCommand = args[0];
			if (subCommand.equalsIgnoreCase("inv")) {
				adminInv(player, args);
			} else if (subCommand.equalsIgnoreCase("tpplot")) {
				tpPlot(player, args);
			}
		}else if(cmd.getName().equalsIgnoreCase("tpplot")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			tpPlot(player, args);
		}else if(cmd.getName().equalsIgnoreCase("tpworld")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			tpWorld(player, args);
		}else if(cmd.getName().equalsIgnoreCase("test")) {
			Player player = (Player) sender;
			PseudoPlayer pPlayer = pm.getPlayer(player);
			for(Scroll scroll : Scroll.values())
				pPlayer.addSpell(scroll);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("setmurders")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			setMurder(player, args);
			return true;
		}
		return true;
	}

	private void setMurder(Player player, String[] args) {
		if(args.length < 2) {
			Output.simpleError(player, "/setmurders (player) (amount)");
			return;
		}
		Player tPlayer = Bukkit.getPlayer(args[0]);
		if(tPlayer == null) {
			Output.plotNotIn(player);
			return;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[1]);
			PseudoPlayer pPlayer = pm.getPlayer(tPlayer);
			pPlayer.setMurderCounts(amount);
			Output.positiveMessage(player, "You have set "+tPlayer.getName()+" murdercounts to "+amount+".");
			Output.positiveMessage(tPlayer, player.getName()+" have set your murdercounts to "+amount+".");
		} catch (Exception e) {
			Output.simpleError(player, "/setmurders (player) (amount)");
			return;
		}
	}

	private void tpPlot(Player player, String[] args) {
		if(args.length < 1) {
			Output.simpleError(player, "/tpplot (plot)");
			return;
		}
		String name = StringUtils.join(args, " ");
		Plot plot = ptm.getPlot(name);
		if(plot == null) {
			Output.simpleError(player, "Coulden find plot, \""+name+"\"");
			return;
		}
		player.teleport(plot.getLocation());
		Output.positiveMessage(player, "Found plot, \""+plot.getName()+"\"");
	}
	
	public void tpWorld(Player player, String[] args) {
		if(args.length < 1) {
			Output.simpleError(player, "/tpworld (world)");
			return;
		}
		String name = StringUtils.join(args, " ");
		World world = null;
		for(World w : Bukkit.getWorlds())
			if(StringUtils.contains(w.getName(), name))
				world = w;
		if(world == null) {
			Output.simpleError(player, "Coulden find world, \""+name+"\"");
			return;
		}
		player.teleport(world.getSpawnLocation());
		Output.positiveMessage(player, "Found world, \""+world.getName()+"\"");
	}

	private void adminInv(Player player, String[] args) {
		if (args.length < 2) {
			Output.simpleError(player, "/admin inv (Player)");
			return;
		}
		Player tPlayer = Utils.getPlayer(player, args, 1);
		if (tPlayer == null) {
			Output.simpleError(player, "Player is not online");
			return;
		}
		player.openInventory(tPlayer.getInventory());
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}

}
