package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.lostshard.lostshard.main.Lostshard;
import com.lostshard.lostshard.plot.Plot;

public class TabUtils {

	static PlotManager ptm = PlotManager.getManager();

	public static List<String> empty() {
		return new ArrayList<String>();
	}
	
	public static List<String> OnlinePlayers(String arg) {
		final List<String> completions = new LinkedList<String>();
		for (final Player option : Bukkit.getOnlinePlayers())
			if (Lostshard.isVanished(option))
				continue;
			else if (StringUtil.startsWithIgnoreCase(option.getName(), arg))
				completions.add(option.getName());
		return completions;
	}

	public static List<String> OnlinePlayersTab(String[] args) {
		final List<String> completions = new ArrayList<String>();
		for (final Player option : Bukkit.getOnlinePlayers())
			if (Lostshard.isVanished(option))
				continue;
			else if (StringUtil.startsWithIgnoreCase(option.getName(), args[args.length - 1]))
				completions.add(option.getName());
		return completions;
	}

	public static List<String> OnlinePlayersTab(String[] args, Player... exclude) {
		final List<String> completions = new ArrayList<String>();
		final List<Player> excludes = Arrays.asList(exclude);
		for (final Player option : Bukkit.getOnlinePlayers()) {
			if (Lostshard.isVanished(option))
				continue;
			if (excludes.contains(option))
				continue;
			if (StringUtil.startsWithIgnoreCase(option.getName(), args[args.length - 1]))
				completions.add(option.getName());
		}
		return completions;
	}

	public static List<String> PlotFriend(Player player, String[] args) {
		final Plot plot = ptm.findPlotAt(player.getLocation());
		final List<String> completions = new ArrayList<String>();
		final List<UUID> excludes = new ArrayList<UUID>();
		excludes.addAll(plot.getFriends());
		excludes.addAll(plot.getCoowners());
		excludes.add(plot.getOwner());
		for (final Player option : Bukkit.getOnlinePlayers()) {
			if (excludes.contains(option))
				continue;
			if (StringUtil.startsWithIgnoreCase(option.getName(), args[args.length - 1]))
				completions.add(option.getName());
		}
		return completions;
	}

	public static List<String> PlotUnfriend(Player player, String[] args) {
		final Plot plot = ptm.findPlotAt(player.getLocation());
		final List<String> completions = new ArrayList<String>();
		for (final Player option : Bukkit.getOnlinePlayers())
			if (plot.isFriendOrAbove(option))
				completions.add(option.getName());
		return completions;
	}

	public static List<String> StringTab(String[] args, String... options) {
		final List<String> completions = new ArrayList<String>();
		for (final String option : options)
			if (StringUtil.startsWithIgnoreCase(option, args[args.length - 1]))
				completions.add(option);
		return completions;
	}

	public static List<String> Plots() {
		Set<Plot> plots = ptm.getPlots();
		List<String> results = new ArrayList<String>(plots.size());
		for(Plot p : plots) {
			results.add(p.getName());
		}
		return results;
	}

}
