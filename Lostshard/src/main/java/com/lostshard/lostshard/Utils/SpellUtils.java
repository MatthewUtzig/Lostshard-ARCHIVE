package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;

public class SpellUtils {

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
			Material.WALL_SIGN));
	
	public static boolean isValidRuneLocation(Player player, Location location) {

		Block blockAt = location.getBlock();
		Block blockAbove = blockAt.getRelative(0,1,0);
		if(!invisibleBlocks.contains(blockAt.getType()) || !invisibleBlocks.contains(blockAbove.getType())) {
			Output.simpleError(player, "Invalid location, blocked.");
			return false;
		}
		
		return true;
	}
	
	protected static Player playerInLOS(Player player, PseudoPlayer pseudoPlayer, int range) {
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
		
		Clan clan = pseudoPlayer.getClan();
		Party party = pseudoPlayer.getParty();
		
		boolean done = false;
		Player _firstNeutralFound = null;
		while (!done) {
			Block b = aimHit.getNextBlock();
			if(b != null) {
				for(Player p : playersInRange) {
					if(Utils.isWithin(b.getLocation(), p.getLocation(), 1.5)) {
						if(((clan != null) && clan.isInClan(p.getUniqueId())) || ((party != null) && (party.isMember(p)))) {
							return p;
						}
						
						if(_firstNeutralFound == null)
							_firstNeutralFound = p;
					}
				}
			}
			else return _firstNeutralFound;
		}
		
		return _firstNeutralFound;
	}

	public static Block blockInLOS(Player player, int range) {
		Block targetBlock = player.getTargetBlock(invisibleBlocks, range);
		return targetBlock;
	}
}
