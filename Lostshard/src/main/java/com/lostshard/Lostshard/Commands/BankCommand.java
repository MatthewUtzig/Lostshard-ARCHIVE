package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.NPCManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.TabUtils;
import com.lostshard.Lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class BankCommand extends LostshardCommand {

	PlayerManager pm = PlayerManager.getManager();
	NPCManager npcm = NPCManager.getManager();

	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public BankCommand(Lostshard plugin) {
		super(plugin, "bank", "tradegold", "pay", "lottery");
	}

	/**
	 * @param sender
	 *
	 *            Let player access bank.
	 */
	private void bank(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		for (final NPC npc : this.npcm.getBankers())
			if (Utils.isWithin(player.getLocation(), npc.getLocation(),
					Variables.bankRadius)) {
				player.openInventory(pPlayer.getBank().getInventory());
				return;
			}
		Output.simpleError(player, "You are not close enough to a bank.");
		return;
	}

	private void lottery(CommandSender sender, String[] args) {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("bank"))
			this.bank(sender);
		else if (cmd.getName().equalsIgnoreCase("tradegold"))
			this.tradegold(sender, args);
		else if (cmd.getName().equalsIgnoreCase("pay"))
			this.pay(sender, args);
		else if (cmd.getName().equalsIgnoreCase("lottery"))
			this.lottery(sender, args);
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pay"))
			if (args.length == 1)
				if (sender instanceof Player)
					return TabUtils.OnlinePlayersTab(args, (Player) sender);
				else
					return TabUtils.OnlinePlayersTab(args);
		if (cmd.getName().equalsIgnoreCase("tradegold"))
			return TabUtils.empty();
		if (cmd.getName().equalsIgnoreCase("bank"))
			return TabUtils.empty();
		return null;
	}

	/**
	 * @param sender
	 * @param args
	 *
	 *            Let player pay money to another player.
	 */
	private void pay(CommandSender sender, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.DARK_RED + "/pay (player) (amount)");
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
			sender.sendMessage(ChatColor.DARK_RED + "/pay (player) (amount)");
			return;
		}
		if (amount < 1)
			sender.sendMessage(ChatColor.DARK_RED
					+ "Amount must be greater than 0.");
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (player == targetPlayer) {
				Output.simpleError(player, "You may not pay yourself.");
				return;
			}
			final PseudoPlayer pPlayer = this.pm.getPlayer(player);
			if (pPlayer.getMoney() < amount) {
				Output.simpleError(player, "You can't affort to pay "
						+ targetPlayer.getName() + " " + Utils.getDecimalFormater().format(amount) + "gc.");
				return;
			}
			pPlayer.subtractMoney(amount);
		}
		tpPlayer.addMoney(amount);
		sender.sendMessage(ChatColor.GOLD + "You have paied "
				+ targetPlayer.getName() + " " + Utils.getDecimalFormater().format(amount) + "gc.");
		Output.positiveMessage(targetPlayer, sender.getName()
				+ " has paied you " + Utils.getDecimalFormater().format(amount) + "gc.");
	}

	/**
	 * @param sender
	 * @param args
	 *
	 *            Let players tradegold into goldcoins.
	 */
	private void tradegold(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		final NPC npc = this.npcm.getBanker(player.getLocation());
		if (npc == null) {
			Output.simpleError(player, "You are not close enough to a bank.");
			return;
		}
		int amount;
		try {
			if(args[0].equalsIgnoreCase("all")) {
				amount = ItemUtils.containsAmount(player.getInventory(), Material.GOLD_INGOT);
			}else{
				amount = Integer.parseInt(args[0]);
			}
			} catch (final Exception e) {
			Output.simpleError(player, "/tradegold (amount)");
			return;
		}
		if (player.getInventory().contains(Material.GOLD_INGOT, amount)) {
			pPlayer.addMoney(amount * Variables.goldIngotValue);
			ItemUtils.removeItem(player.getInventory(), Material.GOLD_INGOT,
					amount);
			Output.positiveMessage(player, "You have traded " + amount
					+ " gold ingots into " + amount * Variables.goldIngotValue
					+ " gc.");
		} else
			Output.simpleError(player, "You dont have " + amount
					+ " gold ingots in your inventory.");
	}

}
