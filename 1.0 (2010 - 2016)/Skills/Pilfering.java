package com.lostshard.RPG.Skills;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.lostshard.RPG.PseudoPlayer;

public class Pilfering extends Skill{
	
	public static void handleCommand(Player player, String[] split) {
		if(split.length == 1) {
			player.sendMessage(ChatColor.GOLD+"-Blacksmith Commands-");
			player.sendMessage(ChatColor.YELLOW+"repair - "+ChatColor.WHITE+"Attempts to repair the item you are holding.");
			player.sendMessage(ChatColor.YELLOW+"smelt - "+ChatColor.WHITE+"Attempts to smelt the item you are holding.");
		}
		else if(split.length > 1) {
		}
	}
	
	public static void snoop(Player snoopingPlayer, Player snoopedPlayer) {
		SpoutPlayer spoutSnoopingPlayer = (SpoutPlayer)snoopingPlayer;
		spoutSnoopingPlayer.openInventoryWindow(snoopedPlayer.getInventory());
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer) {
		if(pseudoPlayer.isSkillLocked("blacksmithy"))
			return;
		skillGain(player, pseudoPlayer, .35, "blacksmithy", "Blacksmithy");
	}
}

