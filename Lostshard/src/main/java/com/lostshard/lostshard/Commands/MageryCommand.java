package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.Skill;
import com.lostshard.lostshard.Utils.Output;

public class MageryCommand implements CommandExecutor, TabCompleter {
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public MageryCommand(Lostshard plugin) {
		plugin.getCommand("skills").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("skills")) {
			skills(sender, args);
			return true;
		}
		return false;
	}
	
	public static void skills(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)){
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if(args.length == 0) {
	    	Output.outputSkills(player);
	    	return;
		}
		else if(args.length > 0) {
			if(args[0].equalsIgnoreCase("reduce")) {
				try {
					double amount = Double.parseDouble(args[2]);
					int amountInt = (int)(amount*10);
					if(amountInt > 0) {
	        			String skillName = args[1];
	        			Skill skill = pPlayer.getSkillByName(skillName);
	        			if(skill == null) {
	        				Output.simpleError(player, "That skill does not exist.");
	        				return;
	        			}
	        			int newSkillAmount = skill.getLvl()-amountInt;
	        			if(newSkillAmount < 0)
	        				newSkillAmount = 0;
	        			skill.setLvl(newSkillAmount);	
	        			Output.positiveMessage(player, "You have reduced your "+skillName+".");
					}
					else Output.simpleError(player, "Must reduce by at least 1.");
	    			
				}
				catch(Exception e) {
					Output.simpleError(player, "Invalid skill amount, use /skills reduce (skill name) (amount)");
				}
				return;
			}
//			else if(args[0].equalsIgnoreCase("increase")) {
//				try {
//					double amount = Double.parseDouble(args[2]);
//					int amountInt = (int)(amount*10);
//					if(amountInt > pseudoPlayer.getFreeSkillPointsRemaining()) {
//						Output.simpleError(player, "Not enough free points. Remaining: "+Utils.scaledIntToString(pseudoPlayer.getFreeSkillPointsRemaining()));
//						return true;
//						
//					}
//					if(amountInt + pseudoPlayer.getTotalSkillVal() > pseudoPlayer.getMaxSkillValTotal()) {
//						Output.simpleError(player, "Cannot increase skills beyond the max total of "+Utils.scaledIntToString(pseudoPlayer.getMaxSkillValTotal())+".");
//						return true;
//						
//					}
//					if(amountInt > 0) {
//	        			String skillName = split[2];
//	        			int curSkill = pseudoPlayer.getSkill(skillName);
//	        			int newSkill = curSkill + amountInt;
//	        			int dif = 0;
//	        			if(newSkill > 1000) {
//	        				dif = newSkill - 1000;
//	        				newSkill = 1000;
//	        			}
//	        			amountInt -= dif;
//	        			if(pseudoPlayer.setSkill(skillName, newSkill)) {
//	        				pseudoPlayer.setFreeSkillPointsRemaining(pseudoPlayer.getFreeSkillPointsRemaining()-amountInt);
//	        				Database.updatePlayer(player.getName());
//	        				Output.positiveMessage(player, "You have increased your "+skillName+".");
//	        			}
//	        			else Output.simpleError(player, "That skill does not exist.");
//					}
//					else Output.simpleError(player, "Must increase by at least 1.");
//	    			
//				}
//				catch(Exception e) {
//					Output.simpleError(player, "Invalid skill amount, use /skills reduce (skill name) (amount)");
//				}
//				return true;
//			}
			else if(args[0].equalsIgnoreCase("lock")) {
				if(args.length == 2) {
					String skillName = args[1];
					Skill skill = pPlayer.getSkillByName(skillName);
					if(skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					skill.setLocked(true);
					Output.positiveMessage(player, "You have locked "+skillName+" it should no longer gain.");
				}
				else Output.simpleError(player, "Use \"/skills lock (skill name)\"");
				return;
			}
			else if(args[0].equalsIgnoreCase("unlock")) {
				if(args.length == 2) {
					String skillName = args[1];
					Skill skill = pPlayer.getSkillByName(skillName);
					if(skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					skill.setLocked(false);
					Output.positiveMessage(player, "You have unlocked "+skillName+" it should once again gain.");
				}
				else Output.simpleError(player, "Use \"/skills unlock (skill name)\"");
				return;
			}
//			else if(split[1].equalsIgnoreCase("give") && player.isOp()) {
//				if(split.length >= 3 && split[2].equalsIgnoreCase("points")){
//					try {	
//					pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(Bukkit.getPlayer(split[3]).getName());
//					double amount = Double.parseDouble(split[4]);
//					int amountInt = (int)(amount*10);
//					
//					pseudoPlayer.setFreeSkillPointsRemaining(pseudoPlayer.getFreeSkillPointsRemaining()+amountInt);
//					Output.positiveMessage(player, "You have increased " + pseudoPlayer.getName() + " freeskillpoints by " + amount + ".");
//					}
//					catch(Exception e) {
//	    				Output.simpleError(player, "Invalid skill amount, use /skills give points (target) (amount)");
//	    			}
//				}else{
//				try {
//					pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(Bukkit.getPlayer(split[2]).getName());
//					double amount = Double.parseDouble(split[4]);
//					int amountInt = (int)(amount*10);
//					if(amountInt + pseudoPlayer.getTotalSkillVal() > pseudoPlayer.getMaxSkillValTotal()) {
//						Output.simpleError(player, "Cannot increase skills beyond the max total of "+Utils.scaledIntToString(pseudoPlayer.getMaxSkillValTotal())+".");
//						return true;
//						
//					}
//					if(amountInt > 0) {
//	        			String skillName = split[3];
//	        			int curSkill = pseudoPlayer.getSkill(skillName);
//	        			int newSkill = curSkill + amountInt;
//	        			int dif = 0;
//	        			if(newSkill > 1000) {
//	        				dif = newSkill - 1000;
//	        				newSkill = 1000;
//	        			}
//	        			amountInt -= dif;
//	        			if(pseudoPlayer.setSkill(skillName, newSkill)) {
//	        				Database.updatePlayer(pseudoPlayer.getName());
//	        				Output.positiveMessage(player, "You have increased "+pseudoPlayer.getName()+" "+skillName+" with "+split[4]+ ".");
//	        			}
//	        			else Output.simpleError(player, "That skill does not exist.");
//					}
//					else Output.simpleError(player, "Must increase by at least 1.");
//	    			
//				}
//				catch(Exception e) {
//	    			Output.simpleError(player, "/skills give (target) (skill) (amount)");	
//	    			Output.simpleError(player, "/skills give points (target) (amount)");
//				}
//				}
//				return;
//			}
			return;
		}
		return;
	}
	
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
