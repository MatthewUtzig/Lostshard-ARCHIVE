package com.lostshard.lostshard.Commands;

import java.util.List;

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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("dc")) {
			dc(sender, args);
			return true;
		}
		return false;
	}
	
	private void dc(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		if(args.length < 2) {
			Output.simpleError(sender, "/dc (rangeMin) (rangeMax)");
			return;
		}
		Player player = (Player) sender;
		Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if(block instanceof Chest) {
			Output.simpleError(sender, "Invalid target.");
			return;
		}
		long rangeMin;
		long rangeMax;
		try {
			rangeMin = Long.parseLong(args[0]);
			rangeMax = Long.parseLong(args[1]);
		} catch(Exception e) {
			Output.simpleError(sender, "/dc (rangeMin) (rangeMax)");
			return;
		}
		ItemStack[] items = ((Chest) block).getInventory().getContents();
		ChestRefill cr = new ChestRefill(block.getLocation(), rangeMin, rangeMax, items);
		cm.add(cr);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		return null;
	}
}
