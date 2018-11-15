package com.lostshard.RPG.Skills;

import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Utils.Output;

public class Skill {
	protected static void skillGain(Player player, PseudoPlayer pseudoPlayer, double minPercentChanceSkillGain, String skillName, String skillProperName) {
		int curSkill = pseudoPlayer.getSkill(skillName);
		if((curSkill < 1000) && (pseudoPlayer.getTotalSkillVal() < pseudoPlayer.getMaxSkillValTotal())) {
			double percentChance;
			percentChance = ((double)(1000-curSkill))/1000;
			// we don't want it to be TOOOO hard to gain skill
			if(percentChance < minPercentChanceSkillGain)
				percentChance = minPercentChanceSkillGain;
			
			double rand = Math.random();
			if(rand < percentChance) {
				//System.out.println(percentChance+"|"+rand);
				int gainAmount;
				// variable gain depending on skill level
				double rand2 = Math.random();
				if(rand2 < .2)
					gainAmount = 1;
				else if(rand2 < .4)
					gainAmount = 2;
				else if(rand2 < .6)
					gainAmount = 3;
				else if(rand2 <= .8)
					gainAmount = 4;
				else
					gainAmount = 5;
				
				// Actually set the gain
				int totalSkill = curSkill + gainAmount;
				if(totalSkill > 1000)
					totalSkill = 1000;
				pseudoPlayer.setSkill(skillName, totalSkill);
				Output.gainSkill(player, skillProperName, gainAmount, totalSkill);
				Database.updatePlayerByPseudoPlayer(pseudoPlayer);
			}
		}
	}
}
