package com.lostshard.RPG.Skills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.material.Mushroom;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Spells.Spell;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Survivalism {
	private static final int TRACK_STAMINA_COST = 25;
	private static final int CAMP_STAMINA_COST = 50;
	
	private static ArrayList<Camp> _camps = new ArrayList<Camp>();
	
	public static void handleCommand(Player player, String[] split) {		
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		//if(split[0].equals("/survivalism")) {
			if(split.length == 1) {
				player.sendMessage(ChatColor.GOLD+"-Survivalism Commands-");
				player.sendMessage(ChatColor.YELLOW+"track (creature name) - "+ChatColor.WHITE+"Attempts to track down a creature.");
				player.sendMessage(ChatColor.YELLOW+"camp - "+ChatColor.WHITE+"Attempts to set up a camp.");
			}
			else if(split.length > 1) {
				String splitCommand = split[1];
				if(splitCommand.equalsIgnoreCase("test"))
					System.out.println("camps: "+ _camps.size());
				if(splitCommand.equalsIgnoreCase("track") || splitCommand.equalsIgnoreCase("tracking")) {
					track(player, pseudoPlayer, split);
				}
				else if(splitCommand.equalsIgnoreCase("camp") || splitCommand.equalsIgnoreCase("camping")) {
					camp(player, pseudoPlayer, split);
				}
			}
		//}
	}
	
	public static void track(Player player, PseudoPlayer pseudoPlayer, String[] split) {
		int curSkill = pseudoPlayer.getSkill("survivalism");
		if(split.length == 3) {			
			if(pseudoPlayer.getStamina() < TRACK_STAMINA_COST) {
				Output.simpleError(player, "Not enough stamina - Tracking requires "+TRACK_STAMINA_COST+".");
				return;
			}
			
			String targetName = split[2];
			
			if(targetName.equalsIgnoreCase(player.getName())) {
				Output.simpleError(player, "You cannot track yourself.");
				return;
			}
			
			Player targetPlayer = Utils.getPlugin().getServer().getPlayer(targetName);
			if(targetPlayer != null && targetPlayer.isOp()) {
				Output.simpleError(player, "Cannot find "+targetName+".");
				return;
			}
			
			int modCurSkill = curSkill;
			
			LivingEntity foundLivingEntity = null;
			if(targetPlayer == null) {
				//not a player
				boolean trackZombie = false;
				boolean trackSkeleton = false;
				boolean trackCreeper = false;
				boolean trackSpider = false;
				boolean trackGhast = false;
				boolean trackSheep = false;
				boolean trackSquid = false;
				boolean trackChicken = false;
				boolean trackSlime = false;
				boolean trackPig = false;
				boolean trackCow = false;
				boolean trackWolf = false;
				boolean trackOcelot = false;
				boolean trackBlaze = false;
		
				if(targetName.equalsIgnoreCase("zombie")) { trackZombie = true; }
				if(targetName.equalsIgnoreCase("skeleton")) { trackSkeleton = true; }
				if(targetName.equalsIgnoreCase("creeper")) { trackCreeper = true; }
				if(targetName.equalsIgnoreCase("spider")) { trackSpider = true; }
				if(targetName.equalsIgnoreCase("ghast")) { trackGhast = true; }
				if(targetName.equalsIgnoreCase("sheep")) { trackSheep = true; }
				if(targetName.equalsIgnoreCase("squid")) { trackSquid = true; }
				if(targetName.equalsIgnoreCase("chicken")) { trackChicken = true; }
				if(targetName.equalsIgnoreCase("slime")) { trackSlime = true; }
				if(targetName.equalsIgnoreCase("pig")) { trackPig = true; }
				if(targetName.equalsIgnoreCase("cow")) { trackCow = true; }
				if(targetName.equalsIgnoreCase("wolf")) { trackWolf = true; }
				
				if(targetName.equalsIgnoreCase("ocelot")) { trackOcelot = true; }
				if(targetName.equalsIgnoreCase("blaze")) { trackBlaze = true; }
				
				ArrayList<LivingEntity> lE = new ArrayList<LivingEntity>();
				List<LivingEntity> livingEntities = player.getWorld().getLivingEntities();
				for(LivingEntity livingEntity : livingEntities) {
					if(trackSquid)
						if(livingEntity instanceof Squid) { lE.add(livingEntity); }
					if(trackWolf)
						if(livingEntity instanceof Wolf) {lE.add(livingEntity); }
					if(livingEntity instanceof Animals) {
						if(trackSheep)
							if(livingEntity instanceof Sheep) { lE.add(livingEntity); }
						if(trackSquid)
							if(livingEntity instanceof Squid) { lE.add(livingEntity); }
						if(trackChicken)
							if(livingEntity instanceof Chicken) { lE.add(livingEntity); }
						if(trackPig)
							if(livingEntity instanceof Pig) { lE.add(livingEntity); }
						if(trackCow)
							if(livingEntity instanceof Cow) { lE.add(livingEntity); }
						if(trackOcelot)
							if(livingEntity instanceof Ocelot) { lE.add(livingEntity); }
					}
					else if(livingEntity instanceof Monster) {
						if(trackZombie)
							if(livingEntity instanceof Zombie) { lE.add(livingEntity); }
						if(trackSkeleton)
							if(livingEntity instanceof Skeleton) { lE.add(livingEntity); }
						if(trackCreeper)
							if(livingEntity instanceof Creeper) { lE.add(livingEntity); }
						if(trackSpider)
							if(livingEntity instanceof Spider) { lE.add(livingEntity); }
						if(trackGhast)
							if(livingEntity instanceof Ghast) { lE.add(livingEntity); }
						if(trackSlime)
							if(livingEntity instanceof Slime) { lE.add(livingEntity); }
						if(trackBlaze)
							if(livingEntity instanceof Blaze) { lE.add(livingEntity); }
					}
				}
				
				if(lE.size() > 0) {
					if(lE.get(0) instanceof Monster) {
						if(curSkill < 250) {
							Output.simpleError(player, "You must have 25 Survivalism to track monsters.");
							return;
						}
					}
					LivingEntity closestEntity = lE.get(0);
					double closestDist = Utils.fastDistance(closestEntity.getLocation(), player.getLocation());
					for(LivingEntity livingEntity : lE) {
						double dist = Utils.fastDistance(livingEntity.getLocation(), player.getLocation());
						if(dist < closestDist) {
							closestDist = dist;
							closestEntity = livingEntity;
						}
					}
					foundLivingEntity = closestEntity;
				}
				else {
					Output.simpleError(player, "Cannot find "+targetName+".");
					return;
				}
			}
			else {
				PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
				int defSkill = pseudoPlayerDefender.getSkill("survivalism");
				
				if(curSkill < 500) {
					Output.simpleError(player, "You must have 50 Survivalism to track a player.");
					return;
				}
				foundLivingEntity = (LivingEntity)targetPlayer;
				if(pseudoPlayerDefender.isNewPlayer()) {
					Output.simpleError(player, "That player is too new, cannot track them.");
					return;
				}
				modCurSkill -= defSkill;
			}
			
			if(foundLivingEntity != null) {
				double chanceToCast;
				
				if(foundLivingEntity instanceof Animals)
					chanceToCast = (double)modCurSkill / 300;
				else if(foundLivingEntity instanceof Monster)
					chanceToCast = (double)modCurSkill / 600;
				else if(foundLivingEntity instanceof Player)
					chanceToCast = (double)modCurSkill/1000;
				else {
					chanceToCast = 0;
					//System.out.println("SURV: chanceToCast = 0");
				}
				//System.out.println("ctc:"+chanceToCast);
				// modify so that its .2-1%
				chanceToCast *= .70;
				chanceToCast += .30;
				
				double rand = Math.random();
				//System.out.println("r:"+rand);
				if(rand > chanceToCast) {
					if(foundLivingEntity instanceof Player) {
						Output.simpleError(player, "You see signs of "+targetName+" but you fail to follow the trail.");
						PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
						int defSkill = pseudoPlayerDefender.getSkill("survivalism");
						if(defSkill < 1000 && Utils.isWithin(player.getLocation(), targetPlayer.getLocation(), 250)) {
							targetPlayer.sendMessage(ChatColor.GRAY+"The hairs on the back of your neck stand up...");
						}
					}
					else
						Output.simpleError(player, "You see signs of a "+targetName+" but you fail to follow the trail.");
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-TRACK_STAMINA_COST);
					if(chanceToCast < 1.0)
						possibleSkillGain(player, pseudoPlayer);
					return;
				}
				
				if(foundLivingEntity.getWorld().getName().equalsIgnoreCase(player.getWorld().getName())) {
					//pseudoPlayer.setCompassResetTicks(200);
					if(foundLivingEntity instanceof Player) {
						//Output.positiveMessage(player, "You have succesfully tracked "+targetName+", follow your compass.");
						Location targetLoc = targetPlayer.getLocation();
						Location playerLoc = player.getLocation();
						double angle = Math.atan2(targetLoc.getX() - playerLoc.getX(), targetLoc.getZ() - playerLoc.getZ());
						double angleDegrees = Math.toDegrees(angle);
						if(angleDegrees < 0)
							angleDegrees += 360;
						//System.out.println(""+angleDegrees);
						Location locAt = player.getLocation();
						if((angleDegrees >= 315) || (angleDegrees <= 45)) {
							locAt.setZ(locAt.getZ()+500);
							Output.positiveMessage(player, "You see tracks leading off to the South");
						}
						else if((angleDegrees >= 45) && (angleDegrees <= 135)) {
							locAt.setX(locAt.getX()+500);
							Output.positiveMessage(player, "You see tracks leading off to the East...");
						}
						else if((angleDegrees >= 135) && (angleDegrees <= 225)) {
							locAt.setZ(locAt.getZ()-500);
							Output.positiveMessage(player, "You see tracks leading off to the North");
						}
						else if((angleDegrees >= 225) && (angleDegrees <= 315)) {
							locAt.setX(locAt.getX()-500);
							Output.positiveMessage(player, "You see tracks leading off to the West...");
						}
						else System.out.println("Tracking Angle Problem");
						
						if(Utils.isWithin(playerLoc, targetLoc, 200)) {
							Output.positiveMessage(player, "The tracks are very fresh.");
						}
						else if(Utils.isWithin(playerLoc, targetLoc, 500)) {
							Output.positiveMessage(player, "The tracks are somewhat fresh.");
						}
						else if(Utils.isWithin(playerLoc, targetLoc, 1000)) {
							Output.positiveMessage(player, "The tracks aren't very fresh.");
						}
						else {
							Output.positiveMessage(player, "The tracks are very faint.");
						}
						//player.setCompassTarget(foundLivingEntity.getLocation());
						//player.setCompassTarget(locAt);
					}
					else { 
						//Output.positiveMessage(player, "You have succesfully tracked a "+targetName+", follow your compass.");
						//pseudoPlayer.setCompassResetTicks(200);
						Location targetLoc = foundLivingEntity.getLocation();
						//System.out.println("targetLoc:"+targetLoc);
						//System.out.println("playerPos:"+player.getLocation());
						Location playerLoc = player.getLocation();
						double angle = Math.atan2(targetLoc.getX() - playerLoc.getX(), targetLoc.getZ() - playerLoc.getZ());
						double angleDegrees = Math.toDegrees(angle);
						if(angleDegrees < 0)
							angleDegrees += 360;
						//System.out.println(""+angleDegrees);
						Location locAt = player.getLocation();
						if((angleDegrees >= 315) || (angleDegrees <= 45)) {
							locAt.setZ(locAt.getZ()+500);
							Output.positiveMessage(player, "You see tracks leading off to the South...");
						}
						else if((angleDegrees >= 45) && (angleDegrees <= 135)) {
							locAt.setX(locAt.getX()+500);
							Output.positiveMessage(player, "You see tracks leading off to the East...");
						}
						else if((angleDegrees >= 135) && (angleDegrees <= 225)) {
							locAt.setZ(locAt.getZ()-500);
							Output.positiveMessage(player, "You see tracks leading off to the North...");
						}
						else if((angleDegrees >= 225) && (angleDegrees <= 315)) {
							locAt.setX(locAt.getX()-500);
							Output.positiveMessage(player, "You see tracks leading off to the West...");
						}
						else System.out.println("Tracking Angle Problem");
						
						if(Utils.isWithin(playerLoc, targetLoc, 200)) {
							Output.positiveMessage(player, "The tracks are very fresh.");
						}
						else if(Utils.isWithin(playerLoc, targetLoc, 500)) {
							Output.positiveMessage(player, "The tracks are somewhat fresh.");
						}
						else if(Utils.isWithin(playerLoc, targetLoc, 1000)) {
							Output.positiveMessage(player, "The tracks aren't very fresh.");
						}
						else {
							Output.positiveMessage(player, "The tracks are very faint.");
						}
						//player.setCompassTarget(foundLivingEntity.getLocation());
						//player.setCompassTarget(locAt);
					}
				}
				else {
					if(foundLivingEntity instanceof Player)
						Output.simpleError(player, "You see signs of "+targetName+" but can't seem to follow the trail...");
					else
						Output.simpleError(player, "You see signs of a "+targetName+" but can't seem to follow the trail...");
				}
				
				pseudoPlayer.setStamina(pseudoPlayer.getStamina()-TRACK_STAMINA_COST);
				if(chanceToCast < 1.0)
					possibleSkillGain(player, pseudoPlayer);
			}
		}
		else Output.simpleError(player, "Use \"/survivalism track (player name)\"");
	}
	
	public static void camp(Player player, PseudoPlayer pseudoPlayer, String[] split) {
		int curSkill = pseudoPlayer.getSkill("survivalism");
		if(curSkill < 500) {
			Output.simpleError(player, "Not enough skill - Camping requires 50");
			return;
		}
		
		if(pseudoPlayer.getStamina() < CAMP_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Camping requires "+CAMP_STAMINA_COST+".");
			return;
		}
		
		for(Camp camp : _camps) {
			if(camp.getCreator().equalsIgnoreCase(player.getName())) {
				Output.simpleError(player, "You may only have 1 camp active at a time.");
				return;
			}
		}
		
		// find a log on fire within 4 meters
		/*Location loc = player.getLocation();
		int myX = (int)loc.getX();
		int myY = (int)loc.getY();
		int myZ = (int)loc.getZ();
		boolean found = false;
		Block logFound = null;
		Block fireFound = null;
		for(int x=myX-4; x<=myX+4 && !found; x++) {
			for(int y=myY-4; y<=myY+4 && !found; y++) {
				for(int z=myZ-4; z<=myZ+4 && !found; z++) {
					Block b = player.getWorld().getBlockAt(x,y,z);
					if(b.getType().equals(Material.LOG)){
						Block f = player.getWorld().getBlockAt(x,y+1,z);
						if(f.getType().equals(Material.FIRE)) {
							logFound = b;
							fireFound = f;
							found = true;
							break;
						}
					}
				}
			}
		}*/
		
		if(true) {
			// 0-1
			double chanceToCast = ((double)curSkill-500)/500;
			// modify so that its .2-1%
			chanceToCast *= .8;
			chanceToCast += .2; 
				
			double rand = Math.random();
			if(rand > chanceToCast) {
				Output.simpleError(player, "You failed to successfully create a camp.");
				pseudoPlayer.setStamina(pseudoPlayer.getStamina()-CAMP_STAMINA_COST);
				possibleSkillGain(player, pseudoPlayer);
				return;
			}
			
			//Place a log where you are looking
			Block blockAt = player.getTargetBlock(Spell.getInvisibleBlocks(), 10);
			Block logFound = null;
			Block fireFound = null;
			if(blockAt.getRelative(0,1,0).getType().equals(Material.AIR) && blockAt.getRelative(0,2,0).getType().equals(Material.AIR)) {
				logFound = blockAt.getRelative(0,1,0);
				logFound.setType(Material.LOG);
				fireFound = blockAt.getRelative(0,2,0);
				fireFound.setType(Material.FIRE);
			}
			else {
				Output.simpleError(player, "Invalid location.");
				return;
			}
			
			_camps.add(new Camp(player.getName(), 600, logFound, fireFound));
			player.sendMessage(ChatColor.GOLD+"You set up a temporary camp.");
			pseudoPlayer.setStamina(pseudoPlayer.getStamina()-CAMP_STAMINA_COST);
			possibleSkillGain(player, pseudoPlayer);
		}
	}
	
	public static void tick() {
		int numCamps = _camps.size();
		for(int i=numCamps-1; i>=0; i--) {
			_camps.get(i).tick();
			if(_camps.get(i).isDead())
				_camps.remove(i);
		}
	}

	public static boolean possibleSkillGain(Player player, PseudoPlayer pseudoPlayer) {
		if(pseudoPlayer.isSkillLocked("survivalism"))
			return false;
		
		int curSkill = pseudoPlayer.getSkill("survivalism");
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
			pseudoPlayer.setSkill("survivalism", totalSkill);
			Output.gainSkill(player, "Survivalism", gainAmount, totalSkill);
			Database.updatePlayer(player.getName());
			return true;
		}
		return false;
	}
	
	public static ArrayList<Camp> getCamps() {
		return _camps;
	}
}

