package com.lostshard.lostshard.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Handlers.DamageHandler;
import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.plot.Plot;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;
import com.sk89q.intake.parametric.annotation.Text;

import me.olivervscreeper.networkutilities.Message;
import me.olivervscreeper.networkutilities.MessageDisplay;

public class AdminCommand {

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();


	@SuppressWarnings("deprecation")
	@Command(aliases = { "item", "i" }, desc = "Gives you an item", usage = "<item> (amount)")
	@Require("losthsard.admin.item")
	public void adminItem(@Sender Player player, String arg, @Optional(value="1") int amount) {
		try {
			Material type;
			try {
				final int id = Integer.parseInt(arg);
				type = Material.getMaterial(id);
			} catch (final Exception e) {
				type = Material.getMaterial(arg);
			}

			if (type == null)
				throw new Exception();

			final ItemStack item = new ItemStack(type, amount);

			player.getWorld().dropItem(player.getLocation(), item);

			Output.positiveMessage(player, "You were given " + item.getAmount() + " " + item.getType() + ".");

		} catch (final Exception e) {
			Output.simpleError(player, "/item (item) (amount)");
		}
	}

	@Command(aliases = { "speed" }, desc = "Sets your fly speed", usage = "<speed>")
	@Require("lostshard.admin.speed")
	public void adminSpeed(@Sender Player player, @Optional(value = "0.1") @Range(min=0, max=1) float speed) {
		player.setFlySpeed(speed);
		Output.positiveMessage(player, "You have sat your speed to "+speed+".");
	}

	@Command(aliases = { "broadcast" }, desc = "Broad cast a given message", usage = "<message>")
	@Require("lostshard.admin.broadcast")
	public void broadcast(CommandSender sender, @Text String message) {
		final String[] msgs = message.split(";");
			Message m = new Message(Message.BLANK);
			m.addRecipients(Bukkit.getOnlinePlayers());
			m.send(ChatColor.GREEN + msgs[0], MessageDisplay.TITLE, Sound.ANVIL_LAND);
			m.send(ChatColor.AQUA+(msgs.length > 1 ? ChatColor.AQUA+msgs[1] : ""), MessageDisplay.SUBTITLE);
		sender.sendMessage(msgs);
	}

	@Command(aliases = { "givemoney" }, desc = "Gives a given player a given amount of money", usage = "<player> <amount>")
	@Require("lostshard.admin.givemoney")
	public void giveMoney(CommandSender sender, Player target, @Range(min=1) int amount) {
		final PseudoPlayer tpPlayer = this.pm.getPlayer(target);
		tpPlayer.getWallet().add(null, amount, "From admin!");
		sender.sendMessage(ChatColor.GOLD + "You have paied " + target.getName() + " "
				+ Utils.getDecimalFormater().format(amount) + "gc.");
		Output.positiveMessage(target,
				sender.getName() + " has given you " + Utils.getDecimalFormater().format(amount) + "gc.");
	}

	@Command(aliases = { "inv" }, desc = "Opens a given players inventory", usage = "<player>")
	@Require("lostshard.admin.inv")
	public void inv(@Sender Player player, Player target) {
		player.openInventory(target.getInventory());
	}
	
	@Command(aliases = { "pvp" }, desc = "Toggles whether to show damage results")
	@Require("lostshard.admin.pvp")
	public void pvp(@Sender Player player) {
		if (DamageHandler.players.contains(player.getUniqueId())) {
			DamageHandler.players.remove(player);
			Output.positiveMessage(player, "You can no longer see all damage.");
		} else {
			DamageHandler.players.add(player.getUniqueId());
			Output.positiveMessage(player, "You can now see all damage.");
		}
	}
	
	@Command(aliases = { "tax" }, desc = "Collect taxes")
	@Require("lostshard.admin.tax")
	public void tax(CommandSender sender) {
		this.ptm.tax();
		this.pm.plotPoints();
	}

	@Command(aliases = { "say" }, desc = "Broadcast a message", usage = "<message>")
	@Require("lostshard.admin.say")
	public void say(CommandSender sender, @Text String message) {
		Output.broadcast(message);
	}

	@Command(aliases = { "setmurders" }, desc = "Set murders counts for a player", usage = "<player> <amount>")
	@Require("lostshard.admin.setmurders")
	public void setMurder(CommandSender sender, Player target, @Range(min=0) int amount) {
		final PseudoPlayer pPlayer = this.pm.getPlayer(target);
		pPlayer.setMurderCounts(amount);
		Output.positiveMessage(sender, "You have set " + target.getName() + " murdercounts to " + Utils.getDecimalFormater().format(amount) + ".");
		Output.positiveMessage(target, sender.getName() + " have set your murdercounts to " + Utils.getDecimalFormater().format(amount) + ".");
	}

	@Command(aliases = { "test" }, desc = "Fast test command")
	@Require("lostshard.admin.test")
	public void test(@Sender Player player) {
		return;
	}

	@Command(aliases = { "tpplot" }, desc = "Teleports you to a given plot", usage="<plot>")
	@Require("lostshard.admin.tpplot")
	public void tpPlot(@Sender Player player, @Text String name) {
		final Plot plot = this.ptm.getPlot(name);
		if (plot == null) {
			Output.simpleError(player, "Coulden find plot, \"" + name + "\"");
			return;
		}
		player.teleport(plot.getLocation());
		Output.positiveMessage(player, "Found plot, \"" + plot.getName() + "\"");
	}

	@Command(aliases = { "tpworld" }, desc = "Teleports you to a given world", usage ="<world>")
	@Require("lostshard.admin.tpworld")
	public void tpWorld(@Sender Player player, @Text String name) {
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
