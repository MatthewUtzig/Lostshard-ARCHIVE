package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_HealOther extends Spell {

	public SPL_HealOther() {
		setName("Heal Other");
		setSpellWords("Buddius Healicus");
		setCastingDelay(0);
		setCooldown(20);
		setManaCost(20);
		addReagentCost(new ItemStack(Material.STRING));
		addReagentCost(new ItemStack(Material.SEEDS));
		setMinMagery(600);
		setPage(6);
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
