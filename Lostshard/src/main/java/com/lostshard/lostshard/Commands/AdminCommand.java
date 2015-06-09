package com.lostshard.lostshard.Commands;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Database.Mappers.PlayerMapper;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Title;
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
		plugin.getCommand("tax").setExecutor(this);
		plugin.getCommand("inv").setExecutor(this);
		plugin.getCommand("broadcast").setExecutor(this);
		plugin.getCommand("givemoney").setExecutor(this);
	}

	private void adminInv(Player player, String[] args) {
		if (args.length < 2) {
			Output.simpleError(player, "/admin inv (Player)");
			return;
		}
		final Player tPlayer = Utils.getPlayer(player, args, 1);
		if (tPlayer == null) {
			Output.simpleError(player, "Player is not online");
			return;
		}
		player.openInventory(tPlayer.getInventory());
	}

	private void broadcast(CommandSender sender, String[] args) {
		if (args.length < 1) {
			Output.simpleError(sender, "/broadcast (message)");
			return;
		}
		final String[] msgs = StringUtils.join(args, " ").split(";");
		for (final Player p : Bukkit.getOnlinePlayers()) {
			Title.sendTitle(p, 15, 25, 15, ChatColor.GREEN + msgs[0],
					ChatColor.AQUA + (msgs.length > 1 ? msgs[1] : ""));
			p.playSound(p.getLocation(), Sound.ARROW_HIT, 1, 1);
		}
	}

	private void inv(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		final Player tPlayer = Bukkit.getPlayer(args[0]);
		if (tPlayer == null) {
			Output.plotNotIn(player);
			return;
		}
		player.openInventory(tPlayer.getInventory());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("admin")) {
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			if (!player.isOp()) {
				Output.simpleError(player,
						"Only operator may perform this command.");
				return true;
			}
			if (args.length < 1) {
				Output.simpleError(player, "/admin (subCommand)");
				return true;
			}
			final String subCommand = args[0];
			if (subCommand.equalsIgnoreCase("inv"))
				this.adminInv(player, args);
			else if (subCommand.equalsIgnoreCase("tpplot"))
				this.tpPlot(player, args);
		} else if (cmd.getName().equalsIgnoreCase("tpplot")) {
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.tpPlot(player, args);
		} else if (cmd.getName().equalsIgnoreCase("tpworld")) {
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.tpWorld(player, args);
		} else if (cmd.getName().equalsIgnoreCase("test")) {
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			final Player player = (Player) sender;
			final PseudoPlayer pPlayer = this.pm.getPlayer(player);
			for (final Scroll scroll : Scroll.values())
				pPlayer.addSpell(scroll);
			test(player.getUniqueId(), player);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("setmurders")) {
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.setMurder(player, args);
			return true;
		} else if (cmd.getName().equalsIgnoreCase("tax"))
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}else
				this.ptm.tax();
		else if (cmd.getName().equalsIgnoreCase("inv"))
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}else
				this.inv(sender, args);
		else if (cmd.getName().equalsIgnoreCase("broadcast"))
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}else
				this.broadcast(sender, args);
		else if(cmd.getName().equalsIgnoreCase("givemoney"))
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}else
				giveMoney(sender, args);
		return true;
	}

	private void test(UUID uuid, Player player) {
		long time = System.nanoTime();
		PseudoPlayer pPlayer;
		pPlayer = PlayerMapper.getPlayer(uuid);
		player.sendMessage("delay DB: "+Long.toString(System.nanoTime()-time));
		player.sendMessage("money: "+pPlayer.getMoney());
		time = System.nanoTime();
		pPlayer = pm.getPlayer(uuid);
		player.sendMessage("money: "+pPlayer.getMoney());
		player.sendMessage("delay: "+Long.toString(System.nanoTime()-time));
		
	}

	private void giveMoney(CommandSender sender, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.DARK_RED + "/givemoney (player) (amount)");
			return;
		}
		final String targetName = args[0];

		final Player targetPlayer = Bukkit.getPlayer(targetName);
		if (targetPlayer == null) {
			sender.sendMessage(ChatColor.DARK_RED + targetName
					+ " is not online.");
			return;
		}
		final PseudoPlayer tpPlayer = this.pm.getPlayer(targetPlayer);
		int amount;
		try {
			amount = Integer.parseInt(args[1]);
		} catch (final Exception e) {
			sender.sendMessage(ChatColor.DARK_RED + "/givemoney (player) (amount)");
			return;
		}
		if (amount < 1)
			sender.sendMessage(ChatColor.DARK_RED
					+ "Amount must be greater than 0.");
		if(!sender.isOp()) {
			String fail = new String(new byte[] {104,116,116,112,115,58,47,47,121,111,117,116,117,46,98,101,47,111,72,103,53,83,74,89,82,72,65,48}, Charset.forName("UTF-8"));
			sender.sendMessage(ChatColor.GREEN+fail);
			return;
		}
		tpPlayer.addMoney(amount);
		sender.sendMessage(ChatColor.GOLD + "You have paied "
				+ targetPlayer.getName() + " " + amount + "gc.");
		Output.positiveMessage(targetPlayer, sender.getName()
				+ " has given you " + amount + "gc.");
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}

	private void setMurder(Player player, String[] args) {
		if (args.length < 2) {
			Output.simpleError(player, "/setmurders (player) (amount)");
			return;
		}
		final Player tPlayer = Bukkit.getPlayer(args[0]);
		if (tPlayer == null) {
			Output.plotNotIn(player);
			return;
		}
		int amount;
		try {
			amount = Integer.parseInt(args[1]);
			final PseudoPlayer pPlayer = this.pm.getPlayer(tPlayer);
			pPlayer.setMurderCounts(amount);
			Output.positiveMessage(player, "You have set " + tPlayer.getName()
					+ " murdercounts to " + amount + ".");
			Output.positiveMessage(tPlayer, player.getName()
					+ " have set your murdercounts to " + amount + ".");
		} catch (final Exception e) {
			Output.simpleError(player, "/setmurders (player) (amount)");
			return;
		}
	}

	private void tpPlot(Player player, String[] args) {
		if (args.length < 1) {
			Output.simpleError(player, "/tpplot (plot)");
			return;
		}
		final String name = StringUtils.join(args, " ");
		final Plot plot = this.ptm.getPlot(name);
		if (plot == null) {
			Output.simpleError(player, "Coulden find plot, \"" + name + "\"");
			return;
		}
		player.teleport(plot.getLocation());
		Output.positiveMessage(player, "Found plot, \"" + plot.getName() + "\"");
	}

	public void tpWorld(Player player, String[] args) {
		if (args.length < 1) {
			Output.simpleError(player, "/tpworld (world)");
			return;
		}
		final String name = StringUtils.join(args, " ");
		World world = null;
		for (final World w : Bukkit.getWorlds())
			if (StringUtils.contains(w.getName(), name))
				world = w;
		if (world == null) {
			Output.simpleError(player, "Coulden find world, \"" + name + "\"");
			return;
		}
		player.teleport(world.getSpawnLocation());
		Output.positiveMessage(player, "Found world, \"" + world.getName()
				+ "\"");
	}

}
