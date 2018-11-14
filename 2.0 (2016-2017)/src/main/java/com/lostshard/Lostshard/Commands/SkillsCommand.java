package com.lostshard.Lostshard.Commands;

import org.bukkit.entity.Player;

import com.lostshard.CommandManager.Annotations.Sender;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.Skill;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;

public class SkillsCommand {

	public void lock(@Sender Player player, @Sender PseudoPlayer pPlayer, String skillName) {
		final Skill skill = pPlayer.getSkillByName(skillName);
		if (skill == null) {
			Output.simpleError(player, "That skill does not exist.");
			return;
		}
		
		skill.setLocked(true);
		Output.positiveMessage(player, "You have locked " + skill.getName() + " it should no longer gain.");
	}
	
	public void unlock(@Sender Player player, @Sender PseudoPlayer pPlayer, String skillName) {
		final Skill skill = pPlayer.getSkillByName(skillName);
		if (skill == null) {
			Output.simpleError(player, "That skill does not exist.");
			return;
		}
		
		skill.setLocked(false);
		Output.positiveMessage(player,
				"You have unlocked " + skill.getName() + " it should once again gain.");
	}
	
	public void increase(@Sender Player player, @Sender PseudoPlayer pPlayer, String skillName, @Optional @Range(min=1, max=100) int amount) {
		if(amount == 0) {
			Output.positiveMessage(player, "You have " + Utils.getDecimalFormater().format(pPlayer.getFreeSkillPoints()) +" free skill points.");
			return;
		}
		
		final Skill skill = pPlayer.getSkillByName(skillName);
		if (skill == null) {
			Output.simpleError(player, "That skill does not exist.");
			return;
		}
		
		amount = amount*10;
		
		int newSkillAmount = Math.min(skill.getLvl()+amount, 100);
		
		amount = newSkillAmount-skill.getLvl();
		
		pPlayer.setFreeSkillPoints(amount);
		skill.setLvl(newSkillAmount);
				
		Output.positiveMessage(player, "You have increased your " + skill.getName() + ".");
	}
	
	public void decrees(@Sender Player player, @Sender PseudoPlayer pPlayer, String skillName, @Optional @Range(min=1, max=100) int amount) {
		if(amount == 0) {
			Output.positiveMessage(player, "You have " + Utils.getDecimalFormater().format(pPlayer.getFreeSkillPoints()) +" free skill points.");
			return;
		}
		
		final Skill skill = pPlayer.getSkillByName(skillName);
		if (skill == null) {
			Output.simpleError(player, "That skill does not exist.");
			return;
		}
		
		amount = amount*10;
		int newSkillAmount = skill.getLvl() - amount;
		if (newSkillAmount < 0)
			newSkillAmount = 0;
		skill.setLvl(newSkillAmount);
		Output.positiveMessage(player, "You have reduced your " + skill.getName() + ".");
	}
	
	public void give() {
		
	}
}
