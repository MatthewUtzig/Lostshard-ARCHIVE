package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_HealOther extends Spell {

	private Player foundPlayer;

	private final int range = 10;

	public SPL_HealOther(Scroll scroll) {
		super(scroll);
	}

	@Override
	public void doAction(Player player) {
		if (this.foundPlayer != null) {
			Output.positiveMessage(player, "You have healed " + this.foundPlayer.getName() + ".");
			this.foundPlayer.setHealth(Math.min(this.foundPlayer.getHealth() + 8, 20));
		}

	}

	public Player getFoundPlayer() {
		return this.foundPlayer;
	}

	@Override
	public void preAction(Player player) {
	}

	public void setFoundPlayer(Player foundPlayer) {
		this.foundPlayer = foundPlayer;
	}

	@Override
	public boolean verifyCastable(Player player) {
		this.foundPlayer = SpellUtils.playerInLOS(player, this.range);
		if (this.foundPlayer == null) {
			Output.simpleError(player, "No target found.");
			return false;
		}
		player.sendMessage(ChatColor.GREEN + this.foundPlayer.getName());
		return true;
	}

}
