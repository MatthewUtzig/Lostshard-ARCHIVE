package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.NPCManager;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.TabUtils;
import com.lostshard.lostshard.Utils.Utils;

/**
 * @author Jacob Rosborg
 *
 */
public class BankCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	NPCManager npcm = NPCManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public BankCommand(Lostshard plugin) {
		plugin.getCommand("bank").setExecutor(this);
		plugin.getCommand("tradegold").setExecutor(this);
		plugin.getCommand("pay").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("bank")) {
			bank(sender);
		} else if (cmd.getName().equalsIgnoreCase("tradegold")) {
			tradegold(sender, args);
		} else if (cmd.getName().equalsIgnoreCase("pay")) {
			pay(sender, args);
		}
		return true;
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
		String targetName = args[0];
		
		Player targetPlayer = Bukkit.getPlayer(targetName);
		if (targetPlayer == null) {
			sender.sendMessage(ChatColor.DARK_RED + targetName
					+ " is not online.");
			return;
		}
		PseudoPlayer tpPlayer = pm.getPlayer(targetPlayer);
		int amount;
		try {
			amount = Integer.parseInt(args[1]);
		} catch (Exception e) {
			sender.sendMessage(ChatColor.DARK_RED + "/pay (player) (amount)");
			return;
		}
		if (amount < 1) {
			sender.sendMessage(ChatColor.DARK_RED
					+ "Amount must be greater than 0.");
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player == targetPlayer) {
				Output.simpleError(player, "You may not pay yourself.");
				return;
			}
			PseudoPlayer pPlayer = pm.getPlayer(player);
			if (pPlayer.getMoney() < amount) {
				Output.simpleError(player, "You can't affort to pay "
						+ targetPlayer.getName() + " " + amount + "gc.");
				return;
			}
			pPlayer.subtractMoney(amount);
		}
		tpPlayer.addMoney(amount);
		sender.sendMessage(ChatColor.GOLD + "You have paied "
				+ targetPlayer.getName() + " " + amount + "gc.");
		Output.positiveMessage(targetPlayer, sender.getName()
				+ " has paied you " + amount + "gc.");
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
		Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		for (NPC npc : npcm.getBankers())
			if (Utils.isWithin(player.getLocation(), npc.getLocation(),
					Variables.bankRadius)) {
				int amount;
				try {
					amount = Integer.parseInt(args[0]);
				} catch (Exception e) {
					Output.simpleError(player, "/tradegold (amount)");
					return;
				}
				if (player.getInventory().contains(Material.GOLD_INGOT, amount)) {
					pPlayer.addMoney(amount * Variables.goldIngotValue);
					ItemUtils.removeItem(player.getInventory(), Material.GOLD_INGOT, amount);
					Output.positiveMessage(player, "You have traded " + amount
							+ " gold ingots into " + amount
							* Variables.goldIngotValue + " gc.");
				} else
					Output.simpleError(player, "You dont have " + amount
							+ " gold ingots in your inventory.");
			}else
				Output.simpleError(player, "You are not close enough to a bank.");
		return;
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
		Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		for (NPC npc : npcm.getBankers())
			if (Utils.isWithin(player.getLocation(), npc.getLocation(),
					Variables.bankRadius)) {
				player.openInventory(pPlayer.getBank().getInventory());
				return;
			}
		Output.simpleError(player, "You are not close enough to a bank.");
		return;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("pay"))
			if (args.length == 1)
				if (sender instanceof Player)
					return TabUtils.OnlinePlayersTab(args,
							new Player[] { (Player) sender });
				else
					return TabUtils.OnlinePlayersTab(args);
		if (cmd.getName().equalsIgnoreCase("tradegold"))
			return null;
		if (cmd.getName().equalsIgnoreCase("bank"))
			return null;
		return null;
	}

}
