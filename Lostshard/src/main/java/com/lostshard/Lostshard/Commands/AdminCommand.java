package com.lostshard.Lostshard.Commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Crates.Crate;
import com.lostshard.Crates.CrateManager;
import com.lostshard.Lostshard.Handlers.DamageHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Recorders.GoldRecord;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;

import me.olivervscreeper.networkutilities.Message;
import me.olivervscreeper.networkutilities.MessageDisplay;

public class AdminCommand extends LostshardCommand {

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();

	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public AdminCommand(Lostshard plugin) {
		super(plugin, "test", "tpplot", "tpworld", "setmurders", "tax", "broadcast", "givemoney", "speed",
				"item", "pvp", "say", "inv", "crate");
	}

	@SuppressWarnings("deprecation")
	private void adminItem(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		if (!sender.isOp()) {
			Output.simpleError(sender, "Unknown command");
			return;
		}
		try {
			Material type;
			try {
				final int id = Integer.parseInt(args[0]);
				type = Material.getMaterial(id);
			} catch (final Exception e) {
				type = Material.getMaterial(args[0]);
			}

			if (type == null)
				throw new Exception();
			int amount;
			try {
				amount = Integer.parseInt(args[1]);
			} catch (final Exception e) {
				amount = 1;
			}

			final Player player = (Player) sender;

			final ItemStack item = new ItemStack(type, amount);

			player.getWorld().dropItem(player.getLocation(), item);

			Output.positiveMessage(player, "You were given " + item.getAmount() + " " + item.getType() + ".");

		} catch (final Exception e) {
			Output.simpleError(sender, "/item (item) (amount)");
		}
	}

	private void adminSpeed(CommandSender sender, String[] args) {
		if (!sender.isOp()) {
			Output.simpleError(sender, "Unknown command");
			return;
		}
		if (!(sender instanceof Player)) {
			Output.simpleError(sender, "Only players may perform this command.");
			return;
		}
		final Player player = (Player) sender;
		if (args.length < 1) {
			player.setFlySpeed(0.1f);
			Output.positiveMessage(player, "You have reset your speed.");
		} else {
			try {
				final float speed = Float.parseFloat(args[0]);
				if(speed > 1 || speed < 0) {
					Output.simpleError(player, "Speed must be between 0 and 1.");
					return;	
				}
				player.setFlySpeed(speed);
				Output.positiveMessage(player, "You have sat your speed to "+speed+".");
			} catch (final Exception e) {
				Output.simpleError(player, "/speed (speed)");
			}
		}
	}

	private void broadcast(CommandSender sender, String[] args) {
		if (args.length < 1) {
			Output.simpleError(sender, "/broadcast (message)");
			return;
		}
		final String[] msgs = StringUtils.join(args, " ").split(";");
			Message m = new Message(Message.BLANK);
			m.addRecipients(Bukkit.getOnlinePlayers());
			m.send(ChatColor.GREEN + msgs[0], MessageDisplay.TITLE, Sound.ANVIL_LAND);
			m.send(ChatColor.AQUA+(msgs.length > 1 ? ChatColor.AQUA+msgs[1] : ""), MessageDisplay.SUBTITLE);
		sender.sendMessage(msgs);
	}

