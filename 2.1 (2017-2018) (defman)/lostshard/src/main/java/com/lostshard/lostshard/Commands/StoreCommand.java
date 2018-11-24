package com.lostshard.lostshard.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.StoreManager;
import com.lostshard.lostshard.Objects.InventoryGUI.GUI;
import com.lostshard.lostshard.Objects.InventoryGUI.StoreGUI;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.lostshard.Objects.Store.Store;
import com.lostshard.lostshard.Objects.Store.StoreItem;
import com.lostshard.lostshard.Utils.Output;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Text;

public class StoreCommand {

	StoreManager sm = StoreManager.getManager();
	PlayerManager pm = PlayerManager.getManager();

	@Command(aliases = { "shop" }, desc = "Opens the shop window")
	public void shop(@Sender Player player) {
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
	
	
	@Command(aliases = { "vendor" }, desc = "Vendor commands", usage = "<subcommand>")
	public void vendor(@Sender Player player, @Text @Optional(value="") String arg) {
		String[] args = arg.split(" ");
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
			player.sendMessage(ChatColor.YELLOW + "/vendor <add|remove|stock|edit|clear>");
			return;
		} else if (subCmd.equalsIgnoreCase("add")) {
			if (args.length < 4) {
				player.sendMessage(ChatColor.YELLOW + "/vendor add (sellPrice) (buyPrice) (maxBuy)");
				return;
			}
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			int sellPrice;
			int buyPrice;
			int maxBuy;
			try {
				sellPrice = Integer.parseInt(args[1]);
				buyPrice = Integer.parseInt(args[2]);
				maxBuy = Integer.parseInt(args[3]);
			} catch (final Exception e) {
				player.sendMessage(ChatColor.YELLOW + "/vendor add (sellPrice) (buyPrice) (maxBuy)");
				return;
			}
			if (sellPrice < 0) {
				Output.simpleError(player, "Sell price can't be less 0.");
				return;
			}
			if (buyPrice < 0) {
				Output.simpleError(player, "Buy price can't be less 0.");
				return;
			}
			if (maxBuy < 0) {
				Output.simpleError(player, "Max buy amount can't be less 0.");
				return;
			}

			if (buyPrice > sellPrice) {
				Output.simpleError(player, "Buy price can't be above sell price.");
				return;
			}

			final ItemStack item = player.getItemInHand();
			final StoreItem sitem = new StoreItem(item, sellPrice, buyPrice, 0, maxBuy);
			store.addItem(sitem);
			Output.positiveMessage(player,
					"You have added an item to the store, right click the store with items to add them to stock or use \"/vendor stock\".");
		} else if (subCmd.equalsIgnoreCase("stock")) {
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			final ItemStack item = player.getItemInHand();
			final StoreItem sitem = store.getStoreItem(item);
			if (sitem == null) {
				Output.simpleError(player,
						"The vendor do not sell that item use \"/vendor add (sellprice) (buyprice) (maxBuy)\".");
				return;
			}
			sitem.setStock(sitem.getStock()+item.getAmount());
			player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
			Output.positiveMessage(player, "You have added " + item.getAmount() + " items to the stores stock.");
		} else if (subCmd.equalsIgnoreCase("remove")) {
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			if (args.length < 2) {
				Output.simpleError(player, "/vendor remove (id) (amount)");
				return;
			}
			int id;
			int amount = 0;
			try {
				id = Integer.parseInt(args[1]);
				if (args.length > 2)
					amount = Integer.parseInt(args[2]);
			} catch (final Exception e) {
				player.sendMessage(ChatColor.YELLOW + "/vendor remove (id) (amount)");
				return;
			}

			if (amount < 1) {
				Output.simpleError(player, "Amount can't be less 1.");
				return;
			}

			final StoreItem si = store.getItems().get(id - 1);
			
			if (amount > si.getStock()) {
				Output.simpleError(player, "The shop do not contain enough items.");
				return;
			}
			
			final ItemStack item = si.getItem().clone();
			item.setAmount(amount);
			if (amount > si.getStock())
				store.removeStoreItem(si);
			else
				si.setStock(si.getStock() - amount);
			player.getWorld().dropItem(player.getLocation(), item);
			Output.positiveMessage(player, "You have removed " + id + "'th item from the store.");
		} else if (subCmd.equalsIgnoreCase("clear")) {
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			final List<StoreItem> si = store.getItems();
			store.setItems(new ArrayList<StoreItem>());
			for (final StoreItem i : si)
				player.getWorld().dropItem(player.getLocation(), i.getItem());
			Output.positiveMessage(player, "You have cleared out the store");
		} else if (subCmd.equalsIgnoreCase("edit")) {
			final Store store = this.sm.getStore(player.getLocation());
			if (store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			if (args.length < 5) {
				Output.simpleError(player, "/vendor edit (id) (sellPrice) (buyPrice) (maxBuy)");
				return;
			}
			int id;
			int sellPrice;
			int buyPrice;
			int maxBuy;
			try {
				id = Integer.parseInt(args[1]);
				sellPrice = Integer.parseInt(args[2]);
				buyPrice = Integer.parseInt(args[3]);
				maxBuy = Integer.parseInt(args[4]);
			} catch (final Exception e) {
				player.sendMessage(ChatColor.YELLOW + "/vendor edit (id) (sellPrice) (buyPrice) (maxBuy)");
				return;
			}
			if (sellPrice < 0) {
				Output.simpleError(player, "Sell price can't be less 0.");
				return;
			}
			if (buyPrice < 0) {
				Output.simpleError(player, "Buy price can't be less 0.");
				return;
			}
			if (maxBuy < 0) {
				Output.simpleError(player, "Max buy amount can't be less 0.");
				return;
			}

			if (buyPrice > sellPrice) {
				Output.simpleError(player, "Buy price can't be above sell price.");
				return;
			}
			final StoreItem si = store.getItems().get(id - 1);
			si.setSalePrice(sellPrice);
			si.setBuyPrice(buyPrice);
			si.setMaxBuyAmount(maxBuy);
			Output.positiveMessage(player, "You have updated " + id + "'th item from the store.");
		} else {
			Output.simpleError(player, "/vendor (add|remove|stock|edit|clear)");
		}
	}
}
