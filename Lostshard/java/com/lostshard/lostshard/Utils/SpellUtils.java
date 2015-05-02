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
		Block targetBlock = player.getTargetBlock(invisibleBlocks, range);
		return targetBlock;
	}
	
	public static ArrayList<LivingEntity> enemiesInLOS(Player player, PseudoPlayer pseudoPlayer, int range) {
		AimBlock aimHit = new AimBlock(player, range, .5);
		
		Clan clan = pseudoPlayer.getClan();
		Party party = pseudoPlayer.getParty();
		
		ArrayList<LivingEntity> livingEntities = new ArrayList<LivingEntity>();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			livingEntities.add(p);
		}
		
		for(LivingEntity lE : player.getWorld().getLivingEntities()) 
		{
			if(lE instanceof Player){
				if(lE.hasMetadata("NPC"))
					continue;
			}
			livingEntities.add(lE);
		}
		
		ArrayList<LivingEntity> livingEntitiesInRange = new ArrayList<LivingEntity>();
		for(LivingEntity lE : livingEntities) {
			if(lE instanceof Player) {
				Player p = (Player)lE;
				if(p == player)
					continue;
				if(clan != null) {
					if(clan.isInClan(p.getUniqueId())) {
						continue;
					}
				}
				if(party != null) {
					if(party.isMember(p)) {
						continue;
					}
				}
				
				if(Utils.isWithin(player.getLocation(), p.getLocation(), range)) {
						livingEntitiesInRange.add(p);
				}
			}
			else {
				if(Utils.isWithin(player.getLocation(), lE.getLocation(), range)) {
					livingEntitiesInRange.add(lE);
				}
			}
		}
		
		
		ArrayList<LivingEntity> validTargets = new ArrayList<LivingEntity>();
		if(livingEntitiesInRange.size() <= 0)
			return validTargets;
		
		boolean done = false;
		while (!done) {
			Block b = aimHit.getNextBlock();
			if(b != null) {
				for(LivingEntity lE : livingEntitiesInRange) {
					if(Utils.isWithin(b.getLocation(), lE.getLocation(), 3)) {
						validTargets.add(lE);
					}
				}
			}
			else return validTargets;
		}
		
		return validTargets;
	}
	
	public static boolean isValidRuneLocation(Player player, Location location) {
		Block blockAt = location.getBlock();
		Block blockAbove = blockAt.getRelative(0,1,0);
		if(blockAbove == null || blockAt == null)
			return true;
		if(!invisibleBlocks.contains(blockAt.getType()) || !invisibleBlocks.contains(blockAbove.getType())) {
			Output.simpleError(player, "Invalid location, blocked.");
			return false;
		}
		
		return true;
	}
	
	public static Player playerInLOS(Player player, int range) {
		AimBlock aimHit = new AimBlock(player, range, .5);
		
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		ArrayList<Player> playersInRange = new ArrayList<Player>();
		for(Player p : onlinePlayers) {
			if(p != player) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), range)) {
					playersInRange.add(p);
				}
			}
		}
		
		if(playersInRange.size() <= 0)
			return null;
		
		PseudoPlayer pPlayer = pm.getPlayer(player);
		
		Clan clan = pPlayer.getClan();
		Party party = pPlayer.getParty();
		
		boolean done = false;
		Player firstNeutralFound = null;
		while (!done) {
			Block b = aimHit.getNextBlock();
			if(b != null) {
				for(Player p : playersInRange) {
					if(Utils.isWithin(b.getLocation(), p.getLocation(), 1.5)) {
						if(((clan != null) && clan.isInClan(p.getUniqueId())) || ((party != null) && (party.isMember(p)))) {
							return p;
						}
						
						if(firstNeutralFound == null)
							firstNeutralFound = p;
					}
				}
			}
			else return firstNeutralFound;
		}
		
		return firstNeutralFound;
	}
	
	static PlayerManager pm = PlayerManager.getManager();

	public static HashSet<Material> invisibleBlocks = new HashSet<Material>(Arrays.asList(
			Material.AIR,
			Material.SNOW,
			Material.FIRE,
			Material.TORCH,
			Material.LADDER,
			Material.RED_MUSHROOM,
			Material.RED_ROSE,
			Material.YELLOW_FLOWER,
			Material.BROWN_MUSHROOM,
			Material.REDSTONE_WIRE,
			Material.WOOD_PLATE,
			Material.STONE_PLATE,
			Material.PORTAL,
			Material.LAVA,
			Material.WATER,
			Material.STONE_BUTTON,
			Material.LEVER,
			Material.CARPET,
			Material.IRON_PLATE,
			Material.GOLD_PLATE,
			Material.WOOD_BUTTON,
			Material.LONG_GRASS,
			Material.RAILS,
			Material.VINE,
			Material.SIGN_POST,
			Material.WALL_SIGN,
			null));
}
