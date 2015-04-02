package com.lostshard.lostshard.Commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Objects.SpellBook;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

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
		plugin.getCommand("runebook").setExecutor(this);
		plugin.getCommand("spellbook").setExecutor(this);
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
			sm.castSpell(player, spell);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("scrolls")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			scrolls(player, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("runebook")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			runebook(player, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("spellbook")) {
			if(!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			Player player = (Player) sender;
			spellbook(player, args);
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	private void scrolls(Player player, String[] args) {
		if(args.length >= 2) {
			if(args[0].equalsIgnoreCase("use") || args[0].equalsIgnoreCase("cast")) {
				PseudoPlayer pseudoPlayer = pm.getPlayer(player);
				int numSpellWords = args.length;
				String spellName = "";
				for(int i=1; i<numSpellWords; i++) {
					spellName += args[i];
					spellName += " ";
				}
				spellName = spellName.trim();
				List<Scroll> scrolls = pseudoPlayer.getScrolls();
				Spell useSpell = null;
				for(Scroll scroll : scrolls) {
					if(scroll.getSpellName().equalsIgnoreCase(spellName)) {
						useSpell = scroll.getSpell();
						break;
					}
				}
				if(useSpell != null) {
					if(sm.useScroll(player, useSpell)) {
						scrolls.remove(useSpell);
//						Database.removeScroll(useSpell, pseudoPlayer.getId());
					}
				}
				else Output.simpleError(player, "You do not have a scroll of "+spellName+".");
			}
			else if(args[0].equalsIgnoreCase("give")) {
				if(args.length < 3) {
					Output.simpleError(player, "Use \"/scrolls give (player name) (spell name)\"");
					return;
				}
				
				PseudoPlayer pseudoPlayer = pm.getPlayer(player);
				Player targetPlayer = player.getServer().getPlayer(args[1]);
				if(targetPlayer == null) {
					Output.simpleError(player, args[1]+" is not online.");
					return;
				}
				
				
				PseudoPlayer targetPseudoPlayer = pm.getPlayer(targetPlayer);
//				if(!player.isOp()){
//				if(targetPseudoPlayer._secret) {
//					Output.simpleError(player, args[1]+" is not online.");
//					return;
//				}
//				}
					
				if(!player.isOp()){
				if(!Utils.isWithin(player.getLocation(), targetPlayer.getLocation(), 10)) {
					Output.simpleError(player, "You are not close enough to give "+targetPlayer.getName()+" a scroll.");
					return;
				}
				}
				
				int numSpellWords = args.length;
				String spellName = "";
				for(int i=2; i<numSpellWords; i++) {
					spellName += args[i];
					spellName += " ";
				}
				spellName = spellName.trim();
				List<Scroll> scrolls = pseudoPlayer.getScrolls();
				Scroll useScroll = null;
				if(!player.isOp()){
				for(Scroll scroll : scrolls) {
					if(scroll.getSpellName().equalsIgnoreCase(spellName)) {
						useScroll = scroll;
						break;
					}
				}
				if(useScroll != null) {
					scrolls.remove(useScroll);
					targetPseudoPlayer.getScrolls().add(useScroll);
//					Database.updateScrollOwner(useScroll, targetPseudoPlayer.getId());
					Output.positiveMessage(player, "You have given "+targetPlayer.getName()+" a scroll of "+useScroll.getSpellName()+".");
					Output.positiveMessage(targetPlayer, player.getName()+" has given you a scroll of "+useScroll.getSpellName()+".");
				}
				else Output.simpleError(player, "You do not have a scroll of "+spellName+".");
				}else{
					if(useScroll != null) {
						scrolls.remove(useScroll);
						targetPseudoPlayer.getScrolls().add(useScroll);
//						Database.updateScrollOwner(useScroll, targetPseudoPlayer.getId());
						Output.positiveMessage(player, "You have given "+targetPlayer.getName()+" a scroll of "+useScroll.getSpellName()+".");
						Output.positiveMessage(targetPlayer, player.getName()+" has given you a scroll of "+useScroll.getSpellName()+".");
					}
					else Output.simpleError(player, "There dosent exist a scroll of "+spellName+".");
				}
				return;
			}
			else if(args[0].equalsIgnoreCase("spellbook")) {
				PseudoPlayer pseudoPlayer = pm.getPlayer(player);
				int numSpellWords = args.length;
				String spellName = "";
				for(int i=1; i<numSpellWords; i++) {
					spellName += args[i];
					spellName += " ";
				}
				spellName = spellName.trim();
				List<Scroll> scrolls = pseudoPlayer.getScrolls();
				Spell useSpell = null;
				for(Scroll scroll : scrolls) {
					if(scroll.getSpellName().equalsIgnoreCase(spellName)) {
						useSpell = scroll.getSpell();
						break;
					}
				}
				if(useSpell != null) {
					SpellBook spellbook = pseudoPlayer.getSpellbook();
					if(!spellbook.containSpell(useSpell.getType())) {
						spellbook.addSpell(useSpell.getType());
						scrolls.remove(useSpell);
//						Database.removeScroll(useSpell, pseudoPlayer.getId());
						pseudoPlayer.update();
						Output.positiveMessage(player, "You have transferred "+useSpell.getName()+" to your spellbook.");
					}
					else Output.simpleError(player, "Your spellbook already contains the "+useSpell.getName()+" spell.");
				}
				else Output.simpleError(player, "You do not have a scroll of "+spellName+".");
			}
			return;
		}
		else {
			Output.outputScrolls(player, args);
			return;
		}
	}

	private void spellbook(Player player, String[] args) {
		Output.outputSpellbook(player, args);
	}

	@SuppressWarnings("deprecation")
	private void runebook(Player player, String[] args) {
		if(args.length == 0 || (args.length >= 11 && args[1].equalsIgnoreCase("page"))) {
        	Output.outputRunebook(player, args);
    	}
    	else if(args.length > 0) {
    		String secondaryCommand = args[0];
    		if(secondaryCommand.equalsIgnoreCase("give")) {
    			if(args.length >= 3) {
    				String targetName = args[1];
    				Player targetPlayer = Bukkit.getPlayer(targetName);
    				if(targetPlayer != null) {
    					PseudoPlayer targetPseudoPlayer = pm.getPlayer(targetPlayer);
    					
    					PseudoPlayer pseudoPlayer = pm.getPlayer(player);
    					Runebook runebook = pseudoPlayer.getRunebook();
    					Runebook targetRunebook = targetPseudoPlayer.getRunebook();
    					if(targetPlayer.isOp() || (targetPseudoPlayer.wasSubscribed() && targetRunebook.getNumRunes() < 16) || targetRunebook.getNumRunes() < 8) {
    						
    						String runeLabel = "";
    						int splitLength = args.length;
    						for(int i=2; i<splitLength; i++) {
    							runeLabel += args[i];
    							if(i < splitLength-1)
    								runeLabel+= " ";
    						}
    						runeLabel = runeLabel.trim();
    						
    						ArrayList<Rune> runes = runebook.getRunes();
    						Rune foundRune = null;
    						for(Rune rune : runes) {
    							if(rune.getLabel().equalsIgnoreCase(runeLabel)) {
    								foundRune = rune;
    								break;
    							}
    						}
    						if(foundRune != null) {
    							ArrayList<Rune> targetRunes = targetRunebook.getRunes();
    							boolean foundMatching = false;
    							for(Rune rune : targetRunes) {
    								if(rune.getLabel().equalsIgnoreCase(foundRune.getLabel())) {
    									foundMatching = true;
    									break;
    								}
    							}
    							if(!foundMatching) {
    								runebook.removeRune(foundRune);
    								targetRunebook.addRune(foundRune);
    								Database.updateRune(targetPseudoPlayer, foundRune);
    								Output.positiveMessage(player, "You have given the rune "+foundRune.getLabel()+" to "+targetPlayer.getName());
    								Output.positiveMessage(targetPlayer, player.getName()+" has given you the rune "+foundRune.getLabel()+".");
    							}
    							else Output.simpleError(player, targetPlayer.getName()+" already has a rune with that label.");
    						}
    						else Output.simpleError(player, "Could not find a rune with that label.");
    					}
    					else Output.simpleError(player, targetPlayer.getName()+" has too many runes.");
    				}
    				else Output.simpleError(player, "That player is not online.");
    			}
    			else Output.simpleError(player, "Use /runebook give (player name) (rune name)");
    		}
    		else if(secondaryCommand.equalsIgnoreCase("remove")) {
    			if(args.length >= 2) {
    				
    				String runeLabel = "";
					int splitLength = args.length;
					for(int i=1; i<splitLength; i++) {
						runeLabel += args[i];
						if(i < splitLength-1)
							runeLabel+= " ";
					}
					runeLabel = runeLabel.trim();
    				
    				PseudoPlayer pseudoPlayer = pm.getPlayer(player);
    				Runebook runebook = pseudoPlayer.getRunebook();
    				ArrayList<Rune> runes = runebook.getRunes();
    				Rune foundRune = null;
    				for(Rune rune : runes) {
    					if(rune.getLabel().equalsIgnoreCase(runeLabel)) {
    						foundRune = rune;
    						break;
    					}
    				}
    				if(foundRune != null) {
    					runebook.removeRune(foundRune);
    					Output.positiveMessage(player, "You have removed the rune "+foundRune.getLabel());
    					Database.deleteRune(foundRune);
    				}
    				else Output.simpleError(player, "Could not find a rune with that label.");
    			}
    			else Output.simpleError(player, "Use /runebook remove (rune label)");
    		}
    	}

	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		return null;
	}
}
