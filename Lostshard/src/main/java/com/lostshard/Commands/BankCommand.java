package com.lostshard.Commands;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Data.Variables;
import com.lostshard.Handlers.NPCHandler;
import com.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.NPC.NPC;
import com.lostshard.Objects.PseudoPlayer;
import com.lostshard.Utils.Output;
import com.lostshard.Utils.Utils;

public class BankCommand implements CommandExecutor, TabCompleter{

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("bank")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
			for(NPC npc : NPCHandler.getBankers())
				if(Utils.isWithin(player.getLocation(), npc.getLocation(), Variables.getBankRadius())){
					player.openInventory(pPlayer.getBank().getInventory());
				}else
					Output.simpelError(player, "You are not close enough to a bank.");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("tradegold")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
			for(NPC npc : NPCHandler.getBankers())
				if(Utils.isWithin(player.getLocation(), npc.getLocation(), Variables.getBankRadius())){
					int amount;
					try{
						amount = Integer.parseInt(args[0]);
					}catch(Exception e){
						Output.simpelError(player, "/tradegold (amount)");
						return true;
					}
					if(player.getInventory().containsAtLeast(new ItemStack(Material.GOLD_INGOT), amount)) {
						pPlayer.addMoney(amount*Variables.getGoldIngotValue());
						//TODO add nice msg
						Output.positiveMessage(player, "Tradegold");
					}else
						Output.simpelError(player, "You dont have "+amount+" gold ingots in your inventory.");
				}else
					Output.simpelError(player, "You are not close enough to a bank.");
			return true;
		}
		return false;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		return null;
	}

}
