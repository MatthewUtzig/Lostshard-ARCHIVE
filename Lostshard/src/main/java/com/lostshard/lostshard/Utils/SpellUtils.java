package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;

public class SpellUtils {

	public static Block blockInLOS(Player player, int range) {
		final Block targetBlock = player.getTargetBlock(invisibleBlocks, range);
		return targetBlock;
	}

	public static ArrayList<LivingEntity> enemiesInLOS(Player player,
			PseudoPlayer pseudoPlayer, int range) {
		final AimBlock aimHit = new AimBlock(player, range, .5);

		final Clan clan = pseudoPlayer.getClan();
		final Party party = pseudoPlayer.getParty();

		final ArrayList<LivingEntity> livingEntities = new ArrayList<LivingEntity>();

		for (final Player p : Bukkit.getOnlinePlayers())
			livingEntities.add(p);

		for (final LivingEntity lE : player.getWorld().getLivingEntities()) {
			if (lE instanceof Player)
				if (lE.hasMetadata("NPC"))
					continue;
			livingEntities.add(lE);
		}

		final ArrayList<LivingEntity> livingEntitiesInRange = new ArrayList<LivingEntity>();
		for (final LivingEntity lE : livingEntities)
			if (lE instanceof Player) {
				final Player p = (Player) lE;
				if (p == player)
					continue;
				if (clan != null)
					if (clan.isInClan(p.getUniqueId()))
						continue;
				if (party != null)
					if (party.isMember(p))
						continue;

				if (Utils
						.isWithin(player.getLocation(), p.getLocation(), range))
					livingEntitiesInRange.add(p);
			} else if (Utils.isWithin(player.getLocation(), lE.getLocation(),
					range))
				livingEntitiesInRange.add(lE);

		final ArrayList<LivingEntity> validTargets = new ArrayList<LivingEntity>();
		if (livingEntitiesInRange.size() <= 0)
			return validTargets;

		final boolean done = false;
		while (!done) {
			final Block b = aimHit.getNextBlock();
			if (b != null) {
				for (final LivingEntity lE : livingEntitiesInRange)
					if (Utils.isWithin(b.getLocation(), lE.getLocation(), 3))
						validTargets.add(lE);
			} else
				return validTargets;
		}
	}
	
	public static Block faceBlockInLOS(Player player, int range) {
		AimBlock aimHit = new AimBlock(player, range, .5);
		return aimHit.getFaceBlock();
	}

	public static boolean isValidRuneLocation(Player player, Location location) {
		final Block blockAt = location.getBlock();
		final Block blockAbove = blockAt.getRelative(0, 1, 0);
		if (blockAbove == null || blockAt == null)
			return true;
		if (!invisibleBlocks.contains(blockAt.getType())
				|| !invisibleBlocks.contains(blockAbove.getType())) {
			Output.simpleError(player, "Invalid location, blocked.");
			return false;
		}

		return true;
	}

	public static Player playerInLOS(Player player, int range) {
		final AimBlock aimHit = new AimBlock(player, range, .5);

		final Collection<? extends Player> onlinePlayers = Bukkit
				.getOnlinePlayers();
		final ArrayList<Player> playersInRange = new ArrayList<Player>();
		for (final Player p : onlinePlayers)
			if (p != player)
				if (Utils
						.isWithin(player.getLocation(), p.getLocation(), range))
					playersInRange.add(p);

		if (playersInRange.size() <= 0)
			return null;

		final PseudoPlayer pPlayer = pm.getPlayer(player);

		final Clan clan = pPlayer.getClan();
		final Party party = pPlayer.getParty();

		final boolean done = false;
		Player firstNeutralFound = null;
		while (!done) {
			final Block b = aimHit.getNextBlock();
			if (b != null) {
				for (final Player p : playersInRange)
					if (Utils.isWithin(b.getLocation(), p.getLocation(), 1.5)) {
						if (clan != null && clan.isInClan(p.getUniqueId())
								|| party != null && party.isMember(p))
							return p;

						if (firstNeutralFound == null)
							firstNeutralFound = p;
					}
			} else
				return firstNeutralFound;
		}
	}

	static PlayerManager pm = PlayerManager.getManager();

	public static HashSet<Material> invisibleBlocks = new HashSet<Material>(
			Arrays.asList(Material.AIR, Material.SNOW, Material.FIRE,
					Material.TORCH, Material.LADDER, Material.RED_MUSHROOM,
					Material.RED_ROSE, Material.YELLOW_FLOWER,
					Material.BROWN_MUSHROOM, Material.REDSTONE_WIRE,
					Material.WOOD_PLATE, Material.STONE_PLATE, Material.PORTAL,
					Material.LAVA, Material.WATER, Material.STONE_BUTTON,
					Material.LEVER, Material.CARPET, Material.IRON_PLATE,
					Material.GOLD_PLATE, Material.WOOD_BUTTON,
					Material.LONG_GRASS, Material.RAILS, Material.VINE,
					Material.SIGN_POST, Material.WALL_SIGN, null));
}
