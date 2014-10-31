package com.lostshard.lostshard.Data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Variables {

	//Plot
	public static final ItemStack plotItemPrice = new ItemStack(Material.DIAMOND, 1);
	public static final int plotCreatePrice = 1000;
	public static final int plotTownPrice = 100000;
	public static final int plotDungeonPrice = 20000;
	public static final int plotAutoKickPrice = 10000;
	public static final int plotNeutralAlignmentPrice = 10000;
	public static final int plotExpandPrice = 10;
	//Vote
	public static final int voteMoney = 1000;
	//Bank
	public static final int bankRadius = 10;
	public static final int goldIngotValue = 100;
	//Database
	public static final String mysqlDriver = "com.mysql.jdbc.Driver";
	public static final String mysqlHost = "jdbc:mysql://";
	public static final String mysqlUsername = "username";
	public static final String mysqlPassword = "password";
	//Server
	public static final String motd = "MOTD, forgot to insert!";
	public static final int maxPlayers = 30;
	//Map
	public static final Location criminalSpawn = new Location(null, 0, 0, 0, 0, 0);
	public static final Location lawfullSpawn = new Location(null, 0, 0, 0, 0, 0);
	//Karma
	public static final int murderPoint = 5;
	//NPC
	public static final int guardRange = 15;

}
