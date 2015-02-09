package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.Skill;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class SkillCommand implements CommandExecutor, TabCompleter {
	
	static PlayerManager pm = PlayerManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public SkillCommand(Lostshard plugin) {
		plugin.getCommand("skills").setExecutor(this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("skills")) {
			skills(sender, args);
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void skills(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)){
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(args.length == 0) {
	    	Output.outputSkills(player);
	    	return;
		}
		else if(args.length > 0) {
			if(args[0].equalsIgnoreCase("reduce")) {
				double amount = 0;
				try {
					amount = Double.parseDouble(args[2]);
				}
				catch(Exception e) {
					Output.simpleError(player, "Invalid skill amount, use /skills reduce (skill name) (amount)");
					return;
				}
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
	        			Output.positiveMessage(player, "You have reduced your "+skill.getName()+".");
					}
					else Output.simpleError(player, "Must reduce by at least 1.");
	    			
				return;
			}
			else if(args[0].equalsIgnoreCase("increase")) {
				double amount = 0;
				try {
					amount = Double.parseDouble(args[2]);
				}
				catch(Exception e) {
					Output.simpleError(player, "Invalid skill amount, use /skills increase (skill name) (amount)");
					return;
				}
				int amountInt = (int)(amount*10);
				if(amountInt > pPlayer.getFreeSkillPoints()) {
					Output.simpleError(player, "Not enough free points. Remaining: "+Utils.scaledIntToString(pPlayer.getFreeSkillPoints()));
					return;
					
				}
				if(amountInt + pPlayer.getCurrentBuild().getTotalSkillVal() > pPlayer.getMaxSkillValTotal()) {
					Output.simpleError(player, "Cannot increase skills beyond the max total of "+Utils.scaledIntToString(pPlayer.getMaxSkillValTotal())+".");
					return;
					
				}
				if(amountInt > 0) {
        			String skillName = args[1];
        			Skill skill = pPlayer.getSkillByName(skillName);
        			if(skill == null) {
        				Output.simpleError(player, "That skill does not exist.");
        			}
        			int curSkill = skill.getLvl();
        			int newSkill = curSkill + amountInt;
        			int dif = 0;
        			if(newSkill > 1000) {
        				dif = newSkill - 1000;
        				newSkill = 1000;
        			}
        			amountInt -= dif;
    				pPlayer.setFreeSkillPoints(pPlayer.getFreeSkillPoints()-amountInt);
    				skill.setLvl(newSkill);
    				Output.positiveMessage(player, "You have increased your "+skill.getName()+".");
    				pPlayer.update();
				}
				else
					Output.simpleError(player, "Must increase by at least 1.");
				return;
			}
			else if(args[0].equalsIgnoreCase("lock")) {
				if(args.length == 2) {
					String skillName = args[1];
					Skill skill = pPlayer.getSkillByName(skillName);
					if(skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					skill.setLocked(true);
					Output.positiveMessage(player, "You have locked "+skill.getName()+" it should no longer gain.");
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
					Output.positiveMessage(player, "You have unlocked "+skill.getName()+" it should once again gain.");
				}
				else Output.simpleError(player, "Use \"/skills unlock (skill name)\"");
				return;
			}
			else if(args[0].equalsIgnoreCase("give") && player.isOp()) {
				if(args.length >= 2 && args[1].equalsIgnoreCase("points")){
					try {
					Player targetPlayer = Bukkit.getPlayer(args[2]);
					pPlayer = pm.getPlayer(targetPlayer);
					double amount = Double.parseDouble(args[3]);
					int amountInt = (int)(amount*10);
					
					pPlayer.setFreeSkillPoints(pPlayer.getFreeSkillPoints()+amountInt);
					Output.positiveMessage(player, "You have increased " + targetPlayer.getName() + " freeskillpoints by " + amount + ".");
					pPlayer.update();
					}
					catch(Exception e) {
	    				Output.simpleError(player, "Invalid skill amount, use /skills give points (target) (amount)");
	    			}
				}else{
				try {
					Player targetPlayer = Bukkit.getPlayer(args[1]);
					pPlayer = pm.getPlayer(targetPlayer);
					double amount = Double.parseDouble(args[3]);
					int amountInt = (int)(amount*10);
					if(amountInt + pPlayer.getCurrentBuild().getTotalSkillVal() > pPlayer.getMaxSkillValTotal()) {
						Output.simpleError(player, "Cannot increase skills beyond the max total of "+Utils.scaledIntToString(pPlayer.getMaxSkillValTotal())+".");
						return;
						
					}
					if(amountInt > 0) {
	        			String skillName = args[2];
	        			Skill skill = pPlayer.getSkillByName(skillName);
	        			if(skill == null) {
	        				Output.simpleError(player, "That skill does not exist.");
	        				return;
	        			}
	        			int curSkill = skill.getLvl();
	        			int newSkill = curSkill + amountInt;
	        			int dif = 0;
	        			if(newSkill > 1000) {
	        				dif = newSkill - 1000;
	        				newSkill = 1000;
	        			}
	        			amountInt -= dif;
        				Output.positiveMessage(player, "You have increased "+targetPlayer.getName()+" "+skill.getName()+" with "+args[3]+ ".");
        				pPlayer.update();
					}
					else Output.simpleError(player, "Must increase by at least 1.");
	    			
				}
				catch(Exception e) {
	    			Output.simpleError(player, "/skills give (target) (skill) (amount)");	
	    			Output.simpleError(player, "/skills give points (target) (amount)");
				}
				}
				return;
			}
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
