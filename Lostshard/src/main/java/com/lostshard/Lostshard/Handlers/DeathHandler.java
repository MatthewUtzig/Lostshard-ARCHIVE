package com.lostshard.Lostshard.Handlers;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.lostshard.Lostshard.Manager.NPCManager;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.NPC.NPCType;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Utils;

public class DeathHandler {

	public static void deathMessage(Player player,
			List<RecentAttacker> recentAttackers, PlayerDeathEvent event) {
		final int numAttackers = recentAttackers.size();
		final Random random = new Random();
		if (numAttackers > 0) {
			String deathMessage = Utils.getDisplayName(player)
					+ ChatColor.WHITE + " was killed by";
			String attackers = "";
			for (int i = 0; i < numAttackers; i++) {
				NPC guard = NPCManager.getManager().getByUUID(recentAttackers.get(i).getUUID());
				if(guard != null && guard.getType().equals(NPCType.GUARD)) {
					deathMessage = player.getDisplayName() + ChatColor.WHITE
							+ " was executed by an "+guard.getPlot().getName()+" guard.";
					attackers = "";
					break;
				}
				final Player p = Bukkit.getPlayer(recentAttackers.get(i)
						.getUUID());
				if (p != null)
					if (i == numAttackers - 1) {
						if (attackers != "")
							attackers += ChatColor.WHITE + " and "
									+ Utils.getDisplayName(p) + ChatColor.WHITE
									+ ".";
						else
							attackers += ChatColor.WHITE + " "
									+ Utils.getDisplayName(p) + ChatColor.WHITE
									+ ".";
					} else
						attackers += ChatColor.WHITE + " "
								+ Utils.getDisplayName(p);
			}
			deathMessage += attackers;
			event.setDeathMessage(deathMessage);
		} else if (player.getLastDamageCause() == null) {
			String message = Utils.getDisplayName(player) + ChatColor.WHITE;
			final int randInt = random.nextInt(5);

			switch (randInt) {
			case 0:
				message += " decided to end it all.";
				break;
			case 1:
				message += " Gomer Pyled.";
				break;
			case 2:
				message += " committed suicide!";
				break;
			case 3:
				message += " just gave up.";
				break;
			case 4:
				message += " really likes the spawn.";
				break;
			}

			event.setDeathMessage(message);
		} else {
			final EntityDamageEvent e = player.getLastDamageCause();
			String message = Utils.getDisplayName(player) + ChatColor.WHITE;
			final Plot plot = ptm.findPlotAt(player.getLocation());
			if (e == null)
				message += " died.";
			else if (e.getCause().equals(DamageCause.BLOCK_EXPLOSION)) {
				message += " exploded";
				if (Math.random() < .05)
					message += " just like the predator *beep*";
				else if (Math.random() < .05)
					message += " just like jaws *Smile, you son of a bitch!*";
				else
					message += ".";
			} else if (e.getCause().equals(DamageCause.DROWNING)) {
				message += " drowned";
				if (plot != null)
					message += " in " + plot.getName();
				message += ".";
			} else if (e.getCause().equals(DamageCause.FALL)) {
				message += " fell to their doom";
				if (plot != null)
					message += " in " + plot.getName();
				message += ".";
			} else if (e.getCause().equals(DamageCause.FIRE)
					|| e.getCause().equals(DamageCause.FIRE_TICK)) {
				message += " burned alive";
				if (plot != null)
					message += " in " + plot.getName();
				if (Math.random() < .1)
					message += " just like Nicolas Cage. *AAaahhhhhhh!*";
				else
					message += ".";
			} else if (e.getCause().equals(DamageCause.LAVA)) {
				message += " melted in a pit of lava";
				if (plot != null)
					message += " in " + plot.getName();
				if (Math.random() < .1)
					message += " just like the Terminator. *thumbs up*";
				else
					message += ".";
			} else if (e.getCause().equals(DamageCause.LIGHTNING)) {
				message += " was struck by lightning";
				if (plot != null)
					message += " in " + plot.getName();
				if (Math.random() < .1)
					message += " just like Powder. *is pale*";
				else
					message += ".";
			} else if (e.getCause().equals(DamageCause.SUFFOCATION)) {
				message += " suffocated";
				if (plot != null)
					message += " in " + plot.getName();
				if (Math.random() < .1)
					message += " just like David Carradine. *ew*";
				else
					message += ".";
			} else if (e.getCause().equals(DamageCause.VOID))
				message += " fell into the abyss.";
			else if (e.getCause().equals(DamageCause.ENTITY_ATTACK)
					|| e instanceof EntityDamageByEntityEvent
					&& ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile) {
				final EntityDamageByEntityEvent eDBEE = (EntityDamageByEntityEvent) e;
				Entity damager = eDBEE.getDamager();
				if (damager instanceof Projectile)
					damager = (Entity) ((Projectile) damager).getShooter();
				if (damager instanceof Creeper)
					message += " was killed by a Creeper.";
				else if (damager instanceof PigZombie)
					message += " was killed by a Zombie Pigman.";
				else if (damager instanceof Zombie)
					message += " was killed by a Zombie.";
				else if (damager instanceof Skeleton)
					message += " was shot by a Skeleton.";
				else if (damager instanceof Ghast)
					message += " was killed by a Ghast.";
				else if (damager instanceof Giant)
					message += " was killed by a huge fucking Zombie.";
				else if (damager instanceof Slime)
					message += " was killed by a Slime.";
				else if (damager instanceof Spider)
					message += " was killed by a Spider.";
				else if (damager instanceof Witch)
					message += " was killed by a Witch";
				else if (damager instanceof Silverfish)
					message += " was killed by a Silverfish";
				else if (damager instanceof IronGolem)
					message += " was killed by a Iron Golem";
				else if (damager instanceof Wither)
					message += " was killed by a Wither";
				else if (damager instanceof Enderman)
					message += " was killed by a Enderman";
				else if (damager instanceof Blaze)
					message += " was killed by a Blaze";
				else if (damager instanceof CaveSpider)
					message += " was killed by a Cave Spider";
				else if (damager instanceof MagmaCube)
					message += " was killed by a Magma Cube";
				else if (damager instanceof Endermite)
					message += " was killed by a Endermite";
				else if (damager instanceof Guardian)
					message += " was killed by a Guardian";
				else if (damager instanceof EnderDragon)
					message += " was killed by the Enderdragon";
				else
					message += "'s life was tragically cut short.";
			} else if (e.getCause().equals(DamageCause.ENTITY_EXPLOSION))
				message += " exploded.";
			else
				message += " died.";
			event.setDeathMessage(message);
		}
	}

