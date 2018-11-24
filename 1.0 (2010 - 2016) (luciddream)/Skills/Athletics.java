package com.lostshard.RPG.Skills;

import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Utils.Output;

public class Athletics {
	
	public static void handleCommand(Player player, String[] split) {		
	}

	public static boolean possibleSkillGain(Player player, PseudoPlayer pseudoPlayer) {
		if(pseudoPlayer.isSkillLocked("athletics"))
			return false;
		
		int curSkill = pseudoPlayer.getSkill("athletics");
		if((curSkill < 1000) && pseudoPlayer.getTotalSkillVal() < pseudoPlayer.getMaxSkillValTotal()) {
			int gainAmount;
			// variable gain depending on skill level
			double rand = Math.random();
			if(rand < .2)
				gainAmount = 1;
			else if(rand < .4)
				gainAmount = 2;
			else if(rand < .6)
				gainAmount = 3;
			else if(rand <= .8)
				gainAmount = 4;
			else
				gainAmount = 5;
			// Actually set the gain
			int totalSkill = curSkill + gainAmount;
			if(totalSkill > 1000)
				totalSkill = 1000;
			pseudoPlayer.setSkill("athletics", totalSkill);
			Output.gainSkill(player, "Athletics", gainAmount, totalSkill);
			Database.updatePlayer(player.getName());
			return true;
		}
		return false;
	}
}

