package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_HealOther extends Spell {

	public SPL_HealOther(Scroll scroll) {
		super(scroll);
	}

	private Player foundPlayer;
	private int range = 10;
	
	@Override
	public boolean verifyCastable(Player player) {
		foundPlayer = SpellUtils.playerInLOS(player, range);
		if(foundPlayer == null) {
			Output.simpleError(player, "No target found.");
			return false;
		}
		player.sendMessage(ChatColor.GREEN + foundPlayer.getName());
		return true;
	}

	@Override
	public void preAction(Player player) {
	}

	@Override
	public void doAction(Player player) {
		if(foundPlayer != null) {
			Output.positiveMessage(player, "You have healed "+foundPlayer.getName()+".");
			foundPlayer.setHealth(Math.min(foundPlayer.getHealth()+8, 20));
		}

	}

	public Player getFoundPlayer() {
		return foundPlayer;
	}

	public void setFoundPlayer(Player foundPlayer) {
		this.foundPlayer = foundPlayer;
	}

}
