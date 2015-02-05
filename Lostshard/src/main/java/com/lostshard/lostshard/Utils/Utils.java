package com.lostshard.lostshard.Utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class Utils {

	static PlayerManager pm = PlayerManager.getManager();
	
	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(Player player, String[] args,
			int argsnr) {
		if (args.length < argsnr + 1)
			return null;
		String targetName = args[argsnr];
		return Bukkit.getOfflinePlayer(targetName);
	}

	public static String scaledIntToString(int x) {
	    return new BigDecimal(BigInteger.valueOf(x), 1).toString();
	}
	
	@SuppressWarnings("deprecation")
	public static Player getPlayer(Player player, String[] args, int argsnr) {
		if (args.length < argsnr + 1)
			return null;
		String targetName = args[argsnr];
		return Bukkit.getPlayer(targetName);
	}

	public static String booleanToString(boolean bol, String ifTrue,
			String ifFalse) {
		return bol ? ifTrue : ifFalse;
	}

	public static String booleanToString(boolean bol) {
		return booleanToString(bol, "Yes", "No");
	}

	public static boolean isWithin(Location loc1, Location loc2, double radius) {
		return fastDistance(loc1, loc2) < Math.pow(radius, 2)
				&& loc1.getWorld().equals(loc2.getWorld());
	}

	public static double fastDistance(Location loc1, Location loc2) {
		double fastDist = Math.pow((loc2.getX() - loc1.getX()), 2)
				+ Math.pow((loc2.getY() - loc1.getY()), 2)
				+ Math.pow((loc2.getZ() - loc1.getZ()), 2);
		return fastDist;
	}

	public static double distance(Location loc1, Location loc2) {
		return Math.sqrt(fastDistance(loc1, loc2));
	}

	public static double adjustDamageForArmor(Player player, double newDamage) {
		int defensePoints = 0;

		ItemStack helmet = player.getInventory().getHelmet();
		ItemStack chestplate = player.getInventory().getChestplate();
		ItemStack leggings = player.getInventory().getLeggings();
		ItemStack boots = player.getInventory().getBoots();

		// Helmet
		defensePoints += helmet == null ? 0 : helmet
				.equals(Material.LEATHER_HELMET) ? 1 : helmet
				.equals(Material.GOLD_HELMET) ? 2 : helmet
				.equals(Material.CHAINMAIL_HELMET) ? 3 : helmet
				.equals(Material.IRON_HELMET) ? 3 : helmet
				.equals(Material.DIAMOND_HELMET) ? 3 : 0;
		// Chestplate
		defensePoints += chestplate == null ? 0 : chestplate
				.equals(Material.LEATHER_CHESTPLATE) ? 3 : chestplate
				.equals(Material.GOLD_CHESTPLATE) ? 5 : chestplate
				.equals(Material.CHAINMAIL_CHESTPLATE) ? 5 : chestplate
				.equals(Material.IRON_CHESTPLATE) ? 6 : chestplate
				.equals(Material.DIAMOND_CHESTPLATE) ? 8 : 0;
		// Leggings
		defensePoints += leggings == null ? 0 : leggings
				.equals(Material.LEATHER_LEGGINGS) ? 2 : leggings
				.equals(Material.GOLD_LEGGINGS) ? 3 : leggings
				.equals(Material.CHAINMAIL_LEGGINGS) ? 4 : leggings
				.equals(Material.IRON_LEGGINGS) ? 5 : leggings
				.equals(Material.DIAMOND_LEGGINGS) ? 6 : 0;
		// Boots
		defensePoints += boots == null ? 0 : boots
				.equals(Material.LEATHER_BOOTS) ? 1 : boots
				.equals(Material.GOLD_BOOTS) ? 1 : boots
				.equals(Material.CHAINMAIL_BOOTS) ? 1 : boots
				.equals(Material.IRON_BOOTS) ? 2 : boots
				.equals(Material.DIAMOND_BOOTS) ? 3 : 0;

		float defensePercent = ((float) defensePoints * 4 / 100);

		double adjustDamage = (int) Math.floor((float) newDamage
				* (1 - defensePercent));

		return adjustDamage;
	}

	public static List<String> UUIDArrayToUsernameArray(List<UUID> list) {
		List<String> result = new ArrayList<String>();
		for(UUID uuid : list)
			result.add(Bukkit.getOfflinePlayer(uuid).getName());
		return result;
	}

	public static List<OfflinePlayer> UUIDArrayToOfflinePlayerArray(List<UUID> list) {
		List<OfflinePlayer> result = new ArrayList<OfflinePlayer>();
		for(UUID uuid : list)
			result.add(Bukkit.getOfflinePlayer(uuid));
		return result;
	}
	
	public static List<Player> UUIDArrayToOnlinePlayerArray(List<UUID> list) {
		List<Player> result = new ArrayList<Player>();
		for(OfflinePlayer p : UUIDArrayToOfflinePlayerArray(list))
			if(p.isOnline())
				result.add(p.getPlayer());
		return result;
	}
	
	public static <T> Iterable<T> nullGuard(Iterable<T> list) {
		return list == null ? Collections.<T> emptyList() : list;
	}

	public static String listToString(List<String> list) {
		return StringUtils.join(list, ", ");
	}

	public static List<Player> getPlayersNear(Player player, int radius) {
		List<Player> result = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers())
			if (isWithin(p.getLocation(), player.getLocation(), radius))
				result.add(p);
		return result;
	}

	public static String getDisplayName(OfflinePlayer player) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		return pPlayer.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED
				+ player.getName()
				: pPlayer.isCriminal() ? ChatColor.GRAY + player.getName()
						: ChatColor.BLUE + player.getName();
	}

	public static String getStringFromList(String[] args) {
		return StringUtils.join(args, ", ");
	}

	public static void addPotion(Player player, int amplifier, int duration,
			PotionEffectType type, boolean force) {
		if (player.hasPotionEffect(type))
			player.removePotionEffect(type);
		player.addPotionEffect(new PotionEffect(type, duration, amplifier),
				force);
	}

	public static void addPotion(Player player, int amplifier, int duration,
			PotionEffectType type, boolean force, int increase) {
		for (PotionEffect pe : player.getActivePotionEffects())
			if (pe.getType().equals(type))
				duration += pe.getDuration();
		duration = Math.min(duration, increase);
		if (player.hasPotionEffect(type))
			player.removePotionEffect(type);
		player.addPotionEffect(new PotionEffect(type, duration, amplifier),
				force);
	}
 }