	public static void handleDeath(EntityDeathEvent event) {

		final Random random = new Random();
		final Entity entity = event.getEntity();
		
		// event.setDroppedExp(0);

		// event.setDroppedExp(event.getDroppedExp() * 2);

		// Start of Horse
		
		if(!(entity instanceof Player) && (entity.getLastDamageCause() == null || event.getEntity().getKiller() == null) && !(entity instanceof Creeper && entity.getLastDamageCause().getCause().equals(DamageCause.PROJECTILE))) {
			event.getDrops().clear();
			return;
		}

		if (entity instanceof Horse)
			event.getDrops().clear();

		// End of Horse
		
		// Start of extra drops
		if (entity instanceof Monster)
			if (entity instanceof Zombie)
				entity.getLocation()
						.getWorld()
						.dropItemNaturally(
								entity.getLocation(),
								new ItemStack(Material.FEATHER, random
										.nextInt(4) + 1));
			else if (entity instanceof Skeleton) {
				entity.getLocation()
						.getWorld()
						.dropItemNaturally(entity.getLocation(),
								new ItemStack(Material.BONE, 1));
				entity.getLocation()
						.getWorld()
						.dropItemNaturally(
								entity.getLocation(),
								new ItemStack(Material.ARROW,
										random.nextInt(7) + 1));
			} else if (entity instanceof Spider)
				entity.getLocation()
						.getWorld()
						.dropItemNaturally(entity.getLocation(),
								new ItemStack(Material.STRING, 1));
			else if (entity instanceof Creeper)
				entity.getLocation()
						.getWorld()
						.dropItemNaturally(entity.getLocation(),
								new ItemStack(Material.SULPHUR, 1));
			else if (entity instanceof Animals)
				if (entity instanceof Pig)
					entity.getLocation()
							.getWorld()
							.dropItemNaturally(entity.getLocation(),
									new ItemStack(Material.PORK, 1));
				else if (entity instanceof Chicken)
					entity.getLocation()
							.getWorld()
							.dropItemNaturally(
									entity.getLocation(),
									new ItemStack(Material.FEATHER, random
											.nextInt(5) + 1));
				else if (entity instanceof Cow)
					entity.getLocation()
							.getWorld()
							.dropItemNaturally(entity.getLocation(),
									new ItemStack(Material.LEATHER, 2));
				else if (entity instanceof Sheep)
					entity.getLocation()
							.getWorld()
							.dropItemNaturally(entity.getLocation(),
									new ItemStack(Material.WOOL, 1));
				else if (entity instanceof Squid)
					entity.getLocation()
							.getWorld()
							.dropItemNaturally(entity.getLocation(),
									new ItemStack(Material.INK_SACK, 1));
		// Survivalism

		if (entity instanceof Animals)
			if (lastAttackers.containsKey(entity)) {
				final Entity lastAttackerEntity = lastAttackers.get(entity);
				if (lastAttackerEntity instanceof Player) {
					final Player attackerPlayer = (Player) lastAttackerEntity;
					final PseudoPlayer pseudoPlayerAttacker = pm
							.getPlayer(attackerPlayer);

					final int survSkill = pseudoPlayerAttacker
							.getCurrentBuild().getSurvivalism().getLvl();
					final double chanceToDropApple = (double) survSkill / 2000;
					// System.out.println("CTC:" + chanceToDropApple);
					final double rand = Math.random();
					// System.out.println("R:" + rand);
					if (rand < chanceToDropApple) {
						ItemStack itemStack = null;
						if (entity instanceof Pig)
							itemStack = new ItemStack(Material.PORK, 1);
						else if (entity instanceof Chicken)
							itemStack = new ItemStack(Material.FEATHER, 1);
						else if (entity instanceof Cow)
							itemStack = new ItemStack(Material.LEATHER, 1);
						else if (entity instanceof Sheep)
							itemStack = new ItemStack(Material.WOOL, 1);
						else if (entity instanceof Squid)
							itemStack = new ItemStack(Material.INK_SACK, 1);
						if (itemStack != null)
							entity.getWorld().dropItemNaturally(
									entity.getLocation(), itemStack);
						// System.out.println("Dropping "+itemStack.getType().name());
					}
					// Survivalism.possibleSkillGain(attackerPlayer,
					// pseudoPlayerAttacker);
				}

				// do stuff
				// /lastAttackers.remove(entity);
			}

		// End of survivalism
		lastAttackers.remove(entity);
	}

