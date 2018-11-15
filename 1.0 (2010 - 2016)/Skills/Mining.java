package com.lostshard.RPG.Skills;

import org.bukkit.entity.*;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Utils.Output;

public class Mining {
	
	public static void handleCommand(Player player, String[] split) {		
	}

	public static boolean possibleSkillGain(Player player, PseudoPlayer pseudoPlayer) {
		if(pseudoPlayer.isSkillLocked("mining"))
			return false;
		
		int curSkill = pseudoPlayer.getSkill("mining");
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
			pseudoPlayer.setSkill("mining", totalSkill);
			Output.gainSkill(player, "Mining", gainAmount, totalSkill);
			Database.updatePlayer(player.getName());
			return true;
		}
		return false;
	}

	public static void prospect(Player player) {
		/*Location loc = player.getLocation();
		Block blockAtPoint = player.getWorld().getBlockAt(new Location(loc.getWorld(), loc.getX()+(loc.getDirection().getX()*20), loc.getY()+player.getEyeHeight()+(loc.getDirection().getY()*20), loc.getZ()+(loc.getDirection().getZ()*20)));
		Block blockAt = player.getWorld().getBlockAt(new Location(loc.getWorld(), loc.getX(), loc.getY()+player.getEyeHeight(), loc.getZ()));
		ArrayList<IntPoint> points = Bresenham.bresenham3d(blockAt.getX(), blockAt.getY(), blockAt.getZ(), blockAtPoint.getX(), blockAtPoint.getY(), blockAtPoint.getZ());
		
		ArrayList<String> blocks = new ArrayList<String>();
		ArrayList<Integer> blockCount = new ArrayList<Integer>();
		
		System.out.println("1("+blockAt.getX()+","+blockAt.getY()+","+blockAt.getZ()+")");
		System.out.println("2("+blockAtPoint.getX()+","+blockAtPoint.getY()+","+blockAtPoint.getZ()+")");
		System.out.println("Num:"+points.size());
		
		for(IntPoint intPoint : points) {
			Block b = loc.getWorld().getBlockAt(new Location(loc.getWorld(), intPoint.x, intPoint.y, intPoint.z));
			if(blocks.contains(b.getType().name())) {
				int index = blocks.indexOf(b.getType().name());
				blockCount.set(index, blockCount.get(index)+1);
			}
			else {
				blocks.add(b.getType().name());
				blockCount.add(1);
			}
		}
		
		Output.positiveMessage(player, "-Prospecting Results-");
		String outputString = "";
		int numBlockTypes = blocks.size();
		for(int i=0; i<numBlockTypes; i++) {
			outputString += blocks.get(i) +"-"+blockCount.get(i)+", ";
		}
		player.sendMessage(outputString);*/
	}
}

