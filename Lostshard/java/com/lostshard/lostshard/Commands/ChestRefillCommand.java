package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.ChestRefillManager;
import com.lostshard.lostshard.Objects.ChestRefill;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class ChestRefillCommand implements CommandExecutor, TabCompleter {
	
	ChestRefillManager cm = ChestRefillManager.getManager();
	
	public ChestRefillCommand(Lostshard plugin) {
		plugin.getCommand("dc").setExecutor(this);
	}

	private void dcCreate(Player player, String[] args) {
		if(args.length < 3) {
			Output.simpleError(player, "/dc create (minMinuters) (maxMinuters)");
			return;
		}
		Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if(!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		ChestRefill cr = cm.getChest((Chest)block.getState());
		if(cr != null) {
			Output.simpleError(player, "Thats already a Dungeon Chest.");
			return;
		}
		long rangeMin;
		long rangeMax;
		try {
			rangeMin = Integer.parseInt(args[1]);
			rangeMax = Integer.parseInt(args[2]);
		} catch(Exception e) {
			Output.simpleError(player, "/dc create (minMinuters) (maxMinuters)");
			return;
		}
		
		rangeMin*=60000;
		rangeMax*=60000;
		
		ItemStack[] items = ((Chest) block.getState()).getInventory().getContents();
		cr = new ChestRefill(block.getLocation(), rangeMin, rangeMax, items);
		cm.add(cr);
	}
	
	private void dcFill(Player player) {
		Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if(!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		ChestRefill cr = cm.getChest((Chest)block.getState());
		if(cr == null) {
			Output.simpleError(player, "Thats not a Dungeon Chest.");
			return;
		}
		cr.refill();
		Output.positiveMessage(player, "You have refilled the Dungeon Chest.");
	}

	private void dcRemove(Player player) {
		Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if(!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		ChestRefill cr = cm.getChest((Chest)block);
		if(cr == null) {
			Output.simpleError(player, "Thats not a Dungeon Chest.");
			return;
		}
		cm.remove(cr);
		Output.positiveMessage(player, "You have removed the Dungeon Chest.");
	}

	private void dcSetContents(Player player) {
		Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if(!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		ChestRefill cr = cm.getChest((Chest)block);
		if(cr == null) {
			Output.simpleError(player, "Thats not a Dungeon Chest.");
			return;
		}
		cr.setItems(((Chest)block.getState()).getInventory().getContents());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dc")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			if(args.length < 1) {
        		Output.positiveMessage(player, "Dungeon Chest");
        		Output.positiveMessage(player, "/dc create (minMinuters) (maxMinuters)");
        		Output.positiveMessage(player, "/dc remove");
        		Output.positiveMessage(player, "/dc setcontents");
        		Output.positiveMessage(player, "/dc fill");
				return true;
			}
			String subCmd = args[0];
			if(subCmd.equalsIgnoreCase("create"))
					dcCreate(player, args);
			else if(subCmd.equalsIgnoreCase("remove"))
				dcRemove(player);
			else if(subCmd.equalsIgnoreCase("setContents")) 
				dcSetContents(player);
			else if(subCmd.equalsIgnoreCase("fill"))
				dcFill(player);
			else {
        		Output.positiveMessage(player, "Dungeon Chest");
        		Output.positiveMessage(player, "/dc create (minMinuters) (maxMinuters)");
        		Output.positiveMessage(player, "/dc remove");
        		Output.positiveMessage(player, "/dc setcontents");
        		Output.positiveMessage(player, "/dc fill");
				return true;
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		return null;
	}
}
