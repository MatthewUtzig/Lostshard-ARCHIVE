package com.lostshard.Lostshard.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Groups.Clan;
import com.lostshard.Lostshard.Objects.Groups.Party;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.Plots.PlotManager;
import com.lostshard.Plots.Models.Plot;
import com.lostshard.Plots.Models.Plot.PlotToggleable;
import com.lostshard.Plots.Models.Plot.PlotUpgrade;

/**
 * @author Jacob Handling entity's hitting entity's.
 */
public class PVPHandler {

	static PlayerManager pm = PlayerManager.getManager();

	static PlotManager ptm = PlotManager.getManager();

	public static void Attack(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		final Player player = (Player) event.getEntity();
		Entity attackerEntity = event.getDamager();

		if (event.getDamager() instanceof Projectile)
			if (((Projectile) attackerEntity).getShooter() instanceof Entity)
				attackerEntity = (Entity) ((Projectile) attackerEntity).getShooter();

		if (!(attackerEntity instanceof Player))
			return;
		final Player attacker = (Player) attackerEntity;

		criminalAction(player, attacker);
	}

	/**
	 * @param attacker
	 * @param defender
	 * @returnEntity can damage Entity. Checking if they are in clan or other
	 *               reasons if they should not be able to damage each other.
	 */
	public static boolean canEntityAttackEntity(Entity attacker, Entity defender) {

		/**
		 * Checking if its an NPC.
		 */
		if (defender.hasMetadata("NPC"))
			return false;

		/**
		 * Ensuring that the defender and is a Player.
		 */
		if (!(defender instanceof Player))
			return true;

		/**
		 * Handling player hitting with feather.
		 */
		if (attacker instanceof Player && defender instanceof Player
				&& ((Player) attacker).getItemInHand().getType().equals(Material.FEATHER)) {
			((Player) defender)
					.sendMessage(ChatColor.GRAY + ((Player) attacker).getName() + " tickled you with a feather.");
			((Player) attacker).sendMessage(
					ChatColor.GRAY + "You have tickled" + ((Player) defender).getName() + "with a feather.");
			return false;
		}

		/**
		 * Allowing potion to be thrown on all if positive.
		 */
		if (attacker instanceof ThrownPotion)
			for (final PotionEffect pe : ((ThrownPotion) attacker).getEffects())
				if (pe.getType().equals(PotionEffectType.HEAL) || pe.getType().equals(PotionEffectType.REGENERATION)
						|| pe.getType().equals(PotionEffectType.ABSORPTION)
						|| pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)
						|| pe.getType().equals(PotionEffectType.FAST_DIGGING)
						|| pe.getType().equals(PotionEffectType.FIRE_RESISTANCE)
						|| pe.getType().equals(PotionEffectType.HEALTH_BOOST)
						|| pe.getType().equals(PotionEffectType.INCREASE_DAMAGE)
						|| pe.getType().equals(PotionEffectType.INVISIBILITY)
						|| pe.getType().equals(PotionEffectType.SPEED)
						|| pe.getType().equals(PotionEffectType.WATER_BREATHING)
						|| pe.getType().equals(PotionEffectType.NIGHT_VISION))
					return true;

		/**
		 * Handling attacker if the damage came from an projectile and getting
		 * the shooter.
		 */
		if (attacker instanceof Projectile)
			if (((Projectile) attacker).getShooter() instanceof Entity)
				attacker = (Entity) ((Projectile) attacker).getShooter();

		/**
		 * Ensuring that the attacker is a Player.
		 */
		if (!(attacker instanceof Player))
			return true;

		/**
		 * Checking if the defender is standing in a none PVP plot.
		 */
		final Plot plotAtDefender = ptm.findPlotAt(defender.getLocation());
		if (plotAtDefender != null && !plotAtDefender.getToggleables().contains(PlotToggleable.NOPVP))
			return false;

		/**
		 * Checking if the attacker is standing a none PVP plot.
		 */
		final Plot plotAtAttacker = ptm.findPlotAt(attacker.getLocation());
		if (plotAtAttacker != null && !plotAtAttacker.getToggleables().contains(PlotToggleable.NOPVP))
			return false;

		/**
		 * Allowing players to damage them self.
		 */
		if (attacker == defender)
			return true;

		final PseudoPlayer pAttacker = pm.getPlayer((Player) attacker);
		final PseudoPlayer pDefender = pm.getPlayer((Player) defender);

		/**
		 * Handling ff
		 */
		if (pDefender.isFriendlyFire())
			return true;

		/**
		 * Checking if they are in same clan or not.
		 */
		if (pAttacker.getClan() != null && pDefender.getClan() != null && pAttacker.getClan() == pDefender.getClan())
			return false;

		/**
		 * Checking if they are in same party or not.
		 */
		if (pAttacker.getParty() != null && pDefender.getParty() != null
				&& pAttacker.getParty() == pDefender.getParty())
			return false;

		return true;
	}

	public static void criminalAction(Player player, Player playerDamager) {
		if (player.hasMetadata("NPC"))
			return;
		if (player == null || playerDamager == null)
			return;

		if (player.getName().equals(playerDamager.getName()))
			return;

		final PseudoPlayer pseudoPlayerDefender = pm.getPlayer(player);
		final PseudoPlayer pseudoPlayerAttacker = pm.getPlayer(playerDamager);

		// add pvp ticks
		pseudoPlayerDefender.setPvpTicks(150); // 10 seconds
		pseudoPlayerDefender.setEngageInCombatTicks(300);
		// attacker attacked someone, he is engaging in combat

		pseudoPlayerAttacker.setPvpTicks(150);
		pseudoPlayerAttacker.setEngageInCombatTicks(300); // attacker attacked
															// someone, he is
															// engaging in
															// combat

		// only criminal if it happens in the normal world
		boolean notCrim = false;
		if (!player.getWorld().getEnvironment().equals(Environment.NORMAL))
			notCrim = true;
		final Plot plot = ptm.findPlotAt(player.getLocation());
		// defender is on a plot
		if (plot != null) {
			if (plot.isCapturepoint())
				notCrim = true;
			if (plot.getUpgrades().contains(PlotUpgrade.ARENA))
				notCrim = true;
			// and the attacker is a member of the plot
			if (plot.isFriendOrAbove(playerDamager))
				// and the defender is NOT a member of the plot
				notCrim = true;
		}

		// If the defender has attacked anyone within 30 seconds they engaged in
		// combat willingly
		// so attacking them will not be criminal even if they are blue

		final Party party = pseudoPlayerDefender.getParty();
		if (party != null)
			if (party.isMember(playerDamager.getUniqueId()))
				return;
		final Clan clan = pseudoPlayerDefender.getClan();
		if (clan != null)
			if (clan.inClan(playerDamager.getUniqueId()))
				return;

		// Determine if a criminal action has taken place
		if (pseudoPlayerDefender.isLawfull() && !notCrim) {
			// attacked non criminal, thats a criminal action
			if (!pseudoPlayerAttacker.isCriminal())
				playerDamager.sendMessage(ChatColor.RED + "You have committed a criminal action");
			pseudoPlayerAttacker.setCriminal(3000);

			if (!pseudoPlayerAttacker.isMurderer())
				playerDamager.setDisplayName(ChatColor.GRAY + playerDamager.getName());
		}

		final RecentAttacker recentAttacker = new RecentAttacker(playerDamager.getUniqueId(), 300);
		recentAttacker.setNotCrim(notCrim);
		pseudoPlayerDefender.addRecentAttacker(recentAttacker);
	}
}
