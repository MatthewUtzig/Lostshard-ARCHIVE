package com.lostshard.RPG.CombatAbilities;

import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;

public class CombatAbility {
	private boolean _isSet = false;
	
	public String getName() 		{ return "AbilityName"; }
	public String getSkillName()	{ return "AbilitySkill"; }
	public int getCooldownTicks()	{ return 0; }
	public int getStaminaCost() 	{ return 0; }
	public int getMinSkill() 		{ return 0; }
	
	public boolean isSet() {
		return _isSet;
	}
	
	public void set(Player player, PseudoPlayer pseudoPlayer) {}
	
	public void unSet(Player player, PseudoPlayer pseudoPlayer) {}
	
	public void swing(Player player, PseudoPlayer pseudoPlayer) {}
	
	public void hit(Player player, PseudoPlayer pseudoPlayer) {}
}