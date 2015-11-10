package com.lostshard.Lostshard.Skills;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Objects.Camp;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;
import com.lostshard.Lostshard.Utils.Utils;

@Embeddable
public class SurvivalismSkill extends Skill {

	public static void camp(Player player) {
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final int curSkill = pPlayer.getCurrentBuild().getSurvivalism()
				.getLvl();
		if (curSkill < 500) {
			Output.simpleError(player, "Not enough skill - Camping requires 50");
			return;
		}

		if (pPlayer.getStamina() < CAMP_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Camping requires "
					+ CAMP_STAMINA_COST + ".");
			return;
		}

		for (final Camp camp : camps)
			if (camp.getCreator().equals(player.getUniqueId())) {
				Output.simpleError(player,
						"You may only have 1 camp active at a time.");
				return;
			}

		// 0-1
		double chanceToCast = ((double) curSkill - 500) / 500;
		chanceToCast *= .8;
		chanceToCast += .2;

		final double rand = Math.random();
		if (rand > chanceToCast) {
			Output.simpleError(player,
					"You failed to successfully create a camp.");
			pPlayer.setStamina(pPlayer.getStamina() - CAMP_STAMINA_COST);
			final int gain = pPlayer.getCurrentBuild().getSurvivalism()
					.skillGain(pPlayer);
			Output.gainSkill(player, "Survivalism", gain, curSkill);
			return;
		}

		// Place a log where you are looking
		final Block blockAt = player.getTargetBlock(SpellUtils.invisibleBlocks,
				10);
		Block logFound = null;
		Block fireFound = null;
		if (blockAt.getRelative(0, 1, 0).getType().equals(Material.AIR)
				&& blockAt.getRelative(0, 2, 0).getType().equals(Material.AIR)) {
			logFound = blockAt.getRelative(0, 1, 0);
			logFound.setType(Material.LOG);
			fireFound = blockAt.getRelative(0, 2, 0);
			fireFound.setType(Material.FIRE);
		} else {
			Output.simpleError(player, "Invalid location.");
			return;
		}