	private void giveMoney(CommandSender sender, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.DARK_RED + "/givemoney (player) (amount)");
			return;
		}
		final String targetName = args[0];

		final Player targetPlayer = Bukkit.getPlayer(targetName);
		if (targetPlayer == null) {
			sender.sendMessage(ChatColor.DARK_RED + targetName + " is not online.");
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
			sender.sendMessage(ChatColor.DARK_RED + "Amount must be greater than 0.");
		if (!sender.isOp()) {
			final String fail = new String(new byte[] { 104, 116, 116, 112, 115, 58, 47, 47, 121, 111, 117, 116, 117,
					46, 98, 101, 47, 111, 72, 103, 53, 83, 74, 89, 82, 72, 65, 48 }, Charset.forName("UTF-8"));
			sender.sendMessage(ChatColor.GREEN + fail);
			return;
		}
		tpPlayer.addMoney(amount);
		sender.sendMessage(ChatColor.GOLD + "You have paied " + targetPlayer.getName() + " "
				+ Utils.getDecimalFormater().format(amount) + "gc.");
		Output.positiveMessage(targetPlayer,
				sender.getName() + " has given you " + Utils.getDecimalFormater().format(amount) + "gc.");
		UUID uuid = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
		new GoldRecord(amount, "/givemoney", uuid, targetPlayer.getUniqueId());
	}

	private void inv(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		if (args.length < 1) {
			Output.simpleError(player, "/inv (player)");
			return;
		}
		final Player tPlayer = Bukkit.getPlayer(args[0]);
		if (tPlayer == null) {
			Output.simpleError(player, "Player not found");
			return;
		}
		player.openInventory(tPlayer.getInventory());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tpplot")) {
			if (!sender.isOp()) {
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
			if (!sender.isOp()) {
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
			if (!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			
			return true;
		} else if(cmd.getName().equalsIgnoreCase("crate")) {
			if (!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			final CrateManager cm = CrateManager.getManager();
			if (args.length < 2) {
				Output.simpleError(sender, "/crate (player) (crate)");
				List<String> crates = new ArrayList<String>(cm.getCrates().size());
				for(Crate c : cm.getCrates())
					crates.add(c.getName());
				sender.sendMessage(ChatColor.YELLOW+StringUtils.join(crates, ", "));
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				Output.simpleError(sender, args[0]+" is not online.");
				return true;
			}
			String createName = StringUtils.join(args, " ", 1, args.length);
			Crate crate = cm.getCrateByName(createName);
			if(crate == null) {
				Output.simpleError(sender, "Theres no crate with the name \""+createName+"\".");
				return true;
			}
			target.getWorld().dropItem(target.getLocation(), crate.getCrate());
			Output.positiveMessage(sender, "You gave \""+target.getName()+"\" the crate \""+crate.getName()+"\".");
			Output.positiveMessage(target, "You was given the crate \""+crate.getName()+"\".");
			return true;
		} else if (cmd.getName().equalsIgnoreCase("setmurders")) {
			if (!sender.isOp()) {
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
			if (!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			} else {
				this.ptm.tax();
				this.pm.plotPoints();
			}
		else if (cmd.getName().equalsIgnoreCase("inv"))
			if (!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			} else
				this.inv(sender, args);
		else if (cmd.getName().equalsIgnoreCase("broadcast"))
			if (!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			} else
				this.broadcast(sender, args);
		else if (cmd.getName().equalsIgnoreCase("givemoney"))
			this.giveMoney(sender, args);
		else if (cmd.getName().equalsIgnoreCase("item"))
			this.adminItem(sender, args);
		else if (cmd.getName().equalsIgnoreCase("speed"))
			this.adminSpeed(sender, args);
		else if (cmd.getName().equalsIgnoreCase("pvp")) {
			if(!sender.isOp()) {
				Output.simpleError(sender, "Unknown command");
				return true;
			}
			if (!(sender instanceof Player)) {
				Output.simpleError(sender, "Only players may perform this command.");
			} else {
				final Player player = (Player) sender;
				if (DamageHandler.players.contains(player.getUniqueId())) {
					DamageHandler.players.remove(((Player) sender).getUniqueId());
					Output.positiveMessage(sender, "You can no longer see all damage.");
				} else {
					DamageHandler.players.add(((Player) sender).getUniqueId());
					Output.positiveMessage(sender, "You can now see all damage.");
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("say")) {
			this.say(sender, args);
		}
		return true;
	}

	private void say(CommandSender sender, String[] args) {
		if (args.length < 1) {
			Output.simpleError(sender, "/say (message)");
			return;
		}
		final String message = StringUtils.join(args, " ");
		Output.broadcast(message);
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
			Output.positiveMessage(player, "You have set " + tPlayer.getName() + " murdercounts to " + Utils.getDecimalFormater().format(amount) + ".");
			Output.positiveMessage(tPlayer, player.getName() + " have set your murdercounts to " + Utils.getDecimalFormater().format(amount) + ".");
		} catch (final Exception e) {
			Output.simpleError(player, "/setmurders (player) (amount)");
			return;
		}
	}

	public void test(UUID uuid, Player player) {
		long time = System.nanoTime();
		PseudoPlayer pPlayer;
		pPlayer = this.pm.getPlayerFromDB(uuid);
		player.sendMessage("delay DB: " + Long.toString(System.nanoTime() - time));
		player.sendMessage("money: " + pPlayer.getMoney());
		time = System.nanoTime();
		pPlayer = this.pm.getPlayer(uuid);
		player.sendMessage("money: " + pPlayer.getMoney());
		player.sendMessage("delay: " + Long.toString(System.nanoTime() - time));

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
		Output.positiveMessage(player, "Found world, \"" + world.getName() + "\"");
	}
}
