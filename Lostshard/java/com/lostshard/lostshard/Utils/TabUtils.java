package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Objects.Plot.Plot;

public class TabUtils {

	public static List<String> empty() {
		return new ArrayList<String>();
	}
		
	public static List<String> OnlinePlayersTab(String[] args) {
		List<String> completions = new ArrayList<String>();
		for (Player option : Bukkit.getOnlinePlayers()) {
			if (StringUtil.startsWithIgnoreCase(option.getName(), args[args.length-1]))
				completions.add(option.getName());
		}
		return completions;
	}

	public static List<String> OnlinePlayersTab(String[] args, Player[] exclude) {
		List<String> completions = new ArrayList<String>();
		List<Player> excludes = new ArrayList<Player>();
		for (Player p : exclude)
			excludes.add(p);
		for (Player option : Bukkit.getOnlinePlayers()) {
			if(excludes.contains(option))
				continue;
			if (StringUtil.startsWithIgnoreCase(option.getName(), args[args.length-1]))
				completions.add(option.getName());
		}
		return completions;
	}

	public static List<String> PlotFriend(Player player, String[] args) {
		Plot plot = ptm.findPlotAt(player.getLocation());
		List<String> completions = new ArrayList<String>();
		List<UUID> excludes = new ArrayList<UUID>();
		excludes.addAll(plot.getFriends());
		excludes.addAll(plot.getCoowners());
		excludes.add(plot.getOwner());
		for (Player option : Bukkit.getOnlinePlayers()) {
			if(excludes.contains(option))
				continue;
			if (StringUtil.startsWithIgnoreCase(option.getName(), args[args.length-1]))
				completions.add(option.getName());
		}
		return completions;
	}

	public static List<String> PlotUnfriend(Player player, String[] args) {
		Plot plot = ptm.findPlotAt(player.getLocation());
		List<String> completions = new ArrayList<String>();
		for (Player option : Bukkit.getOnlinePlayers()) {
			if(plot.isFriendOrAbove(option))
				completions.add(option.getName());
		}
		return completions;
	}

	public static List<String> StringTab(String[] args, String[] options) {
		List<String> completions = new ArrayList<String>();
		for (String option : options) {
			if (StringUtil.startsWithIgnoreCase(option, args[args.length-1]))
				completions.add(option);
		}
		return completions;
	}
	
	static PlotManager ptm = PlotManager.getManager();

}