		camps.add(new Camp(player.getUniqueId(), 600, logFound, fireFound));
		player.sendMessage(ChatColor.GOLD + "You set up a temporary camp.");
		pPlayer.setStamina(pPlayer.getStamina() - CAMP_STAMINA_COST);
		final int gain = pPlayer.getCurrentBuild().getSurvivalism()
				.skillGain(pPlayer);
		Output.gainSkill(player, "Survivalism", gain, curSkill);
	}
	
	public static void entityDeath(EntityDeathEvent event) {
		Entity e = event.getEntity();
		Entity k = e.getLastDamageCause().getEntity();
		if(e instanceof Animals && k != null && k instanceof Player) {
			PseudoPlayer pPlayer = pm.getPlayer((Player) k);
			for(ItemStack i : event.getDrops())
				i.setAmount((int) (i.getAmount()*(Math.random()*pPlayer.getCurrentBuild().getSurvivalism().getLvl()/330+1)));
			Output.gainSkill((Player) k, "Magery", pPlayer.getCurrentBuild().getSurvivalism().skillGain(pPlayer), pPlayer.getCurrentBuild().getSurvivalism().getLvl());
		}
	}

	public static ArrayList<Camp> getCamps() {
		return camps;
	}
	
	public static void onHoe(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;
		final Block block = event.getClickedBlock();
		final ItemStack itemInHand = event.getPlayer().getItemInHand();
		final Player player = event.getPlayer();
		if (!block.getType().equals(Material.GRASS))
			return;
		if (!(itemInHand.getType().equals(Material.WOOD_HOE)
				|| itemInHand.getType().equals(Material.STONE_HOE)
				|| itemInHand.getType().equals(Material.IRON_HOE)
				|| itemInHand.getType().equals(Material.DIAMOND_HOE) || itemInHand
				.getType().equals(Material.GOLD_HOE)))
			return;

		final Plot plot = ptm.findPlotAt(block.getLocation());
		if (plot != null)
			if (plot.isProtected())
				if (!plot.isAllowedToBuild(player)) {
					Output.simpleError(player,
							"You can't do that, this plot is protected.");
					event.setCancelled(true);
					return;
				}

		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final int curSurvSkill = pPlayer.getCurrentBuild().getSurvivalism()
				.getLvl();
		final double percent = curSurvSkill / 1000.0;
		final int hoeRange = (int) (3.0 * percent);

		for (int x = block.getX() - hoeRange; x <= block.getX() + hoeRange; x++)
			for (int y = block.getY() - hoeRange; y <= block.getY() + hoeRange; y++)
				for (int z = block.getZ() - hoeRange; z <= block.getZ()
						+ hoeRange; z++) {
					final Block blockAt = block.getWorld().getBlockAt(x, y, z);
					if (Utils.isWithin(block.getLocation(),
							blockAt.getLocation(), hoeRange))
						if (block.getWorld().getBlockAt(x, y, z).getType()
								.equals(Material.GRASS)) {
							blockAt.setType(Material.SOIL);
							final double rand = Math.random();
							if (rand < .2)
								blockAt.getWorld()
										.dropItemNaturally(
												new Location(
														blockAt.getWorld(),
														blockAt.getLocation()
																.getX() + .5,
														blockAt.getLocation()
																.getY() + 1.5,
														blockAt.getLocation()
																.getZ() + .5),
												new ItemStack(Material.SEEDS, 1));
							else if (rand < .04) {
								final double rand2 = Math.random();
								if (rand2 < .5)
									blockAt.getWorld().dropItemNaturally(
											blockAt.getLocation(),
											new ItemStack(
													Material.RED_MUSHROOM, 1));
								else
									blockAt.getWorld()
											.dropItemNaturally(
													blockAt.getLocation(),
													new ItemStack(
															Material.BROWN_MUSHROOM,
															1));
							}
						}
				}
	}

	public static void onPlayerDamage(EntityDamageEvent event) {
		if (event.getCause().equals(DamageCause.FALL))
			if (event.getEntity() instanceof Player) {
				final Player player = (Player) event.getEntity();
				final PseudoPlayer pPlayer = pm.getPlayer(player);
				if (pPlayer.getCurrentBuild().getSurvivalism().getLvl() >= 500)
					event.setDamage(DamageModifier.BASE,
							event.getDamage(DamageModifier.BASE) / 2);
			}
	}

	public static void setCamps(ArrayList<Camp> camps) {
		SurvivalismSkill.camps = camps;
	}

	public static void track(Player player, String[] args) {
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final int curSkill = pPlayer.getCurrentBuild().getSurvivalism()
				.getLvl();
		if (args.length == 1) {
			if (pPlayer.getStamina() < TRACK_STAMINA_COST) {
				Output.simpleError(player,
						"Not enough stamina - Tracking requires "
								+ TRACK_STAMINA_COST + ".");
				return;
			}

			final String targetName = args[0];

			if (targetName.equalsIgnoreCase(player.getName())) {
				Output.simpleError(player, "You can't track yourself.");
				return;
			}

			final Player targetPlayer = Bukkit.getPlayer(targetName);
			// if(targetPlayer != null && targetPlayer.isOp()) {
			// Output.simpleError(player, "can't find "+targetName+".");
			// return;
			// }

			int modCurSkill = curSkill;

			LivingEntity foundLivingEntity = null;
			if (targetPlayer == null) {
				// not a player
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

				if (targetName.equalsIgnoreCase("zombie"))
					trackZombie = true;
				if (targetName.equalsIgnoreCase("skeleton"))
					trackSkeleton = true;
				if (targetName.equalsIgnoreCase("creeper"))
					trackCreeper = true;
				if (targetName.equalsIgnoreCase("spider"))
					trackSpider = true;
				if (targetName.equalsIgnoreCase("ghast"))
					trackGhast = true;
				if (targetName.equalsIgnoreCase("sheep"))
					trackSheep = true;
				if (targetName.equalsIgnoreCase("squid"))
					trackSquid = true;
				if (targetName.equalsIgnoreCase("chicken"))
					trackChicken = true;
				if (targetName.equalsIgnoreCase("slime"))
					trackSlime = true;
				if (targetName.equalsIgnoreCase("pig"))
					trackPig = true;
				if (targetName.equalsIgnoreCase("cow"))
					trackCow = true;
				if (targetName.equalsIgnoreCase("wolf"))
					trackWolf = true;

				if (targetName.equalsIgnoreCase("ocelot"))
					trackOcelot = true;
				if (targetName.equalsIgnoreCase("blaze"))
					trackBlaze = true;

				final ArrayList<LivingEntity> lE = new ArrayList<LivingEntity>();
				final List<LivingEntity> livingEntities = player.getWorld()
						.getLivingEntities();
				for (final LivingEntity livingEntity : livingEntities) {
					if (trackSquid)
						if (livingEntity instanceof Squid)
							lE.add(livingEntity);
					if (trackWolf)
						if (livingEntity instanceof Wolf)
							lE.add(livingEntity);
					if (livingEntity instanceof Animals) {
						if (trackSheep)
							if (livingEntity instanceof Sheep)
								lE.add(livingEntity);
						if (trackSquid)
							if (livingEntity instanceof Squid)
								lE.add(livingEntity);
						if (trackChicken)
							if (livingEntity instanceof Chicken)
								lE.add(livingEntity);
						if (trackPig)
							if (livingEntity instanceof Pig)
								lE.add(livingEntity);
						if (trackCow)
							if (livingEntity instanceof Cow)
								lE.add(livingEntity);
						if (trackOcelot)
							if (livingEntity instanceof Ocelot)
								lE.add(livingEntity);
					} else if (livingEntity instanceof Monster) {
						if (trackZombie)
							if (livingEntity instanceof Zombie)
								lE.add(livingEntity);
						if (trackSkeleton)
							if (livingEntity instanceof Skeleton)
								lE.add(livingEntity);
						if (trackCreeper)
							if (livingEntity instanceof Creeper)
								lE.add(livingEntity);
						if (trackSpider)
							if (livingEntity instanceof Spider)
								lE.add(livingEntity);
						if (trackGhast)
							if (livingEntity instanceof Ghast)
								lE.add(livingEntity);
						if (trackSlime)
							if (livingEntity instanceof Slime)
								lE.add(livingEntity);
						if (trackBlaze)
							if (livingEntity instanceof Blaze)
								lE.add(livingEntity);
					}
				}

				if (lE.size() > 0) {
					if (lE.get(0) instanceof Monster)
						if (curSkill < 250) {
							Output.simpleError(player,
									"You must have 25 Survivalism to track monsters.");
							return;
						}
					LivingEntity closestEntity = lE.get(0);
					double closestDist = Utils.fastDistance(
							closestEntity.getLocation(), player.getLocation());
					for (final LivingEntity livingEntity : lE) {
						final double dist = Utils.fastDistance(
								livingEntity.getLocation(),
								player.getLocation());
						if (dist < closestDist) {
							closestDist = dist;
							closestEntity = livingEntity;
						}
					}
					foundLivingEntity = closestEntity;
				} else {
					Output.simpleError(player, "can't find " + targetName + ".");
					return;
				}
			} else {
				final PseudoPlayer pseudoPlayerDefender = pm
						.getPlayer(targetPlayer);
				final int defSkill = pseudoPlayerDefender.getCurrentBuild()
						.getSurvivalism().getLvl();

				if (curSkill < 500) {
					Output.simpleError(player,
							"You must have 50 Survivalism to track a player.");
					return;
				}
				foundLivingEntity = targetPlayer;
				// if(false) {
				// Output.simpleError(player,
				// "That player is too new, can't track them.");
				// return;
				// }
				modCurSkill -= defSkill;
			}

			if (foundLivingEntity != null) {
				double chanceToCast;

				if (foundLivingEntity instanceof Animals)
					chanceToCast = (double) modCurSkill / 300;
				else if (foundLivingEntity instanceof Monster)
					chanceToCast = (double) modCurSkill / 600;
				else if (foundLivingEntity instanceof Player)
					chanceToCast = (double) modCurSkill / 1000;
				else
					chanceToCast = 0;
				chanceToCast *= .70;
				chanceToCast += .30;

				final double rand = Math.random();
				if (rand > chanceToCast) {
					if (foundLivingEntity instanceof Player) {
						Output.simpleError(player, "You see signs of "
								+ targetName
								+ " but you fail to follow the trail.");
						final PseudoPlayer pseudoPlayerDefender = pm
								.getPlayer(targetPlayer);
						final int defSkill = pseudoPlayerDefender
								.getCurrentBuild().getSurvivalism().getLvl();
						if (defSkill < 1000
								&& Utils.isWithin(player.getLocation(),
										targetPlayer.getLocation(), 250))
							targetPlayer
									.sendMessage(ChatColor.GRAY
											+ "The hairs on the back of your neck stand up...");
					} else
						Output.simpleError(player, "You see signs of a "
								+ targetName
								+ " but you fail to follow the trail.");
					pPlayer.setStamina(pPlayer.getStamina()
							- TRACK_STAMINA_COST);
					if (chanceToCast < 1.0) {
						final int gain = pPlayer.getCurrentBuild()
								.getSurvivalism().skillGain(pPlayer);
						Output.gainSkill(player, "Survivalism", gain, curSkill);
					}
					return;
				}

				if (foundLivingEntity.getWorld().getName()
						.equalsIgnoreCase(player.getWorld().getName())) {
					if (foundLivingEntity instanceof Player) {
						final Location targetLoc = targetPlayer.getLocation();
						final Location playerLoc = player.getLocation();
						final double angle = Math.atan2(targetLoc.getX()
								- playerLoc.getX(), targetLoc.getZ()
								- playerLoc.getZ());
						double angleDegrees = Math.toDegrees(angle);
						if (angleDegrees < 0)
							angleDegrees += 360;
						final Location locAt = player.getLocation();
						if (angleDegrees >= 315 || angleDegrees <= 45) {
							locAt.setZ(locAt.getZ() + 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the South");
						} else if (angleDegrees >= 45 && angleDegrees <= 135) {
							locAt.setX(locAt.getX() + 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the East...");
						} else if (angleDegrees >= 135 && angleDegrees <= 225) {
							locAt.setZ(locAt.getZ() - 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the North");
						} else if (angleDegrees >= 225 && angleDegrees <= 315) {
							locAt.setX(locAt.getX() - 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the West...");
						} else
							System.out.println("Tracking Angle Problem");

						if (Utils.isWithin(playerLoc, targetLoc, 200))
							Output.positiveMessage(player,
									"The tracks are very fresh.");
						else if (Utils.isWithin(playerLoc, targetLoc, 500))
							Output.positiveMessage(player,
									"The tracks are somewhat fresh.");
						else if (Utils.isWithin(playerLoc, targetLoc, 1000))
							Output.positiveMessage(player,
									"The tracks aren't very fresh.");
						else
							Output.positiveMessage(player,
									"The tracks are very faint.");
					} else {
						final Location targetLoc = foundLivingEntity
								.getLocation();
						final Location playerLoc = player.getLocation();
						final double angle = Math.atan2(targetLoc.getX()
								- playerLoc.getX(), targetLoc.getZ()
								- playerLoc.getZ());
						double angleDegrees = Math.toDegrees(angle);
						if (angleDegrees < 0)
							angleDegrees += 360;
						final Location locAt = player.getLocation();
						if (angleDegrees >= 315 || angleDegrees <= 45) {
							locAt.setZ(locAt.getZ() + 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the South...");
						} else if (angleDegrees >= 45 && angleDegrees <= 135) {
							locAt.setX(locAt.getX() + 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the East...");
						} else if (angleDegrees >= 135 && angleDegrees <= 225) {
							locAt.setZ(locAt.getZ() - 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the North...");
						} else if (angleDegrees >= 225 && angleDegrees <= 315) {
							locAt.setX(locAt.getX() - 500);
							Output.positiveMessage(player,
									"You see tracks leading off to the West...");
						} else
							System.out.println("Tracking Angle Problem");

						if (Utils.isWithin(playerLoc, targetLoc, 200))
							Output.positiveMessage(player,
									"The tracks are very fresh.");
						else if (Utils.isWithin(playerLoc, targetLoc, 500))
							Output.positiveMessage(player,
									"The tracks are somewhat fresh.");
						else if (Utils.isWithin(playerLoc, targetLoc, 1000))
							Output.positiveMessage(player,
									"The tracks aren't very fresh.");
						else
							Output.positiveMessage(player,
									"The tracks are very faint.");
					}
				} else if (foundLivingEntity instanceof Player)
					Output.simpleError(player, "You see signs of " + targetName
							+ " but can't seem to follow the trail...");
				else
					Output.simpleError(player, "You see signs of a "
							+ targetName
							+ " but can't seem to follow the trail...");

				pPlayer.setStamina(pPlayer.getStamina() - TRACK_STAMINA_COST);
				if (chanceToCast < 1.0) {
					final int gain = pPlayer.getCurrentBuild().getSurvivalism()
							.skillGain(pPlayer);
					Output.gainSkill(player, "Survivalism", gain, curSkill);
				}
			}
		} else
			Output.simpleError(player, "Use \"/track (player name)\"");
	}

	private static final int TRACK_STAMINA_COST = 25;

	private static final int CAMP_STAMINA_COST = 50;

	private static ArrayList<Camp> camps = new ArrayList<Camp>();

	public SurvivalismSkill() {
		super();
		this.setName("Survivalism");
		this.setBaseProb(.2);
		this.setScaleConstant(60);
		this.setMat(Material.COMPASS);
	}

	@Override
	public String howToGain() {
		return "You can gain survivalism by camping and tracking";
	}
}
