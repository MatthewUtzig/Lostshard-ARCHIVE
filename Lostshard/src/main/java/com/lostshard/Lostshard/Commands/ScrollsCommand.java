package com.lostshard.Lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.SpellManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.SpellBook;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Output;
import com.sk89q.intake.parametric.annotation.Text;

public class ScrollsCommand {

	SpellManager sm = SpellManager.getManager();
	PlayerManager pm = PlayerManager.getManager();
	
	public void list(@Sender Player player) {
		Output.outputScrolls(player);
	}
	
	public void use(@Sender Player player, @Sender PseudoPlayer pPlayer, @Text String scrollName) {
		Scroll scroll = Scroll.getByString(scrollName);
		
		if(scroll == null) {
			Output.simpleError(player, "There does not exist a sroll with that name.");
			return;
		}
		
		if(!pPlayer.getScrolls().contains(scroll)) {
			Output.simpleError(player, "You do not have a scroll of " + scroll.getName() + ".");
			return;
		}
		
		if (sm.useScroll(player, scroll)) {
			pPlayer.getScrolls().remove(scroll);
		}
	}
	
	public void give(@Sender Player player, @Sender PseudoPlayer pPlayer, Player target, @Text String scrollName) {
		if(player.equals(target)) {
			Output.simpleError(player, "You cannot give your self a scroll.");
			return;
		}
		
		if (!player.isOp())
			if (player.getLocation().distance(target.getLocation()) > 100) {
				Output.simpleError(player,
						"You are not close enough to give " + target.getName() + " a scroll.");
				return;
			}
		
		PseudoPlayer tpPlayer = pm.getPlayer(target);
		
		if (scrollName.equalsIgnoreCase("all") && player.isOp()) {
			for (final Scroll s : Scroll.values())
				tpPlayer.addScroll(s);
			Output.positiveMessage(player, "You have given " + target.getName() + " all scrolls.");
			Output.positiveMessage(target, player.getName() + " has given you all scrolls.");
			return;
		}
		
		Scroll scroll = Scroll.getByString(scrollName);
				
		if(scroll == null) {
			Output.simpleError(player, "There does not exist a sroll with that name.");
			return;
		}
		
		if(!pPlayer.getScrolls().contains(scroll)) {
			Output.simpleError(player, "You do not have a scroll of " + scroll.getName() + ".");
			return;
		}
		
		pPlayer.getScrolls().remove(scroll);
		tpPlayer.getScrolls().add(scroll);
		Output.positiveMessage(player,
				"You have given " + target.getName() + " a scroll of " + scroll.getName() + ".");
		Output.positiveMessage(target,
				player.getName() + " has given you a scroll of " + scroll.getName() + ".");
	}
	
	public void spellbook(@Sender Player player, @Sender PseudoPlayer pPlayer, @Text String scrollName) {
		Scroll scroll = Scroll.getByString(scrollName);
		
		if(scroll == null) {
			Output.simpleError(player, "There does not exist a sroll with that name.");
			return;
		}
		
		SpellBook spellbook = pPlayer.getSpellbook();
		
		if (spellbook.contains(scroll)) {
			Output.simpleError(player, "Your spellbook already contains the " + scroll.getName() + " spell.");
			return;
		}
		
		spellbook.add(scroll);
		Output.positiveMessage(player, "You have transferred " + scroll.getName() + " to your spellbook.");
	}
}