	public static void handleDeath(PlayerDeathEvent event) {
		final Player player = event.getEntity();

		if (player.getKiller() instanceof Player) {
			final Player killer = player.getKiller();

			final PseudoPlayer pKiller = pm.getPlayer(killer);

			final Material wep = killer.getItemInHand().getType();

			if (pKiller != null && ItemUtils.isAxe(wep)
					|| ItemUtils.isSword(wep)) {
				final int swordsSkill = pKiller.getCurrentBuild().getBlades()
						.getLvl();
				final int lumberjackingSkill = pKiller.getCurrentBuild()
						.getLumberjacking().getLvl();

				if (swordsSkill >= 1000 || lumberjackingSkill >= 1000)
					if (Math.random() <= .2) {
						final ItemStack skull = new ItemStack(
								Material.SKULL_ITEM, 1,
								(short) SkullType.PLAYER.ordinal());
						final SkullMeta skullMeta = (SkullMeta) skull
								.getItemMeta();
						skullMeta.setOwner(player.getName());
						skullMeta.setDisplayName(ChatColor.RESET
								+ player.getName() + "'s Head");
						skull.setItemMeta(skullMeta);
						event.getDrops().add(skull);
					}
			}
		}

		System.out.println("PLAYER DEATH: " + player.getName() + " @ "
				+ player.getLocation());

		// NPCHandler.playerDied(player);
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		if (pseudoPlayer == null)
			return;

		// if(pseudoPlayer._claiming) {
		// ArrayList<Plot> controlPoints = PlotHandler.getControlPoints();
		// for(Plot cP : controlPoints) {
		// if(cP.isUnderAttack() &&
		// cP._capturingPlayerName.equals(player.getName())) {
		// cP.failCaptureDied(player);
		// }
		// }
		// }

		pseudoPlayer.getTimer().bleedTick = 0;
		pseudoPlayer.getTimer().spawnTicks = 0;
		pseudoPlayer.setPvpTicks(0);
		pseudoPlayer.setDieLog(0);
		// pseudoPlayer._lastChanceTicks = 0;

		if (player.getVehicle() instanceof Horse)
			((Horse) player.getVehicle()).setHealth(0d);

		// pseudoPlayer._clearTicks = 5;
		final List<RecentAttacker> recentAttackers = pseudoPlayer
				.getRecentAttackers();

		pseudoPlayer.getTimer().lastDeath = new Date().getTime();

		for (final RecentAttacker recentAttacker : recentAttackers) {
			if (recentAttacker.isNotCrime())
				continue;

			final UUID attackerUUID = recentAttacker.getUUID();
			final Player attackerPlayer = Bukkit.getPlayer(attackerUUID);

			if (attackerPlayer != null) {
				final PseudoPlayer attackerPseudo = pm
						.getPlayer(attackerPlayer);
				if (!(attackerPseudo.isCriminal() && !pseudoPlayer.isCriminal()))
					continue;

				attackerPlayer.sendMessage("You have murdered "
						+ player.getName());
				if (attackerPseudo != null) {

					attackerPseudo.setMurderCounts(attackerPseudo
							.getMurderCounts() + 1);
					if (attackerPseudo.isMurderer())
						attackerPlayer.setDisplayName(ChatColor.RED
								+ attackerPlayer.getName());
				}
			}
		}
		// Start of death messages

		DeathHandler.deathMessage(player, recentAttackers, event);
		
		// Rank stuff

		RankHandler.rank(pseudoPlayer);
		
		pseudoPlayer.clearRecentAttackers();
	}

	static PlayerManager pm = PlayerManager.getManager();

	static PlotManager ptm = PlotManager.getManager();

	public static HashMap<Entity, Entity> lastAttackers = new HashMap<Entity, Entity>();

	public static HashSet<Entity> recentDeath = new HashSet<Entity>();

}
