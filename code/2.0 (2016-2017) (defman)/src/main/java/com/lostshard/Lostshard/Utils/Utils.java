package com.lostshard.Lostshard.Utils;

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

import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {

	static PlayerManager pm = PlayerManager.getManager();

	public static void addPotion(Player player, int amplifier, int duration, PotionEffectType type, boolean force) {
		if (player.hasPotionEffect(type))
			player.removePotionEffect(type);
		player.addPotionEffect(new PotionEffect(type, duration, amplifier), force);
	}

	public static void addPotion(Player player, int amplifier, int duration, PotionEffectType type, boolean force,
			int increase) {
		for (final PotionEffect pe : player.getActivePotionEffects())
			if (pe.getType().equals(type))
				duration += pe.getDuration();
		duration = Math.min(duration, increase);
		if (player.hasPotionEffect(type))
			player.removePotionEffect(type);
		player.addPotionEffect(new PotionEffect(type, duration, amplifier), force);
	}

	public static String booleanToString(boolean bol) {
		return booleanToString(bol, "Yes", "No");
	}

	public static String booleanToString(boolean bol, String ifTrue, String ifFalse) {
		return bol ? ifTrue : ifFalse;
	}

	public static ChatColor getDisplayColor(OfflinePlayer player) {
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		return pPlayer.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED
				: pPlayer.isCriminal() ? ChatColor.GRAY : ChatColor.BLUE;
	}

	public static String getDisplayName(OfflinePlayer player) {
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		return pPlayer.getMurderCounts() >= Variables.murderPoint ? ChatColor.RED + player.getName()
				: pPlayer.isCriminal() ? ChatColor.GRAY + player.getName() : ChatColor.BLUE + player.getName();
	}

	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(Player player, String[] args, int argsnr) {
		if (args.length < argsnr + 1)
			return null;
		final String targetName = args[argsnr];
		return Bukkit.getOfflinePlayer(targetName);
	}

	public static Player getPlayer(Player player, String[] args, int argsnr) {
		if (args.length < argsnr + 1)
			return null;
		final String targetName = args[argsnr];
		return Bukkit.getPlayer(targetName);
	}

	public static Player getPlayer(String name) {
		final Player player = Bukkit.getPlayer(name);
		if (Lostshard.isVanished(player))
			return null;
		return player;
	}

	public static List<Player> getPlayersNear(Player player, int radius) {
		final List<Player> result = new ArrayList<Player>();
		for (final Player p : Bukkit.getOnlinePlayers())
			if (isWithin(p.getLocation(), player.getLocation(), radius))
				result.add(p);
		return result;
	}

	public static String getStringFromList(String[] args) {
		return StringUtils.join(args, ", ");
	}

	public static boolean isWithin(Location loc1, Location loc2, double radius) {
		return loc1.getWorld().equals(loc2.getWorld()) && loc1.distance(loc2) < Math.pow(radius, 2);
	}

	public static String listToString(List<String> list) {
		return StringUtils.join(list, ", ");
	}

	public static <T> Iterable<T> nullGuard(Iterable<T> list) {
		return list == null ? Collections.<T> emptyList() : list;
	}

	public static String scaledIntToString(int x) {
		return new BigDecimal(BigInteger.valueOf(x), 1).toString();
	}

	public static void sendSmartTextCommand(Player player, String text, String hoverText, String command) {
		final TextComponent smartText = new TextComponent(text);
		smartText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		smartText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
		player.spigot().sendMessage(smartText);
	}

	public static List<OfflinePlayer> UUIDArrayToOfflinePlayerArray(List<UUID> list) {
		final List<OfflinePlayer> result = new ArrayList<OfflinePlayer>();
		for (final UUID uuid : list)
			result.add(Bukkit.getOfflinePlayer(uuid));
		return result;
	}

	public static List<Player> UUIDArrayToOnlinePlayerArray(List<UUID> list) {
		final List<Player> result = new ArrayList<Player>();
		for (final OfflinePlayer p : UUIDArrayToOfflinePlayerArray(list))
			if (p.isOnline())
				result.add(p.getPlayer());
		return result;
	}

	public static List<String> UUIDArrayToUsernameArray(List<UUID> list) {
		final List<String> result = new ArrayList<String>();
		for (final UUID uuid : list)
			result.add(Bukkit.getOfflinePlayer(uuid).getName());
		return result;
	}
}
