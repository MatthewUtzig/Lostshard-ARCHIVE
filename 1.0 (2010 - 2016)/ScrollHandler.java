package com.lostshard.RPG;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Skills.Magery;
import com.lostshard.RPG.Skills.Spellbook;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class ScrollHandler {
	public static void handleCommand(Player player, String[] split) {
		if(split.length >= 3) {
			if(split[1].equalsIgnoreCase("use") || split[1].equalsIgnoreCase("cast")) {
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
				int numSpellWords = split.length;
				String spellName = "";
				for(int i=2; i<numSpellWords; i++) {
					spellName += split[i];
					spellName += " ";
				}
				spellName = spellName.trim();
				ArrayList<Spell> scrolls = pseudoPlayer.getScrolls();
				Spell useSpell = null;
				for(Spell spell : scrolls) {
					if(spell.getName().equalsIgnoreCase(spellName)) {
						useSpell = spell;
						break;
					}
				}
				if(useSpell != null) {
					/*if(useSpell.getPageNumber() > 7) {
						Output.simpleError(player, "You cannot cast that spell from a scroll, it is too high level.");
						return;
					}*/
					if(Magery.useScroll(player, useSpell)) {
						scrolls.remove(useSpell);
						Database.removeScroll(useSpell, pseudoPlayer.getId());
					}
				}
				else Output.simpleError(player, "You do not have a scroll of "+spellName+".");
			}
			else if(split[1].equalsIgnoreCase("give")) {
				if(split.length < 4) {
					Output.simpleError(player, "Use \"/scrolls give (player name) (spell name)\"");
					return;
				}
				
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
				Player targetPlayer = player.getServer().getPlayer(split[2]);
				if(targetPlayer == null) {
					Output.simpleError(player, split[2]+" is not online.");
					return;
				}
				
				PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
				if(targetPseudoPlayer._secret) {
					Output.simpleError(player, split[2]+" is not online.");
					return;
				}
					
				
				if(!Utils.isWithin(player.getLocation(), targetPlayer.getLocation(), 10)) {
					Output.simpleError(player, "You are not close enough to give "+targetPlayer+" a scroll.");
					return;
				}
				
				int numSpellWords = split.length;
				String spellName = "";
				for(int i=3; i<numSpellWords; i++) {
					spellName += split[i];
					spellName += " ";
				}
				spellName = spellName.trim();
				ArrayList<Spell> scrolls = pseudoPlayer.getScrolls();
				Spell useSpell = null;
				for(Spell spell : scrolls) {
					if(spell.getName().equalsIgnoreCase(spellName)) {
						useSpell = spell;
						break;
					}
				}
				if(useSpell != null) {
					scrolls.remove(useSpell);
					targetPseudoPlayer.getScrolls().add(useSpell);
					Database.updateScrollOwner(useSpell, targetPseudoPlayer.getId());
					Output.positiveMessage(player, "You have given "+targetPlayer.getName()+" a scroll of "+useSpell.getName()+".");
					Output.positiveMessage(targetPlayer, player.getName()+" has given you a scroll of "+useSpell.getName()+".");
				}
				else Output.simpleError(player, "You do not have a scroll of "+spellName+".");
			}
			else if(split[1].equalsIgnoreCase("spellbook")) {
				PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
				int numSpellWords = split.length;
				String spellName = "";
				for(int i=2; i<numSpellWords; i++) {
					spellName += split[i];
					spellName += " ";
				}
				spellName = spellName.trim();
				ArrayList<Spell> scrolls = pseudoPlayer.getScrolls();
				Spell useSpell = null;
				for(Spell spell : scrolls) {
					if(spell.getName().equalsIgnoreCase(spellName)) {
						useSpell = spell;
						break;
					}
				}
				if(useSpell != null) {
					Spellbook spellbook = pseudoPlayer.getSpellbook();
					if(!spellbook.hasSpellByName(useSpell.getName())) {
						spellbook.addSpell(useSpell);
						scrolls.remove(useSpell);
						Database.removeScroll(useSpell, pseudoPlayer.getId());
						Database.updatePlayerByPseudoPlayer(pseudoPlayer);
						Output.positiveMessage(player, "You have transferred "+useSpell.getName()+" to your spellbook.");
					}
					else Output.simpleError(player, "Your spellbook already contains the "+useSpell.getName()+" spell.");
				}
				else Output.simpleError(player, "You do not have a scroll of "+spellName+".");
			}
		}
		else {
			Output.outputScrolls(player, split);
		}
	}
}
