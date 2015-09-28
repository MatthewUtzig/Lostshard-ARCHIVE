package com.lostshard.Lostshard.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.StoreManager;
import com.lostshard.Lostshard.Objects.InventoryGUI.GUI;
import com.lostshard.Lostshard.Objects.InventoryGUI.StoreGUI;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Store.Store;
import com.lostshard.Lostshard.Objects.Store.StoreItem;
import com.lostshard.Lostshard.Utils.Output;

public class StoreCommand extends LostshardCommand {

	StoreManager sm = StoreManager.getManager();
	PlayerManager pm = PlayerManager.getManager();

	public StoreCommand(Lostshard plugin) {
		super(plugin, "vendor");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("vendor")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			this.vendor(player, args);
			return true;
		}
		return false;
	}

	private void vendor(Player player, String[] args) {
		if (args.length < 1) {
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			final PseudoPlayer pPlayer = this.pm.getPlayer(player);
			final GUI gui = new StoreGUI(pPlayer, store);
			gui.openInventory(player);
			return;
		}
		final String subCmd = args[0];
		if (subCmd.equalsIgnoreCase("help")) {
			player.sendMessage(ChatColor.GOLD + "-Vendor help-");
			player.sendMessage(ChatColor.YELLOW
					+ "/vendor additem (sellprice) (buyprice)");
			return;
		} else if (subCmd.equalsIgnoreCase("additem")) {
			if (args.length < 3) {
				player.sendMessage(ChatColor.YELLOW
						+ "/vendor additem (sellprice) (buyprice)");
				return;
			}
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player,
						"You are not close enough to a vendor.");
				return;
			}
			int sellPrice;
			int buyPrice;
			try {
				sellPrice = Integer.parseInt(args[1]);
				buyPrice = Integer.parseInt(args[2]);
			} catch (final Exception e) {
				player.sendMessage(ChatColor.YELLOW
						+ "/vendor additem (sellprice) (buyprice)");
				return;
			}
			if (sellPrice < 0) {
				Output.simpleError(player, "Sell price can't be below 0.");
				return;
			}
			if (buyPrice < 0) {
				Output.simpleError(player, "Buy price can't be below 0.");
				return;
			}
			final ItemStack item = player.getItemInHand();
			final StoreItem sitem = new StoreItem(item);
			sitem.setSalePrice(sellPrice);
			sitem.setBuyPrice(buyPrice);
			sitem.setStock(0);
			store.addItem(sitem);
			Output.positiveMessage(
					player,
					"You have added an item to the store, right click the store with items to add them to stock or use \"/vendor addstock\".");
		} else if (subCmd.equalsIgnoreCase("addstock")) {
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player,
						"You are not close enough to a vendor.");
				return;
			}
			final ItemStack item = player.getItemInHand();
			final StoreItem sitem = store.getStoreItem(item);
			if (sitem == null) {
				Output.simpleError(
						player,
						"The vendor do not sell that item use \"/vendor additem (sellprice) (buyprice)\".");
				return;
			}
			player.getInventory().remove(item);
			sitem.setStock(item.getAmount());
			Output.positiveMessage(player, "You have added " + item.getAmount()
					+ " items to the stores stock.");
		} else if (subCmd.equalsIgnoreCase("removeitem")) {
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player,
						"You are not close enough to a vendor.");
				return;
			}
			if (args.length < 2) {
				Output.simpleError(player, "/vendor removeitem (id)");
				return;
			}
			int id;
			try {
				id = Integer.parseInt(args[1]);
			} catch (final Exception e) {
				player.sendMessage(ChatColor.YELLOW + "/vendor removeitem (id)");
				return;
			}
			final StoreItem si = store.getStoreItem(id);
			final ItemStack item = si.getItem().clone();
			item.setAmount(si.getStock());
			store.removeStoreItem(si);
			player.getWorld().dropItem(player.getLocation(), item);
			Output.positiveMessage(player, "You have removed " + id
					+ " item from the store.");
		}
	}
}
