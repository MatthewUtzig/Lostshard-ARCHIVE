package com.lostshard.Lostshard.Data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Manager.ConfigManager;

public class Variables {

	static ConfigManager cm = ConfigManager.getManager();

	// Plot
	public static final ItemStack plotCreateItemPrice = new ItemStack(Material.DIAMOND, 1);
	public static final int plotCreatePrice = 1000;
	public static final int plotTownPrice = 100000;
	public static final int plotDungeonPrice = 20000;
	public static final int plotAutoKickPrice = 10000;
	public static final int plotNeutralAlignmentPrice = 10000;
	public static final int plotExpandPrice = 10;
	public static final int plotMaxNameLength = 20;
	public static final int plotRenamePrice = 1000;
	public static final int plotStartingSize = 10;
	// Vote
	public static final int voteMoney = 100;
	// Bank
	public static final int bankRadius = 5;
	public static final int goldIngotValue = 100;
	// Database
	public static String mysqlUrl;
	public static String mysqlUsername;
	public static String mysqlPassword;
	// Server
	public static String motd = "MOTD, forgot to insert!";
	public static final int maxPlayers = 30;
	// Karma
	public static final int murderPoint = 5;
	// NPC
	public static final int guardRange = 15;

	public static final int clanMaxNameLeangh = 20;
	public static final int clanCreateCost = 2000;

	public static final int claimTime = 90;

	public static final int playerStartSkillPoints = 2000;
	public static final ItemStack[] playerStartBank = new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 32),
			new ItemStack(Material.DIAMOND, 3), new ItemStack(Material.MELON, 10) };
}
