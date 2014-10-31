package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class TabUtils {
	
	public static List<String> StringTab(String[] args, String[] options) {
		List<String> completions = new ArrayList<String>();
		for(String option : options) {
			if(StringUtil.startsWithIgnoreCase(option, args[0]))
				completions.add(option);
		}
		return completions;
	}
	
	public static List<String> OnlinePlayersTab(String[] args) {
		List<String> completions = new ArrayList<String>();
		for(Player option : Bukkit.getOnlinePlayers()) {
			if(StringUtil.startsWithIgnoreCase(option.getName(), args[0]))
				completions.add(option.getName());
		}
		return completions;
	}
	
	public static List<String> OnlinePlayersTab(String[] args, Player[] exclude) {
		List<String> completions = new ArrayList<String>();
		for(Player option : Bukkit.getOnlinePlayers()) {
			if(StringUtil.startsWithIgnoreCase(option.getName(), args[0]))
				completions.add(option.getName());
		}
		return completions;
	}
	
	
	
}
