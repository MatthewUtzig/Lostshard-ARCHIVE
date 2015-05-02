package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Manager.NPCManager;
import com.lostshard.lostshard.Manager.StoreManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.Store.Store;
import com.lostshard.lostshard.Objects.Store.StoreItem;
import com.lostshard.lostshard.Utils.Output;

public class NPCCommands implements CommandExecutor, TabCompleter  {

	static NPCManager npcm = NPCManager.getManager();
	static StoreManager sm = StoreManager.getManager();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("vendor")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			if(args.length < 1) {
				Output.simpleError(sender, "/vendor (list|buy|sell|add|remove|)");
				return true;
			}
			Player player = (Player) sender;
			String subCommand = args[0];
			if(subCommand.equalsIgnoreCase("list")) {
				vendorList(player);
			}else if(subCommand.equalsIgnoreCase("buy")) {
				vendorBuy(player, args);
			}else if(subCommand.equalsIgnoreCase("sell")) {
				vendorSell(player, args);
			}else if(subCommand.equalsIgnoreCase("remove")) {
				vendorRemove(player, args);
			}else if(subCommand.equalsIgnoreCase("add")) {
				vendorAdd(player, args);
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	private void vendorAdd(Player player, String[] args) {
		//Get nearest vendor
		NPC npc = npcm.getVendor(player.getLocation());
		if(npc == null) {
			Output.simpleError(player, "Theres no vendors near you");
			return;
		}
		
		//Check command length
		if(args.length < 4 && args.length > 2) {
			Output.simpleError(player, "/npc add (Price) (Amount) (buy|sell)");
			return;
		}
		
		//Get store
		Store store = sm.getStore(npc);
		if(store == null) {
			store = new Store(npc.getId());
		}
		
		//Get price and amount from command
		int price = 0;
		int amount = 0;
		try {
			price = Integer.parseInt(args[1]);
			amount = Integer.parseInt(args[2]);
		} catch (Exception e) {
			
		}
		
		//Check if amount and price != 0
		if(price == 0 || amount == 0) {
			Output.simpleError(player, "Error");
			return;
		}
		
		//Get store item if exists and if not create one
		ItemStack item = player.getItemInHand().clone();
		StoreItem storeItem = store.getStoreItem(item);
		if(storeItem == null) {
			storeItem = new StoreItem(item);
			item.setAmount(1);
			storeItem.setItem(item);
		}
		//For sell
		if(args[3].equalsIgnoreCase("sell")) {
			if(!player.getInventory().contains(item.getType(), amount)) {
				Output.simpleError(player, "Error");
				return;
			}
			storeItem.setStock(amount);
			storeItem.setSalePrice(price);
		}//For buy
		else if(args[3].equalsIgnoreCase("buy")) {
			storeItem.setMaxBuyAmount(amount);
			storeItem.setBuyPrice(price);
		}
	}

	private void vendorBuy(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void vendorList(Player player) {
		NPC npc = npcm.getVendor(player.getLocation());
		if(npc == null) {
			Output.simpleError(player, "Theres no vendors near you");
			return;
		}
		Store store = sm.getStore(npc);
		int currentItem = 0;
		//Display items for sale
		if(!store.getItemsForSale().isEmpty())
			Output.positiveMessage(player, "-"+npc.getName()+"'s Items for sale-");
		for(StoreItem item : store.getItemsForSale()) {
			currentItem ++;
			player.sendMessage(ChatColor.YELLOW+""+currentItem+". "+item.getItem().getType().toString()+"(price: "+item.getBuyPrice()+") (In Stock: "+item.getStock()+")");
		}
		//Display items for buy
		if(!store.getItemsForBuy().isEmpty())
			Output.positiveMessage(player, "-"+npc.getName()+"'s Items for buy-");
		for(StoreItem item : store.getItemsForBuy()) {
			currentItem ++;
			player.sendMessage(ChatColor.YELLOW+""+currentItem+". "+item.getItem().getType().toString()+"(price: "+item.getBuyPrice()+") (In Stock: "+item.getStock()+"/"+item.getMaxBuyAmount()+")");	
		}
	}

	private void vendorRemove(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

	private void vendorSell(Player player, String[] args) {
		// TODO Auto-generated method stub
		
	}

}
