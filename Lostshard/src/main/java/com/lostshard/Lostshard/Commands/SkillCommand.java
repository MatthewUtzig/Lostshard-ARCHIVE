package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Skills.Build;
import com.lostshard.Lostshard.Skills.Skill;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.TabUtils;
import com.lostshard.Lostshard.Utils.Utils;

public class SkillCommand extends LostshardCommand {

	public static void skills(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if (args.length == 0) {
			Output.outputSkills(player);
			return;
		} else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reduce")) {
				double amount = 0;
				try {
					amount = Double.parseDouble(args[2]);
				} catch (final Exception e) {
					Output.simpleError(player,
							"Invalid skill amount, use /skills reduce (skill name) (amount)");
					return;
				}
				final int amountInt = (int) (amount * 10);
				if (amountInt > 0) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					int newSkillAmount = skill.getLvl() - amountInt;
					if (newSkillAmount < 0)
						newSkillAmount = 0;
					skill.setLvl(newSkillAmount);
					Output.positiveMessage(player, "You have reduced your "
							+ skill.getName() + ".");
				} else
					Output.simpleError(player, "Must reduce by at least 1.");

				return;
			} else if (args[0].equalsIgnoreCase("increase")) {
				double amount = 0;
				try {
					amount = Double.parseDouble(args[2]);
				} catch (final Exception e) {
					Output.simpleError(player,
							"Invalid skill amount, use /skills increase (skill name) (amount)");
					return;
				}
				int amountInt = (int) (amount * 10);
				if (amountInt > pPlayer.getFreeSkillPoints()) {
					Output.simpleError(
							player,
							"Not enough free points. Remaining: "
									+ Utils.scaledIntToString(pPlayer
											.getFreeSkillPoints()));
					return;

				}
				if (amountInt + pPlayer.getCurrentBuild().getTotalSkillVal() > pPlayer
						.getMaxSkillValTotal()) {
					Output.simpleError(
							player,
							"can't increase skills beyond the max total of "
									+ Utils.scaledIntToString(pPlayer
											.getMaxSkillValTotal()) + ".");
					return;

				}
				if (amountInt > 0) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					final int curSkill = skill.getLvl();
					int newSkill = curSkill + amountInt;
					int dif = 0;
					if (newSkill > 1000) {
						dif = newSkill - 1000;
						newSkill = 1000;
					}
					amountInt -= dif;
					pPlayer.setFreeSkillPoints(pPlayer.getFreeSkillPoints()
							- amountInt);
					skill.setLvl(newSkill);
					Output.positiveMessage(player, "You have increased your "
							+ skill.getName() + ".");
					pPlayer.update();
				} else
					Output.simpleError(player, "Must increase by at least 1.");
				return;
			} else if (args[0].equalsIgnoreCase("lock")) {
				if (args.length == 2) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					skill.setLocked(true);
					Output.positiveMessage(player,
							"You have locked " + skill.getName()
									+ " it should no longer gain.");
				} else
					Output.simpleError(player,
							"Use \"/skills lock (skill name)\"");
				return;
			} else if (args[0].equalsIgnoreCase("unlock")) {
				if (args.length == 2) {
					final String skillName = args[1];
					final Skill skill = pPlayer.getSkillByName(skillName);
					if (skill == null) {
						Output.simpleError(player, "That skill does not exist.");
						return;
					}
					skill.setLocked(false);
					Output.positiveMessage(player,
							"You have unlocked " + skill.getName()
									+ " it should once again gain.");
				} else
					Output.simpleError(player,
							"Use \"/skills unlock (skill name)\"");
				return;
			} else if (args[0].equalsIgnoreCase("give") && player.isOp()) {
				if (args.length >= 2 && args[1].equalsIgnoreCase("points"))
					try {
						final Player targetPlayer = Bukkit.getPlayer(args[2]);
						pPlayer = pm.getPlayer(targetPlayer);
						final double amount = Double.parseDouble(args[3]);
						final int amountInt = (int) (amount * 10);

						pPlayer.setFreeSkillPoints(pPlayer.getFreeSkillPoints()
								+ amountInt);
						Output.positiveMessage(player, "You have increased "
								+ targetPlayer.getName()
								+ " freeskillpoints by " + amount + ".");
						pPlayer.update();
					} catch (final Exception e) {
						Output.simpleError(player,
								"Invalid skill amount, use /skills give points (target) (amount)");
					}
				else
					try {
						final Player targetPlayer = Bukkit.getPlayer(args[1]);
						pPlayer = pm.getPlayer(targetPlayer);
						final double amount = Double.parseDouble(args[3]);
						int amountInt = (int) (amount * 10);
						if (amountInt
								+ pPlayer.getCurrentBuild().getTotalSkillVal() > pPlayer
									.getMaxSkillValTotal()) {
							Output.simpleError(
									player,
									"can't increase skills beyond the max total of "
											+ Utils.scaledIntToString(pPlayer
													.getMaxSkillValTotal())
											+ ".");
							return;

						}
						if (amountInt > 0) {
							final String skillName = args[2];
							final Skill skill = pPlayer
									.getSkillByName(skillName);
							if (skill == null) {
								Output.simpleError(player,
										"That skill does not exist.");
								return;
							}
							final int curSkill = skill.getLvl();
							int newSkill = curSkill + amountInt;
							int dif = 0;
							if (newSkill > 1000) {
								dif = newSkill - 1000;
								newSkill = 1000;
							}
							amountInt -= dif;
							skill.setLvl(newSkill);
							Output.positiveMessage(
									player,
									"You have increased "
											+ targetPlayer.getName() + " "
											+ skill.getName() + " with "
											+ args[3] + ".");
							pPlayer.update();
						} else
							Output.simpleError(player,
									"Must increase by at least 1.");

					} catch (final Exception e) {
					Output.simpleError(player,
								"/skills give (target) (skill) (amount)");
						Output.simpleError(player,
								"/skills give points (target) (amount)");
				}
				return;
			} else
				Output.simpleError(player, "/skills (reduce|lock|increase)");
			return;
		}
		return;
	}

	static PlayerManager pm = PlayerManager.getManager();

	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public SkillCommand(Lostshard plugin) {
		super(plugin, "skills", "resetallskills", "rest", "meditate");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("skills")) {
			skills(sender, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("resetallskills")) {
			resetallskills(sender, args);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("rest")) {
			playerRest(sender);
			return true;
		}else if(cmd.getName().equalsIgnoreCase("meditate")) {
			playerMeditate(sender);
			return true;
		}
		return false;
	}

	private void playerMeditate(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		Output.positiveMessage(player, "You begin meditating...");
		PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setMeditating(true);
	}

	private void playerRest(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		Output.positiveMessage(player, "You begin resting...");
		PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setResting(true);
	}

	private void resetallskills(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			Output.simpleError(sender, "Only players may perform this command.");
			return;
		}
		Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(args.length < 1) {
			pPlayer.getBuilds().set(pPlayer.getCurrentBuildId(), new Build(pPlayer.getCurrentBuild().getId()));
			pPlayer.update();
			Output.positiveMessage(player, "Skills wiped, but you diden chose a skill to increase.");
		}else if(args.length < 2){
			Build build = new Build(pPlayer.getCurrentBuild().getId());
			Skill skill = build.getSkillByName(args[0]);
			if(skill == null) {
				Output.simpleError(player, "You chose a invalid skill to increase.");
				return;
			}
			pPlayer.getBuilds().set(pPlayer.getCurrentBuildId(), build);
			skill.setLvl(500);
			Output.positiveMessage(player, "Skills wiped, "+skill.getName()+" set to 50.0");
		}
		pPlayer.update();
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String alias, String[] args) {
		if(args.length == 0)
			return TabUtils.StringTab(args, "reduce", "lock", "unlock", "increase");
		else if(args.length == 1 && args[0].equalsIgnoreCase("lock") )
			return TabUtils.StringTab(args, "Skill1", "Skill2", "Skill3", "Tell Defman that he forgot something.");
		else
			return TabUtils.empty();
	}
}
