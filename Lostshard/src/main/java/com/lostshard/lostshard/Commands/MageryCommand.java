package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Spells.Spell;

public class MageryCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	SpellManager sm = SpellManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public MageryCommand(Lostshard plugin) {
		plugin.getCommand("cast").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("cast")) {
			Player player = (Player) sender;
			Spell spell = sm.getSpellByName("heal");
			sm.castSpell(player, spell, "");
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
	
	
	
}
