package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class Utils {

	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(Player player, String[] args,
			int argsnr) {
		if (args.length < argsnr + 1)
			return null;
		String targetName = args[argsnr];
		return Bukkit.getOfflinePlayer(targetName);
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

	public static int adjustDamageForArmor(Player player, int damage) {
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

		int adjustDamage = (int) Math.floor((float) damage
				* (1 - defensePercent));

		return adjustDamage;
	}

	public static int getNumberFromString(String string) {
		return 0;
	}

	public static List<String> UUIDArrayToUsernameArray(List<UUID> list) {
		List<String> result = new ArrayList<String>();
		// for(UUID uuid : list)
		// result.add(Bukkit.getOfflinePlayer(uuid));
		return result;
	}

	public static <T> Iterable<T> nullGuard(Iterable<T> list) {
		return list == null ? Collections.<T> emptyList() : list;
	}

	public static String listToString(List<String> list) {
		String result = "";
		for (String s : list)
			result += list.get(list.size() - 1) == s ? s : s + ", ";
		return result;
	}

	public static List<Player> getPlayersNear(Player player, int radius) {
		List<Player> result = new ArrayList<Player>();
		for (Player p : Bukkit.getOnlinePlayers())
			if (isWithin(p.getLocation(), player.getLocation(), radius))
				result.add(p);
		return result;
	}

	public static String getColoredName(Player player) {
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		return pPlayer.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED
				+ player.getName()
				: pPlayer.isCriminal() ? ChatColor.GRAY + player.getName()
						: ChatColor.BLUE + player.getName();
	}

	public static String getUsernamesFromUUIDs(List<UUID> uuids) {
		String result = "";
		for (UUID uuid : uuids)
			if (uuid == uuids.get(uuids.size() - 1))
				result += Bukkit.getOfflinePlayer(uuid);
			else
				result += Bukkit.getOfflinePlayer(uuid) + ",";
		return result;
	}

	public static String getStringFromList(String[] args) {
		String rs = "";
		for (String s : args) {
			rs += s + " ";
		}
		return rs;
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
