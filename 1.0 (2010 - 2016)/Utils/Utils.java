package com.lostshard.RPG.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.Groups.Clans.Clan;

public class Utils {	
	private static RPG _plugin;
	
	public static void init(RPG plugin) {
		_plugin = plugin;
	}
	
	public static RPG getPlugin() {
		return _plugin;
	}
	
	public static boolean isWithin(Location loc1, Location loc2, double amount) {
		double fastDist = fastDistance(loc1,loc2);
		if(fastDist < Math.pow(amount,2) && loc1.getWorld().equals(loc2.getWorld()))
			return true;
		return false;
	}
	
	public static int AdjustDamageForArmor(Player player, int damage) {
		int defensePoints = 0;
		
		ItemStack helmet = player.getInventory().getHelmet();
		if(helmet != null) {
			if(helmet.getType().equals(Material.LEATHER_HELMET))
				defensePoints += 1;
			else if(helmet.getType().equals(Material.GOLD_HELMET))
				defensePoints += 2;
			else if(helmet.getType().equals(Material.CHAINMAIL_HELMET))
				defensePoints += 2;
			else if(helmet.getType().equals(Material.IRON_HELMET))
				defensePoints += 2;
			else if(helmet.getType().equals(Material.DIAMOND_HELMET))
				defensePoints += 3;
		}
		
		ItemStack chest = player.getInventory().getChestplate();
		if(chest != null) {
			if(chest.getType().equals(Material.LEATHER_CHESTPLATE))
				defensePoints += 3;
			else if(chest.getType().equals(Material.GOLD_CHESTPLATE))
				defensePoints += 5;
			else if(chest.getType().equals(Material.CHAINMAIL_CHESTPLATE))
				defensePoints += 5;
			else if(chest.getType().equals(Material.IRON_CHESTPLATE))
				defensePoints += 6;
			else if(chest.getType().equals(Material.DIAMOND_CHESTPLATE))
				defensePoints += 8;
		}
		
		ItemStack leggings = player.getInventory().getLeggings();
		if(leggings != null) {
			if(leggings.getType().equals(Material.LEATHER_LEGGINGS))
				defensePoints += 2;
			else if(leggings.getType().equals(Material.GOLD_LEGGINGS))
				defensePoints += 3;
			else if(leggings.getType().equals(Material.CHAINMAIL_LEGGINGS))
				defensePoints += 4;
			else if(leggings.getType().equals(Material.IRON_LEGGINGS))
				defensePoints += 5;
			else if(leggings.getType().equals(Material.DIAMOND_LEGGINGS))
				defensePoints += 6;
		}
		
		ItemStack boots = player.getInventory().getBoots();
		if(boots != null) {
			if(boots.getType().equals(Material.LEATHER_BOOTS))
				defensePoints += 1;
			else if(boots.getType().equals(Material.GOLD_BOOTS))
				defensePoints += 1;
			else if(boots.getType().equals(Material.CHAINMAIL_BOOTS))
				defensePoints += 1;
			else if(boots.getType().equals(Material.IRON_BOOTS))
				defensePoints += 2;
			else if(boots.getType().equals(Material.DIAMOND_BOOTS))
				defensePoints += 3;
		}
		
		float defensePercent = (((float)defensePoints * 4) / 100);
		
		int adjustedDamage = (int)Math.floor((float)damage * (1-defensePercent));
		
		return adjustedDamage;
	}
	
	public static double fastDistance(Location loc1, Location loc2) {
		double fastDist = Math.pow((loc2.getX()-loc1.getX()),2)+Math.pow((loc2.getY()-loc1.getY()), 2)+Math.pow((loc2.getZ()-loc1.getZ()), 2);
		return fastDist;
	}
	
	public static Block findHighestBlockAt(World world, int x, int y, int z) {
		for(int i=127; i>= 0; i--) {
			if(world.getBlockTypeIdAt(x,i,z) == 0) {
				// found a space, see if there is ground below it
				if(world.getBlockTypeIdAt(x,i-1,z) != 0) {
					
				}
			}
		}
		return null;
	}
	
	public static double distance(Location loc1, Location loc2) {
		return Math.sqrt(fastDistance(loc1, loc2));
	}
	
	public static String implode(ArrayList<String> toJoin) {
        String joinedString = "";
        for(int i=0; i<toJoin.size(); i++) {
        	joinedString+=toJoin.get(i);
        	if(i < toJoin.size()-1)
        		joinedString+=",";
        }
        return joinedString;
	}
	
	public static String scaledIntToString(int x) {
	    return new BigDecimal(BigInteger.valueOf(x), 1).toString();
	}
	
