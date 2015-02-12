package com.lostshard.lostshard.Commands;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.SPL_HealOther;
import com.lostshard.lostshard.Spells.SPL_HealSelf;
import com.lostshard.lostshard.Spells.SPL_Lightning;
import com.lostshard.lostshard.Spells.SPL_Mark;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;

public class MageryCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	SpellManager sm = SpellManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public MageryCommand(Lostshard plugin) {
		plugin.getCommand("cast").setExecutor(this);
		plugin.getCommand("scrolls").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("cast")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			if(args.length < 1) {
				Output.simpleError(sender, "/cast (spell)");
				return true;
			}
			String spellName = StringUtils.join(args, " ");
			Player player = (Player) sender;
			Spell spell = sm.getSpellByName(spellName);
			if(spell == null) {
				Output.simpleError(sender, "No spell with the name \""+spellName+"\".");
				return true;
			}
			sm.castSpell(player, spell, "");
			return true;
		}else if(cmd.getName().equalsIgnoreCase("scrolls")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			PseudoPlayer pPlayer = pm.getPlayer(player);
			pPlayer.getSpellbook().addSpell(new SPL_HealOther());
			pPlayer.getSpellbook().addSpell(new SPL_HealSelf());
			pPlayer.getSpellbook().addSpell(new SPL_Lightning());
			pPlayer.getSpellbook().addSpell(new SPL_Mark());
			return true;
		}
		return false;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
}
