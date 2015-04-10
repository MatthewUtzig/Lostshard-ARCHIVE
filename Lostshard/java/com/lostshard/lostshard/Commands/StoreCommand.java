package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.StoreManager;
import com.lostshard.lostshard.Objects.Store.Store;
import com.lostshard.lostshard.Objects.Store.StoreItem;
import com.lostshard.lostshard.Utils.Output;

public class StoreCommand implements CommandExecutor, TabCompleter {
	
	StoreManager sm = StoreManager.getManager();
	
	public StoreCommand(Lostshard plugin) {
		plugin.getCommand("buy").setExecutor(this);
		plugin.getCommand("sell").setExecutor(this);
		plugin.getCommand("vendor").setExecutor(this);		
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("vendor")) {
			if(!(sender instanceof Player)){
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			vendor(player, args);
			return true;
		}
		return false;
	}
	
	private void vendor(Player player, String[] args) {
		if(args.length < 1) {
			Output.simpleError(player, "Use \"/vendor help\" commands.");
			return;
		}
		String subCmd = args[0];
		if(subCmd.equalsIgnoreCase("help")) {
			player.sendMessage(ChatColor.GOLD+"-Vendor help-");
			player.sendMessage(ChatColor.YELLOW+"/vendor additem (sellprice) (buyprice)");
			return;
		}else if(subCmd.equalsIgnoreCase("additem")) {
			if(args.length < 3) {
				player.sendMessage(ChatColor.YELLOW+"/vendor additem (sellprice) (buyprice)");
				return;
			}
			Store store = sm.getStore(player.getLocation());
			if(store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			int sellPrice;
			int buyPrice;
			try{
				sellPrice = Integer.parseInt(args[1]);
				buyPrice = Integer.parseInt(args[2]);
			}catch(Exception e) {
				player.sendMessage(ChatColor.YELLOW+"/vendor additem (sellprice) (buyprice)");
				return;
			}
			ItemStack item = player.getItemInHand();
			StoreItem sitem = new StoreItem(item);
			sitem.setSalePrice(sellPrice);
			sitem.setBuyPrice(buyPrice);
			sitem.setStock(0);
			store.addItem(sitem);
			Output.positiveMessage(player, "You have added an item to the store, right click the store with items to add them to stock or use \"/vendor addstock\".");
		}else if(subCmd.equalsIgnoreCase("addstock")) {
			Store store = sm.getStore(player.getLocation());
			if(store == null) {
				Output.simpleError(player, "You are not close enough to a vendor.");
				return;
			}
			ItemStack item = player.getItemInHand();
			StoreItem sitem = store.getStoreItem(item);
			if(sitem == null) {
				Output.simpleError(player, "The vendor do not sell that item use \"/vendor additem (sellprice) (buyprice)\".");
				return;
			}
			player.getInventory().remove(item);
			sitem.setStock(item.getAmount());
			Output.positiveMessage(player, "You have added "+item.getAmount()+" items to the stores stock.");
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
}