	public static Block blockPointingAt(Player player, int range) {
		return null;
		/*
		double radRot = Math.toRadians((originRotation)%360);
		double radPitch = Math.toRadians((originPitch)%360);
		radPitch += Math.PI/2;
		radRot += Math.PI/2;
		for(int i=4; i<range*5; i++) {
			double r = i/5;
			double x = r*Math.sin(radPitch)*Math.cos(radRot);
			double z = r*Math.sin(radPitch)*Math.sin(radRot);
			double y = r*Math.cos(radPitch);
			//new Block(51, (int)originLocation.x+(int)x,1+(int)originLocation.y+(int)y,(int)originLocation.z+(int)z).update();
			Block b = etc.getServer().getBlockAt((int)originLocation.x+(int)x, 1+(int)originLocation.y+(int)y, (int)originLocation.z+(int)z);
			if(b.getType() != 0) {
				// no line of sight
				return new Location((double)b.getX(), (double)b.getY(), (double)b.getZ());
			}
			else {
				// air block, see if player is there
				for(Player p : playersInRange) {
					if(((int)p.getX() == b.getX()) && (((int)p.getY() == b.getY()) || ((int)p.getY()+1 == b.getY())) && ((int)p.getZ() == b.getZ())) {
						// found player
						return p.getLocation();
					}
				}
			}
		}
		return null;*/
	}
	
	public static void loadChunkAtLocation(Location location) {
		World curWorld = location.getWorld();
		if (!curWorld.isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
			curWorld.loadChunk(location.getBlockX() >> 4, location.getBlockZ() >> 4);
		}
	}
	
	public static void setPlayerTitle(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		String playerTitle = "";
		if(player.isOp()) {
			playerTitle = ChatColor.GOLD + pseudoPlayer._activeTitle + " " + player.getName();
		}
		else if(pseudoPlayer._activeTitle != "" || pseudoPlayer._activeTitle != " ")
			playerTitle = pseudoPlayer._activeTitle + " " + player.getDisplayName();
		else
			playerTitle = player.getDisplayName();
		
		if(pseudoPlayer.getClan() != null) {
			playerTitle += "\n" + "[" + pseudoPlayer.getClan().getName()+ "]";
		}
		////SpoutManager.getAppearanceManager().setGlobalTitle(player, playerTitle);
		
		Clan clan = pseudoPlayer.getClan();
		if(clan!=null) {
			try {
				if(clan.getCloakURL()!=null && !clan.getCloakURL().equals("")){}
					////SpoutManager.getAppearanceManager().setGlobalCloak(player, clan.getCloakURL());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String itemToName (int itemId, int durability) {
		switch(itemId) {
			case 35:
				switch(durability) {
					case 0:
						return "White Wool";
					case 1:
						return "Orange Wool";
					case 2:
						return "Magenta Wool";
					case 3:
						return "Light Blue Wool";
					case 4:
						return "Yellow Wool";
					case 5:
						return "Light Green Wool";
					case 6:
						return "Pink Wool";
					case 7:
						return "Gray Wool";
					case 8:
						return "Light Gray Wool";
					case 9:
						return "Cyan Wool";
					case 10:
						return "Purple Wool";
					case 11:
						return "Blue Wool";
					case 12:
						return "Brown Wool";
					case 13:
						return "Dark Green Wool";
					case 14:
						return "Red Wool";
					case 15:
						return "Black Wool";
					default:
						return "I AM ERROR";
				}
			case 44:
				switch(durability) {
					case 0:
						return "Stone Slab";
					case 1:
						return "Sandstone Slab";
					case 2:
						return "Wooden Slab";
					case 3:
						return "Cobblestone Slab";
					default:
						return "I AM ERROR";
				}
			case 351:
				switch(durability) {
					case 0:
						return "Ink Sac";
					case 1:
						return "Rose Red";
					case 2:
						return "Cactus Green";
					case 3:
						return "Cocoa Beans";
					case 4:
						return "Lapis Lazuli";
					case 5:
						return "Purple Dye";
					case 6:
						return "Cyan Dye";
					case 7:
						return "Light Gray Dye";
					case 8:
						return "Gray Dye";
					case 9:
						return "Pink Dye";
					case 10:
						return "Lime Dye";
					case 11:
						return "Dandelion Dye";
					case 12:
						return "Light Blue Dye";
					case 13:
						return "Magenta Dye";
					case 14:
						return "Orange Dye";
					case 15:
						return "Bone Meal";
					default:
						return "I AM ERROR";
				}
			default:
				return "I AM ERROR";
		}
	}
	
	public static String implodeSplit(String[] split, int startIndex) {
		String message = "";
    	int splitMessageLength = split.length;
		for(int i=startIndex; i<splitMessageLength; i++) {
			message += split[i];
			if(i < splitMessageLength-1)
				message+= " ";
		}
		message = message.trim();
		return message;
	}

}