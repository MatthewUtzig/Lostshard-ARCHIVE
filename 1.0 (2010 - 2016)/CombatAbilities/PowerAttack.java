package com.lostshard.RPG.CombatAbilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;

public class PowerAttack extends CombatAbility{
	private static final String 	_name = "Power Attack";
	private static final String 	_skillName = "blades";
	private static final int 		_cooldownTicks = 30;
	private static final int		_staminaCost = 30;
	private static final int 		_minSkill = 0;
	
	private boolean _isSet = false;
	
	public String getName() 		{ return _name; }
	public String getSkillName()	{ return _skillName; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getStaminaCost() 	{ return _staminaCost; }
	public int getMinSkill() 		{ return _minSkill; }
	public boolean isSet() 			{ return _isSet; }
	
	public void set(Player player, PseudoPlayer pseudoPlayer) {
		player.sendMessage("You have activated "+_name+".");
		_isSet = true;
	}
	
	public void unSet(Player player, PseudoPlayer pseudoPlayer) {
		player.sendMessage("You have de-activated "+_name+".");
		_isSet = false;
	}
	
	public void swing(Player player, PseudoPlayer pseudoPlayer) {
		_isSet = false;
		player.sendMessage("You have triggered "+_name+".");
	}
	
	public void hit(Player player, PseudoPlayer pseudoPlayer, LivingEntity target) {
		
	}
}
